package com.example.teroka;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddModifyTrip extends AppCompatActivity {
    RadioGroup radiogroup;
    RadioButton radioButton;
    ImageView imageView;
    EditText edit_text;


    Calendar calendar;
    DBHelper mydb;

    Boolean isModify = false;
    String trip_id;
    TextView toolbar_title;
    TextView dateText;
    Button save_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_add_modify_trip);

        mydb = new DBHelper(getApplicationContext());
        calendar = new GregorianCalendar();
        toolbar_title = findViewById(R.id.toolbar_title);
        edit_text = findViewById(R.id.edit_text);
        dateText = findViewById(R.id.dateText);
        save_btn = findViewById(R.id.save_btn);
        dateText.setText(new SimpleDateFormat("E, dd MMMM yyyy").format(calendar.getTime()));

        imageView = findViewById(R.id.imageView30);
        radiogroup = findViewById(R.id.groupradio1);


        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.hinbusdepot1:
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.hin_bus_depot));
                        break;
                    case R.id.avatarsecretgarden1:
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.avatar));
                        break;
                    case R.id.kampongagong1:
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.kampong_agong));
                        break;
                    case R.id.penangatv1:
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.atv));
                        break;
                }
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra("isModify")) {
            isModify = intent.getBooleanExtra("isModify", false);
            trip_id = intent.getStringExtra("id");
            init_modify();
        }


    }

    public void init_modify() {
        toolbar_title.setText("Modify Trip");
        save_btn.setText("Update");
        LinearLayout deleteTrip = findViewById(R.id.deleteTrip);
        deleteTrip.setVisibility(View.VISIBLE);
        Cursor trip = mydb.getSingleTrip(trip_id);
        if (trip != null) {
            trip.moveToFirst();


            edit_text.setText(trip.getString(1));

            SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                calendar.setTime(iso8601Format.parse(trip.getString(2)));
            } catch (ParseException e) {
            }

            dateText.setText(new SimpleDateFormat("E, dd MMMM yyyy").format(calendar.getTime()));


        }

    }


    public void saveTrip(View v) {


        /*Checking for Empty Trip*/
        if (edit_text.getText().toString().trim().length() > 0) {

            if (isModify) {
                mydb.updateTrip(trip_id, edit_text.getText().toString(), new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
                Toast.makeText(getApplicationContext(), "Trip Updated.", Toast.LENGTH_SHORT).show();
            } else {
                mydb.insertTrip(edit_text.getText().toString(), new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
                Toast.makeText(getApplicationContext(), "Trip Added.", Toast.LENGTH_SHORT).show();
            }
            finish();

        } else {
            Toast.makeText(getApplicationContext(), "Empty trip can't be saved.", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteTrip(View v) {
        mydb.deleteTrip(trip_id);
        Toast.makeText(getApplicationContext(), "Trip Removed", Toast.LENGTH_SHORT).show();
        finish();
    }


    public void chooseDate(View view) {
        final View dialogView = View.inflate(this, R.layout.date_picker, null);
        final DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Choose Date");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


                calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                dateText.setText(new SimpleDateFormat("E, dd MMMM yyyy").format(calendar.getTime()));

            }
        });
        builder.show();
    }

}

package com.example.teroka;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class
ListTripAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private DBHelper database;

    public ListTripAdapter(Activity a, ArrayList<HashMap<String, String>> d, DBHelper mydb) {
        activity = a;
        data = d;
        database = mydb;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ListTripViewHolder holder = null;
        if (convertView == null) {
            holder = new ListTripViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.trip_list_row, parent, false);
            holder.trip_name = convertView.findViewById(R.id.trip_name);
            holder.checkBtn = convertView.findViewById(R.id.checkBtn);
            convertView.setTag(holder);
        } else {
            holder = (ListTripViewHolder) convertView.getTag();
        }


        final HashMap<String, String> singleTrip = data.get(position);
        final ListTripViewHolder tmpHolder = holder;

        holder.trip_name.setId(position);
        holder.checkBtn.setId(position);

        try {


            holder.checkBtn.setOnCheckedChangeListener(null);
            if (singleTrip.get("status").contentEquals("1")) {
                holder.trip_name.setText(Html.fromHtml("<strike>" + singleTrip.get("trip") + "</strike>"));
                holder.checkBtn.setChecked(true);
            } else {
                holder.trip_name.setText(singleTrip.get("trip"));
                holder.checkBtn.setChecked(false);
            }

            holder.checkBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        database.updateTripStatus(singleTrip.get("id"), 1);
                        tmpHolder.trip_name.setText(Html.fromHtml("<strike>" + singleTrip.get("trip") + "</strike>"));
                    } else {
                        database.updateTripStatus(singleTrip.get("id"), 0);
                        tmpHolder.trip_name.setText(singleTrip.get("trip"));
                    }

                }
            });


        } catch (Exception e) {
        }
        return convertView;
    }
}

class ListTripViewHolder {
    TextView trip_name;
    CheckBox checkBtn;
}
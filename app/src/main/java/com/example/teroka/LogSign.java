package com.example.teroka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.teroka.ui.login.LoginActivity;

public class LogSign extends AppCompatActivity {

    TextView create_acc = (TextView)findViewById(R.id.create_acc);
    private Button login_button = (Button)findViewById(R.id.login_button);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_sign);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });

        create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });

    }

    public void openLoginActivity() {
        Intent intent = new Intent(LogSign.this, LoginActivity.class);
        LogSign.this.startActivity(intent);
    }
}
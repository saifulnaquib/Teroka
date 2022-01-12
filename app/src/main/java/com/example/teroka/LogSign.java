package com.example.teroka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teroka.ui.login.LoginActivity;

public class LogSign extends AppCompatActivity implements View.OnClickListener {

    TextView create_acc;
    private Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_sign);

        create_acc = (TextView)findViewById(R.id.create_acc);
        login_button = (Button)findViewById(R.id.login_button);

        //Set button alpha to zero
        login_button.setAlpha(0f);
        login_button.setTranslationY(50);

        //Animate the alpha value to 1f and set duration as 1.5 secs.
        login_button.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        login_button.setOnClickListener(this);
        create_acc.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button:
                openLoginActivity();
                break;
            case R.id.create_acc:
                openRegisterActivity();
                break;
        }
    }

    public void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openRegisterActivity() {
        Intent intent_register = new Intent(this, RegisterActivity.class);
        startActivity(intent_register);
    }
}
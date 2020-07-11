package com.paradisetechnologies.brithwine.startupActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.paradisetechnologies.brithwine.R;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {

    private TextView tvSignUp, tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindViews();

    }

    private void bindViews()
    {
        tvSignUp = findViewById(R.id.tvSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        tvSignUp.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.tvSignUp:
                callSignupActivity();
                break;

            case R.id.tvForgotPassword:
                callForgotPassActivity();
                break;

            default:
                break;
        }
    }

    private void callSignupActivity()
    {
        Intent intent = new Intent(ActivityLogin.this, ActivitySignup.class);
        startActivity(intent);
    }

    private void callForgotPassActivity()
    {
        Intent intent = new Intent(ActivityLogin.this, ActivityForgotPassword.class);
        startActivity(intent);
    }
}
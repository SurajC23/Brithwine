package com.paradisetechnologies.brithwine.startupActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paradisetechnologies.brithwine.R;
import com.paradisetechnologies.brithwine.activity.ActivityHome;
import com.paradisetechnologies.brithwine.constants.AppConstants;
import com.paradisetechnologies.brithwine.entity.BaseResponseObjectEntity;
import com.paradisetechnologies.brithwine.entity.LoginEntity;
import com.paradisetechnologies.brithwine.network.APIRequestService;
import com.paradisetechnologies.brithwine.network.RetrofitClient;
import com.paradisetechnologies.brithwine.utils.StatMethods;
import com.paradisetechnologies.brithwine.utils.UtilitySharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {

    private TextView tvSignUp, tvForgotPassword, tvLogin, tvEmailVerify;
    private EditText etEmailID, etPassword;
    private ImageView ivCancel;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private RelativeLayout rlVerify;
    private String token, name, emailID, classId;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindViews();

    }

    private void bindViews()
    {
        etEmailID = findViewById(R.id.etEmailID);
        etPassword = findViewById(R.id.etPassword);
        ivCancel = findViewById(R.id.ivCancel);
        rlVerify = findViewById(R.id.rlVerify);

        tvSignUp = findViewById(R.id.tvSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvLogin = findViewById(R.id.tvLogin);
        tvEmailVerify = findViewById(R.id.tvEmailVerify);

        tvSignUp.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        tvLogin.setOnClickListener(this);

        etEmailID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable emailID)
            {
                if (emailID.toString().trim().equals(""))
                {
                    rlVerify.setVisibility(View.GONE);
                    return;
                }

                rlVerify.setVisibility(View.VISIBLE);

                if (emailID.toString().trim().matches(emailPattern))
                {
                    tvEmailVerify.setText(R.string.valid_email_id);
                    tvEmailVerify.setTextColor(Color.parseColor("#000000"));
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_check_mark);
                    ivCancel.setImageDrawable(drawable);
                }
                else
                {
                    tvEmailVerify.setText(R.string.please_enter_valid_email_id);
                    tvEmailVerify.setTextColor(Color.parseColor("#ff1e1e"));
                    Drawable drawable = getResources().getDrawable(R.drawable.clear);
                    ivCancel.setImageDrawable(drawable);
                }
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.tvSignUp:
                //StatMethods.startNewActivity(ActivityLogin.this, ActivitySignup.class);
                Intent signupIntent = new Intent(ActivityLogin.this, ActivitySignup.class);
                startActivity(signupIntent);
                break;

            case R.id.tvForgotPassword:
                //StatMethods.startNewActivity(ActivityLogin.this, ActivityForgotPassword.class);
                Intent forgotPasswordIntent = new Intent(ActivityLogin.this, ActivityForgotPassword.class);
                startActivity(forgotPasswordIntent);
                break;

            case R.id.tvLogin:
                validate();
                break;

            default:
                break;
        }
    }

    private void validate()
    {
        String email = etEmailID.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.equals(""))
        {
            StatMethods.showToastShort(this, getString(R.string.empty_emailid));
            return;
        }
        else if (!email.matches(emailPattern))
        {
            StatMethods.showToastShort(this, getString(R.string.please_enter_valid_email_id));
            return;
        }
        else if (password.equals(""))
        {
            StatMethods.showToastShort(this, getString(R.string.email_id));
            return;
        }
        sendLoginRequest(email, password);
    }

    private void sendLoginRequest(String email, String password)
    {
        StatMethods.showDialog(this);
        final APIRequestService apiRequestService = RetrofitClient.getApiService();
        Call<BaseResponseObjectEntity<LoginEntity>> call = apiRequestService.sendLoginRequest(email, password);
        call.enqueue(new Callback<BaseResponseObjectEntity<LoginEntity>>() {
            @Override
            public void onResponse(Call<BaseResponseObjectEntity<LoginEntity>> call, Response<BaseResponseObjectEntity<LoginEntity>> response)
            {
                if (response != null)
                {
                    BaseResponseObjectEntity entity = response.body();
                    if (entity != null)
                    {
                        String status = entity.getStatus();
                        if (status.equals(AppConstants.SUCCESS))
                        {
                            int msg_code = entity.getMsg_code();

                            LoginEntity loginEntity = (LoginEntity) entity.getData();
                            userId = loginEntity.getId();
                            token = "Bearer " + loginEntity.getApi_token();
                            name = loginEntity.getName();
                            emailID = loginEntity.getEmail();
                            classId = loginEntity.getClass_id();

                            if (msg_code == 4)
                            {
                                StatMethods.dismissDialog();
                                startNextActivity(userId, token, name, emailID, classId);
                            }
                            else
                            {
                                UtilitySharedPreferences.setPrefs(ActivityLogin.this, AppConstants.SHAREDPREFERENCES.USER_ID, ""+userId);
                                UtilitySharedPreferences.setPrefs(ActivityLogin.this, AppConstants.SHAREDPREFERENCES.USER_AUTH_TOKEN, token);
                                UtilitySharedPreferences.setPrefs(ActivityLogin.this, AppConstants.SHAREDPREFERENCES.USER_NAME, name);
                                UtilitySharedPreferences.setPrefs(ActivityLogin.this, AppConstants.SHAREDPREFERENCES.USER_EMAIL, email);
                                UtilitySharedPreferences.setPrefs(ActivityLogin.this, AppConstants.SHAREDPREFERENCES.USER_CLASSID, classId);

                                StatMethods.dismissDialog();
                                StatMethods.startNewActivity(ActivityLogin.this, ActivityHome.class);
                            }
                        }
                        else if (status.equals(AppConstants.ERROR))
                        {
                            StatMethods.dismissDialog();
                            int msgCode = entity.getMsg_code();
                            StatMethods.showMsgCode(ActivityLogin.this, msgCode);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseObjectEntity<LoginEntity>> call, Throwable t)
            {
            }
        });
    }

    private void startNextActivity(int userId, String token, String name, String email, String classId)
    {
        Intent intent = new Intent(ActivityLogin.this, ActivityEmailOtpConfirmaion.class);
        intent.putExtra(AppConstants.SHAREDPREFERENCES.USER_ID, String.valueOf(userId));
        intent.putExtra(AppConstants.SHAREDPREFERENCES.USER_AUTH_TOKEN, token);
        intent.putExtra(AppConstants.SHAREDPREFERENCES.USER_NAME, name);
        intent.putExtra(AppConstants.SHAREDPREFERENCES.USER_EMAIL, email);
        intent.putExtra(AppConstants.SHAREDPREFERENCES.USER_CLASSID, classId);
        startActivity(intent);
        finish();
    }
}
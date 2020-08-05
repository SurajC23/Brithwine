package com.paradisetechnologies.brithwine.startupActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paradisetechnologies.brithwine.R;
import com.paradisetechnologies.brithwine.activity.ActivityHome;
import com.paradisetechnologies.brithwine.constants.AppConstants;
import com.paradisetechnologies.brithwine.entity.BaseResponseObjectEntity;
import com.paradisetechnologies.brithwine.network.APIRequestService;
import com.paradisetechnologies.brithwine.network.RetrofitClient;
import com.paradisetechnologies.brithwine.utils.StatMethods;
import com.paradisetechnologies.brithwine.utils.UtilitySharedPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityEmailOtpConfirmaion extends AppCompatActivity implements View.OnClickListener {
    private int size = 1;
    private TextView tvProceed;
    private ImageView ivBackArrow;
    private EditText et_one, et_two, et_three, et_four, et_five, et_six;
    private String otp = "";
    private String name, token, email, classID, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_otp_confirmaion);

        userID = getIntent().getStringExtra(AppConstants.SHAREDPREFERENCES.USER_ID);
        name = getIntent().getStringExtra(AppConstants.SHAREDPREFERENCES.USER_NAME);
        token = getIntent().getStringExtra(AppConstants.SHAREDPREFERENCES.USER_AUTH_TOKEN);
        email = getIntent().getStringExtra(AppConstants.SHAREDPREFERENCES.USER_EMAIL);
        classID = getIntent().getStringExtra(AppConstants.SHAREDPREFERENCES.USER_CLASSID);

        Log.e("TAG", "onCreate: USER ID : " + userID);

        bindViews();
    }

    private void bindViews()
    {

        et_one = findViewById(R.id.et_one);
        et_two = findViewById(R.id.et_two);
        et_three = findViewById(R.id.et_three);
        et_four = findViewById(R.id.et_four);
        et_five = findViewById(R.id.et_five);
        et_six = findViewById(R.id.et_six);

        tvProceed = findViewById(R.id.tvProceed);
        ivBackArrow = findViewById(R.id.ivBackArrow);

        et_one.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_one.getText().toString().length() == size)     //size as per your requirement
                {
                    et_two.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {

            }

        });
        et_two.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_two.getText().toString().length() == size)     //size as per your requirement
                {
                    et_three.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {
            }

        });
        et_three.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_three.getText().toString().length() == size)     //size as per your requirement
                {
                    et_four.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {
            }

        });
        et_four.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_four.getText().toString().length() == size)     //size as per your requirement
                {
                    et_five.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {
            }

        });
        et_five.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_five.getText().toString().length() == size)     //size as per your requirement
                {
                    et_six.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {
            }

        });
        et_six.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_six.getText().toString().length() == size)     //size as per your requirement
                {

                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {
            }

        });

        tvProceed.setOnClickListener(this);
        ivBackArrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ivBackArrow:
                StatMethods.startNewActivity(this, ActivityLogin.class);
                finish();
                break;

            case R.id.tvProceed:
                if (checkValidation())
                    validateOTPRequest();
                break;

        }
    }

    private boolean checkValidation()
    {
        String one = et_one.getText().toString().trim();
        String two = et_two.getText().toString().trim();
        String three = et_three.getText().toString().trim();
        String four = et_four.getText().toString().trim();
        String five = et_five.getText().toString().trim();
        String six = et_six.getText().toString().trim();
        if (one.equals("") || two.equals("") || three.equals("") || four.equals("") || five.equals("") || six.equals("")) {
            Toast.makeText(this, getString(R.string.error_enter_otp), Toast.LENGTH_SHORT).show();
            return false;
        }
        otp = one + two + three + four + five + six;
        return true;
    }

    private void validateOTPRequest()
    {
        StatMethods.showDialog(this);
        final APIRequestService apiRequestService = RetrofitClient.getApiService();
        Call<BaseResponseObjectEntity> call = apiRequestService.verifyOtp(email, otp);
        call.enqueue(new Callback<BaseResponseObjectEntity>()
        {
            @Override
            public void onResponse(Call<BaseResponseObjectEntity> call, Response<BaseResponseObjectEntity> response)
            {
                if (response != null)
                {
                    BaseResponseObjectEntity entity = response.body();

                    if (entity != null)
                    {
                        String status = entity.getStatus();
                        if (status.equals(AppConstants.SUCCESS))
                        {
                            UtilitySharedPreferences.setPrefs(ActivityEmailOtpConfirmaion.this, AppConstants.SHAREDPREFERENCES.USER_ID, ""+userID);
                            UtilitySharedPreferences.setPrefs(ActivityEmailOtpConfirmaion.this, AppConstants.SHAREDPREFERENCES.USER_AUTH_TOKEN, token);
                            UtilitySharedPreferences.setPrefs(ActivityEmailOtpConfirmaion.this, AppConstants.SHAREDPREFERENCES.USER_NAME, name);
                            UtilitySharedPreferences.setPrefs(ActivityEmailOtpConfirmaion.this, AppConstants.SHAREDPREFERENCES.USER_EMAIL, email);
                            UtilitySharedPreferences.setPrefs(ActivityEmailOtpConfirmaion.this, AppConstants.SHAREDPREFERENCES.USER_CLASSID, classID);

                            StatMethods.dismissDialog();
                            StatMethods.startNewActivity(ActivityEmailOtpConfirmaion.this, ActivityHome.class);
                        }
                        else if (status.equals(AppConstants.ERROR))
                        {
                            StatMethods.dismissDialog();
                            StatMethods.showToastShort(ActivityEmailOtpConfirmaion.this, getString(R.string.invalid_otp));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseObjectEntity> call, Throwable t)
            {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        StatMethods.startNewActivity(this, ActivityLogin.class);
        finish();
    }
}
package com.paradisetechnologies.brithwine.startupActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.paradisetechnologies.brithwine.GlobalApplication;
import com.paradisetechnologies.brithwine.R;
import com.paradisetechnologies.brithwine.activity.ActivityHome;
import com.paradisetechnologies.brithwine.constants.AppConstants;
import com.paradisetechnologies.brithwine.entity.BaseResponseArrayEntity;
import com.paradisetechnologies.brithwine.entity.BaseResponseObjectEntity;
import com.paradisetechnologies.brithwine.entity.ClassEntity;
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

public class ActivitySignup extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBackArrow;
    private EditText etName, etPhoneno, etEmailID, etPassword;
    private Spinner spClass;
    private TextView tvSignUp;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ArrayAdapter<ClassEntity> classAdapter;
    private String selectedClassId;
    private ClassEntity classEntity;
    private ArrayList<ClassEntity> classEntityArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getClassList();
        bindViews();
    }

    private void getClassList()
    {
        final APIRequestService apiRequestService = RetrofitClient.getApiService();
        Call<BaseResponseArrayEntity<ClassEntity>> call = apiRequestService.getClassList("");
        call.enqueue(new Callback<BaseResponseArrayEntity<ClassEntity>>() {
            @Override
            public void onResponse(Call<BaseResponseArrayEntity<ClassEntity>> call, Response<BaseResponseArrayEntity<ClassEntity>> response)
            {
                if (response != null)
                {
                    BaseResponseArrayEntity<ClassEntity> entity = response.body();
                    if (entity != null)
                    {
                        String status = entity.getStatus();
                        if (status.equals(AppConstants.SUCCESS))
                        {
                            classEntityArrayList = entity.getData();
                            if (classEntityArrayList != null && classEntityArrayList.size() > 0)
                            {
                                classAdapter = new ArrayAdapter(ActivitySignup.this, R.layout.spinner_item, classEntityArrayList);
                                classAdapter.setDropDownViewResource(R.layout.spinner_item);
                                spClass.setAdapter(classAdapter);
                            }
                        }
                        else if (status.equals(AppConstants.ERROR))
                        {
                            int msgCode = entity.getMsg_code();
                            StatMethods.showMsgCode(ActivitySignup.this, msgCode);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseArrayEntity<ClassEntity>> call, Throwable t)
            {

            }
        });
    }

    private void bindViews()
    {
        ivBackArrow = findViewById(R.id.ivBackArrow);
        etName = findViewById(R.id.etName);
        etPhoneno = findViewById(R.id.etPhoneno);
        etEmailID = findViewById(R.id.etEmailID);
        etPassword = findViewById(R.id.etPassword);

        spClass = findViewById(R.id.spClass);
        tvSignUp = findViewById(R.id.tvSignUp);

        tvSignUp.setOnClickListener(this);
        ivBackArrow.setOnClickListener(this);

        spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l)
            {
                classEntity = classEntityArrayList.get(position);
                selectedClassId = String.valueOf(classEntity.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ivBackArrow:
                Intent intent = new Intent(ActivitySignup.this, ActivityLogin.class);
                startActivity(intent);
                finish();
                break;

            case R.id.tvSignUp:
                validate();
                break;

            default:
                break;
        }
    }

    private void validate()
    {
        String name = etName.getText().toString();
        String phone = etPhoneno.getText().toString().trim();
        String email = etEmailID.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (name.equals(""))
        {
            StatMethods.showToastShort(this, getString(R.string.empty_name));
            return;
        }
        else if (phone.equals(""))
        {
            StatMethods.showToastShort(this, getString(R.string.empty_phone));
            return;
        }
        else if (email.equals(""))
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
            StatMethods.showToastShort(this, getString(R.string.empty_password));
            return;
        }
        registerUser(name, phone, email, password, selectedClassId);
    }

    private void registerUser(String name, String phone, String email, String password, String selectedClassId)
    {
        final APIRequestService apiRequestService = RetrofitClient.getApiService();
        Call<BaseResponseObjectEntity<LoginEntity>> call = apiRequestService.sendRegistrationRequest(name, email, password, phone, selectedClassId);
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
                            LoginEntity loginEntity = (LoginEntity) entity.getData();
                            if (loginEntity != null)
                            {
                                int userId = loginEntity.getId();
                                String token = "Bearer  " + loginEntity.getApi_token();
                                String name = loginEntity.getName();
                                String email = loginEntity.getEmail();
                                String classId = loginEntity.getClass_id();

                                UtilitySharedPreferences.setPrefs(ActivitySignup.this, AppConstants.SHAREDPREFERENCES.USER_ID, ""+userId);
                                UtilitySharedPreferences.setPrefs(ActivitySignup.this, AppConstants.SHAREDPREFERENCES.USER_AUTH_TOKEN, token);
                                UtilitySharedPreferences.setPrefs(ActivitySignup.this, AppConstants.SHAREDPREFERENCES.USER_NAME, name);
                                UtilitySharedPreferences.setPrefs(ActivitySignup.this, AppConstants.SHAREDPREFERENCES.USER_EMAIL, email);
                                UtilitySharedPreferences.setPrefs(ActivitySignup.this, AppConstants.SHAREDPREFERENCES.USER_CLASSID, classId);

                                GlobalApplication.authorizationToken = UtilitySharedPreferences.getPrefs(ActivitySignup.this, AppConstants.SHAREDPREFERENCES.USER_AUTH_TOKEN);
                                startNextActivity();
                            }
                        }
                        else if (status.equals(AppConstants.ERROR))
                        {
                            int msgCode = entity.getMsg_code();
                            StatMethods.showMsgCode(ActivitySignup.this, msgCode);
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

    private void startNextActivity()
    {
        Intent intent = new Intent(ActivitySignup.this, ActivityHome.class);
        startActivity(intent);
        finish();
    }
}
package com.paradisetechnologies.brithwine.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.paradisetechnologies.brithwine.R;
import com.paradisetechnologies.brithwine.constants.AppConstants;
import com.paradisetechnologies.brithwine.entity.BaseResponseArrayEntity;
import com.paradisetechnologies.brithwine.entity.BaseResponseObjectEntity;
import com.paradisetechnologies.brithwine.entity.ClassEntity;
import com.paradisetechnologies.brithwine.entity.UserEntity;
import com.paradisetechnologies.brithwine.network.APIRequestService;
import com.paradisetechnologies.brithwine.network.RetrofitClient;
import com.paradisetechnologies.brithwine.utils.StatMethods;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityProfile extends AppCompatActivity implements View.OnClickListener
{
    private EditText name, email, mobile;
    private Spinner spClass;
    private Button btnUpdateprofile, logout, subscriptions;
    private String selectedClassId;
    private int userClassID;
    private ImageView ivBackArrow;
    private ArrayAdapter<ClassEntity> classAdapter;
    private ArrayList<ClassEntity> classEntityArrayList;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ClassEntity classEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bindViews();
        getUserProfile();

    }

    private void bindViews()
    {
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        spClass = findViewById(R.id.spClass);
        btnUpdateprofile = findViewById(R.id.updateprofile);
        logout = findViewById(R.id.logout);
        subscriptions = findViewById(R.id.subscriptions);
        ivBackArrow = findViewById(R.id.ivBackArrow);

        btnUpdateprofile.setOnClickListener(this);
        logout.setOnClickListener(this);
        subscriptions.setOnClickListener(this);
        ivBackArrow.setOnClickListener(this);

        spClass.setEnabled(false);
    }

    private void getUserProfile()
    {
        StatMethods.showDialog(ActivityProfile.this);
        final APIRequestService apiRequestService = RetrofitClient.getApiService();
        Call<BaseResponseObjectEntity<UserEntity>> call = apiRequestService.getUserProfile(StatMethods.isToken(this), "");
        call.enqueue(new Callback<BaseResponseObjectEntity<UserEntity>>()
        {
            @Override
            public void onResponse(Call<BaseResponseObjectEntity<UserEntity>> call, Response<BaseResponseObjectEntity<UserEntity>> response)
            {
                if (response != null)
                {
                    BaseResponseObjectEntity entity = response.body();
                    if (entity != null)
                    {
                        String status = entity.getStatus();
                        if (status.equals(AppConstants.SUCCESS))
                        {
                            UserEntity userEntity = (UserEntity) entity.getData();
                            name.setText(userEntity.getName());
                            email.setText(userEntity.getEmail());
                            mobile.setText(userEntity.getMobile_number());
                            userClassID = Integer.parseInt(userEntity.getClass_id());

                            getClassList(userClassID);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseObjectEntity<UserEntity>> call, Throwable t)
            {

            }
        });
    }

    private void getClassList(int userClassID)
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
                                classAdapter = new ArrayAdapter(ActivityProfile.this, R.layout.spinner_item_black, classEntityArrayList);
                                classAdapter.setDropDownViewResource(R.layout.spinner_item_white);
                                spClass.setAdapter(classAdapter);

                                for (int i = 0; i < classEntityArrayList.size(); i++)
                                {
                                    classEntity = classEntityArrayList.get(i);
                                    if (classEntity.getId() == userClassID)
                                    {
                                        spClass.setSelection(i);
                                        break;
                                    }
                                }

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
                            StatMethods.dismissDialog();
                        }
                        else if (status.equals(AppConstants.ERROR))
                        {
                            StatMethods.dismissDialog();
                            int msgCode = entity.getMsg_code();
                            StatMethods.showMsgCode(ActivityProfile.this, msgCode);
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

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.updateprofile:
                if (btnUpdateprofile.getText().toString().trim().equalsIgnoreCase("edit")) {
                    name.setEnabled(true);
                    mobile.setEnabled(true);
                    spClass.setEnabled(true);
                    btnUpdateprofile.setText(getString(R.string.save));
                } else {
                    updateprofile();
                }

                break;

            case R.id.ivBackArrow:
                onBackPressed();
                break;

            case R.id.logout:
                logoutUser();
                break;

            case R.id.subscriptions:
                startNewActivity();
                break;
        }
    }

    private void updateprofile()
    {
        String userName = name.getText().toString();
        String userEmail = email.getText().toString().trim();
        String mobileNo = mobile.getText().toString().trim();

        if (userName.equals(""))
        {
            StatMethods.showToastShort(this, getString(R.string.empty_name));
            return;
        }
        else if (userEmail.equals(""))
        {
            StatMethods.showToastShort(this, getString(R.string.empty_emailid));
            return;
        }
        else if (!userEmail.matches(emailPattern))
        {
            StatMethods.showToastShort(this, getString(R.string.please_enter_valid_email_id));
            return;
        }
        else if (mobileNo.equals(""))
        {
            StatMethods.showToastShort(this, getString(R.string.empty_phone));
            return;
        }
        updateUser(userName, userEmail, mobileNo, selectedClassId);
    }

    private void updateUser(String userName, String userEmail, String mobileNo, String selectedClassId)
    {
        StatMethods.showDialog(ActivityProfile.this);
        final APIRequestService apiRequestService = RetrofitClient.getApiService();
        Call<BaseResponseObjectEntity> call = apiRequestService.updateUserProfile(StatMethods.isToken(this), userName,
                userEmail, mobileNo, selectedClassId);
        call.enqueue(new Callback<BaseResponseObjectEntity>() {
            @Override
            public void onResponse(Call<BaseResponseObjectEntity> call, Response<BaseResponseObjectEntity> response)
            {
                if (response != null)
                {
                    BaseResponseObjectEntity entity = response.body();
                    if (entity != null) {
                        String status = entity.getStatus();
                        if (status.equals(AppConstants.SUCCESS))
                        {
                            name.setEnabled(false);
                            email.setEnabled(false);
                            mobile.setEnabled(false);
                            spClass.setEnabled(false);
                            btnUpdateprofile.setText(getString(R.string.edit));
                            StatMethods.dismissDialog();
                            StatMethods.showToastShort(ActivityProfile.this, getString(R.string.update_profile_successfull));
                        }
                        else if (status.equals(AppConstants.ERROR))
                        {
                            StatMethods.dismissDialog();
                            int msgCode = entity.getMsg_code();
                            StatMethods.showMsgCode(ActivityProfile.this, msgCode);
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

    private void logoutUser()
    {
        StatMethods.showLogoutDialog(ActivityProfile.this);
    }

    private void startNewActivity()
    {
        Intent intent = new Intent(ActivityProfile.this, ActivityUserSubscription.class);
        startActivity(intent);
    }
}

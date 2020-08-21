package com.paradisetechnologies.brigthwing.startupActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.paradisetechnologies.brigthwing.R;
import com.paradisetechnologies.brigthwing.constants.AppConstants;
import com.paradisetechnologies.brigthwing.entity.BaseResponseObjectEntity;
import com.paradisetechnologies.brigthwing.network.APIRequestService;
import com.paradisetechnologies.brigthwing.network.RetrofitClient;
import com.paradisetechnologies.brigthwing.utils.StatMethods;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityForgotPassword extends AppCompatActivity implements View.OnClickListener
{
    private TextView tvSendPassword;
    private ImageView ivBackArrow;
    private EditText etEmailID;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        bindViews();
    }

    private void bindViews()
    {
        tvSendPassword = findViewById(R.id.tvSendPassword);
        ivBackArrow = findViewById(R.id.ivBackArrow);
        etEmailID = findViewById(R.id.etEmailID);

        tvSendPassword.setOnClickListener(this);
        ivBackArrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.tvSendPassword:
                validate();
                break;

            case R.id.ivBackArrow:
                StatMethods.startNewActivity(ActivityForgotPassword.this, ActivityLogin.class);
                break;

            default:
                break;
        }
    }

    private void validate()
    {
        String email = etEmailID.getText().toString().trim();

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
        sendPassword(email);
    }

    private void sendPassword(final String email)
    {
        StatMethods.showDialog(this);
        final APIRequestService apiRequestService = RetrofitClient.getApiService();
        Call<BaseResponseObjectEntity> call = apiRequestService.getForgotPassword(email);
        call.enqueue(new Callback<BaseResponseObjectEntity>() {
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
                            StatMethods.dismissDialog();
                            startNewActivity(email);
                        }
                        else if (status.equals(AppConstants.ERROR))
                        {
                            Log.e("TAG", "onResponse: ERRPR FROGOT ");
                            StatMethods.dismissDialog();
                            StatMethods.showToastShort(ActivityForgotPassword.this, getString(R.string.user_not_exist));
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

    private void startNewActivity(String email)
    {
        Intent intent = new Intent(ActivityForgotPassword.this, ActivityForgotPassword_LinkSent.class);
        intent.putExtra("USER_MAIL", email);
        startActivity(intent);
    }
}
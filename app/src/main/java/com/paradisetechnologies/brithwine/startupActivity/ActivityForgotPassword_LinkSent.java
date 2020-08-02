package com.paradisetechnologies.brithwine.startupActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.paradisetechnologies.brithwine.R;
import com.paradisetechnologies.brithwine.constants.AppConstants;
import com.paradisetechnologies.brithwine.entity.BaseResponseObjectEntity;
import com.paradisetechnologies.brithwine.network.APIRequestService;
import com.paradisetechnologies.brithwine.network.RetrofitClient;
import com.paradisetechnologies.brithwine.utils.StatMethods;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityForgotPassword_LinkSent extends AppCompatActivity implements View.OnClickListener
{
    private ImageView ivBackArrow;
    private Button btnResendMail;
    private TextView tvBack;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_link_sent);

        Intent intent = getIntent();
        email = intent.getStringExtra("USER_MAIL");

        bindViews();
    }

    private void bindViews()
    {
        btnResendMail = findViewById(R.id.btnResendMail);
        ivBackArrow = findViewById(R.id.ivBackArrow);
        tvBack = findViewById(R.id.tvBack);

        btnResendMail.setOnClickListener(this);
        ivBackArrow.setOnClickListener(this);
        tvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnResendMail:
                resendMail();
                break;

            case R.id.ivBackArrow:
                StatMethods.startNewActivity(ActivityForgotPassword_LinkSent.this, ActivityLogin.class);
                break;

            case R.id.tvBack:
                StatMethods.startNewActivity(ActivityForgotPassword_LinkSent.this, ActivityLogin.class);
                break;

            default:
                break;
        }
    }

    private void resendMail()
    {
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
                            StatMethods.showToastShort(ActivityForgotPassword_LinkSent.this, getString(R.string.verification_mail));
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
}
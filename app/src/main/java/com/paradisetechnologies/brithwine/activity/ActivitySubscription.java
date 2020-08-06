package com.paradisetechnologies.brithwine.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paradisetechnologies.brithwine.R;
import com.paradisetechnologies.brithwine.adapter.SubjectAdapter;
import com.paradisetechnologies.brithwine.constants.AppConstants;
import com.paradisetechnologies.brithwine.entity.BaseResponseArrayEntity;
import com.paradisetechnologies.brithwine.entity.BaseResponseObjectEntity;
import com.paradisetechnologies.brithwine.entity.LoginEntity;
import com.paradisetechnologies.brithwine.entity.OrderIdEntity;
import com.paradisetechnologies.brithwine.entity.SubjectEntity;
import com.paradisetechnologies.brithwine.network.APIRequestService;
import com.paradisetechnologies.brithwine.network.RetrofitClient;
import com.paradisetechnologies.brithwine.startupActivity.ActivityForgotPassword;
import com.paradisetechnologies.brithwine.startupActivity.ActivityLogin;
import com.paradisetechnologies.brithwine.utils.StatMethods;
import com.paradisetechnologies.brithwine.utils.UtilitySharedPreferences;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySubscription extends AppCompatActivity implements PaymentResultWithDataListener, View.OnClickListener
{
    private ImageView ivBackArrow;
    private TextView tvPay, tvClassName, tvClassFee;
    private RecyclerView rvSubjectNames;
    private SubjectAdapter subjectAdapter;
    private String orderId, amount, name, email, mobileNumber, classID, classFEE, className;
    private ArrayList<SubjectEntity> subjectEntityArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_new);

        classID = getIntent().getStringExtra(AppConstants.STRINGS.CLASS_ID);
        classFEE = getIntent().getStringExtra(AppConstants.STRINGS.CLASS_FEE);
        className = getIntent().getStringExtra(AppConstants.STRINGS.CLASS_NAME);

        Checkout.preload(getApplicationContext());

        bindViews();
        getSubjectName(classID);
    }

    private void bindViews()
    {
        ivBackArrow = findViewById(R.id.ivBackArrow);
        tvPay = findViewById(R.id.tvPay);
        tvClassName = findViewById(R.id.tvClassName);
        tvClassFee = findViewById(R.id.tvClassFee);
        rvSubjectNames = findViewById(R.id.rvSubjectNames);

        ivBackArrow.setOnClickListener(this);
        tvPay.setOnClickListener(this);
        tvClassName.setText(className);
        tvClassFee.setText(classFEE);
    }

    private void getSubjectName(String classID)
    {
        StatMethods.showDialog(this);
        final APIRequestService apiRequestService = RetrofitClient.getApiService();
        Call<BaseResponseArrayEntity<SubjectEntity>> call = apiRequestService.getSubjectList(classID);
        call.enqueue(new Callback<BaseResponseArrayEntity<SubjectEntity>>() {
            @Override
            public void onResponse(Call<BaseResponseArrayEntity<SubjectEntity>> call, Response<BaseResponseArrayEntity<SubjectEntity>> response)
            {
                if (response != null)
                {
                    BaseResponseArrayEntity<SubjectEntity> entity = response.body();
                    if (entity != null)
                    {
                        String status = entity.getStatus();
                        if (status.equals(AppConstants.SUCCESS))
                        {
                            subjectEntityArrayList = entity.getData();
                            if (subjectEntityArrayList != null && subjectEntityArrayList.size() > 0)
                            {
                                subjectAdapter = new SubjectAdapter(ActivitySubscription.this, subjectEntityArrayList);
                                rvSubjectNames.setAdapter(subjectAdapter);
                                subjectAdapter.notifyDataSetChanged();
                            }
                            StatMethods.dismissDialog();
                        }
                        else if (status.equals(AppConstants.ERROR))
                        {
                            StatMethods.dismissDialog();
                            int msgCode = entity.getMsg_code();
                            StatMethods.showMsgCode(ActivitySubscription.this, msgCode);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseArrayEntity<SubjectEntity>> call, Throwable t)
            {

            }
        });
    }

    private void generateOrderId()
    {
        StatMethods.showDialog(this);
        final APIRequestService apiRequestService = RetrofitClient.getApiService();
        Call<BaseResponseObjectEntity<OrderIdEntity>> call = apiRequestService.generateOrderId(StatMethods.isToken(this), classID, classFEE);
        call.enqueue(new Callback<BaseResponseObjectEntity<OrderIdEntity>>() {
            @Override
            public void onResponse(Call<BaseResponseObjectEntity<OrderIdEntity>> call, Response<BaseResponseObjectEntity<OrderIdEntity>> response)
            {
                if (response != null)
                {
                    BaseResponseObjectEntity entity = response.body();
                    if (entity != null)
                    {
                        String status = entity.getStatus();
                        if (status.equals(AppConstants.SUCCESS))
                        {
                            OrderIdEntity orderIdEntity = (OrderIdEntity) entity.getData();
                            if (orderIdEntity != null)
                            {
                                orderId = orderIdEntity.getRazorpay_order_id();
                                amount = orderIdEntity.getAmount();
                                name = orderIdEntity.getName();
                                email = orderIdEntity.getEmail();
                                mobileNumber = orderIdEntity.getMobile_number();
                                startPayment(orderId, amount, name, email, mobileNumber);
                            }
                            StatMethods.dismissDialog();
                        }
                        else if (status.equals(AppConstants.ERROR))
                        {
                            StatMethods.dismissDialog();
                            int msgCode = entity.getMsg_code();
                            StatMethods.showMsgCode(ActivitySubscription.this, msgCode);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseObjectEntity<OrderIdEntity>> call, Throwable t)
            {
            }
        });
    }

    private void startPayment(String orderId, String amount, String name, String email, String mobileNumber)
    {
        final Activity activity = this;
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Brightwing");
            options.put("order_id", orderId);
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", amount);

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", mobileNumber);
            options.put("prefill", preFill);
            co.open(activity, options);
        }
        catch (Exception e)
        {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID, PaymentData paymentData)
    {
        try
        {
            final APIRequestService apiRequestService = RetrofitClient.getApiService();
            Call<BaseResponseObjectEntity> call = apiRequestService.updatePayment(StatMethods.isToken(this),
                    "1", orderId, paymentData.getPaymentId(), paymentData.getSignature());
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
                                StatMethods.showToastShort(ActivitySubscription.this, getString(R.string.payment_successfull));
                                startNewActivity();
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
        catch (Exception e)
        {
        }
    }

    private void startNewActivity()
    {
        Intent intent = new Intent(ActivitySubscription.this, ActivityHome.class);
        intent.putExtra("which", 2);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPaymentError(int code, String response, PaymentData paymentData)
    {
        try
        {
            final APIRequestService apiRequestService = RetrofitClient.getApiService();
            Call<BaseResponseObjectEntity> call = apiRequestService.updateFiledPayment(StatMethods.isToken(this),
                    "0", orderId, String.valueOf(code), response);
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
                                StatMethods.showToastShort(ActivitySubscription.this, getString(R.string.payment_cancel));
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
        catch (Exception e)
        {
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.tvPay:
                generateOrderId();
                break;

            case R.id.ivBackArrow:
                onBackPressed();
                break;
        }
    }
}
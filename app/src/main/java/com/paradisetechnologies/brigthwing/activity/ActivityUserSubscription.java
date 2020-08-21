package com.paradisetechnologies.brigthwing.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.paradisetechnologies.brigthwing.R;
import com.paradisetechnologies.brigthwing.adapter.SubscriptionAdapter;
import com.paradisetechnologies.brigthwing.constants.AppConstants;
import com.paradisetechnologies.brigthwing.entity.BaseResponseArrayEntity;
import com.paradisetechnologies.brigthwing.entity.SubscriptionEntity;
import com.paradisetechnologies.brigthwing.network.APIRequestService;
import com.paradisetechnologies.brigthwing.network.RetrofitClient;
import com.paradisetechnologies.brigthwing.utils.StatMethods;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityUserSubscription extends AppCompatActivity
{
    private ImageView ivBackArrow;
    private RecyclerView rvSubscriptions;
    private LinearLayout error_layout;
    private SubscriptionAdapter subscriptionAdapter;
    private List<SubscriptionEntity> subscriptionEntityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_subscription);

        bindViews();

    }

    private void bindViews()
    {
        ivBackArrow = findViewById(R.id.ivBackArrow);
        rvSubscriptions = findViewById(R.id.rvSubscriptions);
        error_layout = findViewById(R.id.error_layout);

        ivBackArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });

        getSubscriptions();
    }

    private void getSubscriptions()
    {
        StatMethods.showDialog(this);
        final APIRequestService apiRequestService = RetrofitClient.getApiService();
        Call<BaseResponseArrayEntity<SubscriptionEntity>> call = apiRequestService.getActiveSubscription(StatMethods.isToken(this), "");
        call.enqueue(new Callback<BaseResponseArrayEntity<SubscriptionEntity>>()
        {
            @Override
            public void onResponse(Call<BaseResponseArrayEntity<SubscriptionEntity>> call, Response<BaseResponseArrayEntity<SubscriptionEntity>> response)
            {
                if (response != null)
                {
                    BaseResponseArrayEntity<SubscriptionEntity> entity = response.body();
                    if (entity != null)
                    {
                        String status = entity.getStatus();
                        if (status.equals(AppConstants.SUCCESS))
                        {
                            subscriptionEntityList = entity.getData();
                            if (subscriptionEntityList != null && subscriptionEntityList.size() > 0)
                            {
                                error_layout.setVisibility(View.GONE);
                                subscriptionAdapter = new SubscriptionAdapter(ActivityUserSubscription.this, subscriptionEntityList);
                                rvSubscriptions.setAdapter(subscriptionAdapter);
                                subscriptionAdapter.notifyDataSetChanged();
                                StatMethods.dismissDialog();
                            }
                            else
                            {
                                StatMethods.dismissDialog();
                                error_layout.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            StatMethods.dismissDialog();
                            error_layout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseArrayEntity<SubscriptionEntity>> call, Throwable t)
            {

            }
        });
    }
}
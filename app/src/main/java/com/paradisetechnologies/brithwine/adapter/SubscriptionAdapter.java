package com.paradisetechnologies.brithwine.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paradisetechnologies.brithwine.R;
import com.paradisetechnologies.brithwine.entity.SubjectEntity;
import com.paradisetechnologies.brithwine.entity.SubscriptionEntity;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder>
{
    private Activity activity;
    private List<SubscriptionEntity> subscriptionEntityList = new ArrayList<>();

    public SubscriptionAdapter(Activity activity, List<SubscriptionEntity> subscriptionEntityList)
    {
        this.activity = activity;
        this.subscriptionEntityList = subscriptionEntityList;
    }

    @NonNull
    @Override
    public SubscriptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionAdapter.ViewHolder holder, int position)
    {
        SubscriptionEntity subscriptionEntity = subscriptionEntityList.get(position);
        holder.tvClassName.setText(subscriptionEntity.getClass_name());
        holder.tvClassFee.setText(subscriptionEntity.getPaid_amount());
        holder.tvStartDate.setText(subscriptionEntity.getStart_date());
        holder.tvEndDate.setText(subscriptionEntity.getEnd_date());
    }

    @Override
    public int getItemCount()
    {
        return subscriptionEntityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvClassName, tvClassFee, tvStartDate, tvEndDate;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvClassName = itemView.findViewById(R.id.tvClassName);
            tvClassFee = itemView.findViewById(R.id.tvClassFee);
            tvStartDate = itemView.findViewById(R.id.tvStartDate);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);

        }
    }
}

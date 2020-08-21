package com.paradisetechnologies.brigthwing.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.paradisetechnologies.brigthwing.R;
import com.paradisetechnologies.brigthwing.entity.SubjectEntity;
import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder>
{
    private Activity activity;
    private List<SubjectEntity> subjectEntityArrayList = new ArrayList<>();

    public SubjectAdapter(Activity activity, List<SubjectEntity> subjectEntityArrayList)
    {
        this.activity = activity;
        this.subjectEntityArrayList = subjectEntityArrayList;
    }

    @NonNull
    @Override
    public SubjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectAdapter.ViewHolder holder, int position)
    {
        SubjectEntity subjectEntity = subjectEntityArrayList.get(position);
        holder.tvSubjectName.setText(subjectEntity.getSubject_name());
    }

    @Override
    public int getItemCount()
    {
        return subjectEntityArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvSubjectName;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
        }
    }
}

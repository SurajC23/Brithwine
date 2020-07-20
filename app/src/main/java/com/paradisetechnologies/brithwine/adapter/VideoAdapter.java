package com.paradisetechnologies.brithwine.adapter;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.paradisetechnologies.brithwine.R;
import com.paradisetechnologies.brithwine.entity.VideoEntity;
import com.paradisetechnologies.brithwine.utils.StatMethods;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>
{
    private Activity activity;
    private List<VideoEntity> videoEntityList = new ArrayList<>();
    private RequestOptions requestOptions = new RequestOptions();

    public VideoAdapter(Activity activity, List<VideoEntity> videoEntityList)
    {
        this.activity = activity;
        this.videoEntityList = videoEntityList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_watch, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final VideoEntity videoEntity = videoEntityList.get(position);

        requestOptions.placeholder(R.mipmap.placeholder_watch)
                .error(R.mipmap.placeholder_watch)
                .transform(new RoundedCorners(10));

        Glide.with(activity).load(videoEntity.getThumbnail_path()).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).into(holder.ivBanner);
        holder.tvVideoTitle.setText(videoEntity.getTitle());

        if (videoEntity.getVideo_type().equals("2"))
        {
            holder.ivPrem.setVisibility(View.VISIBLE);
        }

        holder.rlWatchAnyW.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!videoEntity.getQuiz_file_path().equals(""))
                {
                    StatMethods.showQuizBoxDialog(activity);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return videoEntityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvVideoTitle;
        private ImageView ivBanner, ivPrem;
        private ProgressBar prDuration;
        private RelativeLayout rlWatchAnyW;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvVideoTitle = itemView.findViewById(R.id.tvVideoTitle);
            ivBanner = itemView.findViewById(R.id.ivBanner);
            ivPrem = itemView.findViewById(R.id.ivPrem);
            prDuration = itemView.findViewById(R.id.prDuration);
            rlWatchAnyW = itemView.findViewById(R.id.rlWatchAnyW);
        }
    }
}

package com.paradisetechnologies.brithwine.adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.paradisetechnologies.brithwine.R;
import com.paradisetechnologies.brithwine.entity.VideoEntity;
import com.paradisetechnologies.brithwine.interfcae.PlayVideoClick;
import com.paradisetechnologies.brithwine.utils.StatMethods;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>
{
    private Activity activity;
    private List<VideoEntity> videoEntityList = new ArrayList<>();
    private RequestOptions requestOptions = new RequestOptions();
    private boolean isSubscribe;
    private PlayVideoClick playVideoClick;
    private String selectedClassId, selectedClassFee, selectedClassName;

    public VideoAdapter(Activity activity, List<VideoEntity> videoEntityList, boolean isSubscribe,
                        String selectedClassId, String selectedClassFee, String selectedClassName)
    {
        this.activity = activity;
        this.videoEntityList = videoEntityList;
        this.isSubscribe = isSubscribe;
        this.selectedClassId = selectedClassId;
        this.selectedClassFee = selectedClassFee;
        this.selectedClassName = selectedClassName;
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

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) activity).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        holder.rlWatchAnyW.getLayoutParams().width = (int) Math.round(width / 2.04);
        holder.rlWatchAnyW.getLayoutParams().height = (int) Math.round((width) / 3.12);

        requestOptions.placeholder(R.mipmap.placeholder_watch)
                .error(R.mipmap.placeholder_watch)
                .transform(new RoundedCorners(10));

        Glide.with(activity).load(videoEntity.getThumbnail_path()).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).into(holder.ivBanner);

        if (videoEntity.getVideo_type().equals("2"))
        {
            holder.ivPrem.setVisibility(View.VISIBLE);
        }

        holder.rlWatchAnyW.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if (videoEntity.getVideo_type().equals("1"))
                {
                    if (!videoEntity.getQuiz_file_path().equals(""))
                    {
                        StatMethods.showQuizBoxDialog(activity, videoEntity.getQuiz_file_path(), videoEntity.getVideoID(), videoEntity.getVideo_path(), videoEntity.getTitle(), videoEntity.getThumbnail_path(), videoEntity.getDescription());
                    }
                    else
                    {
                        playVideoClick = (PlayVideoClick) activity;
                        playVideoClick.playVideoClicked(videoEntity.getVideo_path(), videoEntity.getVideoID(), videoEntity.getTitle(), videoEntity.getThumbnail_path(), videoEntity.getDescription(), videoEntity.getVideoID());
                    }
                }
                else if (videoEntity.getVideo_type().equals("2"))
                {
                    if (!videoEntity.getQuiz_file_path().equals(""))
                    {
                        if (!isSubscribe)
                        {
                            StatMethods.showSubscriptionBoxDialog(activity, selectedClassId, selectedClassFee, selectedClassName);
                        }
                        else
                        {
                            StatMethods.showQuizBoxDialog(activity, videoEntity.getQuiz_file_path(), videoEntity.getVideoID(), videoEntity.getVideo_path(), videoEntity.getTitle(), videoEntity.getThumbnail_path(), videoEntity.getDescription());
                        }
                    }
                    else
                    {
                        if (!isSubscribe)
                        {
                            StatMethods.showSubscriptionBoxDialog(activity, selectedClassId, selectedClassFee, selectedClassName);
                        }
                        else
                        {
                            playVideoClick = (PlayVideoClick) activity;
                            playVideoClick.playVideoClicked(videoEntity.getVideo_path(), videoEntity.getVideoID(), videoEntity.getTitle(), videoEntity.getThumbnail_path(), videoEntity.getDescription(), videoEntity.getVideoID());                        }
                    }
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
        private ImageView ivBanner, ivPrem;
        private ProgressBar prDuration;
        private RelativeLayout rlWatchAnyW;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            ivBanner = itemView.findViewById(R.id.ivBanner);
            ivPrem = itemView.findViewById(R.id.ivPrem);
            prDuration = itemView.findViewById(R.id.prDuration);
            rlWatchAnyW = itemView.findViewById(R.id.rlWatchAnyW);
        }
    }
}

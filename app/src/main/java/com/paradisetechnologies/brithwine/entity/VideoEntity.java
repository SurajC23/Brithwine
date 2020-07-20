package com.paradisetechnologies.brithwine.entity;

import com.google.gson.annotations.SerializedName;

public class VideoEntity
{
    @SerializedName("title")
    String title;

    @SerializedName("description")
    String description;

    @SerializedName("video_type")
    String video_type;

    @SerializedName("thumbnail_path")
    String thumbnail_path;

    @SerializedName("video_path")
    String video_path;

    @SerializedName("quiz_file_path")
    String quiz_file_path;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideo_type() {
        return video_type;
    }

    public void setVideo_type(String video_type) {
        this.video_type = video_type;
    }

    public String getThumbnail_path() {
        return thumbnail_path;
    }

    public void setThumbnail_path(String thumbnail_path) {
        this.thumbnail_path = thumbnail_path;
    }

    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    public String getQuiz_file_path() {
        return quiz_file_path;
    }

    public void setQuiz_file_path(String quiz_file_path) {
        this.quiz_file_path = quiz_file_path;
    }
}

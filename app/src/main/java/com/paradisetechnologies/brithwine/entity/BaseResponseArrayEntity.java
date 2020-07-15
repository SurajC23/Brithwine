package com.paradisetechnologies.brithwine.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BaseResponseArrayEntity<T> {
    @SerializedName("status")
    String status;

    @SerializedName("data")
    ArrayList<T> data;

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public ArrayList<T> getData()
    {
        return data;
    }

    public void setData(ArrayList<T> data)
    {
        this.data = data;
    }
}

package com.paradisetechnologies.brithwine.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponseObjectEntity<T> {
    @Expose
    @SerializedName("status")
    String status;

    @Expose
    @SerializedName("data")
    T data;

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }
}

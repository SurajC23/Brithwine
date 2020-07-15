package com.paradisetechnologies.brithwine.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponseObjectEntity<T> {
    @Expose
    @SerializedName("status")
    String status;

    @Expose
    @SerializedName("msg_code")
    int msg_code;

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

    public int getMsg_code()
    {
        return msg_code;
    }

    public void setMsg_code(int msg_code)
    {
        this.msg_code = msg_code;
    }
}

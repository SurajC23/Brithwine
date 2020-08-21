package com.paradisetechnologies.brigthwing.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BaseResponseArrayEntity<T> {
    @SerializedName("status")
    String status;

    @Expose
    @SerializedName("msg_code")
    int msg_code;

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

    public int getMsg_code()
    {
        return msg_code;
    }

    public void setMsg_code(int msg_code)
    {
        this.msg_code = msg_code;
    }
}
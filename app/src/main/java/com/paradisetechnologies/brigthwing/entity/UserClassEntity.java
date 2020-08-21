package com.paradisetechnologies.brigthwing.entity;

import com.google.gson.annotations.SerializedName;

public class UserClassEntity
{
    @SerializedName("id")
    int id;

    @SerializedName("class_name")
    String class_name;

    @SerializedName("class_fee")
    String class_fee;

    @SerializedName("is_subscribed")
    int is_subscribed;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getClass_name()
    {
        return class_name;
    }

    public void setClass_name(String class_name)
    {
        this.class_name = class_name;
    }

    public String getClass_fee()
    {
        return class_fee;
    }

    public void setClass_fee(String class_fee)
    {
        this.class_fee = class_fee;
    }

    public int getIs_subscribed()
    {
        return is_subscribed;
    }

    public void setIs_subscribed(int is_subscribed)
    {
        this.is_subscribed = is_subscribed;
    }

    @Override
    public String toString() {
        return getClass_name();
    }
}

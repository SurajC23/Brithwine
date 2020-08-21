package com.paradisetechnologies.brigthwing.entity;

import com.google.gson.annotations.SerializedName;

public class SubscriptionEntity
{
    @SerializedName("class_id")
    int class_id;

    @SerializedName("class_name")
    String class_name;

    @SerializedName("paid_amount")
    String paid_amount;

    @SerializedName("start_date")
    String start_date;

    @SerializedName("end_date")
    String end_date;

    public int getClass_id()
    {
        return class_id;
    }

    public void setClass_id(int class_id)
    {
        this.class_id = class_id;
    }

    public String getClass_name()
    {
        return class_name;
    }

    public void setClass_name(String class_name)
    {
        this.class_name = class_name;
    }

    public String getPaid_amount()
    {
        return paid_amount;
    }

    public void setPaid_amount(String paid_amount)
    {
        this.paid_amount = paid_amount;
    }

    public String getStart_date()
    {
        return start_date;
    }

    public void setStart_date(String start_date)
    {
        this.start_date = start_date;
    }

    public String getEnd_date()
    {
        return end_date;
    }

    public void setEnd_date(String end_date)
    {
        this.end_date = end_date;
    }
}

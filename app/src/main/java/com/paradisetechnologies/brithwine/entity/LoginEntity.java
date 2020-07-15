package com.paradisetechnologies.brithwine.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginEntity
{
    @Expose
    @SerializedName("id")
    int id;

    @Expose
    @SerializedName("name")
    String name;

    @Expose
    @SerializedName("email")
    String email;

    @Expose
    @SerializedName("class_id")
    String class_id;

    @Expose
    @SerializedName("api_token")
    String api_token;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getClass_id()
    {
        return class_id;
    }

    public void setClass_id(String class_id)
    {
        this.class_id = class_id;
    }

    public String getApi_token()
    {
        return api_token;
    }

    public void setApi_token(String api_token)
    {
        this.api_token = api_token;
    }
}

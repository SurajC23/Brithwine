package com.paradisetechnologies.brigthwing.entity;

import com.google.gson.annotations.SerializedName;

public class SubjectEntity
{
    @SerializedName("id")
    int id;

    @SerializedName("subject_name")
    String subject_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    @Override
    public String toString() {
        return getSubject_name();
    }
}

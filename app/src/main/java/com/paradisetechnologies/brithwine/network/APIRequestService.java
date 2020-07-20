package com.paradisetechnologies.brithwine.network;

import com.paradisetechnologies.brithwine.entity.BaseResponseArrayEntity;
import com.paradisetechnologies.brithwine.entity.BaseResponseObjectEntity;
import com.paradisetechnologies.brithwine.entity.ClassEntity;
import com.paradisetechnologies.brithwine.entity.LoginEntity;
import com.paradisetechnologies.brithwine.entity.SubjectEntity;
import com.paradisetechnologies.brithwine.entity.VideoEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIRequestService {

    @FormUrlEncoded
    @POST("create-user")
    Call<BaseResponseObjectEntity<LoginEntity>> sendRegistrationRequest(@Field("name") String name,
                                                           @Field("email") String email,
                                                           @Field("password") String password,
                                                           @Field("phone_number") String phone_number,
                                                           @Field("class_id") String class_id);

    @FormUrlEncoded
    @POST("login")
    Call<BaseResponseObjectEntity<LoginEntity>> sendLoginRequest(@Field("email") String email, @Field("password") String password);


    @FormUrlEncoded
    @POST("get-all-class")
    Call<BaseResponseArrayEntity<ClassEntity>> getClassList(@Field("optional") String optional);

    @FormUrlEncoded
    @POST("get-subject")
    Call<BaseResponseArrayEntity<SubjectEntity>> getSubjectList(@Field("class_id") String classID);

    @FormUrlEncoded
    @POST("get-videos")
    Call<BaseResponseArrayEntity<VideoEntity>> getVideoList(@Header("Authorization") String auth, @Field("class_id") String classID, @Field("subject_id") String subjectID);

    @FormUrlEncoded
    @POST("forgot-password")
    Call<BaseResponseObjectEntity> getForgotPassword(@Field("email") String emailID);
}



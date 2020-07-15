package com.paradisetechnologies.brithwine.network;

import com.paradisetechnologies.brithwine.entity.BaseResponseArrayEntity;
import com.paradisetechnologies.brithwine.entity.BaseResponseObjectEntity;
import com.paradisetechnologies.brithwine.entity.ClassEntity;
import com.paradisetechnologies.brithwine.entity.LoginEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIRequestService {

    @FormUrlEncoded
    @POST("create-user")
    Call<BaseResponseObjectEntity> sendRegistrationRequest(@Field("name") String name,
                                                           @Field("email") String email,
                                                           @Field("password") String password,
                                                           @Field("phone_number") String phone_number,
                                                           @Field("class_id") String class_id);

    @FormUrlEncoded
    @POST("login")
    Call<BaseResponseObjectEntity<LoginEntity>> sendLoginRequest(@Field("email") String email, @Field("password") String password);


    @FormUrlEncoded
    @POST("get-all-class")
    Call<BaseResponseArrayEntity<ClassEntity>> getClassList();

}



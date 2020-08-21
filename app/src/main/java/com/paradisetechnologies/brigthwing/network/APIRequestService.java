package com.paradisetechnologies.brigthwing.network;

import com.paradisetechnologies.brigthwing.entity.BaseResponseArrayEntity;
import com.paradisetechnologies.brigthwing.entity.BaseResponseObjectEntity;
import com.paradisetechnologies.brigthwing.entity.ClassEntity;
import com.paradisetechnologies.brigthwing.entity.LoginEntity;
import com.paradisetechnologies.brigthwing.entity.OrderIdEntity;
import com.paradisetechnologies.brigthwing.entity.SubjectEntity;
import com.paradisetechnologies.brigthwing.entity.SubscriptionEntity;
import com.paradisetechnologies.brigthwing.entity.UserClassEntity;
import com.paradisetechnologies.brigthwing.entity.UserEntity;
import com.paradisetechnologies.brigthwing.entity.VideoEntity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

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
    Call<BaseResponseObjectEntity<LoginEntity>> sendLoginRequest(@Field("email") String email,
                                                                 @Field("password") String password);


    @FormUrlEncoded
    @POST("get-all-class")
    Call<BaseResponseArrayEntity<ClassEntity>> getClassList(@Field("optional") String optional);

    @FormUrlEncoded
    @POST("get-user-class")
    Call<BaseResponseArrayEntity<UserClassEntity>> getUserClassList(@Header("Authorization") String auth,
                                                                    @Field("Optional") String optional);

    @FormUrlEncoded
    @POST("get-subject")
    Call<BaseResponseArrayEntity<SubjectEntity>> getSubjectList(@Field("class_id") String classID);

    @FormUrlEncoded
    @POST("get-videos")
    Call<BaseResponseArrayEntity<VideoEntity>> getVideoList(@Header("Authorization") String auth,
                                                            @Field("class_id") String classID,
                                                            @Field("subject_id") String subjectID);

    @FormUrlEncoded
    @POST("forgot-password")
    Call<BaseResponseObjectEntity> getForgotPassword(@Field("email") String emailID);

    @FormUrlEncoded
    @POST("get-user-profile")
    Call<BaseResponseObjectEntity<UserEntity>> getUserProfile(@Header("Authorization") String auth,
                                                              @Field("Optional") String optional);

    @FormUrlEncoded
    @POST("create-new-payment")
    Call<BaseResponseObjectEntity<OrderIdEntity>> generateOrderId(@Header("Authorization") String auth,
                                                                  @Field("class_id") String classID,
                                                                  @Field("amount") String amount);

    @FormUrlEncoded
    @POST("verify-token")
    Call<BaseResponseObjectEntity> verifyToken(@Header("Authorization") String auth,
                                               @Field("Optional") String optional);

    @FormUrlEncoded
    @POST("update-payment")
    Call<BaseResponseObjectEntity> updatePayment(@Header("Authorization") String auth,
                                                 @Field("payment_status") String paymentStatus,
                                                 @Field("razorpay_order_id") String orderID,
                                                 @Field("razorpay_payment_id") String paymentID,
                                                 @Field("razorpay_signature") String paymentSignature);

    @FormUrlEncoded
    @POST("update-payment")
    Call<BaseResponseObjectEntity> updateFiledPayment(@Header("Authorization") String auth,
                                                 @Field("payment_status") String paymentStatus,
                                                 @Field("razorpay_order_id") String orderID,
                                                 @Field("error_code") String errorCode,
                                                 @Field("error_description") String errorDesc);

    @FormUrlEncoded
    @POST("update-user-profile")
    Call<BaseResponseObjectEntity> updateUserProfile(@Header("Authorization") String auth,
                                                     @Field("name") String name,
                                                     @Field("email") String email,
                                                     @Field("phone_number") String phone_number,
                                                     @Field("class_id") String class_id);


    @FormUrlEncoded
    @POST("get-active-subscription")
    Call<BaseResponseArrayEntity<SubscriptionEntity>> getActiveSubscription(@Header("Authorization") String auth,
                                                                            @Field("Optional") String optional);


    @FormUrlEncoded
    @POST("verify-email-otp")
    Call<BaseResponseObjectEntity> verifyOtp(@Field("email") String email, @Field("otp") String otp);


    @FormUrlEncoded
    @POST("videos-view")
    Call<BaseResponseObjectEntity> sendVideoView(@Header("Authorization") String auth,
                                                 @Field("video_id") String videoID);


    @Streaming
    @GET
    Call<ResponseBody> downloadFileByUrl(@Url String fileUrl);
}



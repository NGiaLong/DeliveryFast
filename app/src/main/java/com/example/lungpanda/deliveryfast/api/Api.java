package com.example.lungpanda.deliveryfast.api;

/**
 * Created by LungPanda on 10/12/2017.
 */
import com.example.lungpanda.deliveryfast.model.SignIn.SignIn;
import com.example.lungpanda.deliveryfast.model.SignIn.SignInResult;
import com.example.lungpanda.deliveryfast.model.SignUp.SignUp;
import com.example.lungpanda.deliveryfast.model.SignUp.SignUpResult;
import com.example.lungpanda.deliveryfast.model.Store.StoreResult;
import com.example.lungpanda.deliveryfast.model.UpdateResult;
import com.example.lungpanda.deliveryfast.model.User.User;
import com.example.lungpanda.deliveryfast.model.User.UserAddress;
import com.example.lungpanda.deliveryfast.model.User.UserAddressResult;
import com.example.lungpanda.deliveryfast.model.User.UserPhone;
import com.example.lungpanda.deliveryfast.model.User.UserPhoneResult;
import com.example.lungpanda.deliveryfast.model.User.UserResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    //USER

    @GET("users")
    Call<List<UserResult>> listTasks(@Query("offset") int offset, @Query("limit") int limit);

    @GET("users/profile")
    Call<UserResult> getUser(@Header("Content-Type") String content_type,  @Header("Authorization") String id_token);

    @POST("users/signup")
    Call<SignUpResult> createAccount(@Header("Content-Type") String content_type, @Body SignUp signUp);

    @POST("users/signin")
    Call<SignInResult> signIn(@Header("Content-Type") String content_type, @Body SignIn signIn);

    @PUT("users/updateInfo")
    Call<UpdateResult> updateInfor(@Header("Content-Type") String content_type, @Body User user, @Header("Authorization") String id_token);

    //USER/PHONE

    @GET("users/phoneNumber")
    Call<UserPhoneResult> getPhone(@Header("Content-Type") String content_type,  @Header("Authorization") String id_token);

    @POST("users/phoneNumber")
    Call<UserPhoneResult> phone(@Header("Content-Type") String content_type, @Header("Authorization") String id_token, @Body UserPhone userPhone);

    @DELETE("users/phoneNumber/{phoneId}")
    Call<UserPhoneResult> deletePhone(@Path("phoneId") String phoneId, @Header("Content-Type") String content_type, @Header("Authorization") String id_token);

    @PUT("users/phoneNumber/{phoneId}")
    Call<UserPhoneResult> setMainPhone(@Path("phoneId") String phoneId, @Header("Content-Type") String content_type, @Header("Authorization") String id_token);

    //USER/ADDRESS

    @GET("users/address")
    Call<UserAddressResult> getAddress(@Header("Content-Type") String content_type,  @Header("Authorization") String id_token);

    @POST("users/address")
    Call<UserAddressResult> createAddress(@Header("Content-Type") String content_type, @Header("Authorization") String id_token, @Body UserAddress userAddress);

    @DELETE("users/address/{addressId}")
    Call<UserAddressResult> deleteAddress(@Path("addressId") String addressId, @Header("Content-Type") String content_type, @Header("Authorization") String id_token );

    //STORE

    @GET("stores")
    Call<StoreResult> getStores (@Header("Content-Type") String content_type);



}

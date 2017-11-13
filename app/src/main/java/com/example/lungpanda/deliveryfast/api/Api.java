package com.example.lungpanda.deliveryfast.api;

/**
 * Created by LungPanda on 10/12/2017.
 */
import com.example.lungpanda.deliveryfast.model.Order.Order;
import com.example.lungpanda.deliveryfast.model.SignIn.SignIn;
import com.example.lungpanda.deliveryfast.model.SignIn.SignInResult;
import com.example.lungpanda.deliveryfast.model.SignUp.SignUp;
import com.example.lungpanda.deliveryfast.model.SignUp.SignUpResult;
import com.example.lungpanda.deliveryfast.model.Store.StoreListResult;
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
import retrofit2.Callback;
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

    @GET("/api/users")
    Call<List<UserResult>> listTasks(@Query("offset") int offset, @Query("limit") int limit);

    @GET("/api/users/profile")
    Call<UserResult> getUser(@Header("Content-Type") String content_type,  @Header("Authorization") String id_token);

    @POST("/api/users/signup")
    Call<SignUpResult> createAccount(@Header("Content-Type") String content_type, @Body SignUp signUp);

    @POST("/api/users/signin")
    Call<SignInResult> signIn(@Header("Content-Type") String content_type, @Body SignIn signIn);

    @PUT("/api/users/updateInfo")
    Call<UpdateResult> updateInfor(@Header("Content-Type") String content_type, @Body User user, @Header("Authorization") String id_token);

    //USER/PHONE

    @GET("/api/users/phoneNumber")
    Call<UserPhoneResult> getPhone(@Header("Content-Type") String content_type,  @Header("Authorization") String id_token);

    @POST("/api/users/phoneNumber")
    Call<UserPhoneResult> phone(@Header("Content-Type") String content_type, @Header("Authorization") String id_token, @Body UserPhone userPhone);

    @DELETE("/api/users/phoneNumber/{phoneId}")
    Call<UserPhoneResult> deletePhone(@Path("phoneId") String phoneId, @Header("Content-Type") String content_type, @Header("Authorization") String id_token);

    @PUT("/api/users/phoneNumber/{phoneId}")
    Call<UserPhoneResult> setMainPhone(@Path("phoneId") String phoneId, @Header("Content-Type") String content_type, @Header("Authorization") String id_token);

    //USER/ADDRESS

    @GET("/api/users/address")
    Call<UserAddressResult> getAddress(@Header("Content-Type") String content_type,  @Header("Authorization") String id_token);

    @POST("/api/users/address")
    Call<UserAddressResult> createAddress(@Header("Content-Type") String content_type, @Header("Authorization") String id_token, @Body UserAddress userAddress);

    @DELETE("/api/users/address/{addressId}")
    Call<UserAddressResult> deleteAddress(@Path("addressId") String addressId, @Header("Content-Type") String content_type, @Header("Authorization") String id_token );

    @PUT("/api/users/address/{addressId}")
    Call<UserAddressResult> editAddress(@Path("addressId") String addressId, @Header("Content-Type") String content_type, @Header("Authorization") String id_token, @Body UserAddress userAddress);

    //STORE

    @GET("/api/stores")
    Call<StoreListResult> getStores (@Header("Content-Type") String content_type);

    @GET("/api/stores/{storeId}")
    Call<StoreResult> getStoreById(@Path("storeId") String storeId, @Header("Content-Type") String content_type);

    @GET("/api/stores/token/{storeId}")
    Call<StoreResult> getStoreByIdLogin(@Path("storeId") String storeId, @Header("Content-Type") String content_type, @Header("Authorization") String id_token);


    @GET("/api/{storeId}/{discountCode}")
    Call<StoreResult> getDiscount(@Path("storeId") String storeId,@Path("discountCode") String discountCode, @Header("Content-Type") String content_type, @Header("Authorization") String id_token);

}

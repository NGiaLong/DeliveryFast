package com.example.lungpanda.deliveryfast.api;

import com.example.lungpanda.deliveryfast.model.Order.DiscountResult;
import com.example.lungpanda.deliveryfast.model.Order.Order;
import com.example.lungpanda.deliveryfast.model.Order.OrderResult;
import com.example.lungpanda.deliveryfast.model.Store.StoreResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by LungPanda on 11/12/2017.
 */

public interface OrderApi {
    @PUT("/api/orders/{orderId}")
    Call<OrderResult> saveOrder(@Path("orderId") String orderId, @Header("Content-Type") String content_type, @Header("Authorization") String id_token, @Body Order order);

    @GET("/api/orders/{orderId}")
    Call<OrderResult> getOrder(@Path("orderId") String orderId, @Header("Content-Type") String content_type, @Header("Authorization") String id_token);

    @GET("/api/discounts/checkCode")
    Call<DiscountResult> getDiscount(@Query("storeId") String storeId, @Query("code") String code, @Header("Content-Type") String content_type, @Header("Authorization") String id_token);
}

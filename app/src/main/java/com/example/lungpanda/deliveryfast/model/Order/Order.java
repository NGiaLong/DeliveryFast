package com.example.lungpanda.deliveryfast.model.Order;

import com.example.lungpanda.deliveryfast.model.Store.Store;
import com.example.lungpanda.deliveryfast.model.User.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by LungPanda on 11/9/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class Order {
    @SerializedName("id")
    private String id;
    @SerializedName("user_phone")
    private String user_phone;
    @SerializedName("user_address")
    private String user_address;
    @SerializedName("user_name")
    private String user_name;
    @SerializedName("latitude")
    private float latitude;
    @SerializedName("longitude")
    private float longitude;
    @SerializedName("order_date")
    private String order_date;
    @SerializedName("delivery_date")
    private String delivery_date;
    @SerializedName("ship_fee")
    private int ship_fee;
    @SerializedName("total_amount")
    private int total_amount;
    @SerializedName("note")
    private String note;
    @SerializedName("status")
    private String status;
    @SerializedName("payment")
    private String payment = "Cash";
    @SerializedName("user")
    private User user;
    @SerializedName("store")
    private Store store;
    @SerializedName("employee")
    private User employee;
    @SerializedName("deliMan")
    private User deliMan;
    @SerializedName("orderDetails")
    List<OrderDetail> orderDetails;

    public Order() {
    }
}

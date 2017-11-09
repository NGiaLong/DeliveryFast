package com.example.lungpanda.deliveryfast.model.Order;

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
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("store_id")
    private String store_id;
    @SerializedName("employee_id")
    private String employee_id;
    @SerializedName("deliMan_id")
    private String deliMan_id;
    @SerializedName("orderDetails")
    List<OrderDetail> orderDetails;

    public Order() {
    }
}

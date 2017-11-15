package com.example.lungpanda.deliveryfast.model.Order;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by LungPanda on 11/12/2017.
 */
@Data
public class OrderData {
    @SerializedName("order")
    private Order order;
}

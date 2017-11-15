package com.example.lungpanda.deliveryfast.model.Order;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by LungPanda on 11/15/2017.
 */
@Data
public class DiscountResult {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private DiscountData discountData;
}

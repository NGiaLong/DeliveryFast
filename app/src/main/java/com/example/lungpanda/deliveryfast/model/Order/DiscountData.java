package com.example.lungpanda.deliveryfast.model.Order;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by LungPanda on 11/15/2017.
 */
@Data
public class DiscountData {
    @SerializedName("percentage")
    private int discountPercent;
}

package com.example.lungpanda.deliveryfast.model.Store;

import com.example.lungpanda.deliveryfast.model.Order.Order;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by LungPanda on 11/6/2017.
 */
@Data
public class StoreData {
    @SerializedName("store")
    private Store store;
    @SerializedName("order")
    private Order order;
}

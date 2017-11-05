package com.example.lungpanda.deliveryfast.model.Store;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by LungPanda on 11/2/2017.
 */
@Data
public class StoreListResult {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private StoreListData storeListData;
}

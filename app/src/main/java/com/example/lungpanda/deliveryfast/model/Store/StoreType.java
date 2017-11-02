package com.example.lungpanda.deliveryfast.model.Store;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by LungPanda on 11/2/2017.
 */
@Data
public class StoreType {
    @SerializedName("id")
    private String id;
    @SerializedName("type")
    private String type;
}

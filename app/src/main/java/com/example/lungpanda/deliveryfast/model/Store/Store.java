package com.example.lungpanda.deliveryfast.model.Store;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Created by LungPanda on 10/4/2017.
 */
@Data
public class Store {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("phone_number")
    private String phone_number;
    @SerializedName("opening_time")
    private String opening_time;
    @SerializedName("closing_time")
    private String closing_time;
    @SerializedName("latitude")
    private long latitude;
    @SerializedName("longitude")
    private long longitude;
    @SerializedName("status")
    private boolean status;
    @SerializedName("storeTypes")
    private List<StoreType> storeTypes;
}

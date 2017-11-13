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
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("status")
    private boolean status;
    @SerializedName("image_url")
    private String image_url;
    @SerializedName("storeTypes")
    private List<StoreType> storeTypes;
    @SerializedName("categories")
    private List<Category> categories;
}

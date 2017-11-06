package com.example.lungpanda.deliveryfast.model.Store;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by LungPanda on 11/3/2017.
 */
@Data
public class Product {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private long price;
    @SerializedName("image_url")
    private String image_url;
    @SerializedName("category_id")
    private String category_id;
}

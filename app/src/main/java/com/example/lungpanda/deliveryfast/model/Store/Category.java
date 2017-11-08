package com.example.lungpanda.deliveryfast.model.Store;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Created by LungPanda on 11/3/2017.
 */
@Data
public class Category {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("store_id")
    private String store_id;
    @SerializedName("addons")
    private List<AddOn> addOn;
    @SerializedName("products")
    private List<Product> products;
}

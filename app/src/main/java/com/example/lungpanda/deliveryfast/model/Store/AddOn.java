package com.example.lungpanda.deliveryfast.model.Store;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Created by LungPanda on 11/3/2017.
 */
@Data
public class AddOn {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("category_id")
    private String category_id;
    @SerializedName("role")
    private int role;
    @SerializedName("product_addons")
    List<ProductAddOn> productAddOns;
}

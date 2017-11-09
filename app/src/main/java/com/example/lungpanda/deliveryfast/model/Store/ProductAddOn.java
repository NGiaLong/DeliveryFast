package com.example.lungpanda.deliveryfast.model.Store;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by LungPanda on 11/3/2017.
 */
@Data
public class ProductAddOn {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private long price;
    @SerializedName("addon_id")
    private String addon_id;

    private boolean checked = false;
}

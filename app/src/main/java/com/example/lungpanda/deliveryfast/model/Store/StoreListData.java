package com.example.lungpanda.deliveryfast.model.Store;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Created by LungPanda on 11/2/2017.
 */
@Data
public class StoreListData {
    @SerializedName("stores")
    private List<Store> stores;
}

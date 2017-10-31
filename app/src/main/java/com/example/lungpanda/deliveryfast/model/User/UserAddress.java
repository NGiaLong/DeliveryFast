package com.example.lungpanda.deliveryfast.model.User;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by LungPanda on 10/13/2017.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
@Data
public class UserAddress {
    @SerializedName("id")
    private String id;
    @SerializedName("address")
    private String address;
    @SerializedName("latitude")
    private Float latitude;
    @SerializedName("longitude")
    private Float longitude;
    @SerializedName("user_id")
    private String user_id;
}

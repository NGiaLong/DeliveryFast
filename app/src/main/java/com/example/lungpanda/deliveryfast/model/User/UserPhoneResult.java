package com.example.lungpanda.deliveryfast.model.User;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by LungPanda on 10/27/2017.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
@Data
public class UserPhoneResult {
    @SerializedName("status")
    private Boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private UserPhoneData UserPhoneData;
}

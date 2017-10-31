package com.example.lungpanda.deliveryfast.model.User;

import com.example.lungpanda.deliveryfast.model.User.UserData;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by LungPanda on 10/12/2017.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
@Data
public class UserResult {
    @SerializedName("status")
    private Boolean status;
    @SerializedName("data")
    private UserData userData;
}

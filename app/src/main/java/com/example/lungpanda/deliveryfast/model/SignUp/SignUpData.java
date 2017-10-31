package com.example.lungpanda.deliveryfast.model.SignUp;

import com.example.lungpanda.deliveryfast.model.User.User;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by LungPanda on 10/13/2017.
 */
@Data
public class SignUpData {
    @SerializedName("id_token")
    private String id_token;
    @SerializedName("user")
    private User user;
}

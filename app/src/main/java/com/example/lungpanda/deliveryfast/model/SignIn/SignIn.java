package com.example.lungpanda.deliveryfast.model.SignIn;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.experimental.Builder;

/**
 * Created by LungPanda on 10/10/2017.
 */
@Data
public class SignIn {
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private  String password;

    public SignIn(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

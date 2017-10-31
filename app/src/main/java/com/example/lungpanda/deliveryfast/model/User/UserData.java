package com.example.lungpanda.deliveryfast.model.User;

import com.example.lungpanda.deliveryfast.model.User.User;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by LungPanda on 10/13/2017.
 */
@Data
public class UserData {
    @SerializedName("user")
    private User user;
}

package com.example.lungpanda.deliveryfast.model.User;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Created by LungPanda on 10/27/2017.
 */
@Data
public class UserPhoneData {
    @SerializedName("phones")
    private List<UserPhone> userPhones;
}

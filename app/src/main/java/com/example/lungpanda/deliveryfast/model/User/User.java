package com.example.lungpanda.deliveryfast.model.User;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Created by LungPanda on 10/12/2017.
 */
@Data
public class User {
    @SerializedName("id")
    private String id;
    @SerializedName("username")
    private String username;
    private String password;
    @SerializedName("email")
    private String email;
    @SerializedName("first_name")
    private String first_name;
    @SerializedName("last_name")
    private String last_name;
    @SerializedName("gender")
    private Boolean gender;
    @SerializedName("date_of_birth")
    private String date_of_birth;
    @SerializedName("status")
    private Boolean status;
    @SerializedName("point")
    private int point;
    @SerializedName("userPhones")
    private List<UserPhone> userPhones;
    @SerializedName("userAddress")
    private  List<UserAddress> userAddresses;

}

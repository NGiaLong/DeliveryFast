package com.example.lungpanda.deliveryfast.model.User;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Created by LungPanda on 2017/11/01.
 */
@Data
public class UserAddressData {
    @SerializedName("addresses")
    private List<UserAddress> userAddresses;
    @SerializedName("address")
    private UserAddress userAddress;
}

package com.example.lungpanda.deliveryfast.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by LungPanda on 10/18/2017.
 */
@Data
public class ErrorData {
    @SerializedName("code")
    private int code;
}

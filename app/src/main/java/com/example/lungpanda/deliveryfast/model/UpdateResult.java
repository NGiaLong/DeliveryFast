package com.example.lungpanda.deliveryfast.model;

import com.example.lungpanda.deliveryfast.model.User.User;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by LungPanda on 10/16/2017.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
@Data
public class UpdateResult {
    @SerializedName("status")
    private Boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private UpdateData updateData;
    @SerializedName("error")
    private ErrorData errorData;
}

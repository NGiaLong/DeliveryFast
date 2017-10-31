package com.example.lungpanda.deliveryfast.model.SignUp;

import com.example.lungpanda.deliveryfast.model.ErrorData;
import com.example.lungpanda.deliveryfast.model.SignUp.SignUpData;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by LungPanda on 10/12/2017.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
@Data
public class SignUpResult {
    @SerializedName("status")
    private Boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private SignUpData data;
    @SerializedName("error")
    private ErrorData errorData;
}

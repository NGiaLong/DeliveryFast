package com.example.lungpanda.deliveryfast.model.SignIn;

import com.example.lungpanda.deliveryfast.model.ErrorData;
import com.example.lungpanda.deliveryfast.model.SignIn.SignInData;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by LungPanda on 10/13/2017.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
@Data
public class SignInResult {
    @SerializedName("status")
    private Boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private SignInData data;
    @SerializedName("error")
    private ErrorData errorData;
}

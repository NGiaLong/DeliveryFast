package com.example.lungpanda.deliveryfast.model.SignUp;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.Data;

/**
 * Created by LungPanda on 10/10/2017.
 */
@Data
public class SignUp{
    private String email;
    private String password;
    private String username;

    public SignUp(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }
}


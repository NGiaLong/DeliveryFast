package com.example.lungpanda.deliveryfast.model.SignIn;

import com.example.lungpanda.deliveryfast.model.User.User;

import lombok.Data;

/**
 * Created by LungPanda on 10/13/2017.
 */
@Data
public class SignInData {
    private User user;
    private String id_token;
}

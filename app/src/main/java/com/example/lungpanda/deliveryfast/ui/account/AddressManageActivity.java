package com.example.lungpanda.deliveryfast.ui.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.model.User.UserAddress;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

@EActivity(R.layout.activity_address_manage)
public class AddressManageActivity extends AppCompatActivity {

    @Extra
    String id_token;
    @Extra
    UserAddress userAddress;

    @Override
    public void onBackPressed(){
        AddressActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
    }

}

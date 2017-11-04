package com.example.lungpanda.deliveryfast.ui.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lungpanda.deliveryfast.R;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_address_create)
public class AddressCreateActivity extends AppCompatActivity {

    @Override
    public void onBackPressed(){
        AddressActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
    }



}

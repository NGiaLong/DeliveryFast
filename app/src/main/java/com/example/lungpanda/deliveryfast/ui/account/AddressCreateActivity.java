package com.example.lungpanda.deliveryfast.ui.account;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.example.lungpanda.deliveryfast.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_address_create)
public class AddressCreateActivity extends AppCompatActivity {
    @AfterViews
    void init(){

    }
    @Override
    public void onBackPressed(){
        AddressActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
    }

    @Click(R.id.tvAddressOnMaps)
    void onAddressOnMapsClick(){

    }




}

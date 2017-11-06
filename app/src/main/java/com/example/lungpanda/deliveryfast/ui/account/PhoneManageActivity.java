package com.example.lungpanda.deliveryfast.ui.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.api.Api;
import com.example.lungpanda.deliveryfast.api.ApiClient;
import com.example.lungpanda.deliveryfast.model.User.UserPhone;
import com.example.lungpanda.deliveryfast.model.User.UserPhoneResult;
import com.example.lungpanda.deliveryfast.ui.home.HomeActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import retrofit2.Callback;
import retrofit2.Response;

@EActivity(R.layout.activity_phone_manage)
public class PhoneManageActivity extends AppCompatActivity {
    @ViewById(R.id.imgBack)
    ImageView mImgBack;
    @ViewById(R.id.btnDeletePhone)
    Button mBtnDelePhone;
    @ViewById(R.id.tvPhoneNumber)
    TextView mTvPhoneNumber;
    @ViewById(R.id.sIsPrimary)
    Switch mSIsPrimary;

    @Extra
    String id_token;
    @Extra
    UserPhone userPhone;

    private long mLastClickTime = 0;

    @AfterViews
    void init() {
        mTvPhoneNumber.setText(userPhone.getPhone_number());
        mSIsPrimary.setChecked(userPhone.isRole());
    }

    @Click(R.id.imgBack)
    void setmImgBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed(){
        PhoneNumberActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
    }

    @Click(R.id.btnDeletePhone)
    void setmBtnDelePhone() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        // Creating alert Dialog with one Button
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PhoneManageActivity.this);
        //AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("DELETE");
        alertDialog.setIcon(R.drawable.delete);

        // Setting Dialog Message
        alertDialog.setMessage("Do you want to delete this phone number?");

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Api api = ApiClient.retrofit().create(Api.class);
                        retrofit2.Call<UserPhoneResult> result = api.deletePhone(userPhone.getId(), "application/json", "Bearer " + id_token);
                        result.enqueue(new Callback<UserPhoneResult>() {
                            @Override
                            public void onResponse(retrofit2.Call<UserPhoneResult> call, Response<UserPhoneResult> response) {
                                if (response.isSuccessful()){
                                    if (response.body().getStatus()){
                                        PhoneNumberActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
                                        Toast.makeText(getApplication(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplication(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    PhoneNumberActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
                                    Toast.makeText(getApplication(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(retrofit2.Call<UserPhoneResult> call, Throwable t) {
                                HomeActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
                                Toast.makeText(getApplication(), "Server is not working!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // closed

        // Showing Alert Message
        alertDialog.show();

    }
    @CheckedChange(R.id.sIsPrimary)
    void setmSIsPrimary(){
        mLastClickTime = SystemClock.elapsedRealtime();
        // Creating alert Dialog with one Button
        if (!userPhone.isRole()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(PhoneManageActivity.this);
            //AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

            // Setting Dialog Title
            alertDialog.setTitle("PRIMARY");
            alertDialog.setIcon(R.drawable.key);

            // Setting Dialog Message
            alertDialog.setMessage("Do you want to set primary phone number?");

            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Api api = ApiClient.retrofit().create(Api.class);
                            retrofit2.Call<UserPhoneResult> result = api.setMainPhone(userPhone.getId(), "application/json", "Bearer " + id_token);
                            result.enqueue(new Callback<UserPhoneResult>() {
                                @Override
                                public void onResponse(retrofit2.Call<UserPhoneResult> call, Response<UserPhoneResult> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus()) {
                                            PhoneNumberActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
                                            Toast.makeText(getApplication(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplication(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        PhoneNumberActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
                                        Toast.makeText(getApplication(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(retrofit2.Call<UserPhoneResult> call, Throwable t) {
                                    HomeActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
                                    Toast.makeText(getApplication(), "Server is not working!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            PhoneNumberActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
                        }
                    });

            // closed

            // Showing Alert Message
            alertDialog.show();

            mSIsPrimary.setChecked(false);
        } else {
            mSIsPrimary.setChecked(true);
        }

    }
}

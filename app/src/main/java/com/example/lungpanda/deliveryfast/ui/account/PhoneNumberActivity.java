package com.example.lungpanda.deliveryfast.ui.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.adapter.DividerItemDecoration;
import com.example.lungpanda.deliveryfast.adapter.PhoneAdapter;
import com.example.lungpanda.deliveryfast.api.Api;
import com.example.lungpanda.deliveryfast.api.ApiClient;
import com.example.lungpanda.deliveryfast.model.User.UserAddress;
import com.example.lungpanda.deliveryfast.model.User.UserPhone;
import com.example.lungpanda.deliveryfast.model.User.UserPhoneResult;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EActivity(R.layout.activity_phone_number)
public class PhoneNumberActivity extends AppCompatActivity {
    @ViewById(R.id.imgBack)
    ImageView mImBack;
    @ViewById(R.id.tvCreatePhone)
    TextView mTvCPhone;
    @ViewById(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<UserPhone> phoneList = new ArrayList<>();
    private PhoneAdapter phoneAdapter;
    private String id_token;
    private long mLastClickTime = 0;

    @AfterViews
    void init() {
        SharedPreferences settings = getSharedPreferences(SignInActivity.NAME_SHAREPREFERENCE, 0);
        id_token = settings.getString(SignInActivity.ID_TOKEN, "");

        phoneAdapter = new PhoneAdapter(phoneList, new PhoneAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UserPhone userPhone) {
                //Action when click
                //Toast.makeText(getApplication(), userPhone.getId(), Toast.LENGTH_SHORT).show();
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                PhoneManageActivity_.intent(getApplicationContext()).id_token(id_token).userPhone(userPhone).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(phoneAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        prepareData();
    }

    @Click(R.id.tvCreatePhone)
    void setmTvCPhone() {
        // Creating alert Dialog with one Button
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PhoneNumberActivity.this);
        //AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("NEW PHONE NUMBER");
        alertDialog.setIcon(R.drawable.phone_number);

        // Setting Dialog Message
        alertDialog.setMessage("Enter your phone number");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        input.setMaxLines(1);
        alertDialog.setView(input);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        UserPhone tmp = new UserPhone();
                        tmp.setPhone_number(input.getText().toString().trim());

                        Api api = ApiClient.retrofit().create(Api.class);
                        retrofit2.Call<UserPhoneResult> result = api.phone("application/json", "Bearer " + id_token, tmp);
                        result.enqueue(new Callback<UserPhoneResult>() {
                            @Override
                            public void onResponse(retrofit2.Call<UserPhoneResult> call, Response<UserPhoneResult> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().getStatus()) {
                                        prepareData();
                                    } else {
                                        Toast.makeText(getApplication(), "Can not add new number", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplication(), "Server is not working", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(retrofit2.Call<UserPhoneResult> call, Throwable t) {

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


    @Click(R.id.imgBack)
    void setmImBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed(){
        UserProfileActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
    }


    private void prepareData() {
        Api api = ApiClient.retrofit().create(Api.class);
        retrofit2.Call<UserPhoneResult> result = api.getPhone("application/json", "Bearer " + id_token);
        result.enqueue(new Callback<UserPhoneResult>() {
            @Override
            public void onResponse(retrofit2.Call<UserPhoneResult> call, Response<UserPhoneResult> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        List<UserPhone> tmp = response.body().getUserPhoneData().getUserPhones();
                        if(tmp != null){
                            phoneList.clear();
                            for (UserPhone phone : tmp) {
                                phoneList.add(phone);
                            }
                            phoneAdapter.notifyDataSetChanged();
                        }
                    } else {
                        UserProfileActivity_.intent(getApplicationContext()).start();
                    }
                } else {
                    UserProfileActivity_.intent(getApplicationContext()).start();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<UserPhoneResult> call, Throwable t) {

            }
        });

    }
}

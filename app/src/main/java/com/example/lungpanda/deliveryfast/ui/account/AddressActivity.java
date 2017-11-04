package com.example.lungpanda.deliveryfast.ui.account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.adapter.AddressAdapter;
import com.example.lungpanda.deliveryfast.adapter.DividerItemDecoration;
import com.example.lungpanda.deliveryfast.api.Api;
import com.example.lungpanda.deliveryfast.api.ApiClient;
import com.example.lungpanda.deliveryfast.model.User.UserAddress;
import com.example.lungpanda.deliveryfast.model.User.UserAddressResult;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EActivity(R.layout.activity_address)
public class AddressActivity extends AppCompatActivity {
    @ViewById(R.id.imgBack)
    ImageView mImgBack;
    @ViewById(R.id.tvCreateAddress)
    TextView mTvCreatAddress;
    @ViewById(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<UserAddress> userAddresses = new ArrayList<>();
    private AddressAdapter addressAdapter;
    private String id_token;
    private long mLastClickTime = 0;

    @AfterViews
    void init() {
        SharedPreferences settings = getSharedPreferences(SignInActivity.NAME_SHAREPREFERENCE, 0);
        id_token = settings.getString(SignInActivity.ID_TOKEN, "");

        addressAdapter = new AddressAdapter(userAddresses, new AddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UserAddress userAddress) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                AddressManageActivity_.intent(getApplicationContext()).id_token(id_token).userAddress(userAddress).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();

            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(addressAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        prepareData();

    }

    private void prepareData() {
        Api api = ApiClient.retrofit().create(Api.class);
        Call<UserAddressResult> result = api.getAddress("application/json", "Bearer " + id_token);
        result.enqueue(new Callback<UserAddressResult>() {
            @Override
            public void onResponse(Call<UserAddressResult> call, Response<UserAddressResult> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        List<UserAddress> tmp = response.body().getUserAddressData().getUserAddresses();
                        if (tmp != null) {
                            userAddresses.clear();
                            for (UserAddress address : tmp) {
                                userAddresses.add(address);
                            }
                            addressAdapter.notifyDataSetChanged();
                        }
                    } else {
                        onBackPressed();
                    }
                } else {
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<UserAddressResult> call, Throwable t) {
                onBackPressed();

            }
        });
    }

    @Click(R.id.imgBack)
    void setmImBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed(){
        UserProfileActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
    }


    @Click(R.id.tvCreateAddress)
    void setmTvCreatAddress() {
        AddressCreateActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
    }


}

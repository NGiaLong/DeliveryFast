package com.example.lungpanda.deliveryfast.ui.home;

import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.adapter.DividerItemDecoration;
import com.example.lungpanda.deliveryfast.adapter.StoreAdapter;
import com.example.lungpanda.deliveryfast.api.Api;
import com.example.lungpanda.deliveryfast.api.ApiClient;
import com.example.lungpanda.deliveryfast.model.Store.Store;
import com.example.lungpanda.deliveryfast.model.Store.StoreResult;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EFragment(R.layout.activity_store)
public class StoreActivity extends Fragment {
    private List<Store> storeList = new ArrayList<>();
    private StoreAdapter sAdapter;
    @ViewById(R.id.recycler_view)
    RecyclerView recyclerView;


    @AfterViews
    void init() {

        sAdapter = new StoreAdapter(storeList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(sAdapter);
        prepareStoreData();

    }

    private void prepareStoreData() {
        Api api = ApiClient.retrofit().create(Api.class);
        Call<StoreResult> resultCall = api.getStores("application/json");
        resultCall.enqueue(new Callback<StoreResult>() {
            @Override
            public void onResponse(Call<StoreResult> call, Response<StoreResult> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        List<Store> tmp = response.body().getStoreData().getStores();
                        if (tmp != null) {
                            storeList.clear();
                            for (Store store : tmp) {
                                storeList.add(store);
                            }
                            Log.i("TEST123456", "onResponse: " + storeList);
                            sAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.i("TEST123456", "onResponse: Fail respomse");
                    }
                } else {
                    // Get fail
                    Log.i("TEST123456", "onResponse: Fail server");
                }
            }

            @Override
            public void onFailure(Call<StoreResult> call, Throwable t) {
                //Exit app
                Log.i("TEST123456", "onResponse: FAIL");
            }
        });
    }
}

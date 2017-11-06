package com.example.lungpanda.deliveryfast.ui.home;

import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.adapter.DividerItemDecoration;
import com.example.lungpanda.deliveryfast.adapter.StoreListAdapter;
import com.example.lungpanda.deliveryfast.api.Api;
import com.example.lungpanda.deliveryfast.api.ApiClient;
import com.example.lungpanda.deliveryfast.model.Store.Store;
import com.example.lungpanda.deliveryfast.model.Store.StoreListResult;
import com.example.lungpanda.deliveryfast.ui.order.OrderActivity_;
import com.google.gson.Gson;

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
    private StoreListAdapter sAdapter;
    @ViewById(R.id.recycler_view)
    RecyclerView recyclerView;


    @AfterViews
    void init() {

        sAdapter = new StoreListAdapter(storeList, new StoreListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Store store) {
                OrderActivity_.intent(getContext()).store_id(store.getId()).start();
                Toast.makeText(getContext(), store.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(sAdapter);

        prepareStoreData();


    }

    private void prepareStoreData() {
        Api api = ApiClient.retrofit().create(Api.class);
        Call<StoreListResult> resultCall = api.getStores("application/json");
        resultCall.enqueue(new Callback<StoreListResult>() {
            @Override
            public void onResponse(Call<StoreListResult> call, Response<StoreListResult> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        List<Store> tmp = response.body().getStoreListData().getStores();
                        if (tmp != null) {
                            storeList.clear();
                            for (Store store : tmp) {
                                storeList.add(store);
                                sAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG);
                    }
                } else {
                    Toast.makeText(getContext(), "Server is not working", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<StoreListResult> call, Throwable t) {
                Toast.makeText(getContext(), "Server is not working", Toast.LENGTH_LONG);
            }
        });
    }
}

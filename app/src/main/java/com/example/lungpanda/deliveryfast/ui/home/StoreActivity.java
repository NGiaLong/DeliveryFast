package com.example.lungpanda.deliveryfast.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.adapter.DividerItemDecoration;
import com.example.lungpanda.deliveryfast.adapter.StoreListAdapter;
import com.example.lungpanda.deliveryfast.api.Api;
import com.example.lungpanda.deliveryfast.api.ApiClient;
import com.example.lungpanda.deliveryfast.model.Store.Category;
import com.example.lungpanda.deliveryfast.model.Store.Store;
import com.example.lungpanda.deliveryfast.model.Store.StoreListResult;
import com.example.lungpanda.deliveryfast.ui.account.SignInActivity;
import com.example.lungpanda.deliveryfast.ui.account.SignInActivity_;
import com.example.lungpanda.deliveryfast.ui.order.OrderActivity_;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
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
    @ViewById(R.id.progressBar)
    ProgressBar mProgressBar;

    @FragmentArg
    String id_token;


    @AfterViews
    void init() {
        mProgressBar.setVisibility(View.VISIBLE);

        sAdapter = new StoreListAdapter(storeList, new StoreListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Store store) {
                if (id_token != null && !id_token.equals("")) {
                    OrderActivity_.intent(getContext()).store_id(store.getId()).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
                } else {
                    SignInActivity_.intent(getContext()).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
                    Toast.makeText(getContext(), "You have to sign in first!", Toast.LENGTH_LONG).show();
                }
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
                            }
                            for (int i = storeList.size() - 1; i >= 0; i--) {
                                for (int j = storeList.get(i).getCategories().size() - 1; j >= 0; j--) {
                                    if (storeList.get(i).getCategories().get(j).getProducts().size() == 0) {
                                        storeList.get(i).getCategories().remove(storeList.get(i).getCategories().get(j));
                                    }
                                }
                                if (storeList.get(i).getCategories().size() == 0) {
                                    storeList.remove(storeList.get(i));
                                }
                            }
                            sAdapter.notifyDataSetChanged();
                            if (mProgressBar != null) {
                                mProgressBar.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        DialogFragment dialogFragment = new DialogFragment();
                    }
                } else {
                    Toast.makeText((getActivity()).getBaseContext(), "Server is not working", Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<StoreListResult> call, Throwable t) {
                Toast.makeText(getContext(), "Fail", Toast.LENGTH_LONG).show();
                getActivity().onBackPressed();
            }
        });
    }
}

package com.example.lungpanda.deliveryfast.ui.order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.adapter.CategoryAdapter;
import com.example.lungpanda.deliveryfast.adapter.DividerItemDecoration;
import com.example.lungpanda.deliveryfast.api.Api;
import com.example.lungpanda.deliveryfast.api.ApiClient;
import com.example.lungpanda.deliveryfast.model.Store.Category;
import com.example.lungpanda.deliveryfast.model.Store.StoreResult;
import com.google.gson.Gson;

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

@EActivity(R.layout.activity_order)
public class OrderActivity extends AppCompatActivity {
    private List<Category> categoryList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    @ViewById(R.id.category_recycler_view)
    RecyclerView recyclerView;
    @ViewById(R.id.tvStoreName)
    TextView mTvStoreName;
    @Extra
    String store_id;

    @AfterViews
    void init(){
        categoryAdapter = new CategoryAdapter(categoryList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(categoryAdapter);
        prepareStoreData();
    }

    public void prepareStoreData(){
        if (store_id == null){
            onBackPressed();
        } else {
            Api api = ApiClient.retrofit().create(Api.class);
            Call<StoreResult> resultCall = api.getStoreById(store_id,"application/json");
            resultCall.enqueue(new Callback<StoreResult>() {

                @Override
                public void onResponse(Call<StoreResult> call, Response<StoreResult> response) {
                    if (response.isSuccessful()){
                        if (response.body().isStatus()){
                            Log.i("TEST123456", "body "+ response.body());
                            mTvStoreName.setText(response.body().getStoreData().getStore().getName());
                            List<Category> tmp = response.body().getStoreData().getStore().getCategories();
                            if(tmp != null){
                                categoryList.clear();
                                for (Category category: tmp){
                                    categoryList.add(category);
                                    categoryAdapter.notifyDataSetChanged();
                                }
                                Log.i("TEST123456", "categoryList: " + categoryList);
                            }


                        } else {
                            onBackPressed();
                        }
                    } else {
                        onBackPressed();
                    }
                }

                @Override
                public void onFailure(Call<StoreResult> call, Throwable t) {
                    onBackPressed();
                }
            });
        }


    }
    @Click(R.id.tvStoreName)
    void aVoid(){
        Gson gson = new Gson();
        Log.i("DINGU123", "aVoid: " + gson.toJson(categoryList));
    }
}

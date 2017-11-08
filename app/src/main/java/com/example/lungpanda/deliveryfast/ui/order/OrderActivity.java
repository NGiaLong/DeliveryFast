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
import com.example.lungpanda.deliveryfast.model.Order.OrderDetail;
import com.example.lungpanda.deliveryfast.model.Store.Category;
import com.example.lungpanda.deliveryfast.model.Store.Product;
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
    private List<OrderDetail> mainOrderDetailList = new ArrayList<>();
    @ViewById(R.id.category_recycler_view)
    RecyclerView recyclerView;
    @ViewById(R.id.tvStoreName)
    TextView mTvStoreName;
    @Extra
    String store_id;

    @AfterViews
    void init() {
        categoryAdapter = new CategoryAdapter(categoryList, this.getSupportFragmentManager());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(categoryAdapter);
        prepareStoreData();

    }

    public void prepareStoreData() {
        if (store_id == null) {
            onBackPressed();
        } else {
            Api api = ApiClient.retrofit().create(Api.class);
            Call<StoreResult> resultCall = api.getStoreById(store_id, "application/json");
            resultCall.enqueue(new Callback<StoreResult>() {

                @Override
                public void onResponse(Call<StoreResult> call, Response<StoreResult> response) {
                    if (response.isSuccessful()) {
                        if (response.body().isStatus()) {
                            mTvStoreName.setText(response.body().getStoreData().getStore().getName());
                            List<Category> tmp = response.body().getStoreData().getStore().getCategories();
                            if (tmp != null) {
                                categoryList.clear();
                                for (Category category : tmp) {
                                    categoryList.add(category);
                                    categoryAdapter.notifyDataSetChanged();
                                }
                                Log.i("MAMAMA11", "DATA: " + categoryList);
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

    public void addOrderDetail(String product_id, OrderDetail orderDetail) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (Category category : categoryList) {
            for (Product product : category.getProducts()) {
                if (product.getId().equals(product_id)) {
                    List<OrderDetail> orderDetailList = product.getOrderDetails();
                    if (orderDetailList == null) {
                        orderDetails.add(orderDetail);
                        product.setOrderDetails(orderDetails);
                    } else if (orderDetailList.size() == 0) {
                        orderDetails.add(orderDetail);
                        product.setOrderDetails(orderDetails);
                    } else {
                        boolean isAdd = false;
                        for(OrderDetail tmp: orderDetailList){
                            if (isAdd){
                                orderDetails.add(tmp);
                            } else {
                                if (tmp.equals(orderDetail)){
                                    tmp.setQuanlity(tmp.getQuanlity() + orderDetail.getQuanlity());

                                    orderDetails.add(tmp);
                                    isAdd = true;
                                } else {
                                    orderDetails.add(tmp);
                                }
                            }
                        }
                        if (!isAdd){
                            orderDetails.add(orderDetail);
                        }
                        product.setOrderDetails(orderDetails);
                    }
                    categoryAdapter.notifyDataSetChanged();
                    updateMainOrderDetail();
                    break;
                }

            }
        }
    }

    public void minusOrderDetail(String product_id, List<OrderDetail> orderDetails){
        for (Category category : categoryList) {
            for (Product product : category.getProducts()) {
                if (product.getId().equals(product_id)) {
                    product.setOrderDetails(orderDetails);
                    categoryAdapter.notifyDataSetChanged();
                    updateMainOrderDetail();
                    break;
                }
            }
        }
    }

    private void updateMainOrderDetail(){
        mainOrderDetailList.clear();
        for (Category category : categoryList) {
            for (Product product : category.getProducts()) {
                List<OrderDetail> orderDetailList = product.getOrderDetails();
                if (orderDetailList == null) {
                    if (orderDetailList.size() > 0){
                        for (OrderDetail tmp: orderDetailList){
                            mainOrderDetailList.add(tmp);
                        }
                    }
                }
            }
        }
    }
    @Click(R.id.tvStoreName)
    void aVoid(){
        Gson gson = new Gson();
        Log.i("DINGU123456", "aVoid: " + gson.toJson(categoryList));
    }

}

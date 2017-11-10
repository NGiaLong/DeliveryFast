package com.example.lungpanda.deliveryfast.ui.order;



import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.adapter.CategoryAdapter;
import com.example.lungpanda.deliveryfast.adapter.DividerItemDecoration;
import com.example.lungpanda.deliveryfast.api.Api;
import com.example.lungpanda.deliveryfast.api.ApiClient;
import com.example.lungpanda.deliveryfast.model.Order.Order;
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
    @ViewById(R.id.imgBack)
    ImageView mImgBack;
    @ViewById(R.id.tvStoreName)
    TextView mTvStoreName;
    @ViewById(R.id.tvContinue)
    TextView mTvContinue;
    @ViewById(R.id.tvReset)
    TextView mTvReset;
    @ViewById(R.id.tvOrderItem)
    TextView mTvOrderItem;
    @ViewById(R.id.flOrderDetail)
    FrameLayout mFlOrderDetail;

    @Extra
    String store_id;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;


    @AfterViews
    void init() {
        categoryAdapter = new CategoryAdapter(categoryList, this.getSupportFragmentManager());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(categoryAdapter);
        prepareStoreData();
        updateMainOrderDetail();
        updateBottomView();
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
                            if (tmp != null && tmp.size() > 0) {
                                for (int i = tmp.size() - 1; i >= 0; i--) {
                                    if (tmp.get(i).getProducts().size() == 0) {
                                        tmp.remove(tmp.get(i));
                                    }
                                }
                                if (tmp.size() > 0) {
                                    categoryList.clear();
                                    for (Category category : tmp) {
                                        categoryList.add(category);
                                        categoryAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    onBackPressed();
                                }
                            } else {
                                onBackPressed();
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

    public void addOrderDetail(OrderDetail orderDetail) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (Category category : categoryList) {
            for (Product product : category.getProducts()) {
                if (product.getId().equals(orderDetail.getProduct_id())) {
                    List<OrderDetail> orderDetailList = product.getOrderDetails();
                    if (orderDetailList == null) {
                        orderDetails.add(orderDetail);
                        product.setOrderDetails(orderDetails);
                    } else if (orderDetailList.size() == 0) {
                        orderDetails.add(orderDetail);
                        product.setOrderDetails(orderDetails);
                    } else {
                        boolean isAdd = false;
                        for (OrderDetail tmp : orderDetailList) {
                            if (isAdd) {
                                orderDetails.add(tmp);
                            } else {
                                if (tmp.equals(orderDetail)) {
                                    tmp.setQuantity(tmp.getQuantity() + orderDetail.getQuantity());
                                    tmp.setPrice(tmp.getUnit_price() * tmp.getQuantity());
                                    orderDetails.add(tmp);
                                    isAdd = true;
                                } else {
                                    orderDetails.add(tmp);
                                }
                            }
                        }
                        if (!isAdd) {
                            orderDetails.add(orderDetail);
                        }
                        product.setOrderDetails(orderDetails);
                    }
                    categoryAdapter.notifyDataSetChanged();
                    updateMainOrderDetail();
                    updateBottomView();
                    break;
                }

            }
        }
    }

    public void minusOrderDetail(String product_id, List<OrderDetail> orderDetails) {
        for (Category category : categoryList) {
            for (Product product : category.getProducts()) {
                if (product.getId().equals(product_id)) {
                    product.setOrderDetails(orderDetails);
                    categoryAdapter.notifyDataSetChanged();
                    updateMainOrderDetail();
                    updateBottomView();
                    break;
                }
            }
        }
    }

    private void updateMainOrderDetail() {
        mainOrderDetailList.clear();
        for (Category category : categoryList) {
            for (Product product : category.getProducts()) {
                List<OrderDetail> orderDetailList = product.getOrderDetails();
                if (orderDetailList != null) {
                    if (orderDetailList.size() > 0) {
                        for (OrderDetail tmp : orderDetailList) {
                            mainOrderDetailList.add(tmp);
                        }
                    }
                }
            }
        }
        updateBottomView();
    }

    @Click(R.id.tvContinue)
    void setmTvContinue() {
        Order order = new Order();
        order.setStore_id(store_id);
        order.setOrderDetails(mainOrderDetailList);
        Log.i("ORDER123456", "Order: " + new Gson().toJson(order));
        SubmitOrderActivity_.intent(this).sStore_id(store_id).sStoreName(mTvStoreName.getText().toString()).sOrder(new Gson().toJson(order)).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
    }

    @Click(R.id.imgBack)
    void setmImgBack() {
        onBackPressed();
    }

    @Click(R.id.tvReset)
    void setmTvReset() {
        //dialog do you want to reset all?
        mainOrderDetailList.clear();
        List<OrderDetail> list = new ArrayList<>();
        for (Category category : categoryList) {
            for (Product product : category.getProducts()) {
                product.setOrderDetails(list);
            }
        }
        updateBottomView();
        categoryAdapter.notifyDataSetChanged();
    }

    @Click(R.id.tvOrderItem)
    public void setmTvOrderItem() {
        if (mainOrderDetailList.size() > 0) {
            mFlOrderDetail.setVisibility(View.VISIBLE);
            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.addToBackStack("a");
            mFragmentTransaction.add(R.id.flOrderDetail,OrderDetailFragment_.builder().sStoreName(mTvStoreName.getText().toString()).sOrderDetail(new Gson().toJson(mainOrderDetailList)).build()).commit();
        }
    }

    public void updateViewFromFragment(List<OrderDetail> orderDetails){
        mainOrderDetailList.clear();
        mainOrderDetailList.addAll(orderDetails);
        for (Category category : categoryList) {
            for (Product product : category.getProducts()) {
                if (product.getOrderDetails() != null){
                    product.getOrderDetails().clear();
                    List<OrderDetail> resetList = new ArrayList<>();
                    for (OrderDetail tmp : orderDetails){
                        if (tmp.getProduct_id().equals(product.getId())){
                            resetList.add(tmp);
                        }
                    }
                    product.setOrderDetails(resetList);
                }
            }
        }
        updateBottomView();
        categoryAdapter.notifyDataSetChanged();
    }

    public void updateBottomView() {
        if (mainOrderDetailList != null) {
            switch (mainOrderDetailList.size()) {
                case 0:
                    mTvOrderItem.setText("0 item - 0 đ");
                    mTvReset.setVisibility(View.INVISIBLE);
                    mTvReset.setEnabled(false);
                    mTvContinue.setVisibility(View.INVISIBLE);
                    mTvContinue.setEnabled(false);
                    break;
                default:
                    int sumItem = 0, sumPrice = 0;
                    for (OrderDetail tmp : mainOrderDetailList) {
                        sumItem += tmp.getQuantity();
                        sumPrice += tmp.getPrice();
                    }
                    mTvOrderItem.setText(sumItem + " items - " + sumPrice + " đ");
                    mTvReset.setVisibility(View.VISIBLE);
                    mTvReset.setEnabled(true);
                    mTvContinue.setVisibility(View.VISIBLE);
                    mTvContinue.setEnabled(true);

            }
        } else {
            mTvOrderItem.setText("0 item - 0 đ");
            mTvReset.setVisibility(View.INVISIBLE);
            mTvReset.setEnabled(false);
            mTvContinue.setVisibility(View.INVISIBLE);
            mTvContinue.setEnabled(false);
        }
    }

}

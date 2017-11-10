package com.example.lungpanda.deliveryfast.ui.order;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.model.Order.Order;
import com.example.lungpanda.deliveryfast.model.Order.OrderDetail;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_submit_order)
public class SubmitOrderActivity extends AppCompatActivity {
    @ViewById(R.id.flOrderDetail)
    FrameLayout mFlOrderDetail;
    @ViewById(R.id.trAllItem)
    TableRow mTrAllItem;
    @ViewById(R.id.tvStoreName)
    TextView mTvStoreName;
    @ViewById(R.id.llSubmitOrder)
    LinearLayout mLlSubmitOrder;
    @Extra
    String sOrder;
    @Extra
    String sStoreName;
    @Extra
    String sStore_id;

    private int numberItems = 0, totalAmountDetail = 0, shipFee = 0, discountAmount = 0, totalAmount = 0;
    private float distance = 0;
    private String mNote = "";
    private int discountPercent = 0;
    private boolean isDiscount = false;

    private Order mOrder;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private List<OrderDetail> mOrderDetailList = new ArrayList<>();

    @AfterViews
    void init(){
        if (sOrder != null && sStoreName != null){
            mTvStoreName.setText(sStoreName);
            mOrder = new Gson().fromJson(sOrder, Order.class);
            mOrderDetailList.clear();
            mOrderDetailList.addAll(mOrder.getOrderDetails());
            updateAllPriceView();
        }
    }

    @Click(R.id.trAllItem)
    void setmTrAllItem(){
        if (mOrderDetailList.size() > 0) {
            mFlOrderDetail.setVisibility(View.VISIBLE);
            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.addToBackStack("a");
            mFragmentTransaction.replace(R.id.flOrderDetail,OrderDetailFragment_.builder().sStore_id(sStore_id).sStoreName(sStoreName).sOrderDetail(new Gson().toJson(mOrderDetailList)).build()).commit();
        }
    }

    @Click(R.id.llSubmitOrder)
    void setmLlSubmitOrder(){
        mOrder.setNote(mNote);
        mOrder.setShip_fee(shipFee);
        mOrder.setTotal_amount(totalAmount);
        Log.i("FINALORDER123456", "setmLlSubmitOrder: " + new Gson().toJson(mOrder));
    }

    public void updateAllPriceView(){
        numberItems = totalAmountDetail = shipFee = discountAmount = totalAmount = 0;
        for (OrderDetail orderDetail: mOrderDetailList){
            numberItems += orderDetail.getQuantity();
            totalAmountDetail += orderDetail.getPrice();
        }
        shipFee = (int) Math.round(distance * 5000);
        if (isDiscount){
            //Nếu nhập đúng mã code
            discountAmount = (totalAmountDetail + shipFee) * discountPercent / 100;
        } else {
            discountAmount = 0;
        }
        totalAmount = totalAmountDetail + shipFee - discountAmount;
        //Set view lại mà nhác làm quá
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.flView,OrderViewFragment_.builder().sOrderDetail(new Gson().toJson(mOrderDetailList)).itemAmount(numberItems).build()).commit();
    }

}

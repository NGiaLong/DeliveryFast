package com.example.lungpanda.deliveryfast.ui.order;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.adapter.DividerItemDecoration;
import com.example.lungpanda.deliveryfast.adapter.OrderDetailAddapter;
import com.example.lungpanda.deliveryfast.model.Order.OrderDetail;
import com.example.lungpanda.deliveryfast.model.Store.Product;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import lombok.NonNull;

/**
 * Created by LungPanda on 11/8/2017.
 */
@EFragment(R.layout.dialog_minus_addon)
public class MinusAddonDialog extends DialogFragment {
    @ViewById(R.id.orderdetail_recycler_view)
    RecyclerView mRvOrderdetail_recycler_view;
    @ViewById(R.id.tvOrderDetailsTotalPrice)
    TextView mTvOrderDetailsTotalPrice;
    @ViewById(R.id.btnDone)
    Button mBtnDone;

    @FragmentArg
    String product;

    private Product mProduct;
    private List<OrderDetail> mOrderDetails;
    private OrderDetailAddapter mOrderDetailAddapter;

    @AfterViews
    void init() {
        Gson gson = new Gson();
        if (product != null) {
            mProduct = gson.fromJson(product, Product.class);
        } else {
            dismiss();
        }

        if (mProduct != null) {
            mOrderDetails = mProduct.getOrderDetails();
            mOrderDetailAddapter = new OrderDetailAddapter(mOrderDetails, new OrderDetailAddapter.OnItemClickListener() {
                @Override
                public void onIncreaseClick(OrderDetail orderDetail) {
                    orderDetail.setQuantity(orderDetail.getQuantity() + 1);
                    orderDetail.setPrice(orderDetail.getQuantity() * orderDetail.getUnit_price());
                    updateView();
                    mOrderDetailAddapter.notifyDataSetChanged();
                }

                @Override
                public void onDecreaseClick(OrderDetail orderDetail) {
                    if (orderDetail.getQuantity() > 0) {
                        orderDetail.setQuantity(orderDetail.getQuantity() - 1);
                        orderDetail.setPrice(orderDetail.getQuantity() * orderDetail.getUnit_price());
                        updateView();
                        mOrderDetailAddapter.notifyDataSetChanged();
                    }
                }
            });
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            mRvOrderdetail_recycler_view.setLayoutManager(mLayoutManager);
            mRvOrderdetail_recycler_view.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            mRvOrderdetail_recycler_view.setAdapter(mOrderDetailAddapter);
            updateView();

        } else {
            dismiss();
        }

    }

    void updateView() {
        int totalPrice = 0;
        for (OrderDetail tmp : mOrderDetails) {
            totalPrice += tmp.getPrice();
        }
        mTvOrderDetailsTotalPrice.setText(totalPrice + " đ");
    }

    @Click(R.id.btnDone)
    void setmBtnDone() {
        for (int i = mOrderDetails.size() - 1; i >= 0; i--) {
            if (mOrderDetails.get(i).getQuantity() == 0) {
                mOrderDetails.remove(mOrderDetails.get(i));
            }
        }
        ((OrderActivity) getActivity()).minusOrderDetail(mProduct.getId(), mOrderDetails);
        dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * Set width & height of dialog on onResume method follow this
         * http://stackoverflow.com/questions/14946887/setting-the-size-of-a-dialogfragment
         */
        if (getDialog().getWindow() != null) {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = getWidthDialog();
            params.gravity = getPositionDialog();
            getDialog().getWindow().setAttributes(params);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        if (dialog.getWindow() != null) {
//            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return dialog;
    }

    public void show(FragmentManager manager) {
        if (manager != null && manager.beginTransaction() != null) {
            super.show(manager, "");
        }
    }

    public boolean isShow(DialogFragment manager) {
        if (manager != null) {
            return true;
        }
        return false;
    }

    protected int getWidthDialog() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width * 98 / 100;
    }

    protected int getHeightDialog() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        return height * 60 / 100;
    }

    protected int getPositionDialog() {
        return Gravity.CENTER;
    }
}

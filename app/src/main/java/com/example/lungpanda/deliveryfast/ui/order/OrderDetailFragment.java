package com.example.lungpanda.deliveryfast.ui.order;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.adapter.DividerItemDecoration;
import com.example.lungpanda.deliveryfast.adapter.OrderDetailAddapter;
import com.example.lungpanda.deliveryfast.model.Order.OrderDetail;
import com.example.lungpanda.deliveryfast.model.Store.AddOn;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.Type;
import java.util.List;


@EFragment(R.layout.fragment_order_detail)
public class OrderDetailFragment extends Fragment {
    @ViewById(R.id.imgBack)
    ImageView mImgBack;
    @ViewById(R.id.tvStoreName)
    TextView mTvStoreName;
    @ViewById(R.id.tvReset)
    TextView mTvReset;
    @ViewById(R.id.tvDone)
    TextView mTvDone;
    @ViewById(R.id.tvTotalItem)
    TextView mTvTotalItem;
    @ViewById(R.id.order_detail_recycler_view)
    RecyclerView mRecyclerView;
    @ViewById(R.id.llSnackBar)
    LinearLayout mLinearLayout;

    @FragmentArg
    String sOrderDetail;
    @FragmentArg
    String sStoreName;
    @FragmentArg
    String sStore_id;

    private List<OrderDetail> mOrderDetails;
    private OrderDetailAddapter mOrderDetailAddapter;
    private int totalItem = 0;

    @AfterViews
    void init() {
        if (sOrderDetail != null && sStoreName != null) {
            mTvStoreName.setText(sStoreName);
            if (getActivity() instanceof OrderActivity) {
                mTvReset.setVisibility(View.VISIBLE);
                mTvDone.setVisibility(View.GONE);
                mLinearLayout.setVisibility(View.VISIBLE);

            } else if (getActivity() instanceof SubmitOrderActivity) {
                mTvReset.setVisibility(View.GONE);
                mTvDone.setVisibility(View.VISIBLE);
                mLinearLayout.setVisibility(View.GONE);
            }
            Type mType = new TypeToken<List<OrderDetail>>() {
            }.getType();
            mOrderDetails = new Gson().fromJson(sOrderDetail, mType);
            if (mOrderDetails != null && mOrderDetails.size() > 0) {
                mOrderDetailAddapter = new OrderDetailAddapter(mOrderDetails, new OrderDetailAddapter.OnItemClickListener() {
                    @Override
                    public void onIncreaseClick(OrderDetail orderDetail) {
                        orderDetail.setQuantity(orderDetail.getQuantity() + 1);
                        orderDetail.setPrice(orderDetail.getQuantity() * orderDetail.getUnit_price());
                        mOrderDetailAddapter.notifyDataSetChanged();
                        updateView();
                    }

                    @Override
                    public void onDecreaseClick(OrderDetail orderDetail) {
                        if (orderDetail.getQuantity() > 0) {
                            orderDetail.setQuantity(orderDetail.getQuantity() - 1);
                            for (int i = mOrderDetails.size() - 1; i >= 0; i--) {
                                if (mOrderDetails.get(i).getQuantity() == 0) {
                                    mOrderDetails.remove(mOrderDetails.get(i));
                                }
                            }
                            orderDetail.setPrice(orderDetail.getQuantity() * orderDetail.getUnit_price());
                        }
                        mOrderDetailAddapter.notifyDataSetChanged();
                        updateView();
                    }
                });
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                mRecyclerView.setAdapter(mOrderDetailAddapter);
                updateView();
            } else {
                getFragmentManager().popBackStack();
            }
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Click(R.id.tvReset)
    void setmTvReset() {
        if (getActivity() instanceof OrderActivity) {
            ((OrderActivity) getActivity()).setmTvReset();
        } else if(getActivity() instanceof SubmitOrderActivity) {
            OrderActivity_.intent(this).store_id(sStore_id).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
        }
        getFragmentManager().popBackStack();
    }

    @Click(R.id.tvDone)
    void setmImgBack() {
        backActivity();
    }

    @Click(R.id.llSnackBar)
    void setmTvDone() {
        backActivity();
    }

    @Click(R.id.imgBack)
    void backActivity() {
        if (getActivity() instanceof OrderActivity) {
            ((OrderActivity) getActivity()).updateViewFromFragment(mOrderDetails);
        } else if (getActivity() instanceof SubmitOrderActivity) {

        }
        getFragmentManager().popBackStack();
    }

    public void updateView() {
        totalItem = 0;
        for (OrderDetail orderDetail : mOrderDetails) {
            totalItem += orderDetail.getQuantity();
        }
        switch (totalItem) {
            case 0:
                mTvTotalItem.setText("0 item");
                setmTvReset();
                break;
            case 1:
                mTvTotalItem.setText("1 item");
                break;
            default:
                mTvTotalItem.setText(totalItem + " items");
        }
    }
}

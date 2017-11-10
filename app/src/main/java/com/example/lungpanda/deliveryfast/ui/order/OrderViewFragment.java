package com.example.lungpanda.deliveryfast.ui.order;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.adapter.DividerItemDecoration;
import com.example.lungpanda.deliveryfast.adapter.OrderViewAdapter;
import com.example.lungpanda.deliveryfast.model.Order.OrderDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LungPanda on 11/10/2017.
 */
@EFragment(R.layout.fragment_order_view)
public class OrderViewFragment extends Fragment {
    private List<OrderDetail> mDetailList;
    private OrderViewAdapter mOrderViewAdapter;
    @ViewById(R.id.tvViewMore)
    TextView mTvViewMore;
    @ViewById(R.id.order_view_recycler_view)
    RecyclerView mRecyclerView;
    @FragmentArg
    String sOrderDetail;
    @FragmentArg
    int itemAmount;
    @AfterViews
    void init(){
        if (sOrderDetail != null){
            Type mType = new TypeToken<List<OrderDetail>>() {
            }.getType();
            mDetailList = new Gson().fromJson(sOrderDetail, mType);
            if (mDetailList != null){
                if (mDetailList.size() < 4){
                    mTvViewMore.setVisibility(View.GONE);
                    mOrderViewAdapter = new OrderViewAdapter(mDetailList);
                } else {
                    mTvViewMore.setVisibility(View.VISIBLE);
                    List<OrderDetail> tmp = new ArrayList<>();
                    int quanlity = 0;
                    for (int i = 0; i < 3; i++){
                        tmp.add(mDetailList.get(i));
                        quanlity += mDetailList.get(i).getQuantity();
                    }
                    mOrderViewAdapter = new OrderViewAdapter(tmp);
                    switch (itemAmount - quanlity){
                        case 1:
                            mTvViewMore.setText("1 item more...");
                            break;
                        default:
                            mTvViewMore.setText(itemAmount + " items more...");
                    }
                }
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                mRecyclerView.setAdapter(mOrderViewAdapter);
            }
        }
    }
}

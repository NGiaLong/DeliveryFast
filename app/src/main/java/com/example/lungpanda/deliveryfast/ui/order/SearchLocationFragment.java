package com.example.lungpanda.deliveryfast.ui.order;

import android.content.Context;
import android.location.Address;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.adapter.AddressAdapter;
import com.example.lungpanda.deliveryfast.model.Order.Order;
import com.example.lungpanda.deliveryfast.model.User.UserAddress;
import com.example.lungpanda.deliveryfast.utils.LocationUtils;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;


@EFragment(R.layout.fragment_search_location)
public class SearchLocationFragment extends Fragment {
    @ViewById(R.id.imgBack)
    ImageView mImgBack;
    @ViewById(R.id.edtSearchAddress)
    EditText mEdtSearchAddress;
    @ViewById(R.id.tvDone)
    TextView mTvDone;
    @ViewById(R.id.address_recycler_view)
    RecyclerView mRecyclerView;

    private AddressAdapter mAddressAdapter;
    private List<UserAddress> mUserAddresses = new ArrayList<>();
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    @FragmentArg
    String sOrder;
    private Order mOrder;

    @AfterViews
    void init(){
        mOrder = new Gson().fromJson(sOrder, Order.class);
        mUserAddresses = mOrder.getUser().getUserAddresses();
        mAddressAdapter = new AddressAdapter(mUserAddresses, new AddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UserAddress userAddress) {
                mOrder.setUser_address(userAddress.getAddress());
                mOrder.setLatitude((float) userAddress.getLatitude());
                mOrder.setLongitude((float) userAddress.getLongitude());
                mFragmentManager = (getActivity()).getSupportFragmentManager();
                mFragmentManager.popBackStack();
                mFragmentManager.popBackStack();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.addToBackStack("DeliveryDetail");
                mFragmentTransaction.add(R.id.flOrderDetail,DeliveryDetailFragment_.builder().sOrder(sOrder).build()).commit();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAddressAdapter);
    }

    @AfterTextChange(R.id.edtSearchAddress)
    void setmEdtSearchAddress(){
        String tmp = mEdtSearchAddress.getText().toString();
        List<Address> addresses = LocationUtils.getAddressListFromAddress(getContext(), mEdtSearchAddress.getText().toString());
        if(addresses != null) {
            mUserAddresses.clear();
            for (Address address: addresses){
                UserAddress userAddress = new UserAddress();
                userAddress.setAddress(address.getAddressLine(0));
                userAddress.setLatitude(address.getLatitude());
                userAddress.setLongitude(address.getLongitude());
                mUserAddresses.add(userAddress);
                mAddressAdapter.notifyDataSetChanged();
            }
        }
    }
}

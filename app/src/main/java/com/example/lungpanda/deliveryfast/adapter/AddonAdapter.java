package com.example.lungpanda.deliveryfast.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.model.Store.AddOn;
import com.example.lungpanda.deliveryfast.model.Store.ProductAddOn;
import com.example.lungpanda.deliveryfast.ui.order.AddAddonDialog;

import java.util.List;

/**
 * Created by LungPanda on 11/7/2017.
 */

public class AddonAdapter extends RecyclerView.Adapter<AddonAdapter.MyViewHolder> {
    private List<AddOn> mAddOnList;
    private Context mContext;
    private Fragment mFragment;


    public AddonAdapter(List<AddOn> mAddOnList, Context mContext, Fragment mFragment) {
        this.mAddOnList = mAddOnList;
        this.mContext = mContext;
        this.mFragment = mFragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addon_list_header, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.bind(mAddOnList.get(position));
    }

    @Override
    public int getItemCount() {
        return mAddOnList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView mTvAddonHeader;
        final RecyclerView mRvAddonProducts;
        final TextView mTvRequired;
        private Context mContext;
        private AddonProductAdapter mAddonProductAdapter;

        public MyViewHolder(View view) {
            super(view);
            mTvAddonHeader = (TextView) view.findViewById(R.id.tvAddonHeader);
            mTvRequired = (TextView) view.findViewById(R.id.tvRequired);
            mRvAddonProducts = (RecyclerView) view.findViewById(R.id.addon_product_recycler_view);
            mContext = view.getContext();
        }

        public void bind(AddOn addOn) {
            final List<ProductAddOn> productAddOnList = addOn.getProductAddOns();

            mTvAddonHeader.setText(addOn.getName());

            if (addOn.getRole() == 1){
                mTvRequired.setVisibility(View.VISIBLE);
                addOn.getProductAddOns().get(0).toggleCheckState();
            } else {
                mTvRequired.setVisibility(View.GONE);
            }

            if (productAddOnList != null) {
                mRvAddonProducts.setVisibility(View.VISIBLE);
                mAddonProductAdapter = new AddonProductAdapter(productAddOnList, new AddonProductAdapter.OnItemClickListener() {
                    @Override
                    public void onRadioButtonClicked(ProductAddOn productAddOn) {
                        productAddOn.toggleCheckState();
                        mAddonProductAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCheckBoxButtonCicked(ProductAddOn productAddOn) {
                        if (productAddOn.isChecked()) {
                            productAddOn.setChecked(false);
                        } else {
                            productAddOn.setChecked(true);
                        }
                        mAddonProductAdapter.notifyDataSetChanged();
                        ((AddAddonDialog) mFragment).updateData();
                    }
                }, addOn.getRole());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                mRvAddonProducts.setLayoutManager(mLayoutManager);
                mRvAddonProducts.setItemAnimator(new DefaultItemAnimator());
                mRvAddonProducts.setAdapter(mAddonProductAdapter);
            } else {
                mRvAddonProducts.setVisibility(View.GONE);
            }
        }
    }
}

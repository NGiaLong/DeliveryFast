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
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.adapter.AddonAdapter;
import com.example.lungpanda.deliveryfast.adapter.DividerItemDecoration;
import com.example.lungpanda.deliveryfast.api.ApiClient;
import com.example.lungpanda.deliveryfast.model.Order.OrderDetail;
import com.example.lungpanda.deliveryfast.model.Store.AddOn;
import com.example.lungpanda.deliveryfast.model.Store.Product;
import com.example.lungpanda.deliveryfast.model.Store.ProductAddOn;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.Type;
import java.util.List;

import lombok.NonNull;

/**
 * Created by LungPanda on 11/7/2017.
 */
@EFragment(R.layout.dialog_add_addon)
public class AddAddonDialog extends DialogFragment {
    @ViewById(R.id.ivProductImage)
    ImageView mIvProductImage;
    @ViewById(R.id.tvProductName)
    TextView mTvProductName;
    @ViewById(R.id.tvProductPrice)
    TextView mTvProductPrice;
    @ViewById(R.id.ivDecreaseQuantity)
    ImageView mIvDecreaseQuantity;
    @ViewById(R.id.tvQuantity)
    TextView mTvQuantity;
    @ViewById(R.id.ivIncreaseQuantity)
    ImageView mIvIncreaseQuantity;
    @ViewById(R.id.addon_recycler_view)
    RecyclerView mRvAddon_recycler_view;
    @ViewById(R.id.llAddToCart)
    LinearLayout mLlAddToCart;
    @ViewById(R.id.tvTotalPrice)
    TextView mTvTotalPrice;

    @FragmentArg
    String product;
    @FragmentArg
    String addon;

    private Product mProduct;
    private List<AddOn> mAddOnList;
    private AddonAdapter mAddonAdapter;
    private OrderDetail mOrderDetail = new OrderDetail();
    private String baseUrl = ApiClient.getBaseUrl();

    @AfterViews
    void init() {
        Gson gson = new Gson();
        if (product != null && addon != null) {
            mProduct = gson.fromJson(product, Product.class);
            gson = new Gson();
            Type addonType = new TypeToken<List<AddOn>>() {
            }.getType();
            mAddOnList = gson.fromJson(addon, addonType);
        } else {
            dismiss();
        }
        if (mProduct != null) {
            mOrderDetail.setQuantity(1);
            mOrderDetail.setPrice(mProduct.getPrice());
            mOrderDetail.setUnit_price(mProduct.getPrice());
            mOrderDetail.setProduct_name(mProduct.getName());
            mOrderDetail.setPrice(mProduct.getPrice());
            if (mProduct.getImage_url() != null) {
                Picasso.with(getContext()).load(baseUrl + mProduct.getImage_url()).into(mIvProductImage);
            }
            mTvProductName.setText(mProduct.getName());
            mTvProductPrice.setText(mProduct.getPrice() + " đ");
            mTvQuantity.setText(mOrderDetail.getQuantity() + "");
            if (mAddOnList != null && mAddOnList.size() > 0) {
                for (int i = mAddOnList.size() - 1; i >= 0; i--) {
                    if (mAddOnList.get(i).getProductAddOns().size() == 0) {
                        mAddOnList.remove(mAddOnList.get(i));
                    }
                }
                mRvAddon_recycler_view.setVisibility(View.VISIBLE);
                mAddonAdapter = new AddonAdapter(mAddOnList, getActivity(), AddAddonDialog.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                mRvAddon_recycler_view.setLayoutManager(mLayoutManager);
                mRvAddon_recycler_view.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                mRvAddon_recycler_view.setAdapter(mAddonAdapter);
                mTvTotalPrice.setText(mOrderDetail.getPrice() + " đ");
            } else {
                mRvAddon_recycler_view.setVisibility(View.GONE);
            }
        } else {
            dismiss();
        }
    }

    @Click(R.id.llAddToCart)
    void setmLlAddToCart() {
        String detail = " ";
        for (AddOn addOn : mAddOnList) {
            for (ProductAddOn productAddOn : addOn.getProductAddOns()) {
                if (productAddOn.isChecked()) {
                    detail += productAddOn.getName() + " ,";
                }
            }
        }
        if (detail.contains(",")) {
            detail = detail.substring(0, detail.length() - 1);
        }
        mOrderDetail.setDetail(detail.trim());
        mOrderDetail.setProduct_id(mProduct.getId());
        mOrderDetail.setProduct_image_url(mProduct.getImage_url());
        ((OrderActivity) getActivity()).addOrderDetail(mOrderDetail);
        dismiss();
    }

    @Click(R.id.ivIncreaseQuantity)
    void setmIvIncreaseQuantity() {
        if (mOrderDetail.getQuantity() < 50) {
            mOrderDetail.setQuantity(mOrderDetail.getQuantity() + 1);
        }
        updateData();
    }

    @Click(R.id.ivDecreaseQuantity)
    void setmIvDecreaseQuantity() {
        if (mOrderDetail.getQuantity() == 1) {
            dismiss();
        } else {
            mOrderDetail.setQuantity(mOrderDetail.getQuantity() - 1);
        }
        updateData();
    }

    public void updateData() {
        int allAddonPrice = 0;
        if (mAddOnList != null && mAddOnList.size() > 0) {
            for (AddOn addOn : mAddOnList) {
                for (ProductAddOn productAddOn : addOn.getProductAddOns()) {
                    if (productAddOn.isChecked()) {
                        allAddonPrice += productAddOn.getPrice();
                    }
                }
            }
            mAddonAdapter.notifyDataSetChanged();
        }
        mOrderDetail.setUnit_price(mProduct.getPrice() + allAddonPrice);
        mTvQuantity.setText(mOrderDetail.getQuantity() + "");
        mOrderDetail.setPrice(mOrderDetail.getQuantity() * mOrderDetail.getUnit_price());
        mTvTotalPrice.setText(mOrderDetail.getPrice() + " đ");
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getDialog().getWindow() != null) {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = getWidthDialog();
            params.height = getHeighDialog();
            params.gravity = getPositionDialog();
            getDialog().getWindow().setAttributes(params);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        if (dialog.getWindow() != null) {
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

    protected int getHeighDialog() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        return height * 90 / 100;
    }

    protected int getPositionDialog() {
        return Gravity.CENTER;
    }
}

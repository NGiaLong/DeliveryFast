package com.example.lungpanda.deliveryfast.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.model.Order.OrderDetail;
import com.example.lungpanda.deliveryfast.model.Store.Category;
import com.example.lungpanda.deliveryfast.model.Store.Product;
import com.example.lungpanda.deliveryfast.ui.order.AddAddonDialog_;
import com.example.lungpanda.deliveryfast.ui.order.MinusAddonDialog_;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private List<Category> categoryList;
    private FragmentManager mFragmentManager;

    public CategoryAdapter(List<Category> categoryList, FragmentManager fragmentManager) {
        this.categoryList = categoryList;
        this.mFragmentManager = fragmentManager;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_header, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(categoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView mTvCategory;
        final RecyclerView mProducts;
        final CardView mProductHolder;
        private Context context;

        private ProductAdapter productAdapter;

        public MyViewHolder(View view) {
            super(view);
            mTvCategory = (TextView) view.findViewById(R.id.tvCategory);
            mProducts = (RecyclerView) view.findViewById(R.id.product_recycler_view);
            mProductHolder = (CardView) view.findViewById(R.id.cvProduct_recycler_view);
            context = view.getContext();
        }

        public void bind(Category category) {
            List<Product> proList = category.getProducts();
            mTvCategory.setText(category.getName());
            Log.i("TAG11", "BBBBBBBB" + proList.size());
            if (proList != null) {
                productAdapter = new ProductAdapter(proList, new ProductAdapter.OnItemClickListener() {
                    @Override
                    public void onIncreaseClick(Product product) {
                        Gson gson = new Gson();
                        String productString = gson.toJson(product);
                        String addonString = "";
                        for (Category tmp : categoryList){
                            if (tmp.getId().equals(product.getCategory_id())){
                                addonString = gson.toJson(tmp.getAddOn());
                            }
                        }
                        AddAddonDialog_.builder().product(productString).addon(addonString).build().show(mFragmentManager);
                    }

                    @Override
                    public void onDecreaseClick(Product product) {
                        Gson gson = new Gson();
                        String productString = gson.toJson(product);
                        MinusAddonDialog_.builder().product(productString).build().show(mFragmentManager);
                    }
                });
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                mProducts.setLayoutManager(mLayoutManager);
                mProducts.setItemAnimator(new DefaultItemAnimator());
                mProducts.setAdapter(productAdapter);
            }
        }
    }
}
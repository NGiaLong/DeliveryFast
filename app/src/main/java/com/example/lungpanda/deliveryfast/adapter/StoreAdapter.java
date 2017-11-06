package com.example.lungpanda.deliveryfast.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.model.Store.Category;
import com.example.lungpanda.deliveryfast.model.Store.Product;

import java.util.List;

/**
 * Created by LungPanda on 11/5/2017.
 */

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder> {
    public interface OnItemClickListener {
        void onIncreaseClick(Product product);

        void onDecreaseClick(Product product);
    }

    private final static int HEADER_VIEW = 0;
    private final static int CONTENT_VIEW = 1;

    private List<Category> categories;
    private final OnItemClickListener listener;

    public StoreAdapter(List<Category> categories, OnItemClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    public void add(Category category) {
        categories.add(category);
        notifyItemInserted(categories.size() - 1);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes = 0;
        switch (viewType) {
            case HEADER_VIEW:
                layoutRes = R.layout.category_list_header;
                break;
            case CONTENT_VIEW:
                layoutRes = R.layout.category_list_content;
                break;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemViewType(int position){
        switch(position) {
            case 0:
                return HEADER_VIEW;
            default:
                return CONTENT_VIEW;
        }
    }

    @Override
    public void onBindViewHolder(StoreAdapter.MyViewHolder holder, int position){
        holder.bind(getItemViewType(position), categories.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView mTvCategory,mTvProductName, mTvProductPrice, mTvQuantity;
        final ImageView mIvProductImage, mIvDecreaseQuantity, mIvIncreaseQuantity;

        public MyViewHolder(View view){
            super(view);
            mTvCategory = (TextView) view.findViewById(R.id.tvCategory);
            mTvProductName = (TextView) view.findViewById(R.id.tvProductName);
            mTvProductPrice = (TextView) view.findViewById(R.id.tvProductPrice);
            mTvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
            mIvProductImage = (ImageView) view.findViewById(R.id.ivProductImage);
            mIvDecreaseQuantity = (ImageView) view.findViewById(R.id.ivDecreaseQuantity);
            mIvIncreaseQuantity = (ImageView) view.findViewById(R.id.ivIncreaseQuantity);
        }

        public void bind(final int viewType, final Category category, final OnItemClickListener listener){
            if (viewType == 0){
                mTvCategory.setText(category.getName().toUpperCase());
            } else {

            }
        }
    }
}


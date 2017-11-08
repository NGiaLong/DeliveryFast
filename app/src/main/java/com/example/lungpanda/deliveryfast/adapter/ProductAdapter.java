package com.example.lungpanda.deliveryfast.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.api.ApiClient;
import com.example.lungpanda.deliveryfast.model.Order.OrderDetail;
import com.example.lungpanda.deliveryfast.model.Store.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by LungPanda on 11/6/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder>{
    final String baseUrl = ApiClient.getBaseUrl();
    private List<Product> productList;
    private final OnItemClickListener listener;

    public ProductAdapter(List<Product> productList, OnItemClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onIncreaseClick(Product product);

        void onDecreaseClick(Product product);
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_content, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(productList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView mTvProductName, mTvProductPrice, mTvQuantity;
        final ImageView mIvProductImage, mIvDecreaseQuantity, mIvIncreaseQuantity;
        final Context context;

        public MyViewHolder(View view){
            super(view);
            context = view.getContext();
            mTvProductName = (TextView) view.findViewById(R.id.tvProductName);
            mTvProductPrice = (TextView) view.findViewById(R.id.tvProductPrice);
            mTvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
            mIvProductImage = (ImageView) view.findViewById(R.id.ivProductImage);
            mIvDecreaseQuantity = (ImageView) view.findViewById(R.id.ivDecreaseQuantity);
            mIvIncreaseQuantity = (ImageView) view.findViewById(R.id.ivIncreaseQuantity);
        }
        public void changeView(boolean checked, final Product product){
            if (checked) {
                mIvDecreaseQuantity.setVisibility(View.VISIBLE);
                mIvDecreaseQuantity.setEnabled(true);
                mTvQuantity.setVisibility(View.VISIBLE);
                int tmpQuanlity = 0;
                for (OrderDetail orderDetail : product.getOrderDetails()){
                    tmpQuanlity += orderDetail.getQuanlity();
                }
                mTvQuantity.setText(tmpQuanlity+"");
            } else {
                mIvDecreaseQuantity.setVisibility(View.INVISIBLE);
                mIvDecreaseQuantity.setEnabled(false);
                mTvQuantity.setVisibility(View.INVISIBLE);
            }
        }
        public void bind(final Product product, final OnItemClickListener listener) {
            mTvProductName.setText(product.getName());
            mTvProductPrice.setText(product.getPrice()+" đ");
            if(product.getImage_url()!=null){
                Picasso.with(context).load(baseUrl+product.getImage_url()).into(mIvProductImage);
            }
            if(product.getOrderDetails() != null){
                if (product.getOrderDetails().size() == 0 ) {
                    changeView(false, product);
                } else {
                    changeView(true, product);
                }
            } else {
                changeView(false, product);
            }
            mIvIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onIncreaseClick(product);
                }
            });
            mIvDecreaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onDecreaseClick(product);
                }
            });
        }

    }
}

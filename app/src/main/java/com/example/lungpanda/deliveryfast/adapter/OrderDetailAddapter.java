package com.example.lungpanda.deliveryfast.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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
import java.util.zip.CheckedOutputStream;

/**
 * Created by LungPanda on 11/8/2017.
 */

public class OrderDetailAddapter extends RecyclerView.Adapter<OrderDetailAddapter.MyViewHolder> {
    public interface OnItemClickListener {
        void onIncreaseClick(OrderDetail orderDetail);

        void onDecreaseClick(OrderDetail orderDetail);
    }

    private List<OrderDetail> mOrderDetails;
    private String mProductImage;
    private String baseUrl = ApiClient.getBaseUrl();
    private OnItemClickListener mListener;

    public OrderDetailAddapter(List<OrderDetail> mOrderDetails,String mProductImage, OnItemClickListener mListener) {
        this.mOrderDetails = mOrderDetails;
        this.mProductImage = mProductImage;
        this.mListener = mListener;
    }

    @Override
    public OrderDetailAddapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderdetail_list_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderDetailAddapter.MyViewHolder holder, int position) {
        holder.bind(mOrderDetails.get(position));
    }

    @Override
    public int getItemCount() {
        return mOrderDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final ImageView mIvProductImage, mIvDecreaseQuantity, mIvIncreaseQuantity;
        final TextView mTvProductName, mTvOrDetailDetail, mTvOrDetailPrice, mTvQuantity;
        final Context context;
        public MyViewHolder(View view) {
            super(view);
            mIvProductImage = (ImageView) view.findViewById(R.id.ivProductImage);
            mIvDecreaseQuantity = (ImageView) view.findViewById(R.id.ivDecreaseQuantity);
            mIvIncreaseQuantity = (ImageView) view.findViewById(R.id.ivIncreaseQuantity);
            mTvProductName = (TextView) view.findViewById(R.id.tvProductName);
            mTvOrDetailDetail = (TextView) view.findViewById(R.id.tvOrDetailDetail);
            mTvOrDetailPrice = (TextView) view.findViewById(R.id.tvOrDetailPrice);
            mTvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
            context = view.getContext();
        }
        public void bind(final OrderDetail orderDetail){
            if(mProductImage!=null){
                Picasso.with(context).load(baseUrl+mProductImage).into(mIvProductImage);
            }
            mTvProductName.setText(orderDetail.getProduct_name());
            mTvOrDetailDetail.setText(orderDetail.getDetail());
            if (orderDetail.getQuanlity() > 0) {
                updateView(true, orderDetail);
            } else {
                updateView(false, orderDetail);
            }
            mTvOrDetailPrice.setText(orderDetail.getUnit_price() + " đ x " + orderDetail.getQuanlity() + " = " + (orderDetail.getUnit_price()*orderDetail.getQuanlity()) + " đ" );
            mIvIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onIncreaseClick(orderDetail);
                }
            });
            mIvDecreaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onDecreaseClick(orderDetail);
                }
            });
        }
        public void updateView(boolean checked, OrderDetail orderDetail){
            if (checked){
                mIvDecreaseQuantity.setVisibility(View.VISIBLE);
                mIvDecreaseQuantity.setEnabled(true);
                mTvQuantity.setVisibility(View.VISIBLE);
                mTvQuantity.setText(orderDetail.getQuanlity()+"");
            } else {
                mIvDecreaseQuantity.setVisibility(View.INVISIBLE);
                mIvDecreaseQuantity.setEnabled(false);
                mTvQuantity.setVisibility(View.INVISIBLE);
            }
        }
    }
}

package com.example.lungpanda.deliveryfast.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.model.Order.OrderDetail;

import java.util.List;

/**
 * Created by LungPanda on 11/10/2017.
 */

public class OrderViewAdapter extends RecyclerView.Adapter<OrderViewAdapter.MyViewHolder> {
    private List<OrderDetail> mOrderDetails;

    public OrderViewAdapter(List<OrderDetail> mOrderDetails) {
        this.mOrderDetails = mOrderDetails;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_content, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        OrderDetail mOrderDetail = mOrderDetails.get(position);
        holder.orderDetailQuantity.setText(mOrderDetail.getQuantity()+"");
        holder.productName.setText(mOrderDetail.getProduct_name());
        holder.orderDetailPrice.setText(mOrderDetail.getPrice() + " đ");
        if (!mOrderDetail.getDetail().equals("")){
            holder.orderDetailDetail.setText(mOrderDetail.getDetail());
        } else {
            holder.orderDetailDetail.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mOrderDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView productName, orderDetailQuantity, orderDetailPrice, orderDetailDetail;
        public MyViewHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.tvProductName);
            orderDetailQuantity = (TextView) itemView.findViewById(R.id.tvQuantity);
            orderDetailPrice = (TextView) itemView.findViewById(R.id.tvOrDetailPrice);
            orderDetailDetail = (TextView) itemView.findViewById(R.id.tvOrDetailDetail);
        }
    }
}

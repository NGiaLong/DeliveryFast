package com.example.lungpanda.deliveryfast.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.model.Order.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by LungPanda on 11/14/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Order order);
    }

    private final OnItemClickListener listener;
    private List<Order> mOrders;

    public HistoryAdapter(List<Order> mOrders, OnItemClickListener listener) {
        this.mOrders = mOrders;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_content, parent, false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(mOrders.get(position), listener);
    }


    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView storeName, storeAddress, orderId, orderDate, deliveryDate, orderInformation, orderStatus;
        final ImageView isSuccess;
        public MyViewHolder(View view) {
            super(view);
            storeName = (TextView) view.findViewById(R.id.tvStoreName);
            storeAddress = (TextView) view.findViewById(R.id.tvStoreAddress);
            orderId = (TextView) view.findViewById(R.id.tvOrderId);
            orderDate = (TextView) view.findViewById(R.id.tvOrderDate);
            deliveryDate = (TextView) view.findViewById(R.id.tvDeliveryDate);
            orderInformation = (TextView) view.findViewById(R.id.tvOrderInformation);
            orderStatus = (TextView) view.findViewById(R.id.tvOrderStatus);
            isSuccess = (ImageView) view.findViewById(R.id.ivIsSuccess);
        }

        public void bind(Order order, final OnItemClickListener listener) {
            storeName.setText(order.getStore().getName());
            storeAddress.setText(order.getStore().getAddress());
            orderId.setText(order.getId());
            Date date = new Date(order.getDelivery_date());
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            deliveryDate.setText(format.format(date.getTime()));
            date = new Date(order.getOrder_date());
            orderDate.setText(format.format(date.getTime()));
            orderInformation.setText(order.getTotal_amount() + " đ - " + order.getPayment());
            orderStatus.setText(order.getStatus());
//            if(order.getStatus().equals("Cancelled")){
//                isSuccess.setImageResource(R.drawable.cancel);
//            } else {
//                isSuccess.setImageResource(R.drawable.checked);
//            }
        }
    }
}

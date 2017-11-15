package com.example.lungpanda.deliveryfast.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.model.User.UserAddress;

import java.util.List;

/**
 * Created by LungPanda on 10/31/2017.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder>{
    public interface OnItemClickListener {
        void onItemClick(UserAddress userAddress);
    }

    private List<UserAddress> userAddresses;
    private final OnItemClickListener listener;

    public AddressAdapter(List<UserAddress> userAddresses, OnItemClickListener listener) {
        this.userAddresses = userAddresses;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_list_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override public void onBindViewHolder(AddressAdapter.MyViewHolder holder, int position) {
        holder.bind(userAddresses.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return userAddresses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView address;
        final ImageView isChecked;

        public MyViewHolder(View view){
            super(view);
            address = (TextView) view.findViewById(R.id.tvAddress);
            isChecked = (ImageView) view.findViewById(R.id.isChecked);
        }

        public void bind (final UserAddress userAddress, final OnItemClickListener listener){
            address.setText(userAddress.getAddress());
            if (userAddress.isChecked()){
                isChecked.setVisibility(View.VISIBLE);
            } else {
                isChecked.setVisibility(View.INVISIBLE);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(userAddress);
                }
            });
        }
    }
}

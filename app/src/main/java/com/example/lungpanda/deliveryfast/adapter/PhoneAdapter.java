package com.example.lungpanda.deliveryfast.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.model.User.UserPhone;

import java.util.List;

/**
 * Created by LungPanda on 10/27/2017.
 */

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.MyViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(UserPhone userPhone);
    }

    private List<UserPhone> userPhones;
    private final OnItemClickListener listener;

    public PhoneAdapter(List<UserPhone> userPhones, OnItemClickListener listener) {
        this.userPhones = userPhones;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_list_row, parent, false);
        return new MyViewHolder(v);

    }
    @Override public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(userPhones.get(position), listener);
    }


    @Override
    public int getItemCount() {
        return userPhones.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView phone;
        public ImageView primary_key;

        public MyViewHolder(View view) {
            super(view);
            phone = (TextView) view.findViewById(R.id.tvPhoneNumber);
            primary_key = (ImageView) view.findViewById(R.id.ivPrimaryKey);
        }

        public void bind(final UserPhone userPhone, final OnItemClickListener listener) {
            phone.setText(userPhone.getPhone_number());
            if (userPhone.isRole()) {
                primary_key.setVisibility(View.VISIBLE);
            } else {
                primary_key.setVisibility(View.INVISIBLE);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(userPhone);
                }
            });
        }
    }
}

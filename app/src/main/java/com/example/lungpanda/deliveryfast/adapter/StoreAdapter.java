package com.example.lungpanda.deliveryfast.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.model.Store.Store;
import com.example.lungpanda.deliveryfast.model.Store.StoreType;

import java.util.List;

/**
 * Created by LungPanda on 10/4/2017.
 */

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Store store);
    }

    private List<Store> storesList;
    private final OnItemClickListener listener;

    public StoreAdapter(List<Store> storesList, OnItemClickListener listener) {
        this.storesList = storesList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_list_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StoreAdapter.MyViewHolder holder, int position) {
        holder.bind(storesList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return storesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, address, store_type;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tvStoreName);
            address = (TextView) view.findViewById(R.id.tvStoreAddress);
            store_type = (TextView) view.findViewById(R.id.tvStoreType);
        }

        public void bind(final Store store, final OnItemClickListener listener) {
            name.setText(store.getName());
            address.setText(store.getAddress());
            if (store.getStoreTypes() == null) {
                store_type.setText("Unknown");
            } else if (store.getStoreTypes().size() == 0) {
                store_type.setText("Unknown");
            } else {
                String tmp = "";
                for (StoreType storeType : store.getStoreTypes()) {
                    tmp += storeType.getType() + "/";
                }
                tmp = tmp.substring(0, tmp.length() - 1);
                store_type.setText(tmp);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(store);
                }
            });
        }
    }
}

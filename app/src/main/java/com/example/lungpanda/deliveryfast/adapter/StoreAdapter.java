package com.example.lungpanda.deliveryfast.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.model.Store;

import java.util.List;
/**
 * Created by LungPanda on 10/4/2017.
 */

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder> {
    private List<Store> storesList;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name, address, store_type;
        public MyViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            address = (TextView) view.findViewById(R.id.address);
            store_type = (TextView) view.findViewById(R.id.store_type);
        }
    }
    public StoreAdapter(List<Store> storesList){
        this.storesList = storesList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Store store = storesList.get(position);
        holder.name.setText(store.getName());
        holder.address.setText(store.getAddress());
        holder.store_type.setText(store.getOpening_time());
    }

    @Override
    public int getItemCount() {
        return storesList.size();
    }
}

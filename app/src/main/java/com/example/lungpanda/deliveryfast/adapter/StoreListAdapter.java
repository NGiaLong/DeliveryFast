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
import com.example.lungpanda.deliveryfast.api.Api;
import com.example.lungpanda.deliveryfast.api.ApiClient;
import com.example.lungpanda.deliveryfast.model.Store.Store;
import com.example.lungpanda.deliveryfast.model.Store.StoreType;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by LungPanda on 10/4/2017.
 */

public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.MyViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Store store);
    }

    private List<Store> storesList;
    private final OnItemClickListener listener;

    public StoreListAdapter(List<Store> storesList, OnItemClickListener listener) {
        this.storesList = storesList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_list_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StoreListAdapter.MyViewHolder holder, int position) {
        holder.bind(storesList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return storesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, address, store_type;
        public ImageView store_image;
        LatLng mCurrentLocation, mStoreLocation;
        Context context;
        String baseUrl = ApiClient.getBaseUrl();

        public MyViewHolder(View view) {
            super(view);
            context = view.getContext();
            name = (TextView) view.findViewById(R.id.tvStoreName);
            address = (TextView) view.findViewById(R.id.tvStoreAddress);
            store_type = (TextView) view.findViewById(R.id.tvStoreType);
            store_image = (ImageView) view.findViewById(R.id.ivStoreImage);
        }

        public void bind(final Store store, final OnItemClickListener listener) {
//            Log.i("TEST123456", "Store: " + store);
            mStoreLocation = new LatLng((double) store.getLatitude(), (double) store.getLongitude());
            name.setText(store.getName());
            address.setText(store.getAddress());
            if(store.getImage_url() != null) {
                Picasso.with(context).load(baseUrl + store.getImage_url()).into(store_image);
            }
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

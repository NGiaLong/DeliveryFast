package com.example.lungpanda.deliveryfast.ui.home;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.adapter.StoreAdapter;
import com.example.lungpanda.deliveryfast.model.Store;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_store)
public class StoreActivity extends AppCompatActivity {
    private List<Store> storeList = new ArrayList<>();
    private StoreAdapter sAdapter;
    @ViewById (R.id.recycler_view)
    RecyclerView recyclerView;

    @Extra
    String username;
    @AfterViews
    void  init(){
        sAdapter = new StoreAdapter(storeList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(sAdapter);
        prepareStoreData();

    }

    private void prepareStoreData() {
        Store store = new Store("EMKAY Coffee", "07 Nguyễn Chí Thanh", "Coffee");
        storeList.add(store);
        store = new Store("EMKAY Coffee", "07 Nguyễn Chí Thanh", "Coffee");
        storeList.add(store);
        store = new Store("EMKAY Coffee", "07 Nguyễn Chí Thanh", "Coffee");
        storeList.add(store);
        store = new Store("EMKAY Coffee", "07 Nguyễn Chí Thanh", "Coffee");
        storeList.add(store);
        store = new Store("EMKAY Coffee", "07 Nguyễn Chí Thanh", "Coffee");
        storeList.add(store);
        store = new Store("EMKAY Coffee", "07 Nguyễn Chí Thanh", "Coffee");
        storeList.add(store);
        store = new Store("EMKAY Coffee", "07 Nguyễn Chí Thanh", "Coffee");
        storeList.add(store);
        store = new Store("EMKAY Coffee", "07 Nguyễn Chí Thanh", "Coffee");
        storeList.add(store);
        store = new Store("EMKAY Coffee", "07 Nguyễn Chí Thanh", "Coffee");
        storeList.add(store);
        store = new Store("EMKAY Coffee", "07 Nguyễn Chí Thanh", "Coffee");
        storeList.add(store);
        store = new Store("EMKAY Coffee", "07 Nguyễn Chí Thanh", "Coffee");
        storeList.add(store);
        store = new Store("EMKAY Coffee", "07 Nguyễn Chí Thanh", "Coffee");
        storeList.add(store);
        store = new Store("EMKAY Coffee", "07 Nguyễn Chí Thanh", "Coffee");
        storeList.add(store);
        store = new Store("EMKAY Coffee", "07 Nguyễn Chí Thanh", "Coffee");
        storeList.add(store);
        store = new Store("EMKAY Coffee", "07 Nguyễn Chí Thanh", "Coffee");
        storeList.add(store);
        store = new Store("EMKAY Coffee", "07 Nguyễn Chí Thanh", "Coffee");
        storeList.add(store);
        store = new Store("EMKAY Coffee", "07 Nguyễn Chí Thanh", "Coffee");
        storeList.add(store);
        sAdapter.notifyDataSetChanged();
    }
}

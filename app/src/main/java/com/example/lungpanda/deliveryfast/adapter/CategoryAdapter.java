package com.example.lungpanda.deliveryfast.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.model.Order.OrderDetail;
import com.example.lungpanda.deliveryfast.model.Store.Category;
import com.example.lungpanda.deliveryfast.model.Store.Product;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private List<Category> categoryList;
    private Context mContext;

    public CategoryAdapter(List<Category> categoryList, Context context) {
        this.categoryList = categoryList;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_header, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(categoryList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView mTvCategory;
        final RecyclerView mProducts;
        final CardView mProductHolder;
        private Context context;

        private ProductAdapter productAdapter;

        public MyViewHolder(View view) {
            super(view);
            mTvCategory = (TextView) view.findViewById(R.id.tvCategory);
            mProducts = (RecyclerView) view.findViewById(R.id.product_recycler_view);
            mProductHolder = (CardView) view.findViewById(R.id.cvProduct_recycler_view);
            context = view.getContext();
        }

        public void bind(Category category, final int position) {
            List<Product> proList = category.getProducts();
            mTvCategory.setText(category.getName());
            Log.i("TAG11", "BBBBBBBB" + proList.size());
            if (proList.size() == 0) {
                mProductHolder.setVisibility(View.GONE);
            } else {
                productAdapter = new ProductAdapter(proList, new ProductAdapter.OnItemClickListener() {
                    @Override
                    public void onIncreaseClick(Product product) {
                        List<OrderDetail> orderDetails = product.getOrderDetails();
                        List<OrderDetail> details = new ArrayList<>();
                        if (orderDetails == null) {
                            OrderDetail orderDetail = new OrderDetail("", product.getName(), "", 1, product.getPrice());
                            details.add(orderDetail);
                            product.setOrderDetails(details);
                            productAdapter.notifyDataSetChanged();
                        } else if (orderDetails.size() == 0) {
                            OrderDetail orderDetail = new OrderDetail("", product.getName(), "", 1, product.getPrice());
                            details.add(orderDetail);
                            product.setOrderDetails(details);
                            productAdapter.notifyDataSetChanged();
                        } else {
                            OrderDetail orderDetail = new OrderDetail("", product.getName(), "", 1, product.getPrice());
                            boolean check = true;
                            for (OrderDetail tmp : orderDetails) {
                                if (orderDetail.getDetail().equals(tmp.getDetail()) && check) {
                                    check = false;
                                    tmp.setQuanlity(tmp.getQuanlity() + 1);
                                    tmp.setPrice(tmp.getQuanlity() * product.getPrice());
                                }
                                details.add(tmp);
                            }
                            product.setOrderDetails(details);
                            productAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onDecreaseClick(Product product) {
                        List<OrderDetail> orderDetails = product.getOrderDetails();
                        List<OrderDetail> details = orderDetails;
                        boolean check = true;
                        for (OrderDetail detail : orderDetails){
                            if (detail.getQuanlity() == 1 && check){
                                details.remove(detail);
                                break;
                            } else if (detail.getQuanlity() > 1 && check) {
                                OrderDetail tmp = detail;
                                details.remove(detail);
                                tmp.setQuanlity(tmp.getQuanlity() - 1);
                                tmp.setPrice(tmp.getPrice()*tmp.getQuanlity()/(tmp.getQuanlity()+1));
                                details.add(tmp);
                                break;
                            }
                        }
                        product.setOrderDetails(details);
                        productAdapter.notifyDataSetChanged();
                    }
                });
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                mProducts.setLayoutManager(mLayoutManager);
                mProducts.setItemAnimator(new DefaultItemAnimator());
                mProducts.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
                mProducts.setAdapter(productAdapter);
            }
        }
    }
}
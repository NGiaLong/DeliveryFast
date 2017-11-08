package com.example.lungpanda.deliveryfast.model.Order;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by LungPanda on 11/6/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class OrderDetail {
    @SerializedName("id")
    private String id;
    @SerializedName("product_name")
    private String product_name;
    @SerializedName("detail")
    private String detail;
    @SerializedName("quanlity")
    private int quanlity;
    @SerializedName("price")
    private int price;
    private int unit_price;

    public OrderDetail() {
    }

    public boolean equals(OrderDetail orderDetail) {
        boolean checked = false;
        if (this.getProduct_name().equals(orderDetail.getProduct_name())) {
            if (this.getPrice() / this.getQuanlity() == orderDetail.getPrice() / orderDetail.getQuanlity()) {
                if (this.getDetail().equals(orderDetail.getDetail())) {
                    checked = true;
                }
            }
        }
        return checked;
    }

}

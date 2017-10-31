package com.example.lungpanda.deliveryfast.model;

import lombok.Data;

/**
 * Created by LungPanda on 10/4/2017.
 */
@Data
public class Store {
    private String id, name, address, phone_number, opening_time, closing_time, lattitude, longitude;
    private Boolean status;


    public Store(String id, String name, String address, String phone_number, String opening_time, String closing_time, String lattitude, String longitude, Boolean status) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone_number = phone_number;
        this.opening_time = opening_time;
        this.closing_time = closing_time;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.status = status;
    }

    public Store() {
    }

    public Store(String name, String address, String opening_time) {
        this.name = name;
        this.address = address;
        this.opening_time = opening_time;
    }
}

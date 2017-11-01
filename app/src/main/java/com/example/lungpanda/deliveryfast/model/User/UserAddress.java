package com.example.lungpanda.deliveryfast.model.User;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by LungPanda on 10/13/2017.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
@Data
public class UserAddress implements Parcelable {
    public static final Creator<UserAddress> CREATOR = new Creator<UserAddress>() {
        @Override
        public UserAddress createFromParcel(Parcel parcel) {
            return new UserAddress(parcel);
        }

        @Override
        public UserAddress[] newArray(int size) {
            return new UserAddress[size];
        }
    };

    @SerializedName("id")
    private String id;
    @SerializedName("address")
    private String address;
    @SerializedName("latitude")
    private long latitude;
    @SerializedName("longitude")
    private long longitude;
    @SerializedName("user_id")
    private String user_id;

    public UserAddress() {
    }

    protected UserAddress(Parcel parcel){
        id = parcel.readString();
        address = parcel.readString();
        latitude = parcel.readLong();
        longitude = parcel.readLong();
        user_id = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeString(id);
        parcel.writeString(address);
        parcel.writeLong(latitude);
        parcel.writeLong(longitude);
        parcel.writeString(user_id);
    }
}

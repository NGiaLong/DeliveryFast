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
public class UserPhone implements Parcelable {
    public static final Creator<UserPhone> CREATOR = new Creator<UserPhone>() {
        @Override
        public UserPhone createFromParcel(Parcel in) {
            return new UserPhone(in);
        }

        @Override
        public UserPhone[] newArray(int size) {
            return new UserPhone[size];
        }
    };

    @SerializedName("id")
    private String id;
    @SerializedName("phone_number")
    private String phone_number;
    @SerializedName("role")
    private boolean role;
    @SerializedName("user_id")
    private String user_id;

    public UserPhone() {
    }

    protected UserPhone(Parcel in){
        id = in.readString();
        phone_number = in.readString();
        role = in.readByte() != 0;
        user_id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeString(id);
        parcel.writeString(phone_number);
        parcel.writeByte((byte) (role ? 1 : 0));
        parcel.writeString(user_id);
    }
}

package com.example.lungpanda.deliveryfast.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by LungPanda on 11/5/2017.
 */

public class LocationUtils {
    public static boolean isCheckGPS(Context context) {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static LatLng getLocationFromAddress(Context context, String location) {
        List<Address> addressList = null;
        Address address = null;
        try {
            if (location != null || !location.equals("")) {
                Geocoder geocoder = new Geocoder(context);
                try {
                    addressList = geocoder.getFromLocationName(location+ ", Da Nang, Viet Nam", 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                address = addressList.get(0);

            }
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            return latLng;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Address> getAddressListFromAddress(Context context, String location) {
        List<Address> addressList = null;
        try {
            if (location != null || !location.equals("")) {
                Geocoder geocoder = new Geocoder(context);
                try {
                    addressList = geocoder.getFromLocationName(location + ", Da Nang, Viet Nam", 10);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return addressList;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getAddressNameFromLocation(Context context, LatLng latLng) {
        String address = "";
        try {
            if (latLng != null) {
                Geocoder geocoder = new Geocoder(context);
                try {
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addresses != null){
                        address = addresses.get(0).getAddressLine(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return address;
        } catch (Exception e1) {
            return null;
        }

    }

}

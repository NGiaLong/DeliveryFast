package com.example.lungpanda.deliveryfast.ui.account;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.lungpanda.deliveryfast.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import lombok.NonNull;

/**
 * Created by LungPanda on 11/5/2017.
 */

@EFragment(R.layout.dialog_map)
public class MapDialog extends DialogFragment implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener {
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;
    private GoogleMap mMap;
    private View mMapView;
    private Marker mMarker;
    private boolean mLocationPermissionGranted;
    @ViewById(R.id.btnOk)
    Button mBtnOk;
    @FragmentArg
    LatLng mCurrentLocation;
    private SupportMapFragment mMapFragment;

    @AfterViews
    void init(){
        mMapFragment = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        mMapView = mMapFragment.getView();
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() instanceof AddressCreateActivity){
                   ((AddressCreateActivity) getActivity()).setLocation(mMarker.getPosition());
                }
                mMarker.remove();
                mMap.clear();
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        getFragmentManager().beginTransaction().remove(mMapFragment).commit();
        super.onDismiss(dialog);
    }

    public void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        if (getActivity().checkSelfPermission(
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
           requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION == requestCode) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateLocationUI();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * Set width & height of dialog on onResume method follow this
         * http://stackoverflow.com/questions/14946887/setting-the-size-of-a-dialogfragment
         */
        if (getDialog().getWindow() != null) {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = getWidthDialog();
            params.gravity = getPositionDialog();
            getDialog().getWindow().setAttributes(params);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        if (dialog.getWindow() != null) {
//            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return dialog;
    }

    public void show(FragmentManager manager) {
        if (manager != null && manager.beginTransaction() != null) {
            super.show(manager, "");
        }
    }

    public boolean isShow(DialogFragment manager) {
        if (manager != null) {
            return true;
        }
        return false;
    }

    protected int getWidthDialog() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width*95/100;
    }

    protected int getPositionDialog(){
        return Gravity.CENTER;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            updateLocationUI();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, 14));
            mMarker=mMap.addMarker(new MarkerOptions()
                    .position(mCurrentLocation)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));
            mMap.setOnCameraMoveListener(this);
        }
    }

    @Override
    public void onCameraMove() {
        mMarker.setPosition(mMap.getCameraPosition().target);
    }
}
package com.example.lungpanda.deliveryfast.ui.account;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.api.Api;
import com.example.lungpanda.deliveryfast.api.ApiClient;
import com.example.lungpanda.deliveryfast.model.User.UserAddress;
import com.example.lungpanda.deliveryfast.model.User.UserAddressResult;
import com.example.lungpanda.deliveryfast.utils.LocationUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.ViewById;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EActivity(R.layout.activity_address_create)
public class AddressCreateActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;
    private GoogleMap mMap;
    @ViewById(R.id.edtAddressName)
    EditText mEdtAddressName;
    @ViewById(R.id.imgBack)
    ImageView mImgBack;
    @ViewById(R.id.tvSaveCreateAddress)
    TextView mTvSaveCreateAddress;

    @Extra
    String id_token;

    private boolean mLocationPermissionGranted;
    LatLng mLatLng;
    private Marker mMarker;
    private GoogleApiClient mGoogleApiClient;
    private UserAddress userAddress = new UserAddress();
    ;

    @AfterViews
    void init() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        buildGoogleApiClient();
        mEdtAddressName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    setmEdtAddressName();
                    handled = true;
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                    }
                }
                return handled;

            }
        });
    }

//    @Change(R.id.edtAddressName)
    void setmEdtAddressName() {
        LatLng location = LocationUtils.getLocationFromAddress(getApplicationContext(), mEdtAddressName.getText().toString());
        if (location != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14));
            mMarker.setPosition(location);
            mLatLng = new LatLng(location.latitude, location.longitude);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null && isLocationChange(location) && LocationUtils.isCheckGPS(this)) {
            mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLatLng.latitude, mLatLng.longitude), 14));
            if (mMarker != null) {
                mMarker.remove();
            }
            mMarker = mMap.addMarker(new MarkerOptions()
                    .position(mLatLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_circle)));
        }
    }

    private boolean isLocationChange(Location location) {
        return location != null || mLatLng == null || mLatLng.latitude != location.getLatitude() || mLatLng.longitude != location.getLongitude();
    }


    @Click(R.id.imgBack)
    public void setmImgBack() {
        onBackPressed();
    }

    @Click(R.id.tvSaveCreateAddress)
    void setmTvSaveCreateAddress() {
        if (mEdtAddressName.getText().toString() == null) {
            mEdtAddressName.setError("Enter your address please");
            mEdtAddressName.requestFocus();
        } else {
            String text = mEdtAddressName.getText().toString();
            userAddress.setAddress(text);
            userAddress.setLatitude((float) mLatLng.latitude);
            userAddress.setLongitude((float) mLatLng.longitude);
            Api api = ApiClient.retrofit().create(Api.class);
            Call<UserAddressResult> result = api.createAddress("application/json", "Bearer " + id_token, userAddress);
            result.enqueue(new Callback<UserAddressResult>() {
                @Override
                public void onResponse(Call<UserAddressResult> call, Response<UserAddressResult> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            AddressActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
                            Toast.makeText(getApplication(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            onBackPressed();
                            Toast.makeText(getApplication(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        onBackPressed();
                        Toast.makeText(getApplication(), "Server is not working", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserAddressResult> call, Throwable t) {
                    onBackPressed();
                    Toast.makeText(getApplication(), "Dead", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Click(R.id.btnEditPin)
    void onAddressOnMapsClick() {
        MapDialog_.builder().mCurrentLocation(mLatLng).build().show(getSupportFragmentManager());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            updateLocationUI();
        }
    }

    public void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            if (LocationUtils.isCheckGPS(this)) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Vui lòng bật GPS để xác thực vị trí", Toast.LENGTH_LONG).show();
            }
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

    public void setLocation(LatLng latLng) {
        mLatLng = latLng;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 14));
        mMarker.setPosition(mLatLng);
    }

    public void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@android.support.annotation.NonNull ConnectionResult connectionResult) {

    }
}

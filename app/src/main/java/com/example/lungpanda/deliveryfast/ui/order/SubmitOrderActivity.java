package com.example.lungpanda.deliveryfast.ui.order;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.api.ApiClient;
import com.example.lungpanda.deliveryfast.api.OrderApi;
import com.example.lungpanda.deliveryfast.model.Order.DiscountResult;
import com.example.lungpanda.deliveryfast.model.Order.Order;
import com.example.lungpanda.deliveryfast.model.Order.OrderDetail;
import com.example.lungpanda.deliveryfast.model.Order.OrderResult;
import com.example.lungpanda.deliveryfast.model.Store.Store;
import com.example.lungpanda.deliveryfast.model.User.User;
import com.example.lungpanda.deliveryfast.model.User.UserPhone;
import com.example.lungpanda.deliveryfast.ui.account.SignInActivity;
import com.example.lungpanda.deliveryfast.ui.home.HomeActivity;
import com.example.lungpanda.deliveryfast.ui.home.HomeActivity_;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EActivity(R.layout.activity_submit_order)
public class SubmitOrderActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    @ViewById(R.id.rlContainer)
    RelativeLayout mRlContainer;
    @ViewById(R.id.progressBarLocation)
    ProgressBar mProgressBarLocation;
    @ViewById(R.id.flOrderDetail)
    FrameLayout mFlOrderDetail;
    @ViewById(R.id.trAllItem)
    TableRow mTrAllItem;
    @ViewById(R.id.tvStoreName)
    TextView mTvStoreName;
    @ViewById(R.id.rlSubmitOrder)
    RelativeLayout mRlSubmitOrder;
    @ViewById(R.id.tvNumberItem)
    TextView mTvNumberItem;
    @ViewById(R.id.tvTotalAmountDetail)
    TextView mTvTotalAmountDetail;
    @ViewById(R.id.tvShipFee)
    TextView mTvShipFee;
    @ViewById(R.id.tvDiscountAmount)
    TextView mTvDiscountAmount;
    @ViewById(R.id.tvTotalAmount)
    TextView mTvTotalAmount;
    @ViewById(R.id.tvTotalAmount2)
    TextView mTvTotalAmount2;
    @ViewById(R.id.tvDistance)
    TextView mTvDistance;
    @ViewById(R.id.btnEnterCode)
    Button mBtnEnterCode;
    @ViewById(R.id.flView)
    FrameLayout mFlView;
    @ViewById(R.id.imgBack)
    ImageView mImgBack;
    @ViewById(R.id.tvEditDeliveryDetail)
    TextView mTvEditDeliveryDetail;
    @ViewById(R.id.progressBar)
    ProgressBar mProgressBar;
    @ViewById(R.id.tvDis)
    TextView mTvDis;
    @ViewById(R.id.tvDestination)
    TextView mTvDestination;
    @ViewById(R.id.tvStoreAddress)
    TextView mTvStoreAddress;
    @ViewById(R.id.tvUserInfor)
    TextView mTvUserInfor;
    @ViewById(R.id.tvUserAddress)
    TextView mTvUserAddress;
    @ViewById(R.id.tvDeliveryDate)
    TextView mTvDeliveryDate;
    @ViewById(R.id.svContent)
    ScrollView mSvContent;
    private int numberItems = 0, totalAmountDetail = 0, shipFee = 0, discountAmount = 0, totalAmount = 0;
    private float distance = 0;
    private String mNote = "";
    private int discountPercent = 0;
    private boolean isDiscount = false;
    private String sStoreName;
    private long mLastClickTime = 0;

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private Marker mMarker;
    private GoogleApiClient mGoogleApiClient;
    private Polyline mPolyline;

    @Extra
    String sStoreId;
    @Extra
    String sOrderId;

    private Order mOrder;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private List<OrderDetail> mOrderDetailList = new ArrayList<>();
    private User mUser;
    private Store mStore;
    private String id_token;
    private LatLng origin, destination;
    private int i = 0;

    @AfterViews
    void init() {

        SharedPreferences settings = getSharedPreferences(SignInActivity.NAME_SHAREPREFERENCE, 0);
        id_token = settings.getString(SignInActivity.ID_TOKEN, "");
        getOrder();
        //
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.flLocation);
        mapFragment.getMapAsync(this);
        buildGoogleApiClient();
    }

    @Click(R.id.trAllItem)
    void setmTrAllItem() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if (mOrderDetailList.size() > 0) {
            mFlOrderDetail.setVisibility(View.VISIBLE);
            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.addToBackStack("abc");
            mFragmentTransaction.add(R.id.flOrderDetail, OrderDetailFragment_.builder().sStore_id(sStoreId).sStoreName(sStoreName).sOrderDetail(new Gson().toJson(mOrderDetailList)).build()).commit();
        }
    }

    @Click(R.id.rlSubmitOrder)
    void setmRlSubmitOrder() {
        mRlSubmitOrder.setEnabled(false);
        mProgressBar.setVisibility(View.VISIBLE);
        mOrder.setNote(mNote);
        mOrder.setStatus("Order Submitted");
        mOrder.setUser(null);
        mOrder.setStore(null);
        mOrder.setOrderDetails(mOrderDetailList);
        Log.i("FINALORDER123", "onBackPressed: " + new Gson().toJson(mOrder));
        OrderApi api = ApiClient.retrofit().create(OrderApi.class);
        Call<OrderResult> resultCall = api.saveOrder(mOrder.getId(), "application/json", "Bearer " + id_token, mOrder);
        resultCall.enqueue(new Callback<OrderResult>() {
            @Override
            public void onResponse(Call<OrderResult> call, Response<OrderResult> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        HomeActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_CLEAR_TASK).start();
                        Toast.makeText(getApplication(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplication(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        mRlSubmitOrder.setEnabled(true);
                        if (mProgressBar!= null ){
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }
                } else {
                    HomeActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_CLEAR_TASK).start();
                    Toast.makeText(getApplication(), "Server is not working", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResult> call, Throwable t) {
                HomeActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_CLEAR_TASK).start();
                Toast.makeText(getApplication(), "Server is not working", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void updateAllPriceView() {
        mProgressBar.setVisibility(View.VISIBLE);
        mSvContent.setVisibility(View.GONE);
        mRlSubmitOrder.setEnabled(false);

        Date date = new Date(mOrder.getDelivery_date());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        mTvDeliveryDate.setText(format.format(date));
        numberItems = totalAmountDetail = discountAmount = totalAmount = 0;
        if (destination != null) {
            if (mOrder.getUser_address() == null) {
                mOrder.setUser_address(LocationUtils.getAddressNameFromLocation(getApplicationContext(), destination));
                mOrder.setLatitude((float) destination.latitude);
                mOrder.setLongitude((float) destination.longitude);
                mTvUserAddress.setText(mOrder.getUser_address());
            } else if (!mOrder.getUser_address().equals("")){
                mTvUserAddress.setText(mOrder.getUser_address());
                updateLocation();
            }
        }
        if (mOrder.getUser_phone() != null && mOrder.getUser_name() != null) {
            mTvUserInfor.setText(mOrder.getUser_name() + " - " + mOrder.getUser_phone());
        }
        for (OrderDetail orderDetail : mOrderDetailList) {
            numberItems += orderDetail.getQuantity();
            totalAmountDetail += orderDetail.getPrice();
        }
        switch (numberItems) {
            case 1:
                mTvNumberItem.setText("Total 1 item");
                break;
            default:
                mTvNumberItem.setText("Total " + numberItems + " items");
        }
        mTvTotalAmountDetail.setText(totalAmountDetail + " đ");

        if (isDiscount) {
            //Nếu nhập đúng mã code
            mTvDiscountAmount.setVisibility(View.VISIBLE);
            discountAmount = (totalAmountDetail + shipFee) * discountPercent / 100;
            mTvDiscountAmount.setText("- " + discountAmount + "đ");
        } else {
            discountAmount = 0;
            mTvDiscountAmount.setVisibility(View.INVISIBLE);
        }
        totalAmount = totalAmountDetail + shipFee - discountAmount;
        mTvTotalAmount.setText(totalAmount + " đ");
        mTvTotalAmount2.setText(totalAmount + " đ");
        mOrder.setTotal_amount(totalAmount);
        //Update Fragment Detail View
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.flView, OrderViewFragment_.builder().sOrderDetail(new Gson().toJson(mOrderDetailList)).itemAmount(numberItems).build()).commit();
        mProgressBar.setVisibility(View.GONE);
        mSvContent.setVisibility(View.VISIBLE);
        mRlSubmitOrder.setEnabled(true);
    }

    public void updateOrder(List<OrderDetail> orderDetails) {
        mOrderDetailList.clear();
        mOrderDetailList.addAll(orderDetails);
        updateAllPriceView();
    }

    @Click(R.id.btnEnterCode)
    void setmBtnEnterCode() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if (mBtnEnterCode.getText().toString().equals("Enter code")) {
            // Creating alert Dialog with one Button
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SubmitOrderActivity.this);
            //AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

            // Setting Dialog Title
            alertDialog.setTitle("Enter promotion code");
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            input.setHint("Enter code");
            input.setGravity(Gravity.CENTER_VERTICAL);
            input.setMaxLines(1);
            input.setPadding(80, 50, 80, 0);
            input.setBackgroundColor(Color.WHITE);
            alertDialog.setView(input);
            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
//                            if (input.getText().toString().equals("GIAMGIA")) {
//                                isDiscount = true;
//                                discountPercent = 30;
//                                mBtnEnterCode.setText("GIAMGIA");
//                                mBtnEnterCode.setEnabled(false);
//                                updateAllPriceView();
//                            } else {
//                                Toast.makeText(getApplicationContext(), "Code is not valid", Toast.LENGTH_LONG).show();
//                            }
                            OrderApi orderApi = ApiClient.retrofit().create(OrderApi.class);
                            Call<DiscountResult> discountResultCall = orderApi.getDiscount(mOrder.getStore().getId(),input.getText().toString(),"application/json","Bearer " + id_token);
                            discountResultCall.enqueue(new Callback<DiscountResult>() {
                                @Override
                                public void onResponse(Call<DiscountResult> call, Response<DiscountResult> response) {
                                    if (response.isSuccessful()){
                                        isDiscount = response.body().isStatus();
                                        if (response.body().isStatus()){
                                            discountPercent = response.body().getDiscountData().getDiscountPercent();
                                            mBtnEnterCode.setText(input.getText().toString());
                                        }
                                        updateAllPriceView();
                                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<DiscountResult> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), "Server is not working", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
            alertDialog.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();

        }
    }

    @Click(R.id.imgBack)
    void setmImgBack() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        onBackPressed();
    }

    public void updateOrder(Order order) {
        mOrder = order;
        updateLocation();
        updateAllPriceView();
    }

    @Override
    public void onBackPressed() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            mOrder.setOrderDetails(mOrderDetailList);
            Log.i("", "onBackPressed: " + new Gson().toJson(mOrder));
            OrderApi api = ApiClient.retrofit().create(OrderApi.class);
            Call<OrderResult> resultCall = api.saveOrder(mOrder.getId(), "application/json", "Bearer " + id_token, mOrder);
            resultCall.enqueue(new Callback<OrderResult>() {
                @Override
                public void onResponse(Call<OrderResult> call, Response<OrderResult> response) {
                    if (response.isSuccessful()) {
                        OrderActivity_.intent(getApplicationContext()).store_id(sStoreId).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
                    } else {
                        OrderActivity_.intent(getApplicationContext()).store_id(sStoreId).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
                        Toast.makeText(getApplication(), "Server is not working", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<OrderResult> call, Throwable t) {
                    OrderActivity_.intent(getApplicationContext()).store_id(sStoreId).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
                }
            });
        }
    }

    public void resetOrder() {
        mOrderDetailList.clear();
        mOrder.setOrderDetails(mOrderDetailList);
        OrderApi api = ApiClient.retrofit().create(OrderApi.class);
        Call<OrderResult> resultCall = api.saveOrder(mOrder.getId(), "application/json", "Bearer " + id_token, mOrder);
        resultCall.enqueue(new Callback<OrderResult>() {
            @Override
            public void onResponse(Call<OrderResult> call, Response<OrderResult> response) {
                if (response.isSuccessful()) {
                    OrderActivity_.intent(getApplicationContext()).store_id(sStoreId).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
                } else {
                    OrderActivity_.intent(getApplicationContext()).store_id(sStoreId).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
                    Toast.makeText(getApplication(), "Server is not working", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResult> call, Throwable t) {
                OrderActivity_.intent(getApplicationContext()).store_id(sStoreId).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
            }
        });
    }

    @Click(R.id.tvEditDeliveryDetail)
    void setmTvEditDeliveryDetail() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        mFlOrderDetail.setVisibility(View.VISIBLE);
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.addToBackStack("DeliveryDetail");
//        mOrder.setStore(null);
        mFragmentTransaction.replace(R.id.flOrderDetail, DeliveryDetailFragment_.builder().sOrder(new Gson().toJson(mOrder)).build()).commit();
    }

    public void getOrder() {
        mProgressBar.setVisibility(View.VISIBLE);
        mSvContent.setVisibility(View.GONE);
        mRlContainer.setEnabled(false);
        if (sOrderId != null && !sOrderId.equals("")) {
            OrderApi orderApi = ApiClient.retrofit().create(OrderApi.class);
            Call<OrderResult> result = orderApi.getOrder(sOrderId, "application/json", "Bearer " + id_token);
            result.enqueue(new Callback<OrderResult>() {
                @Override
                public void onResponse(Call<OrderResult> call, Response<OrderResult> response) {
                    if (response.isSuccessful()) {
                        if (response.body().isStatus()) {
                            mOrder = response.body().getOrderData().getOrder();
                            Log.i("ORDER111", "onResponse: " + new Gson().toJson(mOrder));
                            sStoreName = mOrder.getStore().getName();
                            mTvStoreName.setText(sStoreName);
                            mTvDestination.setText(sStoreName);
                            mTvStoreAddress.setText(mOrder.getStore().getAddress());
                            mUser = mOrder.getUser();
                            if (mOrder.getUser_phone() == null || mOrder.getUser_phone().equals("")) {
                                if (mUser.getUserPhones() != null && mUser.getUserPhones().size() > 0) {
                                    for (UserPhone userPhone : mUser.getUserPhones()) {
                                        if (userPhone.isRole()) {
                                            mOrder.setUser_phone(userPhone.getPhone_number());
                                            break;
                                        }
                                    }
                                } else {
                                    mOrder.setUser_phone("");
                                }
                            }
                            mStore = mOrder.getStore();
                            if (mOrder.getUser_name() == null || mOrder.getUser_phone().equals("")) {
                                if (mUser.getFirst_name() != null && !mUser.getFirst_name().equals("") && mUser.getLast_name() != null && !mUser.getLast_name().equals("")) {
                                    mOrder.setUser_name(mUser.getFirst_name().trim() + " " + mUser.getLast_name().trim());
                                } else {
                                    mOrder.setUser_name("");
                                }
                            }
                            mTvUserInfor.setText(mOrder.getUser_name() + " - " + mOrder.getUser_phone());
                            origin = new LatLng(mStore.getLatitude(), mStore.getLongitude());

                            //Gắn date
                            Calendar calendar = Calendar.getInstance();
                            mOrder.setOrder_date(calendar.getTime().toString());
                            int today = calendar.get(Calendar.DATE);
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            int round = calendar.get(Calendar.MINUTE) % 15;
                            calendar.add(Calendar.MINUTE, round < 8 ? -round : (15 - round));
                            calendar.add(Calendar.MINUTE, 45);
                            calendar.set(Calendar.SECOND, 0);
                            int startHour, finishHour;
                            startHour = Integer.parseInt(mOrder.getStore().getOpening_time().split(":")[0]);
                            finishHour = Integer.parseInt(mOrder.getStore().getClosing_time().split(":")[0]);
                            if (calendar.get(calendar.HOUR_OF_DAY) >= startHour && calendar.get(calendar.HOUR_OF_DAY) < finishHour) {
                                mTvDeliveryDate.setText(format.format(calendar.getTime()));
                                mOrder.setDelivery_date(calendar.getTime().toString());
                            } else {
                                if (today == calendar.get(Calendar.DATE)) {
                                    calendar.add(Calendar.DATE, 1);
                                }
                                calendar.set(Calendar.HOUR_OF_DAY, startHour);
                                calendar.set(Calendar.MINUTE, 0);
                                mTvDeliveryDate.setText(format.format(calendar.getTime()));
                                mOrder.setDelivery_date(calendar.getTime().toString());
                            }
//                            String deliDate = calendar.getTime().getHours() + ":" + calendar.getTime().getMinutes() + " - " + calendar.getTime().getDate() + "/" + (calendar.getTime().getMonth() + 1);
                            if (destination != null){
                                mOrder.setLatitude((float) destination.latitude);
                                mOrder.setLongitude((float) destination.longitude);
                                mOrder.setUser_address(LocationUtils.getAddressNameFromLocation(getApplicationContext(), destination));
                            }
                            updateLocation();
                            updateOrder(mOrder.getOrderDetails());
                            if (mProgressBar != null) {
                                mProgressBar.setVisibility(View.GONE);
                            }
                            mSvContent.setVisibility(View.VISIBLE);
                            mRlContainer.setEnabled(true);
                        } else {
                            OrderActivity_.intent(getApplicationContext()).store_id(sStoreId).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        OrderActivity_.intent(getApplicationContext()).store_id(sStoreId).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
                        Toast.makeText(getApplicationContext(), "Server is not working!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<OrderResult> call, Throwable t) {
                    OrderActivity_.intent(getApplicationContext()).store_id(sStoreId).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
                    Toast.makeText(getApplicationContext(), "Server is not working!", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            onBackPressed();
        }
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
                                           @lombok.NonNull String permissions[],
                                           @lombok.NonNull int[] grantResults) {
        if (PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION == requestCode) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateLocationUI();
            }
        }
    }

    public void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
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
            destination = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(destination.latitude, destination.longitude), 11));
            if (mMarker != null) {
                mMarker.remove();
            }
            mMarker = mMap.addMarker(new MarkerOptions()
                    .position(destination)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.detinationpin)));

        }
    }

    private boolean isLocationChange(Location location) {
        return location != null || destination == null || destination.latitude != location.getLatitude() || destination.longitude != location.getLongitude();
    }

    private void createPolyline() {
        Log.i("UPDATEVIEW111", "setmRlSubmitOrder: " + i++);
        mProgressBarLocation.setVisibility(View.VISIBLE);
        if (origin != null) {
            mMap.addMarker(new MarkerOptions()
                    .position(origin)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.originpin)));
            GoogleDirection.withServerKey("AIzaSyBDcRa6b6gyjR8nnRJImLOdsCFQ8gsQTqE")
                    .from(origin)
                    .to(destination)
                    .transportMode(TransportMode.DRIVING)
                    .execute(new DirectionCallback() {
                        @Override
                        public void onDirectionSuccess(Direction direction, String rawBody) {
                            ArrayList<LatLng> directionPositionList;
                            if (mPolyline != null) {
                                mPolyline.remove();
                            }
                            directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
                            mPolyline = mMap.addPolyline(DirectionConverter.createPolyline(SubmitOrderActivity.this, directionPositionList, 3, Color.GREEN));
                            LatLngBounds.Builder builder = LatLngBounds.builder();
                            builder.include(origin);
                            builder.include(destination);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200));
                            mTvDis.setText(direction.getRouteList().get(0).getLegList().get(0).getDistance().getText());
                            mTvDistance.setText(mTvDis.getText().toString());
                            try {
                                String sDistance = mTvDistance.getText().toString();
                                if (sDistance.contains("km")) {
                                    sDistance = sDistance.substring(0, sDistance.length() - 2).trim();
                                    distance = Float.parseFloat(sDistance);
                                    shipFee = (int) Math.round(distance * 5000);
                                    mOrder.setShip_fee(shipFee);
                                    mTvShipFee.setText(shipFee + " đ");
                                } else {
                                    sDistance = sDistance.substring(0, sDistance.length() - 1).trim();
                                    distance = Float.parseFloat(sDistance) / 1000;
                                    shipFee = (int) Math.round(distance * 5000);
                                    mOrder.setShip_fee(shipFee);
                                    mTvShipFee.setText(shipFee + " đ");
                                }
                            } catch (Exception e) {
                            }
                            totalAmount = totalAmountDetail + shipFee - discountAmount;
                            mTvTotalAmount.setText(totalAmount + " đ");
                            mTvTotalAmount2.setText(totalAmount + " đ");
                            mOrder.setTotal_amount(totalAmount);
                            if (mProgressBarLocation != null){
                                mProgressBarLocation.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onDirectionFailure(Throwable t) {
                            Log.i("TAG111", "Fail");
                        }
                    });
        } else {
            Log.i("TAG111", "Null cmnr");
        }
    }

    public void updateLocation() {
        destination = new LatLng(mOrder.getLatitude(), mOrder.getLongitude());
        if (mMarker != null) {
            mMarker.remove();
        }
        mMarker = mMap.addMarker(new MarkerOptions()
                .position(destination)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.detinationpin)));
        createPolyline();
    }

}


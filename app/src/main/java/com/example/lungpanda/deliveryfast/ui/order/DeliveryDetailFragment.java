package com.example.lungpanda.deliveryfast.ui.order;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.location.Address;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.adapter.AddressAdapter;
import com.example.lungpanda.deliveryfast.adapter.DividerItemDecoration;
import com.example.lungpanda.deliveryfast.model.Order.Order;
import com.example.lungpanda.deliveryfast.model.Store.Store;
import com.example.lungpanda.deliveryfast.model.User.User;
import com.example.lungpanda.deliveryfast.model.User.UserAddress;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EFragment(R.layout.fragment_delivery_detail)
public class DeliveryDetailFragment extends Fragment {
    @ViewById(R.id.tvDone)
    TextView mTvDone;
    @ViewById(R.id.tvSearchAddress)
    TextView mTvSearchAddress;
    @ViewById(R.id.etReceiverName)
    EditText mEtReceiverName;
    @ViewById(R.id.etReceiverPhone)
    EditText mEtReceiverPhone;
    @ViewById(R.id.ivContacts)
    ImageView mIvContacts;
    @ViewById(R.id.tvDeliveryDate)
    TextView mTvDeliveryDate;
    @ViewById(R.id.tvDeliveryTime)
    TextView mTvDeliveryTime;
    @ViewById(R.id.address_recycler_view)
    RecyclerView mRecyclerView;
    @ViewById(R.id.tvViewMore)
    TextView mtvViewMore;

    private AddressAdapter mAddressAdapter;
    private List<UserAddress> mUserAddresses = new ArrayList<>();
    private List<UserAddress> mUserAddressesView = new ArrayList<>();

    @FragmentArg
    String sOrder;
    private Order mOrder;
    private int fDay, fMonth, fYear, mHour, mMinute;
    private final int REQUEST_CODE = 99;
    private boolean mContactPermissionGranted;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Calendar mCalendar;
    private Store mStore;
    private int asapHour, asapMinute, startHour, finishHour;
    private String today;

    @SuppressLint("WrongConstant")
    @AfterViews
    void init() {
        //Prepare check time
        mCalendar = Calendar.getInstance();
        int round = mCalendar.get(java.util.Calendar.MINUTE) % 15;
        mCalendar.add(java.util.Calendar.MINUTE, round < 8 ? -round : (15 - round));
        mCalendar.add(java.util.Calendar.MINUTE, 45);
        mCalendar.set(java.util.Calendar.SECOND, 0);
        asapHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        asapMinute = mCalendar.get(Calendar.MINUTE);
        mOrder = new Gson().fromJson(sOrder, Order.class);
        Log.i("ORRDER111", "init: " + mOrder);
        if (mOrder.getStore().getOpening_time() != null && mOrder.getStore().getClosing_time() != null) {
            startHour = Integer.parseInt(mOrder.getStore().getOpening_time().split(":")[0]);
            finishHour = Integer.parseInt(mOrder.getStore().getClosing_time().split(":")[0]);
        } else {
            startHour = 5;
            finishHour = 22;
        }
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        today = formatDate.format(mCalendar.getTime());

        mEtReceiverName.setText(mOrder.getUser_name());
        mEtReceiverPhone.setText(mOrder.getUser_phone());
        Date date = new Date(mOrder.getDelivery_date());
        mUserAddresses = mOrder.getUser().getUserAddresses();
        boolean hasChecked = true;
        for (UserAddress userAddress : mUserAddresses) {
            if (userAddress.isChecked()) {
                hasChecked = false;
                break;
            }
        }
        if (hasChecked) {
            UserAddress tmp = new UserAddress();
            tmp.setAddress(mOrder.getUser_address());
            tmp.setLatitude(mOrder.getLatitude());
            tmp.setLongitude(mOrder.getLongitude());
            mUserAddresses.add(0, tmp);
            mUserAddresses.get(0).setChecked(true);
        }
        if (mUserAddresses.size() > 3) {
            mtvViewMore.setVisibility(View.VISIBLE);
            for (int i = 0; i < 3; i++) {
                mUserAddressesView.add(mUserAddresses.get(i));
            }

        } else {
            mtvViewMore.setVisibility(View.GONE);
            mUserAddressesView = mUserAddresses;
        }
        mAddressAdapter = new AddressAdapter(mUserAddressesView, new AddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UserAddress userAddress) {
                userAddress.setChecked(true);
                for (UserAddress userAddress1 : mUserAddresses) {
                    if (userAddress != userAddress1 && userAddress1.isChecked()) {
                        userAddress1.setChecked(false);
                    }
                }
                mAddressAdapter.notifyDataSetChanged();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAddressAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String[] strings = format.format(date.getTime()).split(" ");
        mTvDeliveryDate.setText(strings[0]);
        mTvDeliveryTime.setText(strings[1]);
    }

    @Click(R.id.tvSearchAddress)
    void setmTvSearchAddress() {
        updateOrder();
        mFragmentManager = (getActivity()).getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.addToBackStack("SearchAddress");
        mFragmentTransaction.add(R.id.flOrderDetail, SearchLocationFragment_.builder().sOrder(new Gson().toJson(mOrder)).build()).commit();
    }

    @Click(R.id.tvViewMore)
    void setMtvViewMore() {
        setmTvSearchAddress();
    }

    @Click(R.id.tvDeliveryTime)
    void setmTvDeliveryTime() {
        final Calendar c = Calendar.getInstance();
        try {
            String[] tmp = mTvDeliveryTime.getText().toString().split(":");
            mHour = Integer.parseInt(tmp[0]);
            mMinute = Integer.parseInt(tmp[1]);
        } catch (Exception e) {
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
        }

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                if (mTvDeliveryDate.getText().toString().equals(today)){
                    if (selectedHour >= asapHour && selectedHour < finishHour){
                        if (selectedHour == asapHour){
                            if (selectedMinute > asapMinute){
                                String hour = (selectedHour < 10) ? "0" + (selectedHour) : (selectedHour) + "";
                                String minutes = (selectedMinute < 10) ? "0" + (selectedMinute) : (selectedMinute) + "";
                                mTvDeliveryTime.setText(hour + ":" + minutes);
                            } else{
                                Toast.makeText(getContext(), "Invalid Time", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            String hour = (selectedHour < 10) ? "0" + (selectedHour) : (selectedHour) + "";
                            String minutes = (selectedMinute < 10) ? "0" + (selectedMinute) : (selectedMinute) + "";
                            mTvDeliveryTime.setText(hour + ":" + minutes);
                        }
                    } else {
                        Toast.makeText(getContext(), "Invalid Time", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (selectedHour >= startHour && selectedHour < finishHour){
                        String hour = (selectedHour < 10) ? "0" + (selectedHour) : (selectedHour) + "";
                        String minutes = (selectedMinute < 10) ? "0" + (selectedMinute) : (selectedMinute) + "";
                        mTvDeliveryTime.setText(hour + ":" + minutes);
                    } else {
                        Toast.makeText(getContext(), "Invalid Time", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, mHour, mHour, true);
        timePickerDialog.show();

    }


    @Click(R.id.tvDeliveryDate)
    void setmTvDeliveryDate() {
        final Calendar c = (Calendar) mCalendar.clone();
        try {
            String[] tmp = mTvDeliveryDate.getText().toString().split("/");
            fDay = Integer.parseInt(tmp[0]);
            fMonth = Integer.parseInt(tmp[1]) - 1;
            fYear = Integer.parseInt(tmp[2]);
        } catch (Exception e) {
            fYear = c.get(Calendar.YEAR);
            fMonth = c.get(Calendar.MONTH);
            fDay = c.get(Calendar.DAY_OF_MONTH);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String month = (monthOfYear < 9) ? "0" + (monthOfYear + 1) : (monthOfYear + 1) + "";
                        String date = (dayOfMonth < 10) ? "0" + (dayOfMonth) : (dayOfMonth) + "";
                        mTvDeliveryDate.setText(date + "/" + month + "/" + year);
                    }
                }, fYear, fMonth, fDay);
        datePickerDialog.getDatePicker().setMinDate(c.getTime().getTime());
        c.add(Calendar.DATE, 4);
        datePickerDialog.getDatePicker().setMaxDate(c.getTime().getTime());
        datePickerDialog.show();
    }

    @Click(R.id.btnDone)
    void setmtvDone() {
        updateOrder();
        ((SubmitOrderActivity) getActivity()).updateOrder(mOrder);
        getFragmentManager().popBackStack();
    }

    public void updateOrder() {
        for (UserAddress userAddress : mUserAddresses) {
            if (userAddress.isChecked()) {
                mOrder.setUser_address(userAddress.getAddress());
                mOrder.setLatitude((float) userAddress.getLatitude());
                mOrder.setLongitude((float) userAddress.getLongitude());
                break;
            }
        }
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String sDate = mTvDeliveryDate.getText().toString() + " " + mTvDeliveryTime.getText().toString();
        Date date = new Date();
        try {
            date = (Date) format.parse(sDate);
        } catch (ParseException e) {

        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        mOrder.setDelivery_date(calendar.getTime().toString());
        mOrder.setUser_name(mEtReceiverName.getText().toString());
        mOrder.setUser_phone(mEtReceiverPhone.getText().toString());
    }

    @Click(R.id.ivContacts)
    void setmIvContacts() {
        if (getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            mContactPermissionGranted = true;
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE);
        }
        if (mContactPermissionGranted) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (REQUEST_CODE):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = ((SubmitOrderActivity) getActivity()).getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String num = "";
                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = ((SubmitOrderActivity) getActivity()).getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers.moveToNext()) {
                                num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                mEtReceiverPhone.setText(num);
                            }
                            numbers.close();
                        }
                    }
                    c.close();
                    break;

                }
        }
    }

}

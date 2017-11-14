package com.example.lungpanda.deliveryfast.ui.order;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

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
    @ViewById(R.id.tvTest)
    TextView mTvTest;
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

    @AfterViews
    void init() {
        mOrder = new Gson().fromJson(sOrder, Order.class);
        mEtReceiverName.setText(mOrder.getUser_name());
        mEtReceiverPhone.setText(mOrder.getUser_phone());
        Date date = new Date(mOrder.getDelivery_date());
        mUserAddresses = mOrder.getUser().getUserAddresses();
        UserAddress userAddress = new UserAddress();
        userAddress.setAddress(mOrder.getUser_address());
        userAddress.setLatitude(mOrder.getLatitude());
        userAddress.setLongitude(mOrder.getLongitude());
        mUserAddresses.add(0,userAddress);
        if (mUserAddresses.size() > 3){
            mtvViewMore.setVisibility(View.VISIBLE);
            for (int i =0; i < 4; i++){
                mUserAddressesView.add(mUserAddresses.get(i));
            }

        } else {
            mtvViewMore.setVisibility(View.GONE);
            mUserAddressesView = mUserAddresses;
        }
        mUserAddressesView.get(0).toggleCheckState();
        mUserAddresses.get(0).toggleCheckState();
        mAddressAdapter = new AddressAdapter(mUserAddressesView, new AddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UserAddress userAddress) {
                userAddress.toggleCheckState();
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
    void setMtvViewMore(){
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
                String hour = (selectedHour < 10) ? "0" + (selectedHour) : (selectedHour) + "";
                String minutes = (selectedMinute < 10) ? "0" + (selectedMinute) : (selectedMinute) + "";
                mTvDeliveryTime.setText(hour + ":" + minutes);
            }
        }, mHour, mHour, true);
        timePickerDialog.show();

    }


    @Click(R.id.tvDeliveryDate)
    void setmTvDeliveryDate() {
        final Calendar c = Calendar.getInstance();
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
        c.add(Calendar.DATE, 3);
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
        for (UserAddress userAddress: mUserAddresses){
            if (userAddress.isChecked()){
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

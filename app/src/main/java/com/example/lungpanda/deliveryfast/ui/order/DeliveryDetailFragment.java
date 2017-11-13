package com.example.lungpanda.deliveryfast.ui.order;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.model.Order.Order;
import com.example.lungpanda.deliveryfast.model.User.User;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.Date;

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

    @FragmentArg
    String sOrder;
    private Order mOrder;
    private int fDay, fMonth, fYear;
    private final int REQUEST_CODE = 99;
    private boolean mContactPermissionGranted;

    @AfterViews
    void init() {
        mOrder = new Gson().fromJson(sOrder,Order.class);
        mEtReceiverName.setText(mOrder.getUser_name());
        mEtReceiverPhone.setText(mOrder.getUser_phone());
        Date date = new Date(mOrder.getDelivery_date());
        String sDate, sMonth;
        String sYear = date.getYear() +"";
        sDate = (date.getDate() < 9 )? ("0" + date.getDate() ): (date.getDate()+"");
        sMonth = (date.getMonth() < 8 )? ("0" + (date.getMonth() + 1 )): ((date.getMonth() + 1 )+"");
        mTvDeliveryDate.setText(sDate +"/"+sMonth+"/"+sYear);
        mTvTest.setText(date.toString());
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
        datePickerDialog.show();
    }

    @Click(R.id.btnDone)
    void setmtvDone() {
//        ((SubmitOrderActivity)getActivity()).updateDeliveryDetail();
        getFragmentManager().popBackStack();
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

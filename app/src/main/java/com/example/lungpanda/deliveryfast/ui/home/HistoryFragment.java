package com.example.lungpanda.deliveryfast.ui.home;


import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.icu.util.Calendar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.ui.history.HistoryContentFragment_;
import com.example.lungpanda.deliveryfast.ui.history.IncomingContentFragment;
import com.example.lungpanda.deliveryfast.ui.history.IncomingContentFragment_;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@EFragment(R.layout.fragment_history)
public class HistoryFragment extends Fragment {
    @ViewById(R.id.tvFromDate)
    TextView mTvFromDate;
    @ViewById(R.id.tvFinishDate)
    TextView mTvFinishDate;
    @ViewById(R.id.swHistory)
    Switch mSwHistory;
    @ViewById(R.id.fLContent)
    FrameLayout mFrameLayout;

    private int fYear, fMonth, fDay;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;


    @AfterViews
    void init() {
        setDefaultDate();
        mFragmentManager = (getActivity()).getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.fLContent,HistoryContentFragment_.builder().build()).commit();
    }

    @Click(R.id.tvFromDate)
    void setmTvFromDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String sDate = mTvFinishDate.getText().toString();
        try {
            date = (Date) format.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        try {
            String[] tmp = mTvFromDate.getText().toString().split("/");
            fDay = Integer.parseInt(tmp[0]);
            fMonth = Integer.parseInt(tmp[1]) - 1;
            fYear = Integer.parseInt(tmp[2]);
        } catch (Exception e) {
            fYear = c.get(Calendar.YEAR);
            fMonth = c.get(Calendar.MONTH);
            fDay = c.get(Calendar.DAY_OF_MONTH);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String month = (monthOfYear < 9) ? "0" + (monthOfYear + 1) : (monthOfYear + 1) + "";
                        String date = (dayOfMonth < 10) ? "0" + (dayOfMonth) : (dayOfMonth) + "";
                        mTvFromDate.setText(date + "/" + month + "/" + year);

                    }
                }, fYear, fMonth, fDay);
        datePickerDialog.getDatePicker().setMaxDate(c.getTime().getTime());
        c.add(Calendar.DATE, -30);
        datePickerDialog.getDatePicker().setMinDate(c.getTime().getTime());
        datePickerDialog.show();
    }

    @AfterTextChange(R.id.tvFinishDate)
    void setmTvFinishDateAfter(){

    }

    @Click(R.id.tvFinishDate)
    void setmTvFinishDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String sDate = mTvFromDate.getText().toString();
        try {
            date = (Date) format.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Calendar c = Calendar.getInstance();
        try {
            String[] tmp = mTvFinishDate.getText().toString().split("/");
            fDay = Integer.parseInt(tmp[0]);
            fMonth = Integer.parseInt(tmp[1]) - 1;
            fYear = Integer.parseInt(tmp[2]);
        } catch (Exception e) {
            fYear = c.get(Calendar.YEAR);
            fMonth = c.get(Calendar.MONTH);
            fDay = c.get(Calendar.DAY_OF_MONTH);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String month = (monthOfYear < 9) ? "0" + (monthOfYear + 1) : (monthOfYear + 1) + "";
                        String date = (dayOfMonth < 10) ? "0" + (dayOfMonth) : (dayOfMonth) + "";
                        mTvFinishDate.setText(date + "/" + month + "/" + year);

                    }
                }, fYear, fMonth, fDay);
        datePickerDialog.getDatePicker().setMaxDate(c.getTime().getTime());
        c.setTime(date);
        datePickerDialog.getDatePicker().setMinDate(c.getTime().getTime());
        datePickerDialog.show();
    }

    @CheckedChange(R.id.swHistory)
    void setmSwHistory(){
        mFragmentManager = (getActivity()).getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        if (!mSwHistory.isChecked()){
            mFragmentTransaction.replace(R.id.fLContent,HistoryContentFragment_.builder().build()).commit();
        } else {
            mFragmentTransaction.replace(R.id.fLContent, IncomingContentFragment_.builder().build()).commit();
        }
    }

    public void setDefaultDate() {
        Calendar c = Calendar.getInstance();
        fYear = c.get(Calendar.YEAR);
        fMonth = c.get(Calendar.MONTH);
        fDay = c.get(Calendar.DAY_OF_MONTH);
        String month = (fMonth < 9) ? "0" + (fMonth + 1) : (fMonth + 1) + "";
        String date = (fDay < 10) ? "0" + (fDay) : (fDay) + "";
        mTvFinishDate.setText(date + "/" + month + "/" + fYear);
        c.add(Calendar.DATE, -30);
        fYear = c.get(Calendar.YEAR);
        fMonth = c.get(Calendar.MONTH);
        fDay = c.get(Calendar.DAY_OF_MONTH);
        month = (fMonth < 9) ? "0" + (fMonth + 1) : (fMonth + 1) + "";
        date = (fDay < 10) ? "0" + (fDay) : (fDay) + "";
        mTvFromDate.setText(date + "/" + month + "/" + fYear);

    }

}

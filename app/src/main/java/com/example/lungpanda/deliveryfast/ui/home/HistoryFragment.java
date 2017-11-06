package com.example.lungpanda.deliveryfast.ui.home;


import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.icu.util.Calendar;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.lungpanda.deliveryfast.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

@EFragment(R.layout.fragment_history)
public class HistoryFragment extends Fragment {
    @ViewById(R.id.tvFromDate)
    TextView mTvFromDate;
    @ViewById(R.id.tvFinishDate)
    TextView mTvFinishDate;

    private int fYear, fMonth, fDay;


    @AfterViews
    void init() {
        setDefaultDate();
    }

    @Click(R.id.tvFromDate)
    void setmTvFromDate() {
        chooseDate(mTvFromDate);
    }

    @Click(R.id.tvFinishDate)
    void setmTvFinishDate() {
        chooseDate(mTvFinishDate);
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

    public void chooseDate(final TextView textView) {
        final Calendar c = Calendar.getInstance();
        try {
            String[] tmp = textView.getText().toString().split("/");
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
                        textView.setText(date + "/" + month + "/" + year);

                    }
                }, fYear, fMonth, fDay);
        datePickerDialog.show();
    }

}

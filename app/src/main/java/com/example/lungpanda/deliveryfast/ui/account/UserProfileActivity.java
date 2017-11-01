package com.example.lungpanda.deliveryfast.ui.account;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.api.Api;
import com.example.lungpanda.deliveryfast.api.ApiClient;
import com.example.lungpanda.deliveryfast.model.UpdateResult;
import com.example.lungpanda.deliveryfast.model.User.User;
import com.example.lungpanda.deliveryfast.model.User.UserResult;
import com.example.lungpanda.deliveryfast.ui.home.HomeActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.ParseException;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EActivity(R.layout.activity_user_profile)
public class UserProfileActivity extends AppCompatActivity {
    @ViewById(R.id.edtUsername)
    EditText mEdtUsername;
    @ViewById(R.id.edtFirstName)
    EditText mEdtFirstName;
    @ViewById(R.id.edtLastName)
    EditText mEdtLastName;
    @ViewById(R.id.edtEmail)
    EditText mEdtEmail;
    @ViewById(R.id.edtDateOfBirth)
    Button mEdtDateOfBirth;
    @ViewById(R.id.rFemale)
    RadioButton mRFemale;
    @ViewById(R.id.rMale)
    RadioButton mRMale;
    @ViewById(R.id.rgGender)
    RadioGroup mRgGender;
    @ViewById(R.id.imgBack)
    ImageView mImgBack;
    @ViewById(R.id.tvSave)
    TextView mTvSave;
    @ViewById(R.id.tvFullName)
    TextView mTvFullName;
    @ViewById(R.id.tbrMainPhone)
    TableRow mTbrMainPhone;
    @ViewById(R.id.rlprogressbar)
    RelativeLayout mProgressBar;
    @ViewById(R.id.llContent)
    LinearLayout mLlContent;
    @ViewById(R.id.tvPhoneNumber)
    TextView mTvPhoneNumber;
    @ViewById(R.id.tvManagePhone)
    TextView mTvManagePhone;
    @ViewById(R.id.tvManageAddress)
    TextView getmTvManageAddress;

    private long mLastClickTime = 0;

    private String id_token;
    private User user;
    private int mYear, mMonth, mDay;

    @AfterViews
    void inits() {
        SharedPreferences settings = getSharedPreferences(SignInActivity.NAME_SHAREPREFERENCE, 0);
        id_token = settings.getString(SignInActivity.ID_TOKEN, "");

        Api api = ApiClient.retrofit().create(Api.class);
        Call<UserResult> result = api.getUser("application/json", "Bearer " + id_token);
        result.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        user = response.body().getUserData().getUser();
                        Log.i("USERPHONE", "onResponse: " + user.getUserPhones());
                        if(user.getUserPhones() == null || user.getUserPhones().size()==0) {
                            mTbrMainPhone.setVisibility(View.GONE);
                        } else {
                            for (int i = 0; i < user.getUserPhones().size(); i++){
                                if (user.getUserPhones().get(i).isRole()){
                                    mTvPhoneNumber.setText(user.getUserPhones().get(i).getPhone_number());
                                    break;
                                }
                            }
                        }

                        if(user.getUserAddresses() != null){
                            getmTvManageAddress.setText(user.getUserAddresses().size() + " Address");
                        }


                        if (user.getDate_of_birth()!= null) {
                            String tmp = user.getDate_of_birth();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            Date convertedDate = new Date();
                            try {
                                convertedDate = df.parse(tmp);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            Calendar cal = Calendar.getInstance();
                            cal.setTime(convertedDate);
                            cal.add(Calendar.DAY_OF_YEAR, 1);
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                        String formattedDate = dateFormat.format(convertedDate);

                            String formattedDate = cal.get(Calendar.YEAR) + "-" + ((cal.get(Calendar.MONTH) < 9)
                                    ? ("0" + (cal.get(Calendar.MONTH) + 1))
                                    : ((cal.get(Calendar.MONTH) + 1))) + "-" + ((cal.get(Calendar.DATE) < 10)
                                    ? ("0" + (cal.get(Calendar.DATE)))
                                    : (cal.get(Calendar.DATE)));
                            user.setDate_of_birth(formattedDate);
                        }

                        Log.i("USER0003", "onResponse: " + user.toString());
                        updateView();
                        mProgressBar.setVisibility(View.GONE);
                        mLlContent.setVisibility(View.VISIBLE);
                        mEdtFirstName.requestFocus();

                    } else {
                        HomeActivity_.intent(getApplicationContext()).start();
                        Toast.makeText(getApplication(), "Server is not working", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    HomeActivity_.intent(getApplicationContext()).start();
                    Toast.makeText(getApplication(), "Server is not working", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        HomeActivity_.intent(this).start();
    }

    @Click(R.id.imgBack)
    public void setmImgBack() {
        HomeActivity_.intent(this).start();
    }

    @Click(R.id.tvSave)
    public void setmBtSave() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        try  {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
        Api api = ApiClient.retrofit().create(Api.class);
        SharedPreferences settings = getSharedPreferences(SignInActivity.NAME_SHAREPREFERENCE, 0);
        id_token = settings.getString(SignInActivity.ID_TOKEN, "");

        Call<UpdateResult> result = api.updateInfor("application/json", user, "Bearer " + id_token);
        mProgressBar.setVisibility(View.VISIBLE);
        user.setFirst_name(mEdtFirstName.getText().toString());
        user.setLast_name(mEdtLastName.getText().toString());

        if (mRMale.isChecked()) {
            user.setGender(true);
        } else if (mRFemale.isChecked()) {
            user.setGender(false);
        }

        String tmp = mEdtDateOfBirth.getText().toString();

        user.setDate_of_birth(tmp);
        Log.i("USER0001", "setmBtSave: " + user.toString());
        result.enqueue(new Callback<UpdateResult>() {
            @Override
            public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                mProgressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getStatus()){
                        user = response.body().getUpdateData().getUser();

                        Log.i("USER0002", "onResponse: " + user.toString());
                        updateView();
                        Toast.makeText(getApplication(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplication(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<UpdateResult> call, Throwable t) {

            }
        });

    }


    void updateView() {
        if(user.getFirst_name() != null && user.getLast_name() != null){
            mTvFullName.setText(user.getFirst_name() + " " + user.getLast_name());
        } else {
            mTvFullName.setText("Hello");
        }

        mEdtUsername.setText(user.getUsername());
        mEdtFirstName.setText(user.getFirst_name());
        mEdtLastName.setText(user.getLast_name());
        mEdtEmail.setText(user.getEmail());
        if (user.getGender() != null && user.getGender()) {
            mRMale.setChecked(true);
        } else {
            mRFemale.setChecked(true);
        }

        mEdtDateOfBirth.setText(user.getDate_of_birth());
    }

    @Click(R.id.edtDateOfBirth)
    void chooseDate() {
        final Calendar c = Calendar.getInstance();
        if(user.getDate_of_birth()!= null){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date convertedDate = new Date();
            try {
                convertedDate = df.parse(user.getDate_of_birth());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.setTime(convertedDate);
        }
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String month = (monthOfYear < 9)? "0" + (monthOfYear + 1) : (monthOfYear + 1) + "";
                        String date = (dayOfMonth < 10)? "0" + (dayOfMonth) : (dayOfMonth) + "";
                        mEdtDateOfBirth.setText(year + "-" + month + "-" + date);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    @Click(R.id.tvManagePhone)
    void setmTbrMainPhone(){
        PhoneNumberActivity_.intent(this).start();
    }

    @Click(R.id.tvManageAddress)
    void setmTvManageAddress(){
        AddressActivity_.intent(this).start();
    }


}

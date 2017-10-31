package com.example.lungpanda.deliveryfast.ui.account;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.model.SignIn.SignIn;
import com.example.lungpanda.deliveryfast.model.SignIn.SignInResult;
import com.example.lungpanda.deliveryfast.api.Api;
import com.example.lungpanda.deliveryfast.api.ApiClient;
import com.example.lungpanda.deliveryfast.model.User.User;
import com.example.lungpanda.deliveryfast.ui.home.HomeActivity_;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.FileOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EActivity(R.layout.activity_sign_in)
public class SignInActivity extends AppCompatActivity {
    @ViewById(R.id.tvSignUp)
    TextView mTvSignUp;
    @ViewById(R.id.btnSignIn)
    Button mBtnSignIn;
    @ViewById(R.id.edtUsername)
    EditText mEdtUsername;
    @ViewById(R.id.edtPassword)
    EditText mEdtPassword;
    @ViewById(R.id.rlprogressbar)
    RelativeLayout mProgressBar;

    public static final String NAME_SHAREPREFERENCE = "SharePreferences";
    public static final String ID_TOKEN = "id_token";
    public static final String CURRENT_USER = "current_user";
    private long mLastClickTime = 0;

    @AfterViews
    void init() {
        SignIn si = new SignIn("lungpanda", "12345");
        Gson gson = new Gson();
        String json = gson.toJson(si);
        Log.i("TAG111", "init: " + json);
        SignIn sii = gson.fromJson(json, SignIn.class);

    }

    @Click(R.id.tvSignUp)
    void signUp() {
        SignUpActivity_.intent(this).start();
    }

    @Click(R.id.btnSignIn)
    void setmBtnSignIn() {
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
        String username = mEdtUsername.getText().toString();
        String password = mEdtPassword.getText().toString();
        if (username.equals("")) {
            mEdtUsername.setError("This field can't be blank!");
            mEdtUsername.requestFocus();
        } else if (password.equals("")) {
            mEdtPassword.setError("This field can't be blank!");
            mEdtPassword.requestFocus();
        } else if (password.length() < 7) {
            mEdtPassword.setError("Password must be at least 8 characters");
            mEdtPassword.requestFocus();
        } else {
            SignIn signIn = new SignIn(username, password);
            mProgressBar.setVisibility(View.VISIBLE);
            Call<SignInResult> result = api.signIn("application/json", signIn);
            result.enqueue(new Callback<SignInResult>() {
                @Override
                public void onResponse(Call<SignInResult> call, Response<SignInResult> response) {
                    mProgressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            SharedPreferences settings = getSharedPreferences(NAME_SHAREPREFERENCE, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString(ID_TOKEN, response.body().getData().getId_token());
                            editor.putString(CURRENT_USER, response.body().getData().getUser().toString());
                            editor.commit();
                            UserProfileActivity_.intent(getApplicationContext()).start();
                        } else {
                            Toast.makeText(getApplication(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplication(), "Server is not working", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SignInResult> call, Throwable t) {
                    Log.i("Long111", "onResponse: " + t.toString());
                }
            });

        }
    }
    @Override
    public void onBackPressed() {
        HomeActivity_.intent(this).start();
    }

}

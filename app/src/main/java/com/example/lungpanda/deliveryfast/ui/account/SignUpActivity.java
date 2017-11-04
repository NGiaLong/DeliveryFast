package com.example.lungpanda.deliveryfast.ui.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.model.SignUp.SignUp;
import com.example.lungpanda.deliveryfast.model.SignUp.SignUpResult;
import com.example.lungpanda.deliveryfast.model.User.User;
import com.example.lungpanda.deliveryfast.api.Api;
import com.example.lungpanda.deliveryfast.api.ApiClient;
import com.example.lungpanda.deliveryfast.ui.home.HomeActivity_;
import com.example.lungpanda.deliveryfast.ui.home.StoreActivity_;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EActivity(R.layout.activity_sign_up)
public class SignUpActivity extends AppCompatActivity {
    public static final String NAME_SHAREPREFERENCE = "SharePreferences";
    public static final String ACCESS_TOKEN = "access_token";
    @ViewById(R.id.btSignUp)
    Button mBtSignUp;
    @ViewById(R.id.tvEmail)
    TextView mTvEmail;
    @ViewById(R.id.tvPassword)
    TextView mTvPassword;
    @ViewById(R.id.tvRePassword)
    TextView mTvRePassword;
    @ViewById(R.id.tvUserName)
    TextView mTvUseName;
    @ViewById(R.id.tvSignIn)
    TextView mTvSignIn;

    private List<User> tasks;
    private Toast toast;

    @AfterViews
    void inits() {

    }

    @Click(R.id.tvSignIn)
    void setmTvSignIn() {
        SignInActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
    }


    @Click(R.id.btSignUp)
    void loginAction() {
        try  {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
            Api api = ApiClient.retrofit().create(Api.class);
        String email = mTvEmail.getText().toString();
        String pass = mTvPassword.getText().toString();
        String repass = mTvRePassword.getText().toString();
        String username = mTvUseName.getText().toString();
        if (pass.length() > 7) {
            if (!repass.equals(pass)) {
                mTvRePassword.setError("Confirm password is not the same!");
                mTvRePassword.requestFocus();
            } else {
                if (email.equals("")) {
                    mTvEmail.setError("This field can't be blank!");
                    mTvEmail.requestFocus();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mTvEmail.setError("Invalid Email Address");
                    mTvEmail.requestFocus();

                } else if (username.equals("")) {
                    mTvUseName.setError("This field can't be blank!");
                    mTvUseName.requestFocus();
                } else {
                    if (username.length() < 4) {
                        mTvUseName.setError("Username must be at least 5 characters");
                        mTvUseName.requestFocus();
                    } else {
                        SignUp signUp = new SignUp(email, pass, username);
                        Call<SignUpResult> signUpResultCall = api.createAccount("application/json", signUp);
                        signUpResultCall.enqueue(new Callback<SignUpResult>() {
                            @Override
                            public void onResponse(Call<SignUpResult> call, Response<SignUpResult> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().getStatus()) {
                                        User user = response.body().getData().getUser();
                                        String id_token = response.body().getData().getId_token();
                                        Toast.makeText(getApplication(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        HomeActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
                                    } else {
                                        String message = response.body().getMessage().toString();
                                        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                                        switch (message){
                                            case "Email has been existed!" :
                                                mTvEmail.requestFocus();
                                                break;
                                            case "Username has been existed!":
                                                mTvUseName.requestFocus();
                                                break;
                                        }
                                    }
                                } else {
                                    onBackPressed();
                                    Toast.makeText(getApplication(), "Server is not working", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<SignUpResult> call, Throwable t) {
                                onBackPressed();
                                Toast.makeText(getApplication(), "Server is not working", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            }
        } else {
            mTvPassword.setError("Password must be at least 8 characters");
            mTvPassword.requestFocus();
        }
    }
}

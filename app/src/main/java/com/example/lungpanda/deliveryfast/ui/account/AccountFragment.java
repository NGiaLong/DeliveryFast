package com.example.lungpanda.deliveryfast.ui.account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.ui.home.HomeActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_account)
public class AccountFragment extends Fragment {
    @ViewById(R.id.btnSignIn)
    Button mBtnSignIn;
    @ViewById(R.id.btnViewProfile)
    Button mBtnViewProfile;
    @ViewById(R.id.btnLogOut)
    Button mBtnLogOut;
    private String id_token;
    private String username;
    @AfterViews
    void init() {
        viewStatus();

   }



    @Click(R.id.btnSignIn)
   void setmBtnSignIn(){
       SignInActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
   }
   @Click(R.id.btnViewProfile)
    void setmBtnViewProfile(){
       UserProfileActivity_.intent(this).start();
   }
   @Click(R.id.btnLogOut)
    void setmBtnLogOut(){
       SharedPreferences settings = getActivity().getSharedPreferences(SignInActivity.NAME_SHAREPREFERENCE, 0);
       SharedPreferences.Editor editor = settings.edit();
       editor.putString(SignInActivity.ID_TOKEN, "");
       editor.commit();
       viewStatus();
       HomeActivity_.intent(getContext()).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
   }
   public void viewStatus(){
       SharedPreferences settings = getActivity().getSharedPreferences(SignInActivity.NAME_SHAREPREFERENCE, 0);
       id_token = settings.getString(SignInActivity.ID_TOKEN, "");
       if (id_token!= null && id_token!= ""){
           mBtnSignIn.setVisibility(View.GONE);
           mBtnViewProfile.setVisibility(View.VISIBLE);
           mBtnLogOut.setVisibility(View.VISIBLE);
       } else {
           mBtnSignIn.setVisibility(View.VISIBLE);
           mBtnViewProfile.setVisibility(View.GONE);
           mBtnLogOut.setVisibility(View.GONE);
       }
   }
}

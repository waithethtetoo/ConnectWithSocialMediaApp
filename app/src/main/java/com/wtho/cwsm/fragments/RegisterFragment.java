package com.wtho.cwsm.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.wtho.cwsm.ConnectWithSocialMediaApp;
import com.wtho.cwsm.R;
import com.wtho.cwsm.activities.AccountControlActivity;
import com.wtho.cwsm.controller.UserSessionController;
import com.wtho.cwsm.data.model.UserModel;
import com.wtho.cwsm.data.vo.UserVo;
import com.wtho.cwsm.utils.FacebookUtils;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class RegisterFragment extends Fragment
        implements AccountControlActivity.SocialMediaInfoDelegate, View.OnClickListener {

    private TextView tv_name, tv_email;
    private Button btn_connect_with_facebook, btn_connect_with_google;
    private ImageView iv_profile_cover;

    public static final String FRAGMENT_TRANSITION_TAG = "RegisterFragment";

    private int mConnectedSocialMedia;

    private static final int CONNECT_WITH_FACEBOOK = 1;
    private static final int CONNECT_WITH_GOOGLE = 2;

    private UserVo mRegisteringUser;
    private UserSessionController mUserSessionController;

    public RegisterFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mUserSessionController = (UserSessionController) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_email = (TextView) view.findViewById(R.id.tv_email);
        btn_connect_with_facebook = (Button) view.findViewById(R.id.btn_connect_with_facebook);
        btn_connect_with_google = (Button) view.findViewById(R.id.btn_connect_with_google);
        btn_connect_with_facebook.setOnClickListener(this);
        btn_connect_with_google.setOnClickListener(this);
        iv_profile_cover = (ImageView) view.findViewById(R.id.iv_profile_cover);


        return view;
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onClick(View v) {
        if (v == btn_connect_with_facebook) {
            mUserSessionController.connectToFacebook(this);
        }
        if (v == btn_connect_with_google) {

        }
    }

    public void onRetrieveFacebookInfo(JSONObject facebookLoginUser, String profileImageUrl, String coverPhotoImageUrl) {
        mConnectedSocialMedia = CONNECT_WITH_FACEBOOK;
        mRegisteringUser = UserVo.initFromFacebookInfo(facebookLoginUser, profileImageUrl, coverPhotoImageUrl);
        showRetrievedDataInRegistrationForm(mRegisteringUser);

    }

    private void showRetrievedDataInRegistrationForm(UserVo registeringUser) {
        if (!TextUtils.isEmpty(registeringUser.getName())) {
            tv_name.setText(registeringUser.getName());
        }
        if (!TextUtils.isEmpty(registeringUser.getEmail())) {
            tv_email.setText(registeringUser.getEmail());
        }
        if (!TextUtils.isEmpty(registeringUser.getProfilePicture())) {
            iv_profile_cover.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(registeringUser.getProfilePicture())
                    .asBitmap().centerCrop()
                    .placeholder(R.drawable.dummy_avatar)
                    .error(R.drawable.dummy_avatar)
                    .into(new BitmapImageViewTarget(iv_profile_cover) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable roundedBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(iv_profile_cover.getResources(), resource);
                            roundedBitmapDrawable.setCircular(true);
                            iv_profile_cover.setImageDrawable(roundedBitmapDrawable);
                        }
                    });
        } else {
            iv_profile_cover.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean isRegistering() {
        return true;
    }

}

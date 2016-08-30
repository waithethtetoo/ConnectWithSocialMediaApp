package com.wtho.cwsm.fragments;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.wtho.cwsm.ConnectWithSocialMediaApp;
import com.wtho.cwsm.R;

import com.wtho.cwsm.utils.FacebookUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
        implements GoogleApiClient.OnConnectionFailedListener {
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_email)
    TextView tv_email;

    @BindView(R.id.btn_connect_with_facebook)
    Button btn_connect_with_facebook;
    @BindView(R.id.btn_connect_with_google)
    Button btn_connect_with_google;

    @BindView(R.id.iv_profile_cover)
    ImageView iv_profile_cover;
    @BindView(R.id.iv_profile)
    ImageView iv_profile;

    private CallbackManager mCallbackManger;
    private AccessTokenTracker mtracker;

    private GoogleApiClient mGoogleApiClient;
    protected static final int GOOGLE_SIGN_IN = 111;

    public MainActivityFragment() {
    }

    public static MainActivityFragment newInstance() {
        MainActivityFragment fragment = new MainActivityFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        mCallbackManger = CallbackManager.Factory.create();
        mtracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    Log.d(ConnectWithSocialMediaApp.TAG, "logout from facebook");
                }
            }
        };

        LoginManager.getInstance().registerCallback(mCallbackManger,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        processFacebookInfo(loginResult);
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(ConnectWithSocialMediaApp.getContext())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .addApi(Plus.API)
                .build();
        return view;
    }

    @OnClick(R.id.btn_connect_with_facebook)
    public void connectToFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(FacebookUtils.FACEBOOK_LOGIN_PERMISSIONS));
        } else {
            LoginManager.getInstance().logOut();
        }
    }

    @OnClick(R.id.btn_connect_with_google)
    public void connectToGoogle() {
        Intent intentSignin = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intentSignin, GOOGLE_SIGN_IN);
    }


    private void processFacebookInfo(LoginResult loginResult) {

        final AccessToken accessToken = loginResult.getAccessToken();
        FacebookUtils.getInstance().requestFacebookLoginUser(accessToken, new FacebookUtils.FacebookGetLoginUserCallback() {
            @Override
            public void onSuccess(final JSONObject facebookLoginUser) {
                FacebookUtils.getInstance().requestFacebookProfilePhoto(accessToken, new FacebookUtils.FacebookGetPictureCallback() {
                    @Override
                    public void onSuccess(final String profilePhotoUrl) {
                        FacebookUtils.getInstance().requestFacebookCoverPhoto(accessToken, new FacebookUtils.FacebookGetPictureCallback() {
                            @Override
                            public void onSuccess(String coverPhotoUrl) {
                                onLoginWithFacebook(facebookLoginUser, profilePhotoUrl, coverPhotoUrl);
                            }
                        });
                    }
                });
            }
        });

    }

    private void onLoginWithFacebook(JSONObject loginUser, String profileUrl, String coverUrl) {
        try {
            if (loginUser.has("name")) {
                tv_name.setText(loginUser.getString("name"));
            }
            if (loginUser.has("email")) {
                tv_email.setText(loginUser.getString("email"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Glide.with(iv_profile.getContext()).load(profileUrl).centerCrop().crossFade().error(R.mipmap.ic_launcher).into(iv_profile);
        Glide.with(iv_profile_cover.getContext()).load(coverUrl).centerCrop().crossFade().error(R.mipmap.ic_launcher).into(iv_profile_cover);
    }

    /*   @Override
       public void onClick(View v) {
           if (v == btn_connect_with_facebook) {
               connectToFacebook();
           }
           if (v == btn_connect_with_google) {
               connectToGoogle();
           }
       }
   */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManger.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Person registeringUser = null;
            if (mGoogleApiClient.hasConnectedApi(Plus.API)) {
                registeringUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            }
            processGoogleInfo(result, registeringUser);
        }
    }

    private void processGoogleInfo(GoogleSignInResult result, Person registeringUser) {
        if (result.isSuccess()) {
            GoogleSignInAccount signInResult = result.getSignInAccount();
            onLoginWithGoogle(signInResult, registeringUser);
        }
    }

    private void onLoginWithGoogle(GoogleSignInAccount signInAccount, Person registeringUser) {
        tv_name.setText(signInAccount.getDisplayName());
        tv_email.setText(signInAccount.getEmail());
        Uri imageUri = signInAccount.getPhotoUrl();
        if (imageUri != null) {
            Glide.with(iv_profile.getContext()).load(imageUri.toString()).centerCrop().into(iv_profile);
        }
        if (registeringUser != null) {
            Glide.with(iv_profile.getContext()).load(registeringUser.getCover().getCoverPhoto().getUrl()).centerCrop().into(iv_profile);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mtracker.startTracking();
    }

    @Override
    public void onStop() {
        super.onStop();
        mtracker.stopTracking();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

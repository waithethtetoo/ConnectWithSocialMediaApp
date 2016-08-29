package com.wtho.cwsm.activities;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.wtho.cwsm.ConnectWithSocialMediaApp;
import com.wtho.cwsm.R;
import com.wtho.cwsm.data.model.UserModel;
import com.wtho.cwsm.fragments.RegisterFragment;
import com.wtho.cwsm.utils.FacebookUtils;

import org.json.JSONObject;

import java.util.Arrays;

import de.greenrobot.event.EventBus;

public class AccountControlActivity extends AppCompatActivity {
    private ImageView iv_profile;

    private CallbackManager mCallbackManager;
    private AccessTokenTracker mTracker;

    private SocialMediaInfoDelegate mSocialMediaInfoDelegate;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_control);

        mCallbackManager = CallbackManager.Factory.create();
        mTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.d(ConnectWithSocialMediaApp.TAG, "Logout from Facebook");
            }
        };

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(ConnectWithSocialMediaApp.TAG, "Login with Facebook");
                        processFacebookInfo(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        Log.d(ConnectWithSocialMediaApp.TAG, "Login with Facebook");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(ConnectWithSocialMediaApp.TAG, "Login with Facebook");
                    }
                });
        if (savedInstanceState == null) {
            Fragment fragment;
            String fragmentTransitionTag = null;
            fragment = RegisterFragment.newInstance();
            fragmentTransitionTag = RegisterFragment.FRAGMENT_TRANSITION_TAG;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment, fragmentTransitionTag)
                    .commit();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
        mTracker.startTracking();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus eventBus = EventBus.getDefault();
        eventBus.unregister(this);
        mTracker.startTracking();
    }

    public void connectToFacebook(SocialMediaInfoDelegate socialMediaInfoDelegate) {
        mSocialMediaInfoDelegate = socialMediaInfoDelegate;
        if (AccessToken.getCurrentAccessToken() == null) {
            Toast.makeText(getApplicationContext(), "Logging In ....", Toast.LENGTH_SHORT).show();
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(FacebookUtils.FACEBOOK_LOGIN_PERMISSIONS));
        } else {
            Toast.makeText(getApplicationContext(), "Logging Out ....", Toast.LENGTH_SHORT).show();
            LoginManager.getInstance().logOut();
        }
    }


    private void processFacebookInfo(LoginResult loginResult) {
        final AccessToken accessToken = loginResult.getAccessToken();
        FacebookUtils.getObjInstance().requestFacebookLoginUser(accessToken, new FacebookUtils.FacebookGetLoginUserCallback() {
            @Override
            public void onSuccess(final JSONObject facebookLoginUser) {
                FacebookUtils.getObjInstance().requestFacebookProfilePhoto(accessToken, new FacebookUtils.FacebookGetPictureCallback() {
                    @Override
                    public void onSuccess(final String profileImageUrl) {
                        FacebookUtils.getObjInstance().requestFacebookCoverPhoto(accessToken, new FacebookUtils.FacebookGetPictureCallback() {
                            @Override
                            public void onSuccess(String CoverPhotoImageUrl) {
                                if (mSocialMediaInfoDelegate.isRegistering()) {
                                    RegisterFragment registerFragment = (RegisterFragment) getSupportFragmentManager()
                                            .findFragmentByTag(RegisterFragment.FRAGMENT_TRANSITION_TAG);
                                    registerFragment.onRetrieveFacebookInfo(facebookLoginUser, profileImageUrl, CoverPhotoImageUrl);
                                } else {
                                    onLoginWithFacebook(facebookLoginUser, profileImageUrl, CoverPhotoImageUrl);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private void onLoginWithFacebook(JSONObject facebookLoginUser, String profileImageUrl, String coverPhotoImageUrl) {
        showProgressDialog("Logging In with Facebook Info. Please wait.");
        UserModel.getObjInstance().loginWithFacebook(facebookLoginUser, profileImageUrl, coverPhotoImageUrl);
    }

    /*    public void onLoginWithGoogle(GoogleSignInAccount singInAccount, Person registeringUser) {
            showProgressDialog("Logging In with Google Info. Please wait.");
            UserModel.getObjInstance().loginWithGoogle(singInAccount, registeringUser) l
        } */

    protected void showProgressDialog(String message) {

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(ConnectWithSocialMediaApp.getContext());
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    public interface SocialMediaInfoDelegate {
        boolean isRegistering();
    }
}

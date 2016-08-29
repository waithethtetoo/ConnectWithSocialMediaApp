package com.wtho.cwsm.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.wtho.cwsm.ConnectWithSocialMediaApp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by WT on 8/29/2016.
 */
public class FacebookUtils {
    public static final String[] FACEBOOK_LOGIN_PERMISSIONS = {"public_profile", "email"};

    private static FacebookUtils objInstance;
    private Context context;

    private FacebookUtils() {
        context = ConnectWithSocialMediaApp.getContext();
    }

    public static FacebookUtils getObjInstance() {
        if (objInstance == null) {
            objInstance = new FacebookUtils();
        }
        return objInstance;
    }

    public void requestFacebookLoginUser(final AccessToken accessToken, final FacebookGetLoginUserCallback callback) {
        if (NetworkUtils.isOnline(ConnectWithSocialMediaApp.getContext())) {
            GraphRequest request = GraphRequest.newMeRequest(accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            callback.onSuccess(object);
                        }
                    });
            Bundle bundle = new Bundle();
            bundle.putString("fields", "id,name,email");
            request.setParameters(bundle);
            request.executeAsync();
        }
    }

    public void requestFacebookCoverPhoto(AccessToken accessToken, final FacebookGetPictureCallback callback) {
        if (NetworkUtils.isOnline(ConnectWithSocialMediaApp.getContext())) {
            Bundle bundle = new Bundle();
            bundle.putString("field", "cover");
            GraphRequest request = new GraphRequest(accessToken,
                    "me",
                    bundle,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            try {
                                JSONObject coverRequest = response.getJSONObject().getJSONObject("cover");
                                String coverUrl = coverRequest.getString("source");
                                callback.onSuccess(coverUrl);
                            } catch (JSONException e) {
                                Log.e(ConnectWithSocialMediaApp.TAG, e.getMessage());
                            }
                        }
                    });
        }
    }

    public void requestFacebookProfilePhoto(AccessToken accessToken, final FacebookGetPictureCallback callback) {
        if (NetworkUtils.isOnline(ConnectWithSocialMediaApp.getContext())) {
            Bundle bundle = new Bundle();
            bundle.putString("redirect", "false");
            bundle.putString("type", "large");
            GraphRequest request = new GraphRequest(
                    accessToken,
                    "me/picture",
                    bundle,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            try {
                                String profilePhotoUrl = response.getJSONObject().getJSONObject("data").getString("url");
                                callback.onSuccess(profilePhotoUrl);
                            } catch (JSONException e) {
                                Log.e(ConnectWithSocialMediaApp.TAG, e.getMessage());
                            }
                        }
                    }
            );
        }
    }

    public interface FacebookGetLoginUserCallback {
        void onSuccess(JSONObject facebookLoginUser);
    }

    public interface FacebookGetPictureCallback {
        void onSuccess(String imageUrl);
    }
}


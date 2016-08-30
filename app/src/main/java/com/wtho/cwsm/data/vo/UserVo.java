package com.wtho.cwsm.data.vo;

import android.util.Log;

import com.wtho.cwsm.ConnectWithSocialMediaApp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by WT on 8/29/2016.
 */
public class UserVo {
    private String name;
    private String email;
    private String profilePicture;
    private String coverPicture;
    private String facebookID;
    private String googleId;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getCoverPicture() {
        return coverPicture;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setCoverPicture(String coverPicture) {
        this.coverPicture = coverPicture;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public static UserVo initFromFacebookInfo(JSONObject facebookInfo, String profileUrl, String coverUrl) {
        UserVo loginUser = new UserVo();
        try {
            if (facebookInfo.has("id")) {
                loginUser.setFacebookID(facebookInfo.getString("id"));
            }
            if (facebookInfo.has("name")) {
                loginUser.setName(facebookInfo.getString("name"));
            }
            if (facebookInfo.has("email")) {
                loginUser.setEmail(facebookInfo.getString("email"));
            }
        } catch (JSONException e) {
            Log.e(ConnectWithSocialMediaApp.TAG, e.getMessage());
        }
        loginUser.setProfilePicture(profileUrl);
        loginUser.setCoverPicture(coverUrl);
        return loginUser;
    }

 /*   public static UserVo initFromGoogleInfo(GoogleSignInAccount googleSignInAccount, Person registeringUser) {
        UserVo loginUser = new UserVo();
        loginUser.setGoogleId(googleSignInAccount.getId());
        loginUser.setName(googleSignInAccount.getDisplayName());
        loginUser.setEmail(googleSignInAccount.getEmail());

        Uri imageUri = googleSignInAccount.getPhotoUrl();
        if (imageUri != null) {
            loginUser.setProfilePicture(imageUri.toString());
        }
        if (registeringUser != null) {
            loginUser.setCoverPicture(registeringUser.getCover().getCoverPhoto().getUrl());
        }
        return loginUser;
    }*/
}

package com.wtho.cwsm.data.vo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.wtho.cwsm.ConnectWithSocialMediaApp;
import com.wtho.cwsm.UserContract;

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

    public void saveLoginUser() {
        ContentValues cv = parseToContentValues();
        Context context = ConnectWithSocialMediaApp.getContext();
        Uri insertedUri = context.getContentResolver().insert(UserContract.LoginUserEntry.CONTENT_URI, cv);
        Log.d(ConnectWithSocialMediaApp.TAG, "Login User Inserted Uri : " + insertedUri);
    }

    public static UserVo loadLoginUser() {
        Context context = ConnectWithSocialMediaApp.getContext();
        Cursor loginUserCursor = context.getContentResolver().query(UserContract.LoginUserEntry.CONTENT_URI,
                null, null, null, null);
        if (loginUserCursor != null && loginUserCursor.moveToFirst()) {
            return UserVo.parseFromCursor(loginUserCursor);
        }
        return null;
    }

    private static UserVo parseFromCursor(Cursor cursor) {
        UserVo loginUser = new UserVo();
        loginUser.setName(cursor.getString(cursor.getColumnIndex(UserContract.LoginUserEntry.COLUMN_NAME)));
        loginUser.setEmail(cursor.getString(cursor.getColumnIndex(UserContract.LoginUserEntry.COLUMN_EMAIL)));
        loginUser.setProfilePicture(cursor.getString(cursor.getColumnIndex(UserContract.LoginUserEntry.COLUMN_PROFILE_PICTURE)));
        loginUser.setCoverPicture(cursor.getString(cursor.getColumnIndex(UserContract.LoginUserEntry.COLUMN_COVER_PICTURE)));
        loginUser.setFacebookID(cursor.getString(cursor.getColumnIndex(UserContract.LoginUserEntry.COLUMN_FACEBOOK_ID)));
        return loginUser;
    }

    private ContentValues parseToContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(UserContract.LoginUserEntry.COLUMN_NAME, name);
        cv.put(UserContract.LoginUserEntry.COLUMN_EMAIL, name);
        cv.put(UserContract.LoginUserEntry.COLUMN_PROFILE_PICTURE, name);
        cv.put(UserContract.LoginUserEntry.COLUMN_COVER_PICTURE, name);
        cv.put(UserContract.LoginUserEntry.COLUMN_FACEBOOK_ID, name);
        return cv;
    }

    public void clearData() {
        Context context = ConnectWithSocialMediaApp.getContext();
        int deletedRowCount = context.getContentResolver().delete(UserContract.LoginUserEntry.CONTENT_URI, null, null);
        Log.d(ConnectWithSocialMediaApp.TAG, "User clearData : " + deletedRowCount);
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

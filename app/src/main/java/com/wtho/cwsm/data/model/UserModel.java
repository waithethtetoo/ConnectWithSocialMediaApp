package com.wtho.cwsm.data.model;

import com.wtho.cwsm.data.vo.UserVo;

import org.json.JSONObject;

/**
 * Created by WT on 8/29/2016.
 */
public class UserModel {
    private static UserModel objInstance;
    private UserVo loginUser;

    private UserModel() {
        super();
    }

    public void init() {
        loginUser = UserVo.loadLoginUser();
    }

    public static UserModel getObjInstance() {
        if (objInstance == null) {
            objInstance = new UserModel();
        }
        return objInstance;
    }

    public boolean isUserLogin() {
        return loginUser != null;
    }

    public UserVo getLoginUser() {
        return loginUser;
    }

    public void registerWithFacebook(UserVo registeringUser, String password) {
        loginUser = registeringUser;
    }

    public void registerWithGoogle(UserVo registeringUser, String password) {
        loginUser = registeringUser;
    }

    public void logout() {
        loginUser.clearData();
        loginUser = null;
    }

    public void loginWithFacebook(JSONObject facebookLoginUser, String profile_pict, String cover_pict) {
        loginUser = UserVo.initFromFacebookInfo(facebookLoginUser, profile_pict, cover_pict);
    }

  /*  public void loginWithGoogle(GoogleSignInAccount googleLoginUser, Person registeringUser) {
        loginUser = UserVo.initFromGoogleInfo(googleLoginUser, registeringUser);
    } */

    public void saveProfilePicture(String localPath) {
        loginUser.setProfilePicture(localPath);
        loginUser.saveLoginUser();
    }
}

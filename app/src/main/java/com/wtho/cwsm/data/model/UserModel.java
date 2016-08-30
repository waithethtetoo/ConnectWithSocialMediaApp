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

    public static UserModel getObjInstance() {
        if (objInstance == null) {
            objInstance = new UserModel();
        }
        return objInstance;
    }

    public void connectWithFaebook(JSONObject facebookLoginUser, String profile_pict, String cover_pict) {
        loginUser = UserVo.initFromFacebookInfo(facebookLoginUser, profile_pict, cover_pict);

    }

  /*  public void loginWithGoogle(GoogleSignInAccount googleLoginUser, Person registeringUser) {
        loginUser = UserVo.initFromGoogleInfo(googleLoginUser, registeringUser);
    } */

}

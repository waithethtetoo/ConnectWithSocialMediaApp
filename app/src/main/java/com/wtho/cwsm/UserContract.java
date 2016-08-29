package com.wtho.cwsm;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by WT on 8/29/2016.
 */
public class UserContract {

    public static final String CONTENT_AUTHORITY = ConnectWithSocialMediaApp.class.getPackage().getName();
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_LOGIN_USER = "login_user";

    public static final class LoginUserEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOGIN_USER).build();
        public static final String DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOGIN_USER;
        public static final String ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOGIN_USER;
        public static final String TABLE_NAME = "loign_user";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PROFILE_PICTURE = "profile_picture";
        public static final String COLUMN_COVER_PICTURE = "cover_picture";
        public static final String COLUMN_FACEBOOK_ID = "facebook_id";

        public static Uri buildLoginUserUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}

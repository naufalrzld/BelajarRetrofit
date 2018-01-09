package com.naufalrzld.belajarretrofit.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Naufal on 09/01/2018.
 */

public class SessionManager {
    private static final String PREF_NAME = "Session";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private static SessionManager instance = null;
    private SharedPreferences sharedPreferences;

    private Context mContext;
    private Editor editor;

    public SessionManager(Context context) {
        this.mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}

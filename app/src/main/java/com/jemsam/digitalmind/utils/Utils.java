package com.jemsam.digitalmind.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import okhttp3.OkHttpClient;

/**
 * Created by jeremy.toussaint on 11/10/16.
 */

public class Utils {

    public static final String API_URL = "https://training.loicortola.com/chat-rest/2.0/";
    public static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static String PREF_LOGIN = "pref_login";
    private static String PREF_PASSWORD = "pref_password";

    public static void setPrefLogin(Context context, String login){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_LOGIN, login);
        editor.apply();
    }

    public static String getPrefLogin(Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(PREF_LOGIN, null);
    }

    public static void setPrefPassword(Context context, String password){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_PASSWORD, password);
        editor.apply();
    }

    public static String getPrefPassword(Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(PREF_PASSWORD, null);
    }

}

package com.susankya.ntbnse_library;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class MySharedPreferences {

    private Context c;
    private Editor editor;
    private SharedPreferences sharedPreferences;
    private boolean isAdminSignedIn;
    private boolean isUserSignedIn;
    private String token;

    public String getRollNumber() {
        return sharedPreferences.getString(Constants.Roll_NUMBER,"null");

    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
        editor.putString(Constants.Roll_NUMBER,rollNumber).apply();
    }

    private String rollNumber;

    public String getToken() {
        return sharedPreferences.getString(Constants.TOKEN,"null");
    }

    public void setToken(String token) {
        this.token = token;
        editor.putString(Constants.TOKEN,token).apply();

    }

    public boolean isUserSignedIn() {
        return sharedPreferences.getBoolean(Constants.IS_USER_LOGGED_IN, false);
    }

    public void setUserSignedIn(boolean userSignedIn) {
        isUserSignedIn = userSignedIn;
        editor.putBoolean(Constants.IS_USER_LOGGED_IN,userSignedIn).apply();
    }

    public MySharedPreferences(Context c, String prefName)
    {
        this.c=c;
        sharedPreferences=c.getSharedPreferences(prefName,Context.MODE_PRIVATE);
        editor=c.getSharedPreferences(prefName,Context.MODE_PRIVATE).edit();
    }

    public boolean isAdminSignedIn() {
        return sharedPreferences.getBoolean(Constants.IS_INCHARGE_LOGGED_IN, false);

    }

    public void setAdminSignedIn(boolean signedIn) {
        isAdminSignedIn = signedIn;
        editor.putBoolean(Constants.IS_INCHARGE_LOGGED_IN,signedIn).apply();
    }

public void eraseAll(){
    editor.putBoolean(Constants.IS_USER_LOGGED_IN,false).apply();
    editor.putBoolean(Constants.IS_INCHARGE_LOGGED_IN,false).apply();
    editor.putString(Constants.TOKEN,"").apply();
    editor.putString(Constants.Roll_NUMBER,"").apply();
    ApiClient.tokenLessClient();
}



}

package com.andersen.internship.testproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesManager {

    private  SharedPreferences sharedPreferences;
    private static SharedPreferencesManager instance;

    public static final String DEFAULT_TAG = "DEFAULT_TAG";

    public static synchronized SharedPreferencesManager getInstance(Context context){
        if (instance == null)
            instance = new SharedPreferencesManager(context);
        return instance;
    }

    private SharedPreferencesManager(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void writeText(String text, String tag){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(tag, text);
        editor.commit();
    }

    public void writeText(String text) {
        writeText(DEFAULT_TAG, text);
    }

    public String readText(String tag){
        return sharedPreferences.getString(tag, "");
    }

    public String readText(){
        return readText(DEFAULT_TAG);
    }
}
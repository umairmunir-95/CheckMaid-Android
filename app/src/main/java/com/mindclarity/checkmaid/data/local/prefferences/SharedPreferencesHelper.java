package com.mindclarity.checkmaid.data.local.prefferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesHelper {

    private static final String PREFS_NAME = "CheckMaidsPrefs";
    private static SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(SharedPreferencesHelper.PREFS_NAME, Context.MODE_PRIVATE);
    }

    public SharedPreferences getInstance() {
        return sharedPreferences;
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

    public void setString(String key, String value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(key, value);
        editor.apply();
    }

    public void putBooelan(String key, Boolean value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void clearPreferenceStore() {
        SharedPreferences.Editor editor = getEditor();
        editor.clear();
        editor.commit();
    }
    public void clearPreference(String key) {
        SharedPreferences.Editor editor = getEditor();
        editor.remove(key);
        editor.commit();
    }

    public boolean isPreferencesSet(Context context)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(sharedPreferences.getAll().size()>0)
            return true;
        else
            return false;
    }
}

package in.sportscafe.scgame;

import android.content.SharedPreferences;

/**
 * Created by Jeeva on 6/4/16.
 */
public abstract class AbstractDataHandler implements Constants {

    public abstract SharedPreferences getSharedPreferences();

    public void setSharedIntData(String key, int value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getSharedIntData(String key) {
        return getSharedPreferences().getInt(key, -1);
    }

    public int getSharedIntData(String key, int defValue) {
        return getSharedPreferences().getInt(key, defValue);
    }

    public void setSharedStringData(String key, String value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(key, value);
        editor.commit();
    }

    public String getSharedStringData(String key) {
        return getSharedPreferences().getString(key, null);
    }

    public long getSharedLongData(String key) {
        return getSharedPreferences().getLong(key, -1);
    }

    public void setSharedLongData(String key, long value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putLong(key, value);
        editor.commit();
    }

    public void setSharedBooleanData(String key, boolean value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getSharedBooleanData(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }

    public void setSharedFloatData(String key, float value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putFloat(key, value);
        editor.commit();
    }

    public float getSharedFloatData(String key) {
        return getSharedPreferences().getFloat(key, -1);
    }

    public boolean isKeyShared(String key) {
        return getSharedPreferences().contains(key);
    }

    private SharedPreferences.Editor getEditor() {
        return getSharedPreferences().edit();
    }

    public void clearData(String key) {
//        getEditor().remove(key).commit();
    }

    public void clearAll() {
        getEditor().clear().commit();
    }

}
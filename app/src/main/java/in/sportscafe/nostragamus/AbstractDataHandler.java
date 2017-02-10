package in.sportscafe.nostragamus;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jeeva on 6/4/16.
 */
public abstract class AbstractDataHandler implements Constants {

    public abstract String getPreferenceFileName();

    /**
     * Variable to hold the sharedPreference data
     */
    private SharedPreferences mSharedPreferences;

    public void init(Context context) {
        mSharedPreferences = context.getSharedPreferences(getPreferenceFileName(), Context.MODE_PRIVATE);
    }

    public void setSharedIntData(String key, int value) {
        getEditor().putInt(key, value).apply();
    }

    public int getSharedIntData(String key, int defVal) {
        return mSharedPreferences.getInt(key, defVal);
    }

    public void setSharedStringData(String key, String value) {
        getEditor().putString(key, value).apply();
    }

    public String getSharedStringData(String key) {
        return mSharedPreferences.getString(key, null);
    }

    public void setSharedLongData(String key, long value) {
        getEditor().putLong(key, value).apply();
    }

    public long getSharedLongData(String key, long defVal) {
        return mSharedPreferences.getLong(key, defVal);
    }

    public void setSharedBooleanData(String key, boolean value) {
        getEditor().putBoolean(key, value).apply();
    }

    public boolean getSharedBooleanData(String key, boolean defVal) {
        return mSharedPreferences.getBoolean(key, defVal);
    }

    public boolean isKeyShared(String key) {
        return mSharedPreferences.contains(key);
    }

    private SharedPreferences.Editor getEditor() {
        return mSharedPreferences.edit();
    }

    public void clearData(String key) {
        if (isKeyShared(key)) {
            getEditor().remove(key).apply();
        }
    }

    public void clearAll() {
        getEditor().clear().apply();
    }

}
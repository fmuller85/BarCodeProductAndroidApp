package fr.miage.barcodeproduct.utils;

import android.content.Context;
import android.content.SharedPreferences;

import fr.miage.barcodeproduct.R;

/**
 * Class that holds application preferences.
 */
public class ApplicationPreferences {
    /**
     * used for modifying values in a SharedPreferences prefs
     */
    private SharedPreferences.Editor prefsEditor;

    /**
     * reference to preference
     */
    private SharedPreferences prefs;

    /**
     * the context
     */
    private Context context;

    public ApplicationPreferences(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(context.getString(R.string.shared_pref),
                Context.MODE_PRIVATE);
        prefsEditor = prefs.edit();
    }

    public long getLongPreference(String key) {
        return prefs.getLong(key, 0);
    }

    public void saveLongPreference(String key, long value) {
        prefsEditor.putLong(key, value);
        prefsEditor.commit();
    }

    public int getIntPreference(String key) {
        return prefs.getInt(key, 0);
    }

    public String getStringPreference(String key) {
        return prefs.getString(key, "");
    }

    public boolean getBooleanPreference(String key) {
        return prefs.getBoolean(key, false);
    }

    public void saveStringPreference(String key, String value){
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public void saveBooleanPreference(String key, boolean value) {
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public void logout(){
        prefsEditor.putString("email", "");
        prefsEditor.putString("password", "");
        prefsEditor.putBoolean("userRegistered", false);
        prefsEditor.putBoolean("loaded", false);
        prefsEditor.commit();
    }

    public void saveUser(String email, String password){
        prefsEditor.putString("email", email);
        prefsEditor.putString("password", password);
        prefsEditor.putBoolean("userRegistered", true);
        prefsEditor.commit();
    }

    public boolean isUserRegistered(){
        return prefs.getBoolean("userRegistered", false);
    }

    public String getEmail(){
        return prefs.getString("email", "");
    }

    public String getPassword(){
        return prefs.getString("password", "");
    }
}

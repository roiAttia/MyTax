package roiattia.com.tax.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesUtil {

    public static final String KEY_VAT_RATE = "vat_rate";
    private static final int DEFAULT_VAT_RATE = 17;
    private static final String KEY_VERSION_UPDATE = "version_update";
    private static final boolean DEFAULT_VERSION_UPDATE = true;

    /**
     * set the new vat rate to shared preferences
     * @param vat the new vat rate
     */
    public static void setVatRate(Context context, int vat) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_VAT_RATE, vat);
        editor.apply();
    }

    /**
     * @return the stored vat rate
     */
    public static int getVatRate(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(KEY_VAT_RATE, DEFAULT_VAT_RATE);
    }

    /**
     * set boolean to false to indicate that it is not the first time the is running
     */
    public static void setFirstUpdate(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_VERSION_UPDATE, false);
        editor.apply();
    }

    /**
     * @return true if it is the first time the app is running
     */
    public static boolean isFirstUpdate(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(KEY_VERSION_UPDATE, DEFAULT_VERSION_UPDATE);
    }
}

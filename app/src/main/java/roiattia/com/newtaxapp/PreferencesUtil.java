package roiattia.com.newtaxapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesUtil {

    public static final String KEY_VAT_RATE = "vat_rate";
    private static final int DEFAULT_VAT_RATE = 17;

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
}

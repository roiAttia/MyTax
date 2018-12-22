package roiattia.com.tax.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static roiattia.com.tax.utils.Constants.DEFAULT_VAT_RATE;
import static roiattia.com.tax.utils.Constants.DEFAULT_VERSION_UPDATE;
import static roiattia.com.tax.utils.Constants.KEY_VAT_RATE;
import static roiattia.com.tax.utils.Constants.KEY_VERSION_UPDATE;

public class PreferencesUtil {

    private PreferencesUtil(){}

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

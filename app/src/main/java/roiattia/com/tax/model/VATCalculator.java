package roiattia.com.tax.model;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import roiattia.com.tax.R;
import roiattia.com.tax.utils.FormatsUtil;

import static roiattia.com.tax.utils.Constants.AMOUNT;
import static roiattia.com.tax.utils.Constants.CALCULATED_AMOUNT;
import static roiattia.com.tax.utils.Constants.DOT;
import static roiattia.com.tax.utils.Constants.EMPTY_STRING;
import static roiattia.com.tax.utils.Constants.VAT_AMOUNT;

public class VATCalculator {

    private static final String TAG = VATCalculator.class.getSimpleName();
    private static VATCalculator sInstance = null;

    private double mAmount,mCalculatedAmount,mVatAmount;
    private String mAmountString, mCalculatedAmountString, mVatAmountString;
    private final String mEmptyString = EMPTY_STRING;
    private int mVatRate;
    private boolean mIsAddVat;

    private VATCalculator() {
        resetData();
        mIsAddVat = true;
    }

    public static VATCalculator getInstance() {
        if (sInstance == null) {
            sInstance = new VATCalculator();
        }
        return(sInstance);
    }

    /**
     * Calculator digit click event
     * @param digit a digit - from 0 to 9
     */
    public void digitButton(String digit) {
        // check if it's the first digit clicked. if not then add it to the
        // current amount
        if(mAmountString.equals(mEmptyString)){
            mAmountString = digit;
        } else {
            mAmountString = mAmountString+ digit;
        }
        calculate();
    }

    /**
     * Calculator dot click event
     */
    public void dotButton(Context context) {
        // check if current amount already contains a dot. only one dot allowed
        if(mAmountString.contains(".")){
            Toast.makeText(context, R.string.only_one_dot, Toast.LENGTH_SHORT).show();
        } else {
            mAmountString += DOT;
        }
    }

    /**
     * Calculator delete click event
     */
    public void deleteButton(){
        if(mAmountString.length() == 1){
            resetData();
        } else if(!mAmountString.equals(EMPTY_STRING)) {
            mAmountString = mAmountString.substring(0, mAmountString.length()-1);
        }
        calculate();
    }

    /**
     * Calculator long delete click event - delete entire amount
     */
    public void longDeleteButton(){
        resetData();
    }

    /**
     * Set the calculation type - add vat or reduce vat
     * @param isAddVat - true for add vat, false for reduce vat
     */
    public void setIsAddVat(boolean isAddVat) {
        mIsAddVat = isAddVat;
        calculate();
    }

    /**
     * Set vat rate
     * @param vatRate - new vat rate
     */
    public void setVatRate(int vatRate) {
        mVatRate = vatRate;
        calculate();
    }

    /**
     * Get all amounts packed in a bundle
     */
    public Bundle getAmounts(){
        Bundle bundle = new Bundle();
        bundle.putString(AMOUNT, mAmountString);
        bundle.putString(CALCULATED_AMOUNT, mCalculatedAmountString);
        bundle.putString(VAT_AMOUNT, mVatAmountString);
        return bundle;
    }

    /**
     * Get vat type calculation
     */
    public boolean getIsVat() {
        return mIsAddVat;
    }

    /**
     * Get vat rate
     */
    public int getVatRate() {
        return mVatRate;
    }

    /**
     * Reset data to empty strings
     */
    private void resetData() {
        mAmountString = mEmptyString;
        mCalculatedAmountString = mEmptyString;
        mVatAmountString = mEmptyString;
    }

    /**
     * Handle reduce vat calculations
     */
    private void reduceVatCalculations() {
        mAmount = Double.parseDouble(mAmountString.replaceAll(",",""));
        mCalculatedAmount = mAmount / (1 + mVatRate / 100.0);
        mVatAmount = mAmount - mCalculatedAmount;
    }

    /**
     * Handle add vat calculations
     */
    private void addVatCalculations() {
        mAmount = Double.parseDouble(mAmountString.replaceAll(",",""));
        mCalculatedAmount = mAmount * (1 + mVatRate / 100.0);
        mVatAmount = mCalculatedAmount - mAmount;
    }

    /**
     * Handle strings update with current amounts
     */
    private void updateStrings() {
        mAmountString = FormatsUtil.getStringFormatFromDouble(mAmount);
        mCalculatedAmountString = FormatsUtil.getStringFormatFromDouble(mCalculatedAmount);
        mVatAmountString = FormatsUtil.getStringFormatFromDouble(mVatAmount);
    }

    /**
     * Handle calculations type
     */
    private void calculate() {
        try {
            if(mIsAddVat){
                addVatCalculations();
            } else {
                reduceVatCalculations();
            }
            updateStrings();
        } catch (Exception e){
            Log.i(TAG, e.getMessage());
        }
    }
}

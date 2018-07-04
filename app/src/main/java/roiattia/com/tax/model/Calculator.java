package roiattia.com.tax.model;

import roiattia.com.tax.utils.FormatsUtil;

public class Calculator {

    private static Calculator INSTANCE = null;

    private double mCurrentAmount;
    private double mNewAmount;
    private double mVatAmount;
    private int mVat;
    private String mCurrentAmountString;
    public static final String ZERO_STRING = "--";

    private Calculator() {
        mCurrentAmount = 0;
        mNewAmount = 0;
        mCurrentAmountString = ZERO_STRING;
    }

    public static Calculator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Calculator();
        }
        return(INSTANCE);
    }

    /**
     * Handle add Vat to amount calculations
     */
    public void calculateSumAddVat(){
        mNewAmount = mCurrentAmount * (1 + mVat / 100.0);
        mVatAmount = mNewAmount - mCurrentAmount;
    }

    /**
     * Handle subtract Vat to amount calculations
     */
    public void calculateSumReduceVat(){
        mNewAmount = mCurrentAmount / (1 + mVat / 100.0);
        mVatAmount = mCurrentAmount - mNewAmount;
    }

    /**
     * Handle delete digit
     */
    public void calculateDeleteDigit(){
        // check if current amount is zero then no delete is required
        if(mCurrentAmountString.equals(ZERO_STRING)){
            mCurrentAmount = 0;
        } else {
            mCurrentAmountString = FormatsUtil.removeLastDigit(mCurrentAmountString);
            // check if digit deleted was the last digit
            if (mCurrentAmountString.equals("")) {
                mCurrentAmount = 0;
            } else {
                mCurrentAmount = Double.parseDouble(mCurrentAmountString);
            }
        }
    }

    /**
     * Handle add dot
     */
    public void addDot() {
        mCurrentAmountString = FormatsUtil.addDot(mCurrentAmountString);
    }

    /**
     * Handle add new digit to amount
     * @param newDigit the new digit to add
     */
    public void updateAmount(int newDigit){
        mCurrentAmountString = FormatsUtil.addNewDigitToString(mCurrentAmountString, newDigit);
        mCurrentAmount = Double.parseDouble(mCurrentAmountString);
    }

    /**
     * Handle reset data
     */
    public void resetData() {
        mCurrentAmount = 0;
        mNewAmount = 0;
        mCurrentAmountString = ZERO_STRING;
    }

    public double getCurrentAmount() {
        return mCurrentAmount;
    }

    public double getNewAmount() {
        return mNewAmount;
    }

    public double getVatAmount() {
        return mVatAmount;
    }

    public void setVat(int vat) {
        mVat = vat;
    }

}

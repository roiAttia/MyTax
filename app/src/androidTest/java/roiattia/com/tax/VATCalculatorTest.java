package roiattia.com.tax;

import android.content.Context;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import roiattia.com.tax.model.VATCalculator;
import roiattia.com.tax.utils.Constants;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class VATCalculatorTest {

    private VATCalculator mVATCalculator;

    @Before
    public void prepareTest(){
        mVATCalculator = VATCalculator.getInstance();
        mVATCalculator.setVatRate(17);
    }

    @Test
    public void addDigits(){
        mVATCalculator.digitButton("5");
        Bundle bundle = mVATCalculator.getAmounts();
        assertEquals(bundle.getString(Constants.AMOUNT), "5");
        assertEquals(bundle.getString(Constants.CALCULATED_AMOUNT), "5.85");
        assertEquals(bundle.getString(Constants.VAT_AMOUNT), "0.85");
        mVATCalculator.longDeleteButton();
    }

    @Test
    public void addDot(){
        Context context = InstrumentationRegistry.getTargetContext();
        mVATCalculator.digitButton("6");
        mVATCalculator.dotButton(context);
        mVATCalculator.digitButton("5");
        Bundle bundle = mVATCalculator.getAmounts();
        assertEquals(bundle.getString(Constants.AMOUNT), "6.5");
        assertEquals(bundle.getString(Constants.CALCULATED_AMOUNT), "7.605");
        assertEquals(bundle.getString(Constants.VAT_AMOUNT), "1.105");
        mVATCalculator.longDeleteButton();
    }

    @Test
    public void deleteDigit(){
        mVATCalculator.digitButton("5");
        mVATCalculator.digitButton("0");
        mVATCalculator.deleteButton();
        Bundle bundle = mVATCalculator.getAmounts();
        assertEquals(bundle.getString(Constants.AMOUNT), "5");
        assertEquals(bundle.getString(Constants.CALCULATED_AMOUNT), "5.85");
        assertEquals(bundle.getString(Constants.VAT_AMOUNT), "0.85");
        mVATCalculator.longDeleteButton();
    }

    @Test
    public void longDelete(){
        mVATCalculator.digitButton("5");
        mVATCalculator.digitButton("0");
        mVATCalculator.longDeleteButton();
        Bundle bundle = mVATCalculator.getAmounts();
        assertEquals(bundle.getString(Constants.AMOUNT), Constants.EMPTY_STRING);
        assertEquals(bundle.getString(Constants.CALCULATED_AMOUNT), Constants.EMPTY_STRING);
        assertEquals(bundle.getString(Constants.VAT_AMOUNT), Constants.EMPTY_STRING);
        mVATCalculator.longDeleteButton();
    }

    @Test
    public void changeToReduceVat(){
        mVATCalculator.digitButton("5");
        mVATCalculator.digitButton("0");
        mVATCalculator.setIsAddVat(false);
        Bundle bundle = mVATCalculator.getAmounts();
        assertEquals(bundle.getString(Constants.AMOUNT), "50");
        assertEquals(bundle.getString(Constants.CALCULATED_AMOUNT), "42.735");
        assertEquals(bundle.getString(Constants.VAT_AMOUNT), "7.265");
        mVATCalculator.setIsAddVat(true);
        mVATCalculator.longDeleteButton();
    }

}

package roiattia.com.tax;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TaxFragment extends Fragment {

    private static final String BEFORE_NUMBER = "before_number";
    private static final String VAT_NUMBER = "vat_number";
    private static final String AFTER_NUMBER = "after_number";
    private static final String ADD_VAT = "add_vat";
    private static final String EXCLUDE_VAT = "exclude_vat";

    @BindView(R.id.tv_before_calc) TextView mBeforeCalcText;
    @BindView(R.id.tv_after_calc) TextView mAfterCalcText;
    @BindView(R.id.tv_vat) TextView mVatText;
    @BindView(R.id.tv_vat_headline) TextView mVatHeadlineText;
    @BindView(R.id.tv_input_amount_headline) TextView mInputHeadlineText;
    @BindView(R.id.tv_calculated_amount_headline) TextView mCalculatedHeadlineText;
    @BindView(R.id.rb_add_vat) RadioButton mAddVatRb;
    @BindView(R.id.rb_subtract_vat) RadioButton mSubtractVatRb;
    @BindView(R.id.calc_delete) FrameLayout mDeleteButton;
    @BindString(R.string.text_no_number) String mResetAmountsText;

    private int mVat;
    private double mCurrentNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_tax, container, false);
        ButterKnife.bind(this, rootview);

        mVatHeadlineText.setText(String.format(Locale.getDefault(), "%d %s", mVat, getString(R.string.text_vat)));

        // check savedInstanceState in case of screen rotation
        if (savedInstanceState != null){
            mBeforeCalcText.setText(savedInstanceState.getString(BEFORE_NUMBER));
            mVatText.setText(savedInstanceState.getString(VAT_NUMBER));
            mAfterCalcText.setText(savedInstanceState.getString(AFTER_NUMBER));
            mAddVatRb.setChecked(savedInstanceState.getBoolean(ADD_VAT));
            mSubtractVatRb.setChecked(savedInstanceState.getBoolean(EXCLUDE_VAT));
        }

        headlinesUpdate();

        // set include vat radio button on change listener
        mAddVatRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                headlinesUpdate();
                if(!mResetAmountsText.equals(mBeforeCalcText.getText().toString())) {
                    calculateSum();
                }
            }
        });

        // set exclude vat radio button on change listener
        mSubtractVatRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                headlinesUpdate();
                if(!mResetAmountsText.equals(mBeforeCalcText.getText().toString())) {
                    calculateSum();
                }
            }
        });

        // set delete long click listener to delete all input
        mDeleteButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                resetCalculations();
                return true;
            }
        });

        return rootview;
    }

    /**
     * update information card's headlines according to radio buttons
     */
    private void headlinesUpdate() {
        if(mAddVatRb.isChecked()){
            mInputHeadlineText.setText(getString(R.string.text_sum_without_vat));
            mCalculatedHeadlineText.setText(getString(R.string.text_sum_with_vat));
        }
        else {
            mInputHeadlineText.setText(getString(R.string.text_sum_with_vat));
            mCalculatedHeadlineText.setText(getString(R.string.text_sum_without_vat));
        }
    }

    /**
     * add clicked number to the input amount
     * @param newNumber the number to be added
     */
    public void calculatorAddNumber(int newNumber){
        String oldAmountString = clearCommas(mBeforeCalcText.getText().toString());
        String newAmountString = "";
        // if amount has reached max digits toast a message
        // else add newNumber
        if(oldAmountString.length() + 1 == 10) {
            Toast.makeText(getContext(), R.string.max_number_message, Toast.LENGTH_SHORT).show();
        } else {
            // if the current number is 0, if so then overwrite it with newNumber
            // else add newNumber
            if (oldAmountString.equals(mResetAmountsText)) {
                newAmountString = String.valueOf(newNumber);
                mBeforeCalcText.setText(newAmountString);
            }
            else {
                // if newNumber and current amount are not 0 then add newNumber
                if(!(newNumber == 0 && mCurrentNumber == 0)) {
                    newAmountString = (oldAmountString + newNumber);
                    mBeforeCalcText.setText(new DecimalFormat("#,###,###.###").
                            format(Double.parseDouble(newAmountString)));
                }
            }
            calculateSum();
        }
    }

    /**
     * do calculations on the input amount
     */
    private void calculateSum() {
        double vat;
        double result;
        mCurrentNumber = Double.parseDouble(clearCommas(mBeforeCalcText.getText().toString()));
        // do vat calculation according to which radio button is checked
        if (mSubtractVatRb.isChecked()) {
            result = mCurrentNumber / (1 + mVat / 100.0);
            vat = mCurrentNumber - result;
        }
        else {
            result = mCurrentNumber * (1 + mVat / 100.0);
            vat = result - mCurrentNumber;
        }
        mAfterCalcText.setText(new DecimalFormat("#,###,###.###").format(result));
        mVatText.setText(new DecimalFormat("#,###,###.###").format(vat));
    }

    /**
     * handle delete on the last digit of the input amount,
     * then calculate the new amount
     */
    public void calculatorDelete(){
        String oldAmountString = clearCommas(mBeforeCalcText.getText().toString());
        // if current amount is not "--" or is larger then one digit
        // then delete last digit.
        // else reset.
        if (!oldAmountString.equals(mResetAmountsText)
                && oldAmountString.length() > 1) {
            String newAmountString = oldAmountString.substring(0, oldAmountString.length() - 1);
            mBeforeCalcText.setText(new DecimalFormat("#,###,###.###").
                    format(Double.parseDouble(newAmountString)));
            calculateSum();
        }
        else {
            resetCalculations();
        }
    }

    /**
     * reset all Amounts text to "--" and the current amount to 0
     */
    private void resetCalculations() {
        mCurrentNumber = 0;
        mBeforeCalcText.setText(mResetAmountsText);
        mVatText.setText(mResetAmountsText);
        mAfterCalcText.setText(mResetAmountsText);
    }

    /**
     * handle dot button click
     */
    public void calculatorDot() {
        // check if there is already a dot in the amount as there can
        // be only one dot
        String currentAmount = mBeforeCalcText.getText().toString();
        if(!currentAmount.contains(".")
                && !currentAmount.equals(mResetAmountsText)) {
            mBeforeCalcText.append(getString(R.string.dot));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BEFORE_NUMBER, String.valueOf(mBeforeCalcText.getText()));
        outState.putString(VAT_NUMBER, String.valueOf(mVatText.getText()));
        outState.putString(AFTER_NUMBER, String.valueOf(mAfterCalcText.getText()));
        outState.putBoolean(ADD_VAT, mAddVatRb.isChecked());
        outState.putBoolean(EXCLUDE_VAT, mSubtractVatRb.isChecked());
    }

    /**
     * update the new vat rate
     */
    public void updateVat(Context context) {
        mVat = PreferencesUtil.getVatRate(context);
        // check if headline isn't null in case of app just started
        if(mVatHeadlineText != null) {
            mVatHeadlineText.setText(String.format(Locale.getDefault(), "%d %s", mVat, getString(R.string.text_vat)));
            // calculate only if there is an amount to calculate
            if(!mResetAmountsText.equals(mBeforeCalcText.getText().toString())){
                calculateSum();
            }
        }
    }

    private String clearCommas(String originalString){
        return originalString.replace(",", "");
    }
}

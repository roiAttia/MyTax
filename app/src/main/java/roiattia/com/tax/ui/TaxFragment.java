package roiattia.com.tax.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.tax.model.Calculator;
import roiattia.com.tax.utils.PreferencesUtil;
import roiattia.com.tax.R;

public class TaxFragment extends Fragment {

    private static final String BEFORE_NUMBER = "before_number";
    private static final String VAT_NUMBER = "vat_number";
    private static final String AFTER_NUMBER = "after_number";
    private static final String ADD_VAT = "add_vat";
    private static final String EXCLUDE_VAT = "exclude_vat";

    private int mVat;
    private Calculator mCalculator;
    private LongDeleteListener mLongDeleteListener;

    @BindView(R.id.tv_before_calc) TextView mBeforeCalcText;
    @BindView(R.id.tv_after_calc) TextView mAfterCalcText;
    @BindView(R.id.tv_vat) TextView mVatText;
    @BindView(R.id.tv_vat_headline) TextView mVatHeadlineText;
    @BindView(R.id.tv_input_amount_headline) TextView mInputHeadlineText;
    @BindView(R.id.tv_calculated_amount_headline) TextView mCalculatedHeadlineText;
    @BindView(R.id.rb_add_vat) RadioButton mAddVatRb;
    @BindView(R.id.rb_subtract_vat) RadioButton mSubtractVatRb;
    @BindView(R.id.btn_calc_delete) FrameLayout mDeleteButton;
    @BindString(R.string.text_no_number) String mResetAmountsText;

    public interface LongDeleteListener{
        void onDeleteLongClick();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_tax, container, false);
        ButterKnife.bind(this, rootview);

        mCalculator = Calculator.getInstance();

        mVatHeadlineText.setText(String.format(Locale.getDefault(), "%d %s", mVat, getString(R.string.text_vat)));

        // check savedInstanceState in case of screen rotation
        if (savedInstanceState != null){
            mBeforeCalcText.setText(savedInstanceState.getString(BEFORE_NUMBER));
            mVatText.setText(savedInstanceState.getString(VAT_NUMBER));
            mAfterCalcText.setText(savedInstanceState.getString(AFTER_NUMBER));
            mAddVatRb.setChecked(savedInstanceState.getBoolean(ADD_VAT));
            mSubtractVatRb.setChecked(savedInstanceState.getBoolean(EXCLUDE_VAT));
        }

        // set delete long click listener to delete all input
        mDeleteButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                resetCalculations();
                mLongDeleteListener.onDeleteLongClick();
                return true;
            }
        });

        return rootview;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLongDeleteListener = (LongDeleteListener) context;
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
     * Update the new vat rate
     */
    public void updateVat(Context context) {
        mVat = PreferencesUtil.getVatRate(context);
        // check if headline isn't null in case of app just started
        if(mVatHeadlineText != null) {
            mVatHeadlineText.setText(String.format(Locale.getDefault(),
                    "%d %s", mVat, getString(R.string.text_vat)));
        }
    }

    // Update amounts textviews with data from mCalculator
    public void updateUi() {
        if(mCalculator.getCurrentAmount() == 0){
            resetCalculations();
        } else {
            mBeforeCalcText.setText(new DecimalFormat("#,###,###.###")
                    .format(mCalculator.getCurrentAmount()));
            mAfterCalcText.setText(new DecimalFormat("#,###,###.###")
                    .format(mCalculator.getNewAmount()));
            mVatText.setText(new DecimalFormat("#,###,###.###")
                    .format(mCalculator.getVatAmount()));
        }
    }

    /**
     * Update information card's headlines according to radio buttons
     */
    public void updateHeadlines(boolean isAddVat) {
        if(isAddVat){
            mInputHeadlineText.setText(getString(R.string.text_sum_without_vat));
            mCalculatedHeadlineText.setText(getString(R.string.text_sum_with_vat));
        }
        else {
            mInputHeadlineText.setText(getString(R.string.text_sum_with_vat));
            mCalculatedHeadlineText.setText(getString(R.string.text_sum_without_vat));
        }
    }

    /**
     * Add dot to the mBeforeCalcText textView
     */
    public void addDot() {
        String currentAmount = mBeforeCalcText.getText().toString();
        if(!currentAmount.contains(".")
                && !currentAmount.equals(mResetAmountsText)) {
            mBeforeCalcText.append(getString(R.string.dot));
        }
    }

    /**
     * Reset all Amounts text to "--"
     */
    private void resetCalculations() {
        mBeforeCalcText.setText(Calculator.ZERO_STRING);
        mVatText.setText(Calculator.ZERO_STRING);
        mAfterCalcText.setText(Calculator.ZERO_STRING);
    } 
}

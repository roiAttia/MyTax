package roiattia.com.newtaxapp;

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

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TaxFragment extends Fragment {

    private static final String BEFORE_NUMBER = "before_number";
    private static final String VAT_NUMBER = "vat_number";
    private static final String AFTER_NUMBER = "after_number";

    @BindView(R.id.tv_before_calc) TextView mBeforeCalcText;
    @BindView(R.id.tv_after_calc) TextView mAfterCalcText;
    @BindView(R.id.tv_vat) TextView mVatText;
    @BindView(R.id.tv_vat_headline) TextView mVatHeadlineText;
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

        mVatHeadlineText.setText(String.format("%d %s", mVat, getString(R.string.text_vat)));

        if (savedInstanceState != null){
            mBeforeCalcText.setText(savedInstanceState.getString(BEFORE_NUMBER));
            mVatText.setText(savedInstanceState.getString(VAT_NUMBER));
            mAfterCalcText.setText(savedInstanceState.getString(AFTER_NUMBER));
        }

        CompoundButton.OnCheckedChangeListener checkedChangeListener =
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        calculateSum();
                    }
                };

        mAddVatRb.setOnCheckedChangeListener(checkedChangeListener);
        mSubtractVatRb.setOnCheckedChangeListener(checkedChangeListener);

        mDeleteButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mCurrentNumber = 0;
                mBeforeCalcText.setText(mResetAmountsText);
                mVatText.setText(mResetAmountsText);
                mAfterCalcText.setText(mResetAmountsText);
                return true;
            }
        });

        return rootview;
    }

    public void calculatorAddNumber(int newNumber){
        // if the current number is "--", if so then overwrite it
        // else add it
        if (mBeforeCalcText.getText().equals(mResetAmountsText)) {
            mBeforeCalcText.setText(String.valueOf(newNumber));
        } else {
            if(mBeforeCalcText.getText().length() + 1 == 10) {
                Toast.makeText(getContext(), R.string.max_number_message, Toast.LENGTH_SHORT).show();
            } else {
                mCurrentNumber = Double.parseDouble(String.valueOf(mBeforeCalcText.getText()));
                // if the current number > 0 then append the new number to it
                // else it's 0 and then overwrite it
                if (mCurrentNumber != 0) {
                    mBeforeCalcText.append(String.valueOf(newNumber));
                } else {
                    mBeforeCalcText.setText(String.valueOf(newNumber));
                }
            }
        }
        calculateSum();
    }

    private void calculateSum() {
        double vat;
        double result;
        mCurrentNumber = Double.parseDouble(String.valueOf(mBeforeCalcText.getText()));
        if (mAddVatRb.isChecked()) {
            result = mCurrentNumber / (1 + mVat / 100.0);
            vat = mCurrentNumber - result;
        } else {
            result = mCurrentNumber * (1 + mVat / 100.0);
            vat = result - mCurrentNumber;
        }
        mAfterCalcText.setText(new DecimalFormat("#.###").format(result));
        mVatText.setText(new DecimalFormat("#.###").format(vat));
    }

    public void calculatorDelete(){
        if (!mBeforeCalcText.getText().equals(mResetAmountsText)) {
            String numberInString = mBeforeCalcText.getText().toString();
            if(numberInString.length() <= 1){
                mBeforeCalcText.setText(mResetAmountsText);
            } else {
                mBeforeCalcText.setText(numberInString.substring(0, numberInString.length() - 1));
            }
            calculateSum();
        }
    }

    public void calculatorDot() {
        if(!mBeforeCalcText.getText().toString().contains(".")) {
            mBeforeCalcText.append(getString(R.string.dot));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BEFORE_NUMBER, String.valueOf(mBeforeCalcText.getText()));
        outState.putString(VAT_NUMBER, String.valueOf(mAfterCalcText.getText()));
        outState.putString(AFTER_NUMBER, String.valueOf(mVatText.getText()));
    }

    public void updateVat(Context context) {
        mVat = PreferencesUtil.getVatRate(context);
        if(mVatHeadlineText != null) {
            mVatHeadlineText.setText(String.format("%d %s", mVat, getString(R.string.text_vat)));
            calculateSum();
        }
    }
}

package roiattia.com.newtaxapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaxFragment extends Fragment {

    private static final String TAG = TaxFragment.class.getSimpleName();
    @BindView(R.id.tv_before_calc) TextView mBeforeCalcText;
    @BindView(R.id.tv_after_calc) TextView mAfterCalcText;
    @BindView(R.id.tv_vat) TextView mVatText;
    @BindView(R.id.rb_add_vat) RadioButton mAddVatRb;
    @BindView(R.id.rb_subtract_vat) RadioButton mSubtractVatRb;

    private int mTax = 17;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_tax, container, false);
        ButterKnife.bind(this, rootview);

        CompoundButton.OnCheckedChangeListener checkedChangeListener =
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        calculateSum();
                    }
                };

        mAddVatRb.setOnCheckedChangeListener(checkedChangeListener);
        mSubtractVatRb.setOnCheckedChangeListener(checkedChangeListener);

        return rootview;
    }

    public void calculatorAddNumber(int newNumber){
        Log.i(TAG, String.valueOf(mBeforeCalcText.getText()));
        // if the current number is "--", if so then overwrite it
        if(mBeforeCalcText.getText().equals("--")){
            mBeforeCalcText.setText(String.valueOf(newNumber));
        } else {
            int oldNumber = Integer.parseInt(String.valueOf(mBeforeCalcText.getText()));
            // if the current number > 0 then append the new number to it
            // else it's 0 and then overwrite it
            if(oldNumber > 0) {
                mBeforeCalcText.append(String.valueOf(newNumber));
            } else {
                mBeforeCalcText.setText(String.valueOf(newNumber));
            }
        }

        calculateSum();
    }

    private void calculateSum() {
        int number = Integer.parseInt(String.valueOf(mBeforeCalcText.getText()));
        double vat;
        double result;
        if(mAddVatRb.isChecked()){
            result = number/(1+mTax/100.0);
            vat = number - result;
        } else {
            result = number*(1+mTax/100.0);
            vat = result - number;
        }
        mAfterCalcText.setText(new DecimalFormat("#.###").format(result));
        mVatText.setText(new DecimalFormat("#.###").format(vat));
    }

    public void calculatorDelete(){
        if(!(mBeforeCalcText.getText().equals("--") || (mBeforeCalcText.getText().equals("0")))){
            int sum = Integer.parseInt(String.valueOf(mBeforeCalcText.getText()));
            sum = sum/10;
            mBeforeCalcText.setText(String.valueOf(sum));
            calculateSum();
        }
    }

    public void calculatorDeleteAll(){

    }
}

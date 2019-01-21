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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import roiattia.com.tax.R;
import roiattia.com.tax.model.VATCalculator;
import roiattia.com.tax.utils.Constants;
import roiattia.com.tax.utils.PreferencesUtil;

public class CalculatorFragment extends Fragment {

    public CalculatorFragment(){}

    private OnCalculatorFragmentListener mListener;
    private VATCalculator mVATCalculator;

    public interface OnCalculatorFragmentListener{
        void onVatConfigChange(int viewId);
    }

    @BindView(R.id.tv_vat_headline) TextView mVatHeadlineText;
    @BindView(R.id.tv_input_amount_headline) TextView mInputHeadlineText;
    @BindView(R.id.tv_calculated_amount_headline) TextView mCalculatedHeadlineText;
    @BindView(R.id.tv_before_calc) TextView mBeforeCalcText;
    @BindView(R.id.tv_after_calc) TextView mAfterCalcText;
    @BindView(R.id.tv_vat) TextView mVatText;
    @BindView(R.id.btn_calc_delete) FrameLayout mDeleteButton;

    @OnClick({R.id.rb_subtract_vat, R.id.rb_add_vat})
    public void changeVatConfig(View view){
        mListener.onVatConfigChange(view.getId());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVATCalculator = VATCalculator.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calculator, container, false);
        ButterKnife.bind(this, rootView);

        mDeleteButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mVATCalculator.longDeleteButton();
                setAmountTexts(mVATCalculator.getAmounts());
                return true;
            }
        });

        updateVatHeadline();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnCalculatorFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCalculatorFragmentListener");
        }
    }

    public void updateVatHeadline(){
        mVatHeadlineText.setText(String.format(Locale.getDefault(),"%d %s",
                mVATCalculator.getVatRate(),
                getString(R.string.text_vat)));
    }

    /**
     * Update information card's headlines according to radio buttons
     */
    public void updateHeadlines(){
        if(mVATCalculator.getIsVat()){
            mInputHeadlineText.setText(getString(R.string.text_sum_without_vat));
            mCalculatedHeadlineText.setText(getString(R.string.text_sum_with_vat));
        }
        else {
            mInputHeadlineText.setText(getString(R.string.text_sum_with_vat));
            mCalculatedHeadlineText.setText(getString(R.string.text_sum_without_vat));
        }
    }

    public void updateAmountTexts(){
        setAmountTexts(mVATCalculator.getAmounts());
    }

    /**
     *
     * Update UI texts with current amounts
     * @param bundle bundle of amounts received from calculator
     */
    private void setAmountTexts(Bundle bundle) {
        mBeforeCalcText.setText(bundle.getString(Constants.AMOUNT));
        mAfterCalcText.setText(bundle.getString(Constants.CALCULATED_AMOUNT));
        mVatText.setText(bundle.getString(Constants.VAT_AMOUNT));
    }
}

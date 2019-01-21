package roiattia.com.tax.ui;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.tax.R;

public class CalculatorFragment extends Fragment {

    public CalculatorFragment(){}

    @BindView(R.id.tv_vat_headline) TextView mVatHeadlineText;
    @BindView(R.id.tv_input_amount_headline) TextView mInputHeadlineText;
    @BindView(R.id.tv_calculated_amount_headline) TextView mCalculatedHeadlineText;
    @BindView(R.id.tv_before_calc) TextView mBeforeCalcText;
    @BindView(R.id.tv_after_calc) TextView mAfterCalcText;
    @BindView(R.id.tv_vat) TextView mVatText;
    @BindView(R.id.btn_calc_delete) FrameLayout mDeleteButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calculator, container, false);
        ButterKnife.bind(this, rootView);

        mDeleteButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getContext(), "check", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return rootView;
    }
}

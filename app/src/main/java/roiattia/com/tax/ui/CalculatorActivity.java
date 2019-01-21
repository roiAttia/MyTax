package roiattia.com.tax.ui;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.tax.R;
import roiattia.com.tax.model.VATCalculator;
import roiattia.com.tax.utils.PreferencesUtil;

import static roiattia.com.tax.utils.Constants.AMOUNT;
import static roiattia.com.tax.utils.Constants.CALCULATED_AMOUNT;
import static roiattia.com.tax.utils.Constants.KEY_VAT_RATE;
import static roiattia.com.tax.utils.Constants.VAT_AMOUNT;

public class CalculatorActivity extends AppCompatActivity
    implements EditTextDialog.EditTextDialogListener,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private VATCalculator mVATCalculator;
    private EditTextDialog mUpdateVatRateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        ButterKnife.bind(this);

        mVATCalculator = VATCalculator.getInstance();

        setupSharedPreferences();

//        setupUI();

        if(savedInstanceState != null){
            getAmounts();
        } else {
            CalculatorFragment calculatorFragment = new CalculatorFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_fragment_placeholder, calculatorFragment)
                    .commit();
        }
    }

    /**
     * Handle calculator's button's onClick method
     * @param view the button that was clicked
     */
    public void calculatorClick(View view){
        int viewId = view.getId();
        if(viewId == R.id.btn_calc_delete){ // delete button
            mVATCalculator.deleteButton();
        } else if(viewId == R.id.btn_calc_dot){ // dot button
            mVATCalculator.dotButton(this);
        } else { // digit button
            Button button = (Button) view;
            String digitString = button.getText().toString();
            mVATCalculator.digitButton(digitString);
        }
        getAmounts();
    }

    /**
     * Setup shared preferences and get it's data - updated VAT rate
     */
    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        mVATCalculator.setVatRate(PreferencesUtil.getVatRate(this));
//        mVatHeadlineText.setText(String.format(Locale.getDefault(),"%d %s",
//                PreferencesUtil.getVatRate(this),
//                getString(R.string.text_vat)));
    }

    /**
     * Setup UI elements - radio buttons and delete button
     */
    private void setupUI() {
//        mAddVatRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    mVATCalculator.setIsAddVat(true);
//                }
//                updateHeadlines();
//                getAmounts();
//            }
//        });
//        mSubtractVatRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    mVATCalculator.setIsAddVat(false);
//                }
//                updateHeadlines();
//                getAmounts();
//            }
//        });
//        mDeleteButton.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                mVATCalculator.longDeleteButton();
//                getAmounts();
//                return true;
//            }
//        });
    }

    /**
     * Invoke VAT Calculator for updated amounts
     */
    private void getAmounts() {
        Bundle bundle = mVATCalculator.getAmounts();
        updateAmountTexts(bundle);
    }

    /**
     * Update UI texts with current amounts
     * @param bundle bundle of amounts received from calculator
     */
    private void updateAmountTexts(Bundle bundle) {
//        mBeforeCalcText.setText(bundle.getString(AMOUNT));
//        mAfterCalcText.setText(bundle.getString(CALCULATED_AMOUNT));
//        mVatText.setText(bundle.getString(VAT_AMOUNT));
    }

    /**
     * Update information card's headlines according to radio buttons
     */
    private void updateHeadlines() {
        if(mVATCalculator.getIsVat()){
//            mInputHeadlineText.setText(getString(R.string.text_sum_without_vat));
//            mCalculatedHeadlineText.setText(getString(R.string.text_sum_with_vat));
//        }
//        else {
//            mInputHeadlineText.setText(getString(R.string.text_sum_with_vat));
//            mCalculatedHeadlineText.setText(getString(R.string.text_sum_without_vat));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.mi_vat_update){
            if(mUpdateVatRateDialog == null){
                mUpdateVatRateDialog = new EditTextDialog();
            }
            mUpdateVatRateDialog.setStrings(getString(R.string.update_vat_dialog_title),
                    getString(R.string.update_vat_dialog_message)
                    + mVATCalculator.getVatRate() + "%");
            mUpdateVatRateDialog.show(getSupportFragmentManager(),"update vat");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogFinishClick(String input) {
        try{
            int vatRate = Integer.parseInt(input);
            PreferencesUtil.setVatRate(this, vatRate);
            Toast.makeText(this, R.string.update_vat_dialog_success, Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(this, R.string.update_vat_dialog_error_message, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_VAT_RATE)) {
            mVATCalculator.setVatRate(PreferencesUtil.getVatRate(this));
//            mVatHeadlineText.setText(String.format(Locale.getDefault(),"%d %s",
//                    PreferencesUtil.getVatRate(this),
//                    getString(R.string.text_vat)));
            getAmounts();
        }
    }
}

package roiattia.com.tax.ui;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.Locale;

import roiattia.com.tax.model.Calculator;
import roiattia.com.tax.utils.PreferencesUtil;
import roiattia.com.tax.R;

public class MainActivity extends AppCompatActivity
    implements VatUpdateDialog.VatDialogListener,
        SharedPreferences.OnSharedPreferenceChangeListener,
        TaxFragment.LongDeleteListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TAX_FRAGMENT = "tax_fragment";
    private static final String VAT_DIALOG = "vat_dialog";
    private static final String IS_ADD_VAT = "is_add_vat";

    private TaxFragment mTaxFragment;
    private FrameLayout mLayout;
    private boolean mIsAddVat;
    private Calculator mCalculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayout = findViewById(R.id.layout_main);
        mCalculator = Calculator.getInstance();

        // check for savedInstanceState data
        if(savedInstanceState == null) {
            mTaxFragment = new TaxFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.layout_main, mTaxFragment)
                    .commit();
            mIsAddVat = true;
        } else {
            mTaxFragment = (TaxFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, TAX_FRAGMENT);
            mIsAddVat = savedInstanceState.getBoolean(IS_ADD_VAT);
        }

        setupSharedPreferences();
    }

    @Override
    public void onDeleteLongClick() {
        mCalculator.resetData();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PreferencesUtil.KEY_VAT_RATE)) {
            mTaxFragment.updateVat(this);
            mCalculator.setVat(PreferencesUtil.getVatRate(this));
            triggerCalculator();
        }
    }

    @Override
    public void OnDialogVatUpdateHandler(int newVat) {
        Snackbar.make(mLayout, String.format(Locale.getDefault(), "%s %d%s", getString(R.string.vat_update_message),
                        newVat, getString(R.string.precentege_sign)) , Snackbar.LENGTH_SHORT).show();
        PreferencesUtil.setVatRate(this, newVat);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.mi_vat_update:
                VatUpdateDialog vatDialog = new VatUpdateDialog();
                vatDialog.show(getSupportFragmentManager(), VAT_DIALOG);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the listener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save data in outState in case of screen rotation
        getSupportFragmentManager().putFragment(outState, TAX_FRAGMENT, mTaxFragment);
        outState.putBoolean(IS_ADD_VAT, mIsAddVat);
    }

    /**
     * Handle calculator's button's onClick method
     * @param view the button that was clicked
     */
    public void calculatorClick(View view){
        int viewId = view.getId();
        if(viewId == R.id.btn_calc_delete){
            mCalculator.calculateDeleteDigit();
            triggerCalculator();
        } else if(viewId == R.id.btn_calc_dot){
            mTaxFragment.addDot();
            mCalculator.addDot();
        } else {
            Button button = (Button) view;
            int digit = Integer.parseInt(button.getText().toString());
            mCalculator.updateAmount(digit);
            triggerCalculator();
        }
    }

    /**
     * Handle Radio buttons click
     * @param view the radio button that was clicked
     */
    public void vatClick(View view){
        int viewId = view.getId();
        if(viewId == R.id.rb_add_vat){
            mIsAddVat = true;
        } else {
            mIsAddVat = false;
        }
        triggerCalculator();
        mTaxFragment.updateHeadlines(mIsAddVat);
    }

    /**
     * Trigger mCalculator calculation method according to mIsAddVat
     */
    private void triggerCalculator() {
        if(mIsAddVat){
            mCalculator.calculateSumAddVat();
        } else {
            mCalculator.calculateSumReduceVat();
        }
        mTaxFragment.updateUi();
    }

    /**
     * SharedPreferences setup and registration. Update vat rate
     */
    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        mCalculator.setVat(PreferencesUtil.getVatRate(this));
        mTaxFragment.updateVat(this);
    }

}

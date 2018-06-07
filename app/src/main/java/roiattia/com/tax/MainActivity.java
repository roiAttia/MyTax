package roiattia.com.tax;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
    implements VatUpdateDialog.VatDialogListener, SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TAX_FRAGMENT = "tax_fragment";
    private static final String VAT_DIALOG = "vat_dialog";
    private static final String VERSION_DIALOG = "version_dialog";
    private TaxFragment mTaxFragment;
    private FrameLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayout = findViewById(R.id.layout_main);

        if(savedInstanceState == null) {
            mTaxFragment = new TaxFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.layout_main, mTaxFragment)
                    .commit();
        } else {
            mTaxFragment = (TaxFragment) getSupportFragmentManager().getFragment(savedInstanceState, TAX_FRAGMENT);
        }

        setupSharedPreferences();

//        if (isInstallFromUpdate() && PreferencesUtil.isFirstUpdate(this)){
//            VersionUpdateDialog versionUpdateDialog = new VersionUpdateDialog();
//            versionUpdateDialog.show(getSupportFragmentManager(), VERSION_DIALOG);
//        }
    }

    private boolean isInstallFromUpdate() {
        try {
            long firstInstallTime =   getPackageManager().getPackageInfo(getPackageName(), 0).firstInstallTime;
            long lastUpdateTime = getPackageManager().getPackageInfo(getPackageName(), 0).lastUpdateTime;
            return firstInstallTime != lastUpdateTime;
        } catch (PackageManager.NameNotFoundException e) {
            Log.i(TAG, e.getMessage());
            return false;
        }
    }

    /**
     * SharedPreferences setup and registration. Update vat rate
     */
    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        mTaxFragment.updateVat(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PreferencesUtil.KEY_VAT_RATE)) {
            mTaxFragment.updateVat(this);
        }
    }

    @Override
    public void OnDialogVatUpdateHandler(int newVat) {
        Snackbar.make(mLayout, String.format(Locale.getDefault(), "%s %d%s", getString(R.string.vat_update_message),
                        newVat, getString(R.string.precentege_sign)) , Snackbar.LENGTH_SHORT).show();
        PreferencesUtil.setVatRate(this, newVat);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the listener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * Handle calculator's button's onClick method
     */
    public void calculatorClick(View view){
        // check for special cases: delete or dot button and trigger
        // methods accordingly. numbers buttons are the default case
        switch (view.getId()) {
            case R.id.calc_delete:
                mTaxFragment.calculatorDelete();
                break;
            case R.id.calc_dot:
                mTaxFragment.calculatorDot();
                break;
            default:
                Button button = (Button) view;
                int number = Integer.parseInt(String.valueOf(button.getText()));
                mTaxFragment.calculatorAddNumber(number);
                break;
        }
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, TAX_FRAGMENT, mTaxFragment);
    }




}

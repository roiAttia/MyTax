package roiattia.com.newtaxapp;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private static final String TAX_FRAGMENT = "tax_fragment";
    private static final String JOBS_FRAGMENT = "jobs_fragment";
    private TaxFragment mTaxFragment;
    private JobsFragment mJobsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            mTaxFragment = new TaxFragment();
            mJobsFragment = new JobsFragment();
        } else {
            mTaxFragment = (TaxFragment) getSupportFragmentManager().getFragment(savedInstanceState, TAX_FRAGMENT);
            mJobsFragment = (JobsFragment) getSupportFragmentManager().getFragment(savedInstanceState, JOBS_FRAGMENT);
        }

        setupViewPagerTabs();
    }

    private void setupViewPagerTabs() {

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return position == 0 ? mTaxFragment : mJobsFragment;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabslayout);
        tabLayout.setupWithViewPager(viewPager);

        // first tab initialize
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setTextColor(getResources().getColor(R.color.colorTabSelected));
        tabOne.setText("חישוב מעמ");
        tabLayout.getTabAt(0).setCustomView(tabOne);

        // second tab initialize
        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setTextColor(getResources().getColor(R.color.colorTabUnselected));
        tabTwo.setText("רשימת עבודות");
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                TextView textView = view.findViewById(R.id.tv_tab);
                textView.setTextColor(getResources().getColor(R.color.colorTabSelected));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                TextView textView = view.findViewById(R.id.tv_tab);
                textView.setTextColor(getResources().getColor(R.color.colorTabUnselected));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    public void calculatorClick(View view){
        if(view.getId() == R.id.calc_delete){
            mTaxFragment.calculatorDelete();
        } else {
            Button button = (Button) view;
            int number = Integer.parseInt(String.valueOf(button.getText()));
            mTaxFragment.calculatorAddNumber(number);
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
                Toast.makeText(this, "Vat update", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, TAX_FRAGMENT, mTaxFragment);
        getSupportFragmentManager().putFragment(outState, JOBS_FRAGMENT, mJobsFragment);
    }
}

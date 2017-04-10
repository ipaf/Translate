package ru.pascalman.translate;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import io.realm.Realm;

import ru.pascalman.translate.databinding.MainBinding;
import ru.pascalman.translate.view.ListWithFindFragment;
import ru.pascalman.translate.view.TranslateFragment;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener
{

    private MainBinding binding;
    private ActionBar actionBar;
    private AppSectionsPagerAdapter appSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Realm.init(this);

        binding = DataBindingUtil.setContentView(this, R.layout.main);
        actionBar = getSupportActionBar();
        appSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        ViewPager vpContent = binding.vpContent;

        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        vpContent.setAdapter(appSectionsPagerAdapter);
        vpContent.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position)
            {
                actionBar.setSelectedNavigationItem(position);
            }

        });

        for (int i = 0; i < appSectionsPagerAdapter.getCount(); i++)
            actionBar.addTab(actionBar.newTab()
                    .setText(appSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {}

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
        binding.vpContent.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {}

    public void openTranslationWithResponseId(int id)
    {
        appSectionsPagerAdapter.setLookupResponseById(id);
        actionBar.setSelectedNavigationItem(0);
    }

    public void hideSoftKeyboard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter
    {

        private String[] pageTitles;
        private TranslateFragment translateFragment;

        public AppSectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);

            pageTitles = new String[] { "Translation", "Favorite", "History" };
        }

        @Override
        public Fragment getItem(int position)
        {
            Fragment fragment;

            switch (position)
            {
                case 0:
                    translateFragment = new TranslateFragment();
                    fragment = translateFragment;
                    break;
                default:
                    fragment = new ListWithFindFragment();

                    Bundle args = new Bundle();

                    args.putBoolean("isOnlyFavorite", position == 1);
                    fragment.setArguments(args);
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount()
        {
            return pageTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return pageTitles[position];
        }

        public void setLookupResponseById(int lookupResponseId)
        {
            translateFragment.setLookupResponseById(lookupResponseId);
        }

    }

}
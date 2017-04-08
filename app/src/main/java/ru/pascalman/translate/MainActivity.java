package ru.pascalman.translate;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import io.realm.Realm;

import ru.pascalman.translate.databinding.MainBinding;
import ru.pascalman.translate.view.ListWithFindFragment;
import ru.pascalman.translate.view.TranslateFragment;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener
{

    private MainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Realm.init(this);

        binding = DataBindingUtil.setContentView(this, R.layout.main);

        ViewPager vpContent = binding.vpContent;
        AppSectionsPagerAdapter mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        ActionBar actionBar = getActionBar();

        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        vpContent.setAdapter(mAppSectionsPagerAdapter);
        vpContent.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position)
            {
                actionBar.setSelectedNavigationItem(position);
            }

        });

        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++)
            actionBar.addTab(actionBar.newTab()
                    .setText(mAppSectionsPagerAdapter.getPageTitle(i))
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

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter
    {

        private String[] pageTitles;

        public AppSectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);

            pageTitles = new String[] { "Translation", "Favorite", "History" };
        }

        @Override
        public Fragment getItem(int i)
        {
            switch (i)
            {
                case 0:
                    return new TranslateFragment();
                default:
                    Fragment fragment = new ListWithFindFragment();
                    Bundle args = new Bundle();

                    args.putBoolean("isOnlyFavorite", i == 1);
                    fragment.setArguments(args);

                    return fragment;
            }
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

    }

}
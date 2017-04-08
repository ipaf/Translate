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
    private ActionBar actionBar;
    private AppSectionsPagerAdapter appSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Realm.init(this);

        binding = DataBindingUtil.setContentView(this, R.layout.main);
        actionBar = getActionBar();
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
        appSectionsPagerAdapter.setLookupResponseId(id);
        actionBar.setSelectedNavigationItem(0);
    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter
    {

        private String[] pageTitles;
        private int lookupResponseId = -1;

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
                    Fragment fragment = new TranslateFragment();
                    Bundle args = new Bundle();

                    if (lookupResponseId > -1)
                    {
                        args.putInt("lookupResponseId", lookupResponseId);

                        lookupResponseId = -1;
                    }

                    fragment.setArguments(args);

                    return fragment;
                default:
                    fragment = new ListWithFindFragment();
                    args = new Bundle();

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

        public void setLookupResponseId(int lookupResponseId)
        {
            this.lookupResponseId = lookupResponseId;
        }

    }

}
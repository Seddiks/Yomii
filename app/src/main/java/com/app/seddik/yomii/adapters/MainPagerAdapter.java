package com.app.seddik.yomii.adapters;

/**
 * Created by Seddik on 20/12/2017.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.seddik.yomii.fragments.HomeFragment;
import com.app.seddik.yomii.fragments.NotificationFragment;
import com.app.seddik.yomii.fragments.PopulairCategoriesFragment;
import com.app.seddik.yomii.fragments.ProfileFragment;
import com.app.seddik.yomii.fragments.TestingFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public MainPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomeFragment tab1 = new HomeFragment();
                return tab1;
            case 1:
                PopulairCategoriesFragment tab2 = new PopulairCategoriesFragment();
                return tab2;
            case 2:
                TestingFragment tab3 = new TestingFragment();
                return tab3;
            case 3:
                NotificationFragment tab4 = new NotificationFragment();
                return tab4;
            case 4:
                ProfileFragment tab5 = new ProfileFragment();
                return tab5;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}


package com.app.seddik.yomii.adapters;

/**
 * Created by Seddik on 20/12/2017.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.seddik.yomii.fragments.GuideFragment;
import com.app.seddik.yomii.fragments.PhotosFragment;

public class ProfilPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ProfilPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                PhotosFragment tab1 = new PhotosFragment();
                return tab1;
            case 1:
                GuideFragment tab2 = new GuideFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}


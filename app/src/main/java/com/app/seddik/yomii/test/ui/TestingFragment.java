package com.app.seddik.yomii.test.ui;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.adapters.ProfilPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestingFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_home_grey_500_24dp,
            R.drawable.ic_whatshot_grey_500_24dp,
    };
    private ProfilPagerAdapter adapter;


    public TestingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_testing, container, false);
        viewPager = rootView.findViewById(R.id.viewpager);
        tabLayout =  rootView.findViewById(R.id.tabs);

        createTabLayout();
        return rootView;

    }

/**    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Fragment childFragment = new PhotosFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, childFragment).commit();
    }**/
    private void createTabLayout(){
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[0]));
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[1]));
        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

        adapter = new ProfilPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                viewPager.setCurrentItem(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#757575"), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }




    /**
     <android.support.design.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
     xmlns:app="http://schemas.android.com/apk/res-auto"
     android:id="@+id/main_content"
     android:layout_width="match_parent"
     android:layout_height="match_parent"
     android:fillViewport="true">

     <android.support.design.widget.AppBarLayout
     android:id="@+id/appbar"
     android:layout_width="match_parent"
     android:layout_height="wrap_content">

     <RelativeLayout
     android:id="@+id/home_layout_top_2_recycler"
     android:layout_width="match_parent"
     android:layout_height="220dp"
     app:layout_scrollFlags="scroll">

     <ImageView
     android:id="@+id/img_user_home_tab_recycler"
     android:layout_width="80dp"
     android:layout_height="220dp"
     android:layout_centerVertical="true"
     android:contentDescription="Seddik"
     android:paddingLeft="5dp"
     android:paddingRight="5dp"
     android:src="@drawable/bgmoi" />

     <TextView
     android:id="@+id/tv_user_mind_home_tab_recycler"
     android:layout_width="match_parent"
     android:layout_height="wrap_content"
     android:layout_centerVertical="true"
     android:layout_marginLeft="5dp"
     android:layout_toRightOf="@+id/img_user_home_tab_recycler"
     android:hint="Hellooo"
     android:textColor="#000"
     android:textSize="18sp" />
     </RelativeLayout>

     </android.support.design.widget.AppBarLayout>

     <FrameLayout
     android:id="@+id/child_fragment_container"
     android:layout_width="match_parent"
     android:layout_height="wrap_content"
     app:layout_behavior="@string/appbar_scrolling_view_behavior">

     </FrameLayout>


     </android.support.design.widget.CoordinatorLayout>


     */

}

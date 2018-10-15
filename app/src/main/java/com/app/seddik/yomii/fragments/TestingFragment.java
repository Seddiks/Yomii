package com.app.seddik.yomii.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.ui.PhotosFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestingFragment extends Fragment {

    public TestingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_testing, container, false);

        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Fragment childFragment = new PhotosFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, childFragment).commit();
    }
    /**
     * <android.support.design.widget.CoordinatorLayout android:id="@+id/main_content"
     android:layout_width="match_parent"
     android:layout_height="match_parent"
     xmlns:android="http://schemas.android.com/apk/res/android"
     xmlns:app="http://schemas.android.com/apk/res-auto">

     <android.support.design.widget.AppBarLayout
     android:id="@+id/appbar"
     android:layout_width="match_parent"
     android:layout_height="wrap_content">

     <RelativeLayout
     android:id="@+id/home_layout_top_2_recycler"
     android:layout_width="match_parent"
     android:layout_height="120dp"
     app:layout_scrollFlags="scroll">

     <ImageView
     android:id="@+id/img_user_home_tab_recycler"
     android:layout_width="80dp"
     android:layout_height="120dp"
     android:paddingLeft="5dp"
     android:paddingRight="5dp"
     android:layout_centerVertical="true"
     android:contentDescription="Seddik"
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
     android:layout_height="match_parent">

     </FrameLayout>


     </android.support.design.widget.CoordinatorLayout>

     */

}

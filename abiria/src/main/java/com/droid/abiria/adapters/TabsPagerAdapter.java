package com.droid.abiria.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.droid.abiria.ui.MainActivity;
import com.droid.abiria.ui.frags.NearbyStagesFrag;
import com.droid.abiria.ui.frags.ViewMapFrag;


/**
 * Created by james on 01/02/14.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index){
            case 0: //return nearby_stages frag
                return new NearbyStagesFrag();
            case 1: //return view_map_frag
                return new ViewMapFrag();
        }
        return null;
    }

    //ret. no of tabs
    @Override
    public int getCount() {
        return MainActivity.tabs.length;
    }
}

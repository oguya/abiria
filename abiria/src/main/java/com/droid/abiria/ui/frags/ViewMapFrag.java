package com.droid.abiria.ui.frags;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droid.abiria.R;

/**
 * Created by james on 01/02/14.
 */
public class ViewMapFrag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstance){
        View rootView = inflater.inflate(R.layout.view_map, container, false);
        return rootView;
    }

}

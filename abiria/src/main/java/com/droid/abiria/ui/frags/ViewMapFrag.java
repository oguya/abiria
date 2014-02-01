package com.droid.abiria.ui.frags;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droid.abiria.R;
import com.droid.abiria.db.DBAdapter;
import com.droid.abiria.db.Stop;
import com.droid.abiria.location.LocationUtils;

import java.util.List;

/**
 * Created by james on 01/02/14.
 *
 * Load nearby stages on a map
 */
public class ViewMapFrag extends Fragment {
    public List<Stop> stopList;
    private LocationUtils locationUtils;
    private DBAdapter dbAdapter;
    private Location currentLoc;


    //frag attached to parent activity
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    //load UI stuff
    @Override
    public void onStart(){
        super.onStart();

        //load helpers
        locationUtils = new LocationUtils(getActivity());
        dbAdapter = new DBAdapter(getActivity());
        dbAdapter.open();
        currentLoc = locationUtils.lastKnownLoc();

        //get stages
        stopList = dbAdapter.getStages(currentLoc.getLatitude(), currentLoc.getLongitude());

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstance){
        View rootView = inflater.inflate(R.layout.view_map, container, false);
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        dbAdapter.open();
    }

    @Override
    public void onPause(){
        super.onPause();
        dbAdapter.close();
    }

    @Override
    public void onStop(){
        super.onStop();
        dbAdapter.close();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

}

package com.droid.abiria.ui.frags;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.droid.abiria.R;
import com.droid.abiria.adapters.CustomRouteListAdapter;
import com.droid.abiria.db.DBAdapter;
import com.droid.abiria.db.Route;

import java.util.List;

/**
 * Created by james on 01/02/14.
 */
public class NearbyStagesFrag extends Fragment {
    private final String APP_TAG = "MainActivity";
    private final String PREF_DB_COPIED = "DBCopied";
    private final String DB_NAME = "gtfs.sqlite";
    private final int DELETE_ID = Menu.FIRST+1;

    private DBAdapter dbAdapter;
    private ListView listView;
    private List<Route> routeList;
    private Activity parentActivity;
    private ActionBar actionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.nearby_stages, container, false);
        return rootView;
    }


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        parentActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    //Apply any required UI change now that the Fragment is visible.
    @Override
    public void onStart(){
        super.onStart();

        //load db ops
        dbAdapter = new DBAdapter(getActivity());
        dbAdapter.open();

        //get all routes
        routeList = dbAdapter.getAllRoutes();
        listView = (ListView)getActivity().findViewById(android.R.id.list);
        listView.setAdapter(new CustomRouteListAdapter(getActivity(), routeList));
        listView.setOnItemClickListener(listViewClickListener);
    }

    public AdapterView.OnItemClickListener listViewClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Route route = routeList.get(position);
            String details = "";
            details += "trip_ID: "+route.get_trip_id();
            details += " trip headsign "+route.get_trip_headsign();
            details += " route long name: "+route.get_route_long_name();
            Toast.makeText(getActivity(), details, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        dbAdapter.close();
    }

    @Override
    public void onDetach(){
        super.onDetach();
    }
}


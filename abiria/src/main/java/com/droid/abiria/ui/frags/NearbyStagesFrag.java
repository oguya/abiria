package com.droid.abiria.ui.frags;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.droid.abiria.R;
import com.droid.abiria.adapters.CustomRouteListAdapter;
import com.droid.abiria.adapters.CustomStageListAdapter;
import com.droid.abiria.db.DBAdapter;
import com.droid.abiria.db.Route;
import com.droid.abiria.db.Stop;
import com.droid.abiria.location.LocationUtils;
import com.droid.abiria.ui.Fonts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.support.v4.view.MenuItemCompat.setActionView;

/**
 * Created by james on 01/02/14.
 */
public class NearbyStagesFrag extends Fragment {
    private final String APP_TAG = "NearbyStages_Frag";

    private DBAdapter dbAdapter;
    private ListView listView;
    private List<Stop> stopList;

    private Activity parentActivity;

    private LocationUtils locationUtils;
    private Location location;
    private MenuItem loadingMenuItem;

    private final String KEY_STOP_LIST = "stop_list";
    private int CURRENT_API_VERSION;

    public NearbyStagesFrag(){
        CURRENT_API_VERSION = Build.VERSION.SDK_INT;
    }

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
        setRetainInstance(true);
        setHasOptionsMenu(true);

        if(savedInstanceState != null){
            Log.d(APP_TAG, "restoring saved instance");
            stopList = savedInstanceState.getParcelableArrayList(KEY_STOP_LIST);
            Log.d(APP_TAG, "saved time: "+savedInstanceState.getString("NOW"));
        }else
            Log.d(APP_TAG, "no saved instance to restore");
    }

    //Apply any required UI change now that the Fragment is visible.
    @Override
    public void onStart(){
        super.onStart();

        //set fonts
//        TextView textView = (TextView)getActivity().findViewById(R.id.header);
//        Fonts fonts = new Fonts(getActivity());
//        fonts.setTypeface(textView);

        //load helpers
        locationUtils = new LocationUtils(getActivity());

        //load db ops
        dbAdapter = new DBAdapter(getActivity());
        dbAdapter.open();

        location = locationUtils.lastKnownLoc();
        String locdets = "Lat: "+String.valueOf(location.getLatitude());
        locdets += " Lng: "+String.valueOf(location.getLongitude());
        locdets += " Accuracy: "+String.valueOf(location.getAccuracy())+" meters";
        Log.e(APP_TAG, locdets);

        //get nearby stops..load from saved state
        stopList = (stopList == null) ? dbAdapter.getStages(location.getLatitude(), location.getLongitude()) : stopList;
        listView = (ListView)getActivity().findViewById(android.R.id.list);
        listView.setAdapter(new CustomStageListAdapter(getActivity(), stopList));
        listView.setOnItemClickListener(listViewClickListener); 
    }

    public AdapterView.OnItemClickListener listViewClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Stop stop = stopList.get(position);
            String details = "";
            details += "stop ID: "+stop.get_stop_id();
            details += " stop name: "+stop.get_stop_name();
            details += " stop lat: "+stop.get_stop_lat();
            details += " stop lng: "+stop.get_stop_lon();
            Toast.makeText(getActivity(), details, Toast.LENGTH_SHORT).show();
        }
    };

    public void refreshLocation(){
        int timediff = timeDiff(location.getTime());
        new LoadKiasi().execute();

        if(timediff > 10){
            location = locationUtils.lastKnownLoc();
            stopList = (stopList == null) ? dbAdapter.getStages(location.getLatitude(), location.getLongitude()) : stopList;
            listView = (ListView)getActivity().findViewById(android.R.id.list);
            listView.setAdapter(new CustomStageListAdapter(getActivity(), stopList));
            listView.setOnItemClickListener(listViewClickListener);
        }else{
            location = locationUtils.lastKnownLoc();
            timediff = timeDiff(location.getTime());
            ArrayAdapter<Stop> arrayAdapter = (ArrayAdapter<Stop>)listView.getAdapter();
            arrayAdapter.notifyDataSetChanged();
        }
    }

    public int timeDiff(Long sec){
        Date past = new Date(sec);
        Date now = new Date(System.currentTimeMillis());

        long diff = now.getTime() - past.getTime();
        long diffMins = diff / (60 * 1000) % 60;
        Log.d(APP_TAG, "Time diff: "+diffMins);
        Log.d(APP_TAG, "Time now: "+now.getTime());
        Log.d(APP_TAG, "Time past: "+past.getTime());
        return (int)diffMins;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){

            //refresh
            case R.id.action_refresh:
                Toast.makeText(getActivity(), "Refreshing...", Toast.LENGTH_LONG).show();
                loadingMenuItem = item;

                if(CURRENT_API_VERSION >= Build.VERSION_CODES.HONEYCOMB){
                    showActionView_HC(item, false);
                    refreshLocation();
                }else{
                    showActionView_GB(item, false);
                    refreshLocation();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showActionView_HC(MenuItem item, boolean hide){
        if(hide){
            item.setActionView(null);
        }else{
            item.setActionView(R.layout.loading_widget);
        }
    }

    public void showActionView_GB(MenuItem item, boolean hide){
        if(hide){
            MenuItemCompat.collapseActionView(item);
            MenuItemCompat.setActionView(item, null);
        }else{
            MenuItemCompat.setActionView(item, R.layout.loading_widget);
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        dbAdapter.open();
    }

    @Override
    public void onPause(){
        dbAdapter.close();
        super.onPause();
    }

    //save states
    @Override
    public void onSaveInstanceState(Bundle outState){
        Log.d(APP_TAG, "saving stop lists");
        outState.putParcelableArrayList(KEY_STOP_LIST, (ArrayList<? extends Parcelable>) stopList);
        String now = new Date().toLocaleString();
        outState.putString("NOW", now);
        Log.d(APP_TAG, "saving time: "+now);
        super.onSaveInstanceState(outState);
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

    private class LoadKiasi extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(String results){
            super.onPostExecute(results);
            if(CURRENT_API_VERSION >= Build.VERSION_CODES.HONEYCOMB){
                showActionView_HC(loadingMenuItem, true);
            }else{
                showActionView_GB(loadingMenuItem, true);
            }
        }
    }
}


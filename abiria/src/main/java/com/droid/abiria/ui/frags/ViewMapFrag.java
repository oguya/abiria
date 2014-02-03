package com.droid.abiria.ui.frags;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.droid.abiria.R;
import com.droid.abiria.db.DBAdapter;
import com.droid.abiria.db.Stop;
import com.droid.abiria.location.LocationUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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

    private GoogleMap gMap;
    private UiSettings uiSettings;

    private final String APP_LOG_TAG = "ViewMapFrag";

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

        //map
        gMap = ((SupportMapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        gMap.setMyLocationEnabled(true);
        uiSettings = gMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setAllGesturesEnabled(true);
        getCoord(stopList);
//        CameraPosition currentPos = new CameraPosition.Builder().target(
//                new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude())).zoom(12).build();
//        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(currentPos));
        boundMap(stopList);
    }

    private void boundMap(List<Stop> stops){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for(Stop stop : stops){
            double lat = Double.valueOf(stop.get_stop_lat());
            double lng = Double.valueOf(stop.get_stop_lon());
            builder.include(new LatLng(lat, lng));
        }
        LatLngBounds bounds = builder.build();
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 15));
    }

    private void getCoord(List<Stop> stops){
        for(Stop stop : stops){
            double distance = Math.ceil(stop.get_distance());
            double lat = Double.valueOf(stop.get_stop_lat());
            double lng = Double.valueOf(stop.get_stop_lon());
            String stage_name = stop.get_stop_name();
            String stop_id = stop.get_stop_id();
            int far = 0;
            String snippet = "Approximately "+(int)Math.ceil(distance)+" meters away";

            if(distance <= 200){
                far = 0;
            }else if(distance >= 201 && distance <= 500){
                far = 1;
            }else if(distance >= 501){
                far = 2;
            }

            createMarker(gMap, lat, lng, stage_name, snippet, far);
        }
    }

    private void createMarker(GoogleMap gMap, double lat, double lng, String name, String snippet, int far){
        //far 0 - 0 -> 500meters 1 - >500m
        MarkerOptions options;
        switch (far){
            case 0: //0-200 meters
                options = new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .title(name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .snippet(snippet);
                break;
            case 1: //201 - 500meters
                options = new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .title(name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                        .snippet(snippet);
                break;
            case 2: // >500meters
                options = new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .title(name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .snippet(snippet);
                break;
            default:
                options = new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .title(name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .snippet(snippet);
                break;
        }
        gMap.addMarker(options);
    }

    private void initMap(){

        gMap = (gMap == null) ?
                ((SupportMapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap() : gMap;

        if (gMap == null)
            Toast.makeText(getActivity(),"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();

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
        initMap();
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

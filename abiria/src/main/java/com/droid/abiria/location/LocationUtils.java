package com.droid.abiria.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by james on 01/02/14.
 */
public class LocationUtils  {
    private Context context;


    public LocationUtils(Context _context){
        this.context = _context;
    }

    public Location lastKnownLoc(){
        Location bestLoction = null;
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        for(String provider : locationManager.getProviders(true)){
            Location location = locationManager.getLastKnownLocation(provider);

            //get loc with the highest accuracy
            if(bestLoction == null){
                bestLoction = location;
            }else {
                bestLoction = (location.getAccuracy() < bestLoction.getAccuracy()) ? location : bestLoction;
            }
        }
        return bestLoction;
    }

    //get distance btwn current loc & a given loc
    public float getDistance(Location currentLoc, Location givenLoc){
        Double latitude = currentLoc.getLatitude();
        Double longitude = currentLoc.getLongitude();

        float[] results = new float[1];
        Location.distanceBetween(latitude, longitude, givenLoc.getLatitude(), givenLoc.getLongitude(), results);
        return results[0];
    }

    public static float getDistance(Double currentLat, Double currentLng, Double givenLat, Double givenLng){

        float[] results = new float[1];
        Location.distanceBetween(currentLat, currentLng, givenLat, givenLng, results);
        return results[0];
    }

}

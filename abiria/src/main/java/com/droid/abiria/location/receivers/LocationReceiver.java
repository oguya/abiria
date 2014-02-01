package com.droid.abiria.location.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by james on 01/02/14.
 */
public class LocationReceiver extends BroadcastReceiver {
    private final String LOG_TAG = getClass().getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String locationKey = LocationManager.KEY_LOCATION_CHANGED;
        String providerEnabledKey = LocationManager.KEY_PROVIDER_ENABLED;

        //loc provider status
        if(intent.hasExtra(providerEnabledKey)){
            if(!intent.getBooleanExtra(providerEnabledKey, true)){
                Toast.makeText(context, "Location provider disabled", Toast.LENGTH_SHORT).show();
                Log.v(LOG_TAG, "location provider disabled");

            }else{
                Toast.makeText(context,"Location provider enabled", Toast.LENGTH_SHORT).show();
                Log.v(LOG_TAG, "location provider enabled");
            }
        }

        //new loc available??
        if(intent.hasExtra(locationKey)){
            Location loc = (Location)intent.getExtras().get(locationKey);
            String locStr = "Location Changed: lat: "+loc.getLatitude()+" Lng: "+loc.getLongitude()+" Accuracy: "+loc.getAccuracy();
//            vibrate_notification(context);
            Toast.makeText(context,locStr,Toast.LENGTH_SHORT).show();
            Log.v(LOG_TAG, locStr);

            //consume location

        }
    }

}

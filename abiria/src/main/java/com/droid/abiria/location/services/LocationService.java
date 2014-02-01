package com.droid.abiria.location.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.droid.abiria.location.receivers.LocationReceiver;

/**
 * Created by james on 01/02/14.
 */
public class LocationService extends Service {
    LocationManager lm;
    PendingIntent pendingIntent;
    private final String LOG_TAG = getClass().getName();
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.v(LOG_TAG, "location service started");
        return START_STICKY;
    }

    @Override
    public void onCreate(){
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
        Log.v(LOG_TAG, "starting location service...");

        //use location manager to obtain locations data
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Intent i = new Intent(this, LocationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        //request for location updates using GPS
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 100, pendingIntent);
    }

    @Override
    public void onDestroy(){
        //remove pending intent
        lm.removeUpdates(pendingIntent);

        super.onDestroy();
        Log.v(LOG_TAG, "Location service stopped");
        Toast.makeText(this, "location service stopped", Toast.LENGTH_LONG).show();
    }
}

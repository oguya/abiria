package com.droid.abiria.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.net.URI;

/**
 * Created by james on 04/02/14.
 */
public class DownloadActivity extends Activity{

    private static final String DL_ID = "downloadID";
    private final String URL = "http://www.bigfoto.com/dog-animal.jpg";
    private final String EXTERNAL_DIR = "Abiria";
    private final String FILENAME =  "sample_pic.jpg";
    private SharedPreferences prefs;
    private DownloadManager dm;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //load view...setContentView()

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        dm = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
    }

    @Override
    public void onResume(){
        super.onResume();
        startDownload(EXTERNAL_DIR, FILENAME);
    }

    @Override
    public void onPause(){
        super.onPause();
        unregisterReceiver(dlReceiver);
    }

    @SuppressLint("NewApi")
    public void startDownload(String folderName, String fileName){
        if(!prefs.contains(DL_ID)){
            Uri uri = Uri.parse(URL);
            Request request = new Request(uri);
            request.setAllowedNetworkTypes(Request.NETWORK_MOBILE | Request.NETWORK_WIFI);
            request.setAllowedOverRoaming(false);

            //display in the notification bar
            request.setTitle("Downloading a sample file");
            request.setDescription("Downloading a big sample file please wait");
            request.setNotificationVisibility(Request.VISIBILITY_VISIBLE);
            request.setDestinationInExternalFilesDir(this, folderName, fileName);
            long id = dm.enqueue(request);

            //save the unique id
            prefs.edit().putLong(DL_ID, id).commit();
        }else{
            //download already started..check status
            queryDownloadStatus();
        }

        //register receiver
        registerReceiver(dlReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private BroadcastReceiver dlReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            queryDownloadStatus();
        }
    };

    @SuppressLint("NewApi")
    public void queryDownloadStatus(){
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(prefs.getLong(DL_ID, 0));
        Cursor c = dm.query(query);

        if(c.moveToFirst()){
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status){
                case DownloadManager.STATUS_PAUSED:
                    Toast.makeText(this, "Download paused", Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_PENDING:
                    Toast.makeText(this, "Download pending", Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_RUNNING:
                    Toast.makeText(this, "Downloading...", Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    //done downloading
                    Toast.makeText(this, "Download complete", Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(this, "Download failed! Retrying", Toast.LENGTH_LONG).show();

                    //clear the download..retry
                    dm.remove(prefs.getLong(DL_ID, 0));
                    prefs.edit().clear().commit();
                    break;
            }
        }
    }
}

package com.droid.abiria.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;

import com.droid.abiria.R;
import com.droid.abiria.db.DBAdapter;
import com.droid.abiria.utils.DBUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends ActionBarActivity {

    private final String APP_TAG = "MainActivity";
    private final String PREFKEY_DB_COPIED = "DBCopied";

    private SharedPreferences prefs;
    private String EXTERNAL_DB_DIR = "";
    private String DB_NAME = "gtfs.sqlite";
    private LoadStuff loadStuff;
    private DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //load other stuff
        loadStuff = new LoadStuff(this);
        prefs = getPreferences(MODE_PRIVATE);

        //copy db
        try{
            EXTERNAL_DB_DIR = getApplicationInfo().dataDir+File.pathSeparator;
        }catch (NullPointerException npe){
            EXTERNAL_DB_DIR = "/data/data/"+getPackageName()+"/databases/";
        }
        String DBPath = getDatabasePath(DB_NAME).getAbsolutePath();
        if(!loadStuff.DBExists(DBPath)){
            loadStuff.loadDB(getResources().openRawResource(R.raw.gtfs_testing), DBPath, EXTERNAL_DB_DIR);
        }

        //load db ops
        dbAdapter = new DBAdapter(this);
        dbAdapter.open();

        TextView textView = (TextView)findViewById(R.id.tester);
        Cursor cursor = dbAdapter.getAllRoutes();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){

        }
    }

    @Override
    public void onPause(){
        super.onPause();
        dbAdapter.close();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    //restore saved stuff
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class LoadStuff implements Parcelable {
        private Context context;

        public LoadStuff(Context context){
            this.context = context;
        }

        //parcelling part
        public LoadStuff(Parcel parcel){
        }

        @Override
        public int describeContents(){
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags){

        }

        public boolean DBExists(String DBPath){
            File db = new File(DBPath);
            return (db.exists() && db.isFile());
        }

        public void loadDB(InputStream inputStream, String DBPath, String DBDir){
            DBUtils dbUtils = new DBUtils();
            try{
                dbUtils.copyDB(inputStream, DBDir, DBPath);
            }catch (IOException e){
                Log.e(APP_TAG, "Error while copying DB!\n "+e.getMessage());
            }

        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}

package com.droid.abiria.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.droid.abiria.R;
import com.droid.abiria.db.DBAdapter;
import com.droid.abiria.db.Route;
import com.droid.abiria.utils.DBUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    private final String APP_TAG = "MainActivity";
    private final String PREF_DB_COPIED = "DBCopied";
    private final String DB_NAME = "gtfs.sqlite";
    private final int DELETE_ID = Menu.FIRST+1;

    private final String KEY_LOADER_STATE = "LoadStuff";

    private SharedPreferences prefs;
    private String EXTERNAL_DB_DIR = "";

    private LoadStuff loadStuff;
    private DBAdapter dbAdapter;
    private ListView listView;
    private List<Route> routeList;

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
            loadStuff.loadDB(DBPath);
            prefs.edit().putBoolean(PREF_DB_COPIED, true).commit();
            Log.i(APP_TAG,"copying db to "+DBPath);
        }else{
            Log.i(APP_TAG,"db already copied to:"+DBPath);
        }

        //load db ops
        dbAdapter = new DBAdapter(this);
        dbAdapter.open();

        //get all routes
        routeList = dbAdapter.getAllRoutes();
        ArrayAdapter<Route> adapter = new ArrayAdapter<Route>(this, android.R.layout.simple_list_item_1, routeList);
        listView = (ListView)findViewById(android.R.id.list);
        listView.setAdapter(new CustomRouteListAdapter(MainActivity.this, routeList));
        listView.setOnItemClickListener(listViewClickListener);
        registerForContextMenu(listView);
    }

    public OnItemClickListener listViewClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Route route = routeList.get(position);
            String details = "";
            details += "trip_ID: "+route.get_trip_id();
            details += " trip headsign "+route.get_trip_headsign();
            details += " route long name: "+route.get_route_long_name();
            Toast.makeText(getApplicationContext(), details, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
                Route route = routeList.get(info.position);
                String details = "Abiria";
                details += "trip_ID: "+route.get_trip_id();
                details += " trip headsign "+route.get_trip_headsign();
                details += " route long name: "+route.get_route_long_name();
                Toast.makeText(getApplicationContext(), details, Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    public void onPause(){
        super.onPause();
        dbAdapter.close();
    }

    @Override
    public void onResume(){
        super.onResume();
        dbAdapter.open();
    }

    //restore saved stuff
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        Log.i(APP_TAG, "restoring instance state");
        super.onRestoreInstanceState(savedInstanceState);
        loadStuff = (LoadStuff)savedInstanceState.getSerializable(KEY_LOADER_STATE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        Log.i(APP_TAG, "saving instance state");
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_LOADER_STATE, loadStuff);
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

    public class LoadStuff implements Serializable {
        private Context context;

        public LoadStuff(Context context){
            this.context = context;
        }

        public boolean DBExists(String DBPath){
            File db = new File(DBPath);
            return (db.exists() && db.isFile());
        }

        public void loadDB(String DBPath){
            DBUtils dbUtils = new DBUtils();
            try{
                dbUtils.copyDB(getBaseContext().getResources().openRawResource(R.raw.gtfs_testing),
                        new FileOutputStream(DBPath));
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

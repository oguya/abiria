package com.droid.abiria.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.droid.abiria.location.LocationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 23/01/14.
 */
public class DBAdapter {
    private static final String DATABASE_NAME = "gtfs.sqlite";
    private static final int DATABASE_VERSION = 1;
    private final String LOG_TAG = getClass().getName();

    private Context context;
    DBHelper dbHelper;
    SQLiteDatabase db;

    public DBAdapter(Context _context){
        this.context = _context;
        dbHelper = new DBHelper(context);
    }

    //open db
    public DBAdapter open(){
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        db.close();
    }

    //get all routes & route details
    public List<Route> getAllRoutes(){
        List<Route> routesList = new ArrayList<Route>();

        Cursor cursor = db.rawQuery(Queries.GET_ALL_ROUTES, null);
        Log.e(LOG_TAG, Queries.GET_ALL_ROUTES );
        if(cursor.moveToFirst()){
            do{
                Route route = new Route();

                route.set_route_id(cursor.getString(cursor.getColumnIndex(Route.KEY_ROUTE_ID)));
                route.set_trip_id(cursor.getString(cursor.getColumnIndex(Trip.KEY_TRIP_ID)));
                route.set_trip_headsign(cursor.getString(cursor.getColumnIndex(Trip.KEY_TRIP_HEADSIGN)));
                route.set_route_short_name(cursor.getString(cursor.getColumnIndex(Route.KEY_ROUTE_SHORT_NAME)));
                route.set_route_long_name(cursor.getString(cursor.getColumnIndex(Route.KEY_ROUTE_LONG_NAME)));
                route.set_route_desc(cursor.getString(cursor.getColumnIndex(Route.KEY_ROUTE_DESC)));

                routesList.add(route);
            }while(cursor.moveToNext());
            cursor.close();
        }
        return routesList;
    }

    public List<Stop> getStages(Double lat, Double lng){

        List<Stop> stages  = new ArrayList<Stop>();
        String sql = Queries.GET_NEARBY_STAGES.replace("current_lat",String.valueOf(lat));
        sql = sql.replace("current_lng",String.valueOf(lng));
        Log.e(LOG_TAG, sql);

        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do {
                Stop stop = new Stop();

                stop.set_stop_id(cursor.getString(cursor.getColumnIndex(Stop.KEY_STOP_ID)));
                stop.set_stop_name(cursor.getString(cursor.getColumnIndex(Stop.KEY_STOP_NAME)));

                stop.set_stop_lat(cursor.getString(cursor.getColumnIndex(Stop.KEY_STOP_LAT)));
                stop.set_stop_lon(cursor.getString(cursor.getColumnIndex(Stop.KEY_STOP_LON)));
                stop.set_distance(
                  LocationUtils.getDistance(lat, lng,
                          cursor.getDouble(cursor.getColumnIndex(Stop.KEY_STOP_LAT)),
                          cursor.getDouble(cursor.getColumnIndex(Stop.KEY_STOP_LON)))
                );

                stages.add(stop);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return stages;
    }

    public static class DBHelper extends SQLiteOpenHelper{

        public DBHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    private static class Queries{
        public static  final String GET_ALL_ROUTES =
                "SELECT trips.route_id AS "+Route.KEY_ROUTE_ID+", "+
                        "trips.trip_id AS "+Trip.KEY_TRIP_ID+", "+
                        "trips.trip_headsign AS "+Trip.KEY_TRIP_HEADSIGN+", "+
                        "routes.route_short_name AS "+Route.KEY_ROUTE_SHORT_NAME+", "+
                        "routes.route_long_name AS "+Route.KEY_ROUTE_LONG_NAME+", "+
                        "routes.route_desc AS "+Route.KEY_ROUTE_DESC+" "+
                "FROM trips INNER JOIN routes ON trips.route_id = routes.route_id " +
                "GROUP BY routes.route_short_name ORDER BY routes.route_short_name ASC";

        public static final String GET_NEARBY_STAGES =
                "SELECT stop_id AS "+Stop.KEY_STOP_ID+", "+
                        "stop_name AS "+Stop.KEY_STOP_NAME+", "+
                        "stop_lat AS "+Stop.KEY_STOP_LAT+", "+
                        "stop_lon AS "+Stop.KEY_STOP_LON+" "+
                "FROM stops"+" "+
                "WHERE round(stop_lat, 2) LIKE round(current_lat, 2)" +
                    "AND round(stop_lon, 2) LIKE round(current_lng, 2)";

    }
}

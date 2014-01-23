package com.droid.abiria.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by james on 23/01/14.
 */
public class DBAdapter {
    private static final String DATABASE_NAME = "";
    private static final int DATABASE_VERSION = 1;

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

    public Cursor getAllRoutes(){
        return db.rawQuery(Queries.GET_ALL_ROUTES, null);
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
        public static  final String GET_ALL_ROUTES = "SELECT trips.route_id, trips.trip_id, trips.trip_headsign, " +
                "routes.route_short_name, routes.route_long_name, routes.route_desc from trips " +
                "inner join routes where trips.route_id = routes.route_id";

    }
}

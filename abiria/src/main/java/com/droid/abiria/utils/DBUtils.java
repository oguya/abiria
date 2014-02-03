package com.droid.abiria.utils;

import android.content.Context;
import android.util.Log;

import com.droid.abiria.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by james on 23/01/14.
 *
 * perform db utils ops like copying db
 */
public class DBUtils {

    private final String APP_TAG = "DBUtils";
    private Context context;

    public DBUtils(Context context){
        this.context = context;
    }

    public DBUtils(){}

    public void copyDB(InputStream inputStream, OutputStream outputStream) throws IOException{
        //copy 1kb at a time
        byte[] buffer = new byte[1024];
        int length;

        while( (length = inputStream.read(buffer)) > 0 ){
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }

    public void copyDB(String db_name, String db_dir) throws IOException{
        this.prepDBFolder(db_dir);
        String db_path = db_dir+db_name;
        OutputStream dbOutputStream = new FileOutputStream(db_path);
        InputStream dbInputStream;
        byte[] buffer = new byte[1024];
        int length;

        //copy part A
        dbInputStream = this.context.getResources().openRawResource(R.raw.gtfs_testing_a);
        while ( (length = dbInputStream.read(buffer)) > 0){
            dbOutputStream.write(buffer, 0, length);
        }
        dbInputStream.close();

        //copy part B
        dbInputStream = this.context.getResources().openRawResource(R.raw.gtfs_testing_b);
        while ( (length = dbInputStream.read(buffer)) > 0){
            dbOutputStream.write(buffer, 0, length);
        }
        dbOutputStream.close();
        dbOutputStream.flush();
        dbOutputStream.close();
    }

    //prep db folders
    public boolean prepDBFolder(String DBDir){
        boolean status = false;
        File db = new File(DBDir);

        if(!db.exists() && !db.isDirectory()){
            db.mkdirs();
            status = true;
            Log.i(APP_TAG, "created DB Dir: "+DBDir);
        }else{
            status = true;
            Log.i(APP_TAG, "DB Dir: "+DBDir+" exist!");
        }
        return status;
    }
}

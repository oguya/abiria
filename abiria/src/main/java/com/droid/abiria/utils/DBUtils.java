package com.droid.abiria.utils;

import android.util.Log;

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

    public void copyDB(InputStream inputStream, String DBDir, String DBPath) throws IOException {
        File db_file = new File(DBPath);

        if(prepDBFolder(DBDir) && db_file.isFile()){
            byte[] buffer = new byte[1024];
            int length;
            OutputStream outputStream = new FileOutputStream(DBPath);
            while ((length = inputStream.read(buffer)) > 0){
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
            Log.i(APP_TAG, "Successfully copied DB to "+DBDir+" Path: "+DBPath);
        }else{
            Log.i(APP_TAG, "DB File: "+DBPath+" already exits!");
        }
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
            status = false;
            Log.i(APP_TAG, "DB Dir: "+DBDir+" exist!");
        }
        return status;
    }
}

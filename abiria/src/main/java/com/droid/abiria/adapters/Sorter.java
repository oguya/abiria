package com.droid.abiria.adapters;

import com.droid.abiria.db.Stop;

import java.util.Comparator;

/**
 * Created by james on 03/02/14.
 *
 * sort nearby stages using distance
 */
public class Sorter implements Comparator<Stop> {

    public int compare(Stop stop1, Stop stop2){
        float distance1 = stop1.get_distance();
        float distance2 = stop2.get_distance();

        if(distance1 > distance2){
            return +1;
        }else if(distance1 < distance2){
            return -1;
        }else{
            return 0;
        }
    }

}

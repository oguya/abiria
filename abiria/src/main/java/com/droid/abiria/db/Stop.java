package com.droid.abiria.db;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by james on 01/02/14.
 */
public class Stop implements Parcelable{
    public static final String KEY_STOP_ID = "stop_id";
    public static final String KEY_STOP_CODE = "stop_code";
    public static final String KEY_STOP_NAME = "stop_name";
    public static final String KEY_STOP_DESC = "stop_desc";
    public static final String KEY_STOP_LAT = "stop_lat";
    public static final String KEY_STOP_LON = "stop_lon";
    public static final String KEY_ZONE_ID = "zone_id";
    public static final String KEY_STOP_URL = "stop_url";
    public static final String KEY_LOCATION_TYPE = "location_type";
    public static final String KEY_PARENT_STATION = "parent_station";

    public  String stop_id;
    public  String stop_code;
    public  String stop_name;
    public  String stop_desc;
    public  String stop_lat;
    public  String stop_lon;
    public  String zone_id;
    public  String stop_url;
    public  String location_type;
    public  String parent_station;
    public Float distance;

    public Stop(){}

    public Stop(String _stop_id, String _stop_code, String _stop_name,
                String _stop_desc, String _stop_lat, String _stop_lon,
                String _zone_id, String _stop_url, String _location_type,
                String _parent_station){

        this.stop_id = _stop_id;
        this.stop_name = _stop_name;
        this.stop_code = _stop_code;
        this.stop_desc = _stop_desc;
        this.stop_lat = _stop_lat;
        this.stop_lon = _stop_lon;
        this.zone_id = _zone_id;
        this.stop_url = _stop_url;
        this.location_type = _location_type;
        this.parent_station = _parent_station;
    }

    public Stop(Parcel parcel){
        this.stop_id = parcel.readString();
        this.stop_name = parcel.readString();
        this.stop_code = parcel.readString();
        this.stop_desc = parcel.readString();
        this.stop_lat = parcel.readString();
        this.stop_lon = parcel.readString();
        this.zone_id = parcel.readString();
        this.stop_url = parcel.readString();
        this.location_type = parcel.readString();
        this.parent_station = parcel.readString();
        this.distance = parcel.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(this.stop_id);
        dest.writeString(this.stop_name);
        dest.writeString(this.stop_code);
        dest.writeString(this.stop_desc);
        dest.writeString(this.stop_lat);
        dest.writeString(this.stop_lon);
        dest.writeString(this.zone_id);
        dest.writeString(this.stop_url);
        dest.writeString(this.location_type);
        dest.writeString(this.parent_station);
        dest.writeFloat(this.distance);
    }

    public static final Creator<Stop> CREATOR = new Creator<Stop>() {
        @Override
        public Stop createFromParcel(Parcel source) {
            return new Stop(source);
        }

        @Override
        public Stop[] newArray(int size) {
            return new Stop[size];
        }
    };

    public String get_stop_id(){
        return this.stop_id;
    }

    public void set_stop_id(String stop_id){
        this.stop_id = stop_id;
    }

    public void set_stop_code(String stop_code){
        this.stop_code = stop_code;
    }

    public String get_stop_code(){
        return this.stop_code;
    }

    public String get_stop_name(){
        return this.stop_name;
    }
    public void set_stop_name(String stop_name){
        this.stop_name = stop_name;
    }

    public String get_stop_desc(){
        return this.stop_desc;
    }

    public void set_stop_desc(String stop_desc){
        this.stop_desc = stop_desc;
    }

    public String get_stop_lat(){
        return this.stop_lat;
    }

    public void set_stop_lat(String stop_lat){
        this.stop_lat = stop_lat;
    }

    public String get_stop_lon(){
        return this.stop_lon;
    }
    public void set_stop_lon(String stop_lon){
        this.stop_lon = stop_lon;
    }

    public String get_zone_id(){
        return this.zone_id;
    }
    public void set_zone_id(String zone_id){
        this.zone_id = zone_id;
    }

    public void set_stop_url(String stop_url){
        this.stop_url =stop_url;
    }
    public String get_stop_url(){
        return this.stop_url;
    }

    public String get_location_type(){
        return this.location_type;
    }

    public void set_location_type(String location_type){
        this.location_type = location_type;
    }

    public String get_parent_station(){
        return this.parent_station;
    }

    public void set_parent_station(String parent_station){
        this.parent_station = parent_station;
    }

    public Float get_distance(){
        return this.distance;
    }

    public void set_distance(Float distance){
        this.distance = distance;
    }
}
package com.droid.abiria.db;

/**
 * Created by james on 23/01/14.
 *
 * Routes info
 */

public class Routes {
    public static  final String KEY_AGENCY_ID = "agency_id";
    public static  final String KEY_ROUTE_SHORT_NAME = "route_short_name";
    public static  final String KEY_ROUTE_LONG_NAME = "route_long_name";
    public static  final String KEY_ROUTE_DESC = "route_desc";
    public static  final String KEY_ROUTE_TYPE = "route_type";
    public static  final String KEY_ROUTE_URL = "route_url";
    public static  final String KEY_ROUTE_COLOR = "route_color";
    public static  final String KEY_ROUTE_TEXT_COLOR = "route_text_color";

    private String route_id;
    private String agency_id;
    private String route_short_name;
    private String route_long_name;
    private String route_desc;
    private int route_type = 3;
    private String route_url;
    private String route_color;
    private String route_text_color;

    public Routes(String _route_id, String _agency_id, String _route_short_name, String _route_long_name,
                  String _route_desc, int _route_type, String _route_url,String _route_color,
                  String _route_text_color){

        this.route_id = _route_id;
        this.agency_id = _agency_id;
        this.route_short_name = _route_short_name;
        this.route_long_name = _route_long_name;
        this.route_desc = _route_desc;
        this.route_type = _route_type;
        this.route_url = _route_url;
        this.route_color = _route_color;
        this.route_text_color = _route_text_color;
    }

    public String get_route_id(){
        return this.route_id;
    }

    public void set_route_id(String route_id){
        this.route_id = route_id;
    }

    public String get_agency_id(){
        return this.agency_id;
    }

    public void set_agency_id(String agency_id){
        this.agency_id = agency_id;
    }

    public String get_route_short_name(){
        return this.route_short_name;
    }

    public void set_route_short_name(String route_short_name){
        this.route_short_name = route_short_name;
    }

    public String get_route_long_name(){
        return this.route_long_name;
    }

    public void set_route_long_name(String route_long_name){
        this.route_long_name = route_long_name;
    }

    public String get_route_desc(){
        return this.route_desc;
    }

    public void set_route_desc(String route_desc){
        this.route_desc = route_desc;
    }

    public int get_route_type(){
        return this.route_type;
    }

    public void set_route_type(int route_type){
        this.route_type = route_type;
    }

    public String get_route_color(){
        return this.route_color;
    }

    public void set_route_color(String route_color){
        this.route_color = route_color;
    }

    public String get_route_text_color(){
        return this.route_text_color;
    }

    public void set_route_text_color(String route_text_color){
        this.route_text_color = route_text_color;
    }
}

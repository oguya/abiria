package com.droid.abiria.db;

/**
 * Created by james on 23/01/14.
 *
 * Trip info
 */
public class Trip {

    public static final String KEY_ROUTE_ID = "route_id";
    public static final String KEY_SERVICE_ID = "service_id";
    public static final String KEY_TRIP_ID = "trip_id";
    public static final String KEY_TRIP_HEADSIGN = "trip_headsign";
    public static final String KEY_DIRECTION_ID = "direction_id";
    public static final String KEY_BLOCK_ID = "block_id";
    public static final String KEY_SHAPE_ID = "shape_id";

    private String route_id;
    private String service_id;
    private String trip_id;
    private String trip_headsign;
    private int direction_id;
    private String block_id;
    private String shape_id;

    public Trip(){}

    public Trip(String _route_id, String _service_id, String _trip_id, String _trip_headsign,
                int _direction_id, String _block_id, String _shape_id){
        this.route_id = _route_id;
        this.service_id = _service_id;
        this.trip_id = _trip_id;
        this.trip_headsign = _trip_headsign;
        this.direction_id = _direction_id;
        this.block_id = _block_id;
        this.shape_id = _shape_id;
    }


}

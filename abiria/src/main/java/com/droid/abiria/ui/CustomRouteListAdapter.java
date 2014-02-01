package com.droid.abiria.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.droid.abiria.R;
import com.droid.abiria.db.Route;

import java.util.List;

/**
 * Created by james on 24/01/14.
 *
 * load custom listview for routes
 */
public class CustomRouteListAdapter extends ArrayAdapter<Route> {

    private Activity context;
    private List<Route> routeList;
    private int lastPosition = -1;

    public CustomRouteListAdapter(Activity _context, List<Route> _routeList){
        super(_context, R.layout.route_row_layout, _routeList);
        this.context = _context;
        this.routeList = _routeList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View rowView = convertView;

        if(rowView == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            rowView = layoutInflater.inflate(R.layout.route_row_layout, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.route_name = (TextView)rowView.findViewById(R.id.route_nameTXT);
            viewHolder.route_desc = (TextView)rowView.findViewById(R.id.route_descTXT);

            rowView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder)rowView.getTag();
        String route_name = routeList.get(position).get_route_short_name();
        viewHolder.route_name.setText("Route "+route_name);
        String route_desc = routeList.get(position).get_route_desc();
        viewHolder.route_desc.setText(route_desc);

        //animate rows
        Animation animation = AnimationUtils.loadAnimation(this.context, (position > lastPosition) ?
        R.anim.up_from_bottom : R.anim.down_from_top);
        rowView.startAnimation(animation);
        lastPosition = position;

        return rowView;
    }

    public static class ViewHolder{
        public TextView route_name;
        public TextView route_desc;
    }
}

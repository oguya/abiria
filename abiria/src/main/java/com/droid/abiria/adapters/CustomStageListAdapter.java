package com.droid.abiria.adapters;

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
import com.droid.abiria.db.Stop;

import java.util.List;

/**
 * Created by james on 01/02/14.
 */
public class CustomStageListAdapter extends ArrayAdapter<Stop> {
    private Activity context;
    private List<Stop> stopList;
    private int lastPosition = -1;

    public CustomStageListAdapter(Activity _context, List<Stop> _stopList){
        super(_context, R.layout.stage_row_layout, _stopList);
        this.context = _context;
        this.stopList = _stopList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View rowView = convertView;

        if(rowView == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            rowView = layoutInflater.inflate(R.layout.stage_row_layout, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.stage_name = (TextView)rowView.findViewById(R.id.stage_name);
            viewHolder.distance = (TextView)rowView.findViewById(R.id.distance);

            rowView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder)rowView.getTag();
        String stage_name = stopList.get(position).get_stop_name();
        viewHolder.stage_name.setText(stage_name);
        Float distance = stopList.get(position).get_distance();
        viewHolder.distance.setText("Approximately "+String.valueOf(distance)+" meters away");

        //animate rows
        Animation animation = AnimationUtils.loadAnimation(this.context, (position > lastPosition) ?
                R.anim.up_from_bottom : R.anim.down_from_top);
        rowView.startAnimation(animation);
        lastPosition = position;

        return rowView;
    }

    public static class ViewHolder{
        public TextView stage_name;
        public TextView distance;
    }
}

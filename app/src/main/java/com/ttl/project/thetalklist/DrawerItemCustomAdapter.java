package com.ttl.project.thetalklist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Saubhagyam on 03/06/2017.
 */

// Navigation drawer item list adapter

public class DrawerItemCustomAdapter extends ArrayAdapter<DrawerModel> {
    Context mContext;
    int layoutResourceId;
    DrawerModel data[] = null;
    private static final String TAG = "DrawerItemCustomAdapter";

    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, DrawerModel[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        final ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.customDrawerIcon);
        TextView textViewName = (TextView) listItem.findViewById(R.id.customDrawerText);
        LinearLayout linearLayout = (LinearLayout) listItem.findViewById( R.id.drawer_item_layout);
        DrawerModel folder = data[position];


        imageViewIcon.setImageResource(folder.icon);
        textViewName.setText(folder.name);
        if ( folder.isSelected == true ) {
            textViewName.setTextColor( Color.rgb( 3, 169, 244) );
            linearLayout.setBackgroundColor( Color.rgb( 240, 240, 240) );
        }
        return listItem;
    }
}

package com.ttl.project.thetalklist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Saubhagyam on 03/06/2017.
 */

// Navigation drawer item list adapter

public class DrawerItemCustomAdapter extends ArrayAdapter<DrawerModel> {
    Context mContext;
    int layoutResourceId;
    DrawerModel data[] = null;

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

        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.customDrawerIcon);
        TextView textViewName = (TextView) listItem.findViewById(R.id.customDrawerText);

        DrawerModel folder = data[position];


        imageViewIcon.setImageResource(folder.icon);
        textViewName.setText(folder.name);

        return listItem;
    }
}

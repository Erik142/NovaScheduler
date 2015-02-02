package com.erik.novascheduler;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by erikwahlberger on 2015-02-01.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<schemaDrawerItem> {

    Context context;
    Activity activity;
    int layoutResID;
    LayoutInflater mInflater;

    public NavigationDrawerAdapter(Context context, Activity MainActivity, int resourceID, List<schemaDrawerItem> objects) {
        super(context, resourceID, objects);
        this.layoutResID = resourceID;
        this.context = context;
        this.activity = MainActivity;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        schemaDrawerItem item = getItem(position);

        if (convertView == null) {
            convertView = mInflater.inflate(layoutResID, parent, false);
        }

        TextView scheduleName = (TextView)convertView.findViewById(R.id.scheduleName);
        TextView scheduleSchool = (TextView)convertView.findViewById(R.id.scheduleSchool);

        scheduleName.setText(item.getFreeTextBox());
        scheduleSchool.setText(item.getSchool(activity));

        return convertView;
    }

}

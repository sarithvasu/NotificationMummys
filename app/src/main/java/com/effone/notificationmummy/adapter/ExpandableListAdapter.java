package com.effone.notificationmummy.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.effone.notificationmummy.R;
import com.effone.notificationmummy.common.AppInfo;
import com.effone.notificationmummy.database.DataBaseHandler;
import com.effone.notificationmummy.fragments.IndivalMessageFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.effone.notificationmummy.Service.NotificationListener.appName;

/**
 * Created by sumanth.peddinti on 5/28/2018.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    int va = 0;
    private List<String> header; // header titles
    // Child data in format of header title, child title
    private HashMap<String, List<AppInfo>> child;
    private DataBaseHandler mDataBaseHandler;

    MyInterface myInterface;
    public ExpandableListAdapter(Context context, HashMap<String, List<AppInfo>> hashMap, ArrayList<String> header, MyInterface myInterface) {
        this._context = context;
        this.child = hashMap;
        this.header = header;
        mDataBaseHandler = DataBaseHandler.getInstance(context);
        this.myInterface=myInterface;

    }

    public interface MyInterface {
        void someEvent(int value);
    }

    @Override
    public int getGroupCount() {
        // Get header size
        return this.header.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // return children count
        return this.child.get(this.header.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // Get header position
        return this.header.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // This will return the child
        return this.child.get(this.header.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        // Getting header title
        String headerTitle = (String) getGroup(groupPosition);

        // Inflating header layout and setting text
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.header, parent, false);
        }

        TextView header_text = (TextView) convertView.findViewById(R.id.header);
        header_text.setText(headerTitle);
        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);
        // If group is expanded then change the text into bold and change the
        // icon

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // Getting child text
        final AppInfo childText = (AppInfo) getChild(groupPosition, childPosition);
        if (groupPosition == 0) {
            appName.clear();
            appName.addAll(child.get(getGroup(groupPosition)));
        }
        // Inflating child layout and setting textview
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.childs, parent, false);
        }

        String input = "<font color=#064D45>" + childText.getAppname() + "</font>" + " (" + "<font color=#778899>" + childText.getCount()+ "</font>)";

        TextView child_text = (TextView) convertView.findViewById(R.id.child);
        child_text.setText(Html.fromHtml(input));

        final Switch sw_bar = convertView.findViewById(R.id.sw_bar);

        ImageView mImageView = convertView.findViewById(R.id.icon);
        if(groupPosition ==1)
        sw_bar.setChecked(false);
        PackageManager packageManager = _context.getPackageManager();
        Drawable icon = null;
        try {
            icon = packageManager.getApplicationIcon(childText.getPname());
            mImageView.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        sw_bar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(isChecked)
                {
                    mDataBaseHandler.updateTheFlag(childText, 1);//means true
                }else {
                    mDataBaseHandler.updateTheFlag(childText, 0);//means false
                }

                myInterface.someEvent(0);

            }


        });
        if(groupPosition !=1)
        child_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AppInfo childText = (AppInfo) getChild(groupPosition, childPosition);
                IndivalMessageFragment indivalMessageFragment = new IndivalMessageFragment();
                indivalMessageFragment.setTitle(childText);
                FragmentTransaction ft = ((FragmentActivity) _context).getSupportFragmentManager().beginTransaction();
                ft.add(R.id.fl_bottom, indivalMessageFragment).addToBackStack(null).commit();
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

}

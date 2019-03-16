package com.mindclarity.checkmaid.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindclarity.checkmaid.data.common_models.NavigationDrawer;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.ui.activities.CleaningGuideActivity;
import com.mindclarity.checkmaid.ui.activities.ClockInClockOutActivity;
import com.mindclarity.checkmaid.ui.activities.DamageReportsActivity;
import com.mindclarity.checkmaid.ui.activities.FindPropertyActivity;
import com.mindclarity.checkmaid.ui.activities.PhotoLogsActivity;
import com.mindclarity.checkmaid.ui.activities.PropertiesActivity;
import com.mindclarity.checkmaid.ui.activities.SettingsActivity;
import com.mindclarity.checkmaid.ui.activities.TimeSheetsActivity;
import com.mindclarity.checkmaid.ui.activities.UserTimeSheetActivity;
import com.mindclarity.checkmaid.ui.activities.UsersActivity;

import java.util.HashMap;
import java.util.List;

public class ExpandableListViewNavDrawerAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<NavigationDrawer> mListDataHeader;
    private HashMap<NavigationDrawer, List<String>> mListDataChild;
    ExpandableListView expandList;

    public ExpandableListViewNavDrawerAdapter(Context context, List<NavigationDrawer> listDataHeader, HashMap<NavigationDrawer, List<String>> listChildData, ExpandableListView mView) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
        this.expandList = mView;
    }

    @Override
    public int getGroupCount() {
        int i = mListDataHeader.size();
        Log.d("GROUPCOUNT", String.valueOf(i));
        return this.mListDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List childList = mListDataChild.get(mListDataHeader.get(groupPosition));
        if (childList != null && !childList.isEmpty()) {
            return childList.size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List childList = mListDataChild.get(mListDataHeader.get(groupPosition));
        if (childList != null && !childList.isEmpty()) {
            return childList.get(childPosition);
        }
        return null;
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
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent) {
        final NavigationDrawer headerTitle = (NavigationDrawer) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_lv_group_items, null);
        }
        TextView lblListHeader = convertView.findViewById(R.id.submenu);
        final ImageView headerIcon = convertView.findViewById(R.id.iconimage);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle.getIconName());
        headerIcon.setImageResource(headerTitle.getIconImg());
        if (!headerTitle.isEnabled()) {
            lblListHeader.setAlpha(.5f);
        }

        LinearLayout ll_main =  convertView.findViewById(R.id.ll_main);
        ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (headerTitle.getIconName().equals(mContext.getResources().getString(R.string.properties))){
                        if (headerTitle.isEnabled()) {
                            mContext.startActivity(new Intent(mContext,PropertiesActivity.class));
                        }
                    }
                    else if (headerTitle.getIconName().equals(mContext.getResources().getString(R.string.users))){
                        if (headerTitle.isEnabled()) {
                            mContext.startActivity(new Intent(mContext,UsersActivity.class));
                        }
                    }
                    else if (headerTitle.getIconName().equals(mContext.getResources().getString(R.string.timesheets))){
                        if (headerTitle.isEnabled()) {
                            mContext.startActivity(new Intent(mContext,TimeSheetsActivity.class));
                        }
                    }
                    else if (headerTitle.getIconName().equals(mContext.getResources().getString(R.string.photologs))){
                        if (headerTitle.isEnabled()) {
                            mContext.startActivity(new Intent(mContext,PhotoLogsActivity.class));
                        }
                    }
                    else if (headerTitle.getIconName().equals(mContext.getResources().getString(R.string.damage_reports))){
                        if (headerTitle.isEnabled()) {
                            mContext.startActivity(new Intent(mContext,DamageReportsActivity.class));
                        }
                    }
                    //////////////////////////User Portion///////////////////////////
                    else if (headerTitle.getIconName().equals(mContext.getResources().getString(R.string.find_a_property))){
                        if (headerTitle.isEnabled()) {
                            mContext.startActivity(new Intent(mContext,FindPropertyActivity.class));
                        }
                    }else if (headerTitle.getIconName().equals(mContext.getResources().getString(R.string.cleaning_guides))){
                        if (headerTitle.isEnabled()) {
                            mContext.startActivity(new Intent(mContext,CleaningGuideActivity.class));
                        }
                    }else if (headerTitle.getIconName().equals(mContext.getResources().getString(R.string.clock_in_clock_out))){
                        if (headerTitle.isEnabled()) {
                            mContext.startActivity(new Intent(mContext,ClockInClockOutActivity.class));
                        }
                    }else if (headerTitle.getIconName().equals(mContext.getResources().getString(R.string.timesheet))){
                        if (headerTitle.isEnabled()) {
                            mContext.startActivity(new Intent(mContext,UserTimeSheetActivity.class));
                        }
                    }
                    ////////////////////////////////////////////////////////////////


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText;
        if (getChild(groupPosition, childPosition) != null) {
            childText = (String) (getChild(groupPosition, childPosition));
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.expandable_lv_child_items, null);
            }
            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.submenu);
            txtListChild.setText(childText);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
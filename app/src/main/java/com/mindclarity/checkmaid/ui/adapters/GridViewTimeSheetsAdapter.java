package com.mindclarity.checkmaid.ui.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.common_models.TimeSheet;

import java.util.List;

public class GridViewTimeSheetsAdapter extends BaseAdapter {

    Context context;
    private List<TimeSheet> arraylist;

    public GridViewTimeSheetsAdapter(Context context, List<TimeSheet> arraylist) {
        this.context = context;
        this.arraylist=arraylist;
    }
    @Override
    public int getCount() {
        return arraylist.size();
    }
    @Override
    public Object getItem(int position) {
        return arraylist.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.gv_single_card_item, null);
        }
        ImageView ivImage =  convertView.findViewById(R.id.iv_image);
        TextView tvTitle = convertView.findViewById(R.id.tv_title);
        
        ivImage.setImageResource(arraylist.get(position).getImage());
        tvTitle.setText(arraylist.get(position).getTitle());
        
        return convertView;
    }
}




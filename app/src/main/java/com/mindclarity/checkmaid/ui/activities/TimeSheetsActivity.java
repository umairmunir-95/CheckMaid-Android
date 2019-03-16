package com.mindclarity.checkmaid.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.common_models.TimeSheet;
import com.mindclarity.checkmaid.ui.adapters.GridViewPropertiesAdapter;
import com.mindclarity.checkmaid.ui.adapters.GridViewTimeSheetsAdapter;

import java.util.ArrayList;
import java.util.List;

public class TimeSheetsActivity extends AppCompatActivity {

    private TextView tvHeader;
    private ImageView ivBack;
    private GridView gvTimeSheets;
    private GridViewTimeSheetsAdapter adapter;
    private String gridText[];
    private List<TimeSheet> timeSheets;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timesheets);
        initializeViews();
        prepareGridData();
    }

    public void onBackPressed() {

    }

    private void initializeViews() {

        tvHeader = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        gvTimeSheets=findViewById(R.id.grid_view);
        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setText(getResources().getString(R.string.timesheets));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TimeSheetsActivity.this, PropertiesActivity.class);
                startActivity(i);
            }
        });


    }

    private void prepareGridData() {
        timeSheets = new ArrayList<>();
        TimeSheet byUser = new TimeSheet();
        byUser.setTitle(getResources().getString(R.string.by_user));
        byUser.setImage(R.drawable.ic_users_white);
        TimeSheet byDate = new TimeSheet();
        byDate.setTitle(getResources().getString(R.string.by_date));
        byDate.setImage(R.drawable.ic_date_white);
        TimeSheet byProperty = new TimeSheet();
        byProperty.setTitle(getResources().getString(R.string.by_property));
        byProperty.setImage(R.drawable.ic_properties_white);

        timeSheets.add(byUser);
        timeSheets.add(byDate);
        timeSheets.add(byProperty);

        adapter = new GridViewTimeSheetsAdapter(TimeSheetsActivity.this, timeSheets);
        gvTimeSheets.setAdapter(adapter);
        gvTimeSheets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=null;
                switch (position) {
                    case 0:
                        intent=new Intent(TimeSheetsActivity.this,TimeSheetDetailsActivity.class);
                        intent.putExtra(getResources().getString(R.string.from),getResources().getString(R.string.by_user));
                        startActivity(intent);
                        break;
                    case 1:
                        intent=new Intent(TimeSheetsActivity.this,TimeSheetDetailsActivity.class);
                        intent.putExtra(getResources().getString(R.string.from),getResources().getString(R.string.by_date));
                        startActivity(intent);
                        break;
                    case 2:
                        intent=new Intent(TimeSheetsActivity.this,TimeSheetDetailsActivity.class);
                        intent.putExtra(getResources().getString(R.string.from),getResources().getString(R.string.by_property));
                        startActivity(intent);
                        break;
                 }
            }
        });
    }
}


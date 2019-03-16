package com.mindclarity.checkmaid.ui.activities;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.utils.AppStore;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.ImageUtils;
import com.mindclarity.checkmaid.viewmodel.GuidesViewModel;
import com.mindclarity.checkmaid.viewmodel.PropertiesViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;

public class PropertyDetailsActivity extends AppCompatActivity {

    private TextView tvHeader,tvCheckInCheckOutTime,tvPropertyName,tvPropertyAddress;
    private ImageView ivBack,ivEditProperty,ivPicture;
    private RelativeLayout relativeLayoutEditProperty;
    private Button btnCheckInGuide,btnCleaningGuide;
    private FloatingActionButton floatingActionButtonMap;
    private ProgressBar progressBar;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private GuidesViewModel guidesViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);
        initializeViews();
        populateFields();
    }

    public void onBackPressed() {

    }

    private void initializeViews() {

        tvHeader = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        ivPicture=findViewById(R.id.iv_picture);
        tvPropertyName=findViewById(R.id.tv_property_name);
        tvPropertyAddress=findViewById(R.id.tv_property_address);
        tvCheckInCheckOutTime=findViewById(R.id.tv_time);
        ivEditProperty=findViewById(R.id.iv_edit);
        relativeLayoutEditProperty=findViewById(R.id.rl_edit_property);
        btnCheckInGuide=findViewById(R.id.btn_checking_guide);
        btnCleaningGuide=findViewById(R.id.btn_cleaning_guide);
        progressBar=findViewById(R.id.progress_bar);
        floatingActionButtonMap=findViewById(R.id.fab_map);
        sharedPreferencesHelper=new SharedPreferencesHelper(PropertyDetailsActivity.this);
        guidesViewModel= ViewModelProviders.of(this).get(GuidesViewModel.class);

        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        relativeLayoutEditProperty.setVisibility(View.VISIBLE);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PropertyDetailsActivity.this, PropertiesActivity.class);
                startActivity(i);
            }
        });

        floatingActionButtonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PropertyDetailsActivity.this,MapsActivity.class);
                startActivity(i);
            }
        });

        ivEditProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferencesHelper.setString(App.INTENT_FROM,getResources().getString(R.string.edit_property));
                Intent i = new Intent(PropertyDetailsActivity.this, AddNewPropertyActivity.class);
                startActivity(i);
            }
        });

        btnCheckInGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guidesViewModel.getPropertyByName(PropertyDetailsActivity.this,progressBar,false,getResources().getString(R.string.checkin_guide),App.LIST_ALL_GUIDES+"/"+AppStore.PROPERTY_NAME.trim());
            }
        });

        btnCleaningGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guidesViewModel.getPropertyByName(PropertyDetailsActivity.this,progressBar,false,getResources().getString(R.string.cleaning_guide),App.LIST_ALL_GUIDES+"/"+AppStore.PROPERTY_NAME.trim());
            }
        });
    }

    private void populateFields()
    {
        File file = new File(AppStore.PROPERTY_IMAGE_PATH);
        Picasso.get().load(file).fit().centerCrop().into(ivPicture);
        tvPropertyName.setText(AppStore.PROPERTY_NAME.trim());
        tvPropertyAddress.setText(AppStore.PROPERTY_ADDRESS.trim());
        tvHeader.setText(getResources().getString(R.string.properties));
        tvCheckInCheckOutTime.setText(getResources().getString(R.string.check_in_time)+" : "+AppStore.PROPERTY_CHECK_IN_TIME +" | "+getResources().getString(R.string.check_out_time)+" : "+AppStore.PROPERTY_CHECK_OUT_TIME);
    }
}

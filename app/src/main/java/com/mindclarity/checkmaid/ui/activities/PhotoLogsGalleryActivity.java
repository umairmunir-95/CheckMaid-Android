package com.mindclarity.checkmaid.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.common_models.GalleryModel;
import com.mindclarity.checkmaid.ui.adapters.GalleryAdapter;
import com.mindclarity.checkmaid.utils.AppStore;
import com.mindclarity.checkmaid.utils.GridViewItemDecoration;
import com.mindclarity.checkmaid.utils.Helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoLogsGalleryActivity extends AppCompatActivity {

    private TextView tvHeader;
    private ImageView ivBack;
    private RecyclerView recyclerView;
    private FloatingActionButton fabShare;
    private FloatingActionButton fabDownload;

    private GalleryAdapter adapter;
    private List<GalleryModel> imagesList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photologs_gallery);
        initializeViews();
        prepareGalleryData();
    }

    public void onBackPressed() {

    }

    private void initializeViews() {

        tvHeader = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        fabShare=findViewById(R.id.fab_share);
        fabDownload=findViewById(R.id.fab_download);
        recyclerView=findViewById(R.id.recycler_view);
        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        if(Helpers.getPreferenceValue(PhotoLogsGalleryActivity.this, App.INTENT_FROM).equals(getResources().getString(R.string.photo_logs))) {
//            tvHeader.setText(AppStore.PHOTO_LOGS_PROPERTY_NAME);
            tvHeader.setText(getResources().getString(R.string.photo_logs));

        }
        else
        {
            tvHeader.setText(getResources().getString(R.string.damage_reports));
//            tvHeader.setText(AppStore.DAMAGE_REPORTS_PROPERTY_NAME);
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Helpers.getPreferenceValue(PhotoLogsGalleryActivity.this, App.INTENT_FROM).equals(getResources().getString(R.string.photo_logs)))
                {
                    Intent i = new Intent(PhotoLogsGalleryActivity.this, PhotoLogsActivity.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(PhotoLogsGalleryActivity.this, DamageReportsActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    private void prepareGalleryData() {
        imagesList = new ArrayList<>();
        adapter = new GalleryAdapter(this, imagesList);
        File folder=null;

        if(Helpers.getPreferenceValue(PhotoLogsGalleryActivity.this, App.INTENT_FROM).equals(getResources().getString(R.string.photo_logs))) {
            folder = getDir(getResources().getString(R.string.photo_logs), Context.MODE_PRIVATE);
        }
        else
        {
            folder = getDir(getResources().getString(R.string.damage_reports), Context.MODE_PRIVATE);
        }

        File[] listOfPhotos = folder.listFiles();
        if (listOfPhotos != null) {
            for (File file : listOfPhotos)
            {
                GalleryModel galleryModel = new GalleryModel(file.getName(), "");
                imagesList.add(galleryModel);
                adapter.notifyDataSetChanged();
            }
        }
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridViewItemDecoration(2, Helpers.dpToPx(PhotoLogsGalleryActivity.this, 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}

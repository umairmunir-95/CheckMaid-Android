package com.mindclarity.checkmaid.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.utils.AppStore;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.ImageUtils;

import java.io.File;
import java.io.FileOutputStream;

public class SinglePhotoActivity extends AppCompatActivity {

    private TextView tvHeader;
    private ImageView ivBack;
    private ImageView ivPicture;
    private FloatingActionButton fabDownload,fabShare;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_photo);
        initializeViews();
        loadPicture();
    }
    public void onBackPressed() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            ImageUtils.deleteAfterShare(SinglePhotoActivity.this);
        }
    }

    private void initializeViews() {

        tvHeader = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        ivPicture=findViewById(R.id.iv_picture);
        fabDownload=findViewById(R.id.fab_download);
        fabShare=findViewById(R.id.fab_share);

        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        if(Helpers.getPreferenceValue(SinglePhotoActivity.this, App.INTENT_FROM).equals(getResources().getString(R.string.photo_logs))) {
//            tvHeader.setText(AppStore.PHOTO_LOGS_PROPERTY_NAME);
            tvHeader.setText(getResources().getString(R.string.photo_logs));
        }
        else
        {
//            tvHeader.setText(AppStore.DAMAGE_REPORTS_PROPERTY_NAME);
            tvHeader.setText(getResources().getString(R.string.damage_reports));
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SinglePhotoActivity.this, PhotoLogsSliderActivity.class);
                startActivity(i);
            }
        });

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtils.shareImage(SinglePhotoActivity.this,ivPicture);
            }
        });

        fabDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap=ImageUtils.imageViewToBitmap(SinglePhotoActivity.this,ivPicture);
                if(ImageUtils.downloadImage(bitmap))
                {
                    Toast.makeText(getApplicationContext(),"Saved successfully.",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Error.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loadPicture()
    {
        Bitmap bitmap=null;
        if(Helpers.getPreferenceValue(SinglePhotoActivity.this, App.INTENT_FROM).equals(getResources().getString(R.string.photo_logs))) {
            bitmap = ImageUtils.loadBitmapFromMemory(SinglePhotoActivity.this, getResources().getString(R.string.photo_logs), AppStore.PHOTO_LOGS_IMAGE_NAME);
        }
        else
        {
            bitmap = ImageUtils.loadBitmapFromMemory(SinglePhotoActivity.this, getResources().getString(R.string.damage_reports), AppStore.PHOTO_LOGS_IMAGE_NAME);
        }
        ivPicture.setImageBitmap(bitmap);
    }
}

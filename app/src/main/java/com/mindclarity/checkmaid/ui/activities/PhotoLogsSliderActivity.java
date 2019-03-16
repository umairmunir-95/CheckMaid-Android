package com.mindclarity.checkmaid.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.utils.AppStore;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.ImageUtils;
import java.io.File;

public class PhotoLogsSliderActivity extends AppCompatActivity {

    private TextView tvHeader;
    private ImageView ivBack;
    private FloatingActionButton fabNext,fabPrevious;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_slider);
        initializeViews();
        setSliderViews();
    }
    public void onBackPressed() {

    }

    private void initializeViews() {

        tvHeader = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        fabNext=findViewById(R.id.fab_next);
        fabPrevious=findViewById(R.id.fab_previous);
        linearLayout= findViewById(R.id.view_flipper_root);
        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        if(Helpers.getPreferenceValue(PhotoLogsSliderActivity.this, App.INTENT_FROM).equals(getResources().getString(R.string.photo_logs))) {
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
                Intent i = new Intent(PhotoLogsSliderActivity.this, PhotoLogsGalleryActivity.class);
                startActivity(i);
            }
        });
    }

    private void setSliderViews() {

        final ViewFlipper viewFlipper = new ViewFlipper(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewFlipper.setLayoutParams(layoutParams);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.setAutoStart(false);

        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);

        File folder=null;
        if(Helpers.getPreferenceValue(PhotoLogsSliderActivity.this, App.INTENT_FROM).equals(getResources().getString(R.string.photo_logs))) {
            folder = getDir(getResources().getString(R.string.photo_logs), Context.MODE_PRIVATE);
        }
        else
        {
            folder = getDir(getResources().getString(R.string.damage_reports), Context.MODE_PRIVATE);
        }
        File[] listOfPhotos = folder.listFiles();
        if (listOfPhotos != null) {
            for (File file : listOfPhotos) {
                Bitmap bitmap=null;

                if(Helpers.getPreferenceValue(PhotoLogsSliderActivity.this, App.INTENT_FROM).equals(getResources().getString(R.string.photo_logs))) {
                    bitmap = ImageUtils.loadBitmapFromMemory(PhotoLogsSliderActivity.this, getResources().getString(R.string.photo_logs), file.getName());
                }
                else
                {
                    bitmap = ImageUtils.loadBitmapFromMemory(PhotoLogsSliderActivity.this, getResources().getString(R.string.damage_reports), file.getName());
                }
                ImageView imageView = new ImageView(this);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.CENTER;
                imageView.setLayoutParams(params);
                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                viewFlipper.addView(imageView);
            }
            linearLayout.addView(viewFlipper);
        }


        fabPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showPrevious();
            }
        });

        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showNext();
            }
        });

        viewFlipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppStore.PHOTO_LOGS_IMAGE_NAME=String.valueOf(viewFlipper.getDisplayedChild());
                Intent i=new Intent(PhotoLogsSliderActivity.this,SinglePhotoActivity.class);
                startActivity(i);
            }
        });

        viewFlipper.getInAnimation().setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {}
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationEnd(Animation animation) {

                int displayedChild = viewFlipper.getDisplayedChild();
                int childCount = viewFlipper.getChildCount();

                if(displayedChild == childCount - 1)
                {
                    viewFlipper.stopFlipping();
                }

            }
        });

    }
}

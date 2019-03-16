package com.mindclarity.checkmaid.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.remote.models.photo_logs.create.CreatePhotoLogRequest;
import com.mindclarity.checkmaid.utils.AppStore;
import com.mindclarity.checkmaid.utils.DialogManager;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.ImageUtils;
import com.mindclarity.checkmaid.utils.NetworkManager;
import com.mindclarity.checkmaid.viewmodel.GuidesViewModel;
import com.mindclarity.checkmaid.viewmodel.PhotoLogsViewModel;

import java.util.List;

public class UserCleaningGuideActivity extends AppCompatActivity {

    private TextView tvHeader;
    private ImageView ivBack,ivPicture,ivTakePicture;
    private FloatingActionButton fab;
    private ProgressBar progressBar;
    private boolean canProceed;
    private PhotoLogsViewModel photoLogsViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_user_cleaning_guide);
        initializeViews();
    }

    public void onBackPressed() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ivTakePicture.setImageBitmap(bitmap);
            canProceed=true;
        }
    }

    private void initializeViews() {

        tvHeader = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        ivPicture=findViewById(R.id.iv_picture);
        progressBar=findViewById(R.id.progress_bar);
        ivTakePicture=findViewById(R.id.iv_take_picture);
        fab=findViewById(R.id.fab_next);
        canProceed=false;
        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setText(getResources().getString(R.string.cleaning_guide));
        ivPicture.setVisibility(View.VISIBLE);

        photoLogsViewModel=ViewModelProviders.of(this).get(PhotoLogsViewModel.class);

        ivPicture.setImageBitmap(ImageUtils.base64ToBitmap(AppStore.CLEANING_IMAGE));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserCleaningGuideActivity.this, ClockInClockOutDetailsActivity.class);
                startActivity(i);
            }
        });

        ivTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (NetworkManager.isNetworkAvailable(UserCleaningGuideActivity.this)) {
                    if (canProceed) {
                        photoLogsViewModel.createPhotoLog(UserCleaningGuideActivity.this, progressBar, createPhotoLogRequest());
                    }
                    else
                    {
                        DialogManager.showInfoDialog(UserCleaningGuideActivity.this,"",getResources().getString(R.string.take_picture_before_proceed),false);
                    }
                }
                else
                {
                    DialogManager.showInfoDialog(UserCleaningGuideActivity.this,"",getResources().getString(R.string.internet_available_msg),false);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private CreatePhotoLogRequest createPhotoLogRequest()
    {
        CreatePhotoLogRequest createPhotoLogRequest=new CreatePhotoLogRequest();
        createPhotoLogRequest.setPropertyName(AppStore.PROPERTY_NAME);
        createPhotoLogRequest.setDate(Helpers.getCurrectDate(App.CALENDER_DATE_FORMAT));
        createPhotoLogRequest.setImage(ImageUtils.bitmapToBase64(ImageUtils.imageViewToBitmap(UserCleaningGuideActivity.this,ivTakePicture)));
        return createPhotoLogRequest;
    }

    private void requestPermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 1);
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            DialogManager.showSettingsDialog(UserCleaningGuideActivity.this);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

}


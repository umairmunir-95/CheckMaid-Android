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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.mindclarity.checkmaid.data.remote.models.damage_report.create_damage_report.CreateDamageReportRequest;
import com.mindclarity.checkmaid.utils.AppStore;
import com.mindclarity.checkmaid.utils.DialogManager;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.ImageUtils;
import com.mindclarity.checkmaid.utils.NetworkManager;
import com.mindclarity.checkmaid.viewmodel.DamageReportsViewModel;

import java.util.List;

public class SubmitDamageReportActivity extends AppCompatActivity{

    private TextView tvHeader;
    private ImageButton ibTakePicture;
    private ImageView ivBack,ivTakePicture;
    private ProgressBar progressBar;
    private EditText etNotes;
    private Button btnSubmit;

    private DamageReportsViewModel damageReportsViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_damage_report);
        initializeViews();
    }

    public void onBackPressed() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ibTakePicture.setVisibility(View.GONE);
            ivTakePicture.setVisibility(View.VISIBLE);
            ivTakePicture.setImageBitmap(bitmap);
        }
    }


    private void initializeViews() {

        tvHeader = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        ibTakePicture=findViewById(R.id.ib_take_picture);
        ivTakePicture=findViewById(R.id.iv_take_picture);
        etNotes=findViewById(R.id.et_notes);
        progressBar=findViewById(R.id.progress_bar);
        btnSubmit=findViewById(R.id.btn_submit);
        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setText(getResources().getString(R.string.damage_reports));

        damageReportsViewModel=ViewModelProviders.of(this).get(DamageReportsViewModel.class);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SubmitDamageReportActivity.this, ClockInClockOutDetailsActivity.class);
                startActivity(i);
            }
        });

        ibTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(NetworkManager.isNetworkAvailable(SubmitDamageReportActivity.this)) {
                    if (etNotes.getText().toString().trim().equals("")) {
                        etNotes.setError(getResources().getString(R.string.required));

                    } else {
                        if(ivTakePicture.getVisibility()==View.VISIBLE)
                        {
                            damageReportsViewModel.createDamageReport(SubmitDamageReportActivity.this,progressBar,etNotes,ibTakePicture,ivTakePicture,createDamageReportRequest());
                        }
                        else {
                            DialogManager.showInfoDialog(SubmitDamageReportActivity.this,"",getResources().getString(R.string.take_picture_before_proceed),false);
                        }
                    }
                }
                else
                {
                    DialogManager.showInfoDialog(SubmitDamageReportActivity.this,"",getResources().getString(R.string.internet_available_msg),false);
                }
            }
        });
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
                            DialogManager.showSettingsDialog(SubmitDamageReportActivity.this);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private CreateDamageReportRequest createDamageReportRequest()
    {
        CreateDamageReportRequest createDamageReportRequest=new CreateDamageReportRequest();
        createDamageReportRequest.setImage(ImageUtils.bitmapToBase64(ImageUtils.imageViewToBitmap(SubmitDamageReportActivity.this,ivTakePicture)));
        createDamageReportRequest.setDate(Helpers.getCurrectDate(App.CALENDER_DATE_FORMAT));
        createDamageReportRequest.setPropertyName(AppStore.PROPERTY_NAME.trim());
        createDamageReportRequest.setNotes(etNotes.getText().toString().trim());
        return createDamageReportRequest;
    }

}

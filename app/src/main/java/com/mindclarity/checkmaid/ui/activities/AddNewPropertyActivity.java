package com.mindclarity.checkmaid.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.data.remote.models.properties.add_property.request.AddPropertyRequest;
import com.mindclarity.checkmaid.data.remote.models.properties.add_property.response.AddPropertyResponse;
import com.mindclarity.checkmaid.utils.AppStore;
import com.mindclarity.checkmaid.utils.DialogManager;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.ImageUtils;
import com.mindclarity.checkmaid.utils.LocationManager;
import com.mindclarity.checkmaid.utils.MaterialTimePicker;
import com.mindclarity.checkmaid.utils.NetworkManager;
import com.mindclarity.checkmaid.viewmodel.PropertiesViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;



public class AddNewPropertyActivity extends AppCompatActivity {

    private TextView tvHeader;
    private ImageView ivBack, ivPicture,ivHelp;
    private EditText etPropertyName, etPropertyAddress, etCheckInTime, etCheckOutTime;
    private Spinner spCheckinRadius;
    private Button btnSubmit;
    private ProgressBar progressBar;
    private PropertiesViewModel propertiesViewModel;
    private SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_property);
        initializeViews();
        populateFields();
    }

    public void onBackPressed() {

    }


    private void initializeViews() {
        tvHeader = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        ivPicture = findViewById(R.id.iv_picture);
        ivHelp=findViewById(R.id.iv_help);
        spCheckinRadius = findViewById(R.id.sp_clockin_radius);
        etPropertyName = findViewById(R.id.et_property_name);
        etPropertyAddress = findViewById(R.id.et_property_addres);
        etCheckInTime = findViewById(R.id.et_checkin_time);
        etCheckOutTime = findViewById(R.id.et_checkout_time);
        btnSubmit = findViewById(R.id.btn_submit);
        progressBar = findViewById(R.id.progress_bar);

        sharedPreferencesHelper = new SharedPreferencesHelper(AddNewPropertyActivity.this);
        propertiesViewModel = ViewModelProviders.of(this).get(PropertiesViewModel.class);

        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setText(Helpers.getPreferenceValue(AddNewPropertyActivity.this, App.INTENT_FROM).equals(getResources().getString(R.string.edit_property)) ? getResources().getString(R.string.edit_property) : getResources().getString(R.string.add_property));
        btnSubmit.setText(Helpers.getPreferenceValue(AddNewPropertyActivity.this, App.INTENT_FROM).equals(getResources().getString(R.string.edit_property)) ? getResources().getString(R.string.update) : getResources().getString(R.string.submit));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddNewPropertyActivity.this, PropertiesActivity.class);
                startActivity(i);
            }
        });
        ivHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.showInfoDialog(AddNewPropertyActivity.this,"",getResources().getString(R.string.clock_in_radius_message),false);
            }
        });


        etCheckInTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    MaterialTimePicker.showTimePicker(AddNewPropertyActivity.this, etCheckInTime);
                    return true;
                }
                return false;
            }
        });
        etCheckOutTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    MaterialTimePicker.showTimePicker(AddNewPropertyActivity.this, etCheckOutTime);
                    return true;
                }
                return false;
            }
        });

        ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.isNetworkAvailable(AddNewPropertyActivity.this)) {
                    addEditProperty();
                } else {
                    DialogManager.showInfoDialog(AddNewPropertyActivity.this, "", getResources().getString(R.string.internet_available_msg), false);
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ivPicture.setImageBitmap(bitmap);
        }
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
                            DialogManager.showSettingsDialog(AddNewPropertyActivity.this);
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

    private void addEditProperty() {
        if (etPropertyName.getText().toString().equals("")) {
            etPropertyName.setError(getResources().getString(R.string.required));
        } else {
            if (etPropertyAddress.getText().toString().equals("")) {
                etPropertyAddress.setError(getResources().getString(R.string.required));
            } else {
                if (etCheckInTime.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.check_in_time_required), Toast.LENGTH_SHORT).show();
                } else {
                    if (etCheckOutTime.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.check_out_time_required), Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            LatLng latLng = LocationManager.getLocationFromAddress(AddNewPropertyActivity.this, etPropertyAddress.getText().toString());
                            if(latLng.latitude!=0 && latLng.longitude!=0) {
                                if (Helpers.getPreferenceValue(AddNewPropertyActivity.this, App.INTENT_FROM).equals(getResources().getString(R.string.edit_property))) {
                                    propertiesViewModel.editProperty(AddNewPropertyActivity.this, progressBar, App.LIST_ALL_PROPERTIES + "/" + AppStore.PROPERTY_ID, addPropertyRequest(latLng.latitude,latLng.longitude));
                                } else {
                                    propertiesViewModel.addProperty(AddNewPropertyActivity.this, progressBar, addPropertyRequest(latLng.latitude,latLng.longitude));
                                }
                            }
                            else
                            {
                                etPropertyAddress.setError(getResources().getString(R.string.required));
                                Toast.makeText(getApplicationContext(), "Unable to locate address on map.", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            etPropertyAddress.setError(getResources().getString(R.string.required));
                            Toast.makeText(getApplicationContext(), "Unable to locate address on map.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }
    }

    private AddPropertyRequest addPropertyRequest(Double latitude,Double longtiude) {
        AddPropertyRequest addPropertyRequest = new AddPropertyRequest();
        addPropertyRequest.setPropertyName(etPropertyName.getText().toString().trim());
        addPropertyRequest.setAddress(etPropertyAddress.getText().toString().trim());
        addPropertyRequest.setCheckInTime(etCheckInTime.getText().toString().trim());
        addPropertyRequest.setCheckOutTime(etCheckOutTime.getText().toString().trim());
        addPropertyRequest.setLatitude(latitude);
        addPropertyRequest.setLongitude(longtiude);
        addPropertyRequest.setCheckInRadius(Integer.valueOf(spCheckinRadius.getSelectedItem().toString().trim().replaceAll("ft", "")));
        addPropertyRequest.setImageBase64(ImageUtils.bitmapToBase64(ImageUtils.imageViewToBitmap(AddNewPropertyActivity.this, ivPicture)));
        return addPropertyRequest;
    }

    private void populateFields() {
        if(!AppStore.PROPERTY_ID.equals("")) {
            File file = new File(AppStore.PROPERTY_IMAGE_PATH);
            Picasso.get().load(file).fit().centerCrop().into(ivPicture);
            etPropertyName.setText(AppStore.PROPERTY_NAME.trim());
            etPropertyAddress.setText(AppStore.PROPERTY_ADDRESS.trim());
            etCheckInTime.setText(AppStore.PROPERTY_CHECK_IN_TIME);
            etCheckOutTime.setText(AppStore.PROPERTY_CHECK_OUT_TIME);
        }
    }
}
package com.mindclarity.checkmaid.ui.activities;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.mindclarity.checkmaid.data.common_models.Users;
import com.mindclarity.checkmaid.data.remote.models.clockin_clockout.create.ClockInClockOutRequest;
import com.mindclarity.checkmaid.ui.adapters.ListViewSearchUsersAdapter;
import com.mindclarity.checkmaid.utils.AppStore;
import com.mindclarity.checkmaid.utils.DialogManager;
import com.mindclarity.checkmaid.utils.GpsTracker;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.LocationManager;
import com.mindclarity.checkmaid.utils.NetworkManager;
import com.mindclarity.checkmaid.viewmodel.ClockInClockOutViewModel;
import com.mindclarity.checkmaid.viewmodel.PropertiesViewModel;

import java.util.ArrayList;
import java.util.List;

public class ClockInClockOutActivity extends AppCompatActivity {

    private TextView tvHeader;
    private ImageView ivBack;
    private EditText etSelectProperty;
    private ProgressBar progressBar;
    private Button btnArrived;
    private ClockInClockOutViewModel clockInClockOutViewModel;
    private PropertiesViewModel propertiesViewModel;
    private GpsTracker gpsTracker;
    public static long timerStartTime = 0;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clockin_clockout);
        initializeViews();
        requestPermission();
    }

    public void onBackPressed() {

    }

    private void initializeViews() {

        tvHeader = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        etSelectProperty=findViewById(R.id.et_select_property);
        btnArrived=findViewById(R.id.btn_arrived);
        progressBar=findViewById(R.id.progress_bar);
        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setText(getResources().getString(R.string.clock_in_clock_out));
        btnArrived.setEnabled(false);
        btnArrived.setAlpha(0.3f);

        clockInClockOutViewModel=ViewModelProviders.of(this).get(ClockInClockOutViewModel.class);
        propertiesViewModel=ViewModelProviders.of(this).get(PropertiesViewModel.class);

        etSelectProperty.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    showUsersListPopUp();
                    return true;
                }
                return false;
            }
        });



        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Helpers.getPreferenceValue(ClockInClockOutActivity.this,getResources().getString(R.string.user_type)).equals(getResources().getString(R.string.admin)))
                {
                    Intent i = new Intent(ClockInClockOutActivity.this, PropertiesActivity.class);
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(ClockInClockOutActivity.this, FindPropertyActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    private void initializeListeners()
    {
        etSelectProperty.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                gpsTracker=new GpsTracker(ClockInClockOutActivity.this);
                if(gpsTracker.canGetLocation())
                {
                    if(gpsTracker.getLatitude()!=0 && gpsTracker.getLongitude()!=0)
                    {
                        AppStore.CURRENT_LATITUDE=String.valueOf(gpsTracker.getLatitude());
                        AppStore.CURRENT_LONGITUDE=String.valueOf(gpsTracker.getLongitude());
                        if (!etSelectProperty.getText().toString().trim().equals("")) {
                            propertiesViewModel.getPropertyByName(ClockInClockOutActivity.this, progressBar, btnArrived, etSelectProperty,App.LIST_ALL_PROPERTIES + "/" + App.GET_BY_NAME + "/" + etSelectProperty.getText().toString().trim());
                        }
                    }
                    else
                    {
                        DialogManager.showInfoDialog(ClockInClockOutActivity.this,"",getResources().getString(R.string.unable_to_locate),false);
                    }
                }
                else
                {
                    DialogManager.gpsNotEnabledPopup(ClockInClockOutActivity.this);
                }
            }
        });
        btnArrived.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(NetworkManager.isNetworkAvailable(ClockInClockOutActivity.this))
                {
                    if(!etSelectProperty.getText().toString().trim().equals(""))
                    {
                        clockInClockOutViewModel.createTimeLog(ClockInClockOutActivity.this,progressBar,"clockIn",clockInClockOutRequest());
                    }
                }
                else
                {
                    DialogManager.showInfoDialog(ClockInClockOutActivity.this,"",getResources().getString(R.string.internet_available_msg),false);
                }
            }
        });
    }

    private void showUsersListPopUp()
    {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_user_list, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        EditText etSearch=promptsView.findViewById(R.id.et_search);
        ListView lvProperties=promptsView.findViewById(R.id.lv_users);
        TextView tvProperties=promptsView.findViewById(R.id.tv_option);
        ImageView ivCross=promptsView.findViewById(R.id.iv_close);
        tvProperties.setText(getResources().getString(R.string.list_of_properties));
        lvProperties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView tvSelectedProperty = view.findViewById(R.id.tv_name);
                TextView tvID = view.findViewById(R.id.tv_id);
                etSelectProperty.setText(""+tvSelectedProperty.getText().toString());
                alertDialog.cancel();
            }
        });
        alertDialogBuilder.setCancelable(true);
        alertDialog.show();
        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        ArrayList<Users> users=new ArrayList<>();
        String[] assignedProperties= Helpers.getPreferenceValue(ClockInClockOutActivity.this,getResources().getString(R.string.assigned_properties)).split(",");
        if(assignedProperties.length==0) {
            DialogManager.showInfoDialog(ClockInClockOutActivity.this, "", getResources().getString(R.string.no_property_assignment), false);
        }
        else{
            for(int i=0;i<assignedProperties.length;i++)
            {
                if(!(assignedProperties[i].trim().equals(""))) {
                    Users users1 = new Users();
                    users1.setUserName(assignedProperties[i]);
                    users1.setUserId("");
                    users.add(users1);
                }
            }
        }
        ListViewSearchUsersAdapter adapter=new ListViewSearchUsersAdapter(ClockInClockOutActivity.this,users);
        lvProperties.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ClockInClockOutRequest clockInClockOutRequest()
    {
        AppStore.CHECK_IN_TIME=Helpers.getCurrectDate(App.TIME_FORMAT);
        ClockInClockOutRequest clockInClockOutRequest=new ClockInClockOutRequest();
        clockInClockOutRequest.setUserID(Helpers.getPreferenceValue(ClockInClockOutActivity.this,getResources().getString(R.string.user_id)));
        clockInClockOutRequest.setUserName(Helpers.getPreferenceValue(ClockInClockOutActivity.this,getResources().getString(R.string.username)));
        clockInClockOutRequest.setUserEmail(Helpers.getPreferenceValue(ClockInClockOutActivity.this,getResources().getString(R.string.email)));
        clockInClockOutRequest.setPropertyID(AppStore.CLOCK_IN_OUT_PROPERTY_ID);
        clockInClockOutRequest.setPropertyName(etSelectProperty.getText().toString().trim());
        clockInClockOutRequest.setCheckInTime(Helpers.getCurrectDate(App.TIME_FORMAT));
        clockInClockOutRequest.setCheckOutTime("");
        clockInClockOutRequest.setDate(Helpers.getCurrectDate(App.CALENDER_DATE_FORMAT));
        return clockInClockOutRequest;
    }

    private void requestPermission() {
        Dexter.withActivity(ClockInClockOutActivity.this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            initializeListeners();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            DialogManager.showSettingsDialog(ClockInClockOutActivity.this);
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

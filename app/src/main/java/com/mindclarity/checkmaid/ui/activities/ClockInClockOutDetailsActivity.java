package com.mindclarity.checkmaid.ui.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.remote.models.clockin_clockout.create.ClockInClockOutRequest;
import com.mindclarity.checkmaid.utils.AppStore;
import com.mindclarity.checkmaid.utils.DialogManager;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.NetworkManager;
import com.mindclarity.checkmaid.viewmodel.ClockInClockOutViewModel;
import com.mindclarity.checkmaid.viewmodel.GuidesViewModel;

public class ClockInClockOutDetailsActivity  extends AppCompatActivity {

    private TextView tvHeader,tvLiveTime;
    private ImageView ivBack;
    private ProgressBar progressBar;
    private Button btnClockOut,btnDamageReport,btnCleaningGuide;
    private GuidesViewModel guidesViewModel;
    private ClockInClockOutViewModel clockInClockOutViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clockin_clockout_details);
        initializeViews();
        startTimeCounter();
    }

    public void onBackPressed() {

    }

    private void initializeViews() {

        tvHeader = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        progressBar=findViewById(R.id.progress_bar);
        tvLiveTime=findViewById(R.id.tv_live_time);
        btnDamageReport=findViewById(R.id.btn_damage_report);
        btnCleaningGuide=findViewById(R.id.btn_cleaning_guide);
        btnClockOut=findViewById(R.id.btn_finish);
        tvHeader.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setText(getResources().getString(R.string.clock_in_clock_out));

        guidesViewModel=ViewModelProviders.of(this).get(GuidesViewModel.class);
        clockInClockOutViewModel=ViewModelProviders.of(this).get(ClockInClockOutViewModel.class);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!AppStore.CHECK_OUT_TIME.equals("")) {
                    timerHandler.removeCallbacks(timerRunnable);
                    Intent i = new Intent(ClockInClockOutDetailsActivity.this, ClockInClockOutActivity.class);
                    startActivity(i);
                }
                else
                {
                    DialogManager.showInfoDialog(ClockInClockOutDetailsActivity.this,"",getResources().getString(R.string.need_to_checkout),false);
                }
            }
        });

        btnClockOut.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                if (NetworkManager.isNetworkAvailable(ClockInClockOutDetailsActivity.this)) {
                    clockInClockOutViewModel.createTimeLog(ClockInClockOutDetailsActivity.this, progressBar, "clockOut", clockInClockOutRequest());
                } else {
                    DialogManager.showInfoDialog(ClockInClockOutDetailsActivity.this, "", getResources().getString(R.string.internet_available_msg), false);
                }
            }
        });

        btnCleaningGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guidesViewModel.getPropertyByName(ClockInClockOutDetailsActivity.this,progressBar,true,getResources().getString(R.string.cleaning_guide),App.LIST_ALL_GUIDES+"/"+AppStore.PROPERTY_NAME.trim());
            }
        });

        btnDamageReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ClockInClockOutDetailsActivity.this,SubmitDamageReportActivity.class);
                startActivity(i);
            }
        });
    }

    private void startTimeCounter()
    {
        ClockInClockOutActivity.timerStartTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long milliseconds = System.currentTimeMillis() - ClockInClockOutActivity.timerStartTime;
            int seconds = (int) (milliseconds / 1000) % 60 ;
            int minutes = (int) ((milliseconds / (1000*60)) % 60);
            int hours   = (int) ((milliseconds / (1000*60*60)) % 24);

            tvLiveTime.setText(String.format("%02d:%02d:%02d",hours, minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacks(timerRunnable);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ClockInClockOutRequest clockInClockOutRequest()
    {
        AppStore.CHECK_OUT_TIME=Helpers.getCurrectDate(App.TIME_FORMAT);
        ClockInClockOutRequest clockInClockOutRequest=new ClockInClockOutRequest();
        clockInClockOutRequest.setUserID(Helpers.getPreferenceValue(ClockInClockOutDetailsActivity.this,getResources().getString(R.string.user_id)));
        clockInClockOutRequest.setUserName(Helpers.getPreferenceValue(ClockInClockOutDetailsActivity.this,getResources().getString(R.string.username)));
        clockInClockOutRequest.setUserEmail(Helpers.getPreferenceValue(ClockInClockOutDetailsActivity.this,getResources().getString(R.string.email)));
        clockInClockOutRequest.setPropertyID(AppStore.CLOCK_IN_OUT_PROPERTY_ID);
        clockInClockOutRequest.setPropertyName(AppStore.PROPERTY_NAME);
        clockInClockOutRequest.setCheckInTime(AppStore.CHECK_IN_TIME);
        clockInClockOutRequest.setCheckOutTime(Helpers.getCurrectDate(App.TIME_FORMAT));
        clockInClockOutRequest.setDate(Helpers.getCurrectDate(App.CALENDER_DATE_FORMAT));
        return clockInClockOutRequest;
    }
}

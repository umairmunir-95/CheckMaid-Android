package com.mindclarity.checkmaid.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.mindclarity.checkmaid.data.remote.models.clockin_clockout.create.ClockInClockOutRequest;
import com.mindclarity.checkmaid.data.repositories.ClockInClockOutRepository;

public class ClockInClockOutViewModel extends AndroidViewModel {

    private ClockInClockOutRepository clockInClockOutRepository;

    public ClockInClockOutViewModel(@NonNull Application application) {
        super(application);
        clockInClockOutRepository = new ClockInClockOutRepository(application);
    }

    public void createTimeLog(Context context, ProgressBar progressBar,String logType, ClockInClockOutRequest clockInClockOutRequest) {
        clockInClockOutRepository.createTimeLog(context, progressBar,logType, clockInClockOutRequest);
    }

    public void getLogsByUser(Context context, ProgressBar progressBar, TextView tvTotalTime, TableLayout tableLayout, String url, FloatingActionButton fabDownload,FloatingActionButton fabShare) {
        clockInClockOutRepository.getLogsByUser(context, progressBar,tvTotalTime,tableLayout, url,fabDownload,fabShare);
    }

    public void createTimeSheetCSV(Context context) {
        clockInClockOutRepository.createTimeSheetCSV(context);
    }
}





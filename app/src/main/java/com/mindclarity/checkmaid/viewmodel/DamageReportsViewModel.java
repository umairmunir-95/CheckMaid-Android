package com.mindclarity.checkmaid.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mindclarity.checkmaid.data.remote.models.damage_report.create_damage_report.CreateDamageReportRequest;
import com.mindclarity.checkmaid.data.repositories.DamageReportsRepository;

public class DamageReportsViewModel extends AndroidViewModel {

    private DamageReportsRepository damageReportsRepository;

    public DamageReportsViewModel(@NonNull Application application) {
        super(application);
        damageReportsRepository=new DamageReportsRepository(application);
    }

    public void createDamageReport(Context context, ProgressBar progressBar, EditText editText, ImageButton imageButton, ImageView imageView, CreateDamageReportRequest createDamageReportRequest) {
        damageReportsRepository.createDamageReport(context, progressBar,editText, imageButton,imageView,createDamageReportRequest);
    }

    public void getDamageReports(Context context, ProgressBar progressBar, String url)
    {
        damageReportsRepository.getDamageReports(context,progressBar,url);
    }
}

package com.mindclarity.checkmaid.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ProgressBar;

import com.mindclarity.checkmaid.data.remote.models.photo_logs.create.CreatePhotoLogRequest;
import com.mindclarity.checkmaid.data.repositories.PhotoLogRepository;

public class PhotoLogsViewModel extends AndroidViewModel {

    private PhotoLogRepository photoLogRepository;

    public PhotoLogsViewModel(@NonNull Application application)
    {
        super(application);
        photoLogRepository=new PhotoLogRepository(application);
    }

    public void createPhotoLog(Context context, ProgressBar progressBar, CreatePhotoLogRequest createPhotoLogRequest)
    {
        photoLogRepository.createPhotoLog(context,progressBar,createPhotoLogRequest);
    }

    public void getPhotoLogs(Context context, ProgressBar progressBar, String url)
    {
        photoLogRepository.getPhotoLogs(context,progressBar,url);
    }
}

package com.mindclarity.checkmaid.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.mindclarity.checkmaid.data.remote.models.guides.create_guide.CreateGuideRequest;
import com.mindclarity.checkmaid.data.remote.models.guides.update_guide.UpdateGuideRquestModel;
import com.mindclarity.checkmaid.data.repositories.GuidesRepository;

public class GuidesViewModel extends AndroidViewModel {

    private GuidesRepository guidesRepository;

    public GuidesViewModel(@NonNull Application application) {
        super(application);
        guidesRepository = new GuidesRepository(application);
    }

    public void getPropertyByName(Context context, ProgressBar progressBar, boolean isUserGuide,String goTo,String url) {
        guidesRepository.getPropertyName(context, progressBar,isUserGuide ,goTo, url);
    }

    public void createGuide(Context context, ProgressBar progressBar, CreateGuideRequest createGuideRequest) {
        guidesRepository.createGuide(context, progressBar, createGuideRequest);
    }

    public void updateGuide(Context context, ProgressBar progressBar, String url, UpdateGuideRquestModel updateGuideRquestModel) {
        guidesRepository.updateGuide(context, progressBar, url, updateGuideRquestModel);
    }

    public void findProperty(final Context context, final ProgressBar progressBar, String url, boolean isCheckIn, final LinearLayout llPicture, final LinearLayout llNotes, final ImageView ivPicture, final LinearLayout viewFlipper) {
        guidesRepository.findProperty(context, progressBar, url,isCheckIn, llPicture, llNotes, ivPicture, viewFlipper);
    }
}



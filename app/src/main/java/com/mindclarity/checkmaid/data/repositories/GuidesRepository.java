package com.mindclarity.checkmaid.data.repositories;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.data.remote.ApiClient;
import com.mindclarity.checkmaid.data.remote.interfaces.GuidesInterface;
import com.mindclarity.checkmaid.data.remote.models.guides.create_guide.CreateGuideRequest;
import com.mindclarity.checkmaid.data.remote.models.guides.create_guide.CreateGuideResponse;
import com.mindclarity.checkmaid.data.remote.models.guides.get_guide_by_property.GuidesResponseModel;
import com.mindclarity.checkmaid.data.remote.models.guides.update_guide.UpdateGuideResponseModel;
import com.mindclarity.checkmaid.data.remote.models.guides.update_guide.UpdateGuideRquestModel;
import com.mindclarity.checkmaid.ui.activities.CreateCheckInGuideActivity;
import com.mindclarity.checkmaid.ui.activities.CreateCleaningGuideActivity;
import com.mindclarity.checkmaid.ui.activities.PropertyDetailsActivity;
import com.mindclarity.checkmaid.ui.activities.UserCleaningGuideActivity;
import com.mindclarity.checkmaid.utils.AppStore;
import com.mindclarity.checkmaid.utils.DialogManager;
import com.mindclarity.checkmaid.utils.Helpers;
import com.mindclarity.checkmaid.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuidesRepository {

    private SharedPreferencesHelper sharedPreferencesHelper;
    
    public GuidesRepository(Application application)
    {
        
    }

    public void getPropertyName(final Context context, final ProgressBar progressBar, final boolean isUserGuide, final String goTo, String url) {

        GuidesInterface guidesInterface = ApiClient.getClient().create(GuidesInterface.class);
            Call<List<GuidesResponseModel>> call = guidesInterface.getGuideByPropertyName(url);
        progressBar.setVisibility(View.VISIBLE);
        sharedPreferencesHelper=new SharedPreferencesHelper(context);
        call.enqueue(new Callback<List<GuidesResponseModel>>() {
            @Override
            public void onResponse(Call<List<GuidesResponseModel>> call, Response<List<GuidesResponseModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if(response.body().size()==0) {
                        if (goTo.equals(context.getResources().getString(R.string.checkin_guide))) {
                            AppStore.CHECK_IN_ID = "";
                            AppStore.CHECK_IN_GUIDE_TYPE = "";
                            AppStore.CHECK_IN_PROPERTY_NAME = "";
                            AppStore.CHECK_IN_PROPERTY_ADDRESS = "";
                            AppStore.CHECK_IN_IMAGE = "";
                            AppStore.CHECK_IN_NOTES = "";
                            AppStore.CHECK_IN_PHOTO_PROOF = "";
                            Intent intent = new Intent(context, CreateCheckInGuideActivity.class);
                            context.startActivity(intent);
                        } else {
                            AppStore.CLEANING_ID = "";
                            AppStore.CLEANING_GUIDE_TYPE = "";
                            AppStore.CLEANING_PROPERTY_NAME = "";
                            AppStore.CLEANING_PROPERTY_ADDRESS = "";
                            AppStore.CLEANING_IMAGE = "";
                            AppStore.CLEANING_NOTES = "";
                            AppStore.CLEANING_PHOTO_PROOF = "";
                            if (!isUserGuide) {
                                Intent intent = new Intent(context, CreateCleaningGuideActivity.class);
                                context.startActivity(intent);
                            } else {
                                DialogManager.showNetworkInfo(context, "", context.getResources().getString(R.string.no_data_found), "");
                            }
                        }
                    }
                    else {
                        for (int i = 0; i < response.body().size(); i++) {

                            if (response.body().get(i).getGuideType().equals("checkin")) {
                                AppStore.CHECK_IN_ID = "" + response.body().get(i).getId();
                                AppStore.CHECK_IN_GUIDE_TYPE = response.body().get(i).getGuideType();
                                AppStore.CHECK_IN_PROPERTY_NAME = response.body().get(i).getPropertyName();
                                AppStore.CHECK_IN_PROPERTY_ADDRESS = response.body().get(i).getPropertyAddress();
                                AppStore.CHECK_IN_IMAGE = response.body().get(i).getImage();
                                AppStore.CHECK_IN_NOTES = response.body().get(i).getNotes();
                                AppStore.CHECK_IN_PHOTO_PROOF = "" + response.body().get(i).getRequireGPSProof();
                            } else {
                                AppStore.CLEANING_ID = "" + response.body().get(i).getId();
                                AppStore.CLEANING_GUIDE_TYPE = response.body().get(i).getGuideType();
                                AppStore.CLEANING_PROPERTY_NAME = response.body().get(i).getPropertyName();
                                AppStore.CLEANING_PROPERTY_ADDRESS = response.body().get(i).getPropertyAddress();
                                AppStore.CLEANING_IMAGE = response.body().get(i).getImage();
                                AppStore.CLEANING_NOTES = response.body().get(i).getNotes();
                                AppStore.CLEANING_PHOTO_PROOF = "" + response.body().get(i).getRequireGPSProof();
                            }
                        }
                        if(goTo.equals(context.getResources().getString(R.string.checkin_guide))) {
                            Intent intent = new Intent(context, CreateCheckInGuideActivity.class);
                            context.startActivity(intent);
                        }
                        else {
                            if(!isUserGuide) {
                                Intent intent = new Intent(context, CreateCleaningGuideActivity.class);
                                context.startActivity(intent);
                            }
                            else
                            {
                                Intent intent=new Intent(context,UserCleaningGuideActivity.class);
                                context.startActivity(intent);
                            }
                        }
                    }
                }
                else {
                    DialogManager.showNetworkInfo(context, "", response.message(), "");
                }
            }

            @Override
            public void onFailure(Call<List<GuidesResponseModel>> call, Throwable t) {

                Log.d("TAG", "Error : " + t.toString());
                progressBar.setVisibility(View.GONE);
                DialogManager.showNetworkInfo(context,"",t.toString(),"");
            }
        });
    }

    public void createGuide(final Context context, final ProgressBar progressBar, CreateGuideRequest createGuideRequest) {

        GuidesInterface guidesInterface = ApiClient.getClient().create(GuidesInterface.class);
        Call<CreateGuideResponse> call = guidesInterface.createGuide(createGuideRequest);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<CreateGuideResponse>() {
            @Override
            public void onResponse(Call<CreateGuideResponse> call, Response<CreateGuideResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.d("TAG", "Response Code : " + response.body());
                    DialogManager.showNetworkInfo(context,"",response.body().getMessage(),"createGuide");
                }
                else
                {
                    DialogManager.showNetworkInfo(context,"","Response Code : "+response.code()+"\n"+"Message : "+response.message(),"createGuide");
                }
            }

            @Override
            public void onFailure(Call<CreateGuideResponse> call, Throwable t) {

                Log.d("TAG", "Error : " + t.toString());
                progressBar.setVisibility(View.GONE);
                DialogManager.showNetworkInfo(context,"",t.toString(),"createGuide");
            }
        });
    }


    public void updateGuide(final Context context, final ProgressBar progressBar, String url, UpdateGuideRquestModel updateGuideRquestModel) {

        GuidesInterface guidesInterface = ApiClient.getClient().create(GuidesInterface.class);
        Call<UpdateGuideResponseModel> call = guidesInterface.updateGuide(url,updateGuideRquestModel);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<UpdateGuideResponseModel>() {
            @Override
            public void onResponse(Call<UpdateGuideResponseModel> call, Response<UpdateGuideResponseModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.d("TAG", "Response Code : " + response.body());
                    DialogManager.showNetworkInfo(context,"",response.body().getMessage(),"createGuide");
                }
                else
                {
                    DialogManager.showNetworkInfo(context,"","Response Code : "+response.code()+"\n"+"Message : "+response.message(),"createGuide");
                }
            }

            @Override
            public void onFailure(Call<UpdateGuideResponseModel> call, Throwable t) {

                Log.d("TAG", "Error : " + t.toString());
                progressBar.setVisibility(View.GONE);
                DialogManager.showNetworkInfo(context,"",t.toString(),"createGuide");
            }
        });
    }



    public void findProperty(final Context context, final ProgressBar progressBar, String url, final boolean isCheckin, final LinearLayout llPicture, final LinearLayout llNotes, final ImageView ivPicture, final LinearLayout viewFlipper) {

        GuidesInterface guidesInterface = ApiClient.getClient().create(GuidesInterface.class);
        Call<List<GuidesResponseModel>> call = guidesInterface.getGuideByPropertyName(url);
        progressBar.setVisibility(View.VISIBLE);
        sharedPreferencesHelper=new SharedPreferencesHelper(context);
        call.enqueue(new Callback<List<GuidesResponseModel>>() {
            @Override
            public void onResponse(Call<List<GuidesResponseModel>> call, Response<List<GuidesResponseModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        if(isCheckin) {
                            if (response.body().get(i).getGuideType().equals("checkin")) {
                                llNotes.setVisibility(View.VISIBLE);
                                llPicture.setVisibility(View.VISIBLE);
                                splitNotes(response.body().get(i).getNotes(),viewFlipper,context);
                                ivPicture.setImageBitmap(ImageUtils.base64ToBitmap(response.body().get(i).getImage()));
                            }
                        }
                        else{
                            if (response.body().get(i).getGuideType().equals("cleaning")) {
                                llNotes.setVisibility(View.VISIBLE);
                                llPicture.setVisibility(View.VISIBLE);
                                splitNotes(response.body().get(i).getNotes(),viewFlipper,context);
                                ivPicture.setImageBitmap(ImageUtils.base64ToBitmap(response.body().get(i).getImage()));
                            }
                        }
                     }
                }
                else {
                    DialogManager.showNetworkInfo(context, "", response.message(), "");
                }
            }

            @Override
            public void onFailure(Call<List<GuidesResponseModel>> call, Throwable t) {

                Log.d("TAG", "Error : " + t.toString());
                progressBar.setVisibility(View.GONE);
                DialogManager.showNetworkInfo(context,"",t.toString(),"");
            }
        });
    }


    private void splitNotes(String notes,LinearLayout viewFlipperLayout,Context context)
    {
        int totalSteps=0;
        ArrayList<String> stepNotes=new ArrayList<>();
        String[] notesSteps = notes.split("~");
        for(int i=0;i<notesSteps.length;i++)
        {
            if(!notesSteps[i].toString().trim().equals(""))
            {
                stepNotes.add(notesSteps[i].toString().trim());
                totalSteps++;
            }
        }
        final ViewFlipper viewFlipper = new ViewFlipper(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewFlipper.setLayoutParams(layoutParams);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.setAutoStart(false);

        viewFlipper.setInAnimation(context, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(context, android.R.anim.slide_out_right);

        for(int i=0;i<notesSteps.length;i++) {
            TextView textView = new TextView(context);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            textView.setLayoutParams(params);
            textView.setTextSize(20f);
            textView.setText(""+i+") "+ notesSteps[i]);
            viewFlipper.addView(textView);

        }
        viewFlipperLayout.addView(viewFlipper);
        viewFlipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showNext();
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

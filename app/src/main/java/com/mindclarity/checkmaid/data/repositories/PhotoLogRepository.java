package com.mindclarity.checkmaid.data.repositories;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.data.remote.ApiClient;
import com.mindclarity.checkmaid.data.remote.interfaces.PhotoLogsInterface;
import com.mindclarity.checkmaid.data.remote.models.photo_logs.create.CreatePhotoLogRequest;
import com.mindclarity.checkmaid.data.remote.models.photo_logs.create.CreatePhotoLogResponse;
import com.mindclarity.checkmaid.data.remote.models.photo_logs.get.GetPhotoLogsModel;
import com.mindclarity.checkmaid.ui.activities.PhotoLogsGalleryActivity;
import com.mindclarity.checkmaid.utils.AppStore;
import com.mindclarity.checkmaid.utils.DialogManager;
import com.mindclarity.checkmaid.utils.ImageUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoLogRepository {
    
    public PhotoLogRepository(Application application) {
   
    }

    public void createPhotoLog(final Context context, final ProgressBar progressBar, CreatePhotoLogRequest createPhotoLogRequest) {

        PhotoLogsInterface photoLogsInterface = ApiClient.getClient().create(PhotoLogsInterface.class);
        Call<CreatePhotoLogResponse> call = photoLogsInterface.createPhotoLog(createPhotoLogRequest);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<CreatePhotoLogResponse>() {
            @Override
            public void onResponse(Call<CreatePhotoLogResponse> call, Response<CreatePhotoLogResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    DialogManager.showNetworkInfo(context, "", response.body().getMessage(),"createPhotoLog");
                }
                else {
                    DialogManager.showNetworkInfo(context, "", response.message(), "createPhotoLog");
                }
            }

            @Override
            public void onFailure(Call<CreatePhotoLogResponse> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                DialogManager.showNetworkInfo(context, "", t.toString(), "createPhotoLog");
            }
        });
    }

    public void getPhotoLogs(final Context context, final ProgressBar progressBar, String url) {

        final SharedPreferencesHelper sharedPreferencesHelper=new SharedPreferencesHelper(context);
        PhotoLogsInterface photoLogsInterface= ApiClient.getClient().create(PhotoLogsInterface.class);
        Call<List<GetPhotoLogsModel>> call = photoLogsInterface.getPhotoLogs(url);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<List<GetPhotoLogsModel>>() {
            @Override
            public void onResponse(Call<List<GetPhotoLogsModel>> call, Response<List<GetPhotoLogsModel>> response) {
                progressBar.setVisibility(View.GONE);
                ImageUtils.deleteCheckingImage(context,context.getResources().getString(R.string.photo_logs));
                if (response.isSuccessful()) {

                    if(response.body().size()==0)
                    {
                        DialogManager.showNetworkInfo(context, "", context.getResources().getString(R.string.no_data_found), "");
                    }
                    else {

                        for (int i = 0; i < response.body().size(); i++) {

                            if (response.body().get(i).getPropertyName().trim().equals(AppStore.PHOTO_LOGS_PROPERTY_NAME)) {
                                Bitmap bitmap=ImageUtils.base64ToBitmap(response.body().get(i).getImage());
                                ImageUtils.createTempLogsCache(context,bitmap,context.getResources().getString(R.string.photo_logs),""+i);
                            }
                        }
                        sharedPreferencesHelper.setString(App.INTENT_FROM,context.getResources().getString(R.string.photo_logs));
                        Intent i = new Intent(context, PhotoLogsGalleryActivity.class);
                        i.putExtra(context.getResources().getString(R.string.title), context.getResources().getString(R.string.photologs));
                        context.startActivity(i);
                    }
                }
                else
                {
                    if(response.message().trim().equals("Not Found"))
                    {
                        DialogManager.showNetworkInfo(context, "", context.getResources().getString(R.string.no_data_found), "");
                    }
                    else {
                        DialogManager.showNetworkInfo(context, "", response.message(), "");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GetPhotoLogsModel>> call, Throwable t) {

                Log.d("TAG", "Error : " + t.toString());
                progressBar.setVisibility(View.GONE);
                DialogManager.showNetworkInfo(context,"",t.toString(),"");
            }
        });
    }
}

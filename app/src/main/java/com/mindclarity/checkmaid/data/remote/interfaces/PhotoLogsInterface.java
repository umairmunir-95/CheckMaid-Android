package com.mindclarity.checkmaid.data.remote.interfaces;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.data.remote.models.photo_logs.create.CreatePhotoLogRequest;
import com.mindclarity.checkmaid.data.remote.models.photo_logs.create.CreatePhotoLogResponse;
import com.mindclarity.checkmaid.data.remote.models.photo_logs.get.GetPhotoLogsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface PhotoLogsInterface {

    @POST(App.LIST_ALL_PHOTO_LOGS)
    Call<CreatePhotoLogResponse> createPhotoLog(@Body CreatePhotoLogRequest createPhotoLogRequest);

    @GET
    Call<List<GetPhotoLogsModel>> getPhotoLogs(@Url String url);

}

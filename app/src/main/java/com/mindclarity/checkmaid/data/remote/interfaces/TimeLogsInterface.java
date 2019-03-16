package com.mindclarity.checkmaid.data.remote.interfaces;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.data.remote.models.clockin_clockout.create.ClockInClockOutRequest;
import com.mindclarity.checkmaid.data.remote.models.clockin_clockout.create.ClockInClockOutResponse;
import com.mindclarity.checkmaid.data.remote.models.clockin_clockout.get_by_user.GetTimeLogdByUserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface TimeLogsInterface {

    @POST(App.LIST_ALL_TIME_LOGS)
    Call<ClockInClockOutResponse> createTimeLog(@Body ClockInClockOutRequest clockInClockOutRequest);

    @GET
    Call<List<GetTimeLogdByUserModel>> getLogsByUser(@Url String url);

}

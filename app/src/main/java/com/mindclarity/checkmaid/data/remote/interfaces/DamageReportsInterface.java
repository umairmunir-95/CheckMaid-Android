package com.mindclarity.checkmaid.data.remote.interfaces;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.data.remote.models.damage_report.create_damage_report.CreateDamageReportRequest;
import com.mindclarity.checkmaid.data.remote.models.damage_report.create_damage_report.CreateDamageReportResponse;
import com.mindclarity.checkmaid.data.remote.models.damage_report.get_damage_reports.GetDamageReportsResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface DamageReportsInterface {

    @POST(App.LIST_ALL_DAMAGE_REPORTS)
    Call<CreateDamageReportResponse> createDamageReport(@Body CreateDamageReportRequest createDamageReportRequest);

    @GET
    Call<List<GetDamageReportsResponseModel>> getDamageReports(@Url String url);

}

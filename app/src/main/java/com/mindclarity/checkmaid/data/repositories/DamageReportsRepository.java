package com.mindclarity.checkmaid.data.repositories;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.data.remote.ApiClient;
import com.mindclarity.checkmaid.data.remote.interfaces.DamageReportsInterface;
import com.mindclarity.checkmaid.data.remote.models.damage_report.create_damage_report.CreateDamageReportRequest;
import com.mindclarity.checkmaid.data.remote.models.damage_report.create_damage_report.CreateDamageReportResponse;
import com.mindclarity.checkmaid.data.remote.models.damage_report.get_damage_reports.GetDamageReportsResponseModel;
import com.mindclarity.checkmaid.ui.activities.PhotoLogsGalleryActivity;
import com.mindclarity.checkmaid.utils.AppStore;
import com.mindclarity.checkmaid.utils.DialogManager;
import com.mindclarity.checkmaid.utils.ImageUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DamageReportsRepository {

    public DamageReportsRepository(Application application) {
    }

    public void createDamageReport(final Context context, final ProgressBar progressBar, final EditText editText, final ImageButton imageButton, final ImageView imageView, CreateDamageReportRequest createDamageReportRequest) {

        DamageReportsInterface damageReportsInterface= ApiClient.getClient().create(DamageReportsInterface.class);
        Call<CreateDamageReportResponse> call = damageReportsInterface.createDamageReport(createDamageReportRequest);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<CreateDamageReportResponse>() {
            @Override
            public void onResponse(Call<CreateDamageReportResponse> call, Response<CreateDamageReportResponse> response) {
                progressBar.setVisibility(View.GONE);
                imageButton.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                editText.setText("");
                if (response.isSuccessful()) {
                    DialogManager.showNetworkInfo(context, "", context.getResources().getString(R.string.damage_report_creation_success), "");
                }
                else {
                    DialogManager.showNetworkInfo(context, "", response.message(), "");
                }
            }

            @Override
            public void onFailure(Call<CreateDamageReportResponse> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                DialogManager.showNetworkInfo(context, "", t.toString(), "clockIn");
            }
        });
    }


    public void getDamageReports(final Context context, final ProgressBar progressBar, String url) {
        final SharedPreferencesHelper sharedPreferencesHelper=new SharedPreferencesHelper(context);
        DamageReportsInterface damageReportsInterface= ApiClient.getClient().create(DamageReportsInterface.class);
        Call<List<GetDamageReportsResponseModel>> call = damageReportsInterface.getDamageReports(url);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<List<GetDamageReportsResponseModel>>() {
            @Override
            public void onResponse(Call<List<GetDamageReportsResponseModel>> call, Response<List<GetDamageReportsResponseModel>> response) {
                progressBar.setVisibility(View.GONE);
                ImageUtils.deleteCheckingImage(context,context.getResources().getString(R.string.damage_reports));
                if (response.isSuccessful()) {

                    if(response.body().size()==0)
                    {
                        DialogManager.showNetworkInfo(context, "", context.getResources().getString(R.string.no_data_found), "");
                    }
                    else {

                        for (int i = 0; i < response.body().size(); i++) {

                            if (response.body().get(i).getPropertyName().trim().equals(AppStore.DAMAGE_REPORTS_PROPERTY_NAME)) {
                                Bitmap bitmap=ImageUtils.base64ToBitmap(response.body().get(i).getImage());
                                ImageUtils.createTempLogsCache(context,bitmap,context.getResources().getString(R.string.damage_reports),""+i);
                            }
                        }
                        sharedPreferencesHelper.setString(App.INTENT_FROM,context.getResources().getString(R.string.damage_reports));
                        Intent i = new Intent(context, PhotoLogsGalleryActivity.class);
                        i.putExtra(context.getResources().getString(R.string.title), context.getResources().getString(R.string.damage_reports));
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
            public void onFailure(Call<List<GetDamageReportsResponseModel>> call, Throwable t) {

                Log.d("TAG", "Error : " + t.toString());
                progressBar.setVisibility(View.GONE);
                DialogManager.showNetworkInfo(context,"",t.toString(),"");
            }
        });
    }
}

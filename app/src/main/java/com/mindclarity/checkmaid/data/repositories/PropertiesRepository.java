package com.mindclarity.checkmaid.data.repositories;

import android.app.Application;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.common_models.Properties;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.data.remote.ApiClient;
import com.mindclarity.checkmaid.data.remote.interfaces.PropertiesInterface;
import com.mindclarity.checkmaid.data.remote.models.properties.add_property.request.AddPropertyRequest;
import com.mindclarity.checkmaid.data.remote.models.properties.add_property.response.AddPropertyResponse;
import com.mindclarity.checkmaid.data.remote.models.properties.get_properties.GetPropertiesResponseModel;
import com.mindclarity.checkmaid.ui.adapters.GridViewPropertiesAdapter;
import com.mindclarity.checkmaid.ui.adapters.RecyclerViewAssignPropertiesAdapter;
import com.mindclarity.checkmaid.utils.AppStore;
import com.mindclarity.checkmaid.utils.DialogManager;
import com.mindclarity.checkmaid.utils.ImageUtils;
import com.mindclarity.checkmaid.utils.LocationManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertiesRepository {

    public PropertiesRepository(Application application) {
    }

    public void addProperty(final Context context, final ProgressBar progressBar, AddPropertyRequest addPropertyRequest) {

        PropertiesInterface propertiesInterface = ApiClient.getClient().create(PropertiesInterface.class);
        Call<AddPropertyResponse> call = propertiesInterface.addProperty(addPropertyRequest);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<AddPropertyResponse>() {
            @Override
            public void onResponse(Call<AddPropertyResponse> call, Response<AddPropertyResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    DialogManager.showNetworkInfo(context,"",response.body().getMessage(),"addProperty");
                }
                else
                {
                    DialogManager.showNetworkInfo(context,"",response.body().getMessage(),"addProperty");
                }
            }

            @Override
            public void onFailure(Call<AddPropertyResponse> call, Throwable t) {

                Log.d("TAG", "Error : " + t.toString());
                progressBar.setVisibility(View.GONE);
                DialogManager.showNetworkInfo(context,"",t.toString(),"addProperty");
            }
        });
    }

    public void editProperty(final Context context, final ProgressBar progressBar,String url, AddPropertyRequest addPropertyRequest) {

        PropertiesInterface propertiesInterface = ApiClient.getClient().create(PropertiesInterface.class);
        Call<AddPropertyResponse> call = propertiesInterface.editProperty(url,addPropertyRequest);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<AddPropertyResponse>() {
            @Override
            public void onResponse(Call<AddPropertyResponse> call, Response<AddPropertyResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.d("TAG", "Response Code : " + response.body());
                    DialogManager.showNetworkInfo(context,"",response.body().getMessage(),"addProperty");
                }
                else
                {
                    DialogManager.showNetworkInfo(context,"","Response Code : "+response.code()+" : \n"+"Message : "+response.message(),"addProperty");
                }
            }

            @Override
            public void onFailure(Call<AddPropertyResponse> call, Throwable t) {

                Log.d("TAG", "Error : " + t.toString());
                progressBar.setVisibility(View.GONE);
                DialogManager.showNetworkInfo(context,"",t.toString(),"addProperty");
            }
        });
    }

    public void getAllProperties(final Context context, final ProgressBar progressBar, final RecyclerView recyclerView, String url, final boolean isAssignProperties) {

        PropertiesInterface propertiesInterface = ApiClient.getClient().create(PropertiesInterface.class);
        Call<List<GetPropertiesResponseModel>> call = propertiesInterface.properties(url);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<List<GetPropertiesResponseModel>>() {
            @Override
            public void onResponse(Call<List<GetPropertiesResponseModel>> call, Response<List<GetPropertiesResponseModel>> response) {
                progressBar.setVisibility(View.GONE);
                String allProperties="";
                SharedPreferencesHelper sharedPreferencesHelper=new SharedPreferencesHelper(context);
                sharedPreferencesHelper.setString(context.getResources().getString(R.string.all_properties),allProperties);

                GridViewPropertiesAdapter adapter = new GridViewPropertiesAdapter(context);
                RecyclerViewAssignPropertiesAdapter recyclerViewAssignPropertiesAdapter = new RecyclerViewAssignPropertiesAdapter(context);

                ArrayList<Properties> properties=new ArrayList<>();
                ImageUtils.deleteAllImages(context);
                if (response.isSuccessful()) {
                    for(int i=0;i<response.body().size();i++)
                    {
                        Properties property=new Properties();
                        property.setPropertyName(response.body().get(i).getPropertyName());
                        property.setPropertyAddress(response.body().get(i).getAddress());
                        property.setImage(response.body().get(i).getImageBase64());
                        property.setId(""+response.body().get(i).getId());
                        property.setLatitude(""+response.body().get(i).getLatitude());
                        property.setLongitude(""+response.body().get(i).getLongitude());
                        property.setCheckInTime(""+response.body().get(i).getCheckInTime());
                        property.setCheckOutTime(""+response.body().get(i).getCheckOutTime());
                        property.setClockInRadius(""+response.body().get(i).getCheckInRadius());
                        properties.add(property);
                        allProperties=allProperties+response.body().get(i).getPropertyName().trim()+",";
                    }
                    sharedPreferencesHelper.setString(context.getResources().getString(R.string.all_properties),allProperties);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    if(isAssignProperties)
                    {
                        recyclerViewAssignPropertiesAdapter.setProperties(properties);
                        recyclerView.setAdapter(recyclerViewAssignPropertiesAdapter);
                    }
                    else {
                        adapter.setProperties(properties);
                        recyclerView.setAdapter(adapter);
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
            public void onFailure(Call<List<GetPropertiesResponseModel>> call, Throwable t) {

                Log.d("TAG", "Error : " + t.toString());
                progressBar.setVisibility(View.GONE);
                DialogManager.showNetworkInfo(context,"",t.toString(),"");
            }
        });
    }

    public void getPropertyByName(final Context context, final ProgressBar progressBar, final Button button, final EditText editText, String url) {

        PropertiesInterface propertiesInterface = ApiClient.getClient().create(PropertiesInterface.class);
        Call<List<GetPropertiesResponseModel>> call = propertiesInterface.properties(url);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<List<GetPropertiesResponseModel>>() {
            @Override
            public void onResponse(Call<List<GetPropertiesResponseModel>> call, Response<List<GetPropertiesResponseModel>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    for(int i=0;i<response.body().size();i++)
                    {
                        AppStore.CLOCK_IN_OUT_PROPERTY_ID= response.body().get(i).getId().toString();
                        AppStore.PROPERTY_NAME=response.body().get(i).getPropertyName().trim();
                        AppStore.PROPERTY_LATITUDE=response.body().get(i).getLatitude().toString();
                        AppStore.PROPERTY_LONGITUDE=response.body().get(i).getLongitude().toString();
                        AppStore.PROPERTY_CLOCK_IN_RADIUS=response.body().get(i).getCheckInRadius().toString();
                    }
                    Double radius=Double.valueOf(AppStore.PROPERTY_CLOCK_IN_RADIUS);
                    Double propertyLat=Double.valueOf(AppStore.PROPERTY_LATITUDE);
                    Double propertyLong=Double.valueOf(AppStore.PROPERTY_LONGITUDE);
                    Double currentLat=Double.valueOf(AppStore.CURRENT_LATITUDE);
                    Double currentLong=Double.valueOf(AppStore.CURRENT_LONGITUDE);
                    boolean isValid=LocationManager.validateGeoLocation(radius,currentLat,currentLong,propertyLat,propertyLong);
                    if(isValid) {
                        button.setAlpha(1.0f);
                        button.setEnabled(true);
                    }
                    else
                    {
                        editText.setText("");
                        DialogManager.showInfoDialog(context,"",context.getResources().getString(R.string.clock_in_error),false);
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
            public void onFailure(Call<List<GetPropertiesResponseModel>> call, Throwable t) {

                Log.d("TAG", "Error : " + t.toString());
                progressBar.setVisibility(View.GONE);
                DialogManager.showNetworkInfo(context,"",t.toString(),"");
            }
        });
    }
}

package com.mindclarity.checkmaid.data.repositories;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.data.remote.ApiClient;
import com.mindclarity.checkmaid.data.remote.interfaces.LoginInterface;
import com.mindclarity.checkmaid.data.remote.models.login.LoginResponse;
import com.mindclarity.checkmaid.ui.activities.FindPropertyActivity;
import com.mindclarity.checkmaid.ui.activities.LoginActivity;
import com.mindclarity.checkmaid.ui.activities.PropertiesActivity;
import com.mindclarity.checkmaid.utils.DialogManager;
import com.mindclarity.checkmaid.utils.Helpers;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {

    private SharedPreferencesHelper sharedPreferencesHelper;

    public LoginRepository(Application application) {
    }

    public void login(final Context context, final ProgressBar progressBar,String url) {

        LoginInterface loginInterface = ApiClient.getClient().create(LoginInterface.class);
        Call<List<LoginResponse>> call = loginInterface.login(url);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<List<LoginResponse>>() {
            @Override
            public void onResponse(Call<List<LoginResponse>> call, Response<List<LoginResponse>> response) {
                progressBar.setVisibility(View.GONE);
                sharedPreferencesHelper=new SharedPreferencesHelper(context);
                if (response.isSuccessful()) {
                    if(response.body().size()==0)
                    {
                        DialogManager.showNetworkInfo(context, "", context.getResources().getString(R.string.incorrect_credentials), "");
                    }
                    else
                    {
                        for(int i=0;i<response.body().size();i++)
                        {
                            sharedPreferencesHelper.setString(context.getResources().getString(R.string.user_id),""+response.body().get(i).getId());
                            sharedPreferencesHelper.setString(context.getResources().getString(R.string.username),""+response.body().get(i).getUsername());
                            sharedPreferencesHelper.setString(context.getResources().getString(R.string.email),""+response.body().get(i).getEmail());
                            sharedPreferencesHelper.setString(context.getResources().getString(R.string.user_type),""+response.body().get(i).getUserType());
                            sharedPreferencesHelper.setString(context.getResources().getString(R.string.assigned_properties),response.body().get(i).getAssignedProperties()==null?"":response.body().get(i).getAssignedProperties());
                        }
                        if(Helpers.getPreferenceValue(context,context.getResources().getString(R.string.user_type)).equals(context.getResources().getString(R.string.admin).toLowerCase()))
                        {
                            Intent i = new Intent(context, PropertiesActivity.class);
                            context.startActivity(i);
                        }
                        else
                        {
                            Intent i = new Intent(context, FindPropertyActivity.class);
                            context.startActivity(i);
                        }

                    }
                  }
                else {
                    DialogManager.showNetworkInfo(context, "", response.message(), "");
                }
            }

            @Override
            public void onFailure(Call<List<LoginResponse>> call, Throwable t) {

                Log.d("TAG", "Error : " + t.toString());
                progressBar.setVisibility(View.GONE);
                DialogManager.showNetworkInfo(context,"",t.toString(),"");
            }
        });
    }
}

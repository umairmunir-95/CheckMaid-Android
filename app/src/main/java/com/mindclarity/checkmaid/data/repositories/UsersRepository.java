package com.mindclarity.checkmaid.data.repositories;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.common_models.Users;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.data.remote.ApiClient;
import com.mindclarity.checkmaid.data.remote.interfaces.UsersInterface;
import com.mindclarity.checkmaid.data.remote.models.users.AssignPropertiesRequestModel;
import com.mindclarity.checkmaid.data.remote.models.users.AssignPropertiesResponseModel;
import com.mindclarity.checkmaid.data.remote.models.users.DeleteAccountModel;
import com.mindclarity.checkmaid.data.remote.models.users.UsersResponseModel;
import com.mindclarity.checkmaid.ui.adapters.ListViewSearchUsersAdapter;
import com.mindclarity.checkmaid.utils.DialogManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersRepository {
    
    public UsersRepository(Application application) {
    
    }

    public void listAllUsers(final Context context, final ProgressBar progressBar, final ListView listView,String url) {

        UsersInterface usersInterface = ApiClient.getClient().create(UsersInterface.class);
        Call<List<UsersResponseModel>> call = usersInterface.listAllUsers(url);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<List<UsersResponseModel>>() {
            @Override
            public void onResponse(Call<List<UsersResponseModel>> call, Response<List<UsersResponseModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if(response.body().size()==0)
                    {
                        DialogManager.showNetworkInfo(context, "", context.getResources().getString(R.string.no_user_found), "");
                    }
                    else
                    {
                        ArrayList<Users> users=new ArrayList<>();
                        for(int i=0;i<response.body().size();i++)
                        {
                            Users users1=new Users();
                            users1.setUserName(response.body().get(i).getUsername());
                            users1.setUserId(""+response.body().get(i).getId());
                            if(response.body().get(i).getUserType().equals("cleaner")) {
                                users.add(users1);
                            }
                        }
                        ListViewSearchUsersAdapter adapter=new ListViewSearchUsersAdapter(context,users);
                        listView.setAdapter(adapter);
                    }
                }
                else {
                    DialogManager.showNetworkInfo(context, "", response.message(), "");
                }
            }

            @Override
            public void onFailure(Call<List<UsersResponseModel>> call, Throwable t) {

                Log.d("TAG", "Error : " + t.toString());
                progressBar.setVisibility(View.GONE);
                DialogManager.showNetworkInfo(context,"",t.toString(),"");
            }
        });
    }


    public void assignProperties(final Context context, final ProgressBar progressBar, String url, AssignPropertiesRequestModel assignPropertiesRequestModel) {

        UsersInterface usersInterface = ApiClient.getClient().create(UsersInterface.class);
        Call<AssignPropertiesResponseModel> call = usersInterface.assignProperties(url,assignPropertiesRequestModel);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<AssignPropertiesResponseModel>() {
            @Override
            public void onResponse(Call<AssignPropertiesResponseModel> call, Response<AssignPropertiesResponseModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.d("TAG", "Response Code : " + response.body());
                    DialogManager.showNetworkInfo(context,"",response.body().getMessage(),"");
                }
                else
                {
                    DialogManager.showNetworkInfo(context,"",response.body().getMessage(),"");
                }
            }

            @Override
            public void onFailure(Call<AssignPropertiesResponseModel> call, Throwable t) {

                Log.d("TAG", "Error : " + t.toString());
                progressBar.setVisibility(View.GONE);
                DialogManager.showNetworkInfo(context,"",t.toString(),"");
            }
        });
    }

    public void deleteAccount(final Context context, final ProgressBar progressBar, String url) {

        UsersInterface usersInterface = ApiClient.getClient().create(UsersInterface.class);
        Call<DeleteAccountModel> call = usersInterface.deleteAccount(url);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<DeleteAccountModel>() {
            @Override
            public void onResponse(Call<DeleteAccountModel> call, Response<DeleteAccountModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    SharedPreferencesHelper sharedPreferencesHelper=new SharedPreferencesHelper(context);
                    sharedPreferencesHelper.clearPreferenceStore();
                    DialogManager.showNetworkInfo(context,"",response.body().getMessage(),"deleteAccount");
                }
                else
                {
                    DialogManager.showNetworkInfo(context,"",response.message(),"deleteAccount");
                }
            }

            @Override
            public void onFailure(Call<DeleteAccountModel> call, Throwable t) {

                Log.d("TAG", "Error : " + t.toString());
                progressBar.setVisibility(View.GONE);
                DialogManager.showNetworkInfo(context,"",t.toString(),"deleteAccount");
            }
        });
    }

}

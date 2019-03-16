package com.mindclarity.checkmaid.data.repositories;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.mindclarity.checkmaid.R;
import com.mindclarity.checkmaid.data.local.prefferences.SharedPreferencesHelper;
import com.mindclarity.checkmaid.data.remote.ApiClient;
import com.mindclarity.checkmaid.data.remote.interfaces.UserRegistrationInterface;
import com.mindclarity.checkmaid.data.remote.models.user_registration.request.UserRegistrationRequestModel;
import com.mindclarity.checkmaid.data.remote.models.user_registration.response.UserRegistrationResponseModel;
import com.mindclarity.checkmaid.utils.DialogManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistrationRepository {

    SharedPreferencesHelper sharedPreferencesHelper;

    public UserRegistrationRepository(Application application) {
    }

    public void registerUser(final Context context, final ProgressBar progressBar, String userName, String email, String password, String userType, final boolean addingCleaner) {
        UserRegistrationInterface userRegistrationInterface = ApiClient.getClient().create(UserRegistrationInterface.class);
        Call<UserRegistrationResponseModel> call = userRegistrationInterface.register(userRegistrationModel(userName,email,password,userType));
        progressBar.setVisibility(View.VISIBLE);
        sharedPreferencesHelper=new SharedPreferencesHelper(context);
        call.enqueue(new Callback<UserRegistrationResponseModel>() {
            @Override
            public void onResponse(Call<UserRegistrationResponseModel> call, Response<UserRegistrationResponseModel> response) {
                progressBar.setVisibility(View.GONE);
                if(addingCleaner)
                {
                    sharedPreferencesHelper.setString(context.getResources().getString(R.string.user_type), "admin");
                }
                if (response.isSuccessful()) {
                    Log.d("TAG", "Response Code : " + response.body());
                    if(addingCleaner)
                    {
                        DialogManager.showNetworkInfo(context,"","User added successfully.","addCleaner");
                    }
                    else
                    {
                        DialogManager.showNetworkInfo(context,"",response.body().getMessage(),"userRegistration");
                    }
                 }
                else {
                    if (addingCleaner) {
                        DialogManager.showNetworkInfo(context, "", response.message(), "addCleaner");
                    }
                    else
                    {
                        DialogManager.showNetworkInfo(context,"",response.message(),"userRegistration");
                    }
                }
            }

            @Override
            public void onFailure(Call<UserRegistrationResponseModel> call, Throwable t) {

                Log.d("TAG", "Error : " + t.toString());
                progressBar.setVisibility(View.GONE);
                if (addingCleaner) {
                    sharedPreferencesHelper.setString(context.getResources().getString(R.string.user_type), "admin");
                    DialogManager.showNetworkInfo(context, "", t.toString(), "addCleaner");
                } else {
                    DialogManager.showNetworkInfo(context, "", t.toString(), "userRegistration");
                }
            }
        });
    }

    private UserRegistrationRequestModel userRegistrationModel(String userName, String email, String password, String userType)
    {
        UserRegistrationRequestModel userRegistrationRequestModel =new UserRegistrationRequestModel();
        userRegistrationRequestModel.setUsername(userName);
        userRegistrationRequestModel.setEmail(email);
        userRegistrationRequestModel.setPassword(password);
        userRegistrationRequestModel.setUserType(userType);
        return userRegistrationRequestModel;
    }
}

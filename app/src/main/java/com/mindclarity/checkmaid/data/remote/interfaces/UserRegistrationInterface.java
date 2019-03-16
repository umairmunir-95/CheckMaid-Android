package com.mindclarity.checkmaid.data.remote.interfaces;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.data.remote.models.user_registration.request.UserRegistrationRequestModel;
import com.mindclarity.checkmaid.data.remote.models.user_registration.response.UserRegistrationResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserRegistrationInterface {

    @POST(App.REGISTER_USER)
    Call<UserRegistrationResponseModel> register(@Body UserRegistrationRequestModel userRegistrationRequestModel);
}

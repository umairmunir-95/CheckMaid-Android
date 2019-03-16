package com.mindclarity.checkmaid.data.remote.interfaces;

import com.mindclarity.checkmaid.data.remote.models.login.LoginResponse;
import com.mindclarity.checkmaid.data.remote.models.users.AssignPropertiesRequestModel;
import com.mindclarity.checkmaid.data.remote.models.users.AssignPropertiesResponseModel;
import com.mindclarity.checkmaid.data.remote.models.users.DeleteAccountModel;
import com.mindclarity.checkmaid.data.remote.models.users.UsersResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface UsersInterface {

    @GET
    Call<List<UsersResponseModel>> listAllUsers(@Url String url);

    @PUT
    Call<AssignPropertiesResponseModel> assignProperties(@Url String url, @Body AssignPropertiesRequestModel assignPropertiesRequestModel);

    @DELETE
    Call<DeleteAccountModel> deleteAccount(@Url String url);
}

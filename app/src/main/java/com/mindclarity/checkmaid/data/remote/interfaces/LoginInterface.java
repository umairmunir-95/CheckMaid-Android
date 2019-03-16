package com.mindclarity.checkmaid.data.remote.interfaces;

import com.mindclarity.checkmaid.data.remote.models.login.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface LoginInterface {

    @GET
    Call<List<LoginResponse>> login(@Url String url);
}

package com.mindclarity.checkmaid.data.remote.interfaces;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.data.remote.models.properties.add_property.request.AddPropertyRequest;
import com.mindclarity.checkmaid.data.remote.models.properties.add_property.response.AddPropertyResponse;
import com.mindclarity.checkmaid.data.remote.models.properties.get_properties.GetPropertiesResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface PropertiesInterface {

    @POST(App.ADD_PROPERTY)
    Call<AddPropertyResponse> addProperty(@Body AddPropertyRequest addPropertyRequest);

    @PUT
    Call<AddPropertyResponse> editProperty(@Url String url,@Body AddPropertyRequest addPropertyRequest);

    @GET
    Call<List<GetPropertiesResponseModel>> properties(@Url String url);

    @GET
    Call<List<GetPropertiesResponseModel>> getPropertiesByNae(@Url String url);
}

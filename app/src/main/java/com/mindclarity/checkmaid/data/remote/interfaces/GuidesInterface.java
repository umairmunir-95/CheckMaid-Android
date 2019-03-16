package com.mindclarity.checkmaid.data.remote.interfaces;

import com.mindclarity.checkmaid.App;
import com.mindclarity.checkmaid.data.remote.models.guides.create_guide.CreateGuideRequest;
import com.mindclarity.checkmaid.data.remote.models.guides.create_guide.CreateGuideResponse;
import com.mindclarity.checkmaid.data.remote.models.guides.get_guide_by_property.GuidesResponseModel;
import com.mindclarity.checkmaid.data.remote.models.guides.update_guide.UpdateGuideResponseModel;
import com.mindclarity.checkmaid.data.remote.models.guides.update_guide.UpdateGuideRquestModel;
import com.mindclarity.checkmaid.data.remote.models.user_registration.request.UserRegistrationRequestModel;
import com.mindclarity.checkmaid.data.remote.models.user_registration.response.UserRegistrationResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface GuidesInterface {

    @GET
    Call<List<GuidesResponseModel>> getGuideByPropertyName(@Url String url);

    @POST(App.LIST_ALL_GUIDES)
    Call<CreateGuideResponse> createGuide(@Body CreateGuideRequest createGuideRequest);

    @PUT
    Call<UpdateGuideResponseModel> updateGuide(@Url String url, @Body UpdateGuideRquestModel updateGuideRquestModel);
}


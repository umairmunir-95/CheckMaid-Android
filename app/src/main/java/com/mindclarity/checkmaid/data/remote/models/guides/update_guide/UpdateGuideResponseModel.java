package com.mindclarity.checkmaid.data.remote.models.guides.update_guide;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateGuideResponseModel {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}

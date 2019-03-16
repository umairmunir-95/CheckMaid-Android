package com.mindclarity.checkmaid.data.remote.models.users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssignPropertiesRequestModel {

    @SerializedName("assignedProperties")
    @Expose
    private String assignedProperties;

    public String getAssignedProperties() {
        return assignedProperties;
    }

    public void setAssignedProperties(String assignedProperties) {
        this.assignedProperties = assignedProperties;
    }
}

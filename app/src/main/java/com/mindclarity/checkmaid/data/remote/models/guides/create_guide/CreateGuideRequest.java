package com.mindclarity.checkmaid.data.remote.models.guides.create_guide;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateGuideRequest {

    @SerializedName("propertyName")
    @Expose
    private String propertyName;
    @SerializedName("propertyAddress")
    @Expose
    private String propertyAddress;
    @SerializedName("guideType")
    @Expose
    private String guideType;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("requireGPSProof")
    @Expose
    private Boolean requireGPSProof;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public String getGuideType() {
        return guideType;
    }

    public void setGuideType(String guideType) {
        this.guideType = guideType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getRequireGPSProof() {
        return requireGPSProof;
    }

    public void setRequireGPSProof(Boolean requireGPSProof) {
        this.requireGPSProof = requireGPSProof;
    }

}

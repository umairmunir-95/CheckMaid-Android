package com.mindclarity.checkmaid.data.remote.models.guides.update_guide;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateGuideRquestModel {

    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("requireGPSProof")
    @Expose
    private Boolean requireGPSProof;

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

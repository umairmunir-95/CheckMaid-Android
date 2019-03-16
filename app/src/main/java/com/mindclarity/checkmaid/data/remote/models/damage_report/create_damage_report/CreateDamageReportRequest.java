package com.mindclarity.checkmaid.data.remote.models.damage_report.create_damage_report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateDamageReportRequest {

    @SerializedName("propertyName")
    @Expose
    private String propertyName;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("notes")
    @Expose
    private String notes;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

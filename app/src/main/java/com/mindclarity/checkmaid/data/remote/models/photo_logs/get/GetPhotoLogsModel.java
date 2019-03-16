package com.mindclarity.checkmaid.data.remote.models.photo_logs.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPhotoLogsModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("propertyName")
    @Expose
    private String propertyName;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("date")
    @Expose
    private String date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
}

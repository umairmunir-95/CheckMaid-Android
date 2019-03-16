package com.mindclarity.checkmaid.data.remote.models.properties.get_properties;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPropertiesResponseModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("propertyName")
    @Expose
    private String propertyName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("checkInRadius")
    @Expose
    private Integer checkInRadius;
    @SerializedName("checkInTime")
    @Expose
    private String checkInTime;
    @SerializedName("checkOutTime")
    @Expose
    private String checkOutTime;
    @SerializedName("imageBase64")
    @Expose
    private String imageBase64;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getCheckInRadius() {
        return checkInRadius;
    }

    public void setCheckInRadius(Integer checkInRadius) {
        this.checkInRadius = checkInRadius;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}

package com.mindclarity.checkmaid.data.common_models;

public class TimeSheetDetails {

    String propertyName;
    String date;
    String checkIn;
    String checkOut;

    public TimeSheetDetails(String propertyName, String date, String checkIn, String checkOut) {
        this.propertyName = propertyName;
        this.date = date;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }
}

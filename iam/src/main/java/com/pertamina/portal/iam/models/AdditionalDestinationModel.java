package com.pertamina.portal.iam.models;

public class AdditionalDestinationModel {

    private String city, country, countryID, startDate, endDate, note;

    public AdditionalDestinationModel(String city, String countryID, String country, String startDate, String endDate, String note) {
        this.city = city;
        this.country = country;
        this.countryID = countryID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getNote() {
        return note;
    }

    public String getCountryID() {
        return countryID;
    }
}

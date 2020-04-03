package com.pertamina.portal.iam.models;

public class ReviewerModel {

    private String name, position, adUserName, personelNumber;

    public ReviewerModel(String name, String position, String adUserName, String personelNumber) {
        this.name = name;
        this.position = position;
        this.adUserName = adUserName;
        this.personelNumber = personelNumber;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getAdUserName() {
        return adUserName;
    }

    public String getPersonelNumber() {
        return personelNumber;
    }
}

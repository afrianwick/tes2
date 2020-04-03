package com.pertamina.portal.iam.models;

public class AdditionalParticipantModel {

    private String name, note;

    public AdditionalParticipantModel(String name, String note) {
        this.name = name;
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }
}

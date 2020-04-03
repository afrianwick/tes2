package com.pertamina.portal.iam.models;

import io.realm.RealmObject;

public class Karyawan extends RealmObject {

    public Karyawan() {

    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String fullName;
    public String personalNum;
    public String photoProfile;
    public String email;
    public String kbo;
    public int position;
    public String officeAddress;
    public String phoneNumber;
}

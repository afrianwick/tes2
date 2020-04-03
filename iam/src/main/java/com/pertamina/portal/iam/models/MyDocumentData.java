package com.pertamina.portal.iam.models;

import io.realm.RealmObject;

public class MyDocumentData extends RealmObject {

    public String action;
    public String documentType;
    public String name;
    public int dateOfIssue;
    public String filename;
    public String docName;
    public String docIssuer;
    public String companyCode;
    public String uploadDate;
    public String description;

    public MyDocumentData() {

    }
}

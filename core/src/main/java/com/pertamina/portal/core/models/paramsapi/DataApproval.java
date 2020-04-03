package com.pertamina.portal.core.models.paramsapi;

public class DataApproval {
    private String folioNumber;
    private String k2SerialNumber;
    private String k2Action;
    private String xmlData;
    private String xmlDataSummary;
    private String comment;
    private String K2DataFieldsInJSON;

    public String getFolioNumber() {
        return folioNumber;
    }

    public void setFolioNumber(String folioNumber) {
        this.folioNumber = folioNumber;
    }

    public String getK2SerialNumber() {
        return k2SerialNumber;
    }

    public void setK2SerialNumber(String k2SerialNumber) {
        this.k2SerialNumber = k2SerialNumber;
    }

    public String getK2Action() {
        return k2Action;
    }

    public void setK2Action(String k2Action) {
        this.k2Action = k2Action;
    }

    public String getXmlData() {
        return xmlData;
    }

    public void setXmlData(String xmlData) {
        this.xmlData = xmlData;
    }

    public String getXmlDataSummary() {
        return xmlDataSummary;
    }

    public void setXmlDataSummary(String xmlDataSummary) {
        this.xmlDataSummary = xmlDataSummary;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getK2DataFieldsInJSON() {
        return K2DataFieldsInJSON;
    }

    public void setK2DataFieldsInJSON(String k2DataFieldsInJSON) {
        K2DataFieldsInJSON = k2DataFieldsInJSON;
    }
}

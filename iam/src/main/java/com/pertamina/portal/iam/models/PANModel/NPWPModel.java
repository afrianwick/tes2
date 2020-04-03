package com.pertamina.portal.iam.models.PANModel;

public class NPWPModel {

    private String startDate, endDate, npwpNumber, statusPajak, spouseBenefit;
    private String action;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getNpwpNumber() {
        return npwpNumber;
    }

    public void setNpwpNumber(String npwpNumber) {
        this.npwpNumber = npwpNumber;
    }

    public String getStatusPajak() {
        return statusPajak;
    }

    public void setStatusPajak(String statusPajak) {
        this.statusPajak = statusPajak;
    }

    public String getSpouseBenefit() {
        return spouseBenefit;
    }

    public void setSpouseBenefit(String spouseBenefit) {
        this.spouseBenefit = spouseBenefit;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}

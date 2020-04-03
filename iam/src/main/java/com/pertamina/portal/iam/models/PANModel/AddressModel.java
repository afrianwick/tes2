package com.pertamina.portal.iam.models.PANModel;

public class AddressModel {

    private String startDate, endDate, addressType, contactName, streetHouseNumber;
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

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getStreetHouseNumber() {
        return streetHouseNumber;
    }

    public void setStreetHouseNumber(String streetHouseNumber) {
        this.streetHouseNumber = streetHouseNumber;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}

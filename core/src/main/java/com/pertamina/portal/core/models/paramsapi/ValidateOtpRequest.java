package com.pertamina.portal.core.models.paramsapi;

import com.google.gson.annotations.SerializedName;

public class ValidateOtpRequest {

    @SerializedName("OTPCode")
    private String OTPCode;

    public String getOTPCode() {
        return OTPCode;
    }

    public void setOTPCode(String OTPCode) {
        this.OTPCode = OTPCode;
    }
}

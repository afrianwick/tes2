package com.pertamina.portal.iam.models;

import java.io.Serializable;

public class ClaimRequest implements Serializable {
    /**
     Claim type: ClaimTypeText
     Patient: FI_DEPENDENT
     Patient Age: AGEOFYEAR
     Status: PISA (contoh: "P")
     Claim Amount: FI_CLAIMAMOUNT
     Claim Amount Text: FI_CLAIMAMOUNTTXT
     Granted Amount: diisi free text oleh approver (HR Medical / SSC), mandatory
     Bill Date: FI_BILLDATE (format yyyymmdd, ditampilkan dd-mm-yyyy)
     Bill Number: FI_BILNR
     Diagnose 1-3 : combobox dengan data dari API My Benefit -> Get ICD_10 dengan filter "MCL"
     Diagnose 4: diisi free text oleh approver (HR Medical / SSC), optional
     Cost Center: combobox dengan data dari API My Personal -> Get Cost Center

     */
    public String folioNumber, claimType, patient, patientAge, strStatus,
            claimAmount, claimAmountTxt, grantedAmount, grantedAmountText,
            billDate, billNumber,
            diagnoseId1, diagnoseId2, diagnoseId3, diagnose4, costCenter, postedClaimAmount,
            diagnoseTxt1, diagnoseTxt2, diagnoseTxt3;
}

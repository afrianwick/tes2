package com.pertamina.portal.iam.models;

import java.util.Date;

public class MedicalClaim {
    public long id;
    public String docNumber;
    public int claimType;
    public Family patient;
    public String status;
    public long claimAmount;
    public Date billDate;
    public String billNumber;
    public String diagnose;
    public boolean isResumeMedsDrDocYgMerwtExist;
    public boolean isKuitansiAslDgMateraiExist;
    public boolean isCopyResepObatExist;
    public Comment comment;
}

package com.pertamina.portal.iam.models;

import java.util.Date;

public class AutismClaim {
    public int claimType;
    public Family patient;
    public long claimAmount;
    public Date claimDate;
    public Date endDate;
    public boolean isDocMediclResumeExist;
    public boolean isKuitansiMateraiExist;
    public boolean isCopyResepObatExist;
    public boolean isSuratKetKemajuanTerapiAutsmExist;
    public Comment comment;
}

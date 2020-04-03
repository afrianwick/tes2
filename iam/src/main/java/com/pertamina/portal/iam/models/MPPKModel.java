package com.pertamina.portal.iam.models;

public class MPPKModel {

//    {"Table0":[{"PPERNRM":760010,"PGBDATM":"20-01-1985","PBUKRSM":"1010 - PT Pertamina (Persero)","PPKT01M":5.0,"PBEGDAM":20180101,"PENDDAM":20181231}]}

    private String PPERNRM, PGBDATM, PBUKRSM, PPKT01M, PBEGDAM, PENDDAM;

    public String getPPERNRM() {
        return PPERNRM;
    }

    public void setPPERNRM(String PPERNRM) {
        this.PPERNRM = PPERNRM;
    }

    public String getPGBDATM() {
        return PGBDATM;
    }

    public void setPGBDATM(String PGBDATM) {
        this.PGBDATM = PGBDATM;
    }

    public String getPBUKRSM() {
        return PBUKRSM;
    }

    public void setPBUKRSM(String PBUKRSM) {
        this.PBUKRSM = PBUKRSM;
    }

    public String getPPKT01M() {
        return PPKT01M;
    }

    public void setPPKT01M(String PPKT01M) {
        this.PPKT01M = PPKT01M;
    }

    public String getPBEGDAM() {
        return PBEGDAM;
    }

    public void setPBEGDAM(String PBEGDAM) {
        this.PBEGDAM = PBEGDAM;
    }

    public String getPENDDAM() {
        return PENDDAM;
    }

    public void setPENDDAM(String PENDDAM) {
        this.PENDDAM = PENDDAM;
    }
}

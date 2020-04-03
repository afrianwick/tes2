package com.pertamina.portal.iam.models;

import java.util.Date;
import io.realm.RealmObject;

public class Task extends RealmObject {
    public String id;
    public String folioNumber;
    public String processName;
    public String lastActivity;
    public Date requestDate;
    public boolean statusNotif;
    public int statusDoc;
    public Karyawan karyawan;
    public String code;
    public String processInstanceID;
    public String k2SerailNumber;
    public String action;
    public String processStep; // PENDING, APPROVED, REJECTED

    public Task() {

    }

    public Task(String id, boolean statusNotif, String folioNumber, String processName, String lastActivity) {
        this.id = id;
        this.statusNotif = statusNotif;
        this.folioNumber = folioNumber;
        this.processName = processName;
        this.lastActivity = lastActivity;
    }
}

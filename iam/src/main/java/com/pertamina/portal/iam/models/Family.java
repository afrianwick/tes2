package com.pertamina.portal.iam.models;

import java.util.Date;

public class Family {
    public long id;
    public int familyType; // Istri/Suami, Anak
    public int urutanAnak; // Jika tipe familynya adalah Anak
    public String name;
    public int gender;
    public String birthPlace;
    public Date birthDate;
    public int country;
    public int nationality;
    public boolean dependentTax;
    public boolean medicalReimburstment;
    public String reason;
    public Date since;
}

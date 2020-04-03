package com.pertamina.portal.iam.utils;

import android.content.Context;

import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.DateUtils;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.iam.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SKetXML {

    public static String getXMLDataKemitraan(Context context, String PDIRTXM, String CompanyLegalName, String docName, String docType, String lastComment,
                                             String purpose, String PFANAMM_FAMILY_MEMBER, String PFGBDTM_FAMILY_MEMBER_YYYYMMDD, String PFGBOTM_FAMILY_MEMBER, String PFGBDTM_FAMILY_MEMBER_DMMMMYYYY_TEXT,
                                             String AreaAddress, String PDIRIDM_POSITION_ATTRIBUTE, String PDIRTXM_POSITION_ATTRIBUTE, String HiringDate_YYYYMMDD, String HiringDate_DMMMMYYYY_TXT,
                                             String PGBORTM_PERSONAL_DATA, String PGBDATM_PERSONAL_DATA_YYYYMMDD, String PGBDATM_PERSONAL_DATA_DMMMMYYYY_TEXT, String PCNAMEM_EMPLOYEE_HEADER, String PPERNRM_EMPLOYEE_HEADER,
                                             String PPLANSM_EMPLOYEE_HEADER, String PPLSTXM_POSITION_ATTRIBUTE, String CompanyAddress, String PFGBDTM_FAMILY_MEMBER_DMMMMYYYY, String PPLSTXM_POSITION_ATTRIBUTE_ROLE_OWNER, String PDIRIDM_POSITION_ATTRIBUTE_ROLE_OWNER, String PDIRTXM_POSITION_ATTRIBUTE_ROLE_OWNER) {
        String Field16 = PrefUtils.Build(context).getPref().getString(Constants.KEY_PBUKRSM, "").equals("1010") ? "Direktorat " + PDIRTXM : "SDM " + CompanyLegalName;
        String result = "";

        result += "<TemplateData>";
        result += "<TemplateDataRequest>";
        result += "<PersonID>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, "")  +"</PersonID>";
        result += "<DocName>"+ docName +"</DocName>";
        result += "<DocType>"+ docType +"</DocType>";
        result += "<TMT>"+ new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) +"</TMT>";
        result += "<TemplateName>"+ context.getResources().getStringArray(R.array.leave_type_sket_id)[1] +"</TemplateName>";
        result += "<RoleName>SSC_SKET</RoleName>";
        result += "<LastComment>"+ lastComment +"</LastComment>";
        result += "<Purpose>"+ purpose +"</Purpose>";
        result += "<FamilyMember>"+ PFANAMM_FAMILY_MEMBER +"</FamilyMember>";
        result += "<PFGBDTM>"+ PFGBDTM_FAMILY_MEMBER_YYYYMMDD +"</PFGBDTM>";
        result += "<PFANAMM>"+ PFANAMM_FAMILY_MEMBER +"</PFANAMM>";
        result += "<PFGBOTM>"+ PFGBOTM_FAMILY_MEMBER +"</PFGBOTM>";
        result += "<PFGBDTM_TEXT>"+ PFGBDTM_FAMILY_MEMBER_DMMMMYYYY_TEXT +"</PFGBDTM_TEXT>";
        result += "<ADDRESS>"+ AreaAddress +"</ADDRESS>";
        result += "<PCNAMEM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, "") +"</PCNAMEM>";
        result += "<PPERNRM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, "") +"</PPERNRM>";
        result += "<PBUKRSM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PBUKRSM, "") +"</PBUKRSM>";
        result += "<PPLTXTM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLTXTM, "") +"</PPLTXTM>";
        result += "<PPLANSM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLANSM, "") +"</PPLANSM>";
        result += "<PDIRIDM>"+ PDIRIDM_POSITION_ATTRIBUTE +"</PDIRIDM>";
        result += "<PDIRTXM>"+ PDIRTXM_POSITION_ATTRIBUTE +"</PDIRTXM>";
        result += "<HiringDate>"+ HiringDate_YYYYMMDD +"</HiringDate>";
        result += "<HiringDate_TXT>"+ HiringDate_DMMMMYYYY_TXT +"</HiringDate_TXT>";
        result += "<BaseSalary></BaseSalary>";
        result += "<PGBORTM>"+ PGBORTM_PERSONAL_DATA +"</PGBORTM>";
        result += "<PGBDATM>"+ PGBDATM_PERSONAL_DATA_YYYYMMDD +"</PGBDATM>";
        result += "<PGBDATM_TEXT>"+ PGBDATM_PERSONAL_DATA_DMMMMYYYY_TEXT +"</PGBDATM_TEXT>";
        result += "<CompanyName>"+ CompanyLegalName +"</CompanyName>";
        result += "<PCNAMEM_Signer>"+ PCNAMEM_EMPLOYEE_HEADER +"</PCNAMEM_Signer>";
        result += "<PPERNRM_Signer>"+ PPERNRM_EMPLOYEE_HEADER +"</PPERNRM_Signer>";
        result += "<PPLANSM_Signer>"+ PPLANSM_EMPLOYEE_HEADER +"</PPLANSM_Signer>";
        result += "<PPLSTXM_Signer>"+ PPLSTXM_POSITION_ATTRIBUTE_ROLE_OWNER +"</PPLSTXM_Signer>";
        result += "<PDIRIDM_Signer>"+ PDIRIDM_POSITION_ATTRIBUTE_ROLE_OWNER +"</PDIRIDM_Signer>";
        result += "<PDIRTXM_Signer>"+ PDIRTXM_POSITION_ATTRIBUTE_ROLE_OWNER +"</PDIRTXM_Signer>";
        result += "<CompanyName_Signer>"+ CompanyLegalName +"</CompanyName_Signer>";
        result += "<CompanyAddress_Signer>"+ CompanyAddress +"</CompanyAddress_Signer>";
        result += "<Field01>-</Field01>";
        result += "<Field02>"+ PDIRTXM_POSITION_ATTRIBUTE +"</Field02>";
        result += "<Field03>"+ CompanyLegalName +"</Field03>";
        result += "<Field04>"+ CompanyAddress +"</Field04>";
        result += "<Field05>"+ PFANAMM_FAMILY_MEMBER +"</Field05>";
        result += "<Field06>"+ PFGBOTM_FAMILY_MEMBER +"</Field06>";
        result += "<Field07>"+ PFGBDTM_FAMILY_MEMBER_DMMMMYYYY +"</Field07>";
        result += "<Field08>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, "") +"</Field08>";
        result += "<Field09>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, "") +"</Field09>";
        result += "<Field10>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLTXTM, "") +"</Field10>";
        result += "<Field11>"+ PDIRTXM_POSITION_ATTRIBUTE +"</Field11>";
        result += "<Field12>"+ CompanyAddress +"</Field12>";
        result += "<Field13>"+ CompanyLegalName +"</Field13>";
        result += "<Field14>"+ new SimpleDateFormat("d MMMM yyyy").format(Calendar.getInstance().getTime()) +"</Field14>";
        result += "<Field15>"+ Field16 +"</Field15>";
        result += "<Field16>"+ PPLSTXM_POSITION_ATTRIBUTE +"</Field16>";
        result += "<Field17>"+ PCNAMEM_EMPLOYEE_HEADER +"</Field17>";
//        result += "<Field19>"+ Field19 +"</Field19>";
//        result += "<Field20>"+ Field20 +"</Field20>";
//        result += "<Field21>"+ Field21 +"</Field21>";
//        result += "<Field22>"+ Field22 +"</Field22>";
//        result += "<Field23>"+ Field23 +"</Field23>";
//        result += "<Field24>"+ Field24 +"</Field24>";
//        result += "<Field25>"+ Field25 +"</Field25>";
//        result += "<Field26>"+ Field26 +"</Field26>";
//        result += "<Field27>"+ Field27 +"</Field27>";
//        result += "<Field28>"+ Field28 +"</Field28>";
//        result += "<Field29>"+ Field29 +"</Field29>";
//        result += "<Field30>"+ Field30 +"</Field30>";
//        result += "<Field31>"+ Field31 +"</Field31>";
//        result += "<Field32>"+ Field32 +"</Field32>";
//        result += "<Field33>"+ Field33 +"</Field33>";
//        result += "<Field34>"+ Field34 +"</Field34>";
//        result += "<Field35>"+ Field35 +"</Field35>";
//        result += "<Field36>"+ Field36 +"</Field36>";
//        result += "<Field37>"+ Field37 +"</Field37>";
//        result += "<Field38>"+ Field38 +"</Field38>";
//        result += "<Field39>"+ Field39 +"</Field39>";
//        result += "<Field40>"+ Field40 +"</Field40>";
//        result += "<Field41>"+ Field41 +"</Field41>";
//        result += "<Field42>"+ Field42 +"</Field42>";
//        result += "<Field43>"+ Field43 +"</Field43>";
//        result += "<Field44>"+ Field44 +"</Field44>";
//        result += "<Field45>"+ Field45 +"</Field45>";
        result += "<Image01>TTD_00" + PPERNRM_EMPLOYEE_HEADER +"</Image01>";
        result += "<Prefix>Ket</Prefix>";
        result += "<Endda>"+ new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) +"</Endda>";

        result += "</TemplateDataRequest>";
        result += "</TemplateData>";
        return result;
    }

    public static String getXMLDataKesehatan(Context context, String PDIRTXM, String CompanyLegalName, String docName, String docType, String lastComment,
                                             String purpose, String PFANAMM_FAMILY_MEMBER, String PFGBDTM_FAMILY_MEMBER_YYYYMMDD, String PFGBOTM_FAMILY_MEMBER, String PFGBDTM_FAMILY_MEMBER_DMMMMYYYY_TEXT,
                                             String AreaAddress, String PDIRIDM_POSITION_ATTRIBUTE, String PDIRTXM_POSITION_ATTRIBUTE, String HiringDate_YYYYMMDD, String HiringDate_DMMMMYYYY_TXT,
                                             String PGBORTM_PERSONAL_DATA, String PGBDATM_PERSONAL_DATA_YYYYMMDD, String PGBDATM_PERSONAL_DATA_DMMMMYYYY_TEXT, String PCNAMEM_EMPLOYEE_HEADER, String PPERNRM_EMPLOYEE_HEADER,
                                             String PPLANSM_EMPLOYEE_HEADER, String PPLSTXM_POSITION_ATTRIBUTE, String CompanyAddress, String PFGBDTM_FAMILY_MEMBER_DMMMMYYYY, String PPLSTXM_POSITION_ATTRIBUTE_ROLE_OWNER, String PDIRIDM_POSITION_ATTRIBUTE_ROLE_OWNER, String PDIRTXM_POSITION_ATTRIBUTE_ROLE_OWNER) {
        String Field16 = PrefUtils.Build(context).getPref().getString(Constants.KEY_PBUKRSM, "").equals("1010") ? "Direktorat " + PDIRTXM : "SDM " + CompanyLegalName;
        String result = "";

        result += "<TemplateData>";
        result += "<TemplateDataRequest>";
        result += "<PersonID>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, "")  +"</PersonID>";
        result += "<DocName>"+ docName +"</DocName>";
        result += "<DocType>"+ docType +"</DocType>";
        result += "<TMT>"+ new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) +"</TMT>";
        result += "<TemplateName>"+ context.getResources().getStringArray(R.array.leave_type_sket_id)[1] +"</TemplateName>";
        result += "<RoleName>SSC_SKET</RoleName>";
        result += "<LastComment>"+ lastComment +"</LastComment>";
        result += "<Purpose>"+ purpose +"</Purpose>";
        result += "<FamilyMember>"+ PFANAMM_FAMILY_MEMBER +"</FamilyMember>";
        result += "<PFGBDTM>"+ PFGBDTM_FAMILY_MEMBER_YYYYMMDD +"</PFGBDTM>";
        result += "<PFANAMM>"+ PFANAMM_FAMILY_MEMBER +"</PFANAMM>";
        result += "<PFGBOTM>"+ PFGBOTM_FAMILY_MEMBER +"</PFGBOTM>";
        result += "<PFGBDTM_TEXT>"+ PFGBDTM_FAMILY_MEMBER_DMMMMYYYY_TEXT +"</PFGBDTM_TEXT>";
        result += "<ADDRESS>"+ AreaAddress +"</ADDRESS>";
        result += "<PCNAMEM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, "") +"</PCNAMEM>";
        result += "<PPERNRM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, "") +"</PPERNRM>";
        result += "<PBUKRSM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PBUKRSM, "") +"</PBUKRSM>";
        result += "<PPLTXTM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLTXTM, "") +"</PPLTXTM>";
        result += "<PPLANSM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLANSM, "") +"</PPLANSM>";
        result += "<PDIRIDM>"+ PDIRIDM_POSITION_ATTRIBUTE +"</PDIRIDM>";
        result += "<PDIRTXM>"+ PDIRTXM_POSITION_ATTRIBUTE +"</PDIRTXM>";
        result += "<HiringDate>"+ HiringDate_YYYYMMDD +"</HiringDate>";
        result += "<HiringDate_TXT>"+ HiringDate_DMMMMYYYY_TXT +"</HiringDate_TXT>";
        result += "<BaseSalary></BaseSalary>";
        result += "<PGBORTM>"+ PGBORTM_PERSONAL_DATA +"</PGBORTM>";
        result += "<PGBDATM>"+ PGBDATM_PERSONAL_DATA_YYYYMMDD +"</PGBDATM>";
        result += "<PGBDATM_TEXT>"+ PGBDATM_PERSONAL_DATA_DMMMMYYYY_TEXT +"</PGBDATM_TEXT>";
        result += "<CompanyName>"+ CompanyLegalName +"</CompanyName>";
        result += "<PCNAMEM_Signer>"+ PCNAMEM_EMPLOYEE_HEADER +"</PCNAMEM_Signer>";
        result += "<PPERNRM_Signer>"+ PPERNRM_EMPLOYEE_HEADER +"</PPERNRM_Signer>";
        result += "<PPLANSM_Signer>"+ PPLANSM_EMPLOYEE_HEADER +"</PPLANSM_Signer>";
        result += "<PPLSTXM_Signer>"+ PPLSTXM_POSITION_ATTRIBUTE_ROLE_OWNER +"</PPLSTXM_Signer>";
        result += "<PDIRIDM_Signer>"+ PDIRIDM_POSITION_ATTRIBUTE_ROLE_OWNER +"</PDIRIDM_Signer>";
        result += "<PDIRTXM_Signer>"+ PDIRTXM_POSITION_ATTRIBUTE_ROLE_OWNER +"</PDIRTXM_Signer>";
        result += "<CompanyName_Signer>"+ CompanyLegalName +"</CompanyName_Signer>";
        result += "<CompanyAddress_Signer>"+ CompanyAddress +"</CompanyAddress_Signer>";
        result += "<Field01>-</Field01>";
        result += "<Field02>"+ PDIRTXM_POSITION_ATTRIBUTE +"</Field02>";
        result += "<Field03>"+ CompanyLegalName +"</Field03>";
        result += "<Field04>"+ CompanyAddress +"</Field04>";
        result += "<Field05>"+ PFANAMM_FAMILY_MEMBER +"</Field05>";
        result += "<Field06>"+ PFGBOTM_FAMILY_MEMBER +"</Field06>";
        result += "<Field07>"+ PFGBDTM_FAMILY_MEMBER_DMMMMYYYY +"</Field07>";
        result += "<Field08>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, "") +"</Field08>";
        result += "<Field09>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, "") +"</Field09>";
        result += "<Field10>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLTXTM, "") +"</Field10>";
        result += "<Field11>"+ PDIRTXM_POSITION_ATTRIBUTE +"</Field11>";
        result += "<Field12>"+ CompanyAddress +"</Field12>";
        result += "<Field13>"+ CompanyLegalName +"</Field13>";
        result += "<Field14>"+ CompanyLegalName +"</Field14>";
        result += "<Field15>"+ new SimpleDateFormat("d MMMM yyyy").format(Calendar.getInstance().getTime()) +"</Field15>";
        result += "<Field16>"+ Field16 +"</Field16>";
        result += "<Field17>"+ PPLSTXM_POSITION_ATTRIBUTE +"</Field17>";
        result += "<Field18>"+ PCNAMEM_EMPLOYEE_HEADER +"</Field18>";
//        result += "<Field19>"+ Field19 +"</Field19>";
//        result += "<Field20>"+ Field20 +"</Field20>";
//        result += "<Field21>"+ Field21 +"</Field21>";
//        result += "<Field22>"+ Field22 +"</Field22>";
//        result += "<Field23>"+ Field23 +"</Field23>";
//        result += "<Field24>"+ Field24 +"</Field24>";
//        result += "<Field25>"+ Field25 +"</Field25>";
//        result += "<Field26>"+ Field26 +"</Field26>";
//        result += "<Field27>"+ Field27 +"</Field27>";
//        result += "<Field28>"+ Field28 +"</Field28>";
//        result += "<Field29>"+ Field29 +"</Field29>";
//        result += "<Field30>"+ Field30 +"</Field30>";
//        result += "<Field31>"+ Field31 +"</Field31>";
//        result += "<Field32>"+ Field32 +"</Field32>";
//        result += "<Field33>"+ Field33 +"</Field33>";
//        result += "<Field34>"+ Field34 +"</Field34>";
//        result += "<Field35>"+ Field35 +"</Field35>";
//        result += "<Field36>"+ Field36 +"</Field36>";
//        result += "<Field37>"+ Field37 +"</Field37>";
//        result += "<Field38>"+ Field38 +"</Field38>";
//        result += "<Field39>"+ Field39 +"</Field39>";
//        result += "<Field40>"+ Field40 +"</Field40>";
//        result += "<Field41>"+ Field41 +"</Field41>";
//        result += "<Field42>"+ Field42 +"</Field42>";
//        result += "<Field43>"+ Field43 +"</Field43>";
//        result += "<Field44>"+ Field44 +"</Field44>";
//        result += "<Field45>"+ Field45 +"</Field45>";
        result += "<Image01>TTD_00" + PPERNRM_EMPLOYEE_HEADER +"</Image01>";
        result += "<Prefix>Ket</Prefix>";
        result += "<Endda>"+ new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) +"</Endda>";



        result += "</TemplateDataRequest>";
        result += "</TemplateData>";
        return result;
    }

    public static String getXMLDataTanpaUpahIndonesia(Context context, String docName, String docType, String lastComment,
                                    String purpose, String AreaAddress, String PDIRTXM, String CompanyLegalName, String PDIRIDM_POSITION_ATTRIBUTE, String PDIRTXM_POSITION_ATTRIBUTE,
                                                      String HiringDate, String HiringDate_TXT, String PGBORTM_PERSONAL_DATA, String PGBDATM_PERSONAL_DATA, String PGBDATM_PERSONAL_DATA_TEXT,
                                                      String PCNAMEM_EMPLOYEE_HEADER, String PPERNRM_EMPLOYEE_HEADER, String PPLANSM_EMPLOYEE_HEADER, String PPLSTXM_POSITION_ATTRIBUTE,
                                                      String CompanyAddress, String PPERNRM_CURRENT_ORGANIZATIONAL_ASSIGNMENT, String PBEGDAM_CURRENT_ORGANIZATIONAL_ASSIGNMENT, String PBUTXTM_COMPANY_CODE, String PPLSTXM_POSITION_ATTRIBUTE_ROLE_OWNER, String PDIRIDM_POSITION_ATTRIBUTE_ROLE_OWNER, String PDIRTXM_POSITION_ATTRIBUTE_ROLE_OWNER) {

        String Field16 = PrefUtils.Build(context).getPref().getString(Constants.KEY_PBUKRSM, "").equals("1010") ? "Direktorat " + PDIRTXM : "SDM " + CompanyLegalName;
        String result = "";

        result += "<TemplateData>";
        result += "<TemplateDataRequest>";
        result += "<PersonID>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, "")  +"</PersonID>";
        result += "<DocName>"+ docName +"</DocName>";
        result += "<DocType>"+ docType +"</DocType>";
        result += "<TMT>"+ new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) +"</TMT>";
        result += "<TemplateName>"+ context.getResources().getStringArray(R.array.leave_type_sket_id)[3] +"</TemplateName>";
        result += "<RoleName>"+ "HR_SERVICES" +"</RoleName>";
        result += "<LastComment>"+ lastComment +"</LastComment>";
        result += "<Purpose>"+ purpose +"</Purpose>";
        result += "<FamilyMember></FamilyMember>";
        result += "<PFGBDTM></PFGBDTM>";
        result += "<PFANAMM></PFANAMM>";
        result += "<PFGBOTM></PFGBOTM>";
        result += "<PFGBDTM_TEXT></PFGBDTM_TEXT>";
        result += "<ADDRESS>"+ AreaAddress +"</ADDRESS>";
        result += "<PCNAMEM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, "") +"</PCNAMEM>";
        result += "<PPERNRM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, "") +"</PPERNRM>";
        result += "<PBUKRSM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PBUKRSM, "") +"</PBUKRSM>";
        result += "<PPLTXTM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLTXTM, "") +"</PPLTXTM>";
        result += "<PPLANSM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLANSM, "") +"</PPLANSM>";
        result += "<PDIRIDM>"+ PDIRIDM_POSITION_ATTRIBUTE +"</PDIRIDM>";
        result += "<PDIRTXM>"+ PDIRTXM_POSITION_ATTRIBUTE +"</PDIRTXM>";
        result += "<HiringDate>"+ HiringDate +"</HiringDate>";
        result += "<HiringDate_TXT>"+ HiringDate_TXT +"</HiringDate_TXT>";
        result += "<BaseSalary></BaseSalary>";
        result += "<PGBORTM>"+ PGBORTM_PERSONAL_DATA +"</PGBORTM>";
        result += "<PGBDATM>"+ PGBDATM_PERSONAL_DATA +"</PGBDATM>";
        result += "<PGBDATM_TEXT>"+ PGBDATM_PERSONAL_DATA_TEXT +"</PGBDATM_TEXT>";
        result += "<CompanyName>"+ CompanyLegalName +"</CompanyName>";
        result += "<PCNAMEM_Signer>"+ PCNAMEM_EMPLOYEE_HEADER +"</PCNAMEM_Signer>";
        result += "<PPERNRM_Signer>"+ PPERNRM_EMPLOYEE_HEADER +"</PPERNRM_Signer>";
        result += "<PPLANSM_Signer>"+ PPLANSM_EMPLOYEE_HEADER +"</PPLANSM_Signer>";
        result += "<PPLSTXM_Signer>"+ PPLSTXM_POSITION_ATTRIBUTE_ROLE_OWNER +"</PPLSTXM_Signer>";
        result += "<PDIRIDM_Signer>"+ PDIRIDM_POSITION_ATTRIBUTE_ROLE_OWNER +"</PDIRIDM_Signer>";
        result += "<PDIRTXM_Signer>"+ PDIRTXM_POSITION_ATTRIBUTE_ROLE_OWNER +"</PDIRTXM_Signer>";
        result += "<CompanyName_Signer>"+ CompanyLegalName +"</CompanyName_Signer>";
        result += "<CompanyAddress_Signer>"+ CompanyAddress +"</CompanyAddress_Signer>";
        result += "<Field01>-</Field01>";
        result += "<Field02>"+ PDIRTXM_POSITION_ATTRIBUTE +"</Field02>";
        result += "<Field03>"+ CompanyLegalName +"</Field03>";
        result += "<Field04>"+ CompanyAddress +"</Field04>";
        result += "<Field05>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, "") +"</Field05>";
        result += "<Field06>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, "") +"</Field06>";
        result += "<Field07>"+ PGBORTM_PERSONAL_DATA +"</Field07>";
        result += "<Field08>"+ PGBDATM_PERSONAL_DATA +"</Field08>";
        result += "<Field09>"+ AreaAddress +"</Field09>";
        result += "<Field10>"+ CompanyLegalName +"</Field10>";
        result += "<Field11>"+ HiringDate +"</Field11>";
        result += "<Field12>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLTXTM, "") +"</Field12>";
        result += "<Field13>"+ PDIRTXM_POSITION_ATTRIBUTE +"</Field13>";
        result += "<Field14>"+ CompanyLegalName +"</Field14>";
        result += "<Field15>"+ new SimpleDateFormat("d MMMM yyyy").format(Calendar.getInstance().getTime()) +"</Field15>";
        result += "<Field16>"+ Field16 +"</Field16>";
        result += "<Field17>"+ PPLSTXM_POSITION_ATTRIBUTE +"</Field17>";
        result += "<Field18>"+ PCNAMEM_EMPLOYEE_HEADER +"</Field18>";
        result += "<Field19>"+ PPERNRM_CURRENT_ORGANIZATIONAL_ASSIGNMENT +"</Field19>";
        result += "<Field20>"+ PBEGDAM_CURRENT_ORGANIZATIONAL_ASSIGNMENT +"</Field20>";
        result += "<Field21>"+ PBUTXTM_COMPANY_CODE +"</Field21>";
//        result += "<Field22>"+ Field22 +"</Field22>";
//        result += "<Field23>"+ Field23 +"</Field23>";
//        result += "<Field24>"+ Field24 +"</Field24>";
//        result += "<Field25>"+ Field25 +"</Field25>";
//        result += "<Field26>"+ Field26 +"</Field26>";
//        result += "<Field27>"+ Field27 +"</Field27>";
//        result += "<Field28>"+ Field28 +"</Field28>";
//        result += "<Field29>"+ Field29 +"</Field29>";
//        result += "<Field30>"+ Field30 +"</Field30>";
//        result += "<Field31>"+ Field31 +"</Field31>";
//        result += "<Field32>"+ Field32 +"</Field32>";
//        result += "<Field33>"+ Field33 +"</Field33>";
//        result += "<Field34>"+ Field34 +"</Field34>";
//        result += "<Field35>"+ Field35 +"</Field35>";
//        result += "<Field36>"+ Field36 +"</Field36>";
//        result += "<Field37>"+ Field37 +"</Field37>";
//        result += "<Field38>"+ Field38 +"</Field38>";
//        result += "<Field39>"+ Field39 +"</Field39>";
//        result += "<Field40>"+ Field40 +"</Field40>";
//        result += "<Field41>"+ Field41 +"</Field41>";
//        result += "<Field42>"+ Field42 +"</Field42>";
//        result += "<Field43>"+ Field43 +"</Field43>";
//        result += "<Field44>"+ Field44 +"</Field44>";
//        result += "<Field45>"+ Field45 +"</Field45>";
        result += "<Image01>TTD_00" + PPERNRM_EMPLOYEE_HEADER +"</Image01>";
        result += "<Prefix>Ket</Prefix>";
        result += "<Endda>"+ new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) +"</Endda>";



        result += "</TemplateDataRequest>";
        result += "</TemplateData>";
        return result;
    }

    public static String getXMLDataTanpaUpahIngris(Context context, String docName, String docType, String lastComment,
                                                      String purpose, String AreaAddress, String PDIRTXM, String CompanyLegalName, String PDIRIDM_POSITION_ATTRIBUTE, String PDIRTXM_POSITION_ATTRIBUTE,
                                                      String HiringDate, String HiringDate_TXT, String PGBORTM_PERSONAL_DATA, String PGBDATM_PERSONAL_DATA, String PGBDATM_PERSONAL_DATA_TEXT,
                                                      String PCNAMEM_EMPLOYEE_HEADER, String PPERNRM_EMPLOYEE_HEADER, String PPLANSM_EMPLOYEE_HEADER, String PPLSTXM_POSITION_ATTRIBUTE,
                                                      String CompanyAddress, String PBEGDAM_CURRENT_ORGANIZATIONAL_ASSIGNMENT, String PBUTXTM_COMPANY_CODE, String PPLSTXM_POSITION_ATTRIBUTE_ROLE_OWNER, String PDIRIDM_POSITION_ATTRIBUTE_ROLE_OWNER, String PDIRTXM_POSITION_ATTRIBUTE_ROLE_OWNER) {

        String Field15 = PrefUtils.Build(context).getPref().getString(Constants.KEY_PBUKRSM, "").equals("1010") ? "Directorate ": CompanyLegalName;

        String result = "";

        result += "<TemplateData>";
        result += "<TemplateDataRequest>";
        result += "<PersonID>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, "")  +"</PersonID>";
        result += "<DocName>"+ docName +"</DocName>";
        result += "<DocType>"+ docType +"</DocType>";
        result += "<TMT>"+ new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) +"</TMT>";
        result += "<TemplateName>"+ context.getResources().getStringArray(R.array.leave_type_sket_id)[2] +"</TemplateName>";
        result += "<RoleName>"+ "HR_SERVICES" +"</RoleName>";
        result += "<LastComment>"+ lastComment +"</LastComment>";
        result += "<Purpose>"+ purpose +"</Purpose>";
        result += "<FamilyMember></FamilyMember>";
        result += "<PFGBDTM></PFGBDTM>";
        result += "<PFANAMM></PFANAMM>";
        result += "<PFGBOTM></PFGBOTM>";
        result += "<PFGBDTM_TEXT></PFGBDTM_TEXT>";
        result += "<ADDRESS>"+ AreaAddress +"</ADDRESS>";
        result += "<PCNAMEM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, "") +"</PCNAMEM>";
        result += "<PPERNRM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, "") +"</PPERNRM>";
        result += "<PBUKRSM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PBUKRSM, "") +"</PBUKRSM>";
        result += "<PPLTXTM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLTXTM, "") +"</PPLTXTM>";
        result += "<PPLANSM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLANSM, "") +"</PPLANSM>";
        result += "<PDIRIDM>"+ PDIRIDM_POSITION_ATTRIBUTE +"</PDIRIDM>";
        result += "<PDIRTXM>"+ PDIRTXM_POSITION_ATTRIBUTE +"</PDIRTXM>";
        result += "<HiringDate>"+ HiringDate +"</HiringDate>";
        result += "<HiringDate_TXT>"+ HiringDate_TXT +"</HiringDate_TXT>";
        result += "<BaseSalary></BaseSalary>";
        result += "<PGBORTM>"+ PGBORTM_PERSONAL_DATA +"</PGBORTM>";
        result += "<PGBDATM>"+ PGBDATM_PERSONAL_DATA +"</PGBDATM>";
        result += "<PGBDATM_TEXT>"+ PGBDATM_PERSONAL_DATA_TEXT +"</PGBDATM_TEXT>";
        result += "<CompanyName>"+ CompanyLegalName +"</CompanyName>";
        result += "<PCNAMEM_Signer>"+ PCNAMEM_EMPLOYEE_HEADER +"</PCNAMEM_Signer>";
        result += "<PPERNRM_Signer>"+ PPERNRM_EMPLOYEE_HEADER +"</PPERNRM_Signer>";
        result += "<PPLANSM_Signer>"+ PPLANSM_EMPLOYEE_HEADER +"</PPLANSM_Signer>";
        result += "<PPLSTXM_Signer>"+ PPLSTXM_POSITION_ATTRIBUTE_ROLE_OWNER +"</PPLSTXM_Signer>";
        result += "<PDIRIDM_Signer>"+ PDIRIDM_POSITION_ATTRIBUTE_ROLE_OWNER +"</PDIRIDM_Signer>";
        result += "<PDIRTXM_Signer>"+ PDIRTXM_POSITION_ATTRIBUTE_ROLE_OWNER +"</PDIRTXM_Signer>";
        result += "<CompanyName_Signer>"+ CompanyLegalName +"</CompanyName_Signer>";
        result += "<CompanyAddress_Signer>"+ CompanyAddress +"</CompanyAddress_Signer>";
        result += "<Field01>-</Field01>";
        result += "<Field02>"+ PDIRTXM_POSITION_ATTRIBUTE +"</Field02>";
        result += "<Field03>"+ CompanyLegalName +"</Field03>";
        result += "<Field04>"+ CompanyAddress +"</Field04>";
        result += "<Field05>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, "") +"</Field05>";
        result += "<Field06>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, "") +"</Field06>";
        result += "<Field07>"+ PGBORTM_PERSONAL_DATA +"</Field07>";
        result += "<Field08>"+ PGBDATM_PERSONAL_DATA +"</Field08>";
        result += "<Field09>"+ AreaAddress +"</Field09>";
        result += "<Field10>"+ CompanyLegalName +"</Field10>";
        result += "<Field11>"+ HiringDate +"</Field11>";
        result += "<Field12>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLTXTM, "") +"</Field12>";
        result += "<Field13>"+ CompanyLegalName +"</Field13>";
        result += "<Field14>"+ new SimpleDateFormat("d MMMM yyyy").format(Calendar.getInstance().getTime()) +"</Field14>";
        result += "<Field15>"+ Field15 +"</Field15>";
        result += "<Field16>"+ PPLSTXM_POSITION_ATTRIBUTE +"</Field16>";
        result += "<Field17>"+ PCNAMEM_EMPLOYEE_HEADER +"</Field17>";
        result += "<Field18>"+ PBEGDAM_CURRENT_ORGANIZATIONAL_ASSIGNMENT +"</Field18>";
        result += "<Field19>"+ PBUTXTM_COMPANY_CODE +"</Field19>";
//        result += "<Field20>"+ PDIRTXM_POSITION_ATTRIBUTE +"</Field13>";
//        result += "<Field21>"+ PPERNRM_CURRENT_ORGANIZATIONAL_ASSIGNMENT +"</Field19>";
//        result += "<Field22>"+ Field22 +"</Field22>";
//        result += "<Field23>"+ Field23 +"</Field23>";
//        result += "<Field24>"+ Field24 +"</Field24>";
//        result += "<Field25>"+ Field25 +"</Field25>";
//        result += "<Field26>"+ Field26 +"</Field26>";
//        result += "<Field27>"+ Field27 +"</Field27>";
//        result += "<Field28>"+ Field28 +"</Field28>";
//        result += "<Field29>"+ Field29 +"</Field29>";
//        result += "<Field30>"+ Field30 +"</Field30>";
//        result += "<Field31>"+ Field31 +"</Field31>";
//        result += "<Field32>"+ Field32 +"</Field32>";
//        result += "<Field33>"+ Field33 +"</Field33>";
//        result += "<Field34>"+ Field34 +"</Field34>";
//        result += "<Field35>"+ Field35 +"</Field35>";
//        result += "<Field36>"+ Field36 +"</Field36>";
//        result += "<Field37>"+ Field37 +"</Field37>";
//        result += "<Field38>"+ Field38 +"</Field38>";
//        result += "<Field39>"+ Field39 +"</Field39>";
//        result += "<Field40>"+ Field40 +"</Field40>";
//        result += "<Field41>"+ Field41 +"</Field41>";
//        result += "<Field42>"+ Field42 +"</Field42>";
//        result += "<Field43>"+ Field43 +"</Field43>";
//        result += "<Field44>"+ Field44 +"</Field44>";
//        result += "<Field45>"+ Field45 +"</Field45>";
        result += "<Image01>TTD_00" + PPERNRM_EMPLOYEE_HEADER +"</Image01>";
        result += "<Prefix>Ket</Prefix>";
        result += "<Endda>"+ new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) +"</Endda>";



        result += "</TemplateDataRequest>";
        result += "</TemplateData>";
        return result;
    }

    public static String getXMLDataSummary(String DataType, String DataSubtype, String DataValue) {
        String result = "";

        result += "<DataSummary>";
        result += "<DataType>" + DataType + "</DataType>";
        result += "<DataSubtype>" + DataSubtype + "</DataSubtype>";
        result += "<DataValue>" + DataValue + "</DataValue>";
        result += "</DataSummary>";

        return result;
    }

    public static String getK2FieldInJsonKesehatanDanKemitraan(Context context, String DocName, String DocType, String TemplateName, String comment, String PDIRTXM,
                                                              String CompanyLegalName, String CompanyAddress, String PFANAMM_FAMILY_MEMBER, String PFGBOTM_FAMILY_MEMBER,
                                                              String PFGBDTM_FAMILY_MEMBER, String PCNAMEM_PERSONAL_DATA, String AreaAddress, String PPLSTXM_POSITION_ATTRIBUTE,
                                                              String PCNAMEM_EMPLOYEE_HEADER, String PPERNRM_EMPLOYEE_HEADER, String PDIRTXM_EMPLOYEE_HEADER, String PDIRTXM_POSITION_ATTRIBUTE) {
        try {
            String result = "";

            Locale id = new Locale("in", "ID");
            Calendar calendar_one_month = Calendar.getInstance();
            calendar_one_month.add(Calendar.MONTH, 1);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("PersonID", PrefUtils.Build(context).getPref().getString(Constants.KEY_PERSONAL_NUM, ""));
            jsonObject.put("DocName", DocName);
            jsonObject.put("DocType", DocType);
            jsonObject.put("TMT", new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
            jsonObject.put("TemplateName", TemplateName);
            jsonObject.put("RoleName", "SSC_SKET");
            jsonObject.put("LastComment", comment);
            jsonObject.put("Field01", "-");
            jsonObject.put("Field02", PDIRTXM_EMPLOYEE_HEADER);
            jsonObject.put("Field03", CompanyLegalName);
            jsonObject.put("Field04", CompanyAddress);
            jsonObject.put("Field05", PFANAMM_FAMILY_MEMBER);
            jsonObject.put("Field06", PFGBOTM_FAMILY_MEMBER);
            jsonObject.put("Field07", PFGBDTM_FAMILY_MEMBER);
            jsonObject.put("Field08", PCNAMEM_PERSONAL_DATA);
            jsonObject.put("Field09", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, ""));
            jsonObject.put("Field10", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLTXTM, ""));
            jsonObject.put("Field11", PDIRTXM_POSITION_ATTRIBUTE);
            jsonObject.put("Field12", AreaAddress);
            jsonObject.put("Field13", CompanyLegalName);
            if (TemplateName.equals(context.getResources().getStringArray(R.array.leave_type_sket_id)[0])) {
                jsonObject.put("Field14", new SimpleDateFormat("d MMMM yyyy"));
                jsonObject.put("Field15", PrefUtils.Build(context).getPref().getString(Constants.KEY_PBUKRSM, "").equals("1010") ? "Direktorat " + PDIRTXM : "SDM " + CompanyLegalName);
                jsonObject.put("Field16", PPLSTXM_POSITION_ATTRIBUTE);
                jsonObject.put("Field17", PCNAMEM_EMPLOYEE_HEADER);
                jsonObject.put("Field18", "-");
            } else if (TemplateName.equals(context.getResources().getStringArray(R.array.leave_type_sket_id)[1])) {
                jsonObject.put("Field14", CompanyLegalName);
                jsonObject.put("Field15", new SimpleDateFormat("d MMMM yyyy"));
                jsonObject.put("Field16", PrefUtils.Build(context).getPref().getString(Constants.KEY_PBUKRSM, "").equals("1010") ? "Direktorat " + PDIRTXM : "SDM " + CompanyLegalName);
                jsonObject.put("Field17", PPLSTXM_POSITION_ATTRIBUTE);
                jsonObject.put("Field18", PCNAMEM_EMPLOYEE_HEADER);
            }
            jsonObject.put("Field19", "-");
            jsonObject.put("Field20", "-");
            jsonObject.put("Field21", "-");
            jsonObject.put("Field22", "-");
            jsonObject.put("Field23", "-");
            jsonObject.put("Field24", "-");
            jsonObject.put("Field25", "-");
            jsonObject.put("Field26", "-");
            jsonObject.put("Field27", "-");
            jsonObject.put("Field28", "-");
            jsonObject.put("Field29", "-");
            jsonObject.put("Field30", "-");
            jsonObject.put("Field31", "-");
            jsonObject.put("Field32", "-");
            jsonObject.put("Field33", "-");
            jsonObject.put("Field34", "-");
            jsonObject.put("Field35", "-");
            jsonObject.put("Field36", "-");
            jsonObject.put("Field37", "-");
            jsonObject.put("Field38", "-");
            jsonObject.put("Field39", "-");
            jsonObject.put("Field40", "-");
            jsonObject.put("Field41", "-");
            jsonObject.put("Field42", "-");
            jsonObject.put("Field43", "-");
            jsonObject.put("Field44", "-");
            jsonObject.put("Field45", "-");
            jsonObject.put("Image01", "TTD_00" + PPERNRM_EMPLOYEE_HEADER);
            jsonObject.put("Image02", "-");
            jsonObject.put("Image03", "-");
            jsonObject.put("Image04", "-");
            jsonObject.put("Image05", "-");
            jsonObject.put("ExecutorPERNR", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, ""));
            jsonObject.put("ExecutorADName", PrefUtils.Build(context).getPref().getString(Constants.KEY_USERNAME, ""));
            jsonObject.put("RequesterPERNR", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, ""));
            jsonObject.put("RequesterADName", PrefUtils.Build(context).getPref().getString(Constants.KEY_USERNAME, ""));
            jsonObject.put("Prefix", "Ket");
            jsonObject.put("Endda", new SimpleDateFormat("yyyyMMdd").format(calendar_one_month.getTime()));
            jsonObject.put("ApprovalPERNR", PPERNRM_EMPLOYEE_HEADER);
            return jsonObject.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getK2FieldInJsonTanpaUpahInggris(Context context, String DocName, String DocType, String TemplateName, String comment, String PDIRTXM,
                                          String CompanyLegalName, String CompanyAddress, String AreaAddress, String PGBORTM_PERSONAL_DATA, String CompanyLegalName_Companies_Requestor,
                                          String PCNAMEM_EMPLOYEE_HEADER, String PPERNRM_EMPLOYEE_HEADER, String PGBDATM_PERSONAL_DATA, String HiringDate, String PPLSTXM_POSITION_ATTRIBUTE,
                                                          String PBEGDAM_CURRENT_ORGANIZATIONAL_ASSSIGNMENT, String PBUTXTM_COMPANY_CODE) {
        try {
            String result = "";

            Locale id = new Locale("in", "ID");
            Calendar calendar_one_month = Calendar.getInstance();
            calendar_one_month.add(Calendar.MONTH, 1);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("PersonID", PrefUtils.Build(context).getPref().getString(Constants.KEY_PERSONAL_NUM, ""));
            jsonObject.put("DocName", DocName);
            jsonObject.put("DocType", DocType);
            jsonObject.put("TMT", new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
            jsonObject.put("TemplateName", TemplateName);
            jsonObject.put("RoleName", "SSC_SKET");
            jsonObject.put("LastComment", comment);
            jsonObject.put("Field01", "-");
            jsonObject.put("Field02", CompanyLegalName);
            jsonObject.put("Field03", CompanyAddress);
            jsonObject.put("Field04", PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, ""));
            jsonObject.put("Field05", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, ""));
            jsonObject.put("Field06", PGBORTM_PERSONAL_DATA);
            jsonObject.put("Field07", PGBDATM_PERSONAL_DATA);
            jsonObject.put("Field08", AreaAddress);
            jsonObject.put("Field09", CompanyLegalName_Companies_Requestor);
            jsonObject.put("Field10", HiringDate);
            jsonObject.put("Field11", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLTXTM, ""));
            jsonObject.put("Field12", CompanyLegalName_Companies_Requestor);
            jsonObject.put("Field13", new SimpleDateFormat("d MMMM yyyy").format(Calendar.getInstance().getTime()));
            jsonObject.put("Field14", PrefUtils.Build(context).getPref().getString(Constants.KEY_PBUKRSM, "").equals("1010") ? "Directorate " : CompanyLegalName);
            jsonObject.put("Field15", PPLSTXM_POSITION_ATTRIBUTE);
            jsonObject.put("Field16", PCNAMEM_EMPLOYEE_HEADER);
            jsonObject.put("Field17", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, ""));
            jsonObject.put("Field18", PBEGDAM_CURRENT_ORGANIZATIONAL_ASSSIGNMENT);
            jsonObject.put("Field19", PBUTXTM_COMPANY_CODE);
            jsonObject.put("Field20", "-");
            jsonObject.put("Field21", "-");
            jsonObject.put("Field22", "-");
            jsonObject.put("Field23", "-");
            jsonObject.put("Field24", "-");
            jsonObject.put("Field25", "-");
            jsonObject.put("Field26", "-");
            jsonObject.put("Field27", "-");
            jsonObject.put("Field28", "-");
            jsonObject.put("Field29", "-");
            jsonObject.put("Field30", "-");
            jsonObject.put("Field31", "-");
            jsonObject.put("Field32", "-");
            jsonObject.put("Field33", "-");
            jsonObject.put("Field34", "-");
            jsonObject.put("Field35", "-");
            jsonObject.put("Field36", "-");
            jsonObject.put("Field37", "-");
            jsonObject.put("Field38", "-");
            jsonObject.put("Field39", "-");
            jsonObject.put("Field40", "-");
            jsonObject.put("Field41", "-");
            jsonObject.put("Field42", "-");
            jsonObject.put("Field43", "-");
            jsonObject.put("Field44", "-");
            jsonObject.put("Field45", "-");
            jsonObject.put("Image01", "TTD_00" + PPERNRM_EMPLOYEE_HEADER);
            jsonObject.put("Image02", "-");
            jsonObject.put("Image03", "-");
            jsonObject.put("Image04", "-");
            jsonObject.put("Image05", "-");
            jsonObject.put("ExecutorPERNR", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, ""));
            jsonObject.put("ExecutorADName", PrefUtils.Build(context).getPref().getString(Constants.KEY_USERNAME, ""));
            jsonObject.put("RequesterPERNR", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, ""));
            jsonObject.put("RequesterADName", PrefUtils.Build(context).getPref().getString(Constants.KEY_USERNAME, ""));
            jsonObject.put("Prefix", "Ket");
            jsonObject.put("Endda", new SimpleDateFormat("yyyyMMdd").format(calendar_one_month.getTime()));
            jsonObject.put("ApprovalPERNR", PPERNRM_EMPLOYEE_HEADER);
            return jsonObject.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getK2FieldInJsonTanpaUpahIndonesia(Context context, String DocName, String DocType, String TemplateName, String comment, String PDIRTXM,
                                                          String CompanyLegalName, String CompanyAddress, String AreaAddress, String PGBORTM_PERSONAL_DATA, String CompanyLegalName_Companies_Requestor,
                                                          String PCNAMEM_EMPLOYEE_HEADER, String PPERNRM_EMPLOYEE_HEADER, String PGBDATM_PERSONAL_DATA, String HiringDate, String PPLSTXM_POSITION_ATTRIBUTE,
                                                          String PBEGDAM_CURRENT_ORGANIZATIONAL_ASSSIGNMENT, String PBUTXTM_COMPANY_CODE, String PDIRTXM_POSITION_ATTRIBUT) {
        try {
            String result = "";

            Locale id = new Locale("in", "ID");
            Calendar calendar_one_month = Calendar.getInstance();
            calendar_one_month.add(Calendar.MONTH, 1);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("PersonID", PrefUtils.Build(context).getPref().getString(Constants.KEY_PERSONAL_NUM, ""));
            jsonObject.put("DocName", DocName);
            jsonObject.put("DocType", DocType);
            jsonObject.put("TMT", new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
            jsonObject.put("TemplateName", TemplateName);
            jsonObject.put("RoleName", "SSC_SKET");
            jsonObject.put("LastComment", comment);
            jsonObject.put("Field01", "-");
            jsonObject.put("Field02", PDIRTXM_POSITION_ATTRIBUT);
            jsonObject.put("Field03", CompanyAddress);
            jsonObject.put("Field04", CompanyLegalName);
            jsonObject.put("Field05", PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, ""));
            jsonObject.put("Field06", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, ""));
            jsonObject.put("Field07", PGBORTM_PERSONAL_DATA);
            jsonObject.put("Field08", PGBDATM_PERSONAL_DATA);
            jsonObject.put("Field09", AreaAddress);
            jsonObject.put("Field10", CompanyLegalName_Companies_Requestor);
            jsonObject.put("Field11", HiringDate);
            jsonObject.put("Field12", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLTXTM, ""));
            jsonObject.put("Field13", PDIRTXM_POSITION_ATTRIBUT);
            jsonObject.put("Field14", CompanyLegalName_Companies_Requestor);
            jsonObject.put("Field15", new SimpleDateFormat("d MMMM yyyy").format(Calendar.getInstance().getTime()));
            jsonObject.put("Field16", PrefUtils.Build(context).getPref().getString(Constants.KEY_PBUKRSM, "").equals("1010") ? "Direktorat " + PDIRTXM : "SDM " + CompanyLegalName);
            jsonObject.put("Field17", PPLSTXM_POSITION_ATTRIBUTE);
            jsonObject.put("Field18", PCNAMEM_EMPLOYEE_HEADER);
            jsonObject.put("Field19", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, ""));
            jsonObject.put("Field20", PBEGDAM_CURRENT_ORGANIZATIONAL_ASSSIGNMENT);
            jsonObject.put("Field21", PBUTXTM_COMPANY_CODE);
            jsonObject.put("Field22", "-");
            jsonObject.put("Field23", "-");
            jsonObject.put("Field24", "-");
            jsonObject.put("Field25", "-");
            jsonObject.put("Field26", "-");
            jsonObject.put("Field27", "-");
            jsonObject.put("Field28", "-");
            jsonObject.put("Field29", "-");
            jsonObject.put("Field30", "-");
            jsonObject.put("Field31", "-");
            jsonObject.put("Field32", "-");
            jsonObject.put("Field33", "-");
            jsonObject.put("Field34", "-");
            jsonObject.put("Field35", "-");
            jsonObject.put("Field36", "-");
            jsonObject.put("Field37", "-");
            jsonObject.put("Field38", "-");
            jsonObject.put("Field39", "-");
            jsonObject.put("Field40", "-");
            jsonObject.put("Field41", "-");
            jsonObject.put("Field42", "-");
            jsonObject.put("Field43", "-");
            jsonObject.put("Field44", "-");
            jsonObject.put("Field45", "-");
            jsonObject.put("Image01", "TTD_00" + PPERNRM_EMPLOYEE_HEADER);
            jsonObject.put("Image02", "-");
            jsonObject.put("Image03", "-");
            jsonObject.put("Image04", "-");
            jsonObject.put("Image05", "-");
            jsonObject.put("ExecutorPERNR", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, ""));
            jsonObject.put("ExecutorADName", PrefUtils.Build(context).getPref().getString(Constants.KEY_USERNAME, ""));
            jsonObject.put("RequesterPERNR", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, ""));
            jsonObject.put("RequesterADName", PrefUtils.Build(context).getPref().getString(Constants.KEY_USERNAME, ""));
            jsonObject.put("Prefix", "Ket");
            jsonObject.put("Endda", new SimpleDateFormat("yyyyMMdd").format(calendar_one_month.getTime()));
            jsonObject.put("ApprovalPERNR", PPERNRM_EMPLOYEE_HEADER);
            return jsonObject.toString();
        } catch (Exception e) {
            return "";
        }
    }

    // surat keterangan visa

    public static String getXMLVisaDinas(Context context, String PDIRTXM, String CompanyLegalName, String docName, String docType, String lastComment,
                                         String PPERNRM_EMPLOYEE_HEADER, String destinationCountry, String destinationCity, String description, String embassyConsulate,
                                         String officialName, String address, String area, String startDate, String startDate_Text, String endDate, String endDate_Text, String passport,
                                         String fileName) {
        String Field19 = PrefUtils.Build(context).getPref().getString(Constants.KEY_PBUKRSM, "").equals("1010") ? "Directorate " : CompanyLegalName;
        String result = "";

        result += "<TemplateData>";
        result += "<TemplateDataRequest>";
        result += "<PersonID>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, "")  +"</PersonID>";
//        [PPERNRM] token login + [teks Purpose yang dipilih] + [current YYYYMM] (tidak perlu Date) + [Country]
        result += "<DocName>"+ docName +"</DocName>";
        result += "<DocType>"+ docType +"</DocType>";
        result += "<TMT>"+ new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) +"</TMT>";
        result += "<TemplateName>"+ context.getResources().getStringArray(R.array.purpose_id)[1] +"</TemplateName>";
        result += "<RoleName></RoleName>";
        result += "<LastComment>"+ lastComment +"</LastComment>";

        result += "<DestinationCountry>"+ destinationCountry +"</DestinationCountry>";
        result += "<DestinationCity>"+ destinationCity +"</DestinationCity>";
        result += "<EmbassyConsulate>"+ embassyConsulate +"</EmbassyConsulate>";
        result += "<OfficialName>"+ officialName +"</OfficialName>";
        result += "<Address>"+ address +"</Address>";
        result += "<Area>"+ area +"</Area>";
        result += "<StartDate>"+ startDate +"</StartDate>";
        result += "<StartDate_Text>"+ startDate_Text +"</StartDate_Text>";
        result += "<EndDate>"+ endDate +"</EndDate>";
        result += "<EndDate_Text>"+ endDate_Text +"</EndDate_Text>";
        result += "<Passport>"+ passport +"</Passport>";
        result += "<Description>"+ description +"</Description>";
        result += "<FileName>"+ fileName +"</FileName>";
        result += "<FileUrl>-</FileUrl>";
        result += "<CitySigner>-</CitySigner>";

        result += "<PCNAMEM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, "") +"</PCNAMEM>";
        result += "<PPERNRM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, "") +"</PPERNRM>";
        result += "<PBUKRSM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PBUKRSM, "") +"</PBUKRSM>";
        result += "<PPLTXTM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLTXTM, "") +"</PPLTXTM>";
        result += "<PPLANSM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLANSM, "") +"</PPLANSM>";

        result += "<CompanyName>"+ CompanyLegalName +"</CompanyName>";

        result += "<PCNAMEM_Signer>-</PCNAMEM_Signer>";
        result += "<PPERNRM_Signer>-</PPERNRM_Signer>";
        result += "<PPLANSM_Signer>-</PPLANSM_Signer>";
        result += "<PPLSTXM_Signer>-</PPLSTXM_Signer>";

        result += "<AdditionalName1></AdditionalName1>";
        result += "<AdditionalRelationship1></AdditionalRelationship1>";
        result += "<AdditionalPassport1></AdditionalPassport1>";

        result += "<AdditionalName2></AdditionalName2>";
        result += "<AdditionalRelationship2></AdditionalRelationship2>";
        result += "<AdditionalPassport2></AdditionalPassport2>";

        result += "<AdditionalName3></AdditionalName3>";
        result += "<AdditionalRelationship3></AdditionalRelationship3>";
        result += "<AdditionalPassport3></AdditionalPassport3>";

        result += "<AdditionalName4></AdditionalName4>";
        result += "<AdditionalRelationship4></AdditionalRelationship4>";
        result += "<AdditionalPassport4></AdditionalPassport4>";

        result += "<AdditionalName5></AdditionalName5>";
        result += "<AdditionalRelationship5></AdditionalRelationship5>";
        result += "<AdditionalPassport5></AdditionalPassport5>";

        result += "<Field01>-</Field01>";
        result += "<Field02>-</Field02>";
        result += "<Field03>"+ new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH).format(Calendar.getInstance().getTime()) +"</Field03>";
        result += "<Field04>"+ officialName +"</Field04>";
        result += "<Field05>"+ address +"</Field05>";
        result += "<Field06>"+ area +"</Field06>";
        result += "<Field07>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, "") +"</Field07>";
        result += "<Field08>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLTXTM, "") +"</Field08>";
        result += "<Field09>"+ passport +"</Field09>";
        result += "<Field10>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, "") +"</Field10>";
        result += "<Field11>"+ CompanyLegalName +"</Field11>";
        result += "<Field12>"+ destinationCity +"</Field12>";
        result += "<Field13>"+ destinationCountry +"</Field13>";
        result += "<Field14>"+ startDate +"</Field14>";
        result += "<Field15>"+ endDate +"</Field15>";
        result += "<Field16>"+ CompanyLegalName +"</Field16>";
        result += "<Field17>-</Field17>";
        result += "<Field18>-</Field18>";
        result += "<Field19>"+ Field19 +"</Field19>";
//        result += "<Field20>"+ Field20 +"</Field20>";
//        result += "<Field21>"+ Field21 +"</Field21>";
//        result += "<Field22>"+ Field22 +"</Field22>";
//        result += "<Field23>"+ Field23 +"</Field23>";
//        result += "<Field24>"+ Field24 +"</Field24>";
//        result += "<Field25>"+ Field25 +"</Field25>";
//        result += "<Field26>"+ Field26 +"</Field26>";
//        result += "<Field27>"+ Field27 +"</Field27>";
//        result += "<Field28>"+ Field28 +"</Field28>";
//        result += "<Field29>"+ Field29 +"</Field29>";
//        result += "<Field30>"+ Field30 +"</Field30>";
//        result += "<Field31>"+ Field31 +"</Field31>";
//        result += "<Field32>"+ Field32 +"</Field32>";
//        result += "<Field33>"+ Field33 +"</Field33>";
//        result += "<Field34>"+ Field34 +"</Field34>";
//        result += "<Field35>"+ Field35 +"</Field35>";
//        result += "<Field36>"+ Field36 +"</Field36>";
//        result += "<Field37>"+ Field37 +"</Field37>";
//        result += "<Field38>"+ Field38 +"</Field38>";
//        result += "<Field39>"+ Field39 +"</Field39>";
//        result += "<Field40>"+ Field40 +"</Field40>";
//        result += "<Field41>"+ Field41 +"</Field41>";
//        result += "<Field42>"+ Field42 +"</Field42>";
//        result += "<Field43>"+ Field43 +"</Field43>";
//        result += "<Field44>"+ Field44 +"</Field44>";
//        result += "<Field45>"+ Field45 +"</Field45>";
        result += "<Image01>TTD_00" + PPERNRM_EMPLOYEE_HEADER +"</Image01>";
        result += "<Prefix>Ket</Prefix>";

        result += "</TemplateDataRequest>";
        result += "</TemplateData>";
        return result;
    }

    public static String getK2FieldInJsonVisaDinas(Context context, String DocName, String DocType, String comment, String PDIRTXM,
                                                   String CompanyLegalName, String officialName, String address, String area, String passport, String destinationCity, String destinationCountry,
                                                   String startDate, String endDate) {
        try {
            String result = "";

            Locale id = new Locale("in", "ID");
            Calendar calendar_one_month = Calendar.getInstance();
            calendar_one_month.add(Calendar.MONTH, 1);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("PersonID", PrefUtils.Build(context).getPref().getString(Constants.KEY_PERSONAL_NUM, ""));
            jsonObject.put("DocName", DocName);
            jsonObject.put("DocType", DocType);
            jsonObject.put("TMT", new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
            jsonObject.put("TemplateName", context.getResources().getStringArray(R.array.purpose_id)[1]);
            jsonObject.put("RoleName", "SSC_SKETVISA");
            jsonObject.put("LastComment", comment);
            jsonObject.put("Field01", "-");
            jsonObject.put("Field02", "-");
            jsonObject.put("Field03", new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH).format(Calendar.getInstance().getTime()));
            jsonObject.put("Field04", officialName);
            jsonObject.put("Field05", address);
            jsonObject.put("Field06", area);
            jsonObject.put("Field07", PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, ""));
            jsonObject.put("Field08", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLTXTM, ""));
            jsonObject.put("Field09", passport);
            jsonObject.put("Field10", PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, ""));
            jsonObject.put("Field11", CompanyLegalName);
            jsonObject.put("Field12", destinationCity);
            jsonObject.put("Field13", destinationCountry);
            jsonObject.put("Field14", startDate);
            jsonObject.put("Field15", endDate);
            jsonObject.put("Field16", CompanyLegalName);
            jsonObject.put("Field17", "-");
            jsonObject.put("Field18", "-");
            jsonObject.put("Field19", PrefUtils.Build(context).getPref().getString(Constants.KEY_PBUKRSM, "").equals("1010") ? "Directorate " : CompanyLegalName);
            jsonObject.put("Field20", "-");
            jsonObject.put("Field21", "-");
            jsonObject.put("Field22", "-");
            jsonObject.put("Field23", "-");
            jsonObject.put("Field24", "-");
            jsonObject.put("Field25", "-");
            jsonObject.put("Field26", "-");
            jsonObject.put("Field27", "-");
            jsonObject.put("Field28", "-");
            jsonObject.put("Field29", "-");
            jsonObject.put("Field30", "-");
            jsonObject.put("Field31", "-");
            jsonObject.put("Field32", "-");
            jsonObject.put("Field33", "-");
            jsonObject.put("Field34", "-");
            jsonObject.put("Field35", "-");
            jsonObject.put("Field36", "-");
            jsonObject.put("Field37", "-");
            jsonObject.put("Field38", "-");
            jsonObject.put("Field39", "-");
            jsonObject.put("Field40", "-");
            jsonObject.put("Field41", "-");
            jsonObject.put("Field42", "-");
            jsonObject.put("Field43", "-");
            jsonObject.put("Field44", "-");
            jsonObject.put("Field45", "-");
            jsonObject.put("Image01", "-");
            jsonObject.put("Image02", "-");
            jsonObject.put("Image03", "-");
            jsonObject.put("Image04", "-");
            jsonObject.put("Image05", "-");
            jsonObject.put("ExecutorPERNR", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, ""));
            jsonObject.put("ExecutorADName", PrefUtils.Build(context).getPref().getString(Constants.KEY_USERNAME, ""));
            jsonObject.put("RequesterPERNR", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, ""));
            jsonObject.put("RequesterADName", PrefUtils.Build(context).getPref().getString(Constants.KEY_USERNAME, ""));
            jsonObject.put("Prefix", "Ket");
            jsonObject.put("Endda", new SimpleDateFormat("yyyyMMdd").format(calendar_one_month.getTime()));
            return jsonObject.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getXMLVisaPribadi(Context context, String PDIRTXM, String CompanyLegalName, String docName, String docType, String lastComment,
                                         String PPERNRM_EMPLOYEE_HEADER, String destinationCountry, String destinationCity, String description, String embassyConsulate,
                                         String officialName, String address, String area, String startDate, String startDate_Text, String endDate, String endDate_Text, String passport,
                                         String fileName) {
        String Field19 = PrefUtils.Build(context).getPref().getString(Constants.KEY_PBUKRSM, "").equals("1010") ? "Directorate " : CompanyLegalName;
        String result = "";

        result += "<TemplateData>";
        result += "<TemplateDataRequest>";
        result += "<PersonID>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, "")  +"</PersonID>";
        result += "<DocName>"+ docName +"</DocName>";
        result += "<DocType>"+ docType +"</DocType>";
        result += "<TMT>"+ new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) +"</TMT>";
        result += "<TemplateName>"+ context.getResources().getStringArray(R.array.purpose_id)[2] +"</TemplateName>";
        result += "<RoleName></RoleName>";
        result += "<LastComment>"+ lastComment +"</LastComment>";

        result += "<DestinationCountry>"+ destinationCountry +"</DestinationCountry>";
        result += "<DestinationCity>"+ destinationCity +"</DestinationCity>";
        result += "<EmbassyConsulate>"+ embassyConsulate +"</EmbassyConsulate>";
        result += "<OfficialName>"+ officialName +"</OfficialName>";
        result += "<Address>"+ address +"</Address>";
        result += "<Area>"+ area +"</Area>";
        result += "<StartDate>"+ startDate +"</StartDate>";
        result += "<StartDate_Text>"+ startDate_Text +"</StartDate_Text>";
        result += "<EndDate>"+ endDate +"</EndDate>";
        result += "<EndDate_Text>"+ endDate_Text +"</EndDate_Text>";
        result += "<Passport>"+ passport +"</Passport>";
        result += "<Description>"+ description +"</Description>";
        result += "<FileName>"+ fileName +"</FileName>";
        result += "<FileUrl>-</FileUrl>";
        result += "<CitySigner>-</CitySigner>";

        result += "<PCNAMEM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, "") +"</PCNAMEM>";
        result += "<PPERNRM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, "") +"</PPERNRM>";
        result += "<PBUKRSM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PBUKRSM, "") +"</PBUKRSM>";
        result += "<PPLTXTM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLTXTM, "") +"</PPLTXTM>";
        result += "<PPLANSM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLANSM, "") +"</PPLANSM>";

        result += "<CompanyName>"+ CompanyLegalName +"</CompanyName>";

        result += "<PCNAMEM_Signer>-</PCNAMEM_Signer>";
        result += "<PPERNRM_Signer>-</PPERNRM_Signer>";
        result += "<PPLANSM_Signer>-</PPLANSM_Signer>";
        result += "<PPLSTXM_Signer>-</PPLSTXM_Signer>";

        result += "<AdditionalName1></AdditionalName1>";
        result += "<AdditionalRelationship1></AdditionalRelationship1>";
        result += "<AdditionalPassport1></AdditionalPassport1>";

        result += "<AdditionalName2></AdditionalName2>";
        result += "<AdditionalRelationship2></AdditionalRelationship2>";
        result += "<AdditionalPassport2></AdditionalPassport2>";

        result += "<AdditionalName3></AdditionalName3>";
        result += "<AdditionalRelationship3></AdditionalRelationship3>";
        result += "<AdditionalPassport3></AdditionalPassport3>";

        result += "<AdditionalName4></AdditionalName4>";
        result += "<AdditionalRelationship4></AdditionalRelationship4>";
        result += "<AdditionalPassport4></AdditionalPassport4>";

        result += "<AdditionalName5></AdditionalName5>";
        result += "<AdditionalRelationship5></AdditionalRelationship5>";
        result += "<AdditionalPassport5></AdditionalPassport5>";

        result += "<Field01>-</Field01>";
        result += "<Field02>-</Field02>";
        result += "<Field03>"+ new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH).format(Calendar.getInstance().getTime()) +"</Field03>";
        result += "<Field04>"+ officialName +"</Field04>";
        result += "<Field05>"+ address +"</Field05>";
        result += "<Field06>"+ area +"</Field06>";
        result += "<Field07>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, "") +"</Field07>";
        result += "<Field08>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLTXTM, "") +"</Field08>";
        result += "<Field09>"+ passport +"</Field09>";
        result += "<Field10>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, "") +"</Field10>";
        result += "<Field11>"+ CompanyLegalName +"</Field11>";
        result += "<Field12>"+ destinationCity +"</Field12>";
        result += "<Field13>"+ destinationCountry +"</Field13>";
        result += "<Field14>"+ startDate +"</Field14>";
        result += "<Field15>"+ endDate +"</Field15>";
        result += "<Field16>"+ CompanyLegalName +"</Field16>";
        result += "<Field17>-</Field17>";
        result += "<Field18>-</Field18>";
        result += "<Field19>"+ Field19 +"</Field19>";
//        result += "<Field20>"+ Field20 +"</Field20>";
//        result += "<Field21>"+ Field21 +"</Field21>";
//        result += "<Field22>"+ Field22 +"</Field22>";
//        result += "<Field23>"+ Field23 +"</Field23>";
//        result += "<Field24>"+ Field24 +"</Field24>";
//        result += "<Field25>"+ Field25 +"</Field25>";
//        result += "<Field26>"+ Field26 +"</Field26>";
//        result += "<Field27>"+ Field27 +"</Field27>";
//        result += "<Field28>"+ Field28 +"</Field28>";
//        result += "<Field29>"+ Field29 +"</Field29>";
//        result += "<Field30>"+ Field30 +"</Field30>";
//        result += "<Field31>"+ Field31 +"</Field31>";
//        result += "<Field32>"+ Field32 +"</Field32>";
//        result += "<Field33>"+ Field33 +"</Field33>";
//        result += "<Field34>"+ Field34 +"</Field34>";
//        result += "<Field35>"+ Field35 +"</Field35>";
//        result += "<Field36>"+ Field36 +"</Field36>";
//        result += "<Field37>"+ Field37 +"</Field37>";
//        result += "<Field38>"+ Field38 +"</Field38>";
//        result += "<Field39>"+ Field39 +"</Field39>";
//        result += "<Field40>"+ Field40 +"</Field40>";
//        result += "<Field41>"+ Field41 +"</Field41>";
//        result += "<Field42>"+ Field42 +"</Field42>";
//        result += "<Field43>"+ Field43 +"</Field43>";
//        result += "<Field44>"+ Field44 +"</Field44>";
//        result += "<Field45>"+ Field45 +"</Field45>";
        result += "<Image01>TTD_00" + PPERNRM_EMPLOYEE_HEADER +"</Image01>";
        result += "<Prefix>Ket</Prefix>";

        result += "</TemplateDataRequest>";
        result += "</TemplateData>";
        return result;
    }

    public static String getK2FieldInJsonPribadi(Context context, String DocName, String DocType, String comment, String PDIRTXM,
                                                   String CompanyLegalName, String officialName, String address, String area, String passport, String destinationCity, String destinationCountry,
                                                   String startDate, String endDate) {
        try {
            String result = "";

            Locale id = new Locale("in", "ID");
            Calendar calendar_one_month = Calendar.getInstance();
            calendar_one_month.add(Calendar.MONTH, 1);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("PersonID", PrefUtils.Build(context).getPref().getString(Constants.KEY_PERSONAL_NUM, ""));
            jsonObject.put("DocName", DocName);
            jsonObject.put("DocType", DocType);
            jsonObject.put("TMT", new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
            jsonObject.put("TemplateName", context.getResources().getStringArray(R.array.purpose_id)[2]);
            jsonObject.put("RoleName", "SSC_SKETVISA");
            jsonObject.put("LastComment", comment);
            jsonObject.put("Field01", "-");
            jsonObject.put("Field02", "-");
            jsonObject.put("Field03", new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH).format(Calendar.getInstance().getTime()));
            jsonObject.put("Field04", officialName);
            jsonObject.put("Field05", address);
            jsonObject.put("Field06", area);
            jsonObject.put("Field07", PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, ""));
            jsonObject.put("Field08", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLTXTM, ""));
            jsonObject.put("Field09", passport);
            jsonObject.put("Field10", PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, ""));
            jsonObject.put("Field11", CompanyLegalName);
            jsonObject.put("Field12", destinationCity);
            jsonObject.put("Field13", destinationCountry);
            jsonObject.put("Field14", startDate);
            jsonObject.put("Field15", endDate);
            jsonObject.put("Field16", "-");
            jsonObject.put("Field17", "-");
            jsonObject.put("Field18", PrefUtils.Build(context).getPref().getString(Constants.KEY_PBUKRSM, "").equals("1010") ? "Directorate " : CompanyLegalName);
            jsonObject.put("Field19", "-");
            jsonObject.put("Field20", "-");
            jsonObject.put("Field21", "-");
            jsonObject.put("Field22", "-");
            jsonObject.put("Field23", "-");
            jsonObject.put("Field24", "-");
            jsonObject.put("Field25", "-");
            jsonObject.put("Field26", "-");
            jsonObject.put("Field27", "-");
            jsonObject.put("Field28", "-");
            jsonObject.put("Field29", "-");
            jsonObject.put("Field30", "-");
            jsonObject.put("Field31", "-");
            jsonObject.put("Field32", "-");
            jsonObject.put("Field33", "-");
            jsonObject.put("Field34", "-");
            jsonObject.put("Field35", "-");
            jsonObject.put("Field36", "-");
            jsonObject.put("Field37", "-");
            jsonObject.put("Field38", "-");
            jsonObject.put("Field39", "-");
            jsonObject.put("Field40", "-");
            jsonObject.put("Field41", "-");
            jsonObject.put("Field42", "-");
            jsonObject.put("Field43", "-");
            jsonObject.put("Field44", "-");
            jsonObject.put("Field45", "-");
            jsonObject.put("Image01", "-");
            jsonObject.put("Image02", "-");
            jsonObject.put("Image03", "-");
            jsonObject.put("Image04", "-");
            jsonObject.put("Image05", "-");
            jsonObject.put("ExecutorPERNR", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, ""));
            jsonObject.put("ExecutorADName", PrefUtils.Build(context).getPref().getString(Constants.KEY_USERNAME, ""));
            jsonObject.put("RequesterPERNR", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, ""));
            jsonObject.put("RequesterADName", PrefUtils.Build(context).getPref().getString(Constants.KEY_USERNAME, ""));
            jsonObject.put("Prefix", "Ket");
            jsonObject.put("Endda", new SimpleDateFormat("yyyyMMdd").format(calendar_one_month.getTime()));
            return jsonObject.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getXMLVisaPribadiKeluarga(Context context, String PDIRTXM, String CompanyLegalName, String docName, String docType, String lastComment,
                                           String PPERNRM_EMPLOYEE_HEADER, String destinationCountry, String destinationCity, String description, String embassyConsulate,
                                           String officialName, String address, String area, String startDate, String startDate_Text, String endDate, String endDate_Text, String passport,
                                           String fileName, String additionalName1, String additionalRelationship1, String additionalPassport1,
                                           String additionalName2, String additionalRelationship2, String additionalPassport2,
                                                   String additionalName3, String additionalRelationship3, String additionalPassport3,
                                                   String additionalName4, String additionalRelationship4, String additionalPassport4,
                                                   String additionalName5, String additionalRelationship5, String additionalPassport5) {
        String Field19 = PrefUtils.Build(context).getPref().getString(Constants.KEY_PBUKRSM, "").equals("1010") ? "Directorate " : CompanyLegalName;
        String result = "";

        result += "<TemplateData>";
        result += "<TemplateDataRequest>";
        result += "<PersonID>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, "")  +"</PersonID>";
        result += "<DocName>"+ docName +"</DocName>";
        result += "<DocType>"+ docType +"</DocType>";
        result += "<TMT>"+ new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) +"</TMT>";
        result += "<TemplateName>"+ context.getResources().getStringArray(R.array.purpose_id)[3] +"</TemplateName>";
        result += "<RoleName></RoleName>";
        result += "<LastComment>"+ lastComment +"</LastComment>";

        result += "<DestinationCountry>"+ destinationCountry +"</DestinationCountry>";
        result += "<DestinationCity>"+ destinationCity +"</DestinationCity>";
        result += "<EmbassyConsulate>"+ embassyConsulate +"</EmbassyConsulate>";
        result += "<OfficialName>"+ officialName +"</OfficialName>";
        result += "<Address>"+ address +"</Address>";
        result += "<Area>"+ area +"</Area>";
        result += "<StartDate>"+ startDate +"</StartDate>";
        result += "<StartDate_Text>"+ startDate_Text +"</StartDate_Text>";
        result += "<EndDate>"+ endDate +"</EndDate>";
        result += "<EndDate_Text>"+ endDate_Text +"</EndDate_Text>";
        result += "<Passport>"+ passport +"</Passport>";
        result += "<Description>"+ description +"</Description>";
        result += "<FileName>"+ fileName +"</FileName>";
        result += "<FileUrl>-</FileUrl>";
        result += "<CitySigner>-</CitySigner>";

        result += "<PCNAMEM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, "") +"</PCNAMEM>";
        result += "<PPERNRM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, "") +"</PPERNRM>";
        result += "<PBUKRSM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PBUKRSM, "") +"</PBUKRSM>";
        result += "<PPLTXTM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLTXTM, "") +"</PPLTXTM>";
        result += "<PPLANSM>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLANSM, "") +"</PPLANSM>";

        result += "<CompanyName>"+ CompanyLegalName +"</CompanyName>";

        result += "<PCNAMEM_Signer>-</PCNAMEM_Signer>";
        result += "<PPERNRM_Signer>-</PPERNRM_Signer>";
        result += "<PPLANSM_Signer>-</PPLANSM_Signer>";
        result += "<PPLSTXM_Signer>-</PPLSTXM_Signer>";

        result += "<AdditionalName1>" + additionalName1 + "</AdditionalName1>";
        result += "<AdditionalRelationship1>" + additionalRelationship1 + "</AdditionalRelationship1>";
        result += "<AdditionalPassport1>" + additionalPassport1 + "</AdditionalPassport1>";

        result += "<AdditionalName2>" + additionalName2 + "</AdditionalName2>";
        result += "<AdditionalRelationship2>" + additionalRelationship2 + "</AdditionalRelationship2>";
        result += "<AdditionalPassport2>" + additionalPassport2 + "</AdditionalPassport2>";

        result += "<AdditionalName3>" + additionalName3 + "</AdditionalName3>";
        result += "<AdditionalRelationship3>" + additionalRelationship1 + "</AdditionalRelationship3>";
        result += "<AdditionalPassport3>" + additionalPassport3 + "</AdditionalPassport3>";

        result += "<AdditionalName4>" + additionalName4 + "</AdditionalName4>";
        result += "<AdditionalRelationship4>" + additionalRelationship4 + "</AdditionalRelationship4>";
        result += "<AdditionalPassport4>" + additionalPassport4 + "</AdditionalPassport4>";

        result += "<AdditionalName5>" + additionalName5 + "</AdditionalName5>";
        result += "<AdditionalRelationship5>" + additionalRelationship5 + "</AdditionalRelationship5>";
        result += "<AdditionalPassport5>" + additionalPassport5 + "</AdditionalPassport5>";

        result += "<Field01>-</Field01>";
        result += "<Field02>-</Field02>";
        result += "<Field03>"+ new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH).format(Calendar.getInstance().getTime()) +"</Field03>";
        result += "<Field04>"+ officialName +"</Field04>";
        result += "<Field05>"+ address +"</Field05>";
        result += "<Field06>"+ area +"</Field06>";
        result += "<Field07>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, "") +"</Field07>";
        result += "<Field08>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLTXTM, "") +"</Field08>";
        result += "<Field09>"+ passport +"</Field09>";
        result += "<Field10>"+ PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, "") +"</Field10>";
        result += "<Field11>"+ CompanyLegalName +"</Field11>";
        result += "<Field12>"+ destinationCity +"</Field12>";
        result += "<Field13>"+ destinationCountry +"</Field13>";
        result += "<Field14>"+ startDate +"</Field14>";
        result += "<Field15>"+ endDate +"</Field15>";
        result += "<Field16>-</Field16>";
        result += "<Field17>-</Field17>";
        result += "<Field18>" + additionalName1 + "</Field18>";
        result += "<Field19>" + additionalRelationship1 + "</Field19>";
        result += "<Field20>" + additionalPassport1 + "</Field20>";
        result += "<Field21>" + additionalName2 + "</Field21>";
        result += "<Field22>" + additionalRelationship2 + "</Field22>";
        result += "<Field23>" + additionalPassport2 + "</Field23>";
        result += "<Field24>" + additionalName3 + "</Field24>";
        result += "<Field25>" + additionalRelationship3 + "</Field25>";
        result += "<Field26>" + additionalPassport3 + "</Field26>";
        result += "<Field27>" + additionalName4 + "</Field27>";
        result += "<Field28>" + additionalRelationship4 + "</Field28>";
        result += "<Field29>" + additionalPassport4 + "</Field29>";
        result += "<Field30>" + additionalName5 + "</Field30>";
        result += "<Field31>" + additionalRelationship5 + "</Field31>";
        result += "<Field32>" + additionalPassport5 + "</Field32>";
        result += "<Field33>"+ Field19 +"</Field33>";
//        result += "<Field34>"+ Field34 +"</Field34>";
//        result += "<Field35>"+ Field35 +"</Field35>";
//        result += "<Field36>"+ Field36 +"</Field36>";
//        result += "<Field37>"+ Field37 +"</Field37>";
//        result += "<Field38>"+ Field38 +"</Field38>";
//        result += "<Field39>"+ Field39 +"</Field39>";
//        result += "<Field40>"+ Field40 +"</Field40>";
//        result += "<Field41>"+ Field41 +"</Field41>";
//        result += "<Field42>"+ Field42 +"</Field42>";
//        result += "<Field43>"+ Field43 +"</Field43>";
//        result += "<Field44>"+ Field44 +"</Field44>";
//        result += "<Field45>"+ Field45 +"</Field45>";
        result += "<Image01>TTD_00" + PPERNRM_EMPLOYEE_HEADER +"</Image01>";
        result += "<Prefix>Ket</Prefix>";

        result += "</TemplateDataRequest>";
        result += "</TemplateData>";
        return result;
    }

    public static String getK2FieldInJsonPribadiKeluarga(Context context, String DocName, String DocType, String comment, String PDIRTXM,
                                                 String CompanyLegalName, String officialName, String address, String area, String passport, String destinationCity, String destinationCountry,
                                                 String startDate, String endDate, String additionalName1, String additionalRelationship1, String additionalPassport1,
                                                         String additionalName2, String additionalRelationship2, String additionalPassport2,
                                                         String additionalName3, String additionalRelationship3, String additionalPassport3,
                                                         String additionalName4, String additionalRelationship4, String additionalPassport4,
                                                         String additionalName5, String additionalRelationship5, String additionalPassport5) {
        try {
            String result = "";

            Locale id = new Locale("in", "ID");
            Calendar calendar_one_month = Calendar.getInstance();
            calendar_one_month.add(Calendar.MONTH, 1);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("PersonID", PrefUtils.Build(context).getPref().getString(Constants.KEY_PERSONAL_NUM, ""));
            jsonObject.put("DocName", DocName);
            jsonObject.put("DocType", DocType);
            jsonObject.put("TMT", new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
            jsonObject.put("TemplateName", context.getResources().getStringArray(R.array.purpose_id)[3]);
            jsonObject.put("RoleName", "SSC_SKETVISA");
            jsonObject.put("LastComment", comment);
            jsonObject.put("Field01", "-");
            jsonObject.put("Field02", "-");
            jsonObject.put("Field03", new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH).format(Calendar.getInstance().getTime()));
            jsonObject.put("Field04", officialName);
            jsonObject.put("Field05", address);
            jsonObject.put("Field06", area);
            jsonObject.put("Field07", PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, ""));
            jsonObject.put("Field08", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPLTXTM, ""));
            jsonObject.put("Field09", passport);
            jsonObject.put("Field10", PrefUtils.Build(context).getPref().getString(Constants.KEY_PCNAMEM, ""));
            jsonObject.put("Field11", CompanyLegalName);
            jsonObject.put("Field12", destinationCity);
            jsonObject.put("Field13", destinationCountry);
            jsonObject.put("Field14", startDate);
            jsonObject.put("Field15", endDate);
            jsonObject.put("Field16", "-");
            jsonObject.put("Field17", "-");
            jsonObject.put("Field18", additionalName1);
            jsonObject.put("Field19", additionalRelationship1);
            jsonObject.put("Field20", additionalPassport1);
            jsonObject.put("Field21", additionalName2);
            jsonObject.put("Field22", additionalRelationship2);
            jsonObject.put("Field23", additionalPassport2);
            jsonObject.put("Field24", additionalName3);
            jsonObject.put("Field25", additionalRelationship3);
            jsonObject.put("Field26", additionalPassport3);
            jsonObject.put("Field27", additionalName4);
            jsonObject.put("Field28", additionalRelationship4);
            jsonObject.put("Field29", additionalPassport4);
            jsonObject.put("Field30", additionalName5);
            jsonObject.put("Field31", additionalRelationship5);
            jsonObject.put("Field32", additionalPassport5);
            jsonObject.put("Field33", PrefUtils.Build(context).getPref().getString(Constants.KEY_PBUKRSM, "").equals("1010") ? "Directorate " : CompanyLegalName);
            jsonObject.put("Field34", "-");
            jsonObject.put("Field35", "-");
            jsonObject.put("Field36", "-");
            jsonObject.put("Field37", "-");
            jsonObject.put("Field38", "-");
            jsonObject.put("Field39", "-");
            jsonObject.put("Field40", "-");
            jsonObject.put("Field41", "-");
            jsonObject.put("Field42", "-");
            jsonObject.put("Field43", "-");
            jsonObject.put("Field44", "-");
            jsonObject.put("Field45", "-");
            jsonObject.put("Image01", "-");
            jsonObject.put("Image02", "-");
            jsonObject.put("Image03", "-");
            jsonObject.put("Image04", "-");
            jsonObject.put("Image05", "-");
            jsonObject.put("ExecutorPERNR", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, ""));
            jsonObject.put("ExecutorADName", PrefUtils.Build(context).getPref().getString(Constants.KEY_USERNAME, ""));
            jsonObject.put("RequesterPERNR", PrefUtils.Build(context).getPref().getString(Constants.KEY_PPERNRM, ""));
            jsonObject.put("RequesterADName", PrefUtils.Build(context).getPref().getString(Constants.KEY_USERNAME, ""));
            jsonObject.put("Prefix", "Ket");
            jsonObject.put("Endda", new SimpleDateFormat("yyyyMMdd").format(calendar_one_month.getTime()));
            return jsonObject.toString();
        } catch (Exception e) {
            return "";
        }
    }
}

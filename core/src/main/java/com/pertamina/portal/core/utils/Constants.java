package com.pertamina.portal.core.utils;

import com.pertamina.portal.core.R;

public class Constants {

    public static final String BASE_URL = "https://apps.pertamina.com/";
    public static final String APP_NAME = "pertamina-portal";
    public static final String KEY_ACCES_TOKEN = "access_token";
    public static final String KEY_REFRESH_TOKEN = "refresh_token";
    public static final String KEY_AUTH_DATA = "auth_data";
    public static final String KEY_PHONE_NUM = "phone_num";
    public static final String SALT = "nj37T|*5a,1LUyvT2,9l1L4?o*!5{|zz";
    public static final String KEY_PERSONAL_NUM = "personal_number";
    public static final String KEY_AUTH_STEP = "auth-step";
    public static final String KEY_PROCESS_INSTANCE = "process-id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PCNAMEM = "pcnamem";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHOTO = "url_photo";
    public static final String KEY_K2SERIAL_NUM = "k2serial_number";
    public static final String KEY_K2ACTION = "action";
    public static final String APROVAL_APPROVE = "Approve";
    public static final String APROVAL_REJECT = "Reject";
    public static final String APROVAL_ASK_REVISE = "Ask to Revise";
    public static final String APROVAL_RETRY = "Retry";
    public static final String APROVAL_CONTINUE = "Continue";
    public static final String APROVAL_RESUBMIT = "Resubmit";
    public static final String APROVAL_CANCEL = "Cancel";
    public static final String APROVAL_SUBMIT = "Submit";
    public static final String APROVAL_COMPLETE = "Complete";
    public static final String KEY_PPLANSM = "PPLANSM";
    public static final String KEY_PBUKRSM = "PBUKRSM";
    public static final String KEY_PPERNRM = "PPERNRM";
    public static final String KEY_PBUTXTM = "PBUTXTM";
    public static final String KEY_PPLTXTM = "PPLTXTM";
    public static final String KEY_PERSON_ID = "PERSON_ID";
    public static final String KEY_KBO = "KBO";



    public static class EventColor {
        public static final String PUBLIC_HOLDY = "Public Holiday";
        public static final String LEAVE = "Leave";
        public static final String TRAINING = "Training";
        public static final String TRAVEL = "Travel";
        public static final String ABSENCES = "Absences";

        public static int getEventColor(String eventType) throws Exception {
            if (eventType.equalsIgnoreCase(PUBLIC_HOLDY)) {
                return R.color.iamRed;
            } else if (eventType.equalsIgnoreCase(LEAVE)) {
                return R.color.iamYelow;
            } else if (eventType.equalsIgnoreCase(TRAINING)) {
                return R.color.iamGreenLight;
            } else if (eventType.equalsIgnoreCase(TRAVEL)) {
                return R.color.iamBlueIcon;
            } else if (eventType.equalsIgnoreCase(ABSENCES)) {
                return R.color.iamYelow;
            } else {
                throw new Exception("Event type not found exception");
            }
        }
    }
}

package com.loits.insurance.cm.util;

/**
 * Created by DumindaW on 24/11/2016.
 */

public class Constants {

    public static final String IDS_USER_STORE_NAME = "LOLCINSU/";

    //Dev
    /*public static final String APP_ENGINE_BASE_URL = "http://mobi-insurance-dev.appspot.com/";
    public static final String SERVICES_BASE_URL = "https://203.189.65.69:8246/services/";
    public static final String IDS_TOKEN_PATH = "https://203.189.65.69:8005/";*/

    //QA
    public static final String APP_ENGINE_BASE_URL = "http://mobi-insurance-dev.appspot.com/";
    public static final String SERVICES_BASE_URL = "https://203.189.65.69:8447/services/";
    public static final String IDS_TOKEN_PATH = "https://203.189.65.69:8007/";

    //LIVE
    //public static final String IDS_CLIENT_ID = "aNnUwt4DDmtTEBCaQBdGCqP_sEka";
    //public static final String IDS_CLIENT_SECRET = "nIrFbrOkU8uePUSV31hB60WSVWka";

    //Dev
    //public static String  IDS_CLIENT_ID = "ruX10JIaEdEkoUYtG3yRciiqxs4a";
    //public static String  IDS_CLIENT_SECRET = "GiiPK3Dsf7fCjVb0FtUXLqJx6awa";

    //QA
    public static String  IDS_CLIENT_ID = "XH2tC7dWWWr9_Jx3yVoyvwAsD9Ua";
    public static String  IDS_CLIENT_SECRET = "nIrFbrOkU8uePUSV31hB60WSVWka";


    public static final String ACCOUNT_TYPE = "com.loits.insurance.cm";
    public static final String ACCOUNT_NAME = "Insurance Claim Manager";
    public static final String AUTHORITY = "com.loits.insurance.cm.provider.ca";

    public final static String KEY_REFRESH_TOKEN = "refresh_token";
    public final static String KEY_EXPIRE_TIME = "expires_in";
    public final static String KEY_USERNAME = "username";
    public final static String KEY_SCOPE = "iclaim";

    private static final long SECONDS_PER_MINUTE = 60L;
    private static final long SYNC_INTERVAL_IN_MINUTES = 1L;
    public static final long SYNC_INTERVAL = SYNC_INTERVAL_IN_MINUTES * SECONDS_PER_MINUTE;

    public static int SPLASH_TIME_OUT = 1000;
    public static int TIME_OUT = 20000;

    public static int MAX_IDLE_CONNECTIONS = 30;
    public static int KEEP_ALIVE_DURATION_MS = 3 * 60 * 1000;

    public static final int AUTHORIZATION_CODE = 1993;
    public static final int ACCOUNT_CODE = 1601;

    public static final int DATA_VALIDITY_DAYS = 3;

    public static final String KEY_ROWID 		= "_id";
    public static final String KEY_INTIM 		= "intimation_no";
    //public static final String KEY_CLAIM 		= "claim_no";
    public static final String KEY_SEQ 			= "seq_no";
    public static final String KEY_INSP_TYP		= "insp_type";
    public static final String KEY_POLICY 		= "policy_no";
    public static final String KEY_VEHICLE		= "vehicle_no";
    public static final String KEY_LOC 			= "location";
    public static final String KEY_CONT 		= "contact_no";
    public static final String KEY_CALLER		= "caller_name";
    public static final String KEY_YOM			= "yom";
    public static final String KEY_SUM_INS		= "sum_insured";
    public static final String KEY_EXCESS		= "excess_amt";
    public static final String KEY_FACILITY		= "comp_facility";
    public static final String KEY_SITE_OFFER	= "site_offer";
    public static final String KEY_INSP_DATE	= "insp_date";
    public static final String KEY_ACR			= "acr";
    //public static final String KEY_CLAIM_MIL	= "claim_mileage";
    /*public static final String KEY_ASS_MIL		= "assessor_mileage";*/
    public static final String KEY_CC_REM		= "call_center_remark";
    public static final String KEY_ASS_REM		= "assessor_remark";
    public static final String KEY_R_DATE 		= "respond_date";
    public static final String KEY_USER 		= "user";
    public static final String KEY_C_DATE 		= "c_date";
    public static final String KEY_STATUS 		= "status";
    public static final String KEY_SYNC_DATE 	= "sync_date";
    public static final String KEY_PRE_ACC_VAL  		= "pre_acc_val";
    public static final String KEY_BALD_TIRE_PENALTY 	= "bald_tire_penalty";
    public static final String KEY_UNDER_INS_PENALTY 	= "under_ins_penalty";
    public static final String KEY_INSP_REMARKS  		= "insp_remarks";
    public static final String KEY_SP_REMARKS			= "sp_remarks";
    public static final String KEY_PROFF_FEE 			= "proff_fee";
    public static final String KEY_MILES  				= "miles";
    public static final String KEY_RATE  				= "rate";
    public static final String KEY_TELEPHONE  			= "telephone";
    public static final String KEY_COPIES  				= "copies";
    public static final String KEY_PRICE  				= "price";
    public static final String KEY_PHOTO_CHARGES  		= "photo_charges";
    public static final String KEY_OTHER_CHARGES  		= "other_charges";
    public static final String KEY_TOTAL_CHARGES  		= "total_charges";
    public static final String KEY_REASON  				= "reason";
    public static final String KEY_POLICE_REPORT  		= "police_report";
    public static final String KEY_INVESTIGATE_CLAIM  	= "investigate_claim";
    public static final String KEY_PAYABLE_AMOUNT       = "payable_amont";
    public static final String KEY_ONSITE_OFFER_AMOUNT	= "onsite_offer_amount";
    public static final String KEY_REPAIR_COMPLETED		= "repair_completed";
    public static final String KEY_SALVAGE_RECEIVED		= "salvage_received";
    public static final String KEY_SETTLEMENT_METHOD	= "settlement_method";
    public static final String KEY_NO_OF_IMAGES	        = "images_count";
    public static final String KEY_TOTAL_IMAGES	        = "total_images";


    public static final String KEY_IMG_ROWID 	= "_id";
    public static final String KEY_IMG_INTIM	= "intimation_no";
    public static final String KEY_IMG_INSP_TYPE= "inspection_type";
    public static final String KEY_IMG_SEQ 		= "seq_no";
    public static final String KEY_IMG_DATE 	= "img_date";
    public static final String KEY_IMG_NO 		= "image_no";
    public static final String KEY_IMG 			= "image";
    public static final String KEY_LNGD 		= "longitude";
    public static final String KEY_LTTD 		= "latitude";
    public static final String KEY_IMG_STATUS 	= "status";

    public static final int VIEW_IMAGES = 1;
    public static final int SELECT_MULTY = 2;
    public static final int CAPTURE_IMAGE = 3;
    public static final int KEY_CHECK_GPS = 4;

    public static final int NO_OF_RETRIES = 3;

    public static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 1234;

}

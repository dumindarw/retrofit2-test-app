package com.loits.insurance.cm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.loits.insurance.cm.models.Claim;
import com.loits.insurance.cm.models.Image;

import java.util.ArrayList;
import java.util.List;

import static com.loits.insurance.cm.util.Constants.KEY_ACR;
import static com.loits.insurance.cm.util.Constants.KEY_ASS_REM;
import static com.loits.insurance.cm.util.Constants.KEY_BALD_TIRE_PENALTY;
import static com.loits.insurance.cm.util.Constants.KEY_CALLER;
import static com.loits.insurance.cm.util.Constants.KEY_CC_REM;
import static com.loits.insurance.cm.util.Constants.KEY_CONT;
import static com.loits.insurance.cm.util.Constants.KEY_COPIES;
import static com.loits.insurance.cm.util.Constants.KEY_C_DATE;
import static com.loits.insurance.cm.util.Constants.KEY_EXCESS;
import static com.loits.insurance.cm.util.Constants.KEY_FACILITY;
import static com.loits.insurance.cm.util.Constants.KEY_IMG;
import static com.loits.insurance.cm.util.Constants.KEY_IMG_DATE;
import static com.loits.insurance.cm.util.Constants.KEY_IMG_INSP_TYPE;
import static com.loits.insurance.cm.util.Constants.KEY_IMG_INTIM;
import static com.loits.insurance.cm.util.Constants.KEY_IMG_NO;
import static com.loits.insurance.cm.util.Constants.KEY_IMG_ROWID;
import static com.loits.insurance.cm.util.Constants.KEY_IMG_SEQ;
import static com.loits.insurance.cm.util.Constants.KEY_IMG_STATUS;
import static com.loits.insurance.cm.util.Constants.KEY_INSP_DATE;
import static com.loits.insurance.cm.util.Constants.KEY_INSP_REMARKS;
import static com.loits.insurance.cm.util.Constants.KEY_INSP_TYP;
import static com.loits.insurance.cm.util.Constants.KEY_INTIM;
import static com.loits.insurance.cm.util.Constants.KEY_INVESTIGATE_CLAIM;
import static com.loits.insurance.cm.util.Constants.KEY_LNGD;
import static com.loits.insurance.cm.util.Constants.KEY_LOC;
import static com.loits.insurance.cm.util.Constants.KEY_LTTD;
import static com.loits.insurance.cm.util.Constants.KEY_MILES;
import static com.loits.insurance.cm.util.Constants.KEY_NO_OF_IMAGES;
import static com.loits.insurance.cm.util.Constants.KEY_ONSITE_OFFER_AMOUNT;
import static com.loits.insurance.cm.util.Constants.KEY_OTHER_CHARGES;
import static com.loits.insurance.cm.util.Constants.KEY_PAYABLE_AMOUNT;
import static com.loits.insurance.cm.util.Constants.KEY_PHOTO_CHARGES;
import static com.loits.insurance.cm.util.Constants.KEY_POLICE_REPORT;
import static com.loits.insurance.cm.util.Constants.KEY_POLICY;
import static com.loits.insurance.cm.util.Constants.KEY_PRE_ACC_VAL;
import static com.loits.insurance.cm.util.Constants.KEY_PRICE;
import static com.loits.insurance.cm.util.Constants.KEY_PROFF_FEE;
import static com.loits.insurance.cm.util.Constants.KEY_RATE;
import static com.loits.insurance.cm.util.Constants.KEY_REASON;
import static com.loits.insurance.cm.util.Constants.KEY_REPAIR_COMPLETED;
import static com.loits.insurance.cm.util.Constants.KEY_ROWID;
import static com.loits.insurance.cm.util.Constants.KEY_R_DATE;
import static com.loits.insurance.cm.util.Constants.KEY_SALVAGE_RECEIVED;
import static com.loits.insurance.cm.util.Constants.KEY_SEQ;
import static com.loits.insurance.cm.util.Constants.KEY_SETTLEMENT_METHOD;
import static com.loits.insurance.cm.util.Constants.KEY_SITE_OFFER;
import static com.loits.insurance.cm.util.Constants.KEY_SP_REMARKS;
import static com.loits.insurance.cm.util.Constants.KEY_STATUS;
import static com.loits.insurance.cm.util.Constants.KEY_SUM_INS;
import static com.loits.insurance.cm.util.Constants.KEY_SYNC_DATE;
import static com.loits.insurance.cm.util.Constants.KEY_TELEPHONE;
import static com.loits.insurance.cm.util.Constants.KEY_TOTAL_CHARGES;
import static com.loits.insurance.cm.util.Constants.KEY_UNDER_INS_PENALTY;
import static com.loits.insurance.cm.util.Constants.KEY_VEHICLE;
import static com.loits.insurance.cm.util.Constants.KEY_YOM;

/*import static Constants.KEY_ASS_MIL;*/

/**
 * Created by DumindaW on 13/02/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // Status : A - NEW / B - Completed / C - Header Synced / D - Images Synced

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "claimManager";
    private static final String TABLE_CLAIM = "claims";
    private static final String TABLE_IMAGE = "images";
    private static final String TABLE_INIT = "init_data";
    private static final String CREATE_TABLE_CLAIMS = "create table "
            + TABLE_CLAIM + "("
            + "_id 					integer primary key autoincrement, "
            + "intimation_no	    integer,"
            + "insp_type			integer,"
            + "policy_no			text,"
            + "vehicle_no   		text,"
            + "location     		text,"
            + "contact_no   		text,"
            + "caller_name   		text,"
            + "yom		    		integer,"
            + "sum_insured   		real,"
            + "excess_amt		    real,"
            + "comp_facility	    text,"
            + "site_offer		    text,"
            + "insp_date 			text,"
            + "acr		 			real,"
            /*+ "assessor_mileage		real,"*/
            + "call_center_remark	text,"
            + "assessor_remark		text,"
            + "status	 			text,"
            + "respond_date      	text,"
            + "c_date				text,"//20
            + "sync_date			text,"
            + "pre_acc_val			real,"
            + "bald_tire_penalty	real,"
            + "under_ins_penalty	real,"
            + "insp_remarks			text,"
            + "sp_remarks			text,"
            + "proff_fee			real,"
            + "miles				real,"
            + "rate					real,"
            + "telephone			text,"//30
            + "copies				integer,"
            + "price				real,"
            + "photo_charges		real,"
            + "other_charges		real,"
            + "total_charges		real,"
            + "reason				text,"
            + "police_report		text,"
            + "investigate_claim	text,"
            + "payable_amont		real,"
            + "onsite_offer_amount 	real,"//40
            + "repair_completed 	text,"
            + "salvage_received 	text,"
            + "settlement_method 	text,"
            + "images_count 	    integer,"
            + "seq_no               integer);";//45

    private static final String CREATE_TABLE_IMAGES = "create table "
            + TABLE_IMAGE + "(_id 		integer primary key autoincrement, "
            + "intimation_no	integer,"
            + "inspection_type  integer,"
            + "seq_no	    	integer,"
            + "img_date			text,"
            + "image_no     	integer,"
            + "image        	blob,"
            + "longitude    	real,"
            + "latitude     	real,"
            + "status			text);";
   /* private static final String CREATE_TABLE_INIT = "create table "
            + TABLE_INIT + "(_id 		integer primary key autoincrement, "
            + "intimation_no	    integer,"
            + "claim_no	    		integer,"
            + "seq_no	    		integer,"
            + "insp_type			integer,"
            + "policy_no			text,"
            + "vehicle_no   		text,"
            + "location     		text,"
            + "contact_no   		text,"
            + "caller_name   		text,"
            + "yom		    		integer,"
            + "sum_insured   		real,"
            + "excess_amt		    real,"
            + "comp_facility	    text,"
            + "site_offer		    text,"
            + "call_center_remark	text,"
            + "status      			text,"
            + "inform_date 			text,"
            + "originator 			text,"
            + "respond_date			text);";*/

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_CLAIMS);
        sqLiteDatabase.execSQL(CREATE_TABLE_IMAGES);
        //sqLiteDatabase.execSQL(CREATE_TABLE_INIT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_CLAIMS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_IMAGES);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_INIT);
        onCreate(sqLiteDatabase);
    }

    private ContentValues getClaimContentValues(Claim claim) {

        ContentValues values = new ContentValues();

        values.put(KEY_INTIM, claim.getIntimationNo());
        values.put(KEY_INSP_TYP, claim.getInspType());
        values.put(KEY_POLICY, claim.getPolicyNo());
        values.put(KEY_VEHICLE, claim.getVehicleNo());
        values.put(KEY_LOC, claim.getLocation());
        values.put(KEY_CONT, claim.getContactNo());
        values.put(KEY_CALLER, claim.getCallerName());
        values.put(KEY_YOM, claim.getYom());
        values.put(KEY_SUM_INS, claim.getSumInsured());
        values.put(KEY_EXCESS, claim.getExcessAmt());
        values.put(KEY_FACILITY, claim.getCompFacility());
        values.put(KEY_SITE_OFFER, claim.getSiteOffer());
        values.put(KEY_INSP_DATE, claim.getInspDate());
        values.put(KEY_ACR, claim.getAcr());
        values.put(KEY_CC_REM, claim.getCallCenterRemark());
        values.put(KEY_ASS_REM, claim.getAssessorRemark());
        values.put(KEY_R_DATE, claim.getRespondDate());
        values.put(KEY_C_DATE, claim.getcDate());
        values.put(KEY_STATUS, claim.getStatus());
        values.put(KEY_SYNC_DATE, claim.getSyncDate());
        values.put(KEY_PRE_ACC_VAL, claim.getPreAccValue());
        values.put(KEY_BALD_TIRE_PENALTY, claim.getBaldTirePenalty());
        values.put(KEY_UNDER_INS_PENALTY, claim.getUnderInsPenalty());
        values.put(KEY_INSP_REMARKS, claim.getInspectionRemarks());
        values.put(KEY_SP_REMARKS, claim.getSpecialRemarks());
        values.put(KEY_PROFF_FEE, claim.getProffesionalFee());
        values.put(KEY_MILES, claim.getMiles());
        values.put(KEY_RATE, claim.getRate());
        values.put(KEY_TELEPHONE, claim.getTelephone());
        values.put(KEY_COPIES, claim.getCopies());
        values.put(KEY_PRICE, claim.getPrice());
        values.put(KEY_PHOTO_CHARGES, claim.getPhotoCharges());
        values.put(KEY_OTHER_CHARGES, claim.getOtherCharges());
        values.put(KEY_TOTAL_CHARGES, claim.getTotalCharges());
        values.put(KEY_REASON, claim.getReason());
        values.put(KEY_POLICE_REPORT, claim.getPoliceReport());
        values.put(KEY_INVESTIGATE_CLAIM, claim.getInvestigateClaim());
        values.put(KEY_PAYABLE_AMOUNT, claim.getPayableAmount());
        values.put(KEY_ONSITE_OFFER_AMOUNT, claim.getOnsiteOfferAmount());
        values.put(KEY_REPAIR_COMPLETED, claim.getRepairCompleted());
        values.put(KEY_SALVAGE_RECEIVED, claim.getSalvageReceived());
        values.put(KEY_SETTLEMENT_METHOD, claim.getSettlementMethod());
        values.put(KEY_NO_OF_IMAGES, claim.getImagesCount());
        values.put(KEY_SEQ, claim.getSeqNo());//getSeq_no

        return values;
    }

    public long addClaim(Claim claim) {

        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(TABLE_CLAIM, null, getClaimContentValues(claim));
        db.close();

        return id;
    }

    public boolean updateClaim(Claim claim) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getClaimContentValues(claim);
        boolean updated =  db.update(TABLE_CLAIM, values, KEY_ROWID + "=" + claim.getId(), null) > 0;
        return updated;
    }

    public boolean deleteClaim(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CLAIM, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteClaim(int intimationNo, int inspectionType) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CLAIM, KEY_INSP_TYP + "=" +
                inspectionType + " AND "+ KEY_INTIM + "="+
                intimationNo, null) > 0;
    }

    public List<Claim> fetchReadyToDeleteClaims(long maxTimeAllowed) {

        List<Claim> claimList = new ArrayList<Claim>();

        SQLiteDatabase db = this.getWritableDatabase();
        String where = KEY_STATUS + " = 'D' AND strftime('%Y-%m-%d %H:%M:%S', replace("
                + KEY_R_DATE + ",'/','-')) <  strftime('%Y-%m-%d %H:%M:%S',"
                + maxTimeAllowed + "/1000, 'unixepoch')" ;

        Cursor cursor = db.query(TABLE_CLAIM, null, where, null, null, null, null);
        int c  = cursor.getCount();

        //String date  = getSyncDate();

        if (cursor.moveToFirst()) {
            do {
                Claim claim = new Claim(cursor.getLong(0), cursor.getInt(1),
                        cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getDouble(9),
                        cursor.getDouble(10), cursor.getString(11), cursor.getString(12),
                        cursor.getString(13), cursor.getDouble(14), cursor.getString(15), cursor.getString(16),
                        cursor.getString(17), cursor.getString(18),
                        cursor.getString(19), cursor.getDouble(20), cursor.getDouble(21),
                        cursor.getDouble(22), cursor.getString(23), cursor.getString(24),
                        cursor.getDouble(25), cursor.getDouble(26), cursor.getDouble(27),
                        cursor.getDouble(29), cursor.getInt(30), cursor.getDouble(31), cursor.getDouble(32),
                        cursor.getDouble(33), cursor.getDouble(34), cursor.getString(35),
                        cursor.getString(36),cursor.getString(37), cursor.getDouble(38),
                        cursor.getDouble(39), cursor.getString(40), cursor.getString(41), cursor.getString(42), cursor.getInt(44),"",0);

                claimList.add(claim);
            } while (cursor.moveToNext());
        }
        db.close();
        return claimList;
    }

    public Claim getClaim(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CLAIM, new String[] { KEY_ROWID,//0
                        KEY_INTIM,
                        KEY_INSP_TYP,
                        KEY_POLICY,
                        KEY_VEHICLE,
                        KEY_LOC,
                        KEY_CONT,
                        KEY_CALLER,
                        KEY_YOM,
                        KEY_SUM_INS,
                        KEY_EXCESS,//10
                        KEY_FACILITY,
                        KEY_SITE_OFFER,
                        KEY_INSP_DATE,
                        KEY_ACR,
                        KEY_CC_REM,
                        KEY_ASS_REM,
                        KEY_STATUS,
                        KEY_R_DATE,
                        KEY_C_DATE,
                        KEY_PRE_ACC_VAL,//20
                        KEY_BALD_TIRE_PENALTY,
                        KEY_UNDER_INS_PENALTY,
                        KEY_INSP_REMARKS,
                        KEY_SP_REMARKS,
                        KEY_PROFF_FEE,
                        KEY_MILES,
                        KEY_RATE,
                        KEY_TELEPHONE,
                        KEY_COPIES,
                        KEY_PRICE,//30
                        KEY_PHOTO_CHARGES,
                        KEY_OTHER_CHARGES,
                        KEY_TOTAL_CHARGES,
                        KEY_REASON,
                        KEY_POLICE_REPORT,
                        KEY_INVESTIGATE_CLAIM,
                        KEY_PAYABLE_AMOUNT,
                        KEY_ONSITE_OFFER_AMOUNT,
                        KEY_REPAIR_COMPLETED,
                        KEY_SALVAGE_RECEIVED,//40
                        KEY_SETTLEMENT_METHOD,
                        KEY_NO_OF_IMAGES,
                        KEY_SEQ} , KEY_ROWID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Claim claim = new Claim(cursor.getLong(0), cursor.getInt(1),
                cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getDouble(9),
                cursor.getDouble(10), cursor.getString(11), cursor.getString(12),
                cursor.getString(13), cursor.getDouble(14), cursor.getString(15), cursor.getString(16),
                cursor.getString(17), cursor.getString(18),
                cursor.getString(19), cursor.getDouble(20), cursor.getDouble(21),
                cursor.getDouble(22), cursor.getString(23), cursor.getString(24),
                cursor.getDouble(25), cursor.getDouble(26), cursor.getDouble(27),
                cursor.getDouble(28), cursor.getInt(29), cursor.getDouble(30), cursor.getDouble(31),
                cursor.getDouble(32), cursor.getDouble(33), cursor.getString(34),
                cursor.getString(35),cursor.getString(36), cursor.getDouble(37),
                cursor.getDouble(38), cursor.getString(39), cursor.getString(40),
                cursor.getString(41), cursor.getInt(42),"",cursor.getInt(43));
        // return claim
        db.close();
        return claim;
    }

    public int getInspectionType(int intimationNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCursor = db.query(true, TABLE_CLAIM, new String[] { KEY_INSP_TYP },KEY_INTIM + "=" + intimationNo, null, null, null, null, null);

        int inspectionType = 0;
        if (mCursor != null) {
            mCursor.moveToFirst();
            inspectionType = mCursor.getInt(0);
        }
        db.close();
        return inspectionType;
    }

    /*public String getSyncDate() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCursor = db.query(true, TABLE_CLAIM, new String[] { KEY_SYNC_DATE, KEY_C_DATE},null, null, null, null, null, null);

        String date = "";
        String date2 = "";
        if (mCursor != null) {
            mCursor.moveToFirst();
            date = mCursor.getString(0);
            date2 = mCursor.getString(1);
        }
        db.close();
        return date + " - "+ date2;
    }*/

    public List<Claim> getAllClaims() {
        List<Claim> claimList = new ArrayList<Claim>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CLAIM;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Claim claim = new Claim(cursor.getLong(0), cursor.getInt(1),
                        cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getDouble(9),
                        cursor.getDouble(10), cursor.getString(11), cursor.getString(12),
                        cursor.getString(13), cursor.getDouble(14), cursor.getString(15), cursor.getString(16),
                        cursor.getString(17), cursor.getString(18),
                        cursor.getString(19), cursor.getDouble(20), cursor.getDouble(21),
                        cursor.getDouble(22), cursor.getString(23), cursor.getString(24),
                        cursor.getDouble(25), cursor.getDouble(26), cursor.getDouble(27),
                        cursor.getDouble(28), cursor.getInt(29), cursor.getDouble(30), cursor.getDouble(31),
                        cursor.getDouble(32), cursor.getDouble(33), cursor.getString(34),
                        cursor.getString(35),cursor.getString(36), cursor.getDouble(37),
                        cursor.getDouble(38), cursor.getString(39), cursor.getString(40),
                        cursor.getString(41), cursor.getInt(42),"",cursor.getInt(44));

                claimList.add(claim);
            } while (cursor.moveToNext());
        }
        db.close();
        return claimList;
    }

    public List<Claim> getNewClaims() {
        List<Claim> claimList = new ArrayList<Claim>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CLAIM + " WHERE status = \"A\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                /*long id=cursor.getLong(0);
                int intimationNo=cursor.getInt(1);
                int inspType=cursor.getInt(2);
                String policyNo=cursor.getString(3);
                String vehicleNo=cursor.getString(4);
                String location=cursor.getString(5);
                String contactNo=cursor.getString(6);
                String callerName=cursor.getString(7);
                String yom=cursor.getString(8);
                double sumInsured=cursor.getDouble(9);
                double excessAmt=cursor.getDouble(10);
                String compFacility=cursor.getString(11);
                String siteOffer=cursor.getString(12);
                String inspDate=cursor.getString(13);
                double acr=cursor.getDouble(14);
                String callCenterRemark=cursor.getString(15);
                String assessorRemark=cursor.getString(16);
                String status=cursor.getString(17);
                long respondDate=cursor.getLong(18);
                String cDate=cursor.getString(19);
                String syncDate=cursor.getString(20);
                double preAccValue=cursor.getDouble(21);
                double baldTirePenalty=cursor.getDouble(22);
                double underInsPenalty=cursor.getDouble(23);
                String inspectionRemarks=cursor.getString(24);
                String specialRemarks=cursor.getString(25);
                double proffesionalFee=cursor.getDouble(26);
                double miles=cursor.getDouble(27);
                double rate=cursor.getDouble(28);
                double telephone=cursor.getDouble(29);
                int copie=cursor.getInt(30);
                double price=cursor.getDouble(31);
                double photoCharges=cursor.getDouble(32);
                double otherCharges=cursor.getDouble(33);
                double totalCharges=cursor.getDouble(34);
                String reason=cursor.getString(35);
                String policeReport=cursor.getString(36);
                String investigateClaim=cursor.getString(37);
                double payableAmount=cursor.getDouble(38);
                double onsiteOfferAmount=cursor.getDouble(39);
                String repairCompleted=cursor.getString(40);
                String salvageReceived=cursor.getString(41);
                String settlementMethod=cursor.getString(42);
                int noOfImages=cursor.getInt(43);*/

                Claim contact = new Claim(cursor.getLong(0), cursor.getInt(1),
                        cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getDouble(9),
                        cursor.getDouble(10), cursor.getString(11), cursor.getString(12),
                        cursor.getString(13), cursor.getDouble(14), cursor.getString(15), cursor.getString(16),
                        cursor.getString(17), cursor.getString(18),
                        cursor.getString(19), cursor.getDouble(20), cursor.getDouble(21),
                        cursor.getDouble(22), cursor.getString(23), cursor.getString(24),
                        cursor.getDouble(25), cursor.getDouble(26), cursor.getDouble(27),
                        cursor.getDouble(28), cursor.getInt(29), cursor.getDouble(30), cursor.getDouble(31),
                        cursor.getDouble(32), cursor.getDouble(33), cursor.getString(34),
                        cursor.getString(35),cursor.getString(36), cursor.getDouble(37),
                        cursor.getDouble(38), cursor.getString(39), cursor.getString(40),
                        cursor.getString(41), cursor.getInt(42), "", cursor.getInt(44));

                claimList.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();
        return claimList;
    }

    //+ " in (\"B\",\"C\")"

    public List<Claim> getCompletedClaims() {
        List<Claim> claimList = new ArrayList<Claim>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_CLAIM + " WHERE status = in (\"B\",\"C\",\"D\")";

        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor cursor = db.rawQuery(selectQuery, null);

        Cursor cursor =  db.query(TABLE_CLAIM, new String[] {
                        KEY_ROWID,//0
                        KEY_INTIM,
                        KEY_INSP_TYP,
                        KEY_POLICY,
                        KEY_VEHICLE,
                        KEY_LOC,
                        KEY_CONT,
                        KEY_CALLER,
                        KEY_YOM,
                        KEY_SUM_INS,
                        KEY_EXCESS,
                        KEY_FACILITY,
                        KEY_SITE_OFFER,
                        KEY_INSP_DATE,
                        KEY_ACR,
                        KEY_CC_REM,
                        KEY_ASS_REM,
                        KEY_STATUS,
                        KEY_R_DATE,
                        KEY_C_DATE,
                        KEY_PRE_ACC_VAL,//20
                        KEY_BALD_TIRE_PENALTY,
                        KEY_UNDER_INS_PENALTY,
                        KEY_INSP_REMARKS,
                        KEY_SP_REMARKS,
                        KEY_PROFF_FEE,
                        KEY_MILES,
                        KEY_RATE,
                        KEY_TELEPHONE,
                        KEY_COPIES,
                        KEY_PRICE,//30
                        KEY_PHOTO_CHARGES,
                        KEY_OTHER_CHARGES,
                        KEY_TOTAL_CHARGES,
                        KEY_REASON,
                        KEY_POLICE_REPORT,
                        KEY_INVESTIGATE_CLAIM,
                        KEY_PAYABLE_AMOUNT,
                        KEY_ONSITE_OFFER_AMOUNT,
                        KEY_REPAIR_COMPLETED,
                        KEY_SALVAGE_RECEIVED,//40
                        KEY_SETTLEMENT_METHOD,
                        KEY_NO_OF_IMAGES, KEY_SEQ},
                KEY_STATUS + " in (\"B\",\"C\",\"D\")", null, null, null, null);

        // looping through all rows and adding to list
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Claim contact = new Claim(cursor.getLong(0), cursor.getInt(1),
                    cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getDouble(9),
                    cursor.getDouble(10), cursor.getString(11), cursor.getString(12),
                    cursor.getString(13), cursor.getDouble(14), cursor.getString(15), cursor.getString(16),
                    cursor.getString(17), cursor.getString(18),
                    cursor.getString(19), cursor.getDouble(20), cursor.getDouble(21),
                    cursor.getDouble(22), cursor.getString(23), cursor.getString(24),
                    cursor.getDouble(25), cursor.getDouble(26), cursor.getDouble(27),
                    cursor.getDouble(28), cursor.getInt(29), cursor.getDouble(30), cursor.getDouble(31),
                    cursor.getDouble(32), cursor.getDouble(33), cursor.getString(34),
                    cursor.getString(35), cursor.getString(36), cursor.getDouble(37),
                    cursor.getDouble(38), cursor.getString(39), cursor.getString(40),
                    cursor.getString(41), cursor.getInt(42),"",cursor.getInt(43));

            claimList.add(contact);
            cursor.moveToNext();
        }

        db.close();
        return claimList;
    }

    public List<Claim> getPendingClaims() {
        List<Claim> claimList = new ArrayList<Claim>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_CLAIM + " WHERE status = in (\"B\",\"C\")";

        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor cursor = db.rawQuery(selectQuery, null);

        Cursor cursor =  db.query(TABLE_CLAIM, new String[] {
                        KEY_ROWID,
                        KEY_INTIM,
                        KEY_INSP_TYP,
                        KEY_POLICY,
                        KEY_VEHICLE,
                        KEY_LOC,
                        KEY_CONT,
                        KEY_CALLER,
                        KEY_YOM,
                        KEY_SUM_INS,
                        KEY_EXCESS,//10
                        KEY_FACILITY,
                        KEY_SITE_OFFER,
                        KEY_INSP_DATE,
                        KEY_ACR,
                        KEY_CC_REM,
                        KEY_ASS_REM,
                        KEY_STATUS,
                        KEY_R_DATE,
                        KEY_C_DATE,
                        KEY_PRE_ACC_VAL,//20
                        KEY_BALD_TIRE_PENALTY,
                        KEY_UNDER_INS_PENALTY,
                        KEY_INSP_REMARKS,
                        KEY_SP_REMARKS,
                        KEY_PROFF_FEE,
                        KEY_MILES,
                        KEY_RATE,
                        KEY_TELEPHONE,
                        KEY_COPIES,
                        KEY_PRICE,//30
                        KEY_PHOTO_CHARGES,
                        KEY_OTHER_CHARGES,
                        KEY_TOTAL_CHARGES,
                        KEY_REASON,
                        KEY_POLICE_REPORT,
                        KEY_INVESTIGATE_CLAIM,
                        KEY_PAYABLE_AMOUNT,
                        KEY_ONSITE_OFFER_AMOUNT,
                        KEY_REPAIR_COMPLETED,
                        KEY_SALVAGE_RECEIVED,//40
                        KEY_SETTLEMENT_METHOD,
                        KEY_NO_OF_IMAGES,
                        KEY_SEQ},//42
                KEY_STATUS + " in (\"B\",\"C\")", null, null, null, null);

        // looping through all rows and adding to list

        int c = cursor.getCount();

        if (cursor.moveToFirst()) {
            do {

                /*long id=cursor.getLong(0);
                int intimationNo=cursor.getInt(1);
                int inspType=cursor.getInt(2);
                String policyNo=cursor.getString(3);
                String vehicleNo=cursor.getString(4);
                String location=cursor.getString(5);
                String contactNo=cursor.getString(6);
                String callerName=cursor.getString(7);
                String yom=cursor.getString(8);
                double sumInsured=cursor.getDouble(9);
                double excessAmt=cursor.getDouble(10);
                String compFacility=cursor.getString(11);
                String siteOffer=cursor.getString(12);
                String inspDate=cursor.getString(13);
                double acr=cursor.getDouble(14);
                String callCenterRemark=cursor.getString(15);
                String assessorRemark=cursor.getString(16);
                String status=cursor.getString(17);
                long respondDate=cursor.getLong(18);
                String cDate=cursor.getString(19);
                String syncDate=cursor.getString(20);
                double preAccValue=cursor.getDouble(21);
                double baldTirePenalty=cursor.getDouble(22);
                double underInsPenalty=cursor.getDouble(23);
                String inspectionRemarks=cursor.getString(24);
                String specialRemarks=cursor.getString(25);
                double proffesionalFee=cursor.getDouble(26);
                double miles=cursor.getDouble(27);
                double rate=cursor.getDouble(28);
                double telephone=cursor.getDouble(29);
                int copie=cursor.getInt(30);
                double price=cursor.getDouble(31);
                double photoCharges=cursor.getDouble(32);
                double otherCharges=cursor.getDouble(33);
                double totalCharges=cursor.getDouble(34);
                String reason=cursor.getString(35);
                String policeReport=cursor.getString(36);
                String investigateClaim=cursor.getString(37);
                double payableAmount=cursor.getDouble(38);
                double onsiteOfferAmount=cursor.getDouble(39);
                String repairCompleted=cursor.getString(40);
                String salvageReceived=cursor.getString(41);
                String settlementMethod=cursor.getString(42);
                int noOfImages=cursor.getInt(43);*/

                Claim contact = new Claim(cursor.getLong(0), cursor.getInt(1),
                        cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getDouble(9),
                        cursor.getDouble(10), cursor.getString(11), cursor.getString(12),
                        cursor.getString(13), cursor.getDouble(14), cursor.getString(15), cursor.getString(16),
                        cursor.getString(17), cursor.getString(18),
                        cursor.getString(19), cursor.getDouble(20), cursor.getDouble(21),
                        cursor.getDouble(22), cursor.getString(23), cursor.getString(24),
                        cursor.getDouble(25), cursor.getDouble(26), cursor.getDouble(27),
                        cursor.getDouble(28), cursor.getInt(29), cursor.getDouble(30), cursor.getDouble(31),
                        cursor.getDouble(32), cursor.getDouble(33), cursor.getString(34),
                        cursor.getString(35),cursor.getString(36), cursor.getDouble(37),
                        cursor.getDouble(38), cursor.getString(39), cursor.getString(40),
                        cursor.getString(41), cursor.getInt(42),"",cursor.getInt(43));

                claimList.add(contact);
            } while (cursor.moveToNext());
        }

        db.close();
        return claimList;
    }


    private ContentValues getImageContentValues(Image image) {

        ContentValues values = new ContentValues();
        values.put(KEY_IMG_INTIM, image.getIntim_no());
        values.put(KEY_IMG_SEQ, image.getSeq_no());
        values.put(KEY_IMG, image.getMyimage());
        values.put(KEY_IMG_DATE, image.getImage_date());
        values.put(KEY_IMG_NO, image.getImg_no());
        values.put(KEY_LNGD, image.getLogitude());
        values.put(KEY_LTTD, image.getLatitude());
        values.put(KEY_IMG_INSP_TYPE, image.getInspection_type());
        values.put(KEY_IMG_STATUS, image.getStatus());

        return values;
    }

    public long addImage(Image image) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(TABLE_IMAGE, null, getImageContentValues(image));
        db.close();

        return id;
    }

    public boolean updateImage(Image image) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getImageContentValues(image);
        status =  db.update(TABLE_IMAGE, values, KEY_IMG_ROWID + "=" + image.getId(), null) > 0;

        db.close();
        return status;
    }

    public boolean deleteImage(long rowId) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        status = db.delete(TABLE_IMAGE, KEY_IMG_ROWID + "=" + rowId, null) > 0;

        db.close();
        return status;
    }

    public boolean deleteImages(int intim, int inspectionType) {
        boolean status = false;
        SQLiteDatabase db = this.getWritableDatabase();
        status =  db.delete(TABLE_IMAGE,
                KEY_IMG_INTIM +"="+ intim +" AND "+
                KEY_IMG_INSP_TYPE +"="+ inspectionType + " AND " +
                KEY_IMG_STATUS + " = \"C\"" , null) > 0;
        db.close();
        return status;
    }

    public List<Image> getAllImages(int intimationNo, int inspectionType) {
        List<Image> imageList = new ArrayList<Image>();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_IMAGE, new String[]{
                        KEY_IMG_ROWID,
                        KEY_IMG_INTIM,//1
                        KEY_IMG_SEQ,
                        KEY_IMG,
                        KEY_IMG_DATE,
                        KEY_IMG_NO,
                        KEY_LNGD,
                        KEY_LTTD,
                        KEY_IMG_INSP_TYPE}
                , KEY_IMG_INTIM + " = '" + intimationNo + "' AND " + KEY_IMG_INSP_TYPE + " = '" + inspectionType + "'", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Image image = new Image(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3),
                    cursor.getString(4), cursor.getInt(5), cursor.getDouble(6), cursor.getDouble(7),
                    cursor.getInt(8), "");

            imageList.add(image);
            cursor.moveToNext();
        }

        // looping through all rows and adding to list
       /* if(cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Image image = new Image(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3),
                            cursor.getString(4), cursor.getInt(5), cursor.getDouble(6), cursor.getDouble(7),
                            cursor.getInt(8));

                    imageList.add(image);
                } while (cursor.moveToNext());
            }
        }*/
        db.close();
        return imageList;
    }

    public List<Image> getPendingImages(int intimationNo, int inspectionType) {
        List<Image> imageList = new ArrayList<Image>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_IMAGE + "WHERE "+
                KEY_IMG_INTIM + " = '" + intimationNo + "' AND " +
                KEY_IMG_INSP_TYPE + " = '" + inspectionType+"'" + " AND " + KEY_IMG_STATUS + " = \"A\"";

        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor cursor = db.rawQuery(selectQuery, null);

        Cursor cursor = db.query(TABLE_IMAGE, new String[]{
                        KEY_IMG_ROWID,
                        KEY_IMG_INTIM,//1
                        KEY_IMG_SEQ,
                        KEY_IMG,
                        KEY_IMG_DATE,
                        KEY_IMG_NO,
                        KEY_LNGD,
                        KEY_LTTD,
                        KEY_IMG_INSP_TYPE}
                        ,KEY_IMG_INTIM + " = '" + intimationNo + "' AND " +
                        KEY_IMG_INSP_TYPE + " = '" + inspectionType+"'" + " AND " + KEY_IMG_STATUS + " = \"A\"", null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Image image = new Image(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3),
                        cursor.getString(4), cursor.getInt(5), cursor.getDouble(6), cursor.getDouble(7),
                        cursor.getInt(8), "");

                imageList.add(image);
            } while (cursor.moveToNext());
        }
        db.close();
        return imageList;
    }

    public Image getImage(int intimationNo, int inspectionType, int no) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_IMAGE, new String[]
                {KEY_IMG_ROWID ,
                        KEY_IMG_INTIM,//1
                        KEY_IMG_SEQ,
                        KEY_IMG ,
                        KEY_IMG_DATE,
                        KEY_IMG_NO ,
                        KEY_LNGD ,
                        KEY_LTTD ,
                        KEY_IMG_INSP_TYPE} , KEY_IMG_INTIM + "=" + intimationNo + " AND " + KEY_IMG_INSP_TYPE + "=" + inspectionType + " AND "+ KEY_IMG_NO +"="+ no  , null, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Image image = new Image(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3),
                cursor.getString(4), cursor.getInt(5), cursor.getDouble(6), cursor.getDouble(7),
                cursor.getInt(8), "");
        // return image
        db.close();
        return image;
    }

    public Image getImageById(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_IMAGE, new String[]
                        {KEY_IMG}
                ,KEY_IMG_ROWID +"="+ id  , null, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Image image = new Image(cursor.getString(0));

        db.close();

        return image;
    }
}

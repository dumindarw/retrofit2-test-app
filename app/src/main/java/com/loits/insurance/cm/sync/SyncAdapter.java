package com.loits.insurance.cm.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;

import com.loits.insurance.cm.db.DatabaseHandler;
import com.loits.insurance.cm.models.ActionResponse;
import com.loits.insurance.cm.models.Claim;
import com.loits.insurance.cm.models.Image;
import com.loits.insurance.cm.network.Service;

import java.util.List;

import static com.loits.insurance.cm.util.Constants.IDS_CLIENT_ID;
import static com.loits.insurance.cm.util.Constants.IDS_CLIENT_SECRET;
import static com.loits.insurance.cm.util.Constants.KEY_USERNAME;
import static com.loits.insurance.cm.util.Constants.NO_OF_RETRIES;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    ContentResolver mContentResolver;
    DatabaseHandler mDbHelper;
    AccountManager accountManager;
    Account mAccount;
    Context mContext;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        mDbHelper = new DatabaseHandler(context);
        mContext = context;
    }

    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }


    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        accountManager =
                (AccountManager) getContext().getSystemService(
                        getContext().ACCOUNT_SERVICE);

        mAccount = account;

        syncData();

    }

    public synchronized void syncData() {
        try {
            try {

                List<Claim> claims = mDbHelper.getPendingClaims();

                Service service = new Service(mContext, IDS_CLIENT_ID, IDS_CLIENT_SECRET);


                for (int i = 0; i < claims.size(); i++) {

                    long id = claims.get(i).getId();
                    int intimNo = claims.get(i).getIntimationNo();
                    String vehicle = claims.get(i).getVehicleNo();
                    int insType = claims.get(i).getInspType();
                    String policy = claims.get(i).getPolicyNo();
                    String loc = claims.get(i).getLocation();
                    String caller = claims.get(i).getCallerName();
                    String callerCont = claims.get(i).getContactNo();
                    String yom = claims.get(i).getYom();
                    double sumInsu = claims.get(i).getSumInsured();
                    double excess = claims.get(i).getExcessAmt();
                    String facilty = claims.get(i).getCompFacility();
                    String siteOffer = claims.get(i).getSiteOffer();
                    String cCRemark = claims.get(i).getCallCenterRemark();
                    String inspDate = claims.get(i).getInspDate();
                    double acr = claims.get(i).getAcr();
                    /*double mileage = claims.get(i).getAssMileage();*/
                    String assRemark = claims.get(i).getAssessorRemark();
                    String respondDate = claims.get(i).getRespondDate();
                    String user = claims.get(i).getUser();
                    String cDate = claims.get(i).getcDate();
                    String status = claims.get(i).getStatus();
                    double preAccValue = claims.get(i).getPreAccValue();
                    double baldTirePenalty = claims.get(i).getBaldTirePenalty();
                    double underInsPenalty = claims.get(i).getUnderInsPenalty();
                    String inspectionRemarks = claims.get(i).getInspectionRemarks();
                    String specialRemarks = claims.get(i).getSpecialRemarks();
                    double proffesionalFee = claims.get(i).getProffesionalFee();
                    double miles = claims.get(i).getMiles();
                    double rate = claims.get(i).getRate();
                    double telephone = claims.get(i).getTelephone();
                    int copies = claims.get(i).getCopies();
                    double price = claims.get(i).getPrice();
                    double photoCharges = claims.get(i).getPhotoCharges();
                    double otherCharges = claims.get(i).getOtherCharges();
                    double totalCharges = claims.get(i).getTotalCharges();
                    String reason = claims.get(i).getReason();
                    String policeReport = claims.get(i).getPoliceReport();
                    String investigateClaim = claims.get(i).getInvestigateClaim();
                    double payableAmount = claims.get(i).getPayableAmount();
                    double onsiteOfferAmount = claims.get(i).getOnsiteOfferAmount();
                    String repairCompleted = claims.get(i).getRepairCompleted();
                    String salvageReceived = claims.get(i).getSalvageReceived();
                    String settlementMethod = claims.get(i).getSettlementMethod();
                    int sequenceNo = claims.get(i).getSeqNo();

                    int totalImages = 0;

                    List<Image> images = mDbHelper.getPendingImages(intimNo, insType);
                    totalImages = images.size();

                    String username = accountManager.getUserData(mAccount, KEY_USERNAME);


                    Claim claim = new Claim(id, intimNo, insType,
                            policy, vehicle, loc, callerCont, caller,
                            yom, sumInsu, excess, facilty, siteOffer,
                            inspDate, acr, cCRemark,
                            assRemark, status, respondDate, cDate,
                            preAccValue, baldTirePenalty, underInsPenalty,
                            inspectionRemarks,
                            specialRemarks,
                            proffesionalFee,
                            miles,
                            rate,
                            telephone,
                            copies,
                            price,
                            photoCharges,
                            otherCharges,
                            totalCharges,
                            reason,
                            policeReport,
                            investigateClaim,
                            payableAmount,
                            onsiteOfferAmount,
                            repairCompleted,
                            salvageReceived,
                            settlementMethod,
                            totalImages,
                            username,sequenceNo);

                    int expRounds = 0;

                    while (expRounds < NO_OF_RETRIES) {

                        // should handle a timeout exception and end the
                        // sync
                        try {

                            ActionResponse response = service.uploadClaimValues(claim, sequenceNo);

                            if (response != null && response.getCode() == 1) {

                                // update the flag
                                claim.setStatus("C");
                                //claim.setSyncDate(String.valueOf(System.currentTimeMillis()));
                                mDbHelper.updateClaim(claim);
                                //expRounds = NO_OF_RETRIES + 1;



                                break;

                            } else {
                                expRounds = NO_OF_RETRIES + 1;
                            }

                        } catch (Exception e) {
                            expRounds = NO_OF_RETRIES + 1;
                        }
                    }

                    if (expRounds == NO_OF_RETRIES) {
                        break;
                    }



                    /*if (claim.getStatus().equals("C")
                            && syncImage(intimNo, insType)) {

                        claim.setStatus("D");
                        claim.setSyncDate(DateTimeHelper.now());
                        mDbHelper.updateClaim(claim);
                    }*/

                    //cursor.moveToNext();
                }

                syncImages(service);

            } finally {

            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }



    private synchronized boolean syncImages(Service service) {

        //Service service = new Service(getContext(), IDS_CLIENT_ID, IDS_CLIENT_SECRET);
        String username = accountManager.getUserData(mAccount, KEY_USERNAME);

        List<Claim> claims = mDbHelper.getCompletedClaims();

        boolean sts = true;

        for (int i = 0; i < claims.size(); i++) {

            List<Image> images = mDbHelper.getPendingImages(claims.get(i).getIntimationNo(), claims.get(i).getInspType());

            for (int j = 0; j < images.size(); j++) {

                long id = images.get(j).getId();
                int intim_no = images.get(j).getIntim_no();
                int seq_no = images.get(j).getSeq_no();
                int img_no = images.get(j).getImg_no();
                String img_date = images.get(j).getImage_date();
                double img_lngd = images.get(j).getLogitude();
                double img_lttd = images.get(j).getLatitude();
                String imageStr = images.get(j).getMyimage();
                int inspectionType = images.get(j).getInspection_type();

                Image myImage = new Image(id, intim_no, seq_no, imageStr,
                        img_date, img_no, img_lngd, img_lttd, inspectionType, username);

                int expRounds = 0;

                while (expRounds < NO_OF_RETRIES) {

                    // should handle a timeout exception and end the sync
                    try {

                        ActionResponse response = service.uploadImages(myImage, seq_no);

                        if (response != null && response.getCode() == 1) {
                            // update the flag
                            myImage.setStatus("C");
                            mDbHelper.updateImage(myImage);
                            Log.i(getClass().getSimpleName(), "Image: " + myImage.getImg_no() + " Flag updated");
                            expRounds = NO_OF_RETRIES + 1;
                            //sts = true;
                            Log.i(getClass().getSimpleName(), "ok Image");

                        } else {
                            expRounds++;
                            //sts = false;
                            Log.i(getClass().getSimpleName(), response.getMessage());
                        }

                    } catch (Exception e) {
                        //TODO : Handle Log out
                        expRounds = NO_OF_RETRIES + 1;
                        sts = false;
                        Log.i(getClass().getSimpleName(), "excep 1 Image");
                    }
                }

                if (expRounds == NO_OF_RETRIES) {
                    sts = false;
                    break;
                }
            }

            claims.get(i).setStatus("D");
            claims.get(i).setSyncDate(String.valueOf(System.currentTimeMillis()));
            mDbHelper.updateClaim(claims.get(i));
        }
        return sts;
    }

    /* For image Sync */
    /*public synchronized boolean syncImage_old(int intimNo, int seqNo) {

        boolean sts = true;

        List<Image> images = mDbHelper.getPendingImages(intimNo, seqNo);

        Service service = new Service(getContext(), IDS_CLIENT_ID, IDS_CLIENT_SECRET);

        for (int i = 0; i < images.size(); i++) {

            long id = images.get(i).getId();
            int intim_no = images.get(i).getIntim_no();
            int seq_no = images.get(i).getSeq_no();
            int img_no = images.get(i).getImg_no();
            String img_date = images.get(i).getImage_date();
            double img_lngd = images.get(i).getLogitude();
            double img_lttd = images.get(i).getLatitude();

            Image image = mDbHelper.getImage(intim_no, seq_no, img_no);

            String imageStr = image.getMyimage();
            int inspectionType = image.getInspection_type();

            Image myImage = new Image(id, intim_no, seq_no, imageStr,
                    img_date, img_no, img_lngd, img_lttd, inspectionType);

            int expRounds = 0;

            while (expRounds < NO_OF_RETRIES) {

                // should handle a timeout exception and end the sync
                try {

                    ActionResponse response = service.uploadImages(image, seqNo);

                    if (response != null && response.getCode() == 1) {
                        // update the flag
                        myImage.setStatus("C");
                        mDbHelper.updateImage(myImage);
                        Log.i(getClass().getSimpleName(), "Image: " + myImage.getImg_no() + " Flag updated");
                        expRounds = NO_OF_RETRIES + 1;
                        //sts = true;
                        Log.i(getClass().getSimpleName(), "ok Image");

                    } else {
                        expRounds++;
                        //sts = false;
                        Log.i(getClass().getSimpleName(), response.getMessage());
                    }

                } catch (Exception e) {
                    //TODO : Handle Log out
                    expRounds = NO_OF_RETRIES + 1;
                    sts = false;
                    Log.i(getClass().getSimpleName(), "excep 1 Image");
                }
            }

            if (expRounds == NO_OF_RETRIES) {
                sts = false;
                break;
            }

            *//*Log.i(getClass().getSimpleName(), "Not Last Image");

            if (images != null) {
                if (!imgcursor.isClosed()) {
                    imgcursor.moveToNext();
                    Log.i(getClass().getSimpleName(), "Move Next Image");
                }
            }
            Log.i(getClass().getSimpleName(), "Move Next Image");*//*
        }

        return sts;
    }*/

}

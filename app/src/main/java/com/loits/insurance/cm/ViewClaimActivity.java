package com.loits.insurance.cm;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.loits.insurance.cm.db.DatabaseHandler;
import com.loits.insurance.cm.models.Claim;
import com.loits.insurance.cm.models.InspectionType;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.loits.insurance.cm.util.Constants.ACCOUNT_TYPE;
import static com.loits.insurance.cm.util.Constants.CAPTURE_IMAGE;
import static com.loits.insurance.cm.util.Constants.KEY_IMG_INSP_TYPE;
import static com.loits.insurance.cm.util.Constants.KEY_IMG_INTIM;
import static com.loits.insurance.cm.util.Constants.VIEW_IMAGES;

public class ViewClaimActivity extends AppCompatActivity{

    private static AccountManager mManager;
    private static Account mAccount;
    DatabaseHandler mDbHelper;
    private EditText mIntimNo;
    private EditText mVehicle;
    private Spinner mInsType;
    private EditText mInspDate;
    private EditText mInspTime;
    private EditText mPolicy;
    private EditText mLoc;
    private EditText mCaller;
    private EditText mCallerCont;
    private EditText mYom;
    private EditText mSumInsu;
    private EditText mExcess;
    private EditText mAcr;
    // private EditText mClaimMileage;
    private EditText mAssRemark;
    //private CheckBox mFaciliy;
    //private CheckBox mSiteOffer;
    private EditText mCcRemark;
    //private Button confirmButton;
    private ImageButton btnInspDate;
    private ImageButton btnInspTime;
    private EditText mPreAccValue;
    private EditText mBaldTirePenalty;
    private EditText mUnderInsPenalty;
    private EditText mInspectionRemarks;
    private EditText mSpecialRemarks;
    private EditText mProffesionalFee;
    ////private EditText mMiles;
    //private EditText mRate;
    private EditText mMileage;
    private EditText mTelephone;
    private EditText mCopies;
    //private EditText mPrice;
    //private EditText mPhotoCharges;
    private EditText mOtherCharges;
    private EditText mTotalCharges;
    private EditText mPhotoCharges;
    private EditText mReason;
    private RadioGroup mPoliceReport;
    private RadioGroup mInvestigateClaim;
    private RadioGroup mRepairCompleted;
    private RadioGroup mSalvageReceived;
    private RadioGroup mSettlementMethod;
    private RadioButton mPoliceReportSelectedYes;
    private RadioButton mPoliceReportSelectedNo;
    private RadioButton mInvestigateClaimSelectedYes;
    private RadioButton mInvestigateClaimSelectedNo;
    private RadioButton mRepairCompletedYes;
    private RadioButton mSalvageReceivedYes;
    private RadioButton mRepairCompletedNo;
    private RadioButton mSalvageReceivedNo;
    private RadioButton mSettlementMethodOnsiteOffer;
    private RadioButton mSettlementMethodCallEstimate;
    private EditText mPayableAmount;
    private EditText mOnsiteOfferAmount;
    private ProgressDialog mProgressDialog;
    private LinearLayout ariOnlyLayout;
    private LinearLayout linearLayoutCommon1;
    private LinearLayout linearLayoutCommon2;
    private LinearLayout linearLayoutCommon3;
    private LinearLayout linearLayoutPayableAmount;
    private Long rowid;
    private int intimNo;
    //private int seqNo;
    private String vehicle;
    private int insType;
    private String policy;
    private String loc;
    private String caller;
    private String callerCont;
    private String yom;
    private double sumInsu;
    private double excess;
    private String facilty;
    private String siteOffer;
    private String cCRemark;
    private String inspDate;
    private double acr;
    private String assRemark;
    private String respondDate;
    //private String user;
    private String cDate;
    private String status;
    private double preAccValue;
    private double baldTirePenalty;
    private double underInsPenalty;
    private String inspectionRemarks;
    private String specialRemarks;
    private double proffesionalFee;
    //private double miles;
    private double rate;
    private double assMileage;
    private double telephone;
    private int imageCount;
    private double price;
    private double photoCharges;
    private double otherCharges;
    private double totalCharges;
    private String reason;
    private String policeReport;
    private String investigateClaim;
    private String repairCompleted;
    private String salvageReceived;
    private double payableAmount;
    private String settlementMethod;
    private double onsiteOfferAmount;
    private ArrayList<String> selectedImagePath;

    private Claim mClaim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_claim);

        String jsonPendingClaimObject = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonPendingClaimObject = extras.getString("completedClaimObject");
            mClaim = new Gson().fromJson(jsonPendingClaimObject, Claim.class);
            rowid = mClaim.getId();
            insType = mClaim.getInspType();
        }

        ariOnlyLayout = (LinearLayout) this.findViewById(R.id.LinearLayoutACR);// Repair
        // Completed,
        // Salvage
        // Received
        linearLayoutCommon1 = (LinearLayout) this
                .findViewById(R.id.LinearLayoutCommon1);// Sum Insured, ACR...
        linearLayoutCommon2 = (LinearLayout) this
                .findViewById(R.id.LinearLayoutCommon2);// Police Report
        // Requested ,Special
        // Remarks,Investigate
        // Claim
        linearLayoutCommon3 = (LinearLayout) this
                .findViewById(R.id.LinearLayoutCommon3);// Settlement Method, On
        // Site Offer Amount
        linearLayoutPayableAmount = (LinearLayout) this
                .findViewById(R.id.LinearLayoutPayableAmount);// Payable Amount

        mIntimNo = (EditText) findViewById(R.id.txt_claim_intim_no);
        mIntimNo.setEnabled(false);
        // mSeqNo = (EditText) findViewById(R.id.txt_claim_seq);
        mVehicle = (EditText) findViewById(R.id.txt_claim_vehicle);
        mVehicle.setEnabled(false);
        mInsType = (Spinner) findViewById(R.id.spn_claim_ins_type);
        mInsType.setEnabled(false);
        mPolicy = (EditText) findViewById(R.id.txt_claim_policy);
        mPolicy.setEnabled(false);
        mLoc = (EditText) findViewById(R.id.txt_claim_loc);
        mLoc.setEnabled(false);
        mCaller = (EditText) findViewById(R.id.txt_claim_caller);
        mCaller.setEnabled(false);
        mCallerCont = (EditText) findViewById(R.id.txt_claim_contact_no);
        mCallerCont.setEnabled(false);
        mYom = (EditText) findViewById(R.id.txt_claim_yom);
        mYom.setEnabled(false);
        mSumInsu = (EditText) findViewById(R.id.txt_claim_sum_insured);
        mSumInsu.setEnabled(false);
        mExcess = (EditText) findViewById(R.id.txt_claim_excess);
        mExcess.setEnabled(false);
        //mFaciliy = (CheckBox) findViewById(R.id.chk_claim_fac);
        //mSiteOffer = (CheckBox) findViewById(R.id.chk_claim_site_offer);
        mCcRemark = (EditText) findViewById(R.id.txt_claim_cc_remark);
        mCcRemark.setEnabled(false);
        mAcr = (EditText) findViewById(R.id.txt_claim_acr);
        mAcr.setEnabled(false);
        // mClaimMileage = (EditText) findViewById(R.id.txt_claim_mileage);
        mAssRemark = (EditText) findViewById(R.id.txt_claim_ass_remark);
        mAssRemark.setEnabled(false);

        // newly added fields, by DumindaW
        mPreAccValue = (EditText) findViewById(R.id.txt_pre_acc_val);
        mPreAccValue.setEnabled(false);
        mBaldTirePenalty = (EditText) findViewById(R.id.txt_bald_tire_penalty_val);
        //mBaldTirePenalty.setFilters(new InputFilter[]{new InputFilterMinMax("0.00", "99.99")});
        mBaldTirePenalty.setEnabled(false);
        mUnderInsPenalty = (EditText) findViewById(R.id.txt_under_ins_penalty);
        mUnderInsPenalty.setEnabled(false);
        //mUnderInsPenalty.setFilters(new InputFilter[]{new InputFilterMinMax("0.00", "99.99")});

        mInspDate = (EditText) findViewById(R.id.txt_claim_insp_date);
        mInspDate.setEnabled(false);
        mInspTime = (EditText) findViewById(R.id.txt_claim_insp_time);
        mInspTime.setEnabled(false);

        mInspectionRemarks = (EditText) findViewById(R.id.txt_inspection_remark);
        mInspectionRemarks.setEnabled(false);
        mSpecialRemarks = (EditText) findViewById(R.id.txt_special_remarks);
        mSpecialRemarks.setEnabled(false);
        mProffesionalFee = (EditText) findViewById(R.id.txt_proffesional_fee);
        mProffesionalFee.setEnabled(false);
        mMileage = (EditText) findViewById(R.id.txt_milage);
        mMileage.setEnabled(false);
        mTelephone = (EditText) findViewById(R.id.txt_telephone);
        mTelephone.setEnabled(false);
        mCopies = (EditText) findViewById(R.id.txt_photoCount);
        mCopies.setEnabled(false);
        mPhotoCharges = (EditText) findViewById(R.id.txt_photoCharges);
        mPhotoCharges.setEnabled(false);
        mOtherCharges = (EditText) findViewById(R.id.txt_other_charges);
        mOtherCharges.setEnabled(false);
        mTotalCharges = (EditText) findViewById(R.id.txt_total_charges);
        mTotalCharges.setEnabled(false);
        mReason = (EditText) findViewById(R.id.txt_reason);
        mReason.setEnabled(false);
        mPoliceReport = (RadioGroup) findViewById(R.id.radiogroup_police_report);
        mPoliceReport.setEnabled(false);
        mInvestigateClaim = (RadioGroup) findViewById(R.id.radiogroup_investigate_claim);
        mInvestigateClaim.setEnabled(false);
        mRepairCompleted = (RadioGroup) findViewById(R.id.radiogroup_repair_completed);
        mRepairCompleted.setEnabled(false);
        mSalvageReceived = (RadioGroup) findViewById(R.id.radiogroup_salvage_received);
        mSalvageReceived.setEnabled(false);
        mSettlementMethod = (RadioGroup) findViewById(R.id.radiogroup_settlment_method);
        mSettlementMethod.setEnabled(false);

        mPoliceReportSelectedYes = (RadioButton) findViewById(R.id.radio_police_report_yes);
        mPoliceReportSelectedNo = (RadioButton) findViewById(R.id.radio_police_report_no);
        mPoliceReportSelectedNo.setChecked(true);

        mInvestigateClaimSelectedYes = (RadioButton) findViewById(R.id.radio_investigate_claim_yes);
        mInvestigateClaimSelectedNo = (RadioButton) findViewById(R.id.radio_investigate_claim_no);
        mInvestigateClaimSelectedNo.setChecked(true);

        mPoliceReportSelectedNo.setEnabled(false);
        mPoliceReportSelectedYes.setEnabled(false);
        mInvestigateClaimSelectedYes.setEnabled(false);
        mInvestigateClaimSelectedNo.setEnabled(false);

        mRepairCompletedYes = (RadioButton) findViewById(R.id.radio_repair_completed_yes);
        mRepairCompletedNo = (RadioButton) findViewById(R.id.radio_repair_completed_no);
        mSalvageReceivedYes = (RadioButton) findViewById(R.id.radio_salvage_received_yes);
        mSalvageReceivedNo = (RadioButton) findViewById(R.id.radio_salvage_received_no);
        mSettlementMethodOnsiteOffer = (RadioButton) findViewById(R.id.radio_settlment_onsite_offer);
        mSettlementMethodCallEstimate = (RadioButton) findViewById(R.id.radio_settlment_call_estimate);
        mSettlementMethodCallEstimate.setChecked(true);

        mRepairCompletedYes.setEnabled(false);
        mRepairCompletedNo.setEnabled(false);
        mSalvageReceivedYes.setEnabled(false);
        mSalvageReceivedNo.setEnabled(false);
        mSettlementMethodOnsiteOffer.setEnabled(false);
        mSettlementMethodCallEstimate.setEnabled(false);

        mPayableAmount = (EditText) findViewById(R.id.txt_payable_amount);
		mPayableAmount.setEnabled(false);
        mOnsiteOfferAmount = (EditText) findViewById(R.id.txt_onsite_offer_amount);
		mOnsiteOfferAmount.setEnabled(false);
        // end newly added fields

        //confirmButton = (Button) findViewById(R.id.todo_edit_button);
        btnInspDate = (ImageButton) findViewById(R.id.btn_claim_insp_date);
        btnInspTime = (ImageButton) findViewById(R.id.btn_claim_insp_time);


        final InspectionType items[] = new InspectionType[8];
        items[0] = new InspectionType("On Site Inspection", 0);
        items[1] = new InspectionType("Garage Inspection", 1);
        items[2] = new InspectionType("D/R Inspection", 2);
        items[3] = new InspectionType("Supplementary Inspection", 3);
        items[4] = new InspectionType("ARI", 4);
        items[5] = new InspectionType("Re inspection", 5);
        items[6] = new InspectionType("Under Writing Inspection", 6);
        items[7] = new InspectionType("Special Inspection", 7);

        ArrayAdapter<InspectionType> adapter = new ArrayAdapter<InspectionType>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mInsType.setAdapter(adapter);
        mInsType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // MyData d = items[position];
                // insType = d.getValue();
                mInsType.setSelection(insType);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mInspDate.setFocusable(true);
        mInspTime.setFocusable(true);
        mAcr.setFocusable(true);
        // mClaimMileage.setFocusable(true);
        mAssRemark.setFocusable(true);

        mPreAccValue.setFocusable(true);
        mBaldTirePenalty.setFocusable(true);
        mUnderInsPenalty.setFocusable(true);

        mPreAccValue.setFocusable(true);
        mBaldTirePenalty.setFocusable(true);
        mUnderInsPenalty.setFocusable(true);
        mInspectionRemarks.setFocusable(true);
        mSpecialRemarks.setFocusable(true);
        mProffesionalFee.setFocusable(true);
        mMileage.setFocusable(true);
        mTelephone.setFocusable(true);
        mCopies.setFocusable(true);
        mPhotoCharges.setFocusable(false);
        mOtherCharges.setFocusable(true);
        mTotalCharges.setFocusable(true);
        mReason.setFocusable(true);
        mPoliceReport.setFocusable(true);
        mInvestigateClaim.setFocusable(true);
        mPayableAmount.setFocusable(true);
        mRepairCompleted.setFocusable(true);
        mSalvageReceived.setFocusable(true);
        mSettlementMethod.setFocusable(true);
        mOnsiteOfferAmount.setFocusable(true);

        //confirmButton.setVisibility(View.VISIBLE);

        mIntimNo.setFocusable(false);
        mPolicy.setFocusable(false);
        mVehicle.setFocusable(false);
        mInsType.setFocusable(false);
        mLoc.setFocusable(false);
        mCaller.setFocusable(false);
        mCallerCont.setFocusable(false);
        mYom.setFocusable(false);
        mSumInsu.setFocusable(false);
        mExcess.setFocusable(false);
        mCcRemark.setFocusable(false);

        // Show Hide fields according to the Inspection Type
        if (insType == 0) {// On Site Inspection
            linearLayoutCommon1.setVisibility(LinearLayout.VISIBLE);
            linearLayoutPayableAmount.setVisibility(LinearLayout.VISIBLE);
            ariOnlyLayout.setVisibility(LinearLayout.GONE);
            linearLayoutCommon3.setVisibility(LinearLayout.VISIBLE);
            linearLayoutCommon2.setVisibility(LinearLayout.VISIBLE);
        } else if (insType == 1) {// Garage Inspection
            linearLayoutCommon1.setVisibility(LinearLayout.VISIBLE);
            linearLayoutPayableAmount.setVisibility(LinearLayout.VISIBLE);
            ariOnlyLayout.setVisibility(LinearLayout.GONE);
            linearLayoutCommon3.setVisibility(LinearLayout.GONE);
        } else if (insType == 2 || insType == 3) {// DR & Supplementary
            linearLayoutCommon1.setVisibility(LinearLayout.VISIBLE);
            linearLayoutCommon2.setVisibility(LinearLayout.GONE);
            linearLayoutCommon3.setVisibility(LinearLayout.GONE);
            linearLayoutPayableAmount.setVisibility(LinearLayout.GONE);
            ariOnlyLayout.setVisibility(LinearLayout.GONE);
        } else if (insType == 4) {// ARI
            linearLayoutCommon1.setVisibility(LinearLayout.GONE);
            linearLayoutCommon2.setVisibility(LinearLayout.GONE);
            linearLayoutCommon3.setVisibility(LinearLayout.GONE);
            linearLayoutPayableAmount.setVisibility(LinearLayout.GONE);
            ariOnlyLayout.setVisibility(LinearLayout.VISIBLE);
        } else {
            linearLayoutCommon1.setVisibility(LinearLayout.VISIBLE);
        }

        mSettlementMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_settlment_onsite_offer) {
                    mPoliceReportSelectedNo.setChecked(true);
                    mInvestigateClaimSelectedNo.setChecked(true);
                    mInvestigateClaimSelectedYes.setEnabled(false);
                    mPoliceReportSelectedYes.setEnabled(false);
                } else if (checkedId == R.id.radio_settlment_call_estimate) {
                    mPoliceReportSelectedYes.setEnabled(true);
                    mInvestigateClaimSelectedYes.setEnabled(true);
                }
            }
        });

        mDbHelper = new DatabaseHandler(getApplicationContext());

        populateFields();

        mManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }*/
        mAccount = mManager.getAccountsByType(ACCOUNT_TYPE)[0];
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (rowid == null) {
            populateFields();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        int myMenu;
        myMenu = R.menu.menu_policy_view;
        getMenuInflater().inflate(myMenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.item_view_images:
                viewImages();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void viewImages() {
        Intent myIntent = new Intent(this, GalleryViewActivity.class);
        myIntent.putExtra(KEY_IMG_INTIM, intimNo);
        myIntent.putExtra(KEY_IMG_INSP_TYPE, insType);

        startActivityForResult(myIntent, VIEW_IMAGES);
    }

    private void captureImages() {
        Intent myIntent = new Intent(this, CameraCaptureActivity.class);
        myIntent.putExtra(KEY_IMG_INTIM, intimNo);
        myIntent.putExtra(KEY_IMG_INSP_TYPE, insType);
        if (CameraCaptureActivity.capturedUris != null)
            CameraCaptureActivity.capturedUris.clear();

        startActivityForResult(myIntent, CAPTURE_IMAGE);
    }

    // Method to populate details
    private void populateFields() {

        EditText mInspDate = (EditText) findViewById(R.id.txt_claim_insp_date);
        EditText mInspTime = (EditText) findViewById(R.id.txt_claim_insp_time);

        if (rowid != null) {

            Claim claim = mDbHelper.getClaim(rowid);

            intimNo = claim.getIntimationNo();

            mIntimNo.setText("" + intimNo);

            vehicle = claim.getVehicleNo();
            mVehicle.setText(vehicle);

            // setting spinner position
            insType = claim.getInspType();

            for (int i = 0; i < mInsType.getCount(); i++) {

                InspectionType s = (InspectionType) mInsType.getItemAtPosition(i);
                if (s.getValue() == insType) {
                    mInsType.setSelection(i);
                }
            }

            policy = claim.getPolicyNo();
            mPolicy.setText(policy);

            loc = claim.getLocation();
            mLoc.setText(loc);

            caller = claim.getCallerName();
            mCaller.setText(caller);

            callerCont = claim.getContactNo();
            mCallerCont.setText(callerCont);

            yom = claim.getYom();
            mYom.setText("" + yom);

            sumInsu = claim.getSumInsured();
            // mSumInsu.setText("" + sumInsu);
            mSumInsu.setText((NumberFormat.getCurrencyInstance()
                    .format((sumInsu))).replace(NumberFormat
                    .getCurrencyInstance().getCurrency().getSymbol(), ""));

            excess = claim.getExcessAmt();
            // mExcess.setText("" + excess);
            mExcess.setText((NumberFormat.getCurrencyInstance()
                    .format((excess))).replace(NumberFormat
                    .getCurrencyInstance().getCurrency().getSymbol(), ""));

            cCRemark = claim.getCallCenterRemark();
            mCcRemark.setText(cCRemark);

            inspDate = claim.getInspDate();

            if (inspDate != null && !inspDate.equals("")) {
                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd");
                SimpleDateFormat sdfTime = new SimpleDateFormat("KK:mm a");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(inspDate));
                /*SimpleDateFormat sdfDate = new SimpleDateFormat(
                        DateTimeHelper.DATE_FORMAT, Locale.US);
                SimpleDateFormat sdfTime = new SimpleDateFormat(
                        DateTimeHelper.TIME_FORMAT_MIN, Locale.US);
                SimpleDateFormat sdf24 = new SimpleDateFormat(
                        DateTimeHelper.DATE_TIME_FORMAT_24, Locale.US);*/
                try {
                    mInspDate.setText(sdfDate.format(calendar.getTime()));
                } catch (Exception e) {
                    Log.w(ViewClaimActivity.class.getName(),
                            "Parse Exception when casting to date.. (Populate Fields)");
                    mInspDate.setText("");
                }

                try {
                    mInspTime.setText(sdfTime.format(calendar.getTime()));
                    //mInspTime.setText(sdfTime.format(sdf24.parse(String.valueOf(mInspTime))));
                } catch (Exception e) {
                    Log.w(ViewClaimActivity.class.getName(),
                            "Parse Exception when casting to time.. (Populate Fields)");
                    mInspTime.setText("");
                }

            } else {
                mInspDate.setText("");
            }

            acr = claim.getAcr();
            if (acr  > 0)
                mAcr.setText("" + acr);

            assRemark = claim.getAssessorRemark();
            mAssRemark.setText(assRemark);

            respondDate = claim.getRespondDate();

            // for test
            cDate = claim.getcDate();

            preAccValue = claim.getPreAccValue();
            if (preAccValue > 0)
                mPreAccValue.setText("" + preAccValue);

            baldTirePenalty = claim.getBaldTirePenalty();
            if (baldTirePenalty  > 0)
                mBaldTirePenalty.setText("" + baldTirePenalty);

            underInsPenalty = claim.getUnderInsPenalty();
            if (underInsPenalty  > 0)
                mUnderInsPenalty.setText("" + underInsPenalty);

            inspectionRemarks = claim.getInspectionRemarks();
            mInspectionRemarks.setText(inspectionRemarks);

            specialRemarks = claim.getSpecialRemarks();
            mSpecialRemarks.setText(specialRemarks);

            proffesionalFee = claim.getProffesionalFee();
            if (proffesionalFee  > 0)
                mProffesionalFee.setText("" + proffesionalFee);

            telephone = claim.getTelephone();
            if (telephone  > 0)
                mTelephone.setText("" + telephone);

            imageCount = claim.getCopies();
            if (imageCount  > 0)
                mCopies.setText("" + imageCount);

            otherCharges = claim.getOtherCharges();
            if (otherCharges  > 0)
                mOtherCharges.setText("" + otherCharges);

            totalCharges = claim.getTotalCharges();
            if (totalCharges  > 0)
                mTotalCharges.setText("" + totalCharges);

            reason = claim.getReason();
            mReason.setText(reason);

            policeReport = claim.getPoliceReport();

            if (policeReport != null && !policeReport.equals("")) {
                if (policeReport.equals("Y"))
                    mPoliceReportSelectedYes.setChecked(true);
                else
                    mPoliceReportSelectedNo.setChecked(true);
            }

            investigateClaim = claim.getInvestigateClaim();

            if (investigateClaim != null && !investigateClaim.equals("")) {
                if (investigateClaim.equals("Y"))
                    mInvestigateClaimSelectedYes.setChecked(true);
                else
                    mInvestigateClaimSelectedNo.setChecked(true);
            }

            payableAmount = claim.getPayableAmount();
            if (payableAmount  > 0)
                mPayableAmount.setText("" + payableAmount);

            onsiteOfferAmount = claim.getOnsiteOfferAmount();
            if (onsiteOfferAmount  > 0)
                mOnsiteOfferAmount.setText("" + onsiteOfferAmount);

            repairCompleted = claim.getRepairCompleted();

            if (repairCompleted != null && !repairCompleted.equals("")) {
                if (repairCompleted.equals("Y"))
                    mRepairCompletedYes.setChecked(true);
                else
                    mRepairCompletedNo.setChecked(true);
            }

            salvageReceived = claim.getSalvageReceived();

            if (salvageReceived != null && !salvageReceived.equals("")) {
                if (salvageReceived.equals("Y"))
                    mSalvageReceivedYes.setChecked(true);
                else
                    mSalvageReceivedNo.setChecked(true);
            }

            settlementMethod = claim.getSettlementMethod();

            if (settlementMethod != null && !settlementMethod.equals("")) {
                if (settlementMethod.equals("Call Estimate"))
                    mSettlementMethodCallEstimate.setChecked(true);
                else
                    mSettlementMethodOnsiteOffer.setChecked(true);
            }
            if (settlementMethod != null &&  !settlementMethod.equals("")) {
                if (settlementMethod.equals("Call Estimate")) {
                    mPoliceReportSelectedYes.setEnabled(true);
                    mInvestigateClaimSelectedYes.setEnabled(true);
                } else {
                    mPoliceReportSelectedNo.setChecked(true);
                    mInvestigateClaimSelectedNo.setChecked(true);
                    //mPoliceReportSelectedYes.setEnabled(false);
                    mInvestigateClaimSelectedYes.setEnabled(false);
                    mPoliceReportSelectedYes.setEnabled(false);
                }
            }
        }
    }
}

package com.loits.insurance.cm;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loits.insurance.cm.db.DatabaseHandler;
import com.loits.insurance.cm.interfaces.ActionResponseCallback;
import com.loits.insurance.cm.models.ActionResponse;
import com.loits.insurance.cm.models.Claim;
import com.loits.insurance.cm.models.DateTimeHelper;
import com.loits.insurance.cm.models.Image;
import com.loits.insurance.cm.models.InspectionType;
import com.loits.insurance.cm.models.JobAction;
import com.loits.insurance.cm.network.Service;
import com.loits.insurance.cm.util.ImageUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.loits.insurance.cm.util.Constants.ACCOUNT_TYPE;
import static com.loits.insurance.cm.util.Constants.CAPTURE_IMAGE;
import static com.loits.insurance.cm.util.Constants.IDS_CLIENT_ID;
import static com.loits.insurance.cm.util.Constants.IDS_CLIENT_SECRET;
import static com.loits.insurance.cm.util.Constants.KEY_IMG_INSP_TYPE;
import static com.loits.insurance.cm.util.Constants.KEY_IMG_INTIM;
import static com.loits.insurance.cm.util.Constants.KEY_USERNAME;
import static com.loits.insurance.cm.util.Constants.SELECT_MULTY;
import static com.loits.insurance.cm.util.Constants.VIEW_IMAGES;

public class UpdateClaimActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener{

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
    private ArrayList<ImageData> imageData;
    private ArrayList<String> selectedImagePath;
    private int sequenceNo;

    private Claim mClaim;
    View parentLayout;

    Service mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_claim);

        mService = new Service(this, IDS_CLIENT_ID, IDS_CLIENT_SECRET);

        String jsonPendingClaimObject = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonPendingClaimObject = extras.getString("pendingJobObject");
            mClaim = new Gson().fromJson(jsonPendingClaimObject, Claim.class);
            rowid = mClaim.getId();
            insType = mClaim.getInspType();
            sequenceNo = mClaim.getSeqNo();
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
        // mSeqNo = (EditText) findViewById(R.id.txt_claim_seq);
        mVehicle = (EditText) findViewById(R.id.txt_claim_vehicle);
        mInsType = (Spinner) findViewById(R.id.spn_claim_ins_type);
        mPolicy = (EditText) findViewById(R.id.txt_claim_policy);
        mLoc = (EditText) findViewById(R.id.txt_claim_loc);
        mCaller = (EditText) findViewById(R.id.txt_claim_caller);
        mCallerCont = (EditText) findViewById(R.id.txt_claim_contact_no);
        mYom = (EditText) findViewById(R.id.txt_claim_yom);
        mSumInsu = (EditText) findViewById(R.id.txt_claim_sum_insured);
        mExcess = (EditText) findViewById(R.id.txt_claim_excess);
        //mFaciliy = (CheckBox) findViewById(R.id.chk_claim_fac);
        //mSiteOffer = (CheckBox) findViewById(R.id.chk_claim_site_offer);
        mCcRemark = (EditText) findViewById(R.id.txt_claim_cc_remark);
        mAcr = (EditText) findViewById(R.id.txt_claim_acr);
        mAcr.setFilters(new InputFilter[]{new InputFilterMinMax("0.00", "999999.99")});
        // mClaimMileage = (EditText) findViewById(R.id.txt_claim_mileage);
        mAssRemark = (EditText) findViewById(R.id.txt_claim_ass_remark);

        // newly added fields, by DumindaW
        mPreAccValue = (EditText) findViewById(R.id.txt_pre_acc_val);
        mPreAccValue.setFilters(new InputFilter[]{new InputFilterMinMax("0.00", "999999.99")});
        mBaldTirePenalty = (EditText) findViewById(R.id.txt_bald_tire_penalty_val);
        mBaldTirePenalty.setFilters(new InputFilter[]{new InputFilterMinMax("0.00", "99.99")});
        mUnderInsPenalty = (EditText) findViewById(R.id.txt_under_ins_penalty);
        mUnderInsPenalty.setFilters(new InputFilter[]{new InputFilterMinMax("0.00", "99.99")});

        mInspDate = (EditText) findViewById(R.id.txt_claim_insp_date);
        mInspTime = (EditText) findViewById(R.id.txt_claim_insp_time);
        mInspDate.setEnabled(false);
        mInspTime.setEnabled(false);

        mInspectionRemarks = (EditText) findViewById(R.id.txt_inspection_remark);
        mSpecialRemarks = (EditText) findViewById(R.id.txt_special_remarks);
        mProffesionalFee = (EditText) findViewById(R.id.txt_proffesional_fee);
        mProffesionalFee.setFilters(new InputFilter[]{new InputFilterMinMax("0.00", "9999.99")});
        //mMiles = (EditText) findViewById(R.id.txt_miles);
        //mRate = (EditText) findViewById(R.id.txt_rate);
        mMileage = (EditText) findViewById(R.id.txt_milage);
        mMileage.setFilters(new InputFilter[]{new InputFilterMinMax("0.00", "999.99")});
        mTelephone = (EditText) findViewById(R.id.txt_telephone);
        mTelephone.setFilters(new InputFilter[]{new InputFilterMinMax("0.00", "999.99")});
        mCopies = (EditText) findViewById(R.id.txt_photoCount);
        mCopies.setFilters(new InputFilter[]{new InputFilterMinMax("0.00", "25.00")});
    /*	mPrice = (EditText) findViewById(R.id.txt_price);*/
        //mPhotoCharges = (EditText) findViewById(R.id.txt_photoCharges);
        mPhotoCharges = (EditText) findViewById(R.id.txt_photoCharges);
        mOtherCharges = (EditText) findViewById(R.id.txt_other_charges);
        mOtherCharges.setFilters(new InputFilter[]{new InputFilterMinMax("0.00", "9999.99")});
        mTotalCharges = (EditText) findViewById(R.id.txt_total_charges);
        mReason = (EditText) findViewById(R.id.txt_reason);

        mPoliceReport = (RadioGroup) findViewById(R.id.radiogroup_police_report);
        mInvestigateClaim = (RadioGroup) findViewById(R.id.radiogroup_investigate_claim);

        mRepairCompleted = (RadioGroup) findViewById(R.id.radiogroup_repair_completed);
        mSalvageReceived = (RadioGroup) findViewById(R.id.radiogroup_salvage_received);
        mSettlementMethod = (RadioGroup) findViewById(R.id.radiogroup_settlment_method);

        mPoliceReportSelectedYes = (RadioButton) findViewById(R.id.radio_police_report_yes);
        mPoliceReportSelectedNo = (RadioButton) findViewById(R.id.radio_police_report_no);
        mPoliceReportSelectedNo.setChecked(true);
        mInvestigateClaimSelectedYes = (RadioButton) findViewById(R.id.radio_investigate_claim_yes);
        mInvestigateClaimSelectedNo = (RadioButton) findViewById(R.id.radio_investigate_claim_no);
        mInvestigateClaimSelectedNo.setChecked(true);

        mRepairCompletedYes = (RadioButton) findViewById(R.id.radio_repair_completed_yes);
        mRepairCompletedNo = (RadioButton) findViewById(R.id.radio_repair_completed_no);
        mSalvageReceivedYes = (RadioButton) findViewById(R.id.radio_salvage_received_yes);
        mSalvageReceivedNo = (RadioButton) findViewById(R.id.radio_salvage_received_no);
        mSettlementMethodOnsiteOffer = (RadioButton) findViewById(R.id.radio_settlment_onsite_offer);
        mSettlementMethodCallEstimate = (RadioButton) findViewById(R.id.radio_settlment_call_estimate);
        mSettlementMethodCallEstimate.setChecked(true);

        mPayableAmount = (EditText) findViewById(R.id.txt_payable_amount);
        mOnsiteOfferAmount = (EditText) findViewById(R.id.txt_onsite_offer_amount);
        // end newly added fields

        //confirmButton = (Button) findViewById(R.id.todo_edit_button);
        btnInspDate = (ImageButton) findViewById(R.id.btn_claim_insp_date);
        btnInspTime = (ImageButton) findViewById(R.id.btn_claim_insp_time);

        //confirmButton.setOnClickListener(this);
        btnInspDate.setOnClickListener(this);
        btnInspTime.setOnClickListener(this);
        mTotalCharges.setOnFocusChangeListener(this);
        mPayableAmount.setOnFocusChangeListener(this);
        parentLayout = findViewById(R.id.updateClaim);
        //confirmButton.setFocusable(true);

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

        /*if (Mode.equals("COMPLETE")) {
            myMenu = R.menu.menu_policy_view;
        } else {
            myMenu = R.menu.menu_policy_edit;
        }*/
        myMenu = R.menu.menu_policy_edit;
        getMenuInflater().inflate(myMenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_add_images:
                addImages();
                return true;
            case R.id.item_view_images:
                viewImages();
                return true;
            case R.id.item_capture:
                captureImages();
                return true;
            case R.id.item_sync:
                updateAsCompleted();
                return true;
            case R.id.action_save:
                confirmSaveRecord();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void confirmSaveRecord() {

        EditText mInspDate = (EditText) findViewById(R.id.txt_claim_insp_date);
        EditText mInspTime = (EditText) findViewById(R.id.txt_claim_insp_time);

        String vInspectionDate = mInspDate.getText().toString();
        String vInspectionTime = mInspTime.getText().toString();

        if (validateInspectionDateTime(vInspectionDate, vInspectionTime) > 0) {

            //String date  = getDateString(validateInspectionDateTime(vInspectionDate, vInspectionTime),"yyyy-MM-dd");
            inspDate = String.valueOf(validateInspectionDateTime(vInspectionDate, vInspectionTime));
        } else {
            Snackbar snackbar = Snackbar
                    .make(parentLayout, "Invalid Date", Snackbar.LENGTH_SHORT);

            snackbar.show();
            return;
        }
        if (!mAcr.getText().toString().equals("")) {
            acr = Double.parseDouble(mAcr.getText().toString());
        } else {
            acr = -1;
        }
        assRemark = mAssRemark.getText().toString();

        if (!mPreAccValue.getText().toString().equals("")) {
            preAccValue = Double.parseDouble(mPreAccValue.getText()
                    .toString());
        } else {
            preAccValue = -1;
        }

        if (!mBaldTirePenalty.getText().toString().equals("")) {
            baldTirePenalty = Double.parseDouble(mBaldTirePenalty
                    .getText().toString());
        } else {
            baldTirePenalty = -1;
        }

        if (!mUnderInsPenalty.getText().toString().equals("")) {
            underInsPenalty = Double.parseDouble(mUnderInsPenalty
                    .getText().toString());
        } else {
            underInsPenalty = -1;
        }

        inspectionRemarks = mInspectionRemarks.getText().toString();
        specialRemarks = mSpecialRemarks.getText().toString();

        if (!mProffesionalFee.getText().toString().equals("")) {
            proffesionalFee = Double.parseDouble(mProffesionalFee
                    .getText().toString());
        } else {
            proffesionalFee = -1;
        }
        if (!mMileage.getText().toString().equals("")) {
            assMileage = Double.parseDouble(mMileage.getText().toString());
            mMileage.setText("" + assMileage);
        } else {
            mMileage.setText("");
            assMileage = -1;
        }
        if (!mTelephone.getText().toString().equals("")) {
            telephone = Double.parseDouble(mTelephone.getText().toString());
            mTelephone.setText("" + telephone);
        } else {
            mTelephone.setText("");
            telephone = -1;
        }
        if (!mCopies.getText().toString().equals("")) {
            imageCount = Integer.parseInt(mCopies.getText().toString());
            mCopies.setText("" + imageCount);
        } else {
            mCopies.setText("");
            imageCount = -1;
        }
        if (!mOtherCharges.getText().toString().equals("")) {
            otherCharges = Double.parseDouble(mOtherCharges.getText()
                    .toString());
        } else {
            otherCharges = -1;
        }

        if (!mTotalCharges.getText().toString().equals("")) {

            double proffFee = 0.0;
            double mileageCharges = 0.0;
            double photoCharges = 0.0;
            double telephoneCharges = 0.0;
            double otherCharges = 0.0;
            int noOfCopies = 0;

            if (!mProffesionalFee.getText().toString().equals(""))
                proffFee = Double.parseDouble(mProffesionalFee
                        .getText().toString());
            if (!mMileage.getText().toString().equals(""))
                mileageCharges = Double.parseDouble(mMileage.getText()
                        .toString()) * 20;
            if (!mTelephone.getText().toString().equals(""))
                telephoneCharges = Double.parseDouble(mTelephone.getText()
                        .toString());

            if (!mCopies.getText().toString().equals("")) {
                noOfCopies = Integer.parseInt(mCopies
                        .getText().toString());

                photoCharges = noOfCopies * 20;
                mPhotoCharges.setText("" + photoCharges);

            }

            if (!mOtherCharges.getText().toString().equals(""))
                otherCharges = Double.parseDouble(mOtherCharges
                        .getText().toString());

            totalCharges = proffFee + mileageCharges + photoCharges + telephoneCharges
                    + otherCharges;

            mTotalCharges.setText("" + totalCharges);
        } else {
            totalCharges = -1;
        }

        reason = mReason.getText().toString();

        if (mPoliceReport.getCheckedRadioButtonId() != -1) {

            if (((RadioButton) findViewById(mPoliceReport
                    .getCheckedRadioButtonId())).getText().toString()
                    .equals("Yes")) {
                policeReport = "Y";
            } else {
                policeReport = "N";
            }

        } else {
            policeReport = "N";
        }

        if (mInvestigateClaim.getCheckedRadioButtonId() != -1) {

            if (((RadioButton) findViewById(mInvestigateClaim
                    .getCheckedRadioButtonId())).getText().toString()
                    .equals("Yes")) {
                investigateClaim = "Y";
            } else {
                investigateClaim = "N";
            }
        } else {
            investigateClaim = "";
        }

        if (!mPayableAmount.getText().toString().equals("")) {
            payableAmount = Double.parseDouble(mPayableAmount.getText()
                    .toString());
        } else {
            payableAmount = -1;
        }

        if (!mOnsiteOfferAmount.getText().toString().equals("")) {
            onsiteOfferAmount = Double.parseDouble(mOnsiteOfferAmount
                    .getText().toString());
        } else {
            onsiteOfferAmount = -1;
        }

        if (mRepairCompleted.getCheckedRadioButtonId() != -1) {

            if (((RadioButton) findViewById(mRepairCompleted
                    .getCheckedRadioButtonId())).getText().toString()
                    .equals("Yes")) {
                repairCompleted = "Y";
            } else {
                repairCompleted = "N";
            }

        } else {
            repairCompleted = "";
        }

        if (mSalvageReceived.getCheckedRadioButtonId() != -1) {

            if (((RadioButton) findViewById(mSalvageReceived
                    .getCheckedRadioButtonId())).getText().toString()
                    .equals("Yes")) {
                salvageReceived = "Y";
            } else {
                salvageReceived = "N";
            }

        } else {
            salvageReceived = "";
        }

        if (mSettlementMethod.getCheckedRadioButtonId() != -1) {
            settlementMethod = ((RadioButton) findViewById(mSettlementMethod
                    .getCheckedRadioButtonId())).getText().toString();
        } else {
            settlementMethod = "";
        }

        //user = mManager.getUserData(mAccount, KEY_USERNAME).toUpperCase();

        mProgressDialog = ProgressDialog.show(
                UpdateClaimActivity.this, "Please wait...",
                "Saving...", true);

        new Thread() {
            public void run() {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                List<Image> images = mDbHelper.getAllImages(intimNo, insType);
                int lastImgNo = 0;
                lastImgNo = images.size();

                Claim claim;

                try {
                    // do the background process or any work that takes
                    // time to see progress dialog
                    if (rowid == null) {

                        claim =
                                new Claim(intimNo, insType, policy, vehicle, loc, callerCont,
                                caller, yom, sumInsu, excess, facilty, siteOffer, cCRemark, respondDate, sequenceNo);
                    } else {

                        if (preAccValue == -1)
                            preAccValue = 0;
                        if (baldTirePenalty == -1)
                            baldTirePenalty = 0;
                        if (underInsPenalty == -1)
                            underInsPenalty = 0;
                        if (proffesionalFee == -1)
                            proffesionalFee = 0;
                        if (rate == -1)
                            rate = 0;
                        if (assMileage == -1)
                            assMileage = 0;
                        if (imageCount == -1)
                            imageCount = 0;
                        if (price == -1)
                            price = 0;
                        if (photoCharges == -1)
                            photoCharges = 0;
                        if (onsiteOfferAmount == -1)
                            onsiteOfferAmount = 0;
                        if (payableAmount == -1)
                            payableAmount = 0;
                        if (excess == -1)
                            excess = 0;
                        if (telephone == -1)
                            telephone = 0;
                        if (otherCharges == -1)
                            otherCharges = 0;
                        if (totalCharges == -1)
                            totalCharges = 0;

                        String username = mManager.getUserData(mAccount, KEY_USERNAME);

                        claim = new Claim(rowid, intimNo,
                                insType, policy, vehicle, loc,
                                callerCont, caller, yom, sumInsu,
                                excess, facilty, siteOffer, inspDate,
                                acr/*, claimMileage*/, cCRemark,
                                assRemark, "A", respondDate, String.valueOf(System.currentTimeMillis()),
                                preAccValue, baldTirePenalty,
                                underInsPenalty, inspectionRemarks,
                                specialRemarks, proffesionalFee, assMileage,
                                rate/*, assMileage*/, telephone, imageCount,
                                price, photoCharges, otherCharges,
                                totalCharges, reason, policeReport,
                                investigateClaim, payableAmount,
                                onsiteOfferAmount, repairCompleted,
                                salvageReceived, settlementMethod,0, username, sequenceNo);
                    }

                    if (rowid == null && !mVehicle.getText().toString().isEmpty()) {

                        Log.w(UpdateClaimActivity.class.getName(),
                                "Creating the policy..." + mVehicle.getText().toString());

                        long id = mDbHelper.addClaim(claim);

                        if (id > 0) {
                            rowid = id;
                        }

                    } else if (rowid != null) {
                        mDbHelper.updateClaim(claim);
                    }

                    if (imageData != null) {

                        Log.w(UpdateClaimActivity.class.getName(),
                                "No of images selected to save: " + imageData.size());

                        for (int i = 0; i < imageData.size(); i++) {

                            Log.w(UpdateClaimActivity.class.getName(),
                                    "Converting image: " + i);

                            Uri imagePath = Uri.parse("file://"
                                    + imageData.get(i).getImagePath());
                            InputStream imageStream = null;
                            Bitmap b = null;
                            String encodedString = null;
                            try {
                                imageStream = getContentResolver().openInputStream(
                                        imagePath);

                                b = BitmapFactory.decodeStream(imageStream);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                b.compress(Bitmap.CompressFormat.JPEG, 85, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                encodedString = ImageUtils.getInstant().getCompressedBitmap(byteArray);

                                byteArrayOutputStream.flush();
                                byteArrayOutputStream.close();
                                imageStream.close();

                                b.recycle();

                            } catch (FileNotFoundException e) {

                                e.printStackTrace();
                                Log.w(UpdateClaimActivity.class.getName(),
                                        "Error converting to bitmap"
                                                + imageData.size());
                            }

                            Image img = new Image(intimNo, sequenceNo, encodedString, imageData.get(i)
                                    .getImageDate(), (lastImgNo + i + 1), imageData.get(i)
                                    .getImageLogitude(), imageData.get(i)
                                    .getImageLatitude(), insType);

                            long imageId = mDbHelper.addImage(img);

                            if (imageId != -1) {
                                Log.w(UpdateClaimActivity.class.getName(), "Image No : "
                                        + (lastImgNo + i + 1) + " saved...");
                            } else {
                                Log.w(UpdateClaimActivity.class.getName(),
                                        "Error while saving Image No : "
                                                + (lastImgNo + i + 1));
                            }

                            b.recycle();

                        }
                    }

                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();

                } catch (Exception e) {
                    Log.w(UpdateClaimActivity.class.getName(),
                            "Exception occured while saving (thread)... "
                                    + e.toString());
                    mProgressDialog.dismiss();
                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
                mProgressDialog.dismiss();
            }
        }.start();

    }



    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {

        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {

            case SELECT_MULTY:
                if (resultCode == RESULT_OK) {
                    saveRecord(imageReturnedIntent);
                }
                break;
            case CAPTURE_IMAGE:
                if (resultCode == RESULT_OK) {
                    saveRecord(imageReturnedIntent);
                }
        }
    }

    private void saveRecord(Intent imageReturnedIntent) {

        Bundle extras = imageReturnedIntent.getExtras();

        selectedImagePath = (extras == null) ? null
                : (ArrayList<String>) extras.getSerializable("IMAGE_LIST");
        ArrayList<Double> arrLatitude = (extras == null) ? null
                : (ArrayList<Double>) extras.getSerializable("LTTD_LIST");
        ArrayList<Double> arrLongitude = (extras == null) ? null
                : (ArrayList<Double>) extras.getSerializable("LNGT_LIST");
        ArrayList<Long> arrDateTaken = (extras == null) ? null
                : (ArrayList<Long>) extras.getSerializable("DATE_LIST");

        Log.w(UpdateClaimActivity.class.getName(),
                "No of images selected (Activity return): "
                        + selectedImagePath.size());

        if (!selectedImagePath.isEmpty()) {

            imageData = new ArrayList<ImageData>();
            String sDateTaken = null;

            for (int i = 0; i < selectedImagePath.size(); i++) {
                Calendar myCal = Calendar.getInstance();
                long dateTaken = arrDateTaken.get(i);
                sDateTaken = null;
                if (dateTaken != 0) {
                    myCal.setTimeInMillis(dateTaken);
                    Date dateText = new Date(myCal.get(Calendar.YEAR) - 1900,
                            myCal.get(Calendar.MONTH),
                            myCal.get(Calendar.DAY_OF_MONTH),
                            myCal.get(Calendar.HOUR_OF_DAY),
                            myCal.get(Calendar.MINUTE),
                            myCal.get(Calendar.SECOND));
                    SimpleDateFormat sdf24 = new SimpleDateFormat(
                            DateTimeHelper.DATE_TIME_FORMAT_24, Locale.US);
                    sDateTaken = sdf24.format(dateText);
                }

                ImageData obj = new ImageData(selectedImagePath.get(i),
                        arrLongitude.get(i), arrLatitude.get(i), sDateTaken);
                imageData.add(obj);
            }

        }
    }

    private long validateInspectionDateTime(String inspDate, String inspTime) {
        if (inspDate.equals("") && !inspTime.equals("")) {
            Snackbar.make(parentLayout, "Inspection date must be entered!", Snackbar.LENGTH_SHORT).show();
            return 0;
        } else if (!inspDate.equals("") && inspTime.equals("")) {
            Snackbar.make(parentLayout, "Inspection time must be entered!", Snackbar.LENGTH_SHORT).show();
            return 0;
        } else if (!inspDate.equals("") && !inspTime.equals("")) {
            String inspDateTime = inspDate + " " + inspTime;
            SimpleDateFormat sdfMin = new SimpleDateFormat(
                    DateTimeHelper.DATE_FORMAT_MIN, Locale.US);
            SimpleDateFormat sdf24 = new SimpleDateFormat(
                    DateTimeHelper.DATE_TIME_FORMAT_24, Locale.US);
            try {
                Date date = sdfMin.parse(inspDateTime);
                // checks for a future date or time
                if (date.compareTo(Calendar.getInstance().getTime()) > 0) {
                    Snackbar.make(parentLayout, "Future dates are not allowed!", Snackbar.LENGTH_SHORT).show();
                    return 0;
                } else {
                    return date.getTime();
                    //return sdf24.format(date);
                }

            } catch (ParseException e) {
                Snackbar.make(parentLayout, "Invalid inspected date or time!", Snackbar.LENGTH_SHORT).show();
                return 0;
            }
        } else {
            return 0;
        }

    }

    private void addImages() {
        Intent myIntent = new Intent(this, GalleryActivity.class);
        myIntent.putExtra(KEY_IMG_INTIM, intimNo);
        myIntent.putExtra(KEY_IMG_INSP_TYPE, insType);

        startActivityForResult(myIntent, SELECT_MULTY);
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

    private void updateAsCompleted() {

        if (!validateAll()) return;

        //user = mManager.getUserData(mAccount, KEY_USER).toUpperCase();

        cDate = String.valueOf(System.currentTimeMillis());
        status = "B";

        if (otherCharges == -1) {
            otherCharges = 0;
        }

        final String username = mManager.getUserData(mAccount, KEY_USERNAME);

        Claim claim = new Claim(rowid, intimNo, insType, policy,
                vehicle, loc, callerCont, caller, yom, sumInsu, excess,
                facilty, siteOffer, inspDate, acr, /* claimMileage, */
                cCRemark, assRemark, status, respondDate, cDate,
                preAccValue, baldTirePenalty, underInsPenalty,
                inspectionRemarks, specialRemarks, proffesionalFee, assMileage,
                rate/*, assMileage*/, telephone, imageCount, price, photoCharges,
                otherCharges, totalCharges, reason, policeReport,
                investigateClaim, payableAmount, onsiteOfferAmount,
                repairCompleted, salvageReceived, settlementMethod, 0, username, sequenceNo);

        boolean status = mDbHelper.updateClaim(claim);

        if(status) {

          mService.completeJob(new JobAction(intimNo, insType, username,""), new ActionResponseCallback() {
                @Override
                public void onResponse(ActionResponse result) {
                    Toast.makeText(getBaseContext(),
                            "Job Completed",
                            Toast.LENGTH_SHORT).show();
                    //Snackbar.make(parentLayout, "Job Completed.", Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Throwable result) {
                    Toast.makeText(getBaseContext(),
                            "Job Completion Failed.",
                            Toast.LENGTH_SHORT).show();
                    //Snackbar.make(parentLayout, "Job Completion Failed.", Snackbar.LENGTH_SHORT).show();
                }
            });
        }

        //goNotify();

        Intent intent = new Intent(UpdateClaimActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        /*Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();*/
    }

    private boolean validateAll() {
        boolean valid = true;
        EditText mInspDate = (EditText) findViewById(R.id.txt_claim_insp_date);
        EditText mInspTime = (EditText) findViewById(R.id.txt_claim_insp_time);

        String vInspDate = mInspDate.getText().toString();
        String vInspTime = mInspTime.getText().toString();
        if (inspDate == null || inspDate.equals("")) {
            Toast.makeText(getBaseContext(),
                    "Inspected date and time must be entered and saved!",
                    Toast.LENGTH_SHORT).show();
            valid = false;
        } else if (validateInspectionDateTime(vInspDate, vInspTime) > 0) {
            if (validateInspectionDateTime(vInspDate, vInspTime) == 0) {
                Toast.makeText(getBaseContext(),
                        "Inspected date and time must be entered!",
                        Toast.LENGTH_SHORT).show();
                valid = false;
            }
        } else {
            valid = false;
        }

        if (insType == 1 || insType == 2 || insType == 3) {
            if (acr == -1 || acr == 0) {
                Toast.makeText(getBaseContext(),
                        "ACR should be entered and saved!", Toast.LENGTH_SHORT)
                        .show();
                valid = false;
            }
        }
        if (insType == 1 || insType == 2) {
            if (preAccValue == -1) {
                Toast.makeText(getBaseContext(),
                        "Pre Accident Value should be entered and saved!",
                        Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }

        if (proffesionalFee == -1) {
            Toast.makeText(getBaseContext(),
                    "Professional Fee should be entered and saved!",
                    Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (assMileage == -1) {
            Toast.makeText(getBaseContext(),
                    "Mileage should be entered and saved!", Toast.LENGTH_SHORT)
                    .show();
            valid = false;
        }

        if (insType == 5) {
            if (repairCompleted.equals("")) {
                Toast.makeText(getBaseContext(),
                        "Repair completed should be entered and saved!",
                        Toast.LENGTH_SHORT).show();
                valid = false;
            }
            if (salvageReceived.equals("")) {
                Toast.makeText(getBaseContext(),
                        "Salvage received should be entered and saved!",
                        Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }

        if (insType == 1) {
            if (settlementMethod.equals("")) {
                Toast.makeText(getBaseContext(),
                        "Settlement method should be entered and saved!",
                        Toast.LENGTH_SHORT).show();
                valid = false;
            }
            if (policeReport.equals("")) {
                Toast.makeText(getBaseContext(),
                        "Police report should be entered and saved!",
                        Toast.LENGTH_SHORT).show();
                valid = false;
            }
            if (investigateClaim.equals("")) {
                Toast.makeText(getBaseContext(),
                        "Investigate claim should be entered and saved!",
                        Toast.LENGTH_SHORT).show();
                valid = false;
            }

            if (settlementMethod.equals("Onsite Offer")) {
                if (onsiteOfferAmount < 0) {
                    Toast.makeText(getBaseContext(),
                            "On-site offer amount should be entered and saved!",
                            Toast.LENGTH_SHORT).show();
                    valid = false;
                }

                mPoliceReportSelectedNo.setChecked(true);
                mInvestigateClaimSelectedNo.setChecked(true);
            }

            if (payableAmount < onsiteOfferAmount) {
                Toast.makeText(getBaseContext(),
                        "On-site offer amount should be less than payable amount!",
                        Toast.LENGTH_SHORT).show();
            }
        }

        List<Image> images = mDbHelper.getAllImages(intimNo, insType);
        int imageCount = images.size();

        if (imageCount < Image.MIN_NO_OF_IMAGES) {
            Toast.makeText(
                    getBaseContext(),
                    "At least " + Image.MIN_NO_OF_IMAGES
                            + " image should be added!", Toast.LENGTH_SHORT)
                    .show();
            valid = false;
        }
        return valid;
    }



    private void goNotify() {
        /*NotifierHelper.showPendingClaimsInHand(this,
                PolicyOverviewActivity.class);
        NotifierHelper.showPendingClaimsInQueue(this,
                ApprovalOverviewActivity.class);*/
    }

    // Method to populate details
    private void populateFields() {

        EditText mInspDate = (EditText) findViewById(R.id.txt_claim_insp_date);
        EditText mInspTime = (EditText) findViewById(R.id.txt_claim_insp_time);

        if (rowid != null) {

            Claim claim = mDbHelper.getClaim(rowid);

            intimNo = claim.getIntimationNo();
            sequenceNo = claim.getSeqNo();

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
                    Log.w(UpdateClaimActivity.class.getName(),
                            "Parse Exception when casting to date.. (Populate Fields)");
                    mInspDate.setText("");
                }

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            DateTimeHelper.TIME_FORMAT_MIN, Locale.US);

                    mInspTime.setText(sdf.format(calendar.getTime()));
                    //mInspTime.setText(sdfTime.format(sdf24.parse(String.valueOf(mInspTime))));
                } catch (Exception e) {
                    Log.w(UpdateClaimActivity.class.getName(),
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

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "time");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "date");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_claim_insp_date: {
                showDatePickerDialog(view);
            }
            break;
            case  R.id.btn_claim_insp_time : {
                showTimePickerDialog(view);
            }
            break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()){
            case R.id.txt_payable_amount:{
                if (hasFocus) {

                    if (!mBaldTirePenalty.getText().toString().equals(""))
                        baldTirePenalty = Double.parseDouble(mBaldTirePenalty.getText().toString());
                    else
                        baldTirePenalty = 0;

                    if (!mUnderInsPenalty.getText().toString().equals(""))
                        underInsPenalty = Double.parseDouble(mUnderInsPenalty.getText().toString());
                    else
                        underInsPenalty = 0;

                    if (!mAcr.getText().toString().equals(""))
                        acr = Double.parseDouble(mAcr.getText().toString());
                    else
                        acr = 0;

                    String excessStr = mExcess.getText().toString();

                    String excessStr2 = excessStr.substring(0, excessStr.length() - 2);
                    String result = excessStr2.replaceAll("[-+.^:,]", "");

                    excess = Double.parseDouble(result);

                    double baldTireReduction = (acr * baldTirePenalty) / 100;
                    double underInsuranceReduction = (acr * underInsPenalty) / 100;

                    payableAmount = acr - excess - baldTireReduction - underInsuranceReduction;

                    mPayableAmount.setText("" + payableAmount);

                    mOnsiteOfferAmount.setText("" + payableAmount);
                }
            }
            break;
            case R.id.txt_total_charges:{
                double otherCharges = 0.0;
                double photoCharges = 0.0;
                double proffesionalCharges = 0.0;
                double mileageCharges = 0.0;
                double telephoneCharges = 0.0;
                int noOfCopies = 0;

                if (hasFocus) {
                    if (!mOtherCharges.getText().toString().equals("")) {
                        otherCharges = Double.parseDouble(mOtherCharges
                                .getText().toString());
                    }
                    if (!mProffesionalFee.getText().toString().equals("")) {
                        proffesionalCharges = Double
                                .parseDouble(mProffesionalFee.getText()
                                        .toString());
                    }
                    if (!mTelephone.getText().toString().equals("")) {
                        telephoneCharges = Double
                                .parseDouble(mTelephone.getText()
                                        .toString());
                    }

                    if (!mMileage.getText().toString().equals(""))
                        mileageCharges = Double.parseDouble(mMileage.getText()
                                .toString()) * 20;
                    if (!mTelephone.getText().toString().equals(""))
                        telephoneCharges = Double.parseDouble(mTelephone.getText()
                                .toString());

                    if (!mCopies.getText().toString().equals("")) {
                        noOfCopies = Integer.parseInt(mCopies
                                .getText().toString());

                        photoCharges = noOfCopies * 20;
                        mPhotoCharges.setText("" + photoCharges);

                    }

                    double totalCharges = mileageCharges + photoCharges + telephoneCharges
                            + proffesionalCharges + otherCharges;
                    mTotalCharges.setText("" + totalCharges);
                }
            }
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private Calendar mCalendar = Calendar.getInstance();

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            SimpleDateFormat sdf = new SimpleDateFormat(
                    DateTimeHelper.TIME_FORMAT_MIN, Locale.US);

            mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mCalendar.set(Calendar.MINUTE, minute);

            EditText mInspTime = (EditText) getActivity().findViewById(R.id.txt_claim_insp_time);
            mInspTime.setText(sdf.format(mCalendar.getTime()));
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private Calendar mCalendar = Calendar.getInstance();

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }


        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            SimpleDateFormat sdf = new SimpleDateFormat(
                    DateTimeHelper.DATE_FORMAT, Locale.US);

            mCalendar.set(Calendar.YEAR, i);
            mCalendar.set(Calendar.MONTH, i1);
            mCalendar.set(Calendar.DAY_OF_MONTH, i2);

            EditText mInspDate = (EditText) getActivity().findViewById(R.id.txt_claim_insp_date);
            mInspDate.setText(sdf.format(mCalendar.getTime()));
        }


    }

    class ImageData {

        String imagePath;
        double imageLatitude;
        double imageLogitude;
        String imageDate;

        public ImageData(String imagePath, double imageLatitude,
                         double imageLogitude, String imageDate) {
            this.imagePath = imagePath;
            this.imageLatitude = imageLatitude;
            this.imageLogitude = imageLogitude;
            this.imageDate = imageDate;
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public double getImageLatitude() {
            return imageLatitude;
        }

        public void setImageLatitude(double imageLatitude) {
            this.imageLatitude = imageLatitude;
        }

        public double getImageLogitude() {
            return imageLogitude;
        }

        public void setImageLogitude(double imageLogitude) {
            this.imageLogitude = imageLogitude;
        }

        public String getImageDate() {
            return imageDate;
        }

        public void setImageDate(String imageDate) {
            this.imageDate = imageDate;
        }

    }
}

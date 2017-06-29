package com.loits.insurance.cm;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loits.insurance.cm.db.DatabaseHandler;
import com.loits.insurance.cm.interfaces.ActionResponseCallback;
import com.loits.insurance.cm.models.ActionResponse;
import com.loits.insurance.cm.models.Claim;
import com.loits.insurance.cm.models.InspectionType;
import com.loits.insurance.cm.models.Job;
import com.loits.insurance.cm.models.JobAction;
import com.loits.insurance.cm.network.Service;

import static com.loits.insurance.cm.util.Constants.ACCOUNT_TYPE;
import static com.loits.insurance.cm.util.Constants.IDS_CLIENT_ID;
import static com.loits.insurance.cm.util.Constants.IDS_CLIENT_SECRET;
import static com.loits.insurance.cm.util.Constants.KEY_USERNAME;

public class ApprovalActivity extends AppCompatActivity {

    Job mJob;
    private EditText editTextVehicleNo;
    private EditText editTextIntimationNo;
    private EditText editTextPolicyNo;
    private EditText editTextLocation;
    private EditText editTextCaller;
    private EditText editTextContactNo;
    private EditText editTextYOM;
    private EditText editTextSumInsured;
    private EditText editTextExcess;
    private EditText editTextCallCenterRemarks;
    private Spinner spinnerInspectionType;
    private CheckBox checkBoxFacility;
    private CheckBox checkBoxSiteOffer;

    FrameLayout progressBarHolder;

    Service mService;
    private String respondDate;
    int inspectionType;
    int intimationNo;
    String vehicleNo;

    DatabaseHandler mDbHelper;

    private AccountManager mAccountManager;
    private Account[] mAccounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);

        mAccountManager = AccountManager.get(this);
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            return ;
        }*/
        mAccounts = mAccountManager.getAccountsByType(ACCOUNT_TYPE);

        mDbHelper = new DatabaseHandler(getApplicationContext());

        mService = new Service(this, IDS_CLIENT_ID, IDS_CLIENT_SECRET);

        editTextVehicleNo =  (EditText)findViewById(R.id.editTextVehicleNo);
        editTextIntimationNo =  (EditText)findViewById(R.id.editTextIntimationNo);
        editTextPolicyNo =  (EditText)findViewById(R.id.editTextPolicyNo);
        editTextLocation =  (EditText)findViewById(R.id.editTextLocation);
        editTextCaller =  (EditText)findViewById(R.id.editTextCaller);
        editTextContactNo =  (EditText)findViewById(R.id.editTextContactNo);
        editTextYOM =  (EditText)findViewById(R.id.editTextYOM);
        editTextSumInsured =  (EditText)findViewById(R.id.editTextSumInsured);
        editTextExcess =  (EditText)findViewById(R.id.editTextExcess);
        editTextCallCenterRemarks =  (EditText)findViewById(R.id.editTextCallCenterRemarks);
        spinnerInspectionType = (Spinner) findViewById(R.id.spinnerInspectionType);
        checkBoxFacility = (CheckBox) findViewById(R.id.checkBoxFacility);
        checkBoxSiteOffer = (CheckBox) findViewById(R.id.checkBoxSiteOffer);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        final InspectionType items[] = new InspectionType[8];
        ///items[0] = new InspectionType( "",0 );
        items[0] = new InspectionType( "On Site Inspection",0 );
        items[1] = new InspectionType( "Garage Inspection",1 );
        items[2] = new InspectionType( "D/R Inspection",2 );
        items[3] = new InspectionType( "Supplementary Inspection",3 );
        items[4] = new InspectionType( "ARI",4 );
        items[5] = new InspectionType( "Re inspection",5 );
        items[6] = new InspectionType( "Under Writing Inspection",6 );
        items[7] = new InspectionType( "Special Inspection",7 );

        editTextVehicleNo.setEnabled(false);
        editTextIntimationNo.setEnabled(false);
        editTextPolicyNo.setEnabled(false);
        editTextLocation.setEnabled(false);
        editTextCaller.setEnabled(false);
        editTextContactNo.setEnabled(false);
        editTextYOM.setEnabled(false);
        editTextSumInsured.setEnabled(false);
        editTextExcess.setEnabled(false);
        editTextCallCenterRemarks.setEnabled(false);
        checkBoxFacility.setEnabled(false);
        checkBoxSiteOffer.setEnabled(false);
        checkBoxFacility.setChecked(false);
        checkBoxSiteOffer.setChecked(false);

        String jsonNewJobObject = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonNewJobObject = extras.getString("newJobObject");
            mJob = new Gson().fromJson(jsonNewJobObject, Job.class);

            inspectionType = mJob.getInspectionType();
            intimationNo = mJob.getIntimationNo();
            vehicleNo = mJob.getVehicleNo();

            editTextVehicleNo.setText(mJob.getVehicleNo());
            editTextIntimationNo.setText(String.valueOf(mJob.getIntimationNo()));
            editTextPolicyNo.setText(mJob.getPolicyNo());
            editTextLocation.setText(mJob.getIncidentLoc());
            editTextCaller.setText(mJob.getCallerName());
            editTextContactNo.setText(mJob.getCallerMobile());
            editTextYOM.setText(mJob.getYearOfManuf());
            editTextSumInsured.setText(String.valueOf(mJob.getSumInsurance()));
            editTextExcess.setText(String.valueOf(mJob.getExcessAmount()));
            editTextCallCenterRemarks.setText(mJob.getRemarks());

            ArrayAdapter<InspectionType> adapter =
                    new ArrayAdapter<InspectionType>(
                            this,
                            android.R.layout.simple_spinner_item,
                            items );
            adapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);
            spinnerInspectionType.setAdapter(adapter);
            spinnerInspectionType.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(
                                AdapterView<?> parent,
                                View view,
                                int position,
                                long id) {
                            InspectionType d = items[position];
                            //insType = d.getValue();
                            spinnerInspectionType.setSelection(mJob.getInspectionType());
                            spinnerInspectionType.setEnabled(false);
                        }

                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    }
            );

            if(mJob.getHirePurchase().equals("1")){
                checkBoxFacility.setChecked(true);
            }
            if(mJob.getSiteOffer().equals("1")){
                checkBoxSiteOffer.setChecked(true);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.approval_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_accept: {
                progressBarHolder.setVisibility(View.VISIBLE);
                String username = mAccountManager.getUserData(mAccounts[0], KEY_USERNAME);
                mService.acceptPendingJob(new JobAction(intimationNo, inspectionType, username, ""), new ActionResponseCallback() {
                    @Override
                    public void onResponse(ActionResponse response) {
                        if(response.getCode() == 1){

                            long id = addClaim();
                            if(id > 0) {
                                Intent intent = new Intent(ApprovalActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("TAB_NO", 1);
                                startActivity(intent);
                            }
                            progressBarHolder.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Throwable result) {
                        Toast.makeText(getApplicationContext(),  "Accept Failed " + result.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBarHolder.setVisibility(View.GONE);
                    }
                });
                return true;

            }
            case R.id.action_reject: {

                progressBarHolder.setVisibility(View.VISIBLE);

                createDialog(vehicleNo);

                //progressBarHolder.setVisibility(View.VISIBLE);
                /*String username = mAccountManager.getUserData(mAccounts[0], KEY_USERNAME);
                mService.rejectPendingJob(new JobAction(intimationNo, inspectionType, username, ""), new ActionResponseCallback() {
                    @Override
                    public void onResponse(ActionResponse response) {
                        if(response.getCode() == 1){

                                Intent intent = new Intent(ApprovalActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            progressBarHolder.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Throwable result) {
                        Toast.makeText(getApplicationContext(),  "Reject Failed " + result.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBarHolder.setVisibility(View.GONE);
                    }
                });*/
                return true;
            }
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private long addClaim() {

        //String respondDate =  DateTimeHelper.now();;

        String respondDate = String.valueOf(System.currentTimeMillis());

        Claim claim = new Claim(mJob.getIntimationNo(), mJob.getInspectionType(),
                mJob.getPolicyNo(), mJob.getVehicleNo(), mJob.getIncidentLoc(), mJob.getCallerMobile(),
                mJob.getCallerName(), mJob.getYearOfManuf(), mJob.getSumInsurance(), mJob.getExcessAmount(),
                 mJob.getHirePurchase(), mJob.getSiteOffer(), mJob.getRemarks(), "A",
                String.valueOf(mJob.getCreateDate()),respondDate, mJob.getSeqNo());

        return mDbHelper.addClaim(claim);

    }

    private void createDialog(String vehicleNo){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_confirmation, null);

        String title = "Reject Claim " + vehicleNo + " ?";

        final EditText reason = (EditText)dialogView.findViewById(R.id.reason);

        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        String reasonValue = reason.getText().toString();
                        String username = mAccountManager.getUserData(mAccounts[0], KEY_USERNAME);
                        mService.rejectPendingJob(new JobAction(intimationNo, inspectionType, username, reasonValue),
                                new ActionResponseCallback() {
                            @Override
                            public void onResponse(ActionResponse response) {
                                if(response.getCode() == 1){

                                    Intent intent = new Intent(ApprovalActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                    progressBarHolder.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onFailure(Throwable result) {
                                Toast.makeText(getApplicationContext(),  "Reject Failed " + result.getMessage(), Toast.LENGTH_SHORT).show();
                                progressBarHolder.setVisibility(View.GONE);
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressBarHolder.setVisibility(View.GONE);
                    }
                })
                .setTitle(title).create().show();
    }
}


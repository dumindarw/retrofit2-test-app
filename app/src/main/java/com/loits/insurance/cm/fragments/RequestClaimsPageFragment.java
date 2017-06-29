package com.loits.insurance.cm.fragments;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.loits.insurance.cm.R;
import com.loits.insurance.cm.interfaces.ActionResponseCallback;
import com.loits.insurance.cm.interfaces.OnFragmentInteractionListener;
import com.loits.insurance.cm.models.ActionResponse;
import com.loits.insurance.cm.models.InspectionType;
import com.loits.insurance.cm.models.JobAction;
import com.loits.insurance.cm.network.Service;

import static com.loits.insurance.cm.util.Constants.ACCOUNT_TYPE;
import static com.loits.insurance.cm.util.Constants.IDS_CLIENT_ID;
import static com.loits.insurance.cm.util.Constants.IDS_CLIENT_SECRET;
import static com.loits.insurance.cm.util.Constants.KEY_USERNAME;

/**
 * Created by DumindaW on 30/01/2017.
 */

public class RequestClaimsPageFragment extends Fragment  implements View.OnClickListener{
    public static final String ARG_PAGE = "ARG_PAGE_REQUEST";
    private int mPageNo;
    private String mTitle;
    public static final String ARG_TITLE = "ARG_PAGE_TITLE";

    private Spinner spinnerInspectionType;
    FloatingActionButton mFabfabRevert;
    EditText editTextIntimationNo;
    private FrameLayout progressBarHolder;
    private AccountManager mAccountManager;
    private Account[] mAccounts;

    private OnFragmentInteractionListener mListener;

    public static RequestClaimsPageFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        args.putString(ARG_TITLE, "Request Claims");
        RequestClaimsPageFragment fragment = new RequestClaimsPageFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNo = getArguments().getInt(ARG_PAGE);
        mTitle = getArguments().getString(ARG_TITLE);

        mAccountManager = AccountManager.get(getActivity());
        /*if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            return ;
        }*/
        mAccounts = mAccountManager.getAccountsByType(ACCOUNT_TYPE);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.request_claims_fragment_page, container, false);

        mFabfabRevert = (FloatingActionButton) view.findViewById(R.id.fabRevert);

        spinnerInspectionType = (Spinner) view.findViewById(R.id.spinnerInspectionType);
        editTextIntimationNo = (EditText) view.findViewById(R.id.editTextIntimationNo);

        progressBarHolder = (FrameLayout) view.findViewById(R.id.progressBarHolder);

        final InspectionType items[] = new InspectionType[8];
        items[0] = new InspectionType( "On Site Inspection",0 );
        items[1] = new InspectionType( "Garage Inspection",1 );
        items[2] = new InspectionType( "D/R Inspection",2 );
        items[3] = new InspectionType( "Supplementary Inspection",3 );
        items[4] = new InspectionType( "ARI",4 );
        items[5] = new InspectionType( "Re inspection",5 );
        items[6] = new InspectionType( "Under Writing Inspection",6 );
        items[7] = new InspectionType( "Special Inspection",7 );

        ArrayAdapter<InspectionType> adapter =
                new ArrayAdapter<InspectionType>(
                        getActivity(),
                        android.R.layout.simple_spinner_item,
                        items );
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinnerInspectionType.setAdapter(adapter);
        spinnerInspectionType.setSelection(0);
        spinnerInspectionType.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {
                        InspectionType d = items[position];
                        //insType = d.getValue();
                        //spinnerInspectionType.setSelection(1);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );


        if (mListener != null) {
            //mListener.onFragmentInteraction(mTitle);
        }

        mFabfabRevert.setOnClickListener(this);

        /*TextView textView = (TextView) view;
        textView.setText("Fragment #" + mPageNo);*/
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fabRevert){
            //revertJob();
        }
    }

    private void revertJob(){

        progressBarHolder.setVisibility(View.VISIBLE);

        int intimationNo = Integer.valueOf(editTextIntimationNo.getText().toString());
        int inspectionType = (int) (long) spinnerInspectionType.getSelectedItemId() + 1;

        Service service = new Service(getActivity(), IDS_CLIENT_ID, IDS_CLIENT_SECRET);
        String username = mAccountManager.getUserData(mAccounts[0], KEY_USERNAME);

        service.revertJob(new JobAction(intimationNo, inspectionType, username, ""), new ActionResponseCallback() {
            @Override
            public void onResponse(ActionResponse result) {

                progressBarHolder.setVisibility(View.GONE);

                if(result.getCode() == 1) {
                    if (getActivity() != null)
                        Toast.makeText(getActivity(), "Revert Done.", Toast.LENGTH_SHORT).show();
                }else{
                    if (getActivity() != null)
                        Toast.makeText(getActivity(), "No Records Found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable result) {

                progressBarHolder.setVisibility(View.GONE);

                if(getActivity() != null)
                    Toast.makeText(getActivity(),"Revert Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);
    }
}
package com.loits.insurance.cm.fragments;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loits.insurance.cm.ApprovalActivity;
import com.loits.insurance.cm.DividerItemDecoration;
import com.loits.insurance.cm.R;
import com.loits.insurance.cm.RecyclerTouchListener;
import com.loits.insurance.cm.adapters.ClaimsAdapter;
import com.loits.insurance.cm.interfaces.ClickListener;
import com.loits.insurance.cm.interfaces.JobCallback;
import com.loits.insurance.cm.interfaces.OnFragmentInteractionListener;
import com.loits.insurance.cm.models.Assessor;
import com.loits.insurance.cm.models.ClaimListItem;
import com.loits.insurance.cm.models.Job;
import com.loits.insurance.cm.models.JobList;
import com.loits.insurance.cm.network.Service;
import com.loits.insurance.cm.util.Utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.loits.insurance.cm.util.Constants.ACCOUNT_TYPE;
import static com.loits.insurance.cm.util.Constants.IDS_CLIENT_ID;
import static com.loits.insurance.cm.util.Constants.IDS_CLIENT_SECRET;
import static com.loits.insurance.cm.util.Constants.KEY_USERNAME;

/**
 * Created by DumindaW on 30/01/2017.
 */

public class NewClaimsPageFragment extends Fragment implements View.OnClickListener{
    public static final String ARG_PAGE = "ARG_PAGE_NEW";
    private int mPageNo;
    private String mTitle;
    public static final String ARG_TITLE = "ARG_PAGE_TITLE";

    protected View mView;

    //private List<ClaimListItem> newClaimList;
    private ClaimsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView emptyView;
    private ImageView errorImage;
    private FloatingActionButton mFabRefreshNewClaims;

    private OnFragmentInteractionListener mListener;

    private FrameLayout progressBarHolder;
    private AccountManager mAccountManager;
    private Account[] mAccounts;

    //private SwipeRefreshLayout mSwipeRefreshLayout;

    public static NewClaimsPageFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        args.putString(ARG_TITLE, "New Claims");
        NewClaimsPageFragment fragment = new NewClaimsPageFragment();
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
    public void onStart() {
        super.onStart();

        downloadPendingJobs();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_claims_fragment_page, container, false);
        progressBarHolder = (FrameLayout) view.findViewById(R.id.progressBarHolder);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.newClaimsRecyclerView);
        //mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.newClaimSwipeRefresh);
        mFabRefreshNewClaims = (FloatingActionButton) view.findViewById(R.id.fabRefreshNewClaims);
        emptyView = (TextView) view.findViewById(R.id.emptyText);
        errorImage = (ImageView) view.findViewById(R.id.errorImage);
        emptyView.setVisibility(View.GONE);
        errorImage.setVisibility(View.GONE);

        if(!Utilities.isNetworkAvailable(getActivity())){
            emptyView.setText("No network available.");
            emptyView.setVisibility(View.VISIBLE);
            errorImage.setVisibility(View.VISIBLE);
        }else{
            emptyView.setVisibility(View.GONE);
            errorImage.setVisibility(View.GONE);
        }

        this.mView = view;

        //newClaimList = new ArrayList<>();
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mAdapter = new ClaimsAdapter(/*newClaimList*/);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mFabRefreshNewClaims.setOnClickListener(this);

       /* mSwipeRefreshLayout.setColorSchemeResources(R.color.asbestos, R.color.peter_river, R.color.belize_hole);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        downloadPendingJobs();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });*/


        if (mListener != null) {
            //mListener.onFragmentInteraction(mTitle);
        }

       /* TextView textView = (TextView) view;
        textView.setText("Fragment #" + mPageNo);*/
        return view;
    }

    private void downloadPendingJobs(){

        progressBarHolder.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        errorImage.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

        Service service = new Service(getActivity(), IDS_CLIENT_ID, IDS_CLIENT_SECRET);

        String username = mAccountManager.getUserData(mAccounts[0], KEY_USERNAME);

        service.downloadPendingJobs(new Assessor(username), new JobCallback() {

            @Override
            public void onResponse(JobList result) {
                final List<Job> jobs = result.getInsuranceJob();

                //newClaimsProgressBar.setProgress(progressStatus);

                prepareNewClaimData(jobs);

                mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        if(position < jobs.size()) {
                            Job job = jobs.get(position);

                            Intent approvalIntent = new Intent(getActivity(), ApprovalActivity.class);
                            approvalIntent.putExtra("newJobObject", new Gson().toJson(job));
                            startActivity(approvalIntent);
                        }
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
            }

            @Override
            public void onFailure(Throwable result) {

                progressBarHolder.setVisibility(View.GONE);

                if(result!=null) {

                    /*if(getActivity() != null)
                        Toast.makeText(getActivity(),"Request Failed.", Toast.LENGTH_SHORT).show();*/

                    emptyView.setText("Updating claim list failed.");
                    emptyView.setVisibility(View.VISIBLE);
                    errorImage.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);

                   /* Snackbar snackbar = Snackbar
                            .make(mView, "Request Failed ", Snackbar.LENGTH_SHORT);
                    snackbar.show();*/
                }

            }
        });

    }

    private void prepareNewClaimData(List<Job> job) {

        DateFormat df = new SimpleDateFormat("yyyy-MMM-dd");

        List<ClaimListItem> tempClaimList = new ArrayList<ClaimListItem>();

        int newListSize = job.size();

        if(newListSize > 0) {

            for (int i = 0; i < job.size(); i++) {

                Date createdDate = new Date(job.get(i).getCreateDate());
                String cDate = df.format(createdDate);
                ClaimListItem item = new ClaimListItem(job.get(i).getVehicleNo(), cDate);
                mAdapter.insertAt(i, item, job.size());
                //newClaimList.add(item);
            }

        }else{
            emptyView.setText("You do not have any new claims.");
            emptyView.setVisibility(View.VISIBLE);
            errorImage.setVisibility(View.VISIBLE);
        }

        mRecyclerView.setAdapter(mAdapter);
        progressBarHolder.setVisibility(View.GONE);
        //newClaimList = new ArrayList<>();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fabRefreshNewClaims){
            downloadPendingJobs();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);

    }
}
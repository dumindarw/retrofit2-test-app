package com.loits.insurance.cm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loits.insurance.cm.ApprovalActivity;
import com.loits.insurance.cm.DividerItemDecoration;
import com.loits.insurance.cm.R;
import com.loits.insurance.cm.RecyclerTouchListener;
import com.loits.insurance.cm.UpdateClaimActivity;
import com.loits.insurance.cm.adapters.ClaimsAdapter;
import com.loits.insurance.cm.db.DatabaseHandler;
import com.loits.insurance.cm.interfaces.ClickListener;
import com.loits.insurance.cm.interfaces.JobCallback;
import com.loits.insurance.cm.interfaces.OnFragmentInteractionListener;
import com.loits.insurance.cm.models.Assessor;
import com.loits.insurance.cm.models.Claim;
import com.loits.insurance.cm.models.ClaimListItem;
import com.loits.insurance.cm.models.Job;
import com.loits.insurance.cm.models.JobList;
import com.loits.insurance.cm.network.Service;
import com.loits.insurance.cm.util.Utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.loits.insurance.cm.util.Constants.IDS_CLIENT_ID;
import static com.loits.insurance.cm.util.Constants.IDS_CLIENT_SECRET;

/**
 * Created by DumindaW on 30/01/2017.
 */

public class PendingClaimsPageFragment extends Fragment{
    public static final String ARG_PAGE = "ARG_PAGE_PENDING";
    private int mPageNo;
    private String mTitle;
    public static final String ARG_TITLE = "ARG_PAGE_TITLE";

    protected View mView;

    //private List<ClaimListItem> pendingClaimList;
    private ClaimsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView emptyView;
    private ImageView errorImage;
    //private FloatingActionButton mFabRefreshPendingClaims;
    private FrameLayout progressBarHolder;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    DatabaseHandler mDbHelper;

    private OnFragmentInteractionListener mListener;

    public static PendingClaimsPageFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        args.putString(ARG_TITLE, "Pending Claims");
        PendingClaimsPageFragment fragment = new PendingClaimsPageFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNo = getArguments().getInt(ARG_PAGE);
        mTitle = getArguments().getString(ARG_TITLE);
    }

    @Override
    public void onStart() {
        super.onStart();

        mDbHelper = new DatabaseHandler(getActivity());

        //downloadAcceptedJobs();
        populateAcceptedJobs();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pending_claims_fragment_page, container, false);
        progressBarHolder = (FrameLayout) view.findViewById(R.id.progressBarHolder);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.pendingClaimsRecyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pendingClaimSwipeRefresh);
        //mFabRefreshPendingClaims = (FloatingActionButton) view.findViewById(R.id.fabRefreshPendingClaims);
        emptyView = (TextView) view.findViewById(R.id.emptyText);
        errorImage = (ImageView) view.findViewById(R.id.errorImage);
        emptyView.setVisibility(View.GONE);
        errorImage.setVisibility(View.GONE);

        mDbHelper = new DatabaseHandler(getActivity());

        this.mView = view;

        if(!Utilities.isNetworkAvailable(getActivity())){
            emptyView.setText("No network available.");
            emptyView.setVisibility(View.VISIBLE);
            errorImage.setVisibility(View.VISIBLE);
        }else{
            emptyView.setVisibility(View.GONE);
            errorImage.setVisibility(View.GONE);
        }

        //pendingClaimList = new ArrayList<>();
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mAdapter = new ClaimsAdapter(/*pendingClaimList*/);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //mFabRefreshPendingClaims.setOnClickListener(this);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.asbestos, R.color.peter_river, R.color.belize_hole);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        populateAcceptedJobs();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });

        if (mListener != null) {
            //mListener.onFragmentInteraction(mTitle);
        }
        return view;
    }

    private void populateAcceptedJobs(){
        final List<Claim> newClaims = mDbHelper.getNewClaims();

        prepareNewClaimData(newClaims);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Claim newClaim = newClaims.get(position);
                //Toast.makeText(getActivity(), job.getVehicleNo() + " is selected!", Toast.LENGTH_SHORT).show();

                Intent updateClaimIntent = new Intent(getActivity(), UpdateClaimActivity.class);
                updateClaimIntent.putExtra("pendingJobObject", new Gson().toJson(newClaim));
                //updateClaimIntent.putExtra(KEY_ROWID, position);
                startActivity(updateClaimIntent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void downloadAcceptedJobs(){

        progressBarHolder.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        errorImage.setVisibility(View.GONE);

        Service service = new Service(getActivity(), IDS_CLIENT_ID, IDS_CLIENT_SECRET);

        service.downloadCompletedJobs(new Assessor("dumindaw"), new JobCallback() {
            @Override
            public void onResponse(JobList result) {
                final List<Job> jobs = result.getInsuranceJob();

                prepareNewClaimData2(jobs);

                mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Job job = jobs.get(position);
                        //Toast.makeText(getActivity(), job.getVehicleNo() + " is selected!", Toast.LENGTH_SHORT).show();

                        Intent approvalIntent = new Intent(getActivity(), ApprovalActivity.class);
                        approvalIntent.putExtra("newJobObject", new Gson().toJson(job));
                        //startActivity(approvalIntent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
            }

            @Override
            public void onFailure(Throwable result) {

                progressBarHolder.setVisibility(View.GONE);

                if(getActivity() != null)
                    Toast.makeText(getActivity(),"Request Failed.", Toast.LENGTH_SHORT).show();

                /*Snackbar snackbar = Snackbar
                        .make(mView, "Request Failed ", Snackbar.LENGTH_SHORT);
                snackbar.show();*/

            }
        });
    }

    private void prepareNewClaimData2(List<Job> job) {

        DateFormat df = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);

        if(job.size() > 0) {

            mRecyclerView.setVisibility(View.VISIBLE);

            for (int i = 0; i < job.size(); i++) {

                Date createdDate = new Date(job.get(i).getCreateDate());
                String cDate = df.format(createdDate);

                ClaimListItem item = new ClaimListItem(job.get(i).getVehicleNo(), cDate);
                //pendingClaimList.add(item);
            }
        }else{
            emptyView.setText("You do not have any pending claims.");
            emptyView.setVisibility(View.VISIBLE);
            errorImage.setVisibility(View.VISIBLE);

            mRecyclerView.setVisibility(View.GONE);
        }

        mRecyclerView.setAdapter(mAdapter);
        progressBarHolder.setVisibility(View.GONE);
        //pendingClaimList = new ArrayList<>();
        mAdapter.notifyDataSetChanged();
    }

    private void prepareNewClaimData(List<Claim> claims) {

        DateFormat df = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);

        if(claims.size() > 0) {

            mRecyclerView.setVisibility(View.VISIBLE);

            for (int i = 0; i < claims.size(); i++) {

                Date createdDate = new Date(Long.parseLong(claims.get(i).getcDate()));
                String cDate = df.format(createdDate);

                ClaimListItem item = new ClaimListItem(claims.get(i).getVehicleNo(), cDate);

                mAdapter.insertAt(i, item, claims.size());
                //pendingClaimList.add(item);
            }

            //mAdapter = new ClaimsAdapter(pendingClaimList);

        }else{
            emptyView.setText("Your do not have any pending claims.");
            emptyView.setVisibility(View.VISIBLE);
            errorImage.setVisibility(View.VISIBLE);

            mRecyclerView.setVisibility(View.GONE);
        }

        mRecyclerView.setAdapter(mAdapter);
        progressBarHolder.setVisibility(View.GONE);
        //pendingClaimList = new ArrayList<>();
        mAdapter.notifyDataSetChanged();
    }

    /*@Override
    public void onClick(View view) {
        if(view.getId() == R.id.fabRefreshPendingClaims){
            //downloadAcceptedJobs();
            populateAcceptedJobs();
        }
    }*/

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);
    }
}
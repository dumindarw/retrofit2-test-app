package com.loits.insurance.cm.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import com.loits.insurance.cm.DividerItemDecoration;
import com.loits.insurance.cm.R;
import com.loits.insurance.cm.RecyclerTouchListener;
import com.loits.insurance.cm.ViewClaimActivity;
import com.loits.insurance.cm.adapters.ClaimsAdapter;
import com.loits.insurance.cm.db.DatabaseHandler;
import com.loits.insurance.cm.interfaces.ClickListener;
import com.loits.insurance.cm.interfaces.OnFragmentInteractionListener;
import com.loits.insurance.cm.models.Claim;
import com.loits.insurance.cm.models.ClaimListItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.loits.insurance.cm.util.Constants.DATA_VALIDITY_DAYS;

/**
 * Created by DumindaW on 30/01/2017.
 */

public class CompletedClaimsPageFragment extends Fragment{
    public static final String ARG_PAGE = "ARG_PAGE_COMPLETED";
    private int mPageNo;
    private String mTitle;
    public static final String ARG_TITLE = "ARG_PAGE_TITLE";

    //private List<ClaimListItem> completedClaimList;
    private ClaimsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView emptyView;
    private ImageView errorImage;
    private FrameLayout progressBarHolder;
    private Parcelable recyclerViewState;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    DatabaseHandler mDbHelper;

    private OnFragmentInteractionListener mListener;

    public static CompletedClaimsPageFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        args.putString(ARG_TITLE, "Completed Claims");
        CompletedClaimsPageFragment fragment = new CompletedClaimsPageFragment();
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
        populateCompletedJobs();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.completed_claims_fragment_page, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.completedClaimSwipeRefresh);
        progressBarHolder = (FrameLayout) view.findViewById(R.id.progressBarHolder);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.completedClaimsRecyclerView);
        emptyView = (TextView) view.findViewById(R.id.emptyText);
        errorImage = (ImageView) view.findViewById(R.id.errorImage);
        emptyView.setVisibility(View.GONE);
        errorImage.setVisibility(View.GONE);

       // completedClaimList = new ArrayList<>();
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mAdapter = new ClaimsAdapter(/*completedClaimList*/);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mSwipeRefreshLayout.setColorSchemeResources(R.color.asbestos, R.color.peter_river, R.color.belize_hole);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setupAdapter();
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

    private void setupAdapter() {
        mDbHelper = new DatabaseHandler(getActivity());
        final List<Claim> completedClaims = mDbHelper.getCompletedClaims();

        prepareCompletedClaimData(completedClaims);
    }

    private void populateCompletedJobs(){

        final List<Claim> completedClaims = mDbHelper.getCompletedClaims();

        prepareCompletedClaimData(completedClaims);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Claim completedClaim = completedClaims.get(position);
                //Toast.makeText(getActivity(), job.getVehicleNo() + " is selected!", Toast.LENGTH_SHORT).show();

                Intent viewClaimIntent = new Intent(getActivity(), ViewClaimActivity.class);
                viewClaimIntent.putExtra("completedClaimObject", new Gson().toJson(completedClaim));
                startActivity(viewClaimIntent);
            }

            @Override
            public void onLongClick(View view, final int position) {


                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete Claim")
                        .setMessage("Delete "+ completedClaims.get(position).getVehicleNo() + " ?")
                        .setIcon(R.drawable.ic_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                mAdapter.removeAt(position);

                                int ii = completedClaims.get(position).getIntimationNo();
                                int ii2 = completedClaims.get(position).getInspType();

                                boolean status = mDbHelper.deleteClaim(completedClaims.get(position).getIntimationNo(),completedClaims.get(position).getInspType());
                                boolean status2 = mDbHelper.deleteImages(completedClaims.get(position).getIntimationNo(),completedClaims.get(position).getInspType());

                                boolean x = status;

                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        }));
    }

    private void prepareCompletedClaimData(List<Claim> claims) {

        DateFormat df = new SimpleDateFormat("yyyy-MMM-dd");

        boolean completeStatus = false;

        if(claims.size() > 0) {

            mRecyclerView.setVisibility(View.VISIBLE);

            for (int i = 0; i < claims.size(); i++) {

                int pendingImagesCount = mDbHelper.getPendingImages(claims.get(i).getIntimationNo(),
                        claims.get(i).getInspType()).size();

                completeStatus =  pendingImagesCount == 0;

                String progress = String.valueOf(pendingImagesCount) + "/" + String.valueOf(claims.size());

                long currentTime = System.currentTimeMillis();

                long validUntil = Long.parseLong(claims.get(i).getcDate()) + 1000 * 60 * 60 * 24 * DATA_VALIDITY_DAYS;

                Date createdDate = new Date(Long.parseLong(claims.get(i).getcDate()));
                String cDate = df.format(createdDate);
                ClaimListItem item = new ClaimListItem(claims.get(i).getVehicleNo(), cDate, completeStatus/*, progress*/);

                if(validUntil < currentTime) {
                    mAdapter.removeAt(i);

                    mDbHelper.deleteClaim(claims.get(i).getIntimationNo(), claims.get(i).getInspType());
                    mDbHelper.deleteImages(claims.get(i).getIntimationNo(), claims.get(i).getInspType());
                }else{
                    mAdapter.insertAt(i, item, claims.size());
                }
            }

        }else{
            emptyView.setText("You do not have any completed claims.");
            emptyView.setVisibility(View.VISIBLE);
            errorImage.setVisibility(View.VISIBLE);

            mRecyclerView.setVisibility(View.GONE);
        }

        mRecyclerView.setAdapter(mAdapter);
        progressBarHolder.setVisibility(View.GONE);
        //completedClaimList = new ArrayList<>();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);
    }
}
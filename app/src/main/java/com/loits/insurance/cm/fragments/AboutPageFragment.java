package com.loits.insurance.cm.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loits.insurance.cm.R;
import com.loits.insurance.cm.interfaces.OnFragmentInteractionListener;

/**
 * Created by DumindaW on 30/01/2017.
 */

public class AboutPageFragment extends Fragment  implements View.OnClickListener{
    public static final String ARG_PAGE = "ARG_PAGE_ABOUT";
    private int mPageNo;
    private String mTitle;
    public static final String ARG_TITLE = "ARG_PAGE_TITLE";
    private OnFragmentInteractionListener mListener;

    public static AboutPageFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        args.putString(ARG_TITLE, "Request Claims");
        AboutPageFragment fragment = new AboutPageFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_fragment_page, container, false);


        /*TextView textView = (TextView) view;
        textView.setText("Fragment #" + mPageNo);*/
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fabRevert){

        }
    }

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);
    }
}
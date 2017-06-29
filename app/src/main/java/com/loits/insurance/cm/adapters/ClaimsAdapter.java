package com.loits.insurance.cm.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loits.insurance.cm.R;
import com.loits.insurance.cm.models.ClaimListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DumindaW on 31/01/2017.
 */

public class ClaimsAdapter extends RecyclerView.Adapter<ClaimsAdapter.ClaimsViewHolder>{

    private List<ClaimListItem> claimItemList;

    public ClaimsAdapter() {
        super();
        claimItemList = new ArrayList<ClaimListItem>();
    }

    public class ClaimsViewHolder extends RecyclerView.ViewHolder {

        public TextView vehicleNo, day, month, year/*, progress*/;
        public ImageView syncCompleteImage;

        public ClaimsViewHolder(View view) {
            super(view);
            vehicleNo = (TextView) view.findViewById(R.id.textViewVehicleNo);
            day = (TextView) view.findViewById(R.id.tvDay);
            month = (TextView) view.findViewById(R.id.tvMonth);
            year = (TextView) view.findViewById(R.id.tvYear);
            syncCompleteImage = (ImageView) view.findViewById(R.id.syncCompleteImage);
            //progress = (TextView) view.findViewById(R.id.progress);
        }
    }

    /*public void swap(List<ClaimListItem> claimItems){
        claimItemList.clear();
        claimItemList.addAll(claimItems);
        notifyDataSetChanged();
    }*/

    public void removeAt(int position) {
        if(claimItemList.size()>0) {
            claimItemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, claimItemList.size());
        }
        //notifyDataSetChanged();
    }

    public void insertAt(int position, ClaimListItem item, int listSize) {
        if(position == 0){
            claimItemList = new ArrayList<>(listSize);
        }
        claimItemList.add(position, item);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, claimItemList.size());
    }

    public void addData(ClaimListItem item) {
        claimItemList.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        claimItemList.clear();
        notifyDataSetChanged();
    }

    /*public ClaimsAdapter(List<ClaimListItem> claimItemList) {
        this.claimItemList = claimItemList;
    }*/
    @Override
    public ClaimsAdapter.ClaimsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.claim_list_row, parent, false);

        return new ClaimsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ClaimsAdapter.ClaimsViewHolder holder, int position) {
        ClaimListItem claim = claimItemList.get(position);

        String date = claim.getDate();
        //07-FEB-17 15.00.51.472000000
        String[] splits = date.split("-");
        holder.vehicleNo.setText(claim.getVehicleNo());
        holder.day.setText(splits[2]);
        holder.month.setText(splits[1]);
        holder.year.setText(splits[0]);
        if(claim.isCompleted()){
            holder.syncCompleteImage.setImageResource(R.drawable.ic_check_circle);
            //holder.progress.setVisibility(View.GONE);
        } else{
            //holder.progress.setVisibility(View.VISIBLE);
            //holder.progress.setText(claim.getProgress());
            holder.syncCompleteImage.setImageResource(0);
        }

    }

    @Override
    public int getItemCount() {
        return claimItemList.size();
    }

    public List<ClaimListItem> getClaimItemList() {
        return claimItemList;
    }
}

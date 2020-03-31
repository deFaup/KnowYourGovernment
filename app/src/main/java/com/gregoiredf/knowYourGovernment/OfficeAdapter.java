package com.gregoiredf.knowYourGovernment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class OfficeAdapter extends RecyclerView.Adapter<OfficeViewHolder>
{
    private ArrayList<Office> offices;
    private MainActivity mainActivity;

    public OfficeAdapter(ArrayList<Office> officesList, MainActivity mainActivity)
    {
        this.offices = officesList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public OfficeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.member_entry, parent, false);
        itemView.setOnClickListener(mainActivity);

        return new OfficeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficeViewHolder holder, int position)
    {
        Office office = offices.get(position);
        holder.officeName.setText(office.getOfficeName());
        holder.officialName.setText(office.getOfficialName());
    }

    @Override
    public int getItemCount() {
        return offices.size();
    }
}

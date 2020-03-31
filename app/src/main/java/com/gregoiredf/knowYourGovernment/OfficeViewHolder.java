package com.gregoiredf.knowYourGovernment;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

class OfficeViewHolder extends RecyclerView.ViewHolder
{
    public TextView officeName, officialName;
    public OfficeViewHolder(View itemView)
    {
        super(itemView);
        officeName = itemView.findViewById(R.id.officeName);
        officialName = itemView.findViewById(R.id.photoDetailName);
    }
}

package com.example.checkthedate;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ResultHolderOverview extends RecyclerView.ViewHolder {
    TextView productName;
    TextView expiryDate;
    public ResultHolderOverview(@NonNull View itemView) {
        super(itemView);
        productName = itemView.findViewById(R.id.textViewProductName);
        expiryDate = itemView.findViewById(R.id.textViewExpiryDate);
    }
}

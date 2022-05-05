package com.example.checkthedate;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ResultHolderInventory extends RecyclerView.ViewHolder {
    TextView productNameInventory;
    TextView productExpiryDateInventory;
    CheckBox checkbox;

    public ResultHolderInventory(@NonNull View itemView) {
        super(itemView);
        productNameInventory = itemView.findViewById(R.id.textViewProductNameInventory);
        productExpiryDateInventory = itemView.findViewById(R.id.textViewExpiryDateInventory);
        checkbox = itemView.findViewById(R.id.checkBoxMain);
    }
}

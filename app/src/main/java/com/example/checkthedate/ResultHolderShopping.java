package com.example.checkthedate;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ResultHolderShopping extends RecyclerView.ViewHolder {
    TextView productNameShopping;
    TextView quantity;
    TextView productTypeShopping;
    CheckBox checkbox;

    public ResultHolderShopping(@NonNull View itemView) {
        super(itemView);
        productNameShopping = itemView.findViewById(R.id.textViewProductNameShopping);
        quantity = itemView.findViewById(R.id.textViewQuantityShopping);
        productTypeShopping = itemView.findViewById(R.id.textViewProductTypeShopping);
        checkbox = itemView.findViewById(R.id.checkBox);
    }
}

package com.example.checkthedate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

// Result Adapter for the ParseQuery in the Household Inventory Activity
public class ResultAdapterInventory extends RecyclerView.Adapter<ResultHolderInventory> {
    Context context;
    List<ParseObject> list;

    public ArrayList<String> arrayInventoryChecked = new ArrayList<String>();
    public ArrayList<String> arrayInventoryTypeChecked = new ArrayList<String>();

    public ResultAdapterInventory(Context context, List<ParseObject> list){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ResultHolderInventory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.result_cell_inventory_main, parent,
                false);
        return new ResultHolderInventory(v);
    }

    /* Retrieves the product name and expiry date from the database and sets them so they can be
    displayed */
    @Override
    public void onBindViewHolder(@NonNull ResultHolderInventory holder,
                                 @SuppressLint("RecyclerView") int position) {
        ParseObject object = list.get(position);
        if(object.getString("productName") != null) {
            holder.productNameInventory.setText(object.getString("productName"));
            holder.productExpiryDateInventory.setText(object.getString("expiryDate"));
        } else {
            holder.productNameInventory.setText("null");
            holder.productExpiryDateInventory.setText("null");
        }

        /* Listens for checked items and adds them to an array to be used with the delete, add to
        shopping and recipe search functions on the household inventory activity */
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkbox.isChecked()) {
                    arrayInventoryChecked.add(list.get(position).getString("productName"));
                    arrayInventoryTypeChecked.add(list.get(position).getString("productType"));
                } else {
                    arrayInventoryChecked.remove(list.get(position).getString("productName"));
                    arrayInventoryTypeChecked.remove(list.get(position)
                            .getString("productType"));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // Checked arrays to be returned to household inventory activity
    public ArrayList<String> getCheckedInventoryList() { return arrayInventoryChecked; }
    public ArrayList<String> getArrayInventoryTypeChecked() { return arrayInventoryTypeChecked; }

    public void clearList() {
        list = new ArrayList<>();
        notifyDataSetChanged();
    }
}

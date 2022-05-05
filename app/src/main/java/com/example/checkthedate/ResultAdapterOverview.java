package com.example.checkthedate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

// Result Adapter for the ParseQuery in the Welcome Screen Activity
public class ResultAdapterOverview extends RecyclerView.Adapter<ResultHolderOverview> {
    Context context;
    List<ParseObject> list;

    public ResultAdapterOverview(Context context, List<ParseObject> list){
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public ResultHolderOverview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.result_cell_welcome_overview, parent,
                    false);
        return new ResultHolderOverview(v);
    }

    /* Retrieves the product name and expiry date from the database and sets them so they can be
    displayed */
    @Override
    public void onBindViewHolder(@NonNull ResultHolderOverview holder, int position) {
        ParseObject object = list.get(position);
        if(object.getString("productName") != null) {
            holder.productName.setText(object.getString("productName"));
            holder.expiryDate.setText(object.getString("expiryDate"));
        } else {
            holder.productName.setText("null");
            holder.expiryDate.setText("null");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clearList() {
        list = new ArrayList<>();
        notifyDataSetChanged();
    }
}

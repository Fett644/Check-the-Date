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

// Result Adapter for the ParseQuery in the Shopping List Activity
public class ResultAdapterShopping extends RecyclerView.Adapter<ResultHolderShopping> {
    Context context;
    List<ParseObject> list;

    public ArrayList<String> arrayShoppingChecked = new ArrayList<String>();

    public ResultAdapterShopping(Context context, List<ParseObject> list){
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public ResultHolderShopping onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.result_cell_shopping_list, parent,
                false);
        return new ResultHolderShopping(v);
    }

    /* Retrieves the product name, product type and quantity from the database and sets them so
     they can be displayed */
    @Override
    public void onBindViewHolder(@NonNull ResultHolderShopping holder,
                                 @SuppressLint("RecyclerView") int position) {
        ParseObject object = list.get(position);
        if(object.getString("productName") != null) {
            holder.productNameShopping.setText(object.getString("productName"));
            holder.productTypeShopping.setText(object.getString("productType"));
            holder.quantity.setText(object.getString("quantity"));
        } else {
            holder.productNameShopping.setText("null");
            holder.productTypeShopping.setText("null");
            holder.quantity.setText("null");
        }

        /* Listens for checked items and adds them to an array to be used with the delete item
        * function in the shopping list activity */
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkbox.isChecked()) {
                    arrayShoppingChecked.add(list.get(position).getString("productName"));
                } else {
                    arrayShoppingChecked.remove(list.get(position).getString("productName"));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<String> getCheckedList() { return arrayShoppingChecked; }

    public void clearList() {
        list = new ArrayList<>();
        notifyDataSetChanged();
    }
}

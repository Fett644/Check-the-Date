package com.example.checkthedate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

// Shopping list activity displays users shopping inventory which is managed by them
public class ShoppingListActivity extends AppCompatActivity {
    // Global declaration of variables
    RecyclerView resultShoppingList;
    ResultAdapterShopping adapter;
    ArrayList<String> deleteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        // Declaration of local variables
        Button btnAddItemNav = (Button) findViewById(R.id.buttonAddItemShopping);
        Button btnDeleteItems = (Button) findViewById(R.id.buttonDeleteShopping);
        resultShoppingList = findViewById(R.id.reclycherViewShopping);
        TextView goBack = (TextView) findViewById(R.id.textViewListGoBack);
        Intent intent = getIntent();
        String loginName = intent.getStringExtra("Name");

        // ParseQuery to retrieve shopping list items to be displayed
        ParseQuery<ParseObject> inventoryOverviewQuery =
                new ParseQuery<ParseObject>("ShoppingList")
                        .addAscendingOrder("productType")
                        .whereMatches("username", loginName);
        inventoryOverviewQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objList, ParseException e) {
                if (e == null) {
                    initData(objList);
                } else {
                    // Error handling in case of issue
                    Log.d("ParseQuery", e.getMessage());
                }
            }
        });

        // Takes user to shopping list add item activity
        btnAddItemNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingListActivity.this,
                        ShoppingListAddItemActivity.class);
                intent.putExtra("Name", loginName);
                startActivity(intent);
            }
        });

        // Takes array of checked items and sends them to deleteFromShopping() function
        btnDeleteItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteArray = adapter.getCheckedList();
                for (int i = 0; i < deleteArray.size(); i++) {
                    String productName = deleteArray.get(i);
                    deleteFromShopping(productName);
                }
            }
        });
        // Allows user to go back to previous activity
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingListActivity.this,
                        WelcomeScreenActivity.class);
                intent.putExtra("Name", loginName);
                startActivity(intent);
                finish();
            }
        });
    }

    /* This is the data received from the ParseQuery and is placed in the result adapter to
    organise and display */
    public void initData(List<ParseObject> objects) {
        adapter = new ResultAdapterShopping(this, objects);
        resultShoppingList.setLayoutManager(new LinearLayoutManager(this));
        resultShoppingList.setAdapter(adapter);
    }

    // Function takes items from array and individually deletes them from the class in the DB
    private void deleteFromShopping(String product_Name) {
        Intent intent = getIntent();
        String loginName = intent.getStringExtra("Name");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ShoppingList");
        query.whereEqualTo("productName", product_Name).whereEqualTo("username", loginName);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    try {
                        object.delete();
                        Toast.makeText(ShoppingListActivity.this,
                                "Item/s successfully deleted!", Toast.LENGTH_SHORT).show();
                        startActivity(getIntent());
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                        Toast.makeText(ShoppingListActivity.this,
                                "Error!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ShoppingListActivity.this,"Error!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
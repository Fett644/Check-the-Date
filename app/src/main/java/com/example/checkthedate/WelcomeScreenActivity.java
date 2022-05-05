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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/* Welcome Screen Activity is what every user sees when they login, it gives them full navigation to
* the rest of the apps features as well as a simple snapshot of their inventory */
public class WelcomeScreenActivity extends AppCompatActivity {
    RecyclerView resultList;
    ResultAdapterOverview adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        // Declaration of local variables
        TextView txtname = (TextView) findViewById(R.id.textView_success);
        Button btninventorty = (Button) findViewById(R.id.button_main_inventory);
        Button btnshopping = (Button) findViewById(R.id.button_main_shopping);
        Intent intent = getIntent();
        String loginName = intent.getStringExtra("Name");
        txtname.setText("Welcome, " + loginName);
        resultList = findViewById(R.id.recyclerView_overview);

        /* ParseQuery which retrieves the household inventory and sorts it in ascending order using
        the expiry date */
        ParseQuery<ParseObject> inventoryOverviewQuery =
                new ParseQuery<ParseObject>("HouseholdInventory")
                        .addAscendingOrder("expiryDate")
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

        // OnCLickListeners for navigation from the Welcome screen
        btninventorty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeScreenActivity.this,
                        HouseholdInventoryActivity.class);
                intent.putExtra("Name", loginName);
                intent.putExtra("Category", "");
                startActivity(intent);
            }
        });

        btnshopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeScreenActivity.this,
                        ShoppingListActivity.class);
                intent.putExtra("Name", loginName);
                startActivity(intent);
            }
        });
    }

    // Textview onClick listener that allows the user to logout when they are done
    public void logOut(View view){
        Intent intent = new Intent(WelcomeScreenActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    /* This is the data received from the ParseQuery and is placed in the result adapter to
    organise and display */
    public void initData(List<ParseObject> objects) {
        adapter = new ResultAdapterOverview(this, objects);
        resultList.setLayoutManager(new LinearLayoutManager(this));
        resultList.setAdapter(adapter);
    }

}
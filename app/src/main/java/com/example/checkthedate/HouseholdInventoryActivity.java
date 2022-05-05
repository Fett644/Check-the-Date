package com.example.checkthedate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/* Household inventory activity displays the inventory in greater detail, allows items to be
checked for deletion, adding to shopping list and searching for recipes */
public class HouseholdInventoryActivity extends AppCompatActivity {
    // Global declaration of variables
    RecyclerView inventoryList;
    ResultAdapterInventory adapter;
    String category = "";
    ArrayList<String> inventoryArray;
    ArrayList<String> inventoryTypeArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_household_inventory);

        // Declaration of local variables
        TextView txtInventoryWelcome = (TextView) findViewById(R.id.textView_inventory_welcome);
        Button btn_add_item = (Button) findViewById(R.id.btn_add_item);
        Button btn_shopping = (Button) findViewById(R.id.btn_inventory_shopping);
        Button btn_fresh = (Button) findViewById(R.id.buttonFresh);
        Button btn_frozen = (Button) findViewById(R.id.buttonFrozen);
        Button btn_cupboard = (Button) findViewById(R.id.buttonCupboard);
        Button btn_delete = (Button) findViewById(R.id.buttonDeleteInventory);
        Button btn_add_shopping = (Button) findViewById(R.id.buttonAddShopping);
        Button btn_recipe = (Button) findViewById(R.id.buttonRecipe);
        inventoryList = findViewById(R.id.inventory_list);
        ImageView home_banner = (ImageView) findViewById(R.id.imageView5);
        String loginName = getIntent().getStringExtra("Name");
        String collectedCategory = getIntent().getStringExtra("Category");
        txtInventoryWelcome.setText("Welcome to your Household Inventory, " + loginName);
        TextView goBack = (TextView) findViewById(R.id.textViewInventoryBack);

        /* When the activity loads it looks for a category in the intent putExtra, this is used
        * when the Fresh, Frozen or Cupboard buttons are used to narrow the items on display,
        * however the Welcome Activity automatically sets category to "" so it will use
        * the below if to display the full unsorted inventory */
        if (collectedCategory.equals("")) {
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
        } else {
            /* This query uses the category when the Frozen, Fresh and Cupboard buttons are used to
            sort the inventory on display */
            ParseQuery<ParseObject> inventoryOverviewQuery =
                    new ParseQuery<ParseObject>("HouseholdInventory")
                            .addAscendingOrder("expiryDate")
                            .whereMatches("username", loginName)
                            .whereMatches("productType", collectedCategory);
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
        }

        /* Below are the 3 buttons which sort the inventory on display, when clicked they take
        * the selected category and restart the activity sending that category as a putExtra
        * so the ParseQuery only gets items with that same product type from the database */
        btn_fresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "Fresh";
                Intent intent = new Intent(HouseholdInventoryActivity.this,
                        HouseholdInventoryActivity.class);
                intent.putExtra("Name", loginName);
                intent.putExtra("Category", category);
                startActivity(intent);
            }
        });

        btn_frozen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "Frozen";
                Intent intent = new Intent(HouseholdInventoryActivity.this,
                        HouseholdInventoryActivity.class);
                intent.putExtra("Name", loginName);
                intent.putExtra("Category", category);
                startActivity(intent);
            }
        });

        btn_cupboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "Cupboard";
                Intent intent = new Intent(HouseholdInventoryActivity.this,
                        HouseholdInventoryActivity.class);
                intent.putExtra("Name", loginName);
                intent.putExtra("Category", category);
                startActivity(intent);
            }
        });

        // Starts the add item activity
        btn_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HouseholdInventoryActivity.this,
                        AddItemActivity.class);
                intent.putExtra("Name", loginName);
                startActivity(intent);
            }
        });

        // When clicked it retrieves checked items and sends them to deleteFromInventory() function
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inventoryArray = adapter.getCheckedInventoryList();
                for (int i = 0; i < inventoryArray.size(); i++) {
                    String productName = inventoryArray.get(i);
                    deleteFromInventory(productName);
                }
            }
        });

        // When clicked it retrieves checked items and sends them to searchRecipes() function
        btn_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipe = "";
                inventoryArray = adapter.getCheckedInventoryList();
                for (int i = 0; i < inventoryArray.size(); i++) {
                    String productName = inventoryArray.get(i);
                    recipe = recipe + " " + productName;
                    searchRecipes(recipe);
                }
            }
        });

        // When clicked it retrieves checked items and sends them to addToShoppingList() function
        btn_add_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inventoryArray = adapter.getCheckedInventoryList();
                inventoryTypeArray = adapter.getArrayInventoryTypeChecked();
                for (int i = 0; i < inventoryArray.size(); i++) {
                    String productName = inventoryArray.get(i);
                    String productType = inventoryTypeArray.get(i);
                    addToShoppingList(productName, productType);
                }
            }
        });

        // Simply navigates to the shopping list activity
        btn_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HouseholdInventoryActivity.this,
                        ShoppingListActivity.class);
                intent.putExtra("Name", loginName);
                startActivity(intent);
            }
        });

        home_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HouseholdInventoryActivity.this,
                        WelcomeScreenActivity.class);
                startActivity(intent);
            }
        });

        // Allows user to go back to previous activity
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HouseholdInventoryActivity.this,
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
        adapter = new ResultAdapterInventory(this, objects);
        inventoryList.setLayoutManager(new LinearLayoutManager(this));
        inventoryList.setAdapter(adapter);
    }

    // Function that takes checked items and deletes them from the specified class in the DB
    private void deleteFromInventory(String product_Name){
        Intent intent = getIntent();
        String loginName = intent.getStringExtra("Name");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("HouseholdInventory");
        query.whereEqualTo("productName", product_Name).whereEqualTo("username", loginName);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    try {
                        object.delete();
                        Toast.makeText(HouseholdInventoryActivity.this,
                                "Item/s successfully deleted!", Toast.LENGTH_SHORT).show();
                        startActivity(getIntent());
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                        Toast.makeText(HouseholdInventoryActivity.this,
                                "Error!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HouseholdInventoryActivity.this,"Error!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /* Function that takes checked items and sends them to the recipe search activity and starts
     that activity */
    private void searchRecipes(String product_Name){
        Intent intentLogin = getIntent();
        String loginName = intentLogin.getStringExtra("Name");
        /* "Recipe " is added to string to be passed so the search term will always be "Recipe "
         followed by the food items checked and sent from the household inventory */
        String recipe = "Recipe ";
        recipe = recipe + product_Name;
        Intent intent = new Intent(HouseholdInventoryActivity.this,
                RecipeSearch.class);
        intent.putExtra("Recipe", recipe);
        intent.putExtra("Name", loginName);
        startActivity(intent);
    }

    // Function that takes the checked items and adds them to the shopping list class in the DB
    private void addToShoppingList(String product_Name, String product_Type) {
        Intent intent = getIntent();
        String loginName = intent.getStringExtra("Name");
        ParseObject products = new ParseObject("ShoppingList");

        products.put("productName", product_Name);
        products.put("productType", product_Type);
        products.put("quantity", "1");
        products.put("username", loginName);

        products.saveInBackground(e -> {
            if (e != null) {
                Toast.makeText(this, "Fail to add data.." + e.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Product successfully added to shopping list!",
                        Toast.LENGTH_LONG).show();
            }
        });


    }

}
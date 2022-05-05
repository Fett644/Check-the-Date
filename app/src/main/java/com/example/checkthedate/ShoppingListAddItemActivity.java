package com.example.checkthedate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseObject;

// Activity which allows the user to add items to their shopping list
public class ShoppingListAddItemActivity extends AppCompatActivity {

    //Global Declaration of variables
    String productName, productQuantity, productType, username;
    EditText txtProductName, txtProductQuantity;
    Spinner productTypeDropdownList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_add_item);

        // Declaration of local variables
        txtProductName = (EditText) findViewById(R.id.editTextProductNameList);
        txtProductQuantity = (EditText) findViewById(R.id.editTextQuantity);
        productTypeDropdownList = (Spinner) findViewById(R.id.productTypeDropdownList);
        Button btnAddItem = (Button) findViewById(R.id.buttonAddItemList);
        Intent intent = getIntent();
        String loginName = intent.getStringExtra("Name");

        // Function to populate the spinner dropdown
        populateDropdownList();

        // Takes entered data and calls on the addYoShoppingList() function
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productName = txtProductName.getText().toString();
                productQuantity = txtProductQuantity.getText().toString();
                productType = productTypeDropdownList.getSelectedItem().toString();
                username = loginName;

                addToShoppingList();
            }
        });
    }

    // Function that uses the above data to enter it to the specific class in the DB
    public void addToShoppingList() {
        ParseObject items = new ParseObject("ShoppingList");

        items.put("productName", productName);
        items.put("quantity", productQuantity);
        items.put("productType", productType);
        items.put("username", username);

        items.saveInBackground(e -> {
           if (e != null) {
               Toast.makeText(this, "Fail to add data.." + e.getLocalizedMessage(),
                       Toast.LENGTH_LONG).show();
           } else {
               Toast.makeText(this, "Item successfully added to inventory!",
                       Toast.LENGTH_LONG).show();
               txtProductName.setText("");
               txtProductQuantity.setText("");
           }
        });
    }

    /* Function which populates the spinner dropdown using a specific set of strings from
    strings.xml */
    public void populateDropdownList() {
        // Fills the dropdown list with brand names from strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.productTypesList, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productTypeDropdownList.setAdapter(adapter);
    }

    // Allows user to go back to previous activity
    public void goBack(View view){
        Intent intentName = getIntent();
        String loginName = intentName.getStringExtra("Name");
        Intent intent = new Intent(ShoppingListAddItemActivity.this,
                ShoppingListActivity.class);
        intent.putExtra("Name", loginName);
        startActivity(intent);
        finish();
    }
}
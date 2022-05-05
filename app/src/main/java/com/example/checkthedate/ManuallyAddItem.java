package com.example.checkthedate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ManuallyAddItem extends AppCompatActivity {

    // Global declaration of variables
    String productBarcode, productName, productExpiry, productType, username;
    EditText txtProductBarcode, txtProductName, txtProductExpiry;
    Spinner productTypeDropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manually_add_item);

        // Declaration of local variables
        TextView txtNameManualWelcome = (TextView) findViewById(R.id.textViewManualWelcome);
        txtProductBarcode = (EditText) findViewById(R.id.editText_Product_Barcode);
        txtProductName = (EditText) findViewById(R.id.editText_Product_Name);
        txtProductExpiry = (EditText) findViewById(R.id.editText_Expiry_Date);
        productTypeDropdown = (Spinner) findViewById(R.id.productTypeDropdown);
        Button btnAddToInventory = (Button) findViewById(R.id.button_Add_Product);
        Intent intent = getIntent();
        String loginName = intent.getStringExtra("Name");
        String barcode = intent.getStringExtra("BarcodeData");

        // Function to populate the spinner dropdown
        populateDropdown();

        /* Runs if a valid barcode has been passed from previous activity, if it has it will
        retrieve and set the product id and name in the relevant fields, if the barcode isn't
         found the user is informed and the barcode alone is set*/
        if ( barcode != null) {
            ParseQuery<ParseObject> inventoryOverviewQuery =
                    new ParseQuery<ParseObject>("ProductInventory")
                            .whereMatches("productID", barcode);
            inventoryOverviewQuery.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> objList, ParseException e) {
                    int objectTest = objList.size();
                    if (e == null && objectTest != 0) {
                        ParseObject object = objList.get(0);
                        txtProductBarcode.setText(object.getString("productID"));
                        txtProductName.setText(object.getString("productName"));
                    } else {
                        Toast.makeText(ManuallyAddItem.this,
                                "Barcode not found!",
                                Toast.LENGTH_SHORT).show();
                        txtProductBarcode.setText(barcode);
                    }
                }
            });

        }

        // When clicked it sets the entered product details for the addInventory() function
        btnAddToInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productBarcode = txtProductBarcode.getText().toString();
                productName = txtProductName.getText().toString();
                productExpiry = txtProductExpiry.getText().toString();
                productType = productTypeDropdown.getSelectedItem().toString();
                username = loginName;

                // Validation on entered product data
                if (productType.equals("")) {
                    Toast.makeText(ManuallyAddItem.this,
                            "Please ensure no fields are empty. Thank you!",
                            Toast.LENGTH_SHORT).show();
                } else if (productName.equals("")) {
                    Toast.makeText(ManuallyAddItem.this,
                            "Please ensure no fields are empty. Thank you!",
                            Toast.LENGTH_SHORT).show();
                } else if (productExpiry.equals("")) {
                    Toast.makeText(ManuallyAddItem.this,
                            "Please ensure no fields are empty. Thank you!",
                            Toast.LENGTH_SHORT).show();
                } else if (productBarcode.length() != 13) {
                    Toast.makeText(ManuallyAddItem.this,
                            "Please enter a valid barcode. Thank you!",
                            Toast.LENGTH_SHORT).show();
                } else {
                        addToInventory();
                }
            }
        });
    }

    /* Function that takes entered data and uses a put method to add it to the Household Inventory
    class in the DB */
    public void addToInventory(){
        ParseObject products = new ParseObject("HouseholdInventory");

        products.put("productID", productBarcode);
        products.put("productName", productName);
        products.put("productType", productType);
        products.put("expiryDate", productExpiry);
        products.put("username", username);

        products.saveInBackground(e -> {
           if (e != null) {
               Toast.makeText(this, "Fail to add data.." + e.getLocalizedMessage(),
                       Toast.LENGTH_LONG).show();
           } else {
               Toast.makeText(this, "Product successfully added to inventory!",
                       Toast.LENGTH_LONG).show();
               txtProductBarcode.setText("");
               txtProductName.setText("");
               txtProductExpiry.setText("");
           }
        });
    }

    // Populates the spinner dropdown with items from the strings.xml
    public void populateDropdown() {
        // Fills the dropdown list with brand names from strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.productTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productTypeDropdown.setAdapter(adapter);
    }

    // Allows user to go back to previous activity
    public void goBack(View view){
        Intent intentUsername = getIntent();
        String loginName = intentUsername.getStringExtra("Name");
        Intent intent = new Intent(ManuallyAddItem.this, AddItemActivity.class);
        intent.putExtra("Name", loginName);
        startActivity(intent);
        finish();
    }
}
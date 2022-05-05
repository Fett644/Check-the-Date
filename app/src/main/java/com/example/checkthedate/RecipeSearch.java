package com.example.checkthedate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

/* Activity that allows user to take retrieved inputs from household inventory and use them to do a
 google search for recipes */
public class RecipeSearch extends AppCompatActivity {

    // Global Declaration of variables
    EditText editText;
    Button btnSearch;
    TextView textViewRecipes;
    TextView backRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_search);

        // Declaration of local variables
        String recipe = getIntent().getStringExtra("Recipe");
        String category = "";

        Intent intent = getIntent();
        String loginName = intent.getStringExtra("Name");

        backRecipes = findViewById(R.id.textViewRecipesBack);
        textViewRecipes = findViewById(R.id.textViewRecipes);
        textViewRecipes.setText("Search for Recipes from here!");
        editText = findViewById(R.id.editTextRecipe);
        editText.setText(recipe);
        btnSearch = findViewById(R.id.btnSearch);

        /* Uses string in editText box to send to the SearchManager API that sends it to google and
         does a search */
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                String term = editText.getText().toString();
                intent.putExtra(SearchManager.QUERY, term);
                startActivity(intent);
            }
        });

        // Allows user to go back to previous activity
        backRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeSearch.this,
                        HouseholdInventoryActivity.class);
                intent.putExtra("Name", loginName);
                intent.putExtra("Category", category);
                startActivity(intent);
                finish();
            }
        });
    }
}

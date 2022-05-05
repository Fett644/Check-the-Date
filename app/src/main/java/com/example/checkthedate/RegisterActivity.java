package com.example.checkthedate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;

// Register Activity allows users to create an account using their details
public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Declaration of local variables
        EditText usernameText = (EditText) findViewById(R.id.editText_reg_username);
        EditText emailText = (EditText) findViewById(R.id.editTextText_reg_email);
        EditText passwordText = (EditText) findViewById(R.id.editText_reg_password);
        EditText confirmPasswordText = (EditText) findViewById(R.id.editText_reg_confirm_password);
        Button registerButton = (Button) findViewById(R.id.button_register);

        /* Register button takes the users entered details saves them in the database after passing
        some validation */
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameText.getText().toString();
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                String confirm_password = confirmPasswordText.getText().toString();

                // If statement checks passwords match and if they do calls signUp() function
                if (password.equals(confirm_password)) {
                    signUp(username, password, email);
                } else {
                    Toast.makeText(RegisterActivity.this, "Ensure passwords match.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            // signUp() saves the users details in the database to serve as their login credentials
            public void signUp(String uname, String pword, String email) {
                ParseUser user = new ParseUser();

                user.setUsername(uname);
                user.setEmail(email);
                user.setPassword(pword);
                user.signUpInBackground(e -> {
                    if (e == null) {
                        Toast.makeText(RegisterActivity.this,
                                "User Registered successfully",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this,
                                SignInActivity.class);
                        startActivity(intent);
                    } else {
                        ParseUser.logOut();
                        Toast.makeText(RegisterActivity.this, e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
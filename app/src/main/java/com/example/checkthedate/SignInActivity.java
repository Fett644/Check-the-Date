package com.example.checkthedate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;

// Sign in activity which allows users to sign in in using their registered credentials
public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Declaration of local variables
        EditText usernameText = (EditText) findViewById(R.id.editText_username);
        EditText passwordText = (EditText) findViewById(R.id.editText_password);
        Button signInButton = (Button) findViewById(R.id.button_signin);
        Button register_now = (Button) findViewById(R.id.button_register_now);
        Button forgot_password = (Button) findViewById(R.id.button_forgot_password);

        /* OnClick Listener for the sign in button, when clicked it checks if user exists and if
        the credentials are correct, if they are the user is logged in, if not they get an error*/
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = usernameText.getText().toString();
                String userPassword = passwordText.getText().toString();
                userSignIn(userName, userPassword);
            }
            public void userSignIn(String u_name, String p_word) {
                ParseUser.logInInBackground(u_name, p_word, (parseUser, e) -> {
                    if (parseUser != null) {
                        Toast.makeText(SignInActivity.this,
                                "Login Successful! Welcome back "
                                + u_name + " !", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignInActivity.this,
                                WelcomeScreenActivity.class);
                        intent.putExtra("Name", u_name);
                        startActivity(intent);
                    } else {
                        ParseUser.logOut();
                        Toast.makeText(SignInActivity.this, e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // Register button takes user to the register activity where they can create and account
        register_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        /* Forgot Password takes user to forgot password activity where they can reset
        their password */
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this,
                        ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });


    }
}
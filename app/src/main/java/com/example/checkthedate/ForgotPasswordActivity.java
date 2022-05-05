package com.example.checkthedate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

/* Forgot password activity allows users to request a password reset email if they have forgotten
their password */
public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Declaration of local variables
        EditText emailText = (EditText) findViewById(R.id.editTextText_Email_Reset);
        Button resetButton = (Button) findViewById(R.id.button_reset_password);

        // Reset button takes users enter email and uses parse to request a password reset email
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                resetPassword(email);
            }

            /* resetPassword() is called from onClick above, if the users email exists a password
            reset email is sent, if not an error is shown */
            public void resetPassword(String e_mail) {
                ParseUser.requestPasswordResetInBackground(e_mail,
                        new RequestPasswordResetCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // An email was successfully sent with reset instructions.
                            Toast.makeText(ForgotPasswordActivity.this,
                                    "Password reset email sent!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ForgotPasswordActivity.this,
                                    SignInActivity.class);
                            startActivity(intent);
                        } else {
                            // Something went wrong. Look at the ParseException to see what's up.
                            Toast.makeText(ForgotPasswordActivity.this, e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }
}
package com.example.alcholator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SigninActivity extends AppCompatActivity {
    public static String uid; // a static variable to hold the user ID after login
    private EditText emailTextView, passwordTextView; // text fields for email and password input
    private Button Btn; // button for login
    private ProgressBar progressBar; // progress bar to indicate the login progress
    public CheckBox rememberMe; // checkbox to remember the login credentials

    private FirebaseAuth mAuth; // Firebase authentication object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        rememberMe = findViewById(R.id.rememberme);

        // get an instance of the Firebase authentication object
        mAuth = FirebaseAuth.getInstance();

        // initialize all the views through their IDs defined above
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.password);
        Btn = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar);

        // On subsequent app launches, check if saved login credentials exist in local storage
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            // Automatically log in the user using saved login credentials
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // User logged in successfully
                        uid = mAuth.getCurrentUser().getUid();
                        Toast.makeText(getApplicationContext(), "Login successful!!", Toast.LENGTH_LONG).show();
                        // if sign-in is successful, start the AlcoholCalculator activity
                        Intent intent = new Intent(SigninActivity.this, AlcoholCalculator.class);
                        startActivity(intent);
                        finish(); // close the SigninActivity to prevent going back to it with the back button
                    } else {
                        // User login failed
                        Toast.makeText(getApplicationContext(), "Login failed!!", Toast.LENGTH_LONG).show();
                    }
                }
            });

            // Set an onClickListener on the Sign-in button to call loginUserAccount() when clicked
            Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginUserAccount();
                }
            });

            // Set up the "Not a member? Sign up now!" clickable text
            TextView signupTextView = findViewById(R.id.signup_textview);
            String text = "Not a member? Sign up now!";
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    // When the text is clicked, start the SignupActivity
                    Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                    startActivity(intent);
                }
            };
            // Add the clickable span to the "Sign up now!" text
            spannableStringBuilder.setSpan(clickableSpan, text.indexOf("Sign up now!"), text.indexOf("Sign up now!") + "Sign up now!".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            signupTextView.setText(spannableStringBuilder);
            signupTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    // This method is called when the user clicks the Sign-in button
    private void loginUserAccount() {

        // Show the progress bar to indicate that the login is being processed
        progressBar.setVisibility(View.VISIBLE);

        // Get the email and password entered by the user
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        // Perform some basic validation on the email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(), "Invalid email or password!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the "remember me" checkbox is checked
        if (rememberMe.isChecked()) {
            // Save the user's login credentials securely in local storage
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email", email);
            editor.putString("password", password);
            editor.apply();
        }

        // Sign-in an existing user with the provided email and password
        mAuth.signInWithEmailAndPassword(email, password)
                // Add a listener to be notified when the authentication attempt is complete
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // If sign-in is successful, get the user ID and show a success message
                            uid = mAuth.getCurrentUser().getUid();
                            Toast.makeText(getApplicationContext(), "Login successful!!", Toast.LENGTH_LONG).show();

                            // Hide the progress bar
                            progressBar.setVisibility(View.GONE);

                            // Create an intent to navigate to the home activity
                            Intent intent = new Intent(SigninActivity.this, AlcoholCalculator.class);
                            startActivity(intent);
                        } else {
                            // If sign-in fails, show an error message
                            Toast.makeText(getApplicationContext(), "Login failed!!", Toast.LENGTH_LONG).show();

                            // Hide the progress bar
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
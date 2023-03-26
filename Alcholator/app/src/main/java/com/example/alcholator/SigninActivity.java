package com.example.alcholator;

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
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SigninActivity extends AppCompatActivity {
    public static String uid;
    private EditText emailTextView, passwordTextView;
    private ProgressBar progressBar;
    public CheckBox rememberMe;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        rememberMe = findViewById(R.id.rememberme);

        // taking instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.password);
        Button btn = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar);

        // On subsequent app launches, check if saved login credentials exist in local storage
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            // Automatically log in the user using saved login credentials
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    // User logged in successfully
                    uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                    // if sign-in is successful
                    Intent intent = new Intent(SigninActivity.this, AlcoholCalculator.class);
                    startActivity(intent);
                    finish(); // close the SigninActivity
                } else {
                    // User login failed
                    Toast.makeText(getApplicationContext(), "Login failed!!", Toast.LENGTH_LONG).show();
                }
            });
        }
        // Set on Click Listener on Sign-in button
        btn.setOnClickListener(v -> loginUserAccount());

        TextView signupTextView = findViewById(R.id.signup_textview);
        String text = "Not a member? Sign up now!";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        };
        spannableStringBuilder.setSpan(clickableSpan, text.indexOf("Sign up now!"), text.indexOf("Sign up now!") + "Sign up now!".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        signupTextView.setText(spannableStringBuilder);
        signupTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void loginUserAccount() {

        // show the visibility of progress bar to show loading
        progressBar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(),
                            "Invalid email or password!",
                            Toast.LENGTH_SHORT)
                    .show();
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

        // signin existing user
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_SHORT).show();

                // hide the progress bar
                progressBar.setVisibility(View.GONE);

                // if sign-in is successful
                // intent to home activity
                Intent intent = new Intent(SigninActivity.this, AlcoholCalculator.class);
                startActivity(intent);
            } else {

                // sign-in failed
                Toast.makeText(getApplicationContext(), "Login failed!", Toast.LENGTH_SHORT).show();

                // hide the progress bar
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
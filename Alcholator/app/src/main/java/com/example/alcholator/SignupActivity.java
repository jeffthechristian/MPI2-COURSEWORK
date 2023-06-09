package com.example.alcholator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    private EditText emailTextView, passwordTextView, confirmPasswordTextView;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.passwd);
        Button btn = findViewById(R.id.btnregister);
        progressBar = findViewById(R.id.progressbar);
        confirmPasswordTextView = findViewById(R.id.passwd2);

        // Set on Click Listener on Registration button
        btn.setOnClickListener(v -> registerNewUser());

        TextView signupTextView = findViewById(R.id.signin);
        String text = "Already a member? Sign in!";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        };
        spannableStringBuilder.setSpan(clickableSpan, text.indexOf("Sign in!"), text.indexOf("Sign in!") + "Sign in!".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        signupTextView.setText(spannableStringBuilder);
        signupTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void registerNewUser() {

        // show the visibility of progress bar to show loading
        progressBar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password, confirmPassword;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
        confirmPassword = confirmPasswordTextView.getText().toString();

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Passwords doesn't match!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(), "Invalid email format!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8) {
            Toast.makeText(getApplicationContext(), "Password must be at least 8 characters long!!", Toast.LENGTH_SHORT).show();
            return;
        }

        // create new user or register new user
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

                // if the user created intent to login activity
                Intent intent = new Intent(SignupActivity.this, DataInput.class);
                startActivity(intent);
            } else {
                // Registration failed
                Toast.makeText(getApplicationContext(), "Registration failed!!" + " Please try again later", Toast.LENGTH_SHORT).show();

                // hide the progress bar
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
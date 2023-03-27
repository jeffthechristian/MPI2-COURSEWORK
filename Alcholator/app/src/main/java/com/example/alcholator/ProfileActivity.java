package com.example.alcholator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class ProfileActivity extends AppCompatActivity {
    ImageButton show_history, calculate;
    Button editProfile, friendList, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        show_history = findViewById(R.id.show_history);
        calculate = findViewById(R.id.calculate);
        editProfile = findViewById(R.id.editData);
        friendList = findViewById(R.id.friendlist);
        logout = findViewById(R.id.logout);

        show_history.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        calculate.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, AlcoholCalculator.class);
            startActivity(intent);
        });

        editProfile.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, DataInput.class);
            startActivity(intent);
        });

        logout.setOnClickListener(view -> {
            // Clear saved login credentials from local storage
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}

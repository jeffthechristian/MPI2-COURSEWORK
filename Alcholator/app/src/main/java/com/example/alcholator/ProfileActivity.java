package com.example.alcholator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileActivity extends AppCompatActivity {
    ImageButton show_history, calculate;
    Button editProfile, friendList, logout;
    TextView welcomeText, currentGender, currentWeight;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        assert currentUser != null;
        String email = currentUser.getEmail();
        assert email != null;
        String username = email.substring(0, email.indexOf("@"));

        show_history = findViewById(R.id.show_history);
        calculate = findViewById(R.id.calculate);
        editProfile = findViewById(R.id.editData);
        friendList = findViewById(R.id.friendlist);
        logout = findViewById(R.id.logout);
        currentGender = findViewById(R.id.currentGender);
        currentWeight = findViewById(R.id.currentWeight);
        welcomeText = findViewById(R.id.welcomeName);
        welcomeText.setText("Welcome, " + username + "!");


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userData").child(uid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.exists()) {
                    DataSnapshot genderSnapshot = dataSnapshot.child("gender");
                    DataSnapshot weightSnapshot = dataSnapshot.child("weight");

                    if (genderSnapshot.exists() && weightSnapshot.exists()) {
                        String gender = genderSnapshot.getValue(String.class);
                        String weightString = dataSnapshot.child("weight").getValue(String.class);
                        assert weightString != null;
                        double weight = Double.parseDouble(weightString);
                        if (gender == "0.68") {
                            currentGender.setText("Gender: male");
                        } else {
                            currentGender.setText("Gender: female");
                        }
                        currentWeight.setText("Weight: " + weight + "kg");
                    } else {
                        Log.d("TAG", "Gender or weight node does not exist");
                    }
                } else {
                    Log.d("TAG", "DataSnapshot is null or does not exist");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        calculate.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, AlcoholCalculator.class);
            startActivity(intent);
        });

        show_history.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
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

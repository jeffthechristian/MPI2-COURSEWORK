package com.example.alcholator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class HistoryActivity extends AppCompatActivity {
    private FirebaseUser currentUser;
    private ListView listView;
    ImageButton edit_profile, calculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        edit_profile = findViewById(R.id.edit_profile);
        calculate = findViewById(R.id.calculate);

        // Get the current user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Get a reference to the Firebase Realtime Database
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("history2");

        listView = findViewById(R.id.listView);

        // Set up a listener for changes in the data
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Create a new list to hold the data
                ArrayList<String> listData = new ArrayList<>();
                ArrayList<String> listData1 = new ArrayList<>();

                // Loop through the data and add it to the list if it belongs to the current user
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String uid = data.getKey();
                    if (uid.equals(currentUser.getUid())) {
                        for (DataSnapshot child : data.getChildren()) {
                            String bloodResult = child.child("bloodResult").getValue(String.class);
                            String date = child.child("date").getValue(String.class);
                            String dataString = "Blood result: " + bloodResult;
                            listData.add(dataString);
                            listData1.add(date);
                        }
                    }
                }

                // Reverse the order of the lists
                Collections.reverse(listData);
                Collections.reverse(listData1);

                // Set up the adapter for the ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(HistoryActivity.this, android.R.layout.simple_list_item_2, android.R.id.text1, listData) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView text1 = view.findViewById(android.R.id.text1);
                        TextView text2 = view.findViewById(android.R.id.text2);

                        text1.setTextColor(Color.WHITE);
                        text2.setTextColor(Color.GRAY);
                        text2.setText(listData1.get(position));

                        return view;
                    }
                };
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryActivity.this, AlcoholCalculator.class);
                startActivity(intent);
            }
        });
    }
}

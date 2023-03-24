package com.example.alcholator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Get the current user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Get a reference to the Firebase Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference("history2");

        listView = findViewById(R.id.listView);

        // Set up a listener for changes in the data
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Create a new list to hold the data
                ArrayList<String> listData = new ArrayList<>();

                // Loop through the data and add it to the list if it belongs to the current user
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String uid = data.getKey();
                    if (uid.equals(currentUser.getUid())) {
                        for (DataSnapshot child : data.getChildren()) {
                            String bloodResult = child.child("bloodResult").getValue(String.class);
                            String date = child.child("date").getValue(String.class);
                            String dataString = "Blood result: " + bloodResult + ", Date: " + date;
                            listData.add(dataString);
                        }
                    }
                }

                // Set up the adapter for the ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(HistoryActivity.this, android.R.layout.simple_list_item_1, listData);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }
}

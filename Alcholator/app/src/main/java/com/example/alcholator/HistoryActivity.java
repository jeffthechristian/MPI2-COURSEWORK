package com.example.alcholator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserUid = mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference("history");
        Query currentUserQuery = mDatabase.orderByChild("userUID").equalTo(currentUserUid);

        mListView = findViewById(R.id.listView);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> historyEntries = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String date = ds.child("date").getValue(String.class);
                    String bloodResult = ds.child("bloodResult").getValue(String.class);
                    String historyEntry = "Date: " + date + ", Blood Result: " + bloodResult;
                    historyEntries.add(historyEntry);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter(
                        HistoryActivity.this,
                        android.R.layout.simple_list_item_1,
                        historyEntries
                ) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView textView = view.findViewById(android.R.id.text1);
                        textView.setTextColor(Color.WHITE);
                        return view;
                    }
                };
                mListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("HistoryActivity", databaseError.getMessage());
            }
        };

        currentUserQuery.addValueEventListener(valueEventListener);
    }
}
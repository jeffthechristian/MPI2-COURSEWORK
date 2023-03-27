
package com.example.alcholator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class DataInput extends AppCompatActivity {
    public CheckBox maleBox, femaleBox;
    public TextView weightInput;
    ImageButton show_history, calculate, edit_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_input);

        maleBox = findViewById(R.id.maleBox);
        femaleBox = findViewById(R.id.femaleBox);

        show_history = findViewById(R.id.show_history);
        calculate = findViewById(R.id.calculate);
        edit_profile = findViewById(R.id.edit_profile);

        weightInput = findViewById(R.id.weightInput);
        weightInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        maleBox.setOnClickListener(v -> {
            if (((CheckBox) v).isChecked()) {
                femaleBox.setChecked(false);
            }
        });

        femaleBox.setOnClickListener(v -> {
            if (((CheckBox) v).isChecked()) {
                maleBox.setChecked(false);
            }
        });

        //Go to alc. calculator activity with data input
        Button btnSaveData = findViewById(R.id.editData);
        btnSaveData.setOnClickListener(view -> {
            String gender = genderCheck();
            String weight = weightInput.getText().toString();

            if (TextUtils.isEmpty(weight) || weight.startsWith("0")) {
                Toast.makeText(DataInput.this, "Please enter correct data", Toast.LENGTH_LONG).show();
            } else {
                DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("userData");
                String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                // Check if user has existing data
                dataRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // User has existing data, update it
                            DataEntry dataEntry = new DataEntry(uid, weight, gender);
                            dataRef.child(uid).setValue(dataEntry);
                            Toast.makeText(DataInput.this, "Data updated.", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(DataInput.this, ProfileActivity.class);
                            startActivity(intent);
                        } else {
                            // User does not have existing data, create new node
                            DataEntry dataEntry = new DataEntry(uid, weight, gender);
                            dataRef.child(uid).setValue(dataEntry);
                            Toast.makeText(DataInput.this, "Data saved.", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(DataInput.this, ProfileActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("DataInput", "Error retrieving data: " + error.getMessage());
                        Toast.makeText(DataInput.this, "Error retrieving data.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        show_history.setOnClickListener(view -> {
            Intent intent = new Intent(DataInput.this, HistoryActivity.class);

            startActivity(intent);

        });
        calculate.setOnClickListener(view -> {
            Intent intent = new Intent(DataInput.this, AlcoholCalculator.class);
            startActivity(intent);

        });

        edit_profile.setOnClickListener(view -> {
            Intent intent = new Intent(DataInput.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    public String genderCheck() {
        String male = "0.68";
        String female = "0.55";
        String noGender = "0.68";
        String gender;

        if (maleBox.isChecked()) {
            gender = male;
        } else if (femaleBox.isChecked()) {
            gender = female;
        } else {
            gender = noGender;
        }
        return gender;
    }
}


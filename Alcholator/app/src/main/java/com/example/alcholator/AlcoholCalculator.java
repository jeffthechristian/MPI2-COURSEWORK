package com.example.alcholator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlcoholCalculator extends AppCompatActivity {
    TextView alcStrengthInput, volumeInput;
    Button btnSaveData2;
    ImageButton edit_profile, show_history;
    String sgender, sweight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alcohol_calculator);

        List<alcoholObj> objects = new ArrayList<>();
        objects.add(new alcoholObj("Select a drink",0,0));
        objects.add(new alcoholObj("Light Beer", 4,0.5));
        objects.add(new alcoholObj("Cider", 4.5,0.5));
        objects.add(new alcoholObj("Beer", 5.2,0.5));
        objects.add(new alcoholObj("Champagne", 11,0.75));
        objects.add(new alcoholObj("Wine", 12,0.75));
        objects.add(new alcoholObj("Liquor", 20,0.5));
        objects.add(new alcoholObj("Jagermeister", 35,0.5));
        objects.add(new alcoholObj("Tequila", 38,0.5));
        objects.add(new alcoholObj("Vodka", 40,0.5));
        objects.add(new alcoholObj("Rum", 50,0.5));
        objects.add(new alcoholObj("Spirit", 90,0.1));

        ArrayList<String> spinnerValues = new ArrayList<>();
        for (alcoholObj object : objects) {
            spinnerValues.add(object.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1, spinnerValues){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                text1.setTextColor(Color.WHITE);

                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.spin);
        spinner.setAdapter(adapter);


        alcStrengthInput = findViewById(R.id.alcStrengthInput);
        alcStrengthInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        volumeInput= findViewById(R.id.volumeInput);
        volumeInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        btnSaveData2= findViewById(R.id.btnSaveData2);
        edit_profile = findViewById(R.id.edit_profile);
        show_history = findViewById(R.id.show_history);

        // Retrieve gender and weight values from Firebase Realtime Database
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("userData");
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                alcStrengthInput.setText(objects.get(position).getVolalc().toString());
                volumeInput.setText(objects.get(position).getVol().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                alcStrengthInput.setText("0");
                volumeInput.setText("0");
            }
        });

        dataRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataEntry dataEntry = snapshot.getValue(DataEntry.class);
                    if (dataEntry != null) {
                        sgender = dataEntry.getGender();
                        sweight = dataEntry.getWeight();

                        // Do something with gender and weight values here
                    }
                } else {
                    // User has no data in the database
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AlcoholCalculator.this, "Error retrieving data.", Toast.LENGTH_LONG).show();
            }
        });

        btnSaveData2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences pref = getSharedPreferences("data",Context.MODE_PRIVATE);

                    double gender = Double.parseDouble(sgender);
                    double weight = Double.parseDouble(sweight);
                    try {
                        double alcStrength = Double.parseDouble(alcStrengthInput.getText().toString());
                        double volume = Double.parseDouble(volumeInput.getText().toString());


                        if (alcStrength<=0||volume<=0){
                            throw new Exception();
                        }
                        double vr = alcStrength * (volume * 7.9);
                        double mr = weight * gender;
                        double prom = vr / mr;
                        double sober = prom / 0.16;

                        String ssober = String.valueOf(sober);
                        String sprom = String.valueOf(prom);

                        Intent yoo = new Intent(AlcoholCalculator.this, ResultActivity.class);
                        yoo.putExtra("keyprom", sprom);
                        yoo.putExtra("keysober", ssober);

                        startActivity(yoo);
                    } catch (Exception e) {
                        Toast.makeText(AlcoholCalculator.this, "Please enter correct data", Toast.LENGTH_LONG).show();
                    }
                }
        } );

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlcoholCalculator.this, ProfileActivity.class);
                startActivity(intent);

            }
        } );
        show_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlcoholCalculator.this, HistoryActivity.class);
                startActivity(intent);
            }
        } );
    }
}
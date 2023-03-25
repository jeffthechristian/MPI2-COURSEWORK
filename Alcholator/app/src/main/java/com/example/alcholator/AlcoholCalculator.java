package com.example.alcholator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AlcoholCalculator extends AppCompatActivity {
    TextView alcStrengthInput, volumeInput;
    Button btnSaveData2, btnBack;
    ImageButton edit_profile, show_history, calculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alcohol_calculator);

        alcStrengthInput = findViewById(R.id.alcStrengthInput);
        alcStrengthInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        volumeInput= findViewById(R.id.volumeInput);
        volumeInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        btnSaveData2= findViewById(R.id.btnSaveData2);
        btnBack= findViewById(R.id.btnBack);
        edit_profile = findViewById(R.id.edit_profile);
        show_history = findViewById(R.id.show_history);

        btnSaveData2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences pref = getSharedPreferences("data",Context.MODE_PRIVATE);
                String sgender = pref.getString("keygender", null);
                String sweight = pref.getString("keyweight", "75");



                if(sgender == null){
                    try {
                        sgender = getIntent().getStringExtra("keygender");
                        sweight = getIntent().getStringExtra("keyweight");
                        double gender = Double.parseDouble(sgender);
                        double weight = Double.parseDouble(sweight);
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
                        yoo.putExtra("keygender", sgender);
                        yoo.putExtra("keyweight", sweight);
                        startActivity(yoo);
                    } catch (Exception e) {
                        Toast.makeText(AlcoholCalculator.this, "Please enter correct data", Toast.LENGTH_LONG).show();

                    }
                }
                else{
                    sgender = getIntent().getStringExtra("keygender");
                    sweight = getIntent().getStringExtra("keyweight");
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
                        yoo.putExtra("keygender", sgender);
                        yoo.putExtra("keyweight", sweight);
                        startActivity(yoo);
                    } catch (Exception e) {
                        Toast.makeText(AlcoholCalculator.this, "Please enter correct data", Toast.LENGTH_LONG).show();
                    }
                }

            }
        } );

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AlcoholCalculator.this, DataInput.class));
            }
        } );

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("data",Context.MODE_PRIVATE);
                String gender = pref.getString("keygender", null);
                String weight = pref.getString("keyweight", "0.68");
                Intent intent = new Intent(AlcoholCalculator.this, DataInput.class);
                intent.putExtra("keygender", gender);
                intent.putExtra("keyweight", weight);
                startActivity(intent);

            }
        } );
        show_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("data",Context.MODE_PRIVATE);
                String gender = pref.getString("keygender", null);
                String weight = pref.getString("keyweight", "0.68");
                Intent intent = new Intent(AlcoholCalculator.this, HistoryActivity.class);
                intent.putExtra("keygender", gender);
                intent.putExtra("keyweight", weight);
                startActivity(intent);

            }
        } );
    }
}
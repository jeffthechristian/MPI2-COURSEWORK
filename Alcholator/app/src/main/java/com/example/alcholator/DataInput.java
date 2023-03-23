
package com.example.alcholator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class DataInput extends AppCompatActivity {
    public CheckBox maleBox, femaleBox;
    public TextView weightInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_input);

        maleBox= findViewById(R.id.maleBox);
        femaleBox= findViewById(R.id.femaleBox);

        weightInput = findViewById(R.id.weightInput);
        weightInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        maleBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()) {
                    femaleBox.setChecked(false);
                }
            }
        });

        femaleBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()) {
                    maleBox.setChecked(false);
                }
            }
        });

        //Go to alc. calculator activity with data input
        Button btnSaveData = findViewById(R.id.btnSaveData);
        btnSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String gender = genderCheck();
                String weight = weightInput.getText().toString();

                if(weight.contentEquals("")||weight.startsWith("0")){
                    Toast.makeText(DataInput.this, "Please enter necessary data", Toast.LENGTH_LONG).show();
                }

                else {

                    SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("keygender", gender);
                    editor.putString("keyweight", weight);
                    editor.apply();

                    Intent intent = new Intent(DataInput.this, AlcoholCalculator.class);
                    intent.putExtra("keygender", gender);
                    intent.putExtra("keyweight", weight);

                    startActivity(intent);
                }
            }
        } );

        //Go to alc. calculator activity without data input
        Button btnSkipData = findViewById(R.id.btnSkipData);
        btnSkipData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gender = genderCheck();
                String weight = "75";

                Intent intent = new Intent(DataInput.this, AlcoholCalculator.class);
                intent.putExtra("keygender", gender);
                intent.putExtra("keyweight", weight);
                startActivity(intent);
            }
        } );
    }

    public String genderCheck() {
        String male = "0.68";
        String female = "0.55";
        String noGender = "0.68";
        String gender;

        if(maleBox.isChecked()){
            gender=male;
        }else if(femaleBox.isChecked()){
            gender=female;
        }else{
            gender=noGender;
        }
        return gender;
    }
}


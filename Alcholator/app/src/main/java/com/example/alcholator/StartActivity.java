package com.example.alcholator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //Go to DataInput activity or CheckboxScreen based on checkbox
        Button btnDataInput = findViewById(R.id.btnDataInput);
        CheckBox soulCheck = (CheckBox) findViewById(R.id.soulCheck);
        btnDataInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("data",Context.MODE_PRIVATE);
                String sgender = pref.getString("keygender", null);
                String sweight = pref.getString("keyweight", "0.68");
                if(sgender == null){
                    if (soulCheck.isChecked()) {
                        Intent intent = new Intent(StartActivity.this, DataInput.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(StartActivity.this, CheckboxScreen.class);
                        startActivity(intent);
                    }

                }
                else{
                    if (soulCheck.isChecked()) {
                        Intent intent = new Intent(StartActivity.this, AlcoholCalculator.class);
                        intent.putExtra("keygender", sgender);
                        intent.putExtra("keyweight", sweight);

                        startActivity(intent);

                    } else {
                        Intent intent = new Intent(StartActivity.this, CheckboxScreen.class);
                        startActivity(intent);
                    }


                }

            }
        });
    }
}
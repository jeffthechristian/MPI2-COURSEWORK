package com.example.alcholator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    TextView bloodResult,soberResult, yesDrive, noDrive;
    ImageView pirmais, otrais, ceturtais, piektais, sestais, pedejais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        bloodResult = findViewById(R.id.bloodResult);
        soberResult = findViewById(R.id.soberResult);
        yesDrive = findViewById(R.id.textView16);
        noDrive = findViewById(R.id.textView17);

        pirmais = findViewById(R.id.pirmais);
        otrais = findViewById(R.id.otrais);
        ceturtais = findViewById(R.id.ceturtais);
        piektais = findViewById(R.id.piektais);
        sestais = findViewById(R.id.sestais);
        pedejais = findViewById(R.id.pedejais);

        String sprom = getIntent().getStringExtra("keyprom");
        String ssober = getIntent().getStringExtra("keysober");
        String sgender = getIntent().getStringExtra("keygender");
        String sweight = getIntent().getStringExtra("keyweight");

        double res = Double.parseDouble(sprom);
        double res2 = Double.parseDouble(ssober);


        bloodResult.setText(sprom.substring(0,5) + " ‰");
        soberResult.setText(ssober.substring(0,ssober.indexOf(".")) + " h");

        canYouDrive();
        youProbablyLook();

        Button btnBack2 = findViewById(R.id.btnBack2);
        btnBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent doo = new Intent(ResultActivity.this, AlcoholCalculator.class);
                doo.putExtra("keygender", sgender);
                doo.putExtra("keyweight", sweight);
                startActivity(doo);

            }
        } );
    }

    void canYouDrive() {
        double promiles = Double.parseDouble(getIntent().getStringExtra("keyprom"));
        if (promiles < 0.5) {
            yesDrive.setVisibility(View.VISIBLE);
        }
        if (promiles > 0.5) {
            noDrive.setVisibility(View.VISIBLE);
        }
    }

    void youProbablyLook() {
        double promiles = Double.parseDouble(getIntent().getStringExtra("keyprom"));

        if (promiles < 1) {
            pirmais.setVisibility(View.VISIBLE);
            otrais.setVisibility(View.INVISIBLE);
            ceturtais.setVisibility(View.INVISIBLE);
            piektais.setVisibility(View.INVISIBLE);
            sestais.setVisibility(View.INVISIBLE);
            pedejais.setVisibility(View.INVISIBLE);
        }
        if (promiles < 2 && promiles >=1) {
            pirmais.setVisibility(View.INVISIBLE);
            otrais.setVisibility(View.VISIBLE);
            ceturtais.setVisibility(View.INVISIBLE);
            piektais.setVisibility(View.INVISIBLE);
            sestais.setVisibility(View.INVISIBLE);
            pedejais.setVisibility(View.INVISIBLE);
        }
        if (promiles < 3 && promiles >= 2) {
            pirmais.setVisibility(View.INVISIBLE);
            otrais.setVisibility(View.INVISIBLE);
            ceturtais.setVisibility(View.VISIBLE);
            piektais.setVisibility(View.INVISIBLE);
            sestais.setVisibility(View.INVISIBLE);
            pedejais.setVisibility(View.INVISIBLE);
        }
        if (promiles < 4 && promiles >=3) {
            pirmais.setVisibility(View.INVISIBLE);
            otrais.setVisibility(View.INVISIBLE);
            ceturtais.setVisibility(View.INVISIBLE);
            piektais.setVisibility(View.VISIBLE);
            sestais.setVisibility(View.INVISIBLE);
            pedejais.setVisibility(View.INVISIBLE);
        }
        if (promiles < 5 && promiles >= 4) {
            pirmais.setVisibility(View.INVISIBLE);
            otrais.setVisibility(View.INVISIBLE);
            ceturtais.setVisibility(View.INVISIBLE);
            piektais.setVisibility(View.INVISIBLE);
            sestais.setVisibility(View.VISIBLE);
            pedejais.setVisibility(View.INVISIBLE);
        }
        if (promiles > 5) {
            pirmais.setVisibility(View.INVISIBLE);
            otrais.setVisibility(View.INVISIBLE);
            ceturtais.setVisibility(View.INVISIBLE);
            piektais.setVisibility(View.INVISIBLE);
            sestais.setVisibility(View.INVISIBLE);
            pedejais.setVisibility(View.VISIBLE);
        }
    }
}
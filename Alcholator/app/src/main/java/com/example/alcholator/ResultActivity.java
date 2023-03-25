package com.example.alcholator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ResultActivity extends AppCompatActivity {
    String uid = SigninActivity.uid;
    TextView bloodResult, soberResult, yesDrive, noDrive;
    ImageView pirmais, otrais, ceturtais, piektais, sestais, pedejais;
    ImageButton edit_profile, show_history, calculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference historyRef = database.getReference("history2");


        edit_profile = findViewById(R.id.edit_profile);
        show_history = findViewById(R.id.show_history);
        calculate = findViewById(R.id.calculate);

        // Get current date
        Date currentDate = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateString = dateFormat.format(currentDate);

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

        double res = Double.parseDouble(sprom);
        double res2 = Double.parseDouble(ssober);

        bloodResult.setText(sprom.substring(0,5) + " ‰");
        soberResult.setText(ssober.substring(0,ssober.indexOf(".")) + " h");

        canYouDrive();
        youProbablyLook();

        // Create a new history entry with the date and blood result
        String bloodResultText = bloodResult.getText().toString();
        HistoryEntry historyEntry = new HistoryEntry(uid, dateString, bloodResultText);

        // Save the history entry to the database
        historyRef.child(uid).push().setValue(historyEntry);

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
        double bloodAlcoholLevel = Double.parseDouble(getIntent().getStringExtra("keyprom"));
        View[] views = {pirmais, otrais, ceturtais, piektais, sestais, pedejais};

        for (int i = 0; i < views.length; i++) {
            views[i].setVisibility(View.INVISIBLE);
        }

        switch ((int) Math.floor(bloodAlcoholLevel)) {
            case 0:
                views[0].setVisibility(View.VISIBLE);
                break;
            case 1:
                views[1].setVisibility(View.VISIBLE);
                break;
            case 2:
                views[2].setVisibility(View.VISIBLE);
                break;
            case 3:
                views[3].setVisibility(View.VISIBLE);
                break;
            case 4:
                views[4].setVisibility(View.VISIBLE);
                break;
            default:
                views[5].setVisibility(View.VISIBLE);
                break;
        }

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResultActivity.this, DataInput.class));
            }
        } );
        show_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResultActivity.this, HistoryActivity.class));
            }
        } );
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResultActivity.this, AlcoholCalculator.class));
            }
        } );
    }
}
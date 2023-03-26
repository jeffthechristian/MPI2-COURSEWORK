package com.example.alcholator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class ResultActivity extends AppCompatActivity {
    String uid = SigninActivity.uid;
    TextView bloodResult, soberResult, yesDrive, noDrive;
    ImageView pirmais, otrais, ceturtais, piektais, sestais, pedejais;
    ImageButton edit_profile, show_history, calculate;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://official-joke-api.appspot.com/jokes/random")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        String name = jsonObject.getString("setup");
                        String email = jsonObject.getString("punchline");
                        runOnUiThread(() -> {
                            TextView nameTextView = findViewById(R.id.jok1);
                            TextView emailTextView = findViewById(R.id.jok2);
                            nameTextView.setText(name);
                            emailTextView.setText(email);
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference historyRef = database.getReference("history2");


        edit_profile = findViewById(R.id.edit_profile);
        show_history = findViewById(R.id.show_history);
        calculate = findViewById(R.id.calculate);

        // Get current date
        Date currentDate = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
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

        bloodResult.setText(sprom.substring(0, 5) + " ‰");
        soberResult.setText(ssober.substring(0, ssober.indexOf(".")) + " h");

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

        for (View value : views) {
            value.setVisibility(View.INVISIBLE);
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

        edit_profile.setOnClickListener(view -> startActivity(new Intent(ResultActivity.this, ProfileActivity.class)));
        show_history.setOnClickListener(view -> startActivity(new Intent(ResultActivity.this, HistoryActivity.class)));
        calculate.setOnClickListener(view -> startActivity(new Intent(ResultActivity.this, AlcoholCalculator.class)));
    }
}
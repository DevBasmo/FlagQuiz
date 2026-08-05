package com.example.flagquizgame;
import  android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class ContinentSelectActivity extends AppCompatActivity {

    public static final String EXTRA_SELECTED_REGION = "SELECTED_REGION";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.continents_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.continentMainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;
        });


        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());


        findViewById(R.id.cardAfrica).setOnClickListener(v -> launchQuiz("africa"));

        findViewById(R.id.cardAsia).setOnClickListener(v -> launchQuiz("asia"));

        findViewById(R.id.cardEurope).setOnClickListener(v -> launchQuiz("europe"));

        findViewById(R.id.cardNorthAmerica).setOnClickListener(v -> launchQuiz("north_america"));

        findViewById(R.id.cardSouthAmerica).setOnClickListener(v -> launchQuiz("south_america"));

        findViewById(R.id.cardOceania).setOnClickListener(v -> launchQuiz("oceania"));

    }

    private void launchQuiz (String regionKey)
    {
        Intent intent = new Intent(ContinentSelectActivity.this,
                PlayButtonClass.class);

        intent.putExtra(EXTRA_SELECTED_REGION, regionKey);
        startActivity(intent);

    }




}

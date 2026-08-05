package com.example.flagquizgame;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
public class homeActivity extends AppCompatActivity{

    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.homemainlayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;

        });


        Button btnStartQuiz = findViewById(R.id.btnStartQuiz);



        btnStartQuiz.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeActivity.this, PlayButtonClass.class);

                startActivity(intent);

            }
        });

        View customQuizCard = findViewById(R.id.cardCustomQuiz);
        customQuizCard.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent  intent = new Intent(homeActivity.this,ContinentSelectActivity.class);
                    startActivity(intent);

                }
            });

        View cardExplore = findViewById(R.id.cardExplore);
        cardExplore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(homeActivity.this, ExploreActivity.class);
                startActivity(intent);

            }
        });

        View cardStatistics = findViewById(R.id.cardStatistics);
        cardStatistics.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public  void onClick(View v)
                    {
                        Intent intent = new Intent(homeActivity.this, StatisticsActivity.class);
                        startActivity(intent);
                    }
                }



        );

        View cardSettings = findViewById(R.id.cardSettings);
        cardSettings.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(homeActivity.this, SettingsActivity.class);
                        startActivity(intent);
                    }
                }
        );

    }
}

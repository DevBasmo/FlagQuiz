package com.example.flagquizgame;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ExploreActivity extends AppCompatActivity{

    public static final String EXTRA_SELECTED_REGION ="com.example.flagquizgame.region";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.continentMainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;
        });

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        setupContinentCard(R.id.cardAfrica, "africa");
        setupContinentCard(R.id.cardAsia, "asia");
        setupContinentCard(R.id.cardEurope, "europe");
        setupContinentCard(R.id.cardNorthAmerica, "north_america");
        setupContinentCard(R.id.cardSouthAmerica, "south_america");
        setupContinentCard(R.id.cardOceania, "oceania");



    }

    private void setupContinentCard(int viewId, String regionKey)
    {
        View card = findViewById(viewId);
        card.setOnClickListener(v ->{
            Intent intent = new Intent(ExploreActivity.this,FlagListActivity.class);
            intent.putExtra(EXTRA_SELECTED_REGION, regionKey);
            startActivity(intent);
        });
    }
}

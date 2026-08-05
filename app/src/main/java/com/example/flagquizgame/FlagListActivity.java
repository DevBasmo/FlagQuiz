package com.example.flagquizgame;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import  android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class FlagListActivity extends AppCompatActivity{

    private static final String TAG = "FlagListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flag_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.flagListMainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;

        });


        String region = getIntent().getStringExtra(ExploreActivity.EXTRA_SELECTED_REGION);

        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(getRegionTitle(region));

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());


        List<String> flagFileNames = loadFlagFileNames(region);
        Collections.sort(flagFileNames, (a, b) ->
                getCountryName(a).compareToIgnoreCase(getCountryName(b)));



        RecyclerView recyclerView = findViewById(R.id.flagRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new FlagAdapter(region, flagFileNames));


}
    private List<String> loadFlagFileNames(String region) {
        List<String> fileNames = new ArrayList<>();
        if (region == null) return fileNames;

        AssetManager assets = getAssets();
        try {
            String[] paths = assets.list(region);
            if (paths != null) {
                for (String path : paths) {
                    fileNames.add(path.replace(".png", ""));
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error loading image file names for " + region, e);
        }
        return fileNames;
    }

private String getCountryName(String fileName)
{
    return fileName.substring(fileName.indexOf('-') + 1).replace('_', ' ');
}
    private String getRegionTitle(String region) {
        if (region == null) return "World Countries";
        switch (region) {
            case "africa":
                return "Africa";
            case "asia":
                return "Asia";
            case "europe":
                return "Europe";
            case "north_america":
                return "North America";
            case "south_america":
                return "South America";
            case "oceania":
                return "Oceania";
            default:
                return "World Countries";
        }
    }}

package com.example.flagquizgame;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;

public class StatisticsActivity extends AppCompatActivity {

    private static final String[] REGIONS = {
            "africa", "asia", "europe", "north_america", "south_america", "oceania"
    };

    private CircularProgressIndicator overallAccuracyRing;
    private TextView overallAccuracyText;
    private TextView totalQuizzesValue;
    private TextView bestStreakValue;
    private TextView avgScoreValue;
    private LinearLayout continentBreakdownContainer;
    private RecyclerView recentActivityRecyclerView;
    private LinearLayout emptyStateContainer;
    private LinearLayout statsContentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.statisticsMainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;

        });


        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        overallAccuracyRing = findViewById(R.id.overallAccuracyRing);
        overallAccuracyText = findViewById(R.id.overallAccuracyText);
        totalQuizzesValue = findViewById(R.id.totalQuizzesValue);
        bestStreakValue = findViewById(R.id.bestStreakValue);
        avgScoreValue = findViewById(R.id.avgScoreValue);
        continentBreakdownContainer = findViewById(R.id.continentBreakdownContainer);
        recentActivityRecyclerView = findViewById(R.id.recentActivityRecyclerView);
        emptyStateContainer = findViewById(R.id.emptyStateContainer);
        statsContentContainer = findViewById(R.id.statsContentContainer);

        recentActivityRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadStatistics();
    }

    private void loadStatistics() {
        Executors.newSingleThreadExecutor().execute(() -> {
            QuizResultDao dao = AppDatabase.getInstance(getApplicationContext()).quizResultDao();
            List<QuizResult> allResults = dao.getAllResults();

            runOnUiThread(() -> renderStatistics(allResults));
        });
    }

    private void renderStatistics(List<QuizResult> allResults) {
        if (allResults.isEmpty()) {
            emptyStateContainer.setVisibility(android.view.View.VISIBLE);
            statsContentContainer.setVisibility(android.view.View.GONE);
            return;
        }

        emptyStateContainer.setVisibility(android.view.View.GONE);
        statsContentContainer.setVisibility(android.view.View.VISIBLE);

        // Overall accuracy
        int totalCorrect = 0;
        int totalQuestions = 0;
        for (QuizResult r : allResults) {
            totalCorrect += r.correctAnswers;
            totalQuestions += r.totalQuestions;
        }
        int overallAccuracy = totalQuestions == 0 ? 0 : Math.round((100f * totalCorrect) / totalQuestions);

        overallAccuracyRing.setProgress(overallAccuracy);
        overallAccuracyText.setText(overallAccuracy + "%");

        totalQuizzesValue.setText(String.valueOf(allResults.size()));

        // Current streak: consecutive most-recent quizzes scoring 70%+
        int streak = 0;
        for (QuizResult r : allResults) {
            double resultScore = (100.0 * r.correctAnswers) / r.totalQuestions;
            if (resultScore >= 70.0) {
                streak++;
            } else {
                break;
            }
        }
        bestStreakValue.setText(String.valueOf(streak));

        // Average score across all attempts
        double avgScore = 0;
        for (QuizResult r : allResults) {
            avgScore += (100.0 * r.correctAnswers) / r.totalQuestions;
        }

        avgScore /= allResults.size();
        avgScoreValue.setText(Math.round(avgScore) + "%");

        // Per-continent breakdown
        renderContinentBreakdown(allResults);

        // Recent activity (last 10)
        List<QuizResult> recent = allResults.subList(0, Math.min(10, allResults.size()));
        recentActivityRecyclerView.setAdapter(new RecentQuizAdapter(recent));
    }

    private void renderContinentBreakdown(List<QuizResult> allResults) {
        continentBreakdownContainer.removeAllViews();

        Map<String, int[]> regionTotals = new HashMap<>(); // region -> [correct, total]
        for (QuizResult r : allResults) {
            if (!regionTotals.containsKey(r.region)) {
                regionTotals.put(r.region, new int[]{0, 0});
            }
            int[] totals = regionTotals.get(r.region);
            totals[0] += r.correctAnswers;
            totals[1] += r.totalQuestions;
        }

        for (String region : REGIONS) {
            int[] totals = regionTotals.get(region);
            int accuracy = (totals == null || totals[1] == 0)
                    ? -1
                    : Math.round((100f * totals[0]) / totals[1]);

            addContinentRow(getRegionTitle(region), accuracy);
        }
    }

    private void addContinentRow(String label, int accuracy) {
        android.view.View row = getLayoutInflater().inflate(
                R.layout.item_continent_stat, continentBreakdownContainer, false);

        TextView nameText = row.findViewById(R.id.continentStatName);
        TextView percentText = row.findViewById(R.id.continentStatPercent);
        LinearProgressIndicator progressBar = row.findViewById(R.id.continentStatProgress);

        nameText.setText(label);

        if (accuracy < 0) {
            percentText.setText("—");
            progressBar.setProgress(0);
        } else {
            percentText.setText(accuracy + "%");
            progressBar.setProgress(accuracy);
        }

        continentBreakdownContainer.addView(row);
    }

    private String getRegionTitle(String region) {
        switch (region) {
            case "africa": return "Africa";
            case "asia": return "Asia";
            case "europe": return "Europe";
            case "north_america": return "North America";
            case "south_america": return "South America";
            case "oceania": return "Oceania";
            default: return region;
        }
    }
}
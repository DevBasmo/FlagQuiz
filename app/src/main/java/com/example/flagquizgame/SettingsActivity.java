package com.example.flagquizgame;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.button.MaterialButton;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.slider.Slider;

import java.util.concurrent.Executors;

public class SettingsActivity extends AppCompatActivity {

    private SettingsManager settingsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settingsMainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;

        });


        settingsManager = new SettingsManager(this);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        setupSoundToggle();
        setupQuestionCountToggle();
        setupDarkModeToggle();
        setupTimerToggle();
        setupResetStats();
    }

    private void setupSoundToggle() {
        MaterialSwitch soundSwitch = findViewById(R.id.soundSwitch);
        soundSwitch.setChecked(settingsManager.isSoundEnabled());
        soundSwitch.setOnCheckedChangeListener((btn, isChecked) ->
                settingsManager.setSoundEnabled(isChecked));
    }

    private void setupQuestionCountToggle() {
        MaterialButtonToggleGroup toggleGroup = findViewById(R.id.questionCountToggle);
        MaterialButton button5 = findViewById(R.id.questionCount5Button);
        MaterialButton button10 = findViewById(R.id.questionCount10Button);

        int current = settingsManager.getQuestionCount();
        toggleGroup.check(current == 5 ? R.id.questionCount5Button : R.id.questionCount10Button);

        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;

            int count = (checkedId == R.id.questionCount5Button) ? 5 : 10;
            settingsManager.setQuestionCount(count);
        });
    }

    private void setupDarkModeToggle() {
        MaterialSwitch darkModeSwitch = findViewById(R.id.darkModeSwitch);
        darkModeSwitch.setChecked(settingsManager.isDarkModeEnabled());
        darkModeSwitch.setOnCheckedChangeListener((btn, isChecked) -> {
            settingsManager.setDarkModeEnabled(isChecked);
            AppCompatDelegate.setDefaultNightMode(isChecked
                    ? AppCompatDelegate.MODE_NIGHT_YES
                    : AppCompatDelegate.MODE_NIGHT_NO);

            Intent intent = getIntent();
            finish();
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }


    private void setupTimerToggle() {
        MaterialSwitch timerSwitch = findViewById(R.id.timerSwitch);
        timerSwitch.setChecked(settingsManager.isTimerEnabled());
        timerSwitch.setOnCheckedChangeListener((btn, isChecked) ->
                settingsManager.setTimerEnabled(isChecked));
    }

    private void setupResetStats() {
        findViewById(R.id.resetStatsButton).setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Reset Statistics")
                    .setMessage("This will permanently delete your quiz history and stats. This can't be undone.")
                    .setPositiveButton("Reset", (dialog, which) -> {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            AppDatabase.getInstance(getApplicationContext())
                                    .quizResultDao().deleteAll();
                        });
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

}
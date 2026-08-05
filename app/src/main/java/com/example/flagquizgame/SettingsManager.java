package com.example.flagquizgame;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {

    private static final String PREFS_NAME = "flag_quiz_settings";

    private static final String KEY_SOUND_ENABLED = "sound_enabled";
    private static final String KEY_QUESTION_COUNT = "question_count";
    private static final String KEY_CHOICE_COUNT = "choice_count";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_TIMER_ENABLED = "timer_enabled";
    private static final String KEY_TIMER_SECONDS = "timer_seconds";

    private final SharedPreferences prefs;

    public SettingsManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean isSoundEnabled() {
        return prefs.getBoolean(KEY_SOUND_ENABLED, true);
    }

    public void setSoundEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply();
    }

    public int getQuestionCount() {
        int count = prefs.getInt(KEY_QUESTION_COUNT, 10);
        return (count == 5) ? 5 : 10; // clamp anything else (old slider values) back to 10
    }
    public void setQuestionCount(int count) {
        prefs.edit().putInt(KEY_QUESTION_COUNT, count).apply();
    }

    public boolean isDarkModeEnabled() {
        return prefs.getBoolean(KEY_DARK_MODE, false);
    }

    public void setDarkModeEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply();
    }

    public boolean isTimerEnabled() {
        return prefs.getBoolean(KEY_TIMER_ENABLED, false);
    }

    public void setTimerEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_TIMER_ENABLED, enabled).apply();
    }

    public int getTimerSeconds() {
        return prefs.getInt(KEY_TIMER_SECONDS, 10);
    }

    public void setTimerSeconds(int seconds) {
        prefs.edit().putInt(KEY_TIMER_SECONDS, seconds).apply();
    }
}
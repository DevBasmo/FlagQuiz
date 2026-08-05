package com.example.flagquizgame;

import android.app.Application;
import androidx.appcompat.app.AppCompatDelegate;

public class FlagQuizApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SettingsManager settingsManager = new SettingsManager(this);
        AppCompatDelegate.setDefaultNightMode(settingsManager.isDarkModeEnabled()
                ? AppCompatDelegate.MODE_NIGHT_YES
                : AppCompatDelegate.MODE_NIGHT_NO);
    }
}

package com.example.flagquizgame;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {QuizResult.class}, version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase
    {
        public abstract QuizResultDao quizResultDao();
        private static volatile AppDatabase INSTANCE;

        public static AppDatabase getInstance (Context context)
        {
            if (INSTANCE == null) {
                synchronized (AppDatabase.class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                                context.getApplicationContext(),
                                AppDatabase.class,
                                "flag_quiz_db"
                        ).build();
                    }
                }
            }
            return INSTANCE;

        }


    }

package com.example.flagquizgame;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuizResultDao {

    @Insert
    void insert(QuizResult result);

    @Query("SELECT * FROM quiz_results ORDER BY timestamp DESC")
    List<QuizResult> getAllResults();

    @Query("SELECT * FROM quiz_results ORDER BY timestamp DESC LIMIT :limit")
    List<QuizResult> getRecentResults(int limit);

    @Query("SELECT * FROM quiz_results WHERE region = :region ORDER BY timestamp DESC")
    List<QuizResult> getResultForRegion(String region);

    @Query("DELETE FROM quiz_results")
    void deleteAll();
}

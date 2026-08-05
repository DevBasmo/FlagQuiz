package com.example.flagquizgame;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quiz_results")

public class QuizResult {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String region;
    public int correctAnswers;
    public int totalQuestions;
    public int totalGuesses;
    public long timestamp;


    public QuizResult(String region, int correctAnswers,int totalQuestions, int totalGuesses, long timestamp)
    {
        this.region = region;
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
        this.totalGuesses = totalGuesses;
        this.timestamp = timestamp;
    }
}

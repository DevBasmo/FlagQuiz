package com.example.flagquizgame;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class RecentQuizAdapter extends RecyclerView.Adapter<RecentQuizAdapter.ViewHolder> {

    private final List<QuizResult> results;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, h:mm a", Locale.getDefault());

    public RecentQuizAdapter(List<QuizResult> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recent_quiz, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuizResult r = results.get(position);


        android.util.Log.d("QUIZ_REGION", "Region = " + r.region);



        double score = (100.0 * r.correctAnswers) / r.totalQuestions;

        holder.regionText.setText(getRegionTitle(r.region));
        holder.dateText.setText(dateFormat.format(new java.util.Date(r.timestamp)));
        holder.scoreText.setText(r.correctAnswers + "/" + r.totalQuestions);

        int color;
        if (score >= 80) color = 0xFF10B981;      // green
        else if (score >= 50) color = 0xFFF59E0B; // amber
        else color = 0xFFEF4444;                  // red

        holder.scoreBadge.setBackgroundTintList(android.content.res.ColorStateList.valueOf(color));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    private String getRegionTitle(String region) {
        switch (region) {
            case "africa": return "Africa";
            case "asia": return "Asia";
            case "europe": return "Europe";
            case "north_america": return "North America";
            case "south_america": return "South America";
            case "oceania": return "Oceania";
            default: return "World Countries";
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView regionText, dateText, scoreText;
        View scoreBadge;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            regionText = itemView.findViewById(R.id.recentQuizRegion);
            dateText = itemView.findViewById(R.id.recentQuizDate);
            scoreText = itemView.findViewById(R.id.recentQuizScore);
            scoreBadge = itemView.findViewById(R.id.recentQuizScoreBadge);
        }
    }
}
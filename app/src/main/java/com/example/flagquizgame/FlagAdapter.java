package com.example.flagquizgame;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FlagAdapter extends RecyclerView.Adapter<FlagAdapter.FlagViewHolder> {

    private static final String TAG = "FlagAdapter";

    private final String region;
    private final List<String> fileNames;

    public FlagAdapter(String region, List<String> fileNames) {
        this.region = region;
        this.fileNames = fileNames;
    }

    @NonNull
    @Override
    public FlagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flag, parent, false);
        return new FlagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlagViewHolder holder, int position) {
        String fileName = fileNames.get(position);
        String countryName = fileName.substring(fileName.indexOf('-') + 1).replace('_', ' ');
        holder.countryName.setText(countryName);

        AssetManager assets = holder.itemView.getContext().getAssets();
        try (InputStream stream = assets.open(region + "/" + fileName + ".png")) {
            Drawable flag = Drawable.createFromStream(stream, fileName);
            holder.flagImage.setImageDrawable(flag);
        } catch (IOException e) {
            Log.e(TAG, "Error loading " + fileName, e);
        }
    }

    @Override
    public int getItemCount() {
        return fileNames.size();
    }

    static class FlagViewHolder extends RecyclerView.ViewHolder {
        ImageView flagImage;
        TextView countryName;

        FlagViewHolder(@NonNull View itemView) {
            super(itemView);
            flagImage = itemView.findViewById(R.id.flagImage);
            countryName = itemView.findViewById(R.id.countryNameText);
        }
    }
}
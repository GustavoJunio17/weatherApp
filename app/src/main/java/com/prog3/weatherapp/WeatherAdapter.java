package com.prog3.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private final Context context;
    private final List<Weather> weatherList;

    public WeatherAdapter(Context context, List<Weather> weatherList) {
        this.context = context;
        this.weatherList = weatherList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Weather weather = weatherList.get(position);
        holder.dayTextView.setText(weather.date);
        holder.lowTextView.setText(String.format("Low: %s", weather.minTemp));
        holder.hiTextView.setText(String.format("High: %s", weather.maxTemp));
        holder.humidityTextView.setText(String.format("Humidity: %s", weather.humidity));
        holder.conditionIconTextView.setText(weather.icon);
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayTextView;
        TextView lowTextView;
        TextView hiTextView;
        TextView humidityTextView;
        TextView conditionIconTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            lowTextView = itemView.findViewById(R.id.lowTextView);
            hiTextView = itemView.findViewById(R.id.hiTextView);
            humidityTextView = itemView.findViewById(R.id.humidityTextView);
            conditionIconTextView = itemView.findViewById(R.id.conditionIconTextView);
        }
    }
}

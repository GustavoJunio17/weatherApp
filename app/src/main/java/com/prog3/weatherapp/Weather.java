package com.prog3.weatherapp;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Weather {

    public final String date;
    public final String minTemp;
    public final String maxTemp;
    public final String humidity;
    public final String description;
    public final String icon;

    public Weather(String dateString, double minTempC, double maxTempC,
                   double humidity, String description, String iconEmoji) {

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);
        this.minTemp = numberFormat.format(minTempC) + "°C";
        this.maxTemp = numberFormat.format(maxTempC) + "°C";

        this.humidity = NumberFormat.getPercentInstance().format(humidity);

        this.description = description;
        this.icon = iconEmoji;

        this.date = formatDate(dateString);
    }

    private String formatDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = inputFormat.parse(dateString);

            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, dd/MM", new Locale("pt", "BR"));
            return outputFormat.format(date);
        } catch (Exception e) {
            return dateString;
        }
    }
}
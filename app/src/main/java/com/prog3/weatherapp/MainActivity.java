package com.prog3.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Weather> weatherList = new ArrayList<>();
    private WeatherArrayAdapter weatherArrayAdapter;
    private ListView weatherListView;
    private EditText locationEditText;
    private ProgressBar loadingIndicator;

    private SharedPreferences sharedPreferences;
    private static final String LAST_CITY_PREF = "last_city";

    private final Handler refreshHandler = new Handler();
    private Runnable refreshRunnable;
    private static final long REFRESH_INTERVAL = 5 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        locationEditText = findViewById(R.id.locationEditText);
        weatherListView = findViewById(R.id.weatherListView);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        weatherArrayAdapter = new WeatherArrayAdapter(this, weatherList);
        weatherListView.setAdapter(weatherArrayAdapter);

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        String lastCity = sharedPreferences.getString(LAST_CITY_PREF, null);
        if (lastCity != null && !lastCity.isEmpty()) {
            locationEditText.setText(lastCity);
            getWeatherForCity(lastCity);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = locationEditText.getText().toString();
                getWeatherForCity(city);
            }
        });

        setupAutoRefresh();
    }

    private void getWeatherForCity(String city) {
        URL url = createURL(city);
        if (url != null) {
            dismissKeyboard(locationEditText);
            GetWeatherTask getLocalWeatherTask = new GetWeatherTask();
            getLocalWeatherTask.execute(url);
            sharedPreferences.edit().putString(LAST_CITY_PREF, city).apply();
        } else {
            Snackbar.make(findViewById(R.id.coordinatorLayout),
                    R.string.invalid_url, Snackbar.LENGTH_LONG).show();
        }
    }

    private void setupAutoRefresh() {
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                String currentCity = sharedPreferences.getString(LAST_CITY_PREF, null);
                if (currentCity != null && !currentCity.isEmpty()) {
                    getWeatherForCity(currentCity);
                }
                refreshHandler.postDelayed(this, REFRESH_INTERVAL);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshHandler.postDelayed(refreshRunnable, REFRESH_INTERVAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        refreshHandler.removeCallbacks(refreshRunnable);
    }

    private void dismissKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private URL createURL(String city) {
        String apiKey = getString(R.string.api_key);
        String baseUrl = getString(R.string.web_service_url);

        try {
            String urlString = baseUrl + URLEncoder.encode(city, "UTF-8") +
                    "&days=7&APPID=" + apiKey;
            return new URL(urlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem toggleThemeItem = menu.findItem(R.id.action_toggle_theme);
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            toggleThemeItem.setIcon(R.drawable.ic_sun);
        } else {
            toggleThemeItem.setIcon(R.drawable.ic_moon);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_toggle_theme) {
            int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            recreate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetWeatherTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weatherList.clear();
            weatherArrayAdapter.notifyDataSetChanged();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(URL... params) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) params[0].openConnection();
                int response = connection.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    StringBuilder builder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    }
                    return new JSONObject(builder.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject weather) {
            loadingIndicator.setVisibility(View.GONE);
            if (weather != null) {
                convertJSONtoArrayList(weather);
                weatherArrayAdapter.notifyDataSetChanged();
                weatherListView.smoothScrollToPosition(0);
            } else {
                Snackbar.make(findViewById(R.id.coordinatorLayout),
                        R.string.connect_error, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void convertJSONtoArrayList(JSONObject forecast) {
        try {
            JSONArray list = forecast.getJSONArray("days");

            for (int i = 0; i < list.length(); i++) {
                JSONObject day = list.getJSONObject(i);

                weatherList.add(new Weather(
                        day.getString("date"),
                        day.getDouble("minTempC"),
                        day.getDouble("maxTempC"),
                        day.getDouble("humidity"),
                        day.getString("description"),
                        day.getString("icon")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

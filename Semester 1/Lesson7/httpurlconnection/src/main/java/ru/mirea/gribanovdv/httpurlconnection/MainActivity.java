package ru.mirea.gribanovdv.httpurlconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    private TextView textIpAddress;
    private TextView textRegion;
    private TextView textCity;
    private TextView textLocation;
    private TextView textCountry;
    private TextView textWeather;
    private TextView textWindSpeed;
    private TextView textWindDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textIpAddress = findViewById(R.id.textIpAdress);
        textRegion = findViewById(R.id.textRegion);
        textCity = findViewById(R.id.textCity);
        textLocation = findViewById(R.id.textLocation);
        textCountry = findViewById(R.id.textCountry);
        textWeather = findViewById(R.id.textWeather);
        textWindSpeed = findViewById(R.id.textWindSpeed);
        textWindDirection = findViewById(R.id.textWindDirection);


    }

    public void onClick(View view) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = null;
        if (connectivityManager != null) {
            networkinfo = connectivityManager.getActiveNetworkInfo();
        }
        if (networkinfo != null && networkinfo.isConnected()) {
            new DownloadPageTask().execute("https://ipinfo.io/json");
        } else {
            Toast.makeText(getApplicationContext(), "Нет интернета", Toast.LENGTH_SHORT).show();
        }

    }

    private class DownloadPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textIpAddress.setText("Loading...");
            textCity.setText("Loading...");
            textRegion.setText("Loading...");
            textLocation.setText("Loading...");
            textWeather.setText("Loading...");
            textCountry.setText("Loading...");
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadIpInfo(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(MainActivity.class.getSimpleName(), result);
            try {
                JSONObject responseJson = new JSONObject(result);
                Log.d(MainActivity.class.getSimpleName(), "Response: " + responseJson);

                String ipAddress = responseJson.getString("ip");
                textIpAddress.setText("Ip address: " + ipAddress);

                String country = responseJson.getString("country");
                textCountry.setText("Country: " + country);

                String region = responseJson.getString("region");
                textRegion.setText("Region: " + region);

                String city = responseJson.getString("city");
                textCity.setText("City: " + city);

                textWeather.setText("Weather: ");

                String location = responseJson.getString("loc");
                textLocation.setText("Location: " + location);

                new DownloadWeatherData().execute("https://api.open-meteo.com/v1/forecast?latitude="+location.substring(0,location.indexOf(","))+"&longitude="+location.substring(location.indexOf(",")+1)+"&current_weather=true");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }

    }

    public String downloadIpInfo(String address) throws IOException {
        InputStream inputStream = null;
        String data = "";
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(100000);
            connection.setConnectTimeout(100000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                inputStream = connection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int read = 0;
                while ((read = inputStream.read()) != -1) {
                    bos.write(read);
                }
                bos.close();
                data = bos.toString();
            } else {
                data = connection.getResponseMessage() + ". Error Code: " + responseCode;
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return data;
    }

    private class DownloadWeatherData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadIpInfo(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(MainActivity.class.getSimpleName(), result);
            try {
                JSONObject responseJson = new JSONObject(result);

                Log.d(MainActivity.class.getSimpleName(), "Response: " + responseJson);
                JSONObject weatherJson = responseJson.getJSONObject("current_weather");

                String windSpeed = weatherJson.getString("windspeed");
                textWindSpeed.setText("WinSpeed:" + windSpeed);

                String windDirection = weatherJson.getString("winddirection");
                textWindDirection.setText("WindDirection:" + windDirection);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }
}
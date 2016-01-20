package com.example.manuelseguranavarro.festival;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by manuelseguranavarro on 17/1/16.
 */
public class SincronizaAdaptador extends AbstractThreadedSyncAdapter{

    public SincronizaAdaptador(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;


    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        String locationQuery = Util.getPreferredLocation(getContext());

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JsonString = null;
        String format = "json";
        String artist = "";
        String city ="valencia" ;
        int numDays = 14;

        try {
            final String FORECAST_BASE_URL = "http://www.nvivo.es/api/request.php?api_key=";
            final String QUERY_PARAM = "method";
            final String FORMAT_PARAM = "format";
            final String ARTIST_PARAM = "artist.getEvents";
            final String CITY_PARAM = "city.getEvents";
            final String APPID_PARAM = "APPID";

            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, locationQuery)
                    .appendQueryParameter(FORMAT_PARAM, format)
                    .appendQueryParameter(ARTIST_PARAM, artist)
                    .appendQueryParameter(CITY_PARAM, city)
                    .appendQueryParameter(APPID_PARAM, BuildConfig.API_KEY_CONCIERTOS)
                    .build();

            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            JsonString = buffer.toString();
            getDatosJson(JsonString, locationQuery);

        }catch (IOException ex){
            ex.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void getDatosJson(String conciertoJsonStr,String locationSetting)
            throws JSONException {



        final String ID = "id";
        // Informacion del concierto o actuacion
        final String ARTISTAS = "artists";
        final String ID_ARTISTAS = "id";
        final String NOMBRE_ARTISTAS = "name";
        final String URL_ARTISTAS = "url";
        final String LOGO_ARTISTAS = "art_logo";
        //Informacion del lugar
        final String LUGAR = "venue";
        final String LUGAR_ID="id";
        final String LUGAR_NOMBRE="name";
        final String LUGAR_LOGO ="venue_logo";
        //Informacion de la ciudad
        final String LOCALIZACION ="location";
        final String LOCALIZACION_CIUDAD="city";
        final String LOCALIZACION_PAIS ="country";
        final String LOCALIZACION_CALLE ="street";
        final String LOCALIZACION_CODPOST = "postalcode";
        final String LOCALIZACION_LATITUD="latitude";
        final String LOCALIZACION_LONGITUD="longitude";
        final String LOCALIZACION_URL="url";




        try {
            JSONObject artistasJson = new JSONObject(conciertoJsonStr);
            JSONArray artistasArray = artistasJson.getJSONArray(ARTISTAS);
            JSONObject ciudadesJson = new JSONObject(conciertoJsonStr);
            JSONArray ciudadesArray = ciudadesJson.optJSONArray(LUGAR);
            JSONObject localizacionJson = new JSONObject(conciertoJsonStr)
            JSONArray localizacionArray = localizacionJson.getJSONArray(LOCALIZACION);

            //JSON Artistas
            String nameArtist= artistasJson.getString(NOMBRE_ARTISTAS);
            String urlArtist=artistasJson.getString(URL_ARTISTAS);
            String logoArtist=artistasJson.getString(LOGO_ARTISTAS);
            //JSON Lugares
            String nameCityLugar = ciudadesJson.getString(LUGAR_NOMBRE);
            String logoLugar = ciudadesJson.getString(LUGAR_LOGO);




            JSONObject cityCoord = cityJson.getJSONObject(LOCALIZACION);
            double cityLatitude = cityCoord.getDouble(LOCALIZACION_LATITUD);
            double cityLongitude = cityCoord.getDouble(LOCALIZACION_LONGITUD);

            long locationId = addLocation(locationSetting, cityName, cityLatitude, cityLongitude);

            // Insert the new weather information into the database
            Vector<ContentValues> cVVector = new Vector<ContentValues>(weatherArray.length());

            // OWM returns daily forecasts based upon the local time of the city that is being
            // asked for, which means that we need to know the GMT offset to translate this data
            // properly.

            // Since this data is also sent in-order and the first day is always the
            // current day, we're going to take advantage of that to get a nice
            // normalized UTC date for all of our weather.

            Time dayTime = new Time();
            dayTime.setToNow();

            // we start at the day returned by local time. Otherwise this is a mess.
            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

            // now we work exclusively in UTC
            dayTime = new Time();

            for(int i = 0; i < weatherArray.length(); i++) {
                // These are the values that will be collected.
                long dateTime;
                double pressure;
                int humidity;
                double windSpeed;
                double windDirection;

                double high;
                double low;

                String description;
                int weatherId;

                // Get the JSON object representing the day
                JSONObject dayForecast = weatherArray.getJSONObject(i);

                // Cheating to convert this to UTC time, which is what we want anyhow
                dateTime = dayTime.setJulianDay(julianStartDay+i);

                pressure = dayForecast.getDouble(OWM_PRESSURE);
                humidity = dayForecast.getInt(OWM_HUMIDITY);
                windSpeed = dayForecast.getDouble(OWM_WINDSPEED);
                windDirection = dayForecast.getDouble(OWM_WIND_DIRECTION);

                // Description is in a child array called "weather", which is 1 element long.
                // That element also contains a weather code.
                JSONObject weatherObject =
                        dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                description = weatherObject.getString(OWM_DESCRIPTION);
                weatherId = weatherObject.getInt(OWM_WEATHER_ID);

                // Temperatures are in a child object called "temp".  Try not to name variables
                // "temp" when working with temperature.  It confuses everybody.
                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                high = temperatureObject.getDouble(OWM_MAX);
                low = temperatureObject.getDouble(OWM_MIN);

                ContentValues weatherValues = new ContentValues();

                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_LOC_KEY, locationId);
                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATE, dateTime);
                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, humidity);
                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, pressure);
                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, windSpeed);
                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, windDirection);
                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, high);
                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, low);
                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC, description);
                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, weatherId);

                cVVector.add(weatherValues);
            }


            int inserted=0;

            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, cvArray);

                // delete old data so we don't build up an endless history
                getContext().getContentResolver().delete(WeatherContract.WeatherEntry.CONTENT_URI,
                        WeatherContract.WeatherEntry.COLUMN_DATE + " <= ?",
                        new String[] {Long.toString(dayTime.setJulianDay(julianStartDay-1))});

                notifyWeather();
            }

            Log.d(LOG_TAG, "Sunshine Service Complete. "
                    + cVVector + " Inserted. ");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }
}

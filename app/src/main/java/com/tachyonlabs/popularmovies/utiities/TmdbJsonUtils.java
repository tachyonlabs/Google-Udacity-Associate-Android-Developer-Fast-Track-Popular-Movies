package com.tachyonlabs.popularmovies.utiities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class TmdbJsonUtils {
    private static final String TAG = TmdbJsonUtils.class.getSimpleName();

    public static String[] getPosterUrlsFromJson(Context context, String moviesJsonStr) throws JSONException {

//        /* Weather information. Each day's forecast info is an element of the "list" array */
//        final String OWM_LIST = "list";
//
//        /* All temperatures are children of the "temp" object */
//        final String OWM_TEMPERATURE = "temp";
//
//        /* Max temperature for the day */
//        final String OWM_MAX = "max";
//        final String OWM_MIN = "min";
//
//        final String OWM_WEATHER = "weather";
//        final String OWM_DESCRIPTION = "main";
//
//        final String OWM_MESSAGE_CODE = "cod";
//
        /* String array to hold each day's weather String */
        String temp;

        JSONObject movieDataJson = new JSONObject(moviesJsonStr);

        JSONArray resultsArray = movieDataJson.getJSONArray("results");

        String[] posterUrls = new String[resultsArray.length()];

        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject result = resultsArray.getJSONObject(i);
            posterUrls[i] = result.getString("poster_path");
        }
//            String date;
//            String highAndLow;
//
//            /* These are the values that will be collected */
//            long dateTimeMillis;
//            double high;
//            double low;
//            String description;
//
//            /* Get the JSON object representing the day */
//            JSONObject dayForecast = weatherArray.getJSONObject(i);
//
//            /*
//             * We ignore all the datetime values embedded in the JSON and assume that
//             * the values are returned in-order by day (which is not guaranteed to be correct).
//             */
//            dateTimeMillis = startDay + SunshineDateUtils.DAY_IN_MILLIS * i;
//            date = SunshineDateUtils.getFriendlyDateString(context, dateTimeMillis, false);
//
//            /*
//             * Description is in a child array called "weather", which is 1 element long.
//             * That element also contains a weather code.
//             */
//            JSONObject weatherObject =
//                    dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
//            description = weatherObject.getString(OWM_DESCRIPTION);
//
//            /*
//             * Temperatures are sent by Open Weather Map in a child object called "temp".
//             *
//             * Editor's Note: Try not to name variables "temp" when working with temperature.
//             * It confuses everybody. Temp could easily mean any number of things, including
//             * temperature, temporary and is just a bad variable name.
//             */
//            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
//            high = temperatureObject.getDouble(OWM_MAX);
//            low = temperatureObject.getDouble(OWM_MIN);
//            highAndLow = SunshineWeatherUtils.formatHighLows(context, high, low);
//
//            parsedWeatherData[i] = date + " - " + description + " - " + highAndLow;
//        }

        return posterUrls;
    }
}

package com.example.adrax.dely.core;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Создан Максимом Сунцевым 18.08.2017.
 */

public class GoogleMapsHelper {
    private GoogleMapsHelper() {

    }

    public static GoogleMapsHelper getInstance() {
        return instance;
    }

    public void calculateDistance(
            @NonNull final String origin,
            @NonNull final String destination,
            @NonNull final InternetCallback<Double> callback
    ) {
        InternetTask task = new InternetTask(
                InternetTask.METHOD_GET,
                DISTANCE_URL,
                new InternetCallback<String>() {
            @Override
            public void call(String result) {
                double distance = -1.;

                try {
                    JSONObject response = new JSONObject(result);
                    distance = response.getJSONArray("routes")
                            .getJSONObject(0).getJSONArray("legs")
                            .getJSONObject(0)
                            .getJSONObject("distance").getDouble("value");
                } catch (JSONException e) {
                    LogHelper.error("Не удалось рассчитать дистанцию.");
                }

                callback.call(distance);
            }
        });

        task.execute(
                ORIGIN, origin,
                DESTINATION, destination,
                MODE, DEFAULT_MODE,
                LANGUAGE, DEFAULT_LANGUAGE,
                UNIT, DEFAULT_UNIT,
                REGION, DEFAULT_REGION,
                KEY, DEFAULT_KEY
        );
    }

    private static final String DISTANCE_URL = "https://maps.googleapis.com/maps/api/directions/json";

    private static final String ORIGIN = "origin";
    private static final String DESTINATION = "destination";
    private static final String MODE = "mode";
    private static final String LANGUAGE = "language";
    private static final String UNIT = "unit";
    private static final String REGION = "region";
    private static final String KEY = "key";

    private static final String DEFAULT_MODE = "driving";
    private static final String DEFAULT_LANGUAGE = "ru";
    private static final String DEFAULT_UNIT = "metric";
    private static final String DEFAULT_REGION = "ru";
    private static final String DEFAULT_KEY = "AIzaSyCOn2sDKTCQfGZfT3QTO99snxEikwrZkQ4";

    private static GoogleMapsHelper instance = new GoogleMapsHelper();
}

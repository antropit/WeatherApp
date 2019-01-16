package ru.geekbrains.weatherapp.model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeatherRepo {
    private static OpenWeatherAPI API;

    private static OpenWeatherRepo instance = null;

    private OpenWeatherRepo() { }

    public static OpenWeatherAPI getAPI() {
        if (instance == null) {
            instance = new OpenWeatherRepo();
            API = createAdapter();
        }
        return API;
    }

    private static OpenWeatherAPI createAdapter() {
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return adapter.create(OpenWeatherAPI.class);
    }
}

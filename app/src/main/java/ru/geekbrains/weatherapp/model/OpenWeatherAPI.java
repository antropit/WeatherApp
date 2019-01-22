package ru.geekbrains.weatherapp.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.geekbrains.weatherapp.model.rest_models.WeatherRequestRestModel;

public interface OpenWeatherAPI {
    @GET("data/2.5/weather")
    Call<WeatherRequestRestModel> loadWeather(@Query("q") String cityCountry, @Query("appid") String keyApi);
}

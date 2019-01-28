package ru.geekbrains.weatherapp.model.rest_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//"weather":[{"id":803,"main":"Clouds","description":"broken clouds","icon":"04n"}]
public class WeatherRestModel {
    @SerializedName("id") @Expose public int id;
    @SerializedName("main") @Expose public String main;
    @SerializedName("description") @Expose public String description;
    @SerializedName("icon") @Expose public String icon;
}

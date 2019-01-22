package ru.geekbrains.weatherapp.model.rest_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//"wind":{"speed":1,"deg":140}
public class WindRestModel {
    @SerializedName("speed") @Expose public float speed;
    @SerializedName("deg") @Expose public float deg;
}
package ru.geekbrains.weatherapp.model.rest_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//"coord":{"lon":30.26,"lat":59.89},
public class CoordRestModel {
    @SerializedName("lon") @Expose public float lon;
    @SerializedName("lat") @Expose public float lat;
}

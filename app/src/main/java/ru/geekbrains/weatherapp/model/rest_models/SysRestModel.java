package ru.geekbrains.weatherapp.model.rest_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//"sys":{"type":1,"id":7267,"message":0.0067,"country":"RU","sunrise":1512888593,"sunset":1512910452}
public class SysRestModel {
    @SerializedName("type") @Expose public int type;
    @SerializedName("id") @Expose public int id;
    @SerializedName("message") @Expose public float message;
    @SerializedName("country") @Expose public String country;
    @SerializedName("sunrise") @Expose public long sunrise;
    @SerializedName("sunset") @Expose public long sunset;
}
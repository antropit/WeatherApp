package ru.geekbrains.weatherapp.model.rest_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherRequestRestModel {
    @SerializedName("coord") @Expose public CoordRestModel coordRestModel;
    @SerializedName("weather") @Expose public WeatherRestModel[] weatherRestModel;
    @SerializedName("base") @Expose public String base;
    @SerializedName("main") @Expose public MainRestModel mainRestModel;
    @SerializedName("visibility") @Expose public int visibility;
    @SerializedName("wind") @Expose public WindRestModel windRestModel;
    @SerializedName("clouds") @Expose public CloudsRestModel cloudsRestModel;
    @SerializedName("dt") @Expose public long dt;
    @SerializedName("sys") @Expose public SysRestModel sysRestModel;
    @SerializedName("id") @Expose public long id;
    @SerializedName("name") @Expose public String name;
    @SerializedName("cod") @Expose public int cod;
}

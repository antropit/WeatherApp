package ru.geekbrains.weatherapp.model.rest_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//"clouds":{"all":75}
public class CloudsRestModel {
    @SerializedName("all") @Expose public int all;
}


package ru.geekbrains.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.geekbrains.weatherapp.model.OpenWeatherRepo;
import ru.geekbrains.weatherapp.model.rest_models.MainRestModel;
import ru.geekbrains.weatherapp.model.rest_models.WeatherRequestRestModel;
import ru.geekbrains.weatherapp.model.rest_models.WeatherRestModel;

public class WeatherDataLoader {
    private static final String OPEN_WEATHER_API_KEY = "762ee61f52313fbd10a4eb54ae4d4de2";
    private static final String OPEN_WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
    private static final String KEY = "x-api-key";
    private static final String RESPONSE = "cod";
    private static final int ALL_GOOD = 200;

    private static final String weather_sunny = "&#xf00d;";
    private static final String weather_clear_night = "&#xf02e;";
    private static final String weather_foggy = "&#xf014;";
    private static final String weather_cloudy = "&#xf013;";
    private static final String weather_rainy = "&#xf019;";
    private static final String weather_snowy = "&#xf01b;";
    private static final String weather_thunder = "&#xf01e;";
    private static final String weather_drizzle = "&#xf01c;";

    private static WeatherRequestRestModel retrofitResult;

    static JSONObject getJSONData(String city) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_API_URL, city));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty(KEY, OPEN_WEATHER_API_KEY);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder rawData = new StringBuilder(1024);
            String tempVariable;

            while ((tempVariable = reader.readLine()) != null) {
                rawData.append(tempVariable).append("\n");
            }

            reader.close();

            JSONObject jsonObject = new JSONObject(rawData.toString());
            if(jsonObject.getInt(RESPONSE) != ALL_GOOD) {
                return null;
            } else {
                return jsonObject;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }
    }

    static WeatherRequestRestModel requestRetrofit(String city) {
        OpenWeatherRepo.getAPI().loadWeather(city, OPEN_WEATHER_API_KEY)
                .enqueue(new Callback<WeatherRequestRestModel>() {
                    @Override
                    public void onResponse(Call<WeatherRequestRestModel> call, Response<WeatherRequestRestModel> response) {
                        if (response.isSuccessful()) retrofitResult = response.body();
                    }

                    @Override
                    public void onFailure(Call<WeatherRequestRestModel> call, Throwable t) {
                        retrofitResult = null;
                    }
                });

        while (retrofitResult == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        return retrofitResult;
    }

    static String renderWeather(WeatherRequestRestModel retrofitResult) {
        String res = "";
        try {
            WeatherRestModel[] details = retrofitResult.weatherRestModel;
            MainRestModel main = retrofitResult.mainRestModel;

            res = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "  <head>\n" +
                    "    <meta charset=\"utf-8\">\n" +
                    "    <style type='text/css'>\n" +
                    "       @font-face {\n" +
                    "           font-family: weather;\n" +
                    "           src: url('file:///android_asset/fonts/weather.ttf');\n" +
                    "       }\n" +
                    "    </style>\n" +
                    "  </head>\n" +
                    "  <body>\n";
            res += setPlaceName(retrofitResult);
            res += setUpdatedText(retrofitResult);
            res += setWeatherIcon(details[0].id,
                    retrofitResult.sysRestModel.sunrise * 1000,
                    retrofitResult.sysRestModel.sunset * 1000);

            res += "<br>";
            res += setDetails(details[0], main);
            res += "<br>";
            res += setCurrentTemp(main);
            res += "  </body>\n" +
                    "</html>";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    static String renderWeather(JSONObject jsonObject) {
        String res = "";
        try {
            JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
            JSONObject main = jsonObject.getJSONObject("main");

            res = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "  <head>\n" +
                    "    <meta charset=\"utf-8\">\n" +
                    "    <style type='text/css'>\n" +
                    "       @font-face {\n" +
                    "           font-family: weather;\n" +
                    "           src: url('file:///android_asset/fonts/weather.ttf');\n" +
                    "       }\n" +
                    "    </style>\n" +
                    "  </head>\n" +
                    "  <body>\n";
            res += setPlaceName(jsonObject);
            res += setUpdatedText(jsonObject);
            res += setWeatherIcon(details.getInt("id"),
                    jsonObject.getJSONObject("sys").getLong("sunrise") * 1000,
                    jsonObject.getJSONObject("sys").getLong("sunset") * 1000);

            res += "<br>";
            res += setDetails(details, main);
            res += "<br>";
            res += setCurrentTemp(main);
            res += "  </body>\n" +
                    "</html>";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    static String setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";

        if(actualId == 800) {
            long currentTime = new Date().getTime();
            if(currentTime >= sunrise && currentTime < sunset) {
                icon = "\u2600";
                //icon = getString(R.string.weather_sunny);
            } else {
                icon = weather_clear_night;
            }
        } else {
            switch (id) {
                case 1: {
                    icon = weather_sunny;
                    break;
                }
                case 2: {
                    icon = weather_thunder;
                    break;
                }
                case 3: {
                    icon = weather_drizzle;
                    break;
                }
                case 5: {
                    icon = weather_rainy;
                    break;
                }
                case 6: {
                    icon = weather_snowy;
                    break;
                }
                case 7: {
                    icon = weather_foggy;
                    break;
                }
                case 8: {
                    icon = weather_cloudy;
                    break;
                }
            }
        }
        return String.format("<div align=\"center\" style=\"font-family: weather; font-size:200px\">%s</div>", icon);
    }

    static String setUpdatedText(WeatherRequestRestModel retrofitResult) {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        String updateOn = dateFormat.format(new Date(retrofitResult.dt * 1000));
        String updatedText = "Last update: " + updateOn;
        return String.format("<div align=\"center\" style=\"font-size:x-small\">%s</div>", updatedText);
    }

    static String setUpdatedText(JSONObject jsonObject) throws JSONException {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        String updateOn = dateFormat.format(new Date(jsonObject.getLong("dt") * 1000));
        String updatedText = "Last update: " + updateOn;
        return String.format("<div align=\"center\" style=\"font-size:x-small\">%s</div>", updatedText);
    }

    static String setCurrentTemp(MainRestModel main) {
        String currentTextText = String.format("%.2f", main.temp) + "\u2103";
        return String.format("<div align=\"center\" style=\"font-size:xx-large\">%s</div>", currentTextText);
    }

    static String setCurrentTemp(JSONObject main) throws JSONException {
        String currentTextText = String.format("%.2f", main.getDouble("temp")) + "\u2103";
        return String.format("<div align=\"center\" style=\"font-size:xx-large\">%s</div>", currentTextText);
    }

    static String setDetails(WeatherRestModel details, MainRestModel main) {
        String detailsText = details.description.toUpperCase() + "<br>"
                + "Humidity: " + main.humidity + "%" + "<br>"
                + "Pressure: " + main.pressure + "hPa";
        return String.format("<div align=\"center\">%s</div>", detailsText);
    }

    static String setDetails(JSONObject details, JSONObject main) throws JSONException {
        String detailsText = details.getString("description").toUpperCase() + "<br>"
                + "Humidity: " + main.getString("humidity") + "%" + "<br>"
                + "Pressure: " + main.getString("pressure") + "hPa";
        return String.format("<div align=\"center\">%s</div>", detailsText);
    }

    static String setPlaceName(WeatherRequestRestModel retrofitResult) {
        String cityText = retrofitResult.name.toUpperCase() + ", "
                + retrofitResult.sysRestModel.country;
        return String.format("<div align=\"center\" style=\"font-size:x-large\">%s</div>", cityText);
    }

    static String setPlaceName(JSONObject jsonObject) throws JSONException {
        String cityText = jsonObject.getString("name").toUpperCase() + ", "
                + jsonObject.getJSONObject("sys").getString("country");
        return String.format("<div align=\"center\" style=\"font-size:x-large\">%s</div>", cityText);
    }
}

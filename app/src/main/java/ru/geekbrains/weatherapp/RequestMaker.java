package ru.geekbrains.weatherapp;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.geekbrains.weatherapp.database.DatabaseHelper;
import ru.geekbrains.weatherapp.database.WeatherDBTable;
import ru.geekbrains.weatherapp.model.rest_models.MainRestModel;
import ru.geekbrains.weatherapp.model.rest_models.WeatherRequestRestModel;
import ru.geekbrains.weatherapp.model.rest_models.WeatherRestModel;

// Делатель запросов (класс, умеющий запрашивать страницы)
public class RequestMaker {

    private boolean isWeatherRequest = false; //check if weather data needed
    private SQLiteDatabase database;

    // Слушатель, при помощи которого отправим обратный вызов о готовности страницы
    private OnRequestListener listener;

    // В конструкторе примем слушателя, а в дальнейшем передадим его асинхронной задаче
    public RequestMaker(OnRequestListener onRequestListener){
        listener = onRequestListener;
    }

    // Сделать запрос
    public void make(String uri) {
        // Создаем объект асинхронной задачи (передаем ей слушатель)
        Requester requester = new Requester(listener, isWeatherRequest, database);
        // Запускаем асинхронную задачу
        requester.execute(uri);
    }

    public void setWeatherRequest() {
        isWeatherRequest = true;
    }

    public void setDatabase(SQLiteDatabase db) {
        this.database = db;
    }
    // Интерфейс слушателя с методами обратного вызова
    public interface OnRequestListener {
        void onStatusProgress(String updateProgress);   // Вызов для обновления прогресса
        void onComplete(String result);                 // Вызов при завершении обработки
    }

    // AsyncTask - это обертка для выполнения потока в фоне
    // Начальные и конечные методы работают в потоке UI, а основной метод расчета работает в фоне
    private static class Requester extends AsyncTask<String, String, String> {
        private OnRequestListener listener;
        private boolean isWeatherRequest;
        private SQLiteDatabase database;

        Requester(OnRequestListener listener, boolean weatherRequest, SQLiteDatabase db) {
            this.listener = listener;
            this.isWeatherRequest = weatherRequest;
            this.database = db;
        }

        // Обновление прогресса, работает в основном потоке UI
        @Override
        protected void onProgressUpdate(String... strings) {
            listener.onStatusProgress(strings[0]);
        }

        // Выполнить таск в фоновом потоке
        @Override
        protected String doInBackground(String... strings) {
            if (isWeatherRequest) return updateWeatherData(strings[0]);

            return getResourceUri(strings[0]);
        }

        // Выдать результат (работает в основном потоке UI)
        @Override
        protected void onPostExecute(String content) {
            listener.onComplete(content);
        }


        //get weather data async
        private String updateWeatherData(String city) {
//            final JSONObject jsonObject = WeatherDataLoader.getJSONData(city);
            final WeatherRequestRestModel jsonObject = WeatherDataLoader.requestRetrofit(city);
            if(jsonObject == null) {
                publishProgress("City not found");
                return "City not found";
            }

            MainRestModel main = jsonObject.mainRestModel;
            main.temp -= 273.15; main.temp_max -= 273.15; main.temp_min -= 273.15;

            WeatherDBTable.getInstance().addSite(database, jsonObject.name, jsonObject.weatherRestModel[0].id, main.temp, main.pressure, main.humidity);
            return WeatherDataLoader.renderWeather(jsonObject);
        }

        // Обработка загрузки страницы
        private String getResourceUri(String uri) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(uri); // Указать адрес URI
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET"); // Установка метода получения данных - GET
                urlConnection.setReadTimeout(10000); // Установка таймаута - 10 000 миллисекунд
                publishProgress("Подготовка данных"); // Обновим прогресс
                urlConnection.connect();              // Соединиться
                publishProgress("Соединение");        // Обновим прогресс
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));   // Читаем  данные в поток ввода/вывода
                StringBuilder buf = new StringBuilder(); // Здесь будем формировать результат
                publishProgress("Получение данных");  // Обновим прогресс
                // Обработка выходных данных in
                String line = null;
                int numLine = 0; // Эта переменная нужна лишь для показа прогресса
                // Читаем все строки из полученных выходных данных
                while ((line = in.readLine()) != null) {
                    numLine++;
                    // Обновим прогресс
                    publishProgress(String.format("Строка %d", numLine));                    buf.append(line);   // Добавим еще одну строку в результат
                    // Это перевод каретки
                    buf.append(System.getProperty("line.separator"));
                }
                return buf.toString();

            } catch (Exception e) {
                Log.e("WebBrowser", e.getMessage(), e);
                // Обновим прогресс
                publishProgress("Ошибка");
                return "Ошибка получения URL";
            } finally {
                // Разъединиться
                if (urlConnection != null) urlConnection.disconnect();
            }
        }
    }
}


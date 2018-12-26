package ru.geekbrains.weatherapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class SensorActivity extends AppCompatActivity {

    private TextView textConsole;
    private TextView textLight;
    private TextView textTemperature;
    private TextView textHumidity;

    private SensorManager sensorManager;
    private List<Sensor> sensors;
    private Sensor sensorLight, sensorAmbientTemperature, sensorHumidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        textConsole = findViewById(R.id.textConsole);
        textLight = findViewById(R.id.textLight);
        textTemperature = findViewById(R.id.textTemperature);
        textHumidity = findViewById(R.id.textHumidity);

        // Менеджер датчиков
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // Получить все датчики, какие есть
        sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        // Датчик освещенности (он есть на многих моделях)
        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        // Регистрируем слушатель датчика освещенности
        sensorManager.registerListener(listenerLight, sensorLight,
                SensorManager.SENSOR_DELAY_NORMAL);

        sensorAmbientTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        // Регистрируем слушатель датчика освещенности
        sensorManager.registerListener(listenerTemperature, sensorAmbientTemperature,
                SensorManager.SENSOR_DELAY_NORMAL);

        sensorHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        // Регистрируем слушатель датчика освещенности
        sensorManager.registerListener(listenerHumidity, sensorHumidity,
                SensorManager.SENSOR_DELAY_NORMAL);

        // Показать все сенсоры, какие есть
        showSensors();
    }

    // Если приложение свернуто, то не будем тратить энергию на получение информации по датчикам
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listenerLight, sensorLight);
    }

    // Вывод всех сенсоров
    private void showSensors() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Sensor sensor : sensors) {
            stringBuilder.append("name = ").append(sensor.getName())
                    .append(", type = ").append(sensor.getType())
                    .append("\n")
                    .append("vendor = ").append(sensor.getVendor())
                    .append(" ,version = ").append(sensor.getVersion())
                    .append("\n")
                    .append("max = ").append(sensor.getMaximumRange())
                    .append(", resolution = ").append(sensor.getResolution())
                    .append("\n").append("--------------------------------------").append("\n");
        }
        textConsole.setText(stringBuilder);
    }

    // Вывод датчика освещенности
    private void showLightSensors(SensorEvent event){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Light Sensor value = ").append(event.values[0])
                .append("\n").append("=======================================").append("\n");
        textLight.setText(stringBuilder);
    }

    // Вывод датчика температуры
    private void showTemperature(SensorEvent event){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Temperature value = ").append(event.values[0])
                .append("\n").append("=======================================").append("\n");
        textTemperature.setText(stringBuilder);
    }

    // Вывод датчика влажности
    private void showHumidity(SensorEvent event){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Humidity value = ").append(event.values[0])
                .append("\n").append("=======================================").append("\n");
        textHumidity.setText(stringBuilder);
    }

    // Слушатель датчика освещенности
    SensorEventListener listenerLight = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            showLightSensors(event);
        }
    };

    // Слушатель датчика температуры
    SensorEventListener listenerTemperature = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            showTemperature(event);
        }
    };

    // Слушатель датчика влажности
    SensorEventListener listenerHumidity = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            showHumidity(event);
        }
    };}

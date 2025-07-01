/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package weather;

/**
 *
 * @author brand
 */

class BackupWeatherObject {
    private BackupWeatherData current_weather;

    public BackupWeatherData getCurrentWeather() {
        return current_weather;
    }

    public void setCurrentWeather(BackupWeatherData current_weather) {
        this.current_weather = current_weather;
    }
}

class BackupWeatherData {
    private double temperature;
    private double windspeed;
    private int weathercode;

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(double windspeed) {
        this.windspeed = windspeed;
    }

    public int getWeatherCode() {
        return weathercode;
    }

    public void setWeatherCode(int weathercode) {
        this.weathercode = weathercode;
    }
}


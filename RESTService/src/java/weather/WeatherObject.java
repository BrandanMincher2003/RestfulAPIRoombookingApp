package weather;

import java.util.List;

public class WeatherObject {
    private String product;
    private String init;
    private List<WeatherData> dataseries;

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public String getInit() { return init; }
    public void setInit(String init) { this.init = init; }

    public List<WeatherData> getDataseries() { return dataseries; }
    public void setDataseries(List<WeatherData> dataseries) { this.dataseries = dataseries; }
}

class WeatherData {
    private int date;
    private String weather;
    private Temp2m temp2m;
    private int wind10m_max;

    public int getDate() { return date; }
    public void setDate(int date) { this.date = date; }

    public String getWeather() { return weather; }
    public void setWeather(String weather) { this.weather = weather; }

    public Temp2m getTemp2m() { return temp2m; }
    public void setTemp2m(Temp2m temp2m) { this.temp2m = temp2m; }

    public int getWind10m_max() { return wind10m_max; }
    public void setWind10m_max(int wind10m_max) { this.wind10m_max = wind10m_max; }
}

class Temp2m {
    private int max;
    private int min;

    public int getMax() { return max; }
    public void setMax(int max) { this.max = max; }

    public int getMin() { return min; }
    public void setMin(int min) { this.min = min; }
}

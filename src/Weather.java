public class Weather {
    private String condition; // "Dry", "Wet", or "Mixed"
    private int temperature;  // in Celsius
    private int windSpeed;    // in km/h
    private int rainIntensity; // 0 (dry) to 10 (heavy rain)

    // Constructor
    public Weather(String condition, int temperature, int windSpeed, int rainIntensity) {
        this.condition = condition;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.rainIntensity = rainIntensity;
    }

    // Getters and setters
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getRainIntensity() {
        return rainIntensity;
    }

    public void setRainIntensity(int rainIntensity) {
        this.rainIntensity = rainIntensity;
    }

    // 判断是否属于复杂天气
    public boolean isChallenging() {
        return rainIntensity > 5 || windSpeed > 30;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "condition='" + condition + '\'' +
                ", temperature=" + temperature +
                ", windSpeed=" + windSpeed +
                ", rainIntensity=" + rainIntensity +
                '}';
    }

    // 静态工厂方法
    public static Weather createDryWeather() {
        return new Weather("Dry", 25, 10, 0);
    }

    public static Weather createWetWeather() {
        return new Weather("Wet", 15, 20, 7);
    }

    public static Weather createMixedWeather() {
        return new Weather("Mixed", 20, 25, 3);
    }
}

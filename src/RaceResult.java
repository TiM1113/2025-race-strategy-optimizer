import java.time.LocalDateTime;

/**
 * Represents the result of a race simulation.
 * Updated to match Main.java requirements.
 */
public class RaceResult {
    private String carName;
    private String trackName;
    private double raceTime;
    private String strategy;
    private LocalDateTime timestamp;

    // 保留原有字段作为额外信息（可选）
    private double averageLapTime;
    private int pitStopCount;
    private String weatherCondition;

    // ✅ Main.java中使用的主要构造函数
    public RaceResult(String carName, String trackName, double raceTime, String strategy) {
        this.carName = carName;
        this.trackName = trackName;
        this.raceTime = raceTime;
        this.strategy = strategy;
        this.timestamp = LocalDateTime.now();

        // 设置默认值
        this.averageLapTime = 0.0;
        this.pitStopCount = 0;
        this.weatherCondition = "Unknown";
    }

    // ✅ 扩展构造函数 - 包含你原有的字段
    public RaceResult(String carName, String trackName, double raceTime, String strategy,
                      double averageLapTime, int pitStopCount, String weatherCondition) {
        this.carName = carName;
        this.trackName = trackName;
        this.raceTime = raceTime;
        this.strategy = strategy;
        this.timestamp = LocalDateTime.now();
        this.averageLapTime = averageLapTime;
        this.pitStopCount = pitStopCount;
        this.weatherCondition = weatherCondition;
    }

    // ✅ 完整构造函数
    public RaceResult(String carName, String trackName, double raceTime, String strategy,
                      LocalDateTime timestamp, double averageLapTime, int pitStopCount, String weatherCondition) {
        this.carName = carName;
        this.trackName = trackName;
        this.raceTime = raceTime;
        this.strategy = strategy;
        this.timestamp = timestamp;
        this.averageLapTime = averageLapTime;
        this.pitStopCount = pitStopCount;
        this.weatherCondition = weatherCondition;
    }

    // ✅ Main.java需要的getter方法
    public String getCarName() {
        return carName;
    }

    public String getTrackName() {
        return trackName;
    }

    public double getRaceTime() {
        return raceTime;
    }

    public String getStrategy() {
        return strategy;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // ✅ 保留你原有的getter方法
    public double getTotalTime() {
        return raceTime; // 映射到相同含义
    }

    public double getAverageLapTime() {
        return averageLapTime;
    }

    public int getPitStopCount() {
        return pitStopCount;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    // ✅ 保留你原有的方法
    public boolean isWinningTime(double targetTime) {
        return this.raceTime < targetTime;
    }

    // ✅ Setter方法（如果需要更新结果）
    public void setCarName(String carName) {
        this.carName = carName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public void setRaceTime(double raceTime) {
        this.raceTime = raceTime;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setAverageLapTime(double averageLapTime) {
        this.averageLapTime = averageLapTime;
    }

    public void setPitStopCount(int pitStopCount) {
        this.pitStopCount = pitStopCount;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    @Override
    public String toString() {
        return String.format("RaceResult{car='%s', track='%s', time=%.1f min, strategy='%s', " +
                        "avgLapTime=%.1f s, pitStops=%d, weather='%s', timestamp=%s}",
                carName, trackName, raceTime, strategy, averageLapTime,
                pitStopCount, weatherCondition, timestamp);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        RaceResult that = (RaceResult) obj;
        return Double.compare(that.raceTime, raceTime) == 0 &&
                carName.equals(that.carName) &&
                trackName.equals(that.trackName) &&
                strategy.equals(that.strategy);
    }

    @Override
    public int hashCode() {
        int result = carName.hashCode();
        result = 31 * result + trackName.hashCode();
        result = 31 * result + Double.hashCode(raceTime);
        result = 31 * result + strategy.hashCode();
        return result;
    }
}
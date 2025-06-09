import java.util.Random;

/**
 * Handles race simulation logic and calculations.
 */
public class RaceSimulator {
    private int totalLaps;
    private int currentLap;
    private boolean isRaceFinished;

    public RaceSimulator(int totalLaps) {
        this.totalLaps = totalLaps;
        this.currentLap = 0;
        this.isRaceFinished = false;
    }

    /**
     * Simulates a complete race with the given parameters.
     *
     * @param car The car participating in the race
     * @param track The track being raced on
     * @param strategy The race strategy being used
     * @param weather The weather conditions
     * @return RaceResult containing the race outcome
     */
    public RaceResult simulateRace(Car car, Track track, RaceStrategy strategy, Weather weather) {
        double totalLapTime = 0;
        Random rand = new Random();

        // Reset race state
        currentLap = 0;
        isRaceFinished = false;

        // Simulate each lap
        for (int i = 0; i < totalLaps; i++) {
            double lapTime = simulateLap(car, track, weather);
            // Add realistic variation ±2 seconds
            lapTime += rand.nextDouble() * 4 - 2;
            totalLapTime += lapTime;
            currentLap++;
        }

        // Calculate pit stop time
        double pitStopTime = simulatePitStop(strategy);
        double totalTime = totalLapTime + pitStopTime;

        isRaceFinished = true;

        // Calculate average lap time
        double averageLap = totalLapTime / totalLaps;

        // ✅ 使用新的RaceResult构造函数
        // 创建策略名称
        String strategyName = getStrategyName(strategy);

        // 使用Main.java期望的构造函数：RaceResult(carName, trackName, raceTime, strategy)
        RaceResult result = new RaceResult(
                car.getName(),
                track.getName(),
                totalTime / 60.0,  // 转换为分钟
                strategyName
        );

        // ✅ 设置额外的详细信息（使用新增的setter方法）
        result.setAverageLapTime(averageLap);
        result.setPitStopCount(strategy.getNumberOfPitStops());
        result.setWeatherCondition(weather.getCondition());

        return result;
    }

    /**
     * Simulates a single lap.
     */
    public double simulateLap(Car car, Track track, Weather weather) {
        Performance performance = PerformanceCalculator.createCarPerformance(car, track);
        double lapTime = performance.getLapTime();

        // Apply weather modifiers
        if (weather.getRainIntensity() > 5) {
            lapTime *= 1.10;  // 10% slower for heavy rain
        }

        if (weather.getWindSpeed() > 30) {
            lapTime *= 1.05;  // 5% slower for high wind
        }

        // Apply track difficulty modifier
        switch (track.getDifficulty()) {
            case "Hard":
                lapTime *= 1.05;
                break;
            case "Easy":
                lapTime *= 0.98;
                break;
            // Medium remains unchanged
        }

        return lapTime;
    }

    /**
     * Calculates total pit stop time based on strategy.
     */
    public double simulatePitStop(RaceStrategy strategy) {
        int pitStops = strategy.getNumberOfPitStops();
        double perStopTime;

        switch (strategy.getFuelStrategy()) {
            case "Light":
                perStopTime = 25.0;  // Fast refuel
                break;
            case "Medium":
                perStopTime = 30.0;  // Standard refuel
                break;
            case "Heavy":
                perStopTime = 35.0;  // Slow, full refuel
                break;
            default:
                perStopTime = 30.0;  // Default to medium
                break;
        }

        return pitStops * perStopTime;
    }

    /**
     * Generates a strategy name based on strategy characteristics.
     */
    private String getStrategyName(RaceStrategy strategy) {
        if (strategy.getNumberOfPitStops() >= 3 && "Light".equals(strategy.getFuelStrategy())) {
            return "Aggressive Strategy";
        } else if (strategy.getNumberOfPitStops() <= 1 && "Heavy".equals(strategy.getFuelStrategy())) {
            return "Conservative Strategy";
        } else {
            return "Balanced Strategy";
        }
    }

    /**
     * Simulates a race with automatic lap calculation based on track length.
     */
    public RaceResult simulateRace(Car car, Track track, RaceStrategy strategy, Weather weather, double targetRaceDistance) {
        // Calculate laps needed for target distance
        int calculatedLaps = (int) Math.ceil(targetRaceDistance / track.getLength());

        // Store original totalLaps and restore after simulation
        int originalTotalLaps = this.totalLaps;
        this.totalLaps = calculatedLaps;

        RaceResult result = simulateRace(car, track, strategy, weather);

        // Restore original totalLaps
        this.totalLaps = originalTotalLaps;

        return result;
    }

    // Getter methods
    public boolean isRaceFinished() {
        return isRaceFinished;
    }

    public int getCurrentLap() {
        return currentLap;
    }

    public int getTotalLaps() {
        return totalLaps;
    }

    public void setTotalLaps(int totalLaps) {
        this.totalLaps = totalLaps;
    }

    /**
     * Resets the race state for a new simulation.
     */
    public void resetRace() {
        currentLap = 0;
        isRaceFinished = false;
    }

    /**
     * Gets the current race progress as a percentage.
     */
    public double getRaceProgress() {
        if (totalLaps == 0) return 0.0;
        return (double) currentLap / totalLaps * 100.0;
    }

    @Override
    public String toString() {
        return String.format("RaceSimulator{totalLaps=%d, currentLap=%d, finished=%s, progress=%.1f%%}",
                totalLaps, currentLap, isRaceFinished, getRaceProgress());
    }
}
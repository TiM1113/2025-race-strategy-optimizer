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

        // Tyre wear modeling
        int totalPitStops = strategy.getNumberOfPitStops();
        int stints = totalPitStops + 1;
        int[] stintLengths = new int[stints];
        int baseStint = totalLaps / stints;
        int extra = totalLaps % stints;
        for (int i = 0; i < stints; i++) {
            stintLengths[i] = baseStint + (i < extra ? 1 : 0);
        }
        int stintIndex = 0;
        int lapsOnTyre = 0;
        int lapsInCurrentStint = 0;

        // Parse compounds from strategy
        java.util.List<String> compounds = strategy.getTyreCompoundsForStints(stints);
        Tyre currentTyre = getTyreByCompound(compounds.get(0));

        for (int i = 0; i < totalLaps; i++) {
            // Pit stop at the start of each new stint except the first
            if (lapsInCurrentStint == 0 && i != 0) {
                lapsOnTyre = 0;
                stintIndex++;
                currentTyre = getTyreByCompound(compounds.get(stintIndex));
            }

            double lapTime = simulateLapWithTyre(car, track, weather, currentTyre, lapsOnTyre, strategy);
            // Add realistic variation ±2 seconds
            lapTime += rand.nextDouble() * 4 - 2;
            totalLapTime += lapTime;
            currentLap++;
            lapsOnTyre++;
            lapsInCurrentStint++;
            if (lapsInCurrentStint >= stintLengths[stintIndex]) {
                lapsInCurrentStint = 0;
            }
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

    // Getter methods
    public boolean isRaceFinished() {
        return isRaceFinished;
    }

    public int getCurrentLap() {
        return currentLap;
    }

    // New helper for lap time with tyre wear/compound
    private double simulateLapWithTyre(Car car, Track track, Weather weather, Tyre tyre, int lapsOnTyre, RaceStrategy strategy) {
        Performance performance = PerformanceCalculator.createCarPerformance(car, track);
        double lapTime = performance.getLapTime();

        // Tyre compound base bonus scaled by how technical the track is (more corners -> greater benefit from softer tyres)
        double cornerFactor = Math.max(0.6, track.getCorners() / 15.0);
        double lengthFactor = Math.max(0.7, track.getLength() / 4.5);
        lapTime += tyre.getBaseLapTimeBonus() * cornerFactor * cornerFactor / lengthFactor;
        
        // Tyre wear penalty with cliff effect
        double wearPenalty = 0;
        if (lapsOnTyre > tyre.getDurability()) {
            // After durability is exceeded, apply a sharp performance cliff
            wearPenalty = tyre.getWearRate() * (lapsOnTyre - tyre.getDurability()) * 8.0 * cornerFactor * lengthFactor;
        } else {
            // Normal wear before durability limit
            wearPenalty = tyre.getWearRate() * lapsOnTyre * 3.0 * cornerFactor * lengthFactor;
        }
        lapTime += wearPenalty;

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

        // Fuel load modifier based on strategy
        double fuelAdj = 0.0;
        switch (strategy.getFuelStrategy()) {
            case "Light":
                fuelAdj = -0.004 * cornerFactor; // further reduced bonus
                break;
            case "Heavy":
                fuelAdj = 0.004 * cornerFactor; // reduced penalty
                break;
            // Medium remains unchanged
        }
        lapTime *= (1.0 + fuelAdj);

        return lapTime;
    }

    // Helper to get Tyre object by compound name
    private Tyre getTyreByCompound(String compound) {
        switch (compound.toLowerCase()) {
            case "soft":
                return Tyre.createSoftTyre();
            case "medium":
                return Tyre.createMediumTyre();
            case "hard":
                return Tyre.createHardTyre();
            default:
                return Tyre.createMediumTyre(); // fallback
        }
    }
}
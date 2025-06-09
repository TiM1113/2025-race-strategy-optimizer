public class RaceStrategy {
    private int numberOfPitStops;
    private String tyreStrategy;
    private String fuelStrategy;
    private double estimatedRaceTime;

    public RaceStrategy(int numberOfPitStops, String tyreStrategy, String fuelStrategy, double estimatedRaceTime) {
        this.numberOfPitStops = numberOfPitStops;
        this.tyreStrategy = tyreStrategy;
        this.fuelStrategy = fuelStrategy;
        this.estimatedRaceTime = estimatedRaceTime;
    }

    public int getNumberOfPitStops() {
        return numberOfPitStops;
    }

    public void setNumberOfPitStops(int numberOfPitStops) {
        this.numberOfPitStops = numberOfPitStops;
    }

    public String getTyreStrategy() {
        return tyreStrategy;
    }

    public void setTyreStrategy(String tyreStrategy) {
        this.tyreStrategy = tyreStrategy;
    }

    public String getFuelStrategy() {
        return fuelStrategy;
    }

    public void setFuelStrategy(String fuelStrategy) {
        this.fuelStrategy = fuelStrategy;
    }

    public double getEstimatedRaceTime() {
        return estimatedRaceTime;
    }

    public void setEstimatedRaceTime(double estimatedRaceTime) {
        this.estimatedRaceTime = estimatedRaceTime;
    }

    public boolean isConservativeStrategy() {
        return numberOfPitStops <= 1 && "Heavy".equalsIgnoreCase(fuelStrategy);
    }

    @Override
    public String toString() {
        return "RaceStrategy{" +
                "pitStops=" + numberOfPitStops +
                ", tyreStrategy='" + tyreStrategy + '\'' +
                ", fuelStrategy='" + fuelStrategy + '\'' +
                ", estimatedRaceTime=" + estimatedRaceTime + " min" +
                '}';
    }

    // Static factory methods
    public static RaceStrategy createAggressiveStrategy() {
        return new RaceStrategy(3, "Soft-Medium", "Light", 90.0);
    }

    public static RaceStrategy createBalancedStrategy() {
        return new RaceStrategy(2, "Medium-Hard", "Medium", 95.0);
    }

    public static RaceStrategy createConservativeStrategy() {
        return new RaceStrategy(1, "Medium-Hard", "Heavy", 100.0);
    }
}

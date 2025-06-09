// Performance.java
public class Performance {
    private int topSpeed;
    private double acceleration;
    private double fuelConsumption;
    private double lapTime;
    private int corneringAbility;

    // Constructor
    public Performance(int topSpeed, double acceleration, double fuelConsumption, double lapTime, int corneringAbility) {
        this.topSpeed = topSpeed;
        this.acceleration = acceleration;
        this.fuelConsumption = fuelConsumption;
        this.lapTime = lapTime;
        this.corneringAbility = corneringAbility;
    }

    // Getters and Setters
    public int getTopSpeed() {
        return topSpeed;
    }

    public void setTopSpeed(int topSpeed) {
        this.topSpeed = topSpeed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public double getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(double fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public double getLapTime() {
        return lapTime;
    }

    public void setLapTime(double lapTime) {
        this.lapTime = lapTime;
    }

    public int getCorneringAbility() {
        return corneringAbility;
    }

    public void setCorneringAbility(int corneringAbility) {
        this.corneringAbility = corneringAbility;
    }

    // Method to return formatted performance details
    @Override
    public String toString() {
        return "Performance{" +
                "topSpeed=" + topSpeed + " km/h" +
                ", acceleration=" + acceleration + " sec (0-100)" +
                ", fuelConsumption=" + fuelConsumption + " L/lap" +
                ", lapTime=" + lapTime + " sec" +
                ", corneringAbility=" + corneringAbility +
                '}';
    }

    // Calculate overall rating
    public int getOverallRating() {
        return (int) ((topSpeed / 10.0) + (corneringAbility * 10) - (acceleration * 5));
    }

    // Compare lap times
    public boolean isFewerThan(Performance other) {
        return this.lapTime < other.lapTime;
    }
}

public class Tyre {
    private String compound;         // Tyre compound type (Soft, Medium, Hard)
    private double gripLevel;        // Grip coefficient (0.0 to 1.0)
    private int durability;          // Expected lifespan in laps
    private int optimalTemperature;  // Best performance temperature in Celsius
    private double wearRate;           // How quickly the tyre degrades per lap
    private double baseLapTimeBonus;   // Lap time bonus (negative = faster)

    // Constructor
    public Tyre(String compound, double gripLevel, int durability, int optimalTemperature, double wearRate, double baseLapTimeBonus) {
        this.compound = compound;
        this.gripLevel = gripLevel;
        this.durability = durability;
        this.optimalTemperature = optimalTemperature;
        this.wearRate = wearRate;
        this.baseLapTimeBonus = baseLapTimeBonus;
    }

    // Legacy constructor for compatibility
    public Tyre(String compound, double gripLevel, int durability, int optimalTemperature) {
        this(compound, gripLevel, durability, optimalTemperature, 0.04, 0.0); // Default to medium
    }

    // Getters and Setters
    public String getCompound() {
        return compound;
    }

    public void setCompound(String compound) {
        this.compound = compound;
    }

    public double getGripLevel() {
        return gripLevel;
    }

    public void setGripLevel(double gripLevel) {
        this.gripLevel = gripLevel;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public int getOptimalTemperature() {
        return optimalTemperature;
    }

    public void setOptimalTemperature(int optimalTemperature) {
        this.optimalTemperature = optimalTemperature;
    }

    public double getWearRate() {
        return wearRate;
    }

    public void setWearRate(double wearRate) {
        this.wearRate = wearRate;
    }

    public double getBaseLapTimeBonus() {
        return baseLapTimeBonus;
    }

    public void setBaseLapTimeBonus(double baseLapTimeBonus) {
        this.baseLapTimeBonus = baseLapTimeBonus;
    }

    // Convert grip level to integer percentage rating
    public int getPerformanceRating() {
        return (int)(gripLevel * 100);
    }

    // toString method
    @Override
    public String toString() {
        return "Tyre{" +
                "compound='" + compound + '\'' +
                ", gripLevel=" + gripLevel +
                ", durability=" + durability +
                ", optimalTemperature=" + optimalTemperature +
                ", wearRate=" + wearRate +
                ", baseLapTimeBonus=" + baseLapTimeBonus +
                '}';
    }

    // Static factory methods
    public static Tyre createSoftTyre() {
        return new Tyre("Soft", 0.95, 15, 100, 0.10, -2.5);
    }

    public static Tyre createMediumTyre() {
        return new Tyre("Medium", 0.85, 25, 90, 0.05, -1.0);
    }

    public static Tyre createHardTyre() {
        return new Tyre("Hard", 0.75, 35, 80, 0.02, 0.0);
    }
}

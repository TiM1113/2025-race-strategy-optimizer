public class Tyre {
    private String compound;         // Tyre compound type (Soft, Medium, Hard)
    private double gripLevel;        // Grip coefficient (0.0 to 1.0)
    private int durability;          // Expected lifespan in laps
    private int optimalTemperature;  // Best performance temperature in Celsius

    // Constructor
    public Tyre(String compound, double gripLevel, int durability, int optimalTemperature) {
        this.compound = compound;
        this.gripLevel = gripLevel;
        this.durability = durability;
        this.optimalTemperature = optimalTemperature;
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
                '}';
    }

    // Static factory methods
    public static Tyre createSoftTyre() {
        return new Tyre("Soft", 0.95, 15, 100);
    }

    public static Tyre createMediumTyre() {
        return new Tyre("Medium", 0.85, 25, 90);
    }

    public static Tyre createHardTyre() {
        return new Tyre("Hard", 0.75, 35, 80);
    }
}

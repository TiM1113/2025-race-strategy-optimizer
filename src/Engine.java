public class Engine {

    private String type;             // Name of the engine type
    private int power;               // Engine power in horsepower (HP)
    private double fuelEfficiency;   // Fuel efficiency in km per liter
    private double weight;           // Engine weight in kilograms

    // Constructor to initialize all fields
    public Engine(String type, int power, double fuelEfficiency, double weight) {
        this.type = type;
        this.power = power;
        this.fuelEfficiency = fuelEfficiency;
        this.weight = weight;
    }

    // Getter and Setter methods
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public double getFuelEfficiency() {
        return fuelEfficiency;
    }

    public void setFuelEfficiency(double fuelEfficiency) {
        this.fuelEfficiency = fuelEfficiency;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    // Returns the ratio of power to weight
    public double calculatePowerToWeight() {
        if (weight == 0) {
            return 0; // Avoid division by zero
        }
        return power / weight;
    }

    // Returns string representation of the engine
    @Override
    public String toString() {
        return "Engine{" +
                "type='" + type + '\'' +
                ", power=" + power +
                ", fuelEfficiency=" + fuelEfficiency +
                ", weight=" + weight +
                '}';
    }

    // Static factory method for standard engine
    public static Engine createStandardEngine() {
        return new Engine("Standard", 200, 12.0, 150.0);
    }

    // Static factory method for turbocharged engine
    public static Engine createTurboEngine() {
        return new Engine("Turbocharged", 300, 9.0, 180.0);
    }
}

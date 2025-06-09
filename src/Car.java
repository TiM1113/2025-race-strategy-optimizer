// Car.java
public class Car {
    private int id;
    private String name;
    private double weight;
    private boolean isConfigured;

    private Engine engine;
    private Tyre frontTyres;
    private Tyre rearTyres;
    private AeroKit aeroKit;

    public Car(int id, String name, double weight, boolean isConfigured) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.isConfigured = isConfigured;
    }

    public Car(int id, String name, double weight, Engine engine, Tyre frontTyres, Tyre rearTyres, AeroKit aeroKit) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.engine = engine;
        this.frontTyres = frontTyres;
        this.rearTyres = rearTyres;
        this.aeroKit = aeroKit;
        this.isConfigured = true;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public boolean isConfigured() { return isConfigured; }
    public void setConfigured(boolean configured) { isConfigured = configured; }

    public Engine getEngine() { return engine; }
    public void setEngine(Engine engine) { this.engine = engine; }

    public Tyre getFrontTyres() { return frontTyres; }
    public void setFrontTyres(Tyre frontTyres) { this.frontTyres = frontTyres; }

    public Tyre getRearTyres() { return rearTyres; }
    public void setRearTyres(Tyre rearTyres) { this.rearTyres = rearTyres; }

    public AeroKit getAeroKit() { return aeroKit; }
    public void setAeroKit(AeroKit aeroKit) { this.aeroKit = aeroKit; }

    public double getTotalWeight() {
        return weight + (engine != null ? engine.getWeight() : 0);
    }

    public boolean isFullyConfigured() {
        return engine != null && frontTyres != null && rearTyres != null && aeroKit != null;
    }

    public boolean validateConfiguration() {
        return isFullyConfigured();
    }

    public String getBasicInfo() {
        return String.format("Car #%d: %s | Weight: %.1fkg | Configured: %b", id, name, weight, isConfigured);
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                ", isConfigured=" + isConfigured +
                ", engine=" + engine +
                ", frontTyres=" + frontTyres +
                ", rearTyres=" + rearTyres +
                ", aeroKit=" + aeroKit +
                '}';
    }
}

/**
 * Represents an aerodynamic kit for a race car.
 */
public class AeroKit {
    private String name;
    private double dragCoefficient;
    private int downforce;
    private int topSpeedImpact;

    // Constructor
    public AeroKit(String name, double dragCoefficient, int downforce, int topSpeedImpact) {
        this.name = name;
        this.dragCoefficient = dragCoefficient;
        this.downforce = downforce;
        this.topSpeedImpact = topSpeedImpact;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDragCoefficient() {
        return dragCoefficient;
    }

    public void setDragCoefficient(double dragCoefficient) {
        this.dragCoefficient = dragCoefficient;
    }

    public int getDownforce() {
        return downforce;
    }

    public void setDownforce(int downforce) {
        this.downforce = downforce;
    }

    public int getTopSpeedImpact() {
        return topSpeedImpact;
    }

    public void setTopSpeedImpact(int topSpeedImpact) {
        this.topSpeedImpact = topSpeedImpact;
    }

    // Return a simple rating for aerodynamic effectiveness
    public int getAeroRating() {
        return (int)(downforce - dragCoefficient * 100);
    }

    // Classify the type of aero kit
    public String getKitType() {
        if (dragCoefficient <= 0.28 && topSpeedImpact >= 270) {
            return "High Speed";
        } else if (downforce >= 400) {
            return "High Downforce";
        } else {
            return "Balanced";
        }
    }

    // Factory methods
    public static AeroKit createStandardKit() {
        return new AeroKit("Standard Kit", 0.30, 200, 250);
    }

    public static AeroKit createHighDownforceKit() {
        return new AeroKit("High Downforce Kit", 0.35, 350, 220);
    }

    public static AeroKit createLowDragKit() {
        return new AeroKit("Low Drag Kit", 0.25, 150, 280);
    }

    public static AeroKit createAdjustableKit() {
        return new AeroKit("Adjustable Kit", 0.30, 250, 240);
    }

    public static AeroKit createGroundEffectKit() {
        return new AeroKit("Ground Effect Kit", 0.27, 400, 240);
    }

    public static AeroKit createExtremeAeroKit() {
        return new AeroKit("Extreme Aero Kit", 0.40, 500, 200);
    }

    @Override
    public String toString() {
        return "AeroKit{" +
                "name='" + name + '\'' +
                ", dragCoefficient=" + dragCoefficient +
                ", downforce=" + downforce +
                ", topSpeedImpact=" + topSpeedImpact +
                '}';
    }
}

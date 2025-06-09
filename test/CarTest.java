public class CarTest {
    public static void main(String[] args) {
        // Create sample components
        Engine engine = Engine.createStandardEngine();
        Tyre front = Tyre.createMediumTyre();
        Tyre rear = Tyre.createMediumTyre();
        AeroKit kit = AeroKit.createStandardKit();

        // Create car with components (no isConfigured boolean needed)
        Car myCar = new Car(1, "Falcon GT", 950.0, engine, front, rear, kit);

        // Run checks and print info
        System.out.println(myCar.getBasicInfo());
        System.out.println("Total Weight: " + myCar.getTotalWeight());
        System.out.println("Is fully configured: " + myCar.isFullyConfigured());
        System.out.println("Validation passed: " + myCar.validateConfiguration());
        System.out.println(myCar.toString());
    }
}

public class EngineTest {

    public static void main(String[] args) {
        // Create a standard engine using static factory method
        Engine standard = Engine.createStandardEngine();
        System.out.println("Standard Engine Info:");
        System.out.println(standard.toString());
        System.out.println("Power-to-Weight Ratio: " + standard.calculatePowerToWeight());

        System.out.println("----------------------------------------");

        // Create a turbo engine using static factory method
        Engine turbo = Engine.createTurboEngine();
        System.out.println("Turbo Engine Info:");
        System.out.println(turbo.toString());
        System.out.println("Power-to-Weight Ratio: " + turbo.calculatePowerToWeight());
    }
}

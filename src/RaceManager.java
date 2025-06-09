import java.util.Scanner;

public class RaceManager {
    private static Car car;
    private static Track track;
    private static Performance performance;
    private static RaceStrategy strategy;

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getUserChoice(1, 6);
            switch (choice) {
                case 1 -> createCar();
                case 2 -> selectTrack();
                case 3 -> calculatePerformance();
                case 4 -> chooseStrategy();
                case 5 -> showResults();
                case 6 -> {
                    System.out.println("Exiting... Goodbye!");
                    running = false;
                }
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n=== Race Strategy Manager ===");
        System.out.println("1. Create New Car");
        System.out.println("2. Select Track");
        System.out.println("3. Calculate Performance");
        System.out.println("4. Choose Strategy");
        System.out.println("5. Show Results");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice(int min, int max) {
        int input;
        while (true) {
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.print("Invalid choice. Try again: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a number: ");
            }
        }
    }

    private static void createCar() {
        System.out.println("\nChoose Engine:");
        System.out.println("1. Standard");
        System.out.println("2. Turbocharged");
        Engine engine = switch (getUserChoice(1, 2)) {
            case 1 -> Engine.createStandardEngine();
            case 2 -> Engine.createTurboEngine();
            default -> throw new IllegalStateException("Unexpected value");
        };

        System.out.println("Choose Tyre Compound for Front and Rear:");
        System.out.println("1. Soft\n2. Medium\n3. Hard");
        Tyre tyre = switch (getUserChoice(1, 3)) {
            case 1 -> Tyre.createSoftTyre();
            case 2 -> Tyre.createMediumTyre();
            case 3 -> Tyre.createHardTyre();
            default -> throw new IllegalStateException("Unexpected value");
        };

        System.out.println("Choose AeroKit:");
        var kits = AeroKitFactory.getAllAvailableKits();
        for (int i = 0; i < kits.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, kits.get(i).getName());
        }
        AeroKit selectedKit = kits.get(getUserChoice(1, kits.size()) - 1);

        car = new Car(1, "Custom Racer", 950.0, engine, tyre, tyre, selectedKit);
        System.out.println("Car created successfully!\n" + car);
    }

    private static void selectTrack() {
        System.out.println("\nSelect Track:");
        System.out.println("1. Monaco\n2. Monza\n3. Silverstone");
        track = switch (getUserChoice(1, 3)) {
            case 1 -> Track.createMonacoTrack();
            case 2 -> Track.createMonzaTrack();
            case 3 -> Track.createSilverstoneTrack();
            default -> throw new IllegalStateException("Unexpected value");
        };

        System.out.println("Track selected:\n" + track);

        AeroKit recommendedKit = PerformanceCalculator.getBestKitForTrack(track);
        System.out.println("Recommended AeroKit: " + recommendedKit.getName());
        System.out.println("Kit Type: " + recommendedKit.getKitType());
    }

    private static void calculatePerformance() {
        if (car == null || track == null) {
            System.out.println("Please create a car and select a track first.");
            return;
        }
        performance = PerformanceCalculator.createCarPerformance(car, track);
        System.out.println("Performance calculated:\n" + performance);
    }

    private static void chooseStrategy() {
        System.out.println("\nChoose Strategy:");
        System.out.println("1. Aggressive\n2. Balanced\n3. Conservative");
        strategy = switch (getUserChoice(1, 3)) {
            case 1 -> RaceStrategy.createAggressiveStrategy();
            case 2 -> RaceStrategy.createBalancedStrategy();
            case 3 -> RaceStrategy.createConservativeStrategy();
            default -> throw new IllegalStateException("Unexpected value");
        };
        System.out.println("Strategy selected:\n" + strategy);
    }

    private static void showResults() {
        System.out.println("\n=== Current Setup ===");
        System.out.println("Car: " + (car != null ? car : "Not created"));
        System.out.println("Track: " + (track != null ? track : "Not selected"));
        System.out.println("Performance: " + (performance != null ? performance : "Not calculated"));
        System.out.println("Strategy: " + (strategy != null ? strategy : "Not selected"));
    }
}

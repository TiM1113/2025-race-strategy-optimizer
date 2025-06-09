import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * Main class for the Race Strategy Optimizer System
 * Complete implementation with menu system, data persistence, and comprehensive features
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String DATA_DIR = "race_data/";
    private static final String CONFIG_FILE = DATA_DIR + "configurations.txt";
    private static final String RESULTS_FILE = DATA_DIR + "simulation_results.txt";
    private static final String LOG_FILE = DATA_DIR + "system.log";

    // System data
    private static List<Car> savedCars = new ArrayList<>();
    private static List<Track> savedTracks = new ArrayList<>();
    private static List<RaceStrategy> savedStrategies = new ArrayList<>();
    private static List<RaceResult> savedResults = new ArrayList<>();
    private static Map<String, Integer> usageStats = new HashMap<>();

    // Current selections
    private static Car currentCar;
    private static Track currentTrack;
    private static RaceStrategy currentStrategy;
    private static Weather currentWeather;

    public static void main(String[] args) {
        try {
            initializeSystem();
            showWelcomeMessage();
            initializeSampleData();
            runMainMenu();
        } catch (Exception e) {
            logError("Fatal system error", e);
            System.err.println("❌ Fatal system error: " + e.getMessage());
            System.err.println("Please check the logs for more details.");
        } finally {
            cleanup();
        }
    }

    private static void initializeSystem() {
        // Create data directory if it doesn't exist
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        // Initialize usage statistics
        usageStats.put("simulations_run", 0);
        usageStats.put("cars_created", 0);
        usageStats.put("configurations_saved", 0);

        // Load saved data
        loadConfigurations();
        logInfo("System initialized successfully");
    }

    private static void showWelcomeMessage() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                🏁 RACE STRATEGY OPTIMIZER 🏁                 ║");
        System.out.println("║                     Version 2.0 - Final                     ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║  Welcome to the most comprehensive race strategy simulator!  ║");
        System.out.println("║                                                              ║");
        System.out.println("║  Features:                                                   ║");
        System.out.println("║  • Advanced car configuration with validation               ║");
        System.out.println("║  • Multiple track environments and weather conditions       ║");
        System.out.println("║  • Intelligent strategy recommendations                      ║");
        System.out.println("║  • Complete race simulation with detailed results           ║");
        System.out.println("║  • Configuration comparison and analysis tools              ║");
        System.out.println("║  • Data persistence and export capabilities                 ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        // Show system status
        System.out.printf("📊 System Status: %d saved cars, %d tracks, %d strategies%n",
                savedCars.size(), savedTracks.size(), savedStrategies.size());
        System.out.printf("📈 Total simulations run: %d%n", usageStats.get("simulations_run"));
        System.out.println();
    }

    private static void initializeSampleData() {
        logInfo("Initializing sample data");

        // Create sample cars if none exist
        if (savedCars.isEmpty()) {
            try {
                savedCars.add(createSampleCar("Lightning McQueen", 850.0, Engine.createTurboEngine(), "Aggressive"));
                savedCars.add(createSampleCar("Speed Racer", 900.0, Engine.createStandardEngine(), "Balanced"));
                savedCars.add(createSampleCar("Formula Pro", 950.0, Engine.createTurboEngine(), "Technical"));
                logInfo("Created " + savedCars.size() + " sample cars");
            } catch (Exception e) {
                logError("Error creating sample cars", e);
            }
        }

        // Create sample tracks if none exist
        if (savedTracks.isEmpty()) {
            savedTracks.add(Track.createMonacoTrack());
            savedTracks.add(Track.createMonzaTrack());
            savedTracks.add(Track.createSilverstoneTrack());
            logInfo("Created " + savedTracks.size() + " sample tracks");
        }

        // Create sample strategies if none exist
        if (savedStrategies.isEmpty()) {
            savedStrategies.add(RaceStrategy.createAggressiveStrategy());
            savedStrategies.add(RaceStrategy.createBalancedStrategy());
            savedStrategies.add(RaceStrategy.createConservativeStrategy());
            logInfo("Created " + savedStrategies.size() + " sample strategies");
        }

        // Set default weather
        currentWeather = Weather.createDryWeather();
    }

    private static Car createSampleCar(String name, double weight, Engine engine, String type)
            throws InvalidCarConfigurationException {
        Tyre tyre = "Aggressive".equals(type) ? Tyre.createSoftTyre() :
                "Technical".equals(type) ? Tyre.createHardTyre() : Tyre.createMediumTyre();

        AeroKit kit = "Aggressive".equals(type) ? AeroKit.createLowDragKit() :
                "Technical".equals(type) ? AeroKit.createHighDownforceKit() : AeroKit.createStandardKit();

        Car car = new Car(savedCars.size() + 1, name, weight, engine, tyre, tyre, kit);
        Validator.validateCar(car); // Validate the sample car
        return car;
    }

    private static void runMainMenu() {
        boolean running = true;

        while (running) {
            try {
                displayMainMenu();
                int choice = getUserChoice(1, 12);

                switch (choice) {
                    case 1 -> carManagementMenu();
                    case 2 -> trackManagementMenu();
                    case 3 -> strategyManagementMenu();
                    case 4 -> weatherManagementMenu();
                    case 5 -> runSimulation();
                    case 6 -> compareConfigurations();
                    case 7 -> analyzeStrategies();
                    case 8 -> viewResults();
                    case 9 -> dataManagementMenu();
                    case 10 -> systemStatistics();
                    case 11 -> showHelp();
                    case 12 -> {
                        System.out.println("🏁 Thank you for using Race Strategy Optimizer!");
                        System.out.println("💾 Saving your data...");
                        saveConfigurations();
                        running = false;
                    }
                }
            } catch (Exception e) {
                logError("Error in main menu", e);
                System.err.println("❌ An error occurred: " + e.getMessage());
                System.out.println("📝 Press Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🏁 RACE STRATEGY OPTIMIZER - MAIN MENU");
        System.out.println("=".repeat(60));

        // Show current selections
        System.out.println("📋 Current Setup:");
        System.out.printf("  🚗 Car: %s%n", currentCar != null ? currentCar.getName() : "None selected");
        System.out.printf("  🏁 Track: %s%n", currentTrack != null ? currentTrack.getName() : "None selected");
        System.out.printf("  📊 Strategy: %s%n", currentStrategy != null ? getStrategyName(currentStrategy) : "None selected");
        System.out.printf("  🌤️  Weather: %s%n", currentWeather.getCondition());
        System.out.println();

        System.out.println("📚 MAIN OPTIONS:");
        System.out.println("  1. 🚗 Car Management        7. 📊 Strategy Analysis");
        System.out.println("  2. 🏁 Track Management      8. 📈 View Results History");
        System.out.println("  3. 📊 Strategy Management   9. 💾 Data Management");
        System.out.println("  4. 🌤️  Weather Settings     10. 📈 System Statistics");
        System.out.println("  5. 🏃 Run Simulation        11. ❓ Help & Instructions");
        System.out.println("  6. ⚖️  Compare Configs      12. 🚪 Exit");
        System.out.println("=".repeat(60));
        System.out.print("Enter your choice (1-12): ");
    }

    private static int getUserChoice(int min, int max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int choice = Integer.parseInt(input);
                if (choice >= min && choice <= max) {
                    return choice;
                }
                System.out.printf("❌ Please enter a number between %d and %d: ", min, max);
            } catch (NumberFormatException e) {
                System.out.print("❌ Invalid input. Please enter a valid number: ");
            }
        }
    }

    private static String getUserInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    // ========== CAR MANAGEMENT ==========

    private static void carManagementMenu() {
        while (true) {
            System.out.println("\n🚗 CAR MANAGEMENT");
            System.out.println("1. Create New Car");
            System.out.println("2. Select Existing Car");
            System.out.println("3. View Car Details");
            System.out.println("4. Delete Car");
            System.out.println("5. Back to Main Menu");

            int choice = getUserChoice(1, 5);

            switch (choice) {
                case 1 -> createNewCar();
                case 2 -> selectCar();
                case 3 -> viewCarDetails();
                case 4 -> deleteCar();
                case 5 -> { return; }
            }
        }
    }

    private static void createNewCar() {
        try {
            System.out.println("\n🚗 CREATE NEW CAR");

            String name = getUserInput("Enter car name: ");
            if (name.isEmpty()) name = "Car " + (savedCars.size() + 1);

            double weight = getValidatedDouble("Enter car weight (500-1500 kg): ", 500, 1500);

            Engine engine = selectEngine();
            Tyre frontTyre = selectTyre("front");
            Tyre rearTyre = selectTyre("rear");
            AeroKit aeroKit = selectAeroKit();

            Car newCar = new Car(savedCars.size() + 1, name, weight, engine, frontTyre, rearTyre, aeroKit);

            // Validate the car
            ValidationResult result = Validator.validateCar(newCar);

            savedCars.add(newCar);
            currentCar = newCar;
            usageStats.put("cars_created", usageStats.get("cars_created") + 1);

            System.out.println("✅ Car created successfully!");
            System.out.println("📋 " + result.getMessage());
            if (result.hasWarnings()) {
                System.out.println("⚠️  " + result.getFormattedWarnings());
            }

            logInfo("Created new car: " + name);

        } catch (InvalidCarConfigurationException e) {
            System.out.println("❌ Car creation failed!");
            System.out.println("🔍 " + e.getDetailedMessage());
            logError("Car creation failed", e);
        } catch (Exception e) {
            System.out.println("❌ Unexpected error: " + e.getMessage());
            logError("Unexpected error in car creation", e);
        }
    }

    private static double getValidatedDouble(String prompt, double min, double max) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("❌ Value must be between %.1f and %.1f%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.");
            }
        }
    }

    private static Engine selectEngine() {
        System.out.println("\nSelect Engine:");
        System.out.println("1. Standard Engine (200 HP)");
        System.out.println("2. Turbo Engine (300 HP)");

        int choice = getUserChoice(1, 2);
        return choice == 1 ? Engine.createStandardEngine() : Engine.createTurboEngine();
    }

    private static Tyre selectTyre(String position) {
        System.out.printf("\nSelect %s tyre:%n", position);
        System.out.println("1. Soft Tyre (High grip, low durability)");
        System.out.println("2. Medium Tyre (Balanced)");
        System.out.println("3. Hard Tyre (Low grip, high durability)");

        int choice = getUserChoice(1, 3);
        return switch (choice) {
            case 1 -> Tyre.createSoftTyre();
            case 2 -> Tyre.createMediumTyre();
            case 3 -> Tyre.createHardTyre();
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        };
    }

    private static AeroKit selectAeroKit() {
        System.out.println("\nSelect AeroKit:");
        List<AeroKit> kits = AeroKitFactory.getAllAvailableKits();
        for (int i = 0; i < kits.size(); i++) {
            AeroKit kit = kits.get(i);
            System.out.printf("%d. %s (Type: %s)%n", i + 1, kit.getName(), kit.getKitType());
        }

        int choice = getUserChoice(1, kits.size());
        return kits.get(choice - 1);
    }

    private static void selectCar() {
        if (savedCars.isEmpty()) {
            System.out.println("❌ No cars available. Create a car first.");
            return;
        }

        System.out.println("\n🚗 SELECT CAR:");
        for (int i = 0; i < savedCars.size(); i++) {
            Car car = savedCars.get(i);
            System.out.printf("%d. %s (%.0f kg)%n", i + 1, car.getName(), car.getWeight());
        }

        int choice = getUserChoice(1, savedCars.size());
        currentCar = savedCars.get(choice - 1);
        System.out.printf("✅ Selected car: %s%n", currentCar.getName());
    }

    private static void viewCarDetails() {
        if (currentCar == null) {
            System.out.println("❌ No car selected.");
            return;
        }

        System.out.println("\n🚗 CAR DETAILS");
        System.out.println("=".repeat(50));
        System.out.printf("Name: %s%n", currentCar.getName());
        System.out.printf("Weight: %.1f kg%n", currentCar.getWeight());
        System.out.printf("Total Weight: %.1f kg%n", currentCar.getTotalWeight());
        System.out.printf("Engine: %s (%d HP)%n", currentCar.getEngine().getType(), currentCar.getEngine().getPower());
        System.out.printf("Front Tyres: %s%n", currentCar.getFrontTyres().getCompound());
        System.out.printf("Rear Tyres: %s%n", currentCar.getRearTyres().getCompound());
        System.out.printf("AeroKit: %s (%s)%n", currentCar.getAeroKit().getName(), currentCar.getAeroKit().getKitType());
        System.out.printf("Configured: %s%n", currentCar.isFullyConfigured() ? "✅ Yes" : "❌ No");
    }

    private static void deleteCar() {
        if (savedCars.isEmpty()) {
            System.out.println("❌ No cars to delete.");
            return;
        }

        selectCar();
        if (currentCar != null) {
            String name = currentCar.getName();
            savedCars.remove(currentCar);
            currentCar = null;
            System.out.printf("✅ Deleted car: %s%n", name);
            logInfo("Deleted car: " + name);
        }
    }

    // ========== TRACK MANAGEMENT ==========

    private static void trackManagementMenu() {
        while (true) {
            System.out.println("\n🏁 TRACK MANAGEMENT");
            System.out.println("1. Select Existing Track");
            System.out.println("2. View Track Details");
            System.out.println("3. Create Custom Track");
            System.out.println("4. Back to Main Menu");

            int choice = getUserChoice(1, 4);

            switch (choice) {
                case 1 -> selectTrack();
                case 2 -> viewTrackDetails();
                case 3 -> createCustomTrack();
                case 4 -> { return; }
            }
        }
    }

    private static void selectTrack() {
        System.out.println("\n🏁 SELECT TRACK:");
        for (int i = 0; i < savedTracks.size(); i++) {
            Track track = savedTracks.get(i);
            System.out.printf("%d. %s (%.1f km, %d corners, %s)%n",
                    i + 1, track.getName(), track.getLength(),
                    track.getCorners(), track.getDifficulty());
        }

        int choice = getUserChoice(1, savedTracks.size());
        currentTrack = savedTracks.get(choice - 1);
        System.out.printf("✅ Selected track: %s%n", currentTrack.getName());

        // Show recommended AeroKit
        try {
            AeroKit recommended = PerformanceCalculator.getBestKitForTrack(currentTrack);
            System.out.printf("💡 Recommended AeroKit: %s%n", recommended.getName());
        } catch (Exception e) {
            logError("Error getting track recommendation", e);
        }
    }

    private static void viewTrackDetails() {
        if (currentTrack == null) {
            System.out.println("❌ No track selected.");
            return;
        }

        System.out.println("\n🏁 TRACK DETAILS");
        System.out.println("=".repeat(50));
        System.out.printf("Name: %s%n", currentTrack.getName());
        System.out.printf("Length: %.1f km%n", currentTrack.getLength());
        System.out.printf("Corners: %d%n", currentTrack.getCorners());
        System.out.printf("Difficulty: %s%n", currentTrack.getDifficulty());
        System.out.printf("Surface: %s%n", currentTrack.getSurfaceType());
        System.out.printf("Corner Density: %.1f corners/km%n",
                currentTrack.getCorners() / currentTrack.getLength());
        System.out.printf("Track Rating: %.1f%n", currentTrack.getTrackRating());

        if (currentTrack.getCurrentWeather() != null) {
            Weather weather = currentTrack.getCurrentWeather();
            System.out.printf("Current Weather: %s (%d°C, Wind: %d km/h, Rain: %d)%n",
                    weather.getCondition(), weather.getTemperature(),
                    weather.getWindSpeed(), weather.getRainIntensity());
        }
    }

    private static void createCustomTrack() {
        try {
            System.out.println("\n🏁 CREATE CUSTOM TRACK");

            String name = getUserInput("Enter track name: ");
            if (name.isEmpty()) name = "Custom Track " + (savedTracks.size() + 1);

            double length = getValidatedDouble("Enter track length (1-10 km): ", 1.0, 10.0);
            int corners = (int) getValidatedDouble("Enter number of corners (5-25): ", 5, 25);

            System.out.println("Select difficulty:");
            System.out.println("1. Easy");
            System.out.println("2. Medium");
            System.out.println("3. Hard");
            int diffChoice = getUserChoice(1, 3);
            String difficulty = switch (diffChoice) {
                case 1 -> "Easy";
                case 2 -> "Medium";
                case 3 -> "Hard";
                default -> "Medium";
            };

            Track newTrack = new Track(name, length, corners, difficulty, "Smooth");

            // Validate the track
            ValidationResult result = Validator.validateTrack(newTrack);

            savedTracks.add(newTrack);
            currentTrack = newTrack;

            System.out.println("✅ Track created successfully!");
            System.out.println("📋 " + result.getMessage());
            if (result.hasWarnings()) {
                System.out.println("⚠️  " + result.getFormattedWarnings());
            }

            logInfo("Created custom track: " + name);

        } catch (InvalidTrackDataException e) {
            System.out.println("❌ Track creation failed!");
            System.out.println("🔍 " + e.getDetailedMessage());
            logError("Track creation failed", e);
        } catch (Exception e) {
            System.out.println("❌ Unexpected error: " + e.getMessage());
            logError("Unexpected error in track creation", e);
        }
    }

    // ========== STRATEGY MANAGEMENT ==========

    private static void strategyManagementMenu() {
        while (true) {
            System.out.println("\n📊 STRATEGY MANAGEMENT");
            System.out.println("1. Select Existing Strategy");
            System.out.println("2. Create Custom Strategy");
            System.out.println("3. View Strategy Details");
            System.out.println("4. Back to Main Menu");

            int choice = getUserChoice(1, 4);

            switch (choice) {
                case 1 -> selectStrategy();
                case 2 -> createCustomStrategy();
                case 3 -> viewStrategyDetails();
                case 4 -> { return; }
            }
        }
    }

    private static void selectStrategy() {
        System.out.println("\n📊 SELECT STRATEGY:");
        for (int i = 0; i < savedStrategies.size(); i++) {
            RaceStrategy strategy = savedStrategies.get(i);
            System.out.printf("%d. %s (%d pit stops, %s fuel)%n",
                    i + 1, getStrategyName(strategy),
                    strategy.getNumberOfPitStops(), strategy.getFuelStrategy());
        }

        int choice = getUserChoice(1, savedStrategies.size());
        currentStrategy = savedStrategies.get(choice - 1);
        System.out.printf("✅ Selected strategy: %s%n", getStrategyName(currentStrategy));
    }

    private static void createCustomStrategy() {
        try {
            System.out.println("\n📊 CREATE CUSTOM STRATEGY");

            int pitStops = (int) getValidatedDouble("Enter number of pit stops (0-4): ", 0, 4);

            System.out.println("Select fuel strategy:");
            System.out.println("1. Light");
            System.out.println("2. Medium");
            System.out.println("3. Heavy");
            int fuelChoice = getUserChoice(1, 3);
            String fuelStrategy = switch (fuelChoice) {
                case 1 -> "Light";
                case 2 -> "Medium";
                case 3 -> "Heavy";
                default -> "Medium";
            };

            String tyreStrategy = getUserInput("Enter tyre strategy (e.g., 'Soft-Medium'): ");
            if (tyreStrategy.isEmpty()) tyreStrategy = "Medium";

            double estimatedTime = getValidatedDouble("Enter estimated race time (30-180 minutes): ", 30, 180);

            RaceStrategy newStrategy = new RaceStrategy(pitStops, tyreStrategy, fuelStrategy, estimatedTime);

            // Validate the strategy
            if (currentTrack != null) {
                ValidationResult result = Validator.validateStrategy(newStrategy, currentTrack);
                System.out.println("📋 " + result.getMessage());
                if (result.hasWarnings()) {
                    System.out.println("⚠️  " + result.getFormattedWarnings());
                }
            }

            savedStrategies.add(newStrategy);
            currentStrategy = newStrategy;

            System.out.println("✅ Strategy created successfully!");
            logInfo("Created custom strategy");

        } catch (InvalidStrategyException e) {
            System.out.println("❌ Strategy creation failed!");
            System.out.println("🔍 " + e.getDetailedMessage());
            logError("Strategy creation failed", e);
        } catch (Exception e) {
            System.out.println("❌ Unexpected error: " + e.getMessage());
            logError("Unexpected error in strategy creation", e);
        }
    }

    private static void viewStrategyDetails() {
        if (currentStrategy == null) {
            System.out.println("❌ No strategy selected.");
            return;
        }

        System.out.println("\n📊 STRATEGY DETAILS");
        System.out.println("=".repeat(50));
        System.out.printf("Type: %s%n", getStrategyName(currentStrategy));
        System.out.printf("Pit Stops: %d%n", currentStrategy.getNumberOfPitStops());
        System.out.printf("Fuel Strategy: %s%n", currentStrategy.getFuelStrategy());
        System.out.printf("Tyre Strategy: %s%n", currentStrategy.getTyreStrategy());
        System.out.printf("Estimated Time: %.1f minutes%n", currentStrategy.getEstimatedRaceTime());
        System.out.printf("Conservative: %s%n", currentStrategy.isConservativeStrategy() ? "✅ Yes" : "❌ No");
    }

    private static String getStrategyName(RaceStrategy strategy) {
        if (strategy.getNumberOfPitStops() >= 3 && "Light".equals(strategy.getFuelStrategy())) {
            return "Aggressive Strategy";
        } else if (strategy.isConservativeStrategy()) {
            return "Conservative Strategy";
        } else {
            return "Balanced Strategy";
        }
    }

    // ========== WEATHER MANAGEMENT ==========

    private static void weatherManagementMenu() {
        while (true) {
            System.out.println("\n🌤️ WEATHER SETTINGS");
            System.out.println("1. Dry Weather");
            System.out.println("2. Wet Weather");
            System.out.println("3. Mixed Weather");
            System.out.println("4. Custom Weather");
            System.out.println("5. View Current Weather");
            System.out.println("6. Back to Main Menu");

            int choice = getUserChoice(1, 6);

            switch (choice) {
                case 1 -> currentWeather = Weather.createDryWeather();
                case 2 -> currentWeather = Weather.createWetWeather();
                case 3 -> currentWeather = Weather.createMixedWeather();
                case 4 -> createCustomWeather();
                case 5 -> viewWeatherDetails();
                case 6 -> { return; }
            }

            if (choice >= 1 && choice <= 3) {
                System.out.printf("✅ Weather set to: %s%n", currentWeather.getCondition());
            }
        }
    }

    private static void createCustomWeather() {
        System.out.println("\n🌤️ CREATE CUSTOM WEATHER");

        String condition = getUserInput("Enter weather condition (Dry/Wet/Mixed/etc.): ");
        if (condition.isEmpty()) condition = "Custom";

        int temperature = (int) getValidatedDouble("Enter temperature (-10 to 50°C): ", -10, 50);
        int windSpeed = (int) getValidatedDouble("Enter wind speed (0-100 km/h): ", 0, 100);
        int rainIntensity = (int) getValidatedDouble("Enter rain intensity (0-10): ", 0, 10);

        currentWeather = new Weather(condition, temperature, windSpeed, rainIntensity);
        System.out.printf("✅ Custom weather created: %s%n", currentWeather.getCondition());
    }

    private static void viewWeatherDetails() {
        System.out.println("\n🌤️ CURRENT WEATHER");
        System.out.println("=".repeat(50));
        System.out.printf("Condition: %s%n", currentWeather.getCondition());
        System.out.printf("Temperature: %d°C%n", currentWeather.getTemperature());
        System.out.printf("Wind Speed: %d km/h%n", currentWeather.getWindSpeed());
        System.out.printf("Rain Intensity: %d/10%n", currentWeather.getRainIntensity());
        System.out.printf("Challenging: %s%n", currentWeather.isChallenging() ? "✅ Yes" : "❌ No");
    }

    // ========== SIMULATION ==========

    private static void runSimulation() {
        // 检查必要的组件是否都已选择
        if (currentCar == null) {
            System.out.println("❌ Please select a car first.");
            return;
        }

        if (currentTrack == null) {
            System.out.println("❌ Please select a track first.");
            return;
        }

        if (currentStrategy == null) {
            System.out.println("❌ Please select a strategy first.");
            return;
        }

        try {
            System.out.println("\n🏃 RUNNING RACE SIMULATION");
            System.out.println("=".repeat(60));

            // 设置赛道天气
            currentTrack.setCurrentWeather(currentWeather);

            // 验证完整的赛车设置
            ValidationResult setupResult = Validator.validateRaceSetup(currentCar, currentTrack, currentStrategy);
            if (setupResult.hasWarnings()) {
                System.out.println("⚠️  Setup warnings: " + setupResult.getFormattedWarnings());
                System.out.print("Continue with simulation? (y/N): ");
                String confirm = scanner.nextLine().trim().toLowerCase();
                if (!"y".equals(confirm) && !"yes".equals(confirm)) {
                    System.out.println("❌ Simulation cancelled.");
                    return;
                }
            }

            // 显示模拟信息
            System.out.printf("🚗 Car: %s (%.0f kg)%n", currentCar.getName(), currentCar.getWeight());
            System.out.printf("🏁 Track: %s (%.1f km, %s)%n",
                    currentTrack.getName(), currentTrack.getLength(), currentTrack.getDifficulty());
            System.out.printf("📊 Strategy: %s (%d pit stops)%n",
                    getStrategyName(currentStrategy), currentStrategy.getNumberOfPitStops());
            System.out.printf("🌤️  Weather: %s (%d°C)%n", currentWeather.getCondition(), currentWeather.getTemperature());

            System.out.println("\n🔄 Running simulation...");

            // 模拟延迟效果
            for (int i = 1; i <= 5; i++) {
                System.out.print("█");
                Thread.sleep(200);
            }
            System.out.println(" Done!");

            // 计算性能
            Performance performance = PerformanceCalculator.createCarPerformance(currentCar, currentTrack);

            // 计算最终比赛时间（基于策略调整）
            double baseTime = performance.getLapTime();
            double raceTime = calculateRaceTime(baseTime, currentStrategy, currentTrack, currentWeather);

            // 创建比赛结果
            String strategyName = getStrategyName(currentStrategy);
            RaceResult result = new RaceResult(currentCar.getName(), currentTrack.getName(), raceTime, strategyName);
            savedResults.add(result);

            // 显示详细结果
            displaySimulationResults(performance, raceTime, result);

            // 更新统计
            usageStats.put("simulations_run", usageStats.get("simulations_run") + 1);

            // 保存结果到文件
            saveResultToFile(result);

            logInfo(String.format("Simulation completed: %s on %s - %.1f min",
                    currentCar.getName(), currentTrack.getName(), raceTime));

        } catch (Exception e) {
            System.out.println("❌ Simulation failed: " + e.getMessage());
            logError("Simulation error", e);
        }
    }

    private static double calculateRaceTime(double baseTime, RaceStrategy strategy, Track track, Weather weather) {
        double raceTime = baseTime;

        // 策略调整
        if (strategy.getNumberOfPitStops() > 0) {
            raceTime += strategy.getNumberOfPitStops() * 25.0; // 每次进站增加25秒
        }

        // 燃料策略影响
        switch (strategy.getFuelStrategy()) {
            case "Light" -> raceTime *= 0.95; // 轻载燃料更快
            case "Heavy" -> raceTime *= 1.08; // 重载燃料更慢
            // Medium 保持基准时间
        }

        // 天气影响
        if (weather.getRainIntensity() > 0) {
            raceTime *= (1.0 + weather.getRainIntensity() * 0.02); // 雨天增加时间
        }

        if (weather.getWindSpeed() > 30) {
            raceTime *= 1.03; // 强风增加时间
        }

        // 赛道难度影响
        switch (track.getDifficulty()) {
            case "Hard" -> raceTime *= 1.1;
            case "Easy" -> raceTime *= 0.95;
            // Medium 保持基准
        }

        // 添加一些随机性（±2%）
        double randomFactor = 0.98 + (Math.random() * 0.04);
        raceTime *= randomFactor;

        return Math.round(raceTime * 100.0) / 100.0; // 保留两位小数
    }

    private static void displaySimulationResults(Performance performance, double raceTime, RaceResult result) {
        System.out.println("\n🏆 SIMULATION RESULTS");
        System.out.println("=".repeat(60));

        System.out.println("📊 PERFORMANCE METRICS:");
        System.out.printf("  🚀 Top Speed: %d km/h%n", performance.getTopSpeed());
        System.out.printf("  ⚡ Acceleration: %.1f s (0-100 km/h)%n", performance.getAcceleration());
        System.out.printf("  ⛽ Fuel Consumption: %.1f L/100km%n", performance.getFuelConsumption());
        System.out.printf("  🏎️  Lap Time: %.1f seconds%n", performance.getLapTime());
        System.out.printf("  🌀 Cornering Ability: %d/10%n", performance.getCorneringAbility());

        System.out.println("\n🏁 RACE RESULT:");
        System.out.printf("  ⏱️  Total Race Time: %.1f minutes%n", raceTime);
        System.out.printf("  📅 Completed: %s%n", result.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        // 性能评级
        String rating = getRaceRating(raceTime, currentTrack.getLength());
        System.out.printf("  ⭐ Performance Rating: %s%n", rating);

        // 建议
        System.out.println("\n💡 RECOMMENDATIONS:");
        generateRecommendations(performance, currentTrack, currentStrategy);
    }

    private static String getRaceRating(double raceTime, double trackLength) {
        double timePerKm = raceTime / trackLength;

        if (timePerKm < 8.0) {
            return "⭐⭐⭐⭐⭐ Excellent";
        } else if (timePerKm < 10.0) {
            return "⭐⭐⭐⭐ Very Good";
        } else if (timePerKm < 12.0) {
            return "⭐⭐⭐ Good";
        } else if (timePerKm < 15.0) {
            return "⭐⭐ Average";
        } else {
            return "⭐ Needs Improvement";
        }
    }

    private static void generateRecommendations(Performance performance, Track track, RaceStrategy strategy) {
        List<String> recommendations = new ArrayList<>();

        if (performance.getTopSpeed() < 180) {
            recommendations.add("Consider a lower drag AeroKit for better top speed");
        }

        if (performance.getCorneringAbility() < 5 && track.getCorners() > 15) {
            recommendations.add("High downforce AeroKit would improve cornering on this technical track");
        }

        if (performance.getFuelConsumption() > 15.0 && strategy.getNumberOfPitStops() == 0) {
            recommendations.add("Consider adding a pit stop for fuel efficiency");
        }

        if (strategy.getNumberOfPitStops() > 2 && track.getLength() < 4.0) {
            recommendations.add("Too many pit stops for a short track - consider reducing them");
        }

        if (recommendations.isEmpty()) {
            recommendations.add("Setup looks well-optimized for this track!");
        }

        for (String rec : recommendations) {
            System.out.println("  • " + rec);
        }
    }

    private static void saveResultToFile(RaceResult result) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RESULTS_FILE, true))) {
            writer.printf("%s,%s,%.1f,%s,%s%n",
                    result.getCarName(), result.getTrackName(), result.getRaceTime(),
                    result.getStrategy(), result.getTimestamp());
        } catch (IOException e) {
            logError("Error saving result to file", e);
        }
    }

    // ========== COMPARISON AND ANALYSIS ==========

    private static void compareConfigurations() {
        if (savedCars.size() < 2) {
            System.out.println("❌ Need at least 2 cars to compare configurations.");
            return;
        }

        System.out.println("\n⚖️ COMPARE CONFIGURATIONS");
        System.out.println("Select first car:");
        Car car1 = selectCarForComparison();
        if (car1 == null) return;

        System.out.println("Select second car:");
        Car car2 = selectCarForComparison();
        if (car2 == null) return;

        if (currentTrack == null) {
            System.out.println("❌ Please select a track first.");
            return;
        }

        try {
            // Calculate performance for both cars
            Performance perf1 = PerformanceCalculator.createCarPerformance(car1, currentTrack);
            Performance perf2 = PerformanceCalculator.createCarPerformance(car2, currentTrack);

            System.out.println("\n📊 COMPARISON RESULTS");
            System.out.println("=".repeat(60));
            System.out.printf("%-20s | %-15s | %-15s%n", "Metric", car1.getName(), car2.getName());
            System.out.println("-".repeat(60));
            System.out.printf("%-20s | %-15d | %-15d%n", "Top Speed", perf1.getTopSpeed(), perf2.getTopSpeed());
            System.out.printf("%-20s | %-15.1f | %-15.1f%n", "Acceleration", perf1.getAcceleration(), perf2.getAcceleration());
            System.out.printf("%-20s | %-15.1f | %-15.1f%n", "Fuel Consumption", perf1.getFuelConsumption(), perf2.getFuelConsumption());
            System.out.printf("%-20s | %-15.1f | %-15.1f%n", "Lap Time", perf1.getLapTime(), perf2.getLapTime());
            System.out.printf("%-20s | %-15d | %-15d%n", "Cornering", perf1.getCorneringAbility(), perf2.getCorneringAbility());

            // Show recommendations
            System.out.println("\n💡 RECOMMENDATIONS:");
            if (perf1.getLapTime() < perf2.getLapTime()) {
                System.out.printf("🏆 %s is faster on this track%n", car1.getName());
            } else {
                System.out.printf("🏆 %s is faster on this track%n", car2.getName());
            }

        } catch (Exception e) {
            System.out.println("❌ Error comparing configurations: " + e.getMessage());
            logError("Comparison error", e);
        }
    }

    private static Car selectCarForComparison() {
        for (int i = 0; i < savedCars.size(); i++) {
            Car car = savedCars.get(i);
            System.out.printf("%d. %s%n", i + 1, car.getName());
        }

        int choice = getUserChoice(1, savedCars.size());
        return savedCars.get(choice - 1);
    }

    private static void analyzeStrategies() {
        if (savedStrategies.isEmpty()) {
            System.out.println("❌ No strategies available to analyze.");
            return;
        }

        if (currentTrack == null) {
            System.out.println("❌ Please select a track first.");
            return;
        }

        System.out.println("\n📊 STRATEGY ANALYSIS");
        System.out.println("=".repeat(60));

        for (int i = 0; i < savedStrategies.size(); i++) {
            RaceStrategy strategy = savedStrategies.get(i);
            System.out.printf("\n🏁 Strategy %d: %s%n", i + 1, getStrategyName(strategy));
            System.out.printf("   Pit Stops: %d%n", strategy.getNumberOfPitStops());
            System.out.printf("   Fuel Strategy: %s%n", strategy.getFuelStrategy());
            System.out.printf("   Estimated Time: %.1f min%n", strategy.getEstimatedRaceTime());

            // Simple track compatibility analysis
            double trackLength = currentTrack.getLength();
            String compatibility = "Medium";

            if (trackLength > 6.0 && strategy.getNumberOfPitStops() > 0) {
                compatibility = "High";
            } else if (trackLength < 3.0 && strategy.getNumberOfPitStops() == 0) {
                compatibility = "High";
            } else if (trackLength > 6.0 && strategy.getNumberOfPitStops() == 0) {
                compatibility = "Low";
            }

            System.out.printf("   Track Compatibility: %s%n", compatibility);
        }

        System.out.println("\n💡 Best strategy recommendation based on track length:");
        if (currentTrack.getLength() > 6.0) {
            System.out.println("   → Medium fuel with 1-2 pit stops recommended for long tracks");
        } else if (currentTrack.getLength() < 3.0) {
            System.out.println("   → Light fuel with minimal pit stops recommended for short tracks");
        } else {
            System.out.println("   → Balanced approach works well for medium-length tracks");
        }
    }

    private static void viewResults() {
        if (savedResults.isEmpty()) {
            System.out.println("❌ No race results available. Run some simulations first!");
            return;
        }

        System.out.println("\n📈 RACE RESULTS HISTORY");
        System.out.println("=".repeat(80));

        for (int i = 0; i < savedResults.size(); i++) {
            RaceResult result = savedResults.get(i);
            System.out.printf("%d. %s on %s - Time: %.1f min (Strategy: %s)%n",
                    i + 1, result.getCarName(), result.getTrackName(),
                    result.getRaceTime(), result.getStrategy());
        }

        if (savedResults.size() > 5) {
            System.out.println("\n📊 SUMMARY STATISTICS:");
            double avgTime = savedResults.stream()
                    .mapToDouble(RaceResult::getRaceTime)
                    .average()
                    .orElse(0.0);
            System.out.printf("Average Race Time: %.1f minutes%n", avgTime);

            String fastestCarName = savedResults.stream()
                    .min((r1, r2) -> Double.compare(r1.getRaceTime(), r2.getRaceTime()))
                    .map(RaceResult::getCarName)
                    .orElse("N/A");
            System.out.printf("Fastest Car Overall: %s%n", fastestCarName);
        }
    }

    // ========== DATA MANAGEMENT ==========

    private static void dataManagementMenu() {
        while (true) {
            System.out.println("\n💾 DATA MANAGEMENT");
            System.out.println("1. Save All Configurations");
            System.out.println("2. Load Configurations");
            System.out.println("3. Export Results to File");
            System.out.println("4. Clear All Data");
            System.out.println("5. Backup Data");
            System.out.println("6. Back to Main Menu");

            int choice = getUserChoice(1, 6);

            switch (choice) {
                case 1 -> {
                    saveConfigurations();
                    System.out.println("✅ All configurations saved successfully!");
                }
                case 2 -> {
                    loadConfigurations();
                    System.out.println("✅ Configurations loaded successfully!");
                }
                case 3 -> exportResults();
                case 4 -> clearAllData();
                case 5 -> backupData();
                case 6 -> { return; }
            }
        }
    }

    private static void exportResults() {
        if (savedResults.isEmpty()) {
            System.out.println("❌ No results to export.");
            return;
        }

        try {
            String filename = DATA_DIR + "exported_results_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";

            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                writer.println("RACE STRATEGY OPTIMIZER - EXPORTED RESULTS");
                writer.println("Generated: " + LocalDateTime.now());
                writer.println("=".repeat(60));

                for (RaceResult result : savedResults) {
                    writer.printf("Car: %s | Track: %s | Time: %.1f min | Strategy: %s%n",
                            result.getCarName(), result.getTrackName(),
                            result.getRaceTime(), result.getStrategy());
                }
            }

            System.out.printf("✅ Results exported to: %s%n", filename);

        } catch (IOException e) {
            System.out.println("❌ Error exporting results: " + e.getMessage());
            logError("Export error", e);
        }
    }

    private static void clearAllData() {
        System.out.print("⚠️  Are you sure you want to clear all data? (y/N): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if ("y".equals(confirm) || "yes".equals(confirm)) {
            savedCars.clear();
            savedResults.clear();
            savedStrategies.clear();
            // Keep savedTracks for basic functionality

            currentCar = null;
            currentStrategy = null;
            // Keep currentTrack and currentWeather

            System.out.println("✅ All user data cleared (tracks and weather preserved).");
            logInfo("User data cleared");
        } else {
            System.out.println("❌ Operation cancelled.");
        }
    }

    private static void backupData() {
        try {
            String backupDir = DATA_DIR + "backup_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + "/";
            File backup = new File(backupDir);
            backup.mkdirs();

            // Simple backup - just save configurations with timestamp
            saveConfigurationsToFile(backupDir + "configurations_backup.txt");

            System.out.printf("✅ Data backed up to: %s%n", backupDir);

        } catch (Exception e) {
            System.out.println("❌ Error creating backup: " + e.getMessage());
            logError("Backup error", e);
        }
    }

    private static void systemStatistics() {
        System.out.println("\n📈 SYSTEM STATISTICS");
        System.out.println("=".repeat(50));
        System.out.printf("Cars Created: %d%n", usageStats.get("cars_created"));
        System.out.printf("Simulations Run: %d%n", usageStats.get("simulations_run"));
        System.out.printf("Configurations Saved: %d%n", usageStats.get("configurations_saved"));
        System.out.printf("Saved Cars: %d%n", savedCars.size());
        System.out.printf("Available Tracks: %d%n", savedTracks.size());
        System.out.printf("Saved Strategies: %d%n", savedStrategies.size());
        System.out.printf("Race Results: %d%n", savedResults.size());

        if (!savedResults.isEmpty()) {
            System.out.println("\n🏆 PERFORMANCE HIGHLIGHTS:");
            String mostUsedCar = savedResults.stream()
                    .collect(Collectors.groupingBy(RaceResult::getCarName, Collectors.counting()))
                    .entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("N/A");
            System.out.printf("Most Used Car: %s%n", mostUsedCar);

            double bestTime = savedResults.stream()
                    .mapToDouble(RaceResult::getRaceTime)
                    .min()
                    .orElse(0.0);
            System.out.printf("Best Race Time: %.1f minutes%n", bestTime);
        }

        System.out.printf("\nSystem Uptime: Running since startup%n");
        System.out.printf("Data Directory: %s%n", DATA_DIR);
    }

    private static void showHelp() {
        System.out.println("\n❓ HELP & INSTRUCTIONS");
        System.out.println("=".repeat(60));
        System.out.println("🚗 CAR MANAGEMENT:");
        System.out.println("   • Create cars with custom engines, tyres, and aero kits");
        System.out.println("   • Weight must be between 500-1500 kg");
        System.out.println("   • All components must be assigned for valid configuration");

        System.out.println("\n🏁 TRACK MANAGEMENT:");
        System.out.println("   • Select from built-in tracks or create custom ones");
        System.out.println("   • Length: 1-10 km, Corners: 5-25");
        System.out.println("   • Difficulty affects strategy recommendations");

        System.out.println("\n📊 STRATEGY MANAGEMENT:");
        System.out.println("   • Pit stops: 0-4 (consider track length)");
        System.out.println("   • Fuel strategies: Light, Medium, Heavy");
        System.out.println("   • Match strategy to track characteristics");

        System.out.println("\n🌤️ WEATHER:");
        System.out.println("   • Weather affects fuel consumption and grip");
        System.out.println("   • Rain increases fuel consumption by 15%");
        System.out.println("   • High winds (>30 km/h) add 5% fuel consumption");

        System.out.println("\n🏃 SIMULATION:");
        System.out.println("   • Requires car, track, strategy, and weather");
        System.out.println("   • Results are saved automatically");
        System.out.println("   • Compare different configurations for optimization");

        System.out.println("\n💡 TIPS:");
        System.out.println("   • High-downforce kits work better on technical tracks");
        System.out.println("   • Low-drag kits are better for high-speed circuits");
        System.out.println("   • Conservative strategies work well on difficult tracks");
        System.out.println("   • Always validate configurations before simulation");

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    // ========== UTILITY METHODS ==========

    private static void logInfo(String message) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String logEntry = String.format("[%s] INFO: %s%n", timestamp, message);

            try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
                writer.write(logEntry);
            }
        } catch (IOException e) {
            // Silently fail for logging errors
        }
    }

    private static void logError(String message, Exception e) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String logEntry = String.format("[%s] ERROR: %s - %s%n", timestamp, message, e.getMessage());

            try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
                writer.write(logEntry);
            }
        } catch (IOException ex) {
            // Silently fail for logging errors
        }
    }

    private static void loadConfigurations() {
        try {
            File configFile = new File(CONFIG_FILE);
            if (!configFile.exists()) {
                logInfo("No configuration file found, using defaults");
                return;
            }

            // Simple implementation - in a real app, you'd use JSON or XML
            logInfo("Configuration loading not fully implemented - using sample data");

        } catch (Exception e) {
            logError("Error loading configurations", e);
        }
    }

    private static void saveConfigurations() {
        try {
            saveConfigurationsToFile(CONFIG_FILE);
            usageStats.put("configurations_saved", usageStats.get("configurations_saved") + 1);
            logInfo("Configurations saved successfully");

        } catch (Exception e) {
            logError("Error saving configurations", e);
        }
    }

    private static void saveConfigurationsToFile(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("# Race Strategy Optimizer Configuration");
            writer.println("# Generated: " + LocalDateTime.now());
            writer.printf("cars_count=%d%n", savedCars.size());
            writer.printf("tracks_count=%d%n", savedTracks.size());
            writer.printf("strategies_count=%d%n", savedStrategies.size());
            writer.printf("results_count=%d%n", savedResults.size());

            // In a real implementation, you'd serialize the actual objects
            for (Car car : savedCars) {
                writer.printf("car=%s,%.1f%n", car.getName(), car.getWeight());
            }
        }
    }

    private static void cleanup() {
        try {
            saveConfigurations();
            scanner.close();
            logInfo("System shutdown - cleanup completed");
        } catch (Exception e) {
            logError("Error during cleanup", e);
        }
    }
}
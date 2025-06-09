import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * Comprehensive validation class for race components.
 */
public class Validator {

    // Constants for validation ranges
    public static final double MIN_CAR_WEIGHT = 500.0;
    public static final double MAX_CAR_WEIGHT = 1500.0;
    public static final int MIN_ENGINE_POWER = 100;
    public static final int MAX_ENGINE_POWER = 500;

    public static final double MIN_TRACK_LENGTH = 1.0;
    public static final double MAX_TRACK_LENGTH = 10.0;
    public static final int MIN_CORNER_COUNT = 5;
    public static final int MAX_CORNER_COUNT = 25;

    public static final int MIN_PIT_STOPS = 0;
    public static final int MAX_PIT_STOPS = 4;

    public static final List<String> VALID_DIFFICULTIES = Arrays.asList("Easy", "Medium", "Hard");
    public static final List<String> VALID_FUEL_STRATEGIES = Arrays.asList("Light", "Medium", "Heavy");

    /**
     * Validates car configuration and components.
     *
     * @param car The car to validate
     * @return ValidationResult containing validation status and messages
     * @throws InvalidCarConfigurationException if car configuration is invalid
     */
    public static ValidationResult validateCar(Car car) throws InvalidCarConfigurationException {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        if (car == null) {
            throw new InvalidCarConfigurationException("Car cannot be null");
        }

        // Check if all components are assigned
        if (car.getEngine() == null) {
            errors.add("Engine is not assigned");
        }

        if (car.getFrontTyres() == null) {
            errors.add("Front tyres are not assigned");
        }

        if (car.getRearTyres() == null) {
            errors.add("Rear tyres are not assigned");
        }

        if (car.getAeroKit() == null) {
            errors.add("AeroKit is not assigned");
        }

        // Validate weight
        double weight = car.getWeight();
        if (weight <= 0) {
            errors.add("Car weight must be positive");
        } else if (weight < MIN_CAR_WEIGHT) {
            errors.add(String.format("Car weight (%.1f kg) is below minimum (%.1f kg)",
                    weight, MIN_CAR_WEIGHT));
        } else if (weight > MAX_CAR_WEIGHT) {
            errors.add(String.format("Car weight (%.1f kg) exceeds maximum (%.1f kg)",
                    weight, MAX_CAR_WEIGHT));
        }

        // Validate engine power if engine is present
        if (car.getEngine() != null) {
            int power = car.getEngine().getPower();
            if (power < MIN_ENGINE_POWER) {
                errors.add(String.format("Engine power (%d HP) is below minimum (%d HP)",
                        power, MIN_ENGINE_POWER));
            } else if (power > MAX_ENGINE_POWER) {
                errors.add(String.format("Engine power (%d HP) exceeds maximum (%d HP)",
                        power, MAX_ENGINE_POWER));
            }

            // Check engine weight
            if (car.getEngine().getWeight() <= 0) {
                errors.add("Engine weight must be positive");
            }
        }

        // Check car name
        if (car.getName() == null || car.getName().trim().isEmpty()) {
            warnings.add("Car name is empty or null");
        }

        // Check total weight reasonableness
        if (car.getEngine() != null) {
            double totalWeight = car.getTotalWeight();
            if (totalWeight > MAX_CAR_WEIGHT + 200) {
                warnings.add(String.format("Total weight (%.1f kg) seems very high", totalWeight));
            }
        }

        // Throw exception if there are errors
        if (!errors.isEmpty()) {
            String errorMessage = "Car validation failed: " + String.join(", ", errors);
            throw new InvalidCarConfigurationException(errorMessage, "Multiple Components",
                    MIN_CAR_WEIGHT + "-" + MAX_CAR_WEIGHT + " kg");
        }

        return new ValidationResult(true, "Car validation passed", warnings);
    }

    /**
     * Validates track data and specifications.
     *
     * @param track The track to validate
     * @return ValidationResult containing validation status and messages
     * @throws InvalidTrackDataException if track data is invalid
     */
    public static ValidationResult validateTrack(Track track) throws InvalidTrackDataException {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        if (track == null) {
            throw new InvalidTrackDataException("Track cannot be null");
        }

        // Validate track length
        double length = track.getLength();
        if (length <= 0) {
            errors.add("Track length must be positive");
        } else if (length < MIN_TRACK_LENGTH) {
            errors.add(String.format("Track length (%.1f km) is below minimum (%.1f km)",
                    length, MIN_TRACK_LENGTH));
        } else if (length > MAX_TRACK_LENGTH) {
            errors.add(String.format("Track length (%.1f km) exceeds maximum (%.1f km)",
                    length, MAX_TRACK_LENGTH));
        }

        // Validate corner count
        int corners = track.getCorners();
        if (corners < MIN_CORNER_COUNT) {
            errors.add(String.format("Corner count (%d) is below minimum (%d)",
                    corners, MIN_CORNER_COUNT));
        } else if (corners > MAX_CORNER_COUNT) {
            errors.add(String.format("Corner count (%d) exceeds maximum (%d)",
                    corners, MAX_CORNER_COUNT));
        }

        // Validate difficulty
        String difficulty = track.getDifficulty();
        if (difficulty == null || difficulty.trim().isEmpty()) {
            errors.add("Track difficulty cannot be null or empty");
        } else if (!VALID_DIFFICULTIES.contains(difficulty)) {
            errors.add(String.format("Invalid difficulty '%s'. Valid values: %s",
                    difficulty, VALID_DIFFICULTIES));
        }

        // Check track name
        if (track.getName() == null || track.getName().trim().isEmpty()) {
            warnings.add("Track name is empty or null");
        }

        // Check reasonable corner density
        if (length > 0 && corners > 0) {
            double cornerDensity = corners / length;
            if (cornerDensity > 8) {
                warnings.add(String.format("Very high corner density (%.1f corners/km)", cornerDensity));
            } else if (cornerDensity < 1) {
                warnings.add(String.format("Very low corner density (%.1f corners/km)", cornerDensity));
            }
        }

        // Throw exception if there are errors
        if (!errors.isEmpty()) {
            String errorMessage = "Track validation failed: " + String.join(", ", errors);
            String validRanges = String.format("Length: %.1f-%.1f km, Corners: %d-%d, Difficulty: %s",
                    MIN_TRACK_LENGTH, MAX_TRACK_LENGTH,
                    MIN_CORNER_COUNT, MAX_CORNER_COUNT,
                    VALID_DIFFICULTIES);
            throw new InvalidTrackDataException(errorMessage, "Track Properties", null, validRanges);
        }

        return new ValidationResult(true, "Track validation passed", warnings);
    }

    /**
     * Validates race strategy compatibility with track.
     *
     * @param strategy The race strategy to validate
     * @param track The track to validate against
     * @return ValidationResult containing validation status and messages
     * @throws InvalidStrategyException if strategy is invalid
     */
    public static ValidationResult validateStrategy(RaceStrategy strategy, Track track)
            throws InvalidStrategyException {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        if (strategy == null) {
            throw new InvalidStrategyException("Race strategy cannot be null");
        }

        if (track == null) {
            throw new InvalidStrategyException("Track cannot be null for strategy validation");
        }

        // Validate pit stop count
        int pitStops = strategy.getNumberOfPitStops();
        if (pitStops < MIN_PIT_STOPS) {
            errors.add(String.format("Pit stop count (%d) is below minimum (%d)",
                    pitStops, MIN_PIT_STOPS));
        } else if (pitStops > MAX_PIT_STOPS) {
            errors.add(String.format("Pit stop count (%d) exceeds maximum (%d)",
                    pitStops, MAX_PIT_STOPS));
        }

        // Validate fuel strategy
        String fuelStrategy = strategy.getFuelStrategy();
        if (fuelStrategy == null || fuelStrategy.trim().isEmpty()) {
            errors.add("Fuel strategy cannot be null or empty");
        } else if (!VALID_FUEL_STRATEGIES.contains(fuelStrategy)) {
            errors.add(String.format("Invalid fuel strategy '%s'. Valid values: %s",
                    fuelStrategy, VALID_FUEL_STRATEGIES));
        }

        // Check strategy compatibility with track length
        double trackLength = track.getLength();
        if (trackLength > 6.0 && pitStops == 0) {
            warnings.add("Long track with no pit stops may be risky for fuel consumption");
        }

        if (trackLength < 3.0 && pitStops > 2) {
            warnings.add("Short track with many pit stops may not be optimal");
        }

        // Check fuel strategy vs pit stop compatibility
        if (pitStops == 0 && "Light".equals(fuelStrategy)) {
            errors.add("Light fuel strategy with 0 pit stops is not feasible");
        }

        if (pitStops >= 3 && "Heavy".equals(fuelStrategy)) {
            warnings.add("Heavy fuel strategy with many pit stops may not be optimal");
        }

        // Check track difficulty vs strategy
        String difficulty = track.getDifficulty();
        if ("Hard".equals(difficulty) && pitStops == 0) {
            warnings.add("Hard track with no pit stops may be very challenging");
        }

        // Check tyre strategy validity
        String tyreStrategy = strategy.getTyreStrategy();
        if (tyreStrategy == null || tyreStrategy.trim().isEmpty()) {
            warnings.add("Tyre strategy is not specified");
        }

        // Validate estimated race time
        double estimatedTime = strategy.getEstimatedRaceTime();
        if (estimatedTime <= 0) {
            warnings.add("Estimated race time should be positive");
        } else if (estimatedTime < 30) {
            warnings.add("Estimated race time seems very short");
        } else if (estimatedTime > 180) {
            warnings.add("Estimated race time seems very long");
        }

        // Throw exception if there are errors
        if (!errors.isEmpty()) {
            String errorMessage = "Strategy validation failed: " + String.join(", ", errors);
            String recommendedAction = generateStrategyRecommendation(track, pitStops, fuelStrategy);
            throw new InvalidStrategyException(errorMessage, fuelStrategy,
                    "Incompatible with track requirements", recommendedAction);
        }

        return new ValidationResult(true, "Strategy validation passed", warnings);
    }

    /**
     * Generates strategy recommendations based on track characteristics.
     */
    private static String generateStrategyRecommendation(Track track, int pitStops, String fuelStrategy) {
        StringBuilder recommendation = new StringBuilder();

        if (track.getLength() > 6.0) {
            recommendation.append("For long tracks, consider 1-2 pit stops with medium fuel. ");
        } else if (track.getLength() < 3.0) {
            recommendation.append("For short tracks, minimal pit stops work best. ");
        }

        if ("Hard".equals(track.getDifficulty())) {
            recommendation.append("Hard tracks benefit from conservative strategies. ");
        }

        if (track.getCorners() > 15) {
            recommendation.append("High-corner tracks may require tire changes. ");
        }

        return recommendation.toString();
    }

    /**
     * Validates a complete race setup (car + track + strategy).
     */
    public static ValidationResult validateRaceSetup(Car car, Track track, RaceStrategy strategy)
            throws InvalidCarConfigurationException, InvalidTrackDataException, InvalidStrategyException {

        List<String> allWarnings = new ArrayList<>();

        // Validate each component
        ValidationResult carResult = validateCar(car);
        ValidationResult trackResult = validateTrack(track);
        ValidationResult strategyResult = validateStrategy(strategy, track);

        // Collect all warnings
        allWarnings.addAll(carResult.getWarnings());
        allWarnings.addAll(trackResult.getWarnings());
        allWarnings.addAll(strategyResult.getWarnings());

        // Additional cross-component validations
        if (car.getEngine() != null && track != null) {
            int enginePower = car.getEngine().getPower();
            if (enginePower < 150 && "Hard".equals(track.getDifficulty())) {
                allWarnings.add("Low power engine on hard track may struggle");
            }
        }

        return new ValidationResult(true, "Complete race setup validation passed", allWarnings);
    }
}
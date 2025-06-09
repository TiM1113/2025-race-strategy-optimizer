import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test class for Validator and custom exceptions.
 */
public class ValidatorTest {

    private Car validCar;
    private Track validTrack;
    private RaceStrategy validStrategy;

    @BeforeEach
    public void setUp() {
        // Create valid test objects
        validCar = new Car(1, "TestCar", 950.0,
                Engine.createStandardEngine(),
                Tyre.createMediumTyre(),
                Tyre.createMediumTyre(),
                AeroKit.createStandardKit());

        validTrack = Track.createMonacoTrack();
        validStrategy = RaceStrategy.createBalancedStrategy();
    }

    // ========== CAR VALIDATION TESTS ==========

    @Test
    public void testValidateCar_ValidCar_ShouldPass() throws InvalidCarConfigurationException {
        // Test with a completely valid car
        ValidationResult result = Validator.validateCar(validCar);

        assertTrue(result.isValid(), "Valid car should pass validation");
        assertEquals("Car validation passed", result.getMessage());
    }

    @Test
    public void testValidateCar_NullCar_ShouldThrowException() {
        // Test with null car
        InvalidCarConfigurationException exception = assertThrows(
                InvalidCarConfigurationException.class,
                () -> Validator.validateCar(null),
                "Null car should throw exception"
        );

        assertEquals("Car cannot be null", exception.getMessage());
    }

    @Test
    public void testValidateCar_MissingEngine_ShouldThrowException() {
        // Create car without engine
        Car carWithoutEngine = new Car(1, "NoEngine", 950.0,
                null, // No engine
                Tyre.createMediumTyre(),
                Tyre.createMediumTyre(),
                AeroKit.createStandardKit());

        InvalidCarConfigurationException exception = assertThrows(
                InvalidCarConfigurationException.class,
                () -> Validator.validateCar(carWithoutEngine),
                "Car without engine should throw exception"
        );

        assertTrue(exception.getMessage().contains("Engine is not assigned"));
    }

    @Test
    public void testValidateCar_InvalidWeight_TooLow_ShouldThrowException() {
        // Create car with weight too low
        Car lightCar = new Car(1, "TooLight", 300.0, // Below minimum 500
                Engine.createStandardEngine(),
                Tyre.createMediumTyre(),
                Tyre.createMediumTyre(),
                AeroKit.createStandardKit());

        InvalidCarConfigurationException exception = assertThrows(
                InvalidCarConfigurationException.class,
                () -> Validator.validateCar(lightCar),
                "Car with too low weight should throw exception"
        );

        assertTrue(exception.getMessage().contains("below minimum"));
        assertEquals("Multiple Components", exception.getComponent());
    }

    @Test
    public void testValidateCar_InvalidWeight_TooHigh_ShouldThrowException() {
        // Create car with weight too high
        Car heavyCar = new Car(1, "TooHeavy", 2000.0, // Above maximum 1500
                Engine.createStandardEngine(),
                Tyre.createMediumTyre(),
                Tyre.createMediumTyre(),
                AeroKit.createStandardKit());

        InvalidCarConfigurationException exception = assertThrows(
                InvalidCarConfigurationException.class,
                () -> Validator.validateCar(heavyCar),
                "Car with too high weight should throw exception"
        );

        assertTrue(exception.getMessage().contains("exceeds maximum"));
    }

    @Test
    public void testValidateCar_InvalidEnginePower_ShouldThrowException() {
        // Create engine with invalid power (outside 100-500 HP range)
        Engine weakEngine = new Engine("Weak", 50, 15.0, 100.0); // Below minimum 100 HP
        Car carWithWeakEngine = new Car(1, "WeakCar", 950.0,
                weakEngine,
                Tyre.createMediumTyre(),
                Tyre.createMediumTyre(),
                AeroKit.createStandardKit());

        InvalidCarConfigurationException exception = assertThrows(
                InvalidCarConfigurationException.class,
                () -> Validator.validateCar(carWithWeakEngine),
                "Car with weak engine should throw exception"
        );

        assertTrue(exception.getMessage().contains("below minimum"));
    }

    // ========== TRACK VALIDATION TESTS ==========

    @Test
    public void testValidateTrack_ValidTrack_ShouldPass() throws InvalidTrackDataException {
        // Test with valid track
        ValidationResult result = Validator.validateTrack(validTrack);

        assertTrue(result.isValid(), "Valid track should pass validation");
        assertEquals("Track validation passed", result.getMessage());
    }

    @Test
    public void testValidateTrack_NullTrack_ShouldThrowException() {
        InvalidTrackDataException exception = assertThrows(
                InvalidTrackDataException.class,
                () -> Validator.validateTrack(null),
                "Null track should throw exception"
        );

        assertEquals("Track cannot be null", exception.getMessage());
    }

    @Test
    public void testValidateTrack_InvalidLength_TooShort_ShouldThrowException() {
        Track shortTrack = new Track("TooShort", 0.5, 10, "Medium", "Smooth"); // Below minimum 1.0 km

        InvalidTrackDataException exception = assertThrows(
                InvalidTrackDataException.class,
                () -> Validator.validateTrack(shortTrack),
                "Track too short should throw exception"
        );

        assertTrue(exception.getMessage().contains("below minimum"));
    }

    @Test
    public void testValidateTrack_InvalidLength_TooLong_ShouldThrowException() {
        Track longTrack = new Track("TooLong", 15.0, 10, "Medium", "Smooth"); // Above maximum 10.0 km

        InvalidTrackDataException exception = assertThrows(
                InvalidTrackDataException.class,
                () -> Validator.validateTrack(longTrack),
                "Track too long should throw exception"
        );

        assertTrue(exception.getMessage().contains("exceeds maximum"));
    }

    @Test
    public void testValidateTrack_InvalidCornerCount_TooFew_ShouldThrowException() {
        Track fewCornersTrack = new Track("FewCorners", 5.0, 3, "Medium", "Smooth"); // Below minimum 5

        InvalidTrackDataException exception = assertThrows(
                InvalidTrackDataException.class,
                () -> Validator.validateTrack(fewCornersTrack),
                "Track with too few corners should throw exception"
        );

        assertTrue(exception.getMessage().contains("below minimum"));
    }

    @Test
    public void testValidateTrack_InvalidDifficulty_ShouldThrowException() {
        Track invalidDifficultyTrack = new Track("Invalid", 5.0, 10, "Impossible", "Smooth");

        InvalidTrackDataException exception = assertThrows(
                InvalidTrackDataException.class,
                () -> Validator.validateTrack(invalidDifficultyTrack),
                "Track with invalid difficulty should throw exception"
        );

        assertTrue(exception.getMessage().contains("Invalid difficulty"));
    }

    @Test
    public void testValidateTrack_HighCornerDensity_ShouldGiveWarning() throws InvalidTrackDataException {
        // Create track with high corner density (many corners on short track)
        Track densTrack = new Track("Dense", 2.0, 20, "Medium", "Smooth"); // 10 corners/km

        ValidationResult result = Validator.validateTrack(densTrack);

        assertTrue(result.isValid(), "Track should still be valid");
        assertTrue(result.hasWarnings(), "Should have warnings about corner density");
        assertTrue(result.getFormattedWarnings().contains("corner density"));
    }

    // ========== STRATEGY VALIDATION TESTS ==========

    @Test
    public void testValidateStrategy_ValidStrategy_ShouldPass() throws InvalidStrategyException {
        ValidationResult result = Validator.validateStrategy(validStrategy, validTrack);

        assertTrue(result.isValid(), "Valid strategy should pass validation");
        assertEquals("Strategy validation passed", result.getMessage());
    }

    @Test
    public void testValidateStrategy_NullStrategy_ShouldThrowException() {
        InvalidStrategyException exception = assertThrows(
                InvalidStrategyException.class,
                () -> Validator.validateStrategy(null, validTrack),
                "Null strategy should throw exception"
        );

        assertEquals("Race strategy cannot be null", exception.getMessage());
    }

    @Test
    public void testValidateStrategy_NullTrack_ShouldThrowException() {
        InvalidStrategyException exception = assertThrows(
                InvalidStrategyException.class,
                () -> Validator.validateStrategy(validStrategy, null),
                "Null track should throw exception"
        );

        assertEquals("Track cannot be null for strategy validation", exception.getMessage());
    }

    @Test
    public void testValidateStrategy_TooManyPitStops_ShouldThrowException() {
        RaceStrategy manyPitStopsStrategy = new RaceStrategy(6, "Medium", "Medium", 95.0); // Above maximum 4

        InvalidStrategyException exception = assertThrows(
                InvalidStrategyException.class,
                () -> Validator.validateStrategy(manyPitStopsStrategy, validTrack),
                "Strategy with too many pit stops should throw exception"
        );

        assertTrue(exception.getMessage().contains("exceeds maximum"));
    }

    @Test
    public void testValidateStrategy_InvalidFuelStrategy_ShouldThrowException() {
        RaceStrategy invalidFuelStrategy = new RaceStrategy(2, "Medium", "Super", 95.0); // Invalid fuel strategy

        InvalidStrategyException exception = assertThrows(
                InvalidStrategyException.class,
                () -> Validator.validateStrategy(invalidFuelStrategy, validTrack),
                "Strategy with invalid fuel strategy should throw exception"
        );

        assertTrue(exception.getMessage().contains("Invalid fuel strategy"));
    }

    @Test
    public void testValidateStrategy_IncompatibleLightFuelWithNoPitStops_ShouldThrowException() {
        RaceStrategy incompatibleStrategy = new RaceStrategy(0, "Medium", "Light", 95.0); // Light fuel with 0 pit stops

        InvalidStrategyException exception = assertThrows(
                InvalidStrategyException.class,
                () -> Validator.validateStrategy(incompatibleStrategy, validTrack),
                "Light fuel with no pit stops should throw exception"
        );

        assertTrue(exception.getMessage().contains("Light fuel strategy with 0 pit stops is not feasible"));
    }

    @Test
    public void testValidateStrategy_LongTrackNoPitStops_ShouldGiveWarning() throws InvalidStrategyException {
        // Create long track and strategy with no pit stops
        Track longTrack = new Track("Long", 7.0, 15, "Medium", "Smooth");
        RaceStrategy noPitStrategy = new RaceStrategy(0, "Medium", "Heavy", 95.0);

        ValidationResult result = Validator.validateStrategy(noPitStrategy, longTrack);

        assertTrue(result.isValid(), "Strategy should still be valid");
        assertTrue(result.hasWarnings(), "Should have warnings about fuel consumption");
        assertTrue(result.getFormattedWarnings().contains("risky for fuel consumption"));
    }

    // ========== COMPLETE RACE SETUP VALIDATION TESTS ==========

    @Test
    public void testValidateRaceSetup_AllValid_ShouldPass() throws Exception {
        ValidationResult result = Validator.validateRaceSetup(validCar, validTrack, validStrategy);

        assertTrue(result.isValid(), "All valid components should pass validation");
        assertEquals("Complete race setup validation passed", result.getMessage());
    }

    @Test
    public void testValidateRaceSetup_LowPowerOnHardTrack_ShouldGiveWarning() throws Exception {
        // Create low power engine and hard track
        Engine lowPowerEngine = new Engine("Weak", 120, 15.0, 120.0);
        Car lowPowerCar = new Car(1, "WeakCar", 950.0,
                lowPowerEngine,
                Tyre.createMediumTyre(),
                Tyre.createMediumTyre(),
                AeroKit.createStandardKit());

        Track hardTrack = new Track("Hard", 5.0, 18, "Hard", "Smooth");

        ValidationResult result = Validator.validateRaceSetup(lowPowerCar, hardTrack, validStrategy);

        assertTrue(result.isValid(), "Setup should still be valid");
        assertTrue(result.hasWarnings(), "Should have warnings about low power on hard track");
        assertTrue(result.getFormattedWarnings().contains("Low power engine on hard track"));
    }

    // ========== CUSTOM EXCEPTION DETAILS TESTS ==========

    @Test
    public void testInvalidCarConfigurationException_DetailedMessage() {
        InvalidCarConfigurationException exception = new InvalidCarConfigurationException(
                "Test message", "Engine", "100-500 HP");

        String detailedMessage = exception.getDetailedMessage();

        assertTrue(detailedMessage.contains("Test message"));
        assertTrue(detailedMessage.contains("Component: Engine"));
        assertTrue(detailedMessage.contains("Expected Range: 100-500 HP"));
        assertTrue(detailedMessage.contains("Suggestion:"));
    }

    @Test
    public void testInvalidTrackDataException_DetailedMessage() {
        InvalidTrackDataException exception = new InvalidTrackDataException(
                "Invalid length", "length", 15.0, "1-10 km");

        String detailedMessage = exception.getDetailedMessage();

        assertTrue(detailedMessage.contains("Invalid length"));
        assertTrue(detailedMessage.contains("Property: length"));
        assertTrue(detailedMessage.contains("Actual Value: 15.0"));
        assertTrue(detailedMessage.contains("Valid Range: 1-10 km"));
    }

    @Test
    public void testInvalidStrategyException_DetailedMessage() {
        InvalidStrategyException exception = new InvalidStrategyException(
                "Incompatible strategy", "Light", "Too many pit stops", "Use fewer pit stops");

        String detailedMessage = exception.getDetailedMessage();

        assertTrue(detailedMessage.contains("Incompatible strategy"));
        assertTrue(detailedMessage.contains("Strategy Type: Light"));
        assertTrue(detailedMessage.contains("Conflict: Too many pit stops"));
        assertTrue(detailedMessage.contains("Recommended Action: Use fewer pit stops"));
    }

    // ========== VALIDATION RESULT TESTS ==========

    @Test
    public void testValidationResult_WithWarnings() {
        ValidationResult result = new ValidationResult(true, "Test message",
                java.util.Arrays.asList("Warning 1", "Warning 2"));

        assertTrue(result.isValid());
        assertEquals("Test message", result.getMessage());
        assertTrue(result.hasWarnings());
        assertEquals(2, result.getWarnings().size());
        assertTrue(result.getFormattedWarnings().contains("Warning 1"));
        assertTrue(result.getFormattedWarnings().contains("Warning 2"));
    }

    @Test
    public void testValidationResult_NoWarnings() {
        ValidationResult result = new ValidationResult(false, "Test message", null);

        assertFalse(result.isValid());
        assertEquals("Test message", result.getMessage());
        assertFalse(result.hasWarnings());
        assertEquals("No warnings", result.getFormattedWarnings());
    }

    // ========== EDGE CASES AND BOUNDARY TESTS ==========

    @Test
    public void testValidateCar_BoundaryWeights() throws InvalidCarConfigurationException {
        // Test minimum weight (should pass)
        Car minWeightCar = new Car(1, "MinWeight", 500.0,
                Engine.createStandardEngine(),
                Tyre.createMediumTyre(),
                Tyre.createMediumTyre(),
                AeroKit.createStandardKit());

        ValidationResult minResult = Validator.validateCar(minWeightCar);
        assertTrue(minResult.isValid(), "Minimum weight should be valid");

        // Test maximum weight (should pass)
        Car maxWeightCar = new Car(1, "MaxWeight", 1500.0,
                Engine.createStandardEngine(),
                Tyre.createMediumTyre(),
                Tyre.createMediumTyre(),
                AeroKit.createStandardKit());

        ValidationResult maxResult = Validator.validateCar(maxWeightCar);
        assertTrue(maxResult.isValid(), "Maximum weight should be valid");
    }

    @Test
    public void testValidateTrack_BoundaryValues() throws InvalidTrackDataException {
        // Test minimum valid track
        Track minTrack = new Track("MinTrack", 1.0, 5, "Easy", "Smooth");
        ValidationResult minResult = Validator.validateTrack(minTrack);
        assertTrue(minResult.isValid(), "Minimum track values should be valid");

        // Test maximum valid track
        Track maxTrack = new Track("MaxTrack", 10.0, 25, "Hard", "Smooth");
        ValidationResult maxResult = Validator.validateTrack(maxTrack);
        assertTrue(maxResult.isValid(), "Maximum track values should be valid");
    }

    @Test
    public void testValidateStrategy_BoundaryPitStops() throws InvalidStrategyException {
        // Test minimum pit stops
        RaceStrategy minPitStrategy = new RaceStrategy(0, "Medium", "Heavy", 95.0);
        ValidationResult minResult = Validator.validateStrategy(minPitStrategy, validTrack);
        assertTrue(minResult.isValid(), "Minimum pit stops should be valid");

        // Test maximum pit stops
        RaceStrategy maxPitStrategy = new RaceStrategy(4, "Medium", "Light", 95.0);
        ValidationResult maxResult = Validator.validateStrategy(maxPitStrategy, validTrack);
        assertTrue(maxResult.isValid(), "Maximum pit stops should be valid");
    }
}
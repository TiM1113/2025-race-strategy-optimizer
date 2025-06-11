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

    // 多两行
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

    // duo lianghang
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

    @Test
    public void testValidateCar_MissingFrontRearAndAeroKit_ShouldThrowException() {
        Car incompleteCar = new Car(1, "Incomplete", 950.0,
                Engine.createStandardEngine(),
                null, // Front tyres missing
                null, // Rear tyres missing
                null); // AeroKit missing

        InvalidCarConfigurationException ex = assertThrows(InvalidCarConfigurationException.class,
                () -> Validator.validateCar(incompleteCar));

        String msg = ex.getMessage();
        assertTrue(msg.contains("Front tyres are not assigned"));
        assertTrue(msg.contains("Rear tyres are not assigned"));
        assertTrue(msg.contains("AeroKit is not assigned"));
    }

    @Test
    public void testValidateCar_ZeroWeight_ShouldThrowException() {
        Car zeroWeightCar = new Car(1, "Zero", 0.0,
                Engine.createStandardEngine(),
                Tyre.createMediumTyre(),
                Tyre.createMediumTyre(),
                AeroKit.createStandardKit());

        InvalidCarConfigurationException ex = assertThrows(InvalidCarConfigurationException.class,
                () -> Validator.validateCar(zeroWeightCar));
        assertTrue(ex.getMessage().contains("Car weight must be positive"));
    }

    // ========== TRACK VALIDATION TESTS ==========

    // 很有用
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
    public void testValidateTrack_LengthZero_ShouldThrowException() {
        Track zeroLengthTrack = new Track("Zero", 0.0, 10, "Medium", "Smooth");
        InvalidTrackDataException ex = assertThrows(InvalidTrackDataException.class,
                () -> Validator.validateTrack(zeroLengthTrack));
        assertTrue(ex.getMessage().contains("Track length must be positive"));
    }

    // ========== STRATEGY VALIDATION TESTS ==========

    // important
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

    // important
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

    @Test
    public void testValidateStrategy_NegativePitStops_ShouldThrowException() {
        Track validTrackLocal = new Track("Valid", 5.0, 10, "Medium", "Smooth");
        RaceStrategy negativePit = new RaceStrategy(-1, "Medium", "Medium", 95.0);
        InvalidStrategyException ex = assertThrows(InvalidStrategyException.class,
                () -> Validator.validateStrategy(negativePit, validTrackLocal));
        assertTrue(ex.getMessage().contains("below minimum"));
    }

    @Test
    public void testValidateStrategy_NullFuelStrategy_ShouldThrowException() {
        Track validTrackLocal = new Track("Valid", 5.0, 10, "Medium", "Smooth");
        RaceStrategy nullFuel = new RaceStrategy(1, "Medium", null, 95.0);
        InvalidStrategyException ex = assertThrows(InvalidStrategyException.class,
                () -> Validator.validateStrategy(nullFuel, validTrackLocal));
        assertTrue(ex.getMessage().contains("Fuel strategy cannot be null or empty"));
    }

    @Test
    public void testValidateStrategy_VeryShortEstimatedTime_Warning() throws InvalidStrategyException {
        Track normalTrack = new Track("NormalShort", 5.0, 10, "Medium", "Smooth");
        RaceStrategy quickStrategy = new RaceStrategy(1, "Medium", "Medium", 25.0); // <30 but >0
        ValidationResult result = Validator.validateStrategy(quickStrategy, normalTrack);
        assertTrue(result.hasWarnings());
        assertTrue(result.getFormattedWarnings().contains("very short"));
    }

    // ========== COMPLETE RACE SETUP VALIDATION TESTS ==========

    // important
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

    // ========== VALIDATION RESULT TESTS ==========

    // important for validation result
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
        assertEquals("ValidationResult{isValid=true, message='Test message', warnings=2}", result.toString());
    }

    @Test
    public void testValidationResult_NoWarnings() {
        ValidationResult result = new ValidationResult(false, "Test message", null);

        assertFalse(result.isValid());
        assertEquals("Test message", result.getMessage());
        assertFalse(result.hasWarnings());
        assertEquals("No warnings", result.getFormattedWarnings());
        assertEquals("ValidationResult{isValid=false, message='Test message'}", result.toString());
    }
    // important end

    @Test
    public void testValidateStrategy_ShortTrackManyPitStopsAndHeavyFuel_Warnings() throws InvalidStrategyException {
        Track shortTrack = new Track("Shorty", 2.5, 6, "Medium", "Smooth");
        RaceStrategy strategy = new RaceStrategy(3, "Medium", "Heavy", 95.0);

        ValidationResult result = Validator.validateStrategy(strategy, shortTrack);
        assertTrue(result.isValid());
        assertTrue(result.hasWarnings());
        String warnings = result.getFormattedWarnings();
        assertTrue(warnings.contains("Short track with many pit stops"));
        assertTrue(warnings.contains("Heavy fuel strategy with many pit stops"));
    }

    @Test
    public void testValidateStrategy_TyreStrategyNullAndTimeZero_Warnings() throws InvalidStrategyException {
        Track normalTrack = new Track("Normal", 5.0, 10, "Medium", "Smooth");
        RaceStrategy strategy = new RaceStrategy(1, null, "Medium", 0.0); // time <=0 triggers warning

        ValidationResult result = Validator.validateStrategy(strategy, normalTrack);
        assertTrue(result.isValid());
        assertTrue(result.hasWarnings());
        String warnings = result.getFormattedWarnings();
        assertTrue(warnings.contains("Tyre strategy is not specified"));
        assertTrue(warnings.contains("should be positive"));
    }

    @Test
    public void testValidateStrategy_LongEstimatedTime_Warning() throws InvalidStrategyException {
        Track normalTrack = new Track("Normal2", 5.0, 10, "Medium", "Smooth");
        RaceStrategy strategy = new RaceStrategy(1, "Medium", "Medium", 200.0); // >180

        ValidationResult result = Validator.validateStrategy(strategy, normalTrack);
        assertTrue(result.hasWarnings());
        assertTrue(result.getFormattedWarnings().contains("very long"));
    }

    @Test
    public void testGenerateStrategyRecommendation_ShortTrackBranch() {
        Track shortTrack = new Track("Shorty", 2.0, 16, "Medium", "Smooth"); // length <3, corners>15
        RaceStrategy invalidStrategy = new RaceStrategy(-1, "Medium", "Light", 95.0); // Will trigger errors

        InvalidStrategyException ex = assertThrows(InvalidStrategyException.class,
                () -> Validator.validateStrategy(invalidStrategy, shortTrack));

        String rec = ex.getRecommendedAction();
        assertTrue(rec.contains("short tracks"));
        assertTrue(rec.contains("High-corner tracks"));
    }
}
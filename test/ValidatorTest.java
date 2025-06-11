import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {

    private Car validCar;
    private Track validTrack;
    private RaceStrategy validStrategy;

    @BeforeEach
    public void setUp() {
        validCar = new Car(1, "TestCar", 950.0,
                Engine.createStandardEngine(),
                Tyre.createMediumTyre(),
                Tyre.createMediumTyre(),
                AeroKit.createStandardKit());

        validTrack = Track.createMonacoTrack();
        validStrategy = RaceStrategy.createBalancedStrategy();
    }

    @Test
    public void testShouldPass() throws InvalidCarConfigurationException {
        ValidationResult result = Validator.validateCar(validCar);

        assertTrue(result.isValid());
        assertEquals("Car validation passed", result.getMessage());
    }

    @Test
    public void testShouldThrowException() {
        Car carWithoutEngine = new Car(1, "NoEngine", 950.0,
                null,
                Tyre.createMediumTyre(),
                Tyre.createMediumTyre(),
                AeroKit.createStandardKit());

        InvalidCarConfigurationException exception = assertThrows(
                InvalidCarConfigurationException.class,
                () -> Validator.validateCar(carWithoutEngine)
        );

        assertTrue(exception.getMessage().contains("Engine is not assigned"));
    }

    @ParameterizedTest(name = "Car weight {0} should throw exception")
    @ValueSource(doubles = {300.0, 2000.0})
    public void testWeightShouldThrowException(double weight) {
        Car car = new Car(1, "WeightTest", weight,
                Engine.createStandardEngine(),
                Tyre.createMediumTyre(),
                Tyre.createMediumTyre(),
                AeroKit.createStandardKit());

        InvalidCarConfigurationException ex = assertThrows(
                InvalidCarConfigurationException.class,
                () -> Validator.validateCar(car));
        if (weight < 500) {
            assertTrue(ex.getMessage().contains("below minimum"));
            assertEquals("Multiple Components", ex.getComponent());
        } else {
            assertTrue(ex.getMessage().contains("exceeds maximum"));
        }
    }

    @Test
    public void testEnginePowerShouldThrowException() {
        // too low
        Engine weakEngine = new Engine("Weak", 50, 15.0, 100.0);
        Car carWithWeakEngine = new Car(1, "WeakCar", 950.0,
                weakEngine,
                Tyre.createMediumTyre(),
                Tyre.createMediumTyre(),
                AeroKit.createStandardKit());

        InvalidCarConfigurationException exception = assertThrows(
                InvalidCarConfigurationException.class,
                () -> Validator.validateCar(carWithWeakEngine)
        );

        assertTrue(exception.getMessage().contains("below minimum"));
    }

    @Test
    public void testAeroKitShouldThrowException() {
        Car incompleteCar = new Car(1, "Incomplete", 950.0,
                Engine.createStandardEngine(),
                null,
                null,
                null);

        InvalidCarConfigurationException ex = assertThrows(InvalidCarConfigurationException.class,
                () -> Validator.validateCar(incompleteCar));

        String msg = ex.getMessage();
        assertTrue(msg.contains("Front tyres are not assigned"));
        assertTrue(msg.contains("Rear tyres are not assigned"));
        assertTrue(msg.contains("AeroKit is not assigned"));
    }

    @Test
    public void testZeroWeightShouldThrowException() {
        Car zeroWeightCar = new Car(1, "Zero", 0.0,
                Engine.createStandardEngine(),
                Tyre.createMediumTyre(),
                Tyre.createMediumTyre(),
                AeroKit.createStandardKit());

        InvalidCarConfigurationException ex = assertThrows(InvalidCarConfigurationException.class,
                () -> Validator.validateCar(zeroWeightCar));
        assertTrue(ex.getMessage().contains("Car weight must be positive"));
    }

    @Test
    public void testValidTrackShouldPass() throws InvalidTrackDataException {
        ValidationResult result = Validator.validateTrack(validTrack);

        assertTrue(result.isValid());
        assertEquals("Track validation passed", result.getMessage());
    }

    @Test
    public void testNullTrackShouldThrowException() {
        InvalidTrackDataException exception = assertThrows(
                InvalidTrackDataException.class,
                () -> Validator.validateTrack(null)
        );

        assertEquals("Track cannot be null", exception.getMessage());
    }

    @ParameterizedTest(name = "Track length {0} km should throw exception")
    @ValueSource(doubles = {0.5, 15.0})
    public void testInvalidLengthShouldThrowException(double length) {
        Track invalidTrack = new Track("InvalidLen", length, 10, "Medium", "Smooth");
        InvalidTrackDataException ex = assertThrows(
                InvalidTrackDataException.class,
                () -> Validator.validateTrack(invalidTrack));
        assertTrue(ex.getMessage().contains(length < 1.0 ? "below minimum" : "exceeds maximum"));
    }

    @Test
    public void testTooFewShouldThrowException() {
        Track fewCornersTrack = new Track("FewCorners", 5.0, 3, "Medium", "Smooth");
        InvalidTrackDataException exception = assertThrows(
                InvalidTrackDataException.class,
                () -> Validator.validateTrack(fewCornersTrack)
        );

        assertTrue(exception.getMessage().contains("below minimum"));
    }

    @Test
    public void testDifficultyShouldThrowException() {
        Track invalidDifficultyTrack = new Track("Invalid", 5.0, 10, "Impossible", "Smooth");

        InvalidTrackDataException exception = assertThrows(
                InvalidTrackDataException.class,
                () -> Validator.validateTrack(invalidDifficultyTrack)
        );

        assertTrue(exception.getMessage().contains("Invalid difficulty"));
    }

    @Test
    public void testLengthZeroShouldThrowException() {
        Track zeroLengthTrack = new Track("Zero", 0.0, 10, "Medium", "Smooth");
        InvalidTrackDataException ex = assertThrows(InvalidTrackDataException.class,
                () -> Validator.validateTrack(zeroLengthTrack));
        assertTrue(ex.getMessage().contains("Track length must be positive"));
    }

    @Test
    public void testValidStrategyShouldPass() throws InvalidStrategyException {
        ValidationResult result = Validator.validateStrategy(validStrategy, validTrack);

        assertTrue(result.isValid());
        assertEquals("Strategy validation passed", result.getMessage());
    }

    @Test
    public void testNullStrategyShouldThrowException() {
        InvalidStrategyException exception = assertThrows(
                InvalidStrategyException.class,
                () -> Validator.validateStrategy(null, validTrack)
        );

        assertEquals("Race strategy cannot be null", exception.getMessage());
    }

    @Test
    public void testValidateNullTrackShouldThrowException() {
        InvalidStrategyException exception = assertThrows(
                InvalidStrategyException.class,
                () -> Validator.validateStrategy(validStrategy, null)
        );

        assertEquals("Track cannot be null for strategy validation", exception.getMessage());
    }

    @Test
    public void testFuelStrategyShouldThrowException() {
        RaceStrategy invalidFuelStrategy = new RaceStrategy(2, "Medium", "Super", 95.0);

        InvalidStrategyException exception = assertThrows(
                InvalidStrategyException.class,
                () -> Validator.validateStrategy(invalidFuelStrategy, validTrack)
        );

        assertTrue(exception.getMessage().contains("Invalid fuel strategy"));
    }

    @Test
    public void testNoPitStopsShouldThrowException() {
        RaceStrategy incompatibleStrategy = new RaceStrategy(0, "Medium", "Light", 95.0);

        InvalidStrategyException exception = assertThrows(
                InvalidStrategyException.class,
                () -> Validator.validateStrategy(incompatibleStrategy, validTrack)
        );

        assertTrue(exception.getMessage().contains("Light fuel strategy with 0 pit stops is not feasible"));
    }

    @Test
    public void testNoPitStopsShouldWarn() throws InvalidStrategyException {
        Track longTrack = new Track("Long", 7.0, 15, "Medium", "Smooth");
        RaceStrategy noPitStrategy = new RaceStrategy(0, "Medium", "Heavy", 95.0);

        ValidationResult result = Validator.validateStrategy(noPitStrategy, longTrack);

        assertTrue(result.isValid());
        assertTrue(result.hasWarnings());
        assertTrue(result.getFormattedWarnings().contains("risky for fuel consumption"));
    }

    @ParameterizedTest(name = "Pit stops {0} should throw exception")
    @ValueSource(ints = {-1, 6})
    public void testValidateStrategy_InvalidPitStops_ShouldThrowException(int pitStops) {
        RaceStrategy strategy = new RaceStrategy(pitStops, "Medium", "Medium", 95.0);
        InvalidStrategyException ex = assertThrows(
                InvalidStrategyException.class,
                () -> Validator.validateStrategy(strategy, validTrack));
        assertTrue(ex.getMessage().contains(pitStops < 0 ? "below minimum" : "exceeds maximum"));
    }

    @Test
    public void testNullFuelStrategyShouldThrowException() {
        Track validTrackLocal = new Track("Valid", 5.0, 10, "Medium", "Smooth");
        RaceStrategy nullFuel = new RaceStrategy(1, "Medium", null, 95.0);
        InvalidStrategyException ex = assertThrows(InvalidStrategyException.class,
                () -> Validator.validateStrategy(nullFuel, validTrackLocal));
        assertTrue(ex.getMessage().contains("Fuel strategy cannot be null or empty"));
    }

    @Test
    public void testVeryShortTime() throws InvalidStrategyException {
        Track normalTrack = new Track("NormalShort", 5.0, 10, "Medium", "Smooth");
        RaceStrategy quickStrategy = new RaceStrategy(1, "Medium", "Medium", 25.0);
        ValidationResult result = Validator.validateStrategy(quickStrategy, normalTrack);
        assertTrue(result.hasWarnings());
        assertTrue(result.getFormattedWarnings().contains("very short"));
    }

    // important
    @Test
    public void testAllValidShouldPass() throws Exception {
        ValidationResult result = Validator.validateRaceSetup(validCar, validTrack, validStrategy);

        assertTrue(result.isValid());
        assertEquals("Complete race setup validation passed", result.getMessage());
    }

    @Test
    public void testLowPowerShouldWarn() throws Exception {
        Engine lowPowerEngine = new Engine("Weak", 120, 15.0, 120.0);
        Car lowPowerCar = new Car(1, "WeakCar", 950.0,
                lowPowerEngine,
                Tyre.createMediumTyre(),
                Tyre.createMediumTyre(),
                AeroKit.createStandardKit());

        Track hardTrack = new Track("Hard", 5.0, 18, "Hard", "Smooth");

        ValidationResult result = Validator.validateRaceSetup(lowPowerCar, hardTrack, validStrategy);

        assertTrue(result.isValid());
        assertTrue(result.hasWarnings());
        assertTrue(result.getFormattedWarnings().contains("Low power engine on hard track"));
    }

    // important for validation result
    @Test
    public void testWithWarn() {
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
    public void testNoWarn() {
        ValidationResult result = new ValidationResult(false, "Test message", null);

        assertFalse(result.isValid());
        assertEquals("Test message", result.getMessage());
        assertFalse(result.hasWarnings());
        assertEquals("No warnings", result.getFormattedWarnings());
        assertEquals("ValidationResult{isValid=false, message='Test message'}", result.toString());
    }

    @Test
    public void testManyStopsAndHeavyFuelWarn() throws InvalidStrategyException {
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
    public void testTimeZeroWarn() throws InvalidStrategyException {
        Track normalTrack = new Track("Normal", 5.0, 10, "Medium", "Smooth");
        RaceStrategy strategy = new RaceStrategy(1, null, "Medium", 0.0);

        ValidationResult result = Validator.validateStrategy(strategy, normalTrack);
        assertTrue(result.isValid());
        assertTrue(result.hasWarnings());
        String warnings = result.getFormattedWarnings();
        assertTrue(warnings.contains("Tyre strategy is not specified"));
        assertTrue(warnings.contains("should be positive"));
    }

    @Test
    public void testLongEstimatedTimeWarning() throws InvalidStrategyException {
        Track normalTrack = new Track("Normal2", 5.0, 10, "Medium", "Smooth");
        RaceStrategy strategy = new RaceStrategy(1, "Medium", "Medium", 200.0);
        ValidationResult result = Validator.validateStrategy(strategy, normalTrack);
        assertTrue(result.hasWarnings());
        assertTrue(result.getFormattedWarnings().contains("very long"));
    }

    @Test
    public void testShortTrackBranch() {
        Track shortTrack = new Track("Shorty", 2.0, 16, "Medium", "Smooth");
        RaceStrategy invalidStrategy = new RaceStrategy(-1, "Medium", "Light", 95.0);

        InvalidStrategyException ex = assertThrows(InvalidStrategyException.class,
                () -> Validator.validateStrategy(invalidStrategy, shortTrack));

        String rec = ex.getRecommendedAction();
        assertTrue(rec.contains("short tracks"));
        assertTrue(rec.contains("High-corner tracks"));
    }
}
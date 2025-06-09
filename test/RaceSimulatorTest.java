import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the RaceSimulator class.
 */
public class RaceSimulatorTest {

    private RaceSimulator simulator;
    private Car testCar;
    private Track testTrack;
    private RaceStrategy testStrategy;
    private Weather testWeather;

    @BeforeEach
    public void setUp() {
        // Set up test objects before each test
        simulator = new RaceSimulator(10); // 10 lap race

        testCar = new Car(1, "TestCar", 950.0,
                Engine.createStandardEngine(),
                Tyre.createMediumTyre(),
                Tyre.createMediumTyre(),
                AeroKit.createStandardKit());

        testTrack = Track.createMonacoTrack();
        testStrategy = RaceStrategy.createBalancedStrategy();
        testWeather = Weather.createDryWeather();
    }

    @Test
    public void testConstructor() {
        // Test constructor initialization
        RaceSimulator newSimulator = new RaceSimulator(50);

        // Test indirectly by checking initial state
        assertEquals(0, newSimulator.getCurrentLap());
        assertFalse(newSimulator.isRaceFinished());

        // Verify total laps by running a simulation and checking final lap count
        newSimulator.simulateRace(testCar, testTrack, testStrategy, testWeather);
        assertEquals(50, newSimulator.getCurrentLap());
    }

    @Test
    public void testSimulateRace_BasicFunctionality() {
        // Test basic race simulation
        RaceResult result = simulator.simulateRace(testCar, testTrack, testStrategy, testWeather);

        assertNotNull(result, "Race result should not be null");
        assertTrue(result.getTotalTime() > 0, "Total time should be positive");
        assertTrue(result.getAverageLapTime() > 0, "Average lap time should be positive");
        assertEquals(testStrategy.getNumberOfPitStops(), result.getPitStopCount());
        assertEquals(testWeather.getCondition(), result.getWeatherCondition());
        assertTrue(simulator.isRaceFinished(), "Race should be marked as finished");
    }

    @Test
    public void testSimulateRace_LapCountProgression() {
        // Test that lap count progresses correctly
        simulator.simulateRace(testCar, testTrack, testStrategy, testWeather);

        assertEquals(10, simulator.getCurrentLap(), "Should complete all 10 laps");
        assertTrue(simulator.isRaceFinished(), "Race should be finished");
    }

    @Test
    public void testSimulateLap_DryWeather() {
        // Test lap simulation with dry weather
        double lapTime = simulator.simulateLap(testCar, testTrack, testWeather);

        assertTrue(lapTime > 0, "Lap time should be positive");
        assertTrue(lapTime < 300, "Lap time should be reasonable (< 5 minutes)");
    }

    @Test
    public void testSimulateLap_WetWeather() {
        // Test lap simulation with wet weather
        Weather wetWeather = Weather.createWetWeather(); // Rain intensity = 7
        double wetLapTime = simulator.simulateLap(testCar, testTrack, wetWeather);
        double dryLapTime = simulator.simulateLap(testCar, testTrack, testWeather);

        assertTrue(wetLapTime > dryLapTime,
                "Wet lap time should be slower than dry lap time");
    }

    @Test
    public void testSimulateLap_HighWindWeather() {
        // Test lap simulation with high wind
        Weather windyWeather = new Weather("Windy", 20, 35, 0); // Wind speed = 35
        double windyLapTime = simulator.simulateLap(testCar, testTrack, windyWeather);
        double normalLapTime = simulator.simulateLap(testCar, testTrack, testWeather);

        assertTrue(windyLapTime > normalLapTime,
                "Windy lap time should be slower than normal lap time");
    }

    @Test
    public void testSimulateLap_ExtremeCombinedWeather() {
        // Test lap simulation with both rain and wind
        Weather extremeWeather = new Weather("Extreme", 15, 40, 8); // High wind + heavy rain
        double extremeLapTime = simulator.simulateLap(testCar, testTrack, extremeWeather);
        double normalLapTime = simulator.simulateLap(testCar, testTrack, testWeather);

        assertTrue(extremeLapTime > normalLapTime * 1.15,
                "Extreme weather should significantly slow lap times");
    }

    @Test
    public void testSimulatePitStop_LightFuelStrategy() {
        // Test pit stop with light fuel strategy
        RaceStrategy lightStrategy = RaceStrategy.createAggressiveStrategy(); // "Light" fuel
        double pitStopTime = simulator.simulatePitStop(lightStrategy);

        double expectedTime = lightStrategy.getNumberOfPitStops() * 25; // 25 seconds per stop
        assertEquals(expectedTime, pitStopTime, 0.001,
                "Light fuel strategy should have 25 seconds per pit stop");
    }

    @Test
    public void testSimulatePitStop_MediumFuelStrategy() {
        // Test pit stop with medium fuel strategy
        RaceStrategy mediumStrategy = RaceStrategy.createBalancedStrategy(); // "Medium" fuel
        double pitStopTime = simulator.simulatePitStop(mediumStrategy);

        double expectedTime = mediumStrategy.getNumberOfPitStops() * 30; // 30 seconds per stop
        assertEquals(expectedTime, pitStopTime, 0.001,
                "Medium fuel strategy should have 30 seconds per pit stop");
    }

    @Test
    public void testSimulatePitStop_HeavyFuelStrategy() {
        // Test pit stop with heavy fuel strategy
        RaceStrategy heavyStrategy = RaceStrategy.createConservativeStrategy(); // "Heavy" fuel
        double pitStopTime = simulator.simulatePitStop(heavyStrategy);

        double expectedTime = heavyStrategy.getNumberOfPitStops() * 35; // 35 seconds per stop
        assertEquals(expectedTime, pitStopTime, 0.001,
                "Heavy fuel strategy should have 35 seconds per pit stop");
    }

    @Test
    public void testSimulatePitStop_ZeroPitStops() {
        // Test strategy with zero pit stops
        RaceStrategy noPitStrategy = new RaceStrategy(0, "Medium", "Heavy", 90.0);
        double pitStopTime = simulator.simulatePitStop(noPitStrategy);

        assertEquals(0.0, pitStopTime, 0.001,
                "Zero pit stops should result in zero pit stop time");
    }

    @Test
    public void testSimulatePitStop_MultiplePitStops() {
        // Test strategy with multiple pit stops
        RaceStrategy multiPitStrategy = new RaceStrategy(4, "Soft", "Light", 85.0);
        double pitStopTime = simulator.simulatePitStop(multiPitStrategy);

        double expectedTime = 4 * 25; // 4 stops * 25 seconds each
        assertEquals(expectedTime, pitStopTime, 0.001,
                "Multiple pit stops should multiply the time correctly");
    }

    @Test
    public void testRaceResult_TotalTimeCalculation() {
        // Test that total time includes both lap time and pit stop time
        RaceResult result = simulator.simulateRace(testCar, testTrack, testStrategy, testWeather);

        // Calculate expected minimum time (without random variation)
        double expectedLapTime = simulator.simulateLap(testCar, testTrack, testWeather);
        double expectedTotalLapTime = expectedLapTime * 10; // 10 laps
        double expectedPitStopTime = simulator.simulatePitStop(testStrategy);
        double expectedMinTotalTime = (expectedTotalLapTime + expectedPitStopTime) / 60.0; // Convert to minutes

        // Allow for random variation (±2 seconds per lap = ±20 seconds total)
        assertTrue(result.getTotalTime() >= (expectedMinTotalTime - 20.0/60.0),
                "Total time should be within expected range (accounting for variation)");
    }

    @Test
    public void testMultipleRaces_StateReset() {
        // Test that simulator can handle multiple races
        RaceResult firstRace = simulator.simulateRace(testCar, testTrack, testStrategy, testWeather);

        // Reset for second race
        simulator = new RaceSimulator(15); // Different lap count
        RaceResult secondRace = simulator.simulateRace(testCar, testTrack, testStrategy, testWeather);

        assertNotNull(firstRace, "First race should complete successfully");
        assertNotNull(secondRace, "Second race should complete successfully");
        assertEquals(15, simulator.getCurrentLap(), "Should complete 15 laps in second race");
    }

    @Test
    public void testDifferentCarConfigurations() {
        // Test with different car configurations
        Car fastCar = new Car(2, "FastCar", 800.0,
                Engine.createTurboEngine(),
                Tyre.createSoftTyre(),
                Tyre.createSoftTyre(),
                AeroKit.createLowDragKit());

        Car slowCar = new Car(3, "SlowCar", 1100.0,
                Engine.createStandardEngine(),
                Tyre.createHardTyre(),
                Tyre.createHardTyre(),
                AeroKit.createHighDownforceKit());

        RaceResult fastResult = simulator.simulateRace(fastCar, testTrack, testStrategy, testWeather);

        // Reset simulator for second race
        simulator = new RaceSimulator(10);
        RaceResult slowResult = simulator.simulateRace(slowCar, testTrack, testStrategy, testWeather);

        // Fast car should generally have better lap times (allowing for random variation)
        assertTrue(fastResult.getAverageLapTime() <= slowResult.getAverageLapTime() + 10,
                "Fast car should have competitive lap times compared to slow car");
    }

    @Test
    public void testDifferentTracks() {
        // Test with different tracks
        Track monaco = Track.createMonacoTrack();
        Track silverstone = Track.createSilverstoneTrack();

        RaceResult monacoResult = simulator.simulateRace(testCar, monaco, testStrategy, testWeather);

        // Reset simulator
        simulator = new RaceSimulator(10);
        RaceResult silverstoneResult = simulator.simulateRace(testCar, silverstone, testStrategy, testWeather);

        assertNotNull(monacoResult, "Monaco race should complete");
        assertNotNull(silverstoneResult, "Silverstone race should complete");

        // Both should have positive times
        assertTrue(monacoResult.getTotalTime() > 0, "Monaco total time should be positive");
        assertTrue(silverstoneResult.getTotalTime() > 0, "Silverstone total time should be positive");
    }

    @Test
    public void testEdgeCases() {
        // Test with minimal lap count
        RaceSimulator shortRace = new RaceSimulator(1);
        RaceResult shortResult = shortRace.simulateRace(testCar, testTrack, testStrategy, testWeather);

        assertNotNull(shortResult, "Single lap race should complete");
        assertEquals(1, shortRace.getCurrentLap(), "Should complete exactly 1 lap");
        assertTrue(shortRace.isRaceFinished(), "Short race should be finished");
    }

    @Test
    public void testRandomVariationExists() {
        // Test that random variation is actually applied
        simulator = new RaceSimulator(1); // Single lap for consistency

        double lapTime1 = 0, lapTime2 = 0;
        boolean variationFound = false;

        // Run multiple simulations to check for variation
        for (int i = 0; i < 20; i++) {
            simulator = new RaceSimulator(1);
            RaceResult result1 = simulator.simulateRace(testCar, testTrack, testStrategy, testWeather);

            simulator = new RaceSimulator(1);
            RaceResult result2 = simulator.simulateRace(testCar, testTrack, testStrategy, testWeather);

            if (Math.abs(result1.getAverageLapTime() - result2.getAverageLapTime()) > 0.1) {
                variationFound = true;
                break;
            }
        }

        assertTrue(variationFound, "Random variation should be present in lap times");
    }

    // Helper method removed since we don't need to access private totalLaps
    // We test total laps indirectly through getCurrentLap() after race completion
}
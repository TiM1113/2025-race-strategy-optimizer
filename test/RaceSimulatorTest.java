import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class RaceSimulatorTest {

    private RaceSimulator simulator;
    private Car testCar;
    private Track testTrack;
    private RaceStrategy testStrategy;
    private Weather testWeather;

    @BeforeEach
    public void setUp() {
        simulator = new RaceSimulator(10);

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
        RaceSimulator newSimulator = new RaceSimulator(50);

        assertEquals(0, newSimulator.getCurrentLap());
        assertFalse(newSimulator.isRaceFinished());

        newSimulator.simulateRace(testCar, testTrack, testStrategy, testWeather);
        assertEquals(50, newSimulator.getCurrentLap());
    }

    @Test
    public void testBasic() {
        RaceResult result = simulator.simulateRace(testCar, testTrack, testStrategy, testWeather);

        assertNotNull(result);
        assertTrue(result.getTotalTime() > 0);
        assertTrue(result.getAverageLapTime() > 0);
        assertEquals(testStrategy.getNumberOfPitStops(), result.getPitStopCount());
        assertEquals(testWeather.getCondition(), result.getWeatherCondition());
        assertTrue(simulator.isRaceFinished());
    }

    @Test
    public void testLapCount() {
        simulator.simulateRace(testCar, testTrack, testStrategy, testWeather);

        assertEquals(10, simulator.getCurrentLap());
        assertTrue(simulator.isRaceFinished());
    }

    @Test
    public void testDryWeather() {
        double lapTime = simulator.simulateLap(testCar, testTrack, testWeather);

        assertTrue(lapTime > 0);
        assertTrue(lapTime < 300);
    }

    @Test
    public void testWetWeather() {
        Weather wetWeather = Weather.createWetWeather();
        double wetLapTime = simulator.simulateLap(testCar, testTrack, wetWeather);
        double dryLapTime = simulator.simulateLap(testCar, testTrack, testWeather);

        assertTrue(wetLapTime > dryLapTime);
    }

    @Test
    public void testHighWindWeather() {
        Weather windyWeather = new Weather("Windy", 20, 35, 0);
        double windyLapTime = simulator.simulateLap(testCar, testTrack, windyWeather);
        double normalLapTime = simulator.simulateLap(testCar, testTrack, testWeather);

        assertTrue(windyLapTime > normalLapTime);
    }

    @Test
    public void testExtremeCombinedWeather() {
        Weather extremeWeather = new Weather("Extreme", 15, 40, 8);
        double extremeLapTime = simulator.simulateLap(testCar, testTrack, extremeWeather);
        double normalLapTime = simulator.simulateLap(testCar, testTrack, testWeather);

        assertTrue(extremeLapTime > normalLapTime * 1.15);
    }

    @Test
    public void testLightFuelStrategy() {
        RaceStrategy lightStrategy = RaceStrategy.createAggressiveStrategy();
        double pitStopTime = simulator.simulatePitStop(lightStrategy);

        double expectedTime = lightStrategy.getNumberOfPitStops() * 25;
        assertEquals(expectedTime, pitStopTime, 0.001);
    }

    @Test
    public void testMediumFuelStrategy() {
        RaceStrategy mediumStrategy = RaceStrategy.createBalancedStrategy();
        double pitStopTime = simulator.simulatePitStop(mediumStrategy);

        double expectedTime = mediumStrategy.getNumberOfPitStops() * 30;
        assertEquals(expectedTime, pitStopTime, 0.001);
    }

    @Test
    public void testHeavyFuelStrategy() {
        RaceStrategy heavyStrategy = RaceStrategy.createConservativeStrategy();
        double pitStopTime = simulator.simulatePitStop(heavyStrategy);

        double expectedTime = heavyStrategy.getNumberOfPitStops() * 35;
        assertEquals(expectedTime, pitStopTime, 0.001);
    }

    @Test
    public void testZeroPitStops() {
        RaceStrategy noPitStrategy = new RaceStrategy(0, "Medium", "Heavy", 90.0);
        double pitStopTime = simulator.simulatePitStop(noPitStrategy);

        assertEquals(0.0, pitStopTime, 0.001);
    }

    @Test
    public void testMultiplePitStops() {
        RaceStrategy multiPitStrategy = new RaceStrategy(4, "Soft", "Light", 85.0);
        double pitStopTime = simulator.simulatePitStop(multiPitStrategy);

        double expectedTime = 4 * 25;
        assertEquals(expectedTime, pitStopTime, 0.001);
    }

    @Test
    public void testTotalTimeCalculation() {
        RaceResult result = simulator.simulateRace(testCar, testTrack, testStrategy, testWeather);

        double expectedLapTime = simulator.simulateLap(testCar, testTrack, testWeather);
        double expectedTotalLapTime = expectedLapTime * 10;
        double expectedPitStopTime = simulator.simulatePitStop(testStrategy);
        // right
        double expectedMinTotalTime = (expectedTotalLapTime + expectedPitStopTime) / 60.0;

        assertTrue(result.getTotalTime() >= (expectedMinTotalTime - 20.0 / 60.0));
    }

    @Test
    public void testStateReset() {
        RaceResult firstRace = simulator.simulateRace(testCar, testTrack, testStrategy, testWeather);

        simulator = new RaceSimulator(15);
        RaceResult secondRace = simulator.simulateRace(testCar, testTrack, testStrategy, testWeather);

        // /
        assertNotNull(firstRace);
        assertNotNull(secondRace);
        assertEquals(15, simulator.getCurrentLap());
    }

    @Test
    public void testDifferentCarConfigurations() {
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

        simulator = new RaceSimulator(10);
        RaceResult slowResult = simulator.simulateRace(slowCar, testTrack, testStrategy, testWeather);

        assertTrue(fastResult.getAverageLapTime() <= slowResult.getAverageLapTime() + 10);
    }

    @Test
    public void testDifferentTracks() {
        Track monaco = Track.createMonacoTrack();
        Track silverstone = Track.createSilverstoneTrack();

        RaceResult monacoResult = simulator.simulateRace(testCar, monaco, testStrategy, testWeather);

        simulator = new RaceSimulator(10);
        RaceResult silverstoneResult = simulator.simulateRace(testCar, silverstone, testStrategy, testWeather);

        assertNotNull(monacoResult);
        assertNotNull(silverstoneResult);

        assertTrue(monacoResult.getTotalTime() > 0);
        assertTrue(silverstoneResult.getTotalTime() > 0);
    }

    @Test
    public void testEdgeCases() {
        RaceSimulator shortRace = new RaceSimulator(1);
        RaceResult shortResult = shortRace.simulateRace(testCar, testTrack, testStrategy, testWeather);

        assertNotNull(shortResult);
        assertEquals(1, shortRace.getCurrentLap());
        assertTrue(shortRace.isRaceFinished());
    }

    @Test
    public void testRandomVariationExists() {
        boolean variationFound = false;

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

        assertTrue(variationFound);
    }

    @Test
    public void testOnDifferentTrackLengths() {
        Car corneringCar = new Car(5, "CornerMaster", 900.0,
                Engine.createTurboEngine(),
                Tyre.createSoftTyre(), Tyre.createSoftTyre(),
                AeroKit.createHighDownforceKit());

        Track monaco = Track.createMonacoTrack();
        Track monza = Track.createMonzaTrack();

        // 3 stops
        RaceStrategy aggressive = RaceStrategy.createAggressiveStrategy();
        // 1 stops
        RaceStrategy conservative = RaceStrategy.createConservativeStrategy();

        // Monaco, short
        RaceSimulator monacoSim = new RaceSimulator(25);
        RaceResult aggressiveMonaco = monacoSim.simulateRace(corneringCar, monaco, aggressive, testWeather);
        RaceResult conservativeMonaco = monacoSim.simulateRace(corneringCar, monaco, conservative, testWeather);

        // wrong
        System.out.println(aggressiveMonaco.getTotalTime());
        System.out.println(conservativeMonaco.getTotalTime());

        assertTrue(aggressiveMonaco.getTotalTime() < conservativeMonaco.getTotalTime());

        // Monza, long
        RaceSimulator monzaSim = new RaceSimulator(15);
        RaceResult aggressiveMonza = monzaSim.simulateRace(corneringCar, monza, aggressive, testWeather);
        RaceResult conservativeMonza = monzaSim.simulateRace(corneringCar, monza, conservative, testWeather);

        System.out.println(aggressiveMonza.getTotalTime());
        System.out.println(conservativeMonza.getTotalTime());

        assertTrue(conservativeMonza.getTotalTime() < aggressiveMonza.getTotalTime());
    }
}
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PerformanceCalculatorTest {
    private Car testCar;
    private Track testTrack;

    @BeforeEach
    void setUp() {
        testCar = new Car(1, "TestCar", 950.0,
                Engine.createStandardEngine(),
                Tyre.createMediumTyre(),
                Tyre.createMediumTyre(),
                AeroKit.createStandardKit());
        testTrack = Track.createMonzaTrack();
    }

    @Test
    void testCalculateTopSpeed() {
        int topSpeed = PerformanceCalculator.calculateTopSpeed(testCar);
        assertTrue(topSpeed > 0);
    }

    @Test
    void testCalculateAcceleration() {
        double acceleration = PerformanceCalculator.calculateAcceleration(testCar);
        assertTrue(acceleration > 0);
    }

    @Test
    void testCalculateFuelConsumption() {
        testTrack.setCurrentWeather(Weather.createDryWeather());
        double dry = PerformanceCalculator.calculateFuelConsumption(testCar, testTrack);
        assertTrue(dry > 0);

        testTrack.setCurrentWeather(Weather.createWetWeather());
        double wet = PerformanceCalculator.calculateFuelConsumption(testCar, testTrack);
        assertTrue(wet > 0);
        assertTrue(wet > dry);

        Weather highWind = Weather.createDryWeather();
        highWind.setWindSpeed(35);
        testTrack.setCurrentWeather(highWind);
        double wind = PerformanceCalculator.calculateFuelConsumption(testCar, testTrack);
        assertTrue(wind > dry);
    }

    @Test
    void testCalculateCorneringAbility() {
        testTrack.setCurrentWeather(Weather.createDryWeather());
        int dry = PerformanceCalculator.calculateCorneringAbility(testCar, testTrack);
        assertTrue(dry > 0);

        testTrack.setCurrentWeather(Weather.createWetWeather());
        int wet = PerformanceCalculator.calculateCorneringAbility(testCar, testTrack);
        assertTrue(wet > 0);
    }

    @Test
    void testGetBestKitForTrack() {
        // hard
        Track hardTrack = new Track("HardTest", 4.0, 19, "Hard", "Smooth");
        AeroKit hardKit = PerformanceCalculator.getBestKitForTrack(hardTrack);
        assertNotNull(hardKit);
        assertEquals("Extreme Aero Kit", hardKit.getName());

        // medium
        Track mediumTrack = new Track("MediumTest", 5.0, 12, "Medium", "Smooth");
        AeroKit mediumKit = PerformanceCalculator.getBestKitForTrack(mediumTrack);
        assertNotNull(mediumKit);
        assertEquals("Ground Effect Kit", mediumKit.getName());

        // easy
        Track easyTrack = new Track("EasyTest", 6.0, 8, "Easy", "Smooth");
        AeroKit easyKit = PerformanceCalculator.getBestKitForTrack(easyTrack);
        assertNotNull(easyKit);
        assertEquals("Low Drag Kit", easyKit.getName());
    }

    @Test
    void testCreateCarPerformance() {
        testTrack.setCurrentWeather(Weather.createMixedWeather());
        Performance perf = PerformanceCalculator.createCarPerformance(testCar, testTrack);
        assertNotNull(perf);
        assertTrue(perf.getTopSpeed() > 0);
        assertTrue(perf.getAcceleration() > 0);
        assertTrue(perf.getFuelConsumption() > 0);
        assertTrue(perf.getCorneringAbility() >= 1 && perf.getCorneringAbility() <= 10);
        assertTrue(perf.getLapTime() > 0);
    }

    @Test
    void testCompareCarConfigurations() {
        Car fastCar = new Car(2, "FastCar", 800.0,
                Engine.createTurboEngine(),
                Tyre.createSoftTyre(),
                Tyre.createSoftTyre(),
                AeroKit.createLowDragKit());

        Car balancedCar = new Car(3, "BalancedCar", 950.0,
                Engine.createStandardEngine(),
                Tyre.createMediumTyre(),
                Tyre.createMediumTyre(),
                AeroKit.createStandardKit());

        Performance fastPerf = PerformanceCalculator.createCarPerformance(fastCar, testTrack);
        Performance balancedPerf = PerformanceCalculator.createCarPerformance(balancedCar, testTrack);

        assertTrue(fastPerf.getTopSpeed() > balancedPerf.getTopSpeed() ||
                        fastPerf.getAcceleration() < balancedPerf.getAcceleration());
    }
}

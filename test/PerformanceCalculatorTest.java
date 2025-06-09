import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PerformanceCalculatorTest {

    @Test
    public void testCalculateTopSpeed() {
        Engine engine = Engine.createStandardEngine();
        AeroKit kit = AeroKit.createLowDragKit();
        Car car = new Car(1, "TestCar", 950.0, engine,
                Tyre.createSoftTyre(), Tyre.createSoftTyre(), kit);

        int expected = (int) (engine.getPower() * 0.75 + kit.getTopSpeedImpact() - kit.getDragCoefficient() * 120);
        int actual = PerformanceCalculator.calculateTopSpeed(car);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetBestKitForTrack_Silverstone() {
        Track track = Track.createSilverstoneTrack();
        AeroKit kit = PerformanceCalculator.getBestKitForTrack(track);
        assertNotNull(kit);
        assertEquals("High Downforce", kit.getKitType());
    }

    @Test
    public void testGetBestKitForTrack_Monaco() {
        Track track = Track.createMonacoTrack();
        AeroKit kit = PerformanceCalculator.getBestKitForTrack(track);
        assertNotNull(kit);
        assertEquals("High Downforce", kit.getKitType());
    }

    @Test
    public void testFuelConsumption_DryVsWet() {
        Car car = new Car(2, "DryWetCar", 950.0,
                Engine.createStandardEngine(),
                Tyre.createMediumTyre(), Tyre.createMediumTyre(),
                AeroKit.createLowDragKit());

        Track track = Track.createMonzaTrack();

        // Dry conditions
        track.setCurrentWeather(Weather.createDryWeather());
        double dryFuel = PerformanceCalculator.calculateFuelConsumption(car, track);

        // Wet conditions
        track.setCurrentWeather(Weather.createWetWeather());
        double wetFuel = PerformanceCalculator.calculateFuelConsumption(car, track);

        assertTrue(wetFuel > dryFuel, "Wet fuel should be higher than dry");
    }

    @Test
    public void testCorneringAbility_DryVsWet() {
        Car car = new Car(3, "CornerCar", 950.0,
                Engine.createStandardEngine(),
                Tyre.createHardTyre(), Tyre.createHardTyre(),    // 改为硬胎
                AeroKit.createLowDragKit());

        Track track = Track.createMonacoTrack();

        // Dry conditions
        track.setCurrentWeather(Weather.createDryWeather());
        int dryCorner = PerformanceCalculator.calculateCorneringAbility(car, track);

        // Wet conditions
        track.setCurrentWeather(Weather.createWetWeather());
        int wetCorner = PerformanceCalculator.calculateCorneringAbility(car, track);

        assertTrue(wetCorner < dryCorner, "Wet cornering should be lower than dry");
    }

    @Test
    public void testCreateCarPerformance() {
        Car car = new Car(4, "FullCalc", 950.0,
                Engine.createTurboEngine(),
                Tyre.createHardTyre(), Tyre.createHardTyre(),
                AeroKit.createAdjustableKit());

        Track track = Track.createSilverstoneTrack();
        track.setCurrentWeather(Weather.createMixedWeather());

        Performance perf = PerformanceCalculator.createCarPerformance(car, track);

        assertTrue(perf.getTopSpeed() > 0);
        assertTrue(perf.getAcceleration() > 0);
        assertTrue(perf.getFuelConsumption() > 0);
        assertTrue(perf.getCorneringAbility() >= 1 && perf.getCorneringAbility() <= 10);
        assertTrue(perf.getLapTime() > 0);
    }
}

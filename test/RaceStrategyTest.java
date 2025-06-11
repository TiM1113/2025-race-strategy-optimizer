import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RaceStrategyTest {
    @Test
    void testGetters() {
        RaceStrategy strategy = new RaceStrategy(2, "Soft-Medium", "Medium", 95.5);
        assertEquals(2, strategy.getNumberOfPitStops());
        assertEquals("Soft-Medium", strategy.getTyreStrategy());
        assertEquals("Medium", strategy.getFuelStrategy());
        assertEquals(95.5, strategy.getEstimatedRaceTime());
    }

    @Test
    void testSetters() {
        RaceStrategy strategy = new RaceStrategy(1, "Medium-Hard", "Heavy", 100.0);
        strategy.setNumberOfPitStops(3);
        assertEquals(3, strategy.getNumberOfPitStops());
        strategy.setTyreStrategy("Soft-Medium");
        assertEquals("Soft-Medium", strategy.getTyreStrategy());
        strategy.setFuelStrategy("Light");
        assertEquals("Light", strategy.getFuelStrategy());
        strategy.setEstimatedRaceTime(90.0);
        assertEquals(90.0, strategy.getEstimatedRaceTime());
    }

    @Test
    void testStrategy() {
        RaceStrategy conservative = new RaceStrategy(1, "Medium-Hard", "Heavy", 100.0);
        assertTrue(conservative.isConservativeStrategy());
        RaceStrategy notConservative1 = new RaceStrategy(2, "Medium-Hard", "Heavy", 100.0);
        assertFalse(notConservative1.isConservativeStrategy());
        RaceStrategy notConservative2 = new RaceStrategy(1, "Medium-Hard", "Light", 100.0);
        assertFalse(notConservative2.isConservativeStrategy());
        RaceStrategy caseInsensitive = new RaceStrategy(1, "Medium-Hard", "hEaVy", 100.0);
        assertTrue(caseInsensitive.isConservativeStrategy());
    }

    @Test
    void testToString() {
        RaceStrategy strategy = new RaceStrategy(2, "Soft-Medium", "Medium", 95.5);
        assertEquals("RaceStrategy{pitStops=2, tyreStrategy='Soft-Medium', fuelStrategy='Medium', estimatedRaceTime=95.5 min}", strategy.toString());
    }

    @Test
    void testStaticMethods() {
        RaceStrategy aggressive = RaceStrategy.createAggressiveStrategy();
        assertEquals(3, aggressive.getNumberOfPitStops());
        assertEquals("Soft-Medium", aggressive.getTyreStrategy());
        assertEquals("Light", aggressive.getFuelStrategy());
        assertEquals(90.0, aggressive.getEstimatedRaceTime());

        RaceStrategy balanced = RaceStrategy.createBalancedStrategy();
        assertEquals(2, balanced.getNumberOfPitStops());

        RaceStrategy conservative = RaceStrategy.createConservativeStrategy();
        assertEquals(1, conservative.getNumberOfPitStops());
    }
}

// PerformanceTest.java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PerformanceTest {
    
    @Test
    public void testGetters() {
        Performance performance = new Performance(300, 2.5, 2.0, 80.5, 8);
        
        assertEquals(300, performance.getTopSpeed());
        assertEquals(2.5, performance.getAcceleration());
        assertEquals(2.0, performance.getFuelConsumption());
        assertEquals(80.5, performance.getLapTime());
        assertEquals(8, performance.getCorneringAbility());
    }
    
    @Test
    public void testSetters() {
        Performance performance = new Performance(300, 2.5, 2.0, 80.5, 8);
        
        performance.setTopSpeed(320);
        assertEquals(320, performance.getTopSpeed());
        performance.setAcceleration(2.3);
        assertEquals(2.3, performance.getAcceleration());
        performance.setFuelConsumption(1.8);
        assertEquals(1.8, performance.getFuelConsumption());
        performance.setLapTime(79.5);
        assertEquals(79.5, performance.getLapTime());
        performance.setCorneringAbility(9);
        assertEquals(9, performance.getCorneringAbility());
    }
    
    @Test
    public void testToString() {
        Performance performance = new Performance(300, 2.5, 2.0, 80.5, 8);
        assertEquals("Performance{topSpeed=300 km/h, acceleration=2.5 sec (0-100), fuelConsumption=2.0 L/lap, lapTime=80.5 sec, corneringAbility=8}", performance.toString());
    }
    
    @Test
    public void testGetOverallRating() {
        Performance performance = new Performance(300, 2.5, 2.0, 80.5, 8);
        // shold be 97
        assertEquals(97, performance.getOverallRating());
    }
    
    @Test
    public void testIsFewerThan() {
        Performance faster = new Performance(300, 2.5, 2.0, 80.5, 8);
        Performance slower = new Performance(290, 2.6, 2.1, 81.5, 7);
        
        assertTrue(faster.isFewerThan(slower));
        assertFalse(slower.isFewerThan(faster));
    }
}

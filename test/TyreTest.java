import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TyreTest {
    @Test
    void testGetters() {
        Tyre tyre = new Tyre("Soft", 0.92, 20, 95);
        assertEquals("Soft", tyre.getCompound());
        assertEquals(0.92, tyre.getGripLevel());
        assertEquals(20, tyre.getDurability());
        assertEquals(95, tyre.getOptimalTemperature());
    }

    @Test
    void testSetters() {
        Tyre tyre = new Tyre("Medium", 0.85, 25, 90);
        tyre.setCompound("Hard");
        assertEquals("Hard", tyre.getCompound());
        tyre.setGripLevel(0.75);
        assertEquals(0.75, tyre.getGripLevel());
        tyre.setDurability(30);
        assertEquals(30, tyre.getDurability());
        tyre.setOptimalTemperature(80);
        assertEquals(80, tyre.getOptimalTemperature());
        tyre.setWearRate(0.775);
        assertEquals(0.775, tyre.getWearRate());
        tyre.setBaseLapTimeBonus(0.2);
        assertEquals(0.2, tyre.getBaseLapTimeBonus());
    }

    @Test
    void testGetPerformanceRating() {
        Tyre tyre = new Tyre("Soft", 0.95, 15, 100);
        assertEquals(95, tyre.getPerformanceRating());
        Tyre tyre2 = new Tyre("Medium", 0.80, 20, 90);
        assertEquals(80, tyre2.getPerformanceRating());
    }

    @Test
    void testToString() {
        Tyre tyre = new Tyre("Soft", 0.95, 15, 100);
        String str = tyre.toString();
        assertTrue(str.contains("Soft"));
        assertTrue(str.contains("0.95"));
    }

    @Test
    void testStaticFactoryMethods() {
        Tyre soft = Tyre.createSoftTyre();
        assertEquals("Soft", soft.getCompound());
        assertEquals(0.95, soft.getGripLevel());
        assertEquals(15, soft.getDurability());
        assertEquals(100, soft.getOptimalTemperature());

        Tyre medium = Tyre.createMediumTyre();
        assertEquals("Medium", medium.getCompound());

        Tyre hard = Tyre.createHardTyre();
        assertEquals("Hard", hard.getCompound());
    }
}

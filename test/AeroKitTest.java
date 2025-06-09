import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the AeroKit class.
 */
public class AeroKitTest {

    @Test
    public void testFactoryMethods() {
        AeroKit kit = AeroKit.createLowDragKit();
        assertEquals("Low Drag Kit", kit.getName());
        assertEquals(0.25, kit.getDragCoefficient(), 0.001);
        assertEquals(150, kit.getDownforce());
        assertEquals(280, kit.getTopSpeedImpact());
    }

    @Test
    public void testAeroRating() {
        AeroKit kit = new AeroKit("Test Kit", 0.32, 220, 230);
        int rating = kit.getAeroRating();
        assertEquals((int)(220 - 0.32 * 100), rating);
    }

    @Test
    public void testGetKitType_HighSpeed() {
        AeroKit kit = AeroKit.createLowDragKit();
        assertEquals("High Speed", kit.getKitType());
    }

    @Test
    public void testGetKitType_HighDownforce() {
        AeroKit kit = AeroKit.createExtremeAeroKit();
        assertEquals("High Downforce", kit.getKitType());
    }

    @Test
    public void testGetKitType_Balanced() {
        AeroKit kit = AeroKit.createAdjustableKit();
        assertEquals("Balanced", kit.getKitType());
    }
}

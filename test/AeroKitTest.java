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
    public void testGetKitTypeHighSpeed() {
        AeroKit kit = AeroKit.createLowDragKit();
        assertEquals("High Speed", kit.getKitType());
    }

    @Test
    public void testGetKitTypeHighDownforce() {
        AeroKit kit = AeroKit.createExtremeAeroKit();
        assertEquals("High Downforce", kit.getKitType());
    }

    @Test
    public void testGetKitTypeBalanced() {
        AeroKit kit = AeroKit.createAdjustableKit();
        assertEquals("Balanced", kit.getKitType());
    }

    @Test
    public void testToString() {
        AeroKit kit = new AeroKit("Test Kit", 0.32, 220, 230);
        String expected = "AeroKit{name='Test Kit', dragCoefficient=0.32, downforce=220, topSpeedImpact=230}";
        assertEquals(expected, kit.toString());
    }

    @Test
    public void testSetters() {
        AeroKit kit = new AeroKit("Initial Kit", 0.30, 200, 250);
        
        // Test setName
        kit.setName("Updated Kit");
        assertEquals("Updated Kit", kit.getName());
        
        // Test setDragCoefficient
        kit.setDragCoefficient(0.35);
        assertEquals(0.35, kit.getDragCoefficient(), 0.001);
        
        // Test setDownforce
        kit.setDownforce(300);
        assertEquals(300, kit.getDownforce());
        
        // Test setTopSpeedImpact
        kit.setTopSpeedImpact(220);
        assertEquals(220, kit.getTopSpeedImpact());

        // cover branch of getKitType
        kit.setDragCoefficient(0.27);
        kit.setDownforce(400);
        kit.setTopSpeedImpact(260);
        assertEquals("High Downforce", kit.getKitType());
    }
}

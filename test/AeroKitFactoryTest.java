import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class AeroKitFactoryTest {

    @Test
    public void testGetAllAvailableKits() {
        List<AeroKit> kits = AeroKitFactory.getAllAvailableKits();
        assertEquals(6, kits.size());
    }

    @Test
    public void testGetKitByNameFound() {
        AeroKit kit = AeroKitFactory.getKitByName("Low Drag Kit");
        assertNotNull(kit);
        assertEquals("Low Drag Kit", kit.getName());
    }

    @Test
    public void testGetKitByNameNotFound() {
        AeroKit kit = AeroKitFactory.getKitByName("Nonexistent Kit");
        assertNull(kit);
    }

    @Test
    public void testGetKitsForHighSpeedTrack() {
        List<AeroKit> kits = AeroKitFactory.getKitsForTrackType("HighSpeed");
        assertTrue(kits.stream().anyMatch(k -> k.getName().equals("Low Drag Kit")));
        assertTrue(kits.stream().anyMatch(k -> k.getName().equals("Standard Kit")));
    }

    @Test
    public void testGetKitsForTechnicalTrack() {
        List<AeroKit> kits = AeroKitFactory.getKitsForTrackType("Technical");
        assertTrue(kits.stream().anyMatch(k -> k.getName().equals("High Downforce Kit")));
        assertTrue(kits.stream().anyMatch(k -> k.getName().equals("Ground Effect Kit")));
    }

    @Test
    public void testGetKisForBalancedTrack() {
        List<AeroKit> kits = AeroKitFactory.getKitsForTrackType("balanced");
        assertTrue(kits.stream().anyMatch(k -> k.getName().equals("Adjustable Kit")));
        assertTrue(kits.stream().anyMatch(k -> k.getName().equals("Standard Kit")));
    }

    @Test
    public void testGetKisForNoSuchTrack() {
        List<AeroKit> defaultKits = AeroKitFactory.getKitsForTrackType("noSuchName");
        assertTrue(defaultKits.stream().anyMatch(k -> k.getName().equals("Adjustable Kit")));
        assertTrue(defaultKits.stream().anyMatch(k -> k.getName().equals("Standard Kit")));
    }
}

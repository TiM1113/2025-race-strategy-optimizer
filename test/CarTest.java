import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CarTest {
    private Car basicCar;
    private Car configuredCar;
    private Engine testEngine;
    private Tyre testFrontTyres;
    private Tyre testRearTyres;
    private AeroKit testAeroKit;

    @BeforeEach
    void setUp() {
        basicCar = new Car(1, "Test Car", 1000.0, false);
        testEngine = new Engine("Test Engine", 300, 10.0, 150.0);
        testFrontTyres = new Tyre("Medium", 0.85, 25, 90);
        testRearTyres = new Tyre("Medium", 0.85, 25, 90);
        testAeroKit = new AeroKit("Test Aero", 0.30, 200, 250);
        configuredCar = new Car(2, "Configured Car", 1000.0, testEngine, testFrontTyres, testRearTyres, testAeroKit);
    }

    @Test
    void testBasicCarConstructor() {
        assertEquals(1, basicCar.getId());
        assertEquals("Test Car", basicCar.getName());
        assertEquals(1000.0, basicCar.getWeight());
        assertFalse(basicCar.isConfigured());
        assertNull(basicCar.getEngine());
        assertNull(basicCar.getFrontTyres());
        assertNull(basicCar.getRearTyres());
        assertNull(basicCar.getAeroKit());
    }

    @Test
    void testConfiguredCarConstructor() {
        assertEquals(2, configuredCar.getId());
        assertEquals("Configured Car", configuredCar.getName());
        assertEquals(1000.0, configuredCar.getWeight());
        assertTrue(configuredCar.isConfigured());
        assertEquals(testEngine, configuredCar.getEngine());
        assertEquals(testFrontTyres, configuredCar.getFrontTyres());
        assertEquals(testRearTyres, configuredCar.getRearTyres());
        assertEquals(testAeroKit, configuredCar.getAeroKit());
    }

    @Test
    void testSettersAndGetters() {
        basicCar.setId(3);
        assertEquals(3, basicCar.getId());

        basicCar.setName("Updated Car");
        assertEquals("Updated Car", basicCar.getName());

        basicCar.setWeight(1200.0);
        assertEquals(1200.0, basicCar.getWeight());

        basicCar.setConfigured(true);
        assertTrue(basicCar.isConfigured());

        basicCar.setEngine(testEngine);
        assertEquals(testEngine, basicCar.getEngine());

        basicCar.setFrontTyres(testFrontTyres);
        assertEquals(testFrontTyres, basicCar.getFrontTyres());

        basicCar.setRearTyres(testRearTyres);
        assertEquals(testRearTyres, basicCar.getRearTyres());

        basicCar.setAeroKit(testAeroKit);
        assertEquals(testAeroKit, basicCar.getAeroKit());
    }

    @Test
    void testGetTotalWeight() {
        assertEquals(1000.0, basicCar.getTotalWeight());
        assertEquals(1150.0, configuredCar.getTotalWeight());
    }

    @Test
    void testIsFullyConfigured() {
        assertFalse(basicCar.isFullyConfigured());
        assertTrue(configuredCar.isFullyConfigured());
    }

    @Test
    void testValidateConfiguration() {
        assertFalse(basicCar.validateConfiguration());
        assertTrue(configuredCar.validateConfiguration());
    }

    @Test
    void testGetBasicInfo() {
        String expectedInfo = "Car #1: Test Car | Weight: 1000.0kg | Configured: false";
        assertEquals(expectedInfo, basicCar.getBasicInfo());
    }

    @Test
    void testToString() {
        String expectedToString = "Car{id=1, name='Test Car', weight=1000.0, isConfigured=false, engine=null, frontTyres=null, rearTyres=null, aeroKit=null}";
        assertEquals(expectedToString, basicCar.toString());
    }
}

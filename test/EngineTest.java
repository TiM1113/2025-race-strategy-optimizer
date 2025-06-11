import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EngineTest {
    private Engine testEngine;
    private Engine standardEngine;
    private Engine turboEngine;

    @BeforeEach
    void setUp() {
        testEngine = new Engine("Test Engine", 250, 10.0, 150.0);
        standardEngine = Engine.createStandardEngine();
        turboEngine = Engine.createTurboEngine();
    }

    @Test
    void testConstructor() {
        assertEquals("Test Engine", testEngine.getType());
        assertEquals(250, testEngine.getPower());
        assertEquals(10.0, testEngine.getFuelEfficiency());
        assertEquals(150.0, testEngine.getWeight());
    }

    @Test
    void testStandardEngineFactory() {
        assertEquals("Standard", standardEngine.getType());
        assertEquals(200, standardEngine.getPower());
        assertEquals(12.0, standardEngine.getFuelEfficiency());
        assertEquals(150.0, standardEngine.getWeight());
    }

    @Test
    void testTurboEngineFactory() {
        assertEquals("Turbocharged", turboEngine.getType());
        assertEquals(300, turboEngine.getPower());
        assertEquals(9.0, turboEngine.getFuelEfficiency());
        assertEquals(180.0, turboEngine.getWeight());
    }

    @Test
    void testSettersAndGetters() {
        testEngine.setType("Updated Engine");
        assertEquals("Updated Engine", testEngine.getType());

        testEngine.setPower(300);
        assertEquals(300, testEngine.getPower());

        testEngine.setFuelEfficiency(11.0);
        assertEquals(11.0, testEngine.getFuelEfficiency());

        testEngine.setWeight(160.0);
        assertEquals(160.0, testEngine.getWeight());
    }

    @Test
    void testCalculatePowerToWeight() {
        // normal
        assertEquals(1.67, testEngine.calculatePowerToWeight(), 0.01);

        // zero
        testEngine.setWeight(0);
        assertEquals(0, testEngine.calculatePowerToWeight());
    }

    @Test
    void testToString() {
        assertEquals("Engine{type='Test Engine', power=250, fuelEfficiency=10.0, weight=150.0}", testEngine.toString());
    }

    @Test
    void testPowerToWeightComparison() {
        assertTrue(turboEngine.calculatePowerToWeight() > standardEngine.calculatePowerToWeight());
    }

    @Test
    void testFuelEfficiencyComparison() {
        assertTrue(standardEngine.getFuelEfficiency() > turboEngine.getFuelEfficiency());
    }
}

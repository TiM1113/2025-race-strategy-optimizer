import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class RaceManagerTest {

    @Test
    public void testCreateCarConfigurationIsValid() {
        Car car = new Car(
                1,
                "UnitTestCar",
                950.0,
                Engine.createTurboEngine(),
                Tyre.createMediumTyre(),
                Tyre.createMediumTyre(),
                AeroKit.createGroundEffectKit()
        );

        assertTrue(car.isConfigured());
        assertTrue(car.validateConfiguration());
        assertEquals(1130.0, car.getTotalWeight());
    }

    @Test
    public void testTrackRecommendsCorrectKitType_Monaco() {
        Track monaco = Track.createMonacoTrack();
        AeroKit kit = PerformanceCalculator.getBestKitForTrack(monaco);
        assertEquals("High Downforce", kit.getKitType());
    }

    @Test
    public void testTrackRecommendsCorrectKitType_Silverstone() {
        Track track = Track.createSilverstoneTrack();
        AeroKit kit = PerformanceCalculator.getBestKitForTrack(track);
        assertNotNull(kit);
        assertEquals("High Downforce", kit.getKitType());
    }

    @Test
    public void testPerformanceCalculation() {
        Car car = new Car(
                2,
                "Speedster",
                900.0,
                Engine.createStandardEngine(),
                Tyre.createSoftTyre(),
                Tyre.createSoftTyre(),
                AeroKit.createLowDragKit()
        );

        Track track = Track.createMonzaTrack();

        Performance performance = PerformanceCalculator.createCarPerformance(car, track);

        assertTrue(performance.getTopSpeed() > 300);
        assertTrue(performance.getAcceleration() > 0);
        assertTrue(performance.getCorneringAbility() >= 1 && performance.getCorneringAbility() <= 10);
    }

    @Test
    public void testConservativeStrategyDetection() {
        RaceStrategy conservative = RaceStrategy.createConservativeStrategy();
        assertTrue(conservative.isConservativeStrategy());

        RaceStrategy aggressive = RaceStrategy.createAggressiveStrategy();
        assertFalse(aggressive.isConservativeStrategy());
    }

    @Test
    public void testSimulatedUserInputForMenu() {
        // 模拟用户依次输入：1（创建车）→ 1（选择标准引擎）→ 1（选择软胎）→ 1（选择第一个AeroKit）
        String simulatedInput = "1\n1\n1\n1\n6\n";
        InputStream stdin = System.in;
        try {
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            RaceManager.main(null); // 调用 main 模拟用户流程
        } catch (Exception e) {
            fail("Should not throw exception: " + e.getMessage());
        } finally {
            System.setIn(stdin); // 恢复输入流
        }
    }
}

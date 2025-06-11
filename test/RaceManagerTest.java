import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class RaceManagerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    public void setUpStreams() {
//        System.setIn();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

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
    public void testTrackMonaco() {
        Track monaco = Track.createMonacoTrack();
        AeroKit kit = PerformanceCalculator.getBestKitForTrack(monaco);
        assertEquals("Extreme Aero Kit", kit.getName());
    }

    @Test
    public void testTrackSilverstone() {
        Track track = Track.createSilverstoneTrack();
        AeroKit kit = PerformanceCalculator.getBestKitForTrack(track);
        assertNotNull(kit);
        assertEquals("Extreme Aero Kit", kit.getName());
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

        assertTrue(performance.getTopSpeed() > 250);
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
        String simulatedInput = "1\n1\n1\n1\n6\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        try {
            RaceManager.main(null);
        } catch (Exception e) {
            if (!(e instanceof java.util.NoSuchElementException)) {
                fail(e.getMessage());
            }
        }
    }

    @Test
    void testGetUserChoice() {
        String simulatedInput = "abc\n2\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        int choice = RaceManager.getUserChoice(1, 5);

        assertEquals(2, choice);

        String consoleOutput = outContent.toString();
        assertTrue(consoleOutput.contains("Invalid input. Enter a number:"));
    }

    @Test
    void testGetUserChoiceOORange() {
        String simulatedInput = "99\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        int choice = RaceManager.getUserChoice(1, 5);

        assertEquals(3, choice);

        String consoleOutput = outContent.toString();
        assertTrue(consoleOutput.contains("Invalid choice. Try again:"));
    }

    @Test
    void testFullInteractiveFlow() {
        outContent.reset();

        String simulatedInput = String.join("\n",
                "1",
                "2",
                "1",
                "1",
                "2",
                "2",
                "3",
                "4",
                "1",
                "5",
                "6"
        ) + "\n";
        // final newline is a must

        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        try {
            RaceManager.main(null);
        } catch (Exception e) {
            if (!(e instanceof java.util.NoSuchElementException)) {
                fail(e.getMessage());
            }
        }

        String output = outContent.toString();

        System.setOut(originalOut);
        System.out.println(output);

        assertAll(
                () -> assertTrue(output.contains("Car created successfully")),
                () -> assertTrue(output.contains("Track selected")),
                () -> assertTrue(output.contains("Performance calculated")),
                () -> assertTrue(output.contains("Strategy selected")),
                () -> assertTrue(output.contains("=== Current Setup ===")),
                () -> assertTrue(output.contains("Exiting..."))
        );
    }
}
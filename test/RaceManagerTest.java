import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class RaceManagerTest {

    // For capturing console output
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    public void setUpStreams() {
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
    public void testTrackRecommendsCorrectKitType_Monaco() {
        Track monaco = Track.createMonacoTrack();
        // NOTE: The logic in PerformanceCalculator was updated in the case study document.
        // It now recommends "Extreme Aero Kit" for Hard tracks. Let's align the test with that.
        AeroKit kit = PerformanceCalculator.getBestKitForTrack(monaco);
        assertEquals("Extreme Aero Kit", kit.getName());
    }

    @Test
    public void testTrackRecommendsCorrectKitType_Silverstone() {
        Track track = Track.createSilverstoneTrack();
        AeroKit kit = PerformanceCalculator.getBestKitForTrack(track);
        assertNotNull(kit);
        // Silverstone has 18 corners (>15), so it should also recommend Extreme Aero Kit.
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

        assertTrue(performance.getTopSpeed() > 250); // Adjusted for more realistic expectation
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

    // This test is kept for regression, but the new tests below are more specific.
    @Test
    public void testSimulatedUserInputForMenu() {
        String simulatedInput = "1\n1\n1\n1\n6\n"; // Create Car -> Standard -> Soft -> Standard Kit -> Exit
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        try {
            RaceManager.main(null);
        } catch (Exception e) {
            // This test is limited because it can be exited by different means.
            // We only assert it doesn't throw an unexpected, fatal exception.
            if (e instanceof java.util.NoSuchElementException) {
                // This exception is expected when input runs out.
            } else {
                fail("Should not throw unexpected exception: " + e.getMessage());
            }
        }
    }


    // #################################################################
    // ##                  NEW TESTS FROM STAGE 2                     ##
    // #################################################################

    /**
     * NEW TEST: Verifies that the getUserChoice method correctly handles invalid non-numeric input.
     * Aligns with Assignment Requirement: "Create tests for edge cases like invalid inputs".
     */
    @Test
    void testGetUserChoice_HandlesInvalidInput() {
        // Arrange: Simulate a user typing "abc", then a valid "2"
        String simulatedInput = "abc\n2\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Act: Call the method we want to test
        int choice = RaceManager.getUserChoice(1, 5);

        // Assert
        // 1. Check that the method returned the correct, valid integer.
        assertEquals(2, choice, "Should return the valid number '2' after ignoring 'abc'.");

        // 2. Check that the correct error message was printed to the console.
        String consoleOutput = outContent.toString();
        assertTrue(consoleOutput.contains("Invalid input. Enter a number:"),
                "Should print an error message for non-numeric input.");
    }

    /**
     * NEW TEST: Verifies that getUserChoice correctly handles a value outside the valid range.
     * Aligns with Assignment Requirement: "Create tests for edge cases like invalid inputs".
     */
    @Test
    void testGetUserChoice_HandlesOutOfRangeInput() {
        // Arrange: Simulate a user typing "99" (out of range), then a valid "3"
        String simulatedInput = "99\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Act
        int choice = RaceManager.getUserChoice(1, 5);

        // Assert
        assertEquals(3, choice, "Should return the valid number '3' after ignoring '99'.");

        String consoleOutput = outContent.toString();
        assertTrue(consoleOutput.contains("Invalid choice. Try again:"),
                "Should print an error message for out-of-range input.");
    }

    /**
     * NEW TEST: Runs through an entire interactive session covering all main menu branches in {@link RaceManager}.
     * Steps:
     * 1. Create Car  → choose Turbo engine, Soft tyre, first AeroKit.
     * 2. Select Track → choose Monza.
     * 3. Calculate Performance.
     * 4. Choose Strategy → Aggressive.
     * 5. Show Results.
     * 6. Exit.
     *
     * This ensures that code paths for createCar, selectTrack, calculatePerformance,
     * chooseStrategy, and showResults are executed at least once.
     */
    @Test
    void testFullInteractiveFlow() {
        // Reset captured output from earlier tests (helps isolate assertions)
        outContent.reset();

        // Build a simulated input sequence matching the menu-driven prompts
        String simulatedInput = String.join("\n",
                "1", // Main menu: Create Car
                "2", // Engine: Turbocharged
                "1", // Tyre: Soft
                "1", // AeroKit: first in list
                "2", // Main menu: Select Track
                "2", // Track: Monza
                "3", // Main menu: Calculate Performance
                "4", // Main menu: Choose Strategy
                "1", // Strategy: Aggressive
                "5", // Main menu: Show Results
                "6"  // Main menu: Exit
        ) + "\n"; // final newline so last read succeeds

        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        try {
            RaceManager.main(null);
        } catch (Exception e) {
            // The interactive loop may throw NoSuchElementException when input runs out; that's expected
            if (!(e instanceof java.util.NoSuchElementException)) {
                fail("Unexpected exception during full interactive flow: " + e.getMessage());
            }
        }

        String output = outContent.toString();

        System.setOut(originalOut);
        System.out.println(output);

        // Basic sanity asserts to confirm key steps executed
        assertAll(
                () -> assertTrue(output.contains("Car created successfully"), "Car creation step should occur"),
                () -> assertTrue(output.contains("Track selected"), "Track selection step should occur"),
                () -> assertTrue(output.contains("Performance calculated"), "Performance calculation should occur"),
                () -> assertTrue(output.contains("Strategy selected"), "Strategy selection should occur"),
                () -> assertTrue(output.contains("=== Current Setup ==="), "Results display should occur"),
                () -> assertTrue(output.contains("Exiting... Goodbye!"), "Exit message should be printed")
        );
    }
}
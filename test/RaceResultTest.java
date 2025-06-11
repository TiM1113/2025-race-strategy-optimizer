import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for the RaceResult class.
 */
public class RaceResultTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        String expectedCarName = "Test Car";
        String expectedTrackName = "Test Track";
        double expectedRaceTime = 95.5;
        String expectedStrategy = "Aggressive Strategy";

        // Act
        RaceResult result = new RaceResult(expectedCarName, expectedTrackName, expectedRaceTime, expectedStrategy);

        // Assert
        assertEquals(expectedCarName, result.getCarName());
        assertEquals(expectedTrackName, result.getTrackName());
        assertEquals(expectedRaceTime, result.getRaceTime(), 0.01);
        assertEquals(expectedStrategy, result.getStrategy());
        assertNotNull(result.getTimestamp());
    }

    @Test
    public void testExtendedConstructor() {
        // Arrange
        String carName = "Formula Car";
        String trackName = "Monaco";
        double raceTime = 120.75;
        String strategy = "Conservative Strategy";
        double avgLapTime = 85.2;
        int pitStops = 2;
        String weather = "Wet";

        // Act
        RaceResult result = new RaceResult(carName, trackName, raceTime, strategy, avgLapTime, pitStops, weather);

        // Assert
        assertEquals(carName, result.getCarName());
        assertEquals(trackName, result.getTrackName());
        assertEquals(raceTime, result.getRaceTime(), 0.01);
        assertEquals(strategy, result.getStrategy());
        assertEquals(avgLapTime, result.getAverageLapTime(), 0.01);
        assertEquals(pitStops, result.getPitStopCount());
        assertEquals(weather, result.getWeatherCondition());
    }

    @Test
    public void testComprehensiveConstructor() {
        // Arrange
        String carName = "Formula Car";
        String trackName = "Monaco";
        double raceTime = 120.75;
        String strategy = "Conservative Strategy";
        double avgLapTime = 85.2;
        int pitStops = 2;
        String weather = "Wet";

        // Act
        RaceResult result = new RaceResult(carName, trackName, raceTime, strategy, LocalDateTime.now(), avgLapTime, pitStops, weather);

        // Assert
        assertEquals(carName, result.getCarName());
        assertEquals(trackName, result.getTrackName());
        assertEquals(raceTime, result.getRaceTime(), 0.01);
        assertEquals(strategy, result.getStrategy());
        assertEquals(avgLapTime, result.getAverageLapTime(), 0.01);
        assertEquals(pitStops, result.getPitStopCount());
        assertEquals(weather, result.getWeatherCondition());
    }

    @Test
    public void testSettersAndGetters() {
        // Arrange
        RaceResult result = new RaceResult("Initial Car", "Initial Track", 100.0, "Initial Strategy");

        // Act
        result.setCarName("Updated Car");
        result.setTrackName("Updated Track");
        result.setRaceTime(105.5);
        result.setStrategy("Updated Strategy");
        result.setAverageLapTime(88.5);
        result.setPitStopCount(3);
        result.setWeatherCondition("Dry");

        LocalDateTime ldt = LocalDateTime.now();

        // Assert
        assertEquals("Updated Car", result.getCarName());
        assertEquals("Updated Track", result.getTrackName());
        assertEquals(105.5, result.getRaceTime(), 0.01);
        assertEquals("Updated Strategy", result.getStrategy());
        assertEquals(88.5, result.getAverageLapTime(), 0.01);
        assertEquals(3, result.getPitStopCount());
        assertEquals("Dry", result.getWeatherCondition());

        // make the coverage 100%
        result.setTimestamp(ldt);
        assertEquals(ldt, result.getTimestamp());
    }

    @Test
    public void testTotalTimeCompatibility() {
        // Arrange
        double expectedTime = 95.5;
        RaceResult result = new RaceResult("Car", "Track", expectedTime, "Strategy");

        // Act & Assert - getTotalTime() should return the same as getRaceTime()
        assertEquals(expectedTime, result.getTotalTime(), 0.01);
        assertEquals(result.getRaceTime(), result.getTotalTime(), 0.01);
    }

    @Test
    public void testIsWinningTime() {
        // Arrange
        RaceResult result = new RaceResult("Fast Car", "Track", 90.0, "Strategy");

        // Act & Assert
        assertTrue(result.isWinningTime(95.0));  // 90 < 95
        assertFalse(result.isWinningTime(85.0)); // 90 > 85
        assertFalse(result.isWinningTime(90.0)); // 90 == 90
    }

    @Test
    public void testToString() {
        // Arrange
        RaceResult result = new RaceResult("Test Car", "Test Track", 100.5, "Balanced Strategy");
        result.setAverageLapTime(75.0);
        result.setPitStopCount(2);
        result.setWeatherCondition("Sunny");

        // Act
        String resultString = result.toString();

        // Assert
        assertNotNull(resultString);
        assertTrue(resultString.contains("Test Car"));
        assertTrue(resultString.contains("Test Track"));
        assertTrue(resultString.contains("100.5"));
        assertTrue(resultString.contains("Balanced Strategy"));
    }

    @Test
    public void testEquals() {
        // Arrange
        RaceResult result1 = new RaceResult("Car1", "Track1", 95.5, "Strategy1");
        RaceResult result2 = new RaceResult("Car1", "Track1", 95.5, "Strategy1");
        RaceResult result3 = new RaceResult("Car2", "Track1", 95.5, "Strategy1");

        // Act & Assert
        assertEquals(result1, result2);
        assertNotEquals(result1, result3);
        assertNotEquals(null, result1);
        assertNotEquals("Not a RaceResult", result1);
    }

    @Test
    public void testHashCode() {
        // Arrange
        RaceResult result1 = new RaceResult("Car1", "Track1", 95.5, "Strategy1");
        RaceResult result2 = new RaceResult("Car1", "Track1", 95.5, "Strategy1");

        // Act & Assert
        assertEquals(result1.hashCode(), result2.hashCode());
    }

    @Test
    public void testDefaultValues() {
        // Arrange & Act
        RaceResult result = new RaceResult("Car", "Track", 100.0, "Strategy");

        // Assert - default values should be set
        assertEquals(0.0, result.getAverageLapTime(), 0.01);
        assertEquals(0, result.getPitStopCount());
        assertEquals("Unknown", result.getWeatherCondition());
    }

    @Test
    public void testTimestampIsSet() {
        // Arrange & Act
        RaceResult result = new RaceResult("Car", "Track", 100.0, "Strategy");

        // Assert
        assertNotNull(result.getTimestamp());
        // Timestamp should be recent (within last second)
        assertTrue(java.time.LocalDateTime.now().isAfter(result.getTimestamp().minusSeconds(1)));
    }

    @Test
    public void testNegativeValues() {
        // Arrange & Act
        RaceResult result = new RaceResult("Car", "Track", -10.0, "Strategy");
        result.setAverageLapTime(-5.0);
        result.setPitStopCount(-1);

        // Assert - should accept negative values (validation might be done elsewhere)
        assertEquals(-10.0, result.getRaceTime(), 0.01);
        assertEquals(-5.0, result.getAverageLapTime(), 0.01);
        assertEquals(-1, result.getPitStopCount());
    }

    @Test
    public void testZeroValues() {
        // Arrange & Act
        RaceResult result = new RaceResult("Car", "Track", 0.0, "Strategy");
        result.setAverageLapTime(0.0);
        result.setPitStopCount(0);

        // Assert
        assertEquals(0.0, result.getRaceTime(), 0.01);
        assertEquals(0.0, result.getAverageLapTime(), 0.01);
        assertEquals(0, result.getPitStopCount());
    }

    @Test
    public void testNullStringValues() {
        // This test verifies behavior with null strings
        // Note: In a production system, you might want to validate and prevent null values

        // Arrange & Act
        RaceResult result = new RaceResult(null, null, 100.0, null);
        result.setWeatherCondition(null);

        // Assert - should handle null values gracefully
        assertNull(result.getCarName());
        assertNull(result.getTrackName());
        assertNull(result.getStrategy());
        assertNull(result.getWeatherCondition());
    }
}
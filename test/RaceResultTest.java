import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for the RaceResult class.
 */
public class RaceResultTest {

    @Test
    public void testGetters() {
        String expectedCarName = "Test Car";
        String expectedTrackName = "Test Track";
        double expectedRaceTime = 95.5;
        String expectedStrategy = "Aggressive Strategy";

        RaceResult result = new RaceResult(expectedCarName, expectedTrackName, expectedRaceTime, expectedStrategy);

        assertEquals(expectedCarName, result.getCarName());
        assertEquals(expectedTrackName, result.getTrackName());
        // use delta
        assertEquals(expectedRaceTime, result.getRaceTime(), 0.01);
        assertEquals(expectedStrategy, result.getStrategy());
        assertNotNull(result.getTimestamp());
    }

    @Test
    public void testConstructor() {
        String carName = "Formula Car";
        String trackName = "Monaco";
        double raceTime = 120.75;
        String strategy = "Conservative Strategy";
        double avgLapTime = 85.2;
        int pitStops = 2;
        String weather = "Wet";

        RaceResult result = new RaceResult(carName, trackName, raceTime, strategy, avgLapTime, pitStops, weather);
        assertEquals(carName, result.getCarName());
        assertEquals(trackName, result.getTrackName());
        assertEquals(raceTime, result.getRaceTime(), 0.01);
        assertEquals(strategy, result.getStrategy());
        assertEquals(avgLapTime, result.getAverageLapTime(), 0.01);
        assertEquals(pitStops, result.getPitStopCount());
        assertEquals(weather, result.getWeatherCondition());

        result = new RaceResult(carName, trackName, raceTime, strategy, LocalDateTime.now(), avgLapTime, pitStops, weather);
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
        RaceResult result = new RaceResult("Initial Car", "Initial Track", 100.0, "Initial Strategy");

        result.setCarName("Updated Car");
        result.setTrackName("Updated Track");
        result.setRaceTime(105.5);
        result.setStrategy("Updated Strategy");
        result.setAverageLapTime(88.5);
        result.setPitStopCount(3);
        result.setWeatherCondition("Dry");

        LocalDateTime ldt = LocalDateTime.now();

        System.out.println(ldt);

        assertEquals("Updated Car", result.getCarName());
        assertEquals("Updated Track", result.getTrackName());
        assertEquals(105.5, result.getRaceTime(), 0.01);
        assertEquals("Updated Strategy", result.getStrategy());
        assertEquals(88.5, result.getAverageLapTime(), 0.01);
        assertEquals(3, result.getPitStopCount());
        assertEquals("Dry", result.getWeatherCondition());

        result.setTimestamp(ldt);
        assertEquals(ldt, result.getTimestamp());
    }

    @Test
    public void testTotalTime() {
        double expectedTime = 95.5;
        RaceResult result = new RaceResult("Car", "Track", expectedTime, "Strategy");

        assertEquals(expectedTime, result.getTotalTime(), 0.01);
        assertEquals(result.getRaceTime(), result.getTotalTime(), 0.01);
    }

    @Test
    public void testIsWinningTime() {
        RaceResult result = new RaceResult("Fast Car", "Track", 90.0, "Strategy");

        assertTrue(result.isWinningTime(95.0));
        assertFalse(result.isWinningTime(85.0));
        assertFalse(result.isWinningTime(90.0));
    }

    @Test
    public void testToString() {
        RaceResult result = new RaceResult("Test Car", "Test Track", 100.5, "Balanced Strategy");
        result.setAverageLapTime(75.0);
        result.setPitStopCount(2);
        result.setWeatherCondition("Sunny");

        String resultString = result.toString();

        assertNotNull(resultString);
        assertTrue(resultString.contains("Test Car"));
        assertTrue(resultString.contains("Test Track"));
        assertTrue(resultString.contains("100.5"));
        assertTrue(resultString.contains("Balanced Strategy"));
    }

    @Test
    public void testEquals() {
        RaceResult result1 = new RaceResult("Car1", "Track1", 95.5, "Strategy1");
        RaceResult result2 = new RaceResult("Car1", "Track1", 95.5, "Strategy1");
        RaceResult result3 = new RaceResult("Car2", "Track1", 95.5, "Strategy1");

        assertEquals(result1, result2);
        assertNotEquals(result1, result3);
        assertNotEquals(null, result1);
        // line cover
        assertNotEquals("Not a RaceResult", result1);
    }

    @Test
    public void testHashCode() {
        RaceResult result1 = new RaceResult("Car1", "Track1", 95.5, "Strategy1");
        RaceResult result2 = new RaceResult("Car1", "Track1", 95.5, "Strategy1");

        assertEquals(result1.hashCode(), result2.hashCode());
    }

    @Test
    public void testDefaultValues() {
        RaceResult result = new RaceResult("Car", "Track", 100.0, "Strategy");

        assertEquals(0.0, result.getAverageLapTime(), 0.01);
        assertEquals(0, result.getPitStopCount());
        assertEquals("Unknown", result.getWeatherCondition());
    }

    @Test
    public void testTimestampIsSet() {
        RaceResult result = new RaceResult("Car", "Track", 100.0, "Strategy");

        assertNotNull(result.getTimestamp());
        assertTrue(LocalDateTime.now().isAfter(result.getTimestamp().minusSeconds(1)));
    }

    @Test
    public void testNegativeValues() {
        RaceResult result = new RaceResult("Car", "Track", -10.0, "Strategy");
        result.setAverageLapTime(-5.0);
        result.setPitStopCount(-1);

        assertEquals(-10.0, result.getRaceTime(), 0.01);
        assertEquals(-5.0, result.getAverageLapTime(), 0.01);
        assertEquals(-1, result.getPitStopCount());
    }

    @Test
    public void testZero() {
        RaceResult result = new RaceResult("Car", "Track", 0.0, "Strategy");
        result.setAverageLapTime(0.0);
        result.setPitStopCount(0);

        assertEquals(0.0, result.getRaceTime(), 0.01);
        assertEquals(0.0, result.getAverageLapTime(), 0.01);
        assertEquals(0, result.getPitStopCount());
    }

    @Test
    public void testNullString() {
        RaceResult result = new RaceResult(null, null, 100.0, null);
        result.setWeatherCondition(null);

        assertNull(result.getCarName());
        assertNull(result.getTrackName());
        assertNull(result.getStrategy());
        assertNull(result.getWeatherCondition());
    }
}
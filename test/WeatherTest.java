import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WeatherTest {

    @Test
    public void testDryWeatherFactory() {
        Weather dry = Weather.createDryWeather();
        assertEquals("Dry", dry.getCondition());
        assertEquals(25, dry.getTemperature());
        assertEquals(10, dry.getWindSpeed());
        assertEquals(0, dry.getRainIntensity());
        assertFalse(dry.isChallenging());
    }

    @Test
    public void testWetWeatherFactory() {
        Weather wet = Weather.createWetWeather();
        assertEquals("Wet", wet.getCondition());
        assertEquals(15, wet.getTemperature());
        assertEquals(20, wet.getWindSpeed());
        assertEquals(7, wet.getRainIntensity());
        assertTrue(wet.isChallenging());
    }

    @Test
    public void testMixedWeatherFactory() {
        Weather mixed = Weather.createMixedWeather();
        assertEquals("Mixed", mixed.getCondition());
        assertEquals(20, mixed.getTemperature());
        assertEquals(25, mixed.getWindSpeed());
        assertEquals(3, mixed.getRainIntensity());
        assertFalse(mixed.isChallenging());
    }

    @Test
    public void testChallengingByWind() {
        Weather windy = new Weather("Windy", 18, 31, 0);
        assertTrue(windy.isChallenging());
    }

    @Test
    public void testSettersAndGetters() {
        Weather w = new Weather("Test", 10, 5, 2);
        w.setCondition("Storm");
        assertEquals("Storm", w.getCondition());
        w.setTemperature(5);
        assertEquals(5, w.getTemperature());
        w.setWindSpeed(50);
        assertEquals(50, w.getWindSpeed());
        w.setRainIntensity(9);
        assertEquals(9, w.getRainIntensity());
        assertTrue(w.isChallenging());
    }

    @Test
    public void testToString() {
        Weather w = new Weather("Foggy", 8, 12, 1);
        String str = w.toString();
        assertTrue(str.contains("Foggy"));
        assertTrue(str.contains("8"));
        assertTrue(str.contains("12"));
        assertTrue(str.contains("1"));
    }
}

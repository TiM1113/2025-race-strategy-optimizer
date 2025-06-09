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
        assertTrue(wet.getRainIntensity() > 5);
        assertTrue(wet.isChallenging());
    }

    @Test
    public void testMixedWeatherChallenging() {
        Weather mixed = new Weather("Mixed", 22, 35, 4); // wind > 30
        assertTrue(mixed.isChallenging());
    }
}

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TrackTest {
    @Test
    void testGetters() {
        Track track = new Track("TestTrack", 4.5, 10, "Medium", "Smooth");
        assertEquals("TestTrack", track.getName());
        assertEquals(4.5, track.getLength());
        assertEquals(10, track.getCorners());
        assertEquals("Medium", track.getDifficulty());
        assertEquals("Smooth", track.getSurfaceType());
        assertNotNull(track.getCurrentWeather());
    }

    @Test
    void testSetCurrentWeather() {
        Track track = new Track("TestTrack", 4.5, 10, "Medium", "Smooth");
        Weather wet = Weather.createWetWeather();
        track.setCurrentWeather(wet);
        assertEquals(wet, track.getCurrentWeather());
    }

    @Test
    void testGetTrackRating() {
        Track track = new Track("TestTrack", 3.0, 8, "Easy", "Rough");
        assertEquals(24.0, track.getTrackRating());
    }

    @Test
    void testGetEffectiveGripNormal() {
        Track track = new Track("TestTrack", 4.0, 12, "Medium", "Smooth");
        Weather dry = Weather.createDryWeather();
        track.setCurrentWeather(dry);
        assertEquals(1.0, track.getEffectiveGrip());
    }

    @Test
    void testGetEffectiveGripRain() {
        Track track = new Track("TestTrack", 4.0, 12, "Medium", "Smooth");
        Weather wet = Weather.createWetWeather();
        wet.setRainIntensity(6);
        track.setCurrentWeather(wet);
        assertEquals(0.8, track.getEffectiveGrip());
    }

    @Test
    void testToString() {
        Track track = new Track("TestTrack", 4.0, 12, "Medium", "Smooth");
        String str = track.toString();
        assertTrue(str.contains("TestTrack"));
        assertTrue(str.contains("length=4.0"));
        assertTrue(str.contains("corners=12"));
        assertTrue(str.contains("difficulty='Medium'"));
        assertTrue(str.contains("surfaceType='Smooth'"));
    }

    @Test
    void testStaticFactoryMethods() {
        Track monaco = Track.createMonacoTrack();
        assertEquals("Monaco", monaco.getName());
        assertEquals(3.3, monaco.getLength());
        assertEquals(19, monaco.getCorners());
        assertEquals("Hard", monaco.getDifficulty());
        assertEquals("Smooth", monaco.getSurfaceType());

        Track monza = Track.createMonzaTrack();
        assertEquals("Monza", monza.getName());

        Track silverstone = Track.createSilverstoneTrack();
        assertEquals("Silverstone", silverstone.getName());
    }
}

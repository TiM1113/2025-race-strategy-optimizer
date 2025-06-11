import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExceptionTest {

    @Test
    public void testInvalidTrackDataException() {
        String msg = "Basic track data error";
        InvalidTrackDataException ex = new InvalidTrackDataException(msg);

        assertEquals(msg, ex.getMessage());
        assertNull(ex.getTrackProperty());
        assertNull(ex.getActualValue());
        assertNull(ex.getValidRange());

        String detailed = ex.getDetailedMessage();
        assertTrue(detailed.contains(msg));
        assertFalse(detailed.contains("Property:"));
    }

    @Test
    public void testInvalidTrackDataExceptionCause() {
        Throwable cause = new RuntimeException("Root cause");
        InvalidTrackDataException ex = new InvalidTrackDataException("With cause", cause);
        assertSame(cause, ex.getCause());
    }

    @Test
    public void testInvalidStrategyExceptionNoAction() {
        InvalidStrategyException ex = new InvalidStrategyException(
                "Conflict", "Aggressive", "Too many pit stops");

        assertEquals("Aggressive", ex.getStrategyType());
        assertEquals("Too many pit stops", ex.getConflictReason());
        assertNull(ex.getRecommendedAction());

        String detailed = ex.getDetailedMessage();
        assertTrue(detailed.contains("Conflict"));
        assertFalse(detailed.contains("Recommended Action:"));
    }

    @Test
    public void testInvalidStrategyExceptionCause() {
        Throwable cause = new IllegalArgumentException("Illegal argument");
        InvalidStrategyException ex = new InvalidStrategyException("Msg", cause);
        assertSame(cause, ex.getCause());
    }

    @Test
    public void testInvalidStrategyException() {
        String msg = "Only message";
        InvalidStrategyException ex = new InvalidStrategyException(msg);
        assertEquals(msg, ex.getMessage());
        assertNull(ex.getStrategyType());
        assertNull(ex.getConflictReason());
        assertNull(ex.getRecommendedAction());
    }

    @Test
    public void testInvalidCarConfigurationException() {
        String msg = "Car config error";
        InvalidCarConfigurationException ex = new InvalidCarConfigurationException(msg);

        assertNull(ex.getComponent());
        assertNull(ex.getExpectedRange());

        String detailed = ex.getDetailedMessage();
        assertTrue(detailed.contains(msg));
        assertFalse(detailed.contains("Component:"));
        assertTrue(detailed.contains("Suggestion:"));
    }

    @Test
    public void testInvalidCarConfigurationExceptionCause() {
        Exception root = new Exception("Root");
        InvalidCarConfigurationException ex = new InvalidCarConfigurationException("Message", root);
        assertSame(root, ex.getCause());
    }
} 
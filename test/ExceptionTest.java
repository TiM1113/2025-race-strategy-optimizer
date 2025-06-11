import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExceptionTest {

    // ===== InvalidTrackDataException Tests =====

    @Test
    public void testInvalidTrackDataException_MessageOnly() {
        String msg = "Basic track data error";
        InvalidTrackDataException ex = new InvalidTrackDataException(msg);

        // Basic assertions
        assertEquals(msg, ex.getMessage());
        assertNull(ex.getTrackProperty());
        assertNull(ex.getActualValue());
        assertNull(ex.getValidRange());

        // Detailed message should contain the suggestion but not the optional parts
        String detailed = ex.getDetailedMessage();
        assertTrue(detailed.contains(msg));
        assertFalse(detailed.contains("Property:"));
        assertFalse(detailed.contains("Actual Value:"));
        assertFalse(detailed.contains("Valid Range:"));
        assertTrue(detailed.contains("Suggestion:"));
    }

    @Test
    public void testInvalidTrackDataException_WithCause() {
        Throwable cause = new RuntimeException("Root cause");
        InvalidTrackDataException ex = new InvalidTrackDataException("With cause", cause);
        assertSame(cause, ex.getCause());
    }

    // ===== InvalidStrategyException Tests =====

    @Test
    public void testInvalidStrategyException_NoRecommendedAction() {
        InvalidStrategyException ex = new InvalidStrategyException(
                "Conflict", "Aggressive", "Too many pit stops");

        // Getter verification
        assertEquals("Aggressive", ex.getStrategyType());
        assertEquals("Too many pit stops", ex.getConflictReason());
        assertNull(ex.getRecommendedAction());

        // Detailed message should fall back to suggestion when recommendedAction is null
        String detailed = ex.getDetailedMessage();
        assertTrue(detailed.contains("Conflict"));
        assertTrue(detailed.contains("Strategy Type: Aggressive"));
        assertTrue(detailed.contains("Conflict: Too many pit stops"));
        assertTrue(detailed.contains("Suggestion:"));
        assertFalse(detailed.contains("Recommended Action:"));
    }

    @Test
    public void testInvalidStrategyException_WithCause() {
        Throwable cause = new IllegalArgumentException("Illegal argument");
        InvalidStrategyException ex = new InvalidStrategyException("Msg", cause);
        assertSame(cause, ex.getCause());
    }

    @Test
    public void testInvalidStrategyException_MessageOnly() {
        String msg = "Only message";
        InvalidStrategyException ex = new InvalidStrategyException(msg);
        assertEquals(msg, ex.getMessage());
        assertNull(ex.getStrategyType());
        assertNull(ex.getConflictReason());
        assertNull(ex.getRecommendedAction());
    }

    // ===== InvalidCarConfigurationException Tests =====

    @Test
    public void testInvalidCarConfigurationException_MessageOnly() {
        String msg = "Car config error";
        InvalidCarConfigurationException ex = new InvalidCarConfigurationException(msg);

        // Getter verification
        assertNull(ex.getComponent());
        assertNull(ex.getExpectedRange());

        String detailed = ex.getDetailedMessage();
        assertTrue(detailed.contains(msg));
        assertFalse(detailed.contains("Component:"));
        assertFalse(detailed.contains("Expected Range:"));
        assertTrue(detailed.contains("Suggestion:"));
    }

    @Test
    public void testInvalidCarConfigurationException_WithCause() {
        Exception root = new Exception("Root");
        InvalidCarConfigurationException ex = new InvalidCarConfigurationException("Message", root);
        assertSame(root, ex.getCause());
    }
} 
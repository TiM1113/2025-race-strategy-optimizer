/**
 * Exception thrown when track data is invalid.
 */
public class InvalidTrackDataException extends Exception {
    private String trackProperty;
    private Object actualValue;
    private String validRange;

    public InvalidTrackDataException(String message) {
        super(message);
    }

    public InvalidTrackDataException(String message, String trackProperty, Object actualValue, String validRange) {
        super(message);
        this.trackProperty = trackProperty;
        this.actualValue = actualValue;
        this.validRange = validRange;
    }

    public InvalidTrackDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getTrackProperty() {
        return trackProperty;
    }

    public Object getActualValue() {
        return actualValue;
    }

    public String getValidRange() {
        return validRange;
    }

    public String getDetailedMessage() {
        StringBuilder sb = new StringBuilder(getMessage());
        if (trackProperty != null) {
            sb.append("\nProperty: ").append(trackProperty);
        }
        if (actualValue != null) {
            sb.append("\nActual Value: ").append(actualValue);
        }
        if (validRange != null) {
            sb.append("\nValid Range: ").append(validRange);
        }
        sb.append("\nSuggestion: Please verify track data meets the required specifications.");
        return sb.toString();
    }
}
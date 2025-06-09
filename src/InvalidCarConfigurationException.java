/**
 * Exception thrown when car configuration is invalid.
 */
public class InvalidCarConfigurationException extends Exception {
    private String component;
    private String expectedRange;

    public InvalidCarConfigurationException(String message) {
        super(message);
    }

    public InvalidCarConfigurationException(String message, String component, String expectedRange) {
        super(message);
        this.component = component;
        this.expectedRange = expectedRange;
    }

    public InvalidCarConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getComponent() {
        return component;
    }

    public String getExpectedRange() {
        return expectedRange;
    }

    public String getDetailedMessage() {
        StringBuilder sb = new StringBuilder(getMessage());
        if (component != null) {
            sb.append("\nComponent: ").append(component);
        }
        if (expectedRange != null) {
            sb.append("\nExpected Range: ").append(expectedRange);
        }
        sb.append("\nSuggestion: Please check car configuration and ensure all components are properly assigned.");
        return sb.toString();
    }
}
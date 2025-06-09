import java.util.ArrayList;
import java.util.List;

/**
 * Result of a validation operation.
 */
public class ValidationResult {
    private final boolean isValid;
    private final String message;
    private final List<String> warnings;

    public ValidationResult(boolean isValid, String message, List<String> warnings) {
        this.isValid = isValid;
        this.message = message;
        this.warnings = warnings != null ? new ArrayList<>(warnings) : new ArrayList<>();
    }

    public boolean isValid() {
        return isValid;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getWarnings() {
        return new ArrayList<>(warnings);
    }

    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }

    public String getFormattedWarnings() {
        if (warnings.isEmpty()) {
            return "No warnings";
        }
        return "Warnings: " + String.join("; ", warnings);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ValidationResult{isValid=").append(isValid);
        sb.append(", message='").append(message).append("'");
        if (!warnings.isEmpty()) {
            sb.append(", warnings=").append(warnings.size());
        }
        sb.append("}");
        return sb.toString();
    }
}
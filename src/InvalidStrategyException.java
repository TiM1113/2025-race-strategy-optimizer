/**
 * Exception thrown when race strategy is invalid.
 */
public class InvalidStrategyException extends Exception {
    private String strategyType;
    private String conflictReason;
    private String recommendedAction;

    public InvalidStrategyException(String message) {
        super(message);
    }

    public InvalidStrategyException(String message, String strategyType, String conflictReason) {
        super(message);
        this.strategyType = strategyType;
        this.conflictReason = conflictReason;
    }

    public InvalidStrategyException(String message, String strategyType, String conflictReason, String recommendedAction) {
        super(message);
        this.strategyType = strategyType;
        this.conflictReason = conflictReason;
        this.recommendedAction = recommendedAction;
    }

    public InvalidStrategyException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getStrategyType() {
        return strategyType;
    }

    public String getConflictReason() {
        return conflictReason;
    }

    public String getRecommendedAction() {
        return recommendedAction;
    }

    public String getDetailedMessage() {
        StringBuilder sb = new StringBuilder(getMessage());
        if (strategyType != null) {
            sb.append("\nStrategy Type: ").append(strategyType);
        }
        if (conflictReason != null) {
            sb.append("\nConflict: ").append(conflictReason);
        }
        if (recommendedAction != null) {
            sb.append("\nRecommended Action: ").append(recommendedAction);
        } else {
            sb.append("\nSuggestion: Please review strategy parameters and ensure compatibility with track requirements.");
        }
        return sb.toString();
    }
}
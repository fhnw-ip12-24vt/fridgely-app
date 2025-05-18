package ch.primeo.fridgely.model;

/**
 * Represents the possible facial expressions of the penguin, each with a corresponding sprite.
 */
public enum PenguinFacialExpression {
    /**
     * Represents a happy facial expression.
     */
    HAPPY,
    /**
     * Represents a neutral facial expression.
     */
    NEUTRAL,
    /**
     * Represents an alert facial expression.
     */
    ALERT,
    /**
     * Represents a critical facial expression.
     */
    CRITICAL,
    /**
     * Represents an angry facial expression.
     */
    ANGRY,
    /**
     * Represents a disappointed facial expression.
     */
    DISAPPOINTED;

    /**
     * Returns the sprite path for this facial expression.
     *
     * @return the sprite image path
     */
    public String getSprite() {
        String basePath = "/ch/primeo/fridgely/sprites/";

        // Nice checkstyle rules, if cases are indented one more scope, it cries.
        return switch (this) {
            case HAPPY -> basePath + "happy.png";
            case NEUTRAL -> basePath + "neutral.png";
            case ALERT -> basePath + "alert.png";
            case CRITICAL -> basePath + "what.png";
            case ANGRY -> basePath + "angry.png";
            case DISAPPOINTED -> basePath + "disappointed.png";
        };
    }
}

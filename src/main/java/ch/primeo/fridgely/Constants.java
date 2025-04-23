package ch.primeo.fridgely;

import java.awt.Color;

/**
 * Utility class containing application-wide constants.
 */
public final class Constants {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Constants() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * The default background color used throughout the application (RGB: 248, 248, 255).
     * This is a very light gray with a slight blue tint.
     */
    public static final Color BACKGROUND_COLOR = new Color(248, 248, 255);
}

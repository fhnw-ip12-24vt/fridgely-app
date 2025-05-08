package ch.primeo.fridgely.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the PenguinFacialExpression enum.
 */
public class PenguinFacialExpressionTest {

    private static final String BASE_PATH = "/ch/primeo/fridgely/sprites/";

    @Test
    public void testHappySpritePath() {
        assertEquals(BASE_PATH + "happy.png", PenguinFacialExpression.HAPPY.getSprite());
    }

    @Test
    public void testNeutralSpritePath() {
        assertEquals(BASE_PATH + "neutral.png", PenguinFacialExpression.NEUTRAL.getSprite());
    }

    @Test
    public void testAlertSpritePath() {
        assertEquals(BASE_PATH + "alert.png", PenguinFacialExpression.ALERT.getSprite());
    }

    @Test
    public void testCriticalSpritePath() {
        assertEquals(BASE_PATH + "what.png", PenguinFacialExpression.CRITICAL.getSprite());
    }

    @Test
    public void testAngrySpritePath() {
        assertEquals(BASE_PATH + "angry.png", PenguinFacialExpression.ANGRY.getSprite());
    }

    @Test
    public void testDisappointedSpritePath() {
        assertEquals(BASE_PATH + "disappointed.png", PenguinFacialExpression.DISAPPOINTED.getSprite());
    }

    @Test
    public void testAllEnumValuesHaveSpriteImplementation() {
        // Ensure each enum value returns a non-null sprite path
        for (PenguinFacialExpression expression : PenguinFacialExpression.values()) {
            String spritePath = expression.getSprite();
            assertNotNull(spritePath, "Sprite path for " + expression + " should not be null");
            assertTrue(spritePath.startsWith(BASE_PATH), 
                    "Sprite path for " + expression + " should start with the base path");
            assertTrue(spritePath.endsWith(".png"), 
                    "Sprite path for " + expression + " should end with .png extension");
        }
    }
}

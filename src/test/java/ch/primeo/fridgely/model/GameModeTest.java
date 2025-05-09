package ch.primeo.fridgely.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test class for {@link GameMode} enum.
 */
public class GameModeTest {

    @Test
    public void testGameModeValues() {
        // Verify the GameMode enum contains exactly the expected values
        GameMode[] modes = GameMode.values();
        assertEquals(2, modes.length);
        assertEquals(GameMode.SinglePlayer, modes[0]);
        assertEquals(GameMode.Multiplayer, modes[1]);
    }

    @Test
    public void testGameModeValueOf() {
        // Test converting strings to enum values
        assertEquals(GameMode.SinglePlayer, GameMode.valueOf("SinglePlayer"));
        assertEquals(GameMode.Multiplayer, GameMode.valueOf("Multiplayer"));
    }

    @Test
    public void testGameModeValueOfInvalidName() {
        // Test that invalid enum name throws an exception
        assertThrows(IllegalArgumentException.class, () -> GameMode.valueOf("InvalidGameMode"));
    }

    @Test
    public void testGameModeToString() {
        // Test string representation of enum values
        assertEquals("SinglePlayer", GameMode.SinglePlayer.toString());
        assertEquals("Multiplayer", GameMode.Multiplayer.toString());
    }
}

package ch.primeo.fridgely.model;

import ch.primeo.fridgely.config.GameConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link PenguinHPState} enum.
 */
public class PenguinHPStateTest {

    @Test
    public void testSpritePathConstruction() {
        assertEquals("/ch/primeo/fridgely/sprites/ice_big_size.png", PenguinHPState.EXCELLENT.getSpritePath());
        assertEquals("/ch/primeo/fridgely/sprites/ice_middle_size.png", PenguinHPState.GOOD.getSpritePath());
        assertEquals("/ch/primeo/fridgely/sprites/penguin_on_small_block_of_ice.png",
                PenguinHPState.OKAY.getSpritePath());
        assertEquals("/ch/primeo/fridgely/sprites/penguin_swimming.png", PenguinHPState.STRUGGLING.getSpritePath());
        assertEquals("/ch/primeo/fridgely/sprites/penguin_on_fire.png", PenguinHPState.CRITICAL.getSpritePath());
        assertEquals("/ch/primeo/fridgely/sprites/penguin_unalive.png", PenguinHPState.DEAD.getSpritePath());
    }

    @Test
    public void testBoundaryValues() {
        assertEquals(PenguinHPState.EXCELLENT, PenguinHPState.fromHP(GameConfig.SCORE_EXCELLENT));
        assertEquals(PenguinHPState.GOOD, PenguinHPState.fromHP(GameConfig.SCORE_EXCELLENT - 1));
        assertEquals(PenguinHPState.GOOD, PenguinHPState.fromHP(GameConfig.SCORE_GOOD));
        assertEquals(PenguinHPState.OKAY, PenguinHPState.fromHP(GameConfig.SCORE_GOOD - 1));
        assertEquals(PenguinHPState.OKAY, PenguinHPState.fromHP(GameConfig.SCORE_OKAY));
        assertEquals(PenguinHPState.OKAY, PenguinHPState.fromHP(GameConfig.SCORE_CRITICAL+1));
        assertEquals(PenguinHPState.CRITICAL, PenguinHPState.fromHP(GameConfig.SCORE_CRITICAL));
        assertEquals(PenguinHPState.CRITICAL, PenguinHPState.fromHP(GameConfig.SCORE_DEAD+1));
        assertEquals(PenguinHPState.DEAD, PenguinHPState.fromHP(GameConfig.SCORE_DEAD));
    }
}

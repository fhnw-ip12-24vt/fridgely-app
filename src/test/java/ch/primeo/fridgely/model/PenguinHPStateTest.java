package ch.primeo.fridgely.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link PenguinHPState} enum.
 */
public class PenguinHPStateTest {

    @Test
    public void testSpritePathConstruction() {
        assertEquals("/ch/primeo/fridgely/sprites/ice_big_size.png", 
                     PenguinHPState.EXCELLENT.getSpritePath());
        assertEquals("/ch/primeo/fridgely/sprites/ice_middle_size.png", 
                     PenguinHPState.GOOD.getSpritePath());
        assertEquals("/ch/primeo/fridgely/sprites/penguin_on_small_block_of_ice.png", 
                     PenguinHPState.OKAY.getSpritePath());
        assertEquals("/ch/primeo/fridgely/sprites/penguin_swimming.png", 
                     PenguinHPState.STRUGGLING.getSpritePath());
        assertEquals("/ch/primeo/fridgely/sprites/penguin_on_fire.png", 
                     PenguinHPState.CRITICAL.getSpritePath());
        assertEquals("/ch/primeo/fridgely/sprites/penguin_unalive.png", 
                     PenguinHPState.DEAD.getSpritePath());
    }

    @ParameterizedTest
    @CsvSource({
        "100, EXCELLENT",
        "70, EXCELLENT",
        "60, EXCELLENT",
        "55, GOOD",
        "50, GOOD",
        "40, OKAY",
        "30, OKAY",
        "25, STRUGGLING",
        "20, STRUGGLING",
        "15, CRITICAL",
        "10, CRITICAL",
        "9, DEAD",
        "5, DEAD",
        "0, DEAD",
        "-5, DEAD"
    })
    public void testFromHP(int hp, PenguinHPState expected) {
        assertEquals(expected, PenguinHPState.fromHP(hp));
    }

    @Test
    public void testBoundaryValues() {
        assertEquals(PenguinHPState.EXCELLENT, PenguinHPState.fromHP(60));
        assertEquals(PenguinHPState.GOOD, PenguinHPState.fromHP(59));
        assertEquals(PenguinHPState.GOOD, PenguinHPState.fromHP(50));
        assertEquals(PenguinHPState.OKAY, PenguinHPState.fromHP(49));
        assertEquals(PenguinHPState.OKAY, PenguinHPState.fromHP(30));
        assertEquals(PenguinHPState.STRUGGLING, PenguinHPState.fromHP(29));
        assertEquals(PenguinHPState.STRUGGLING, PenguinHPState.fromHP(20));
        assertEquals(PenguinHPState.CRITICAL, PenguinHPState.fromHP(19));
        assertEquals(PenguinHPState.CRITICAL, PenguinHPState.fromHP(10));
        assertEquals(PenguinHPState.DEAD, PenguinHPState.fromHP(9));
    }
}

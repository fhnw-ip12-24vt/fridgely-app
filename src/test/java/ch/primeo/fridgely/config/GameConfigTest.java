package ch.primeo.fridgely.config;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameConfigTest {

    @Test
    void testDefaultRounds() {
        assertEquals(3, GameConfig.DEFAULT_ROUNDS);
    }

    @Test
    void testMinProductsPerRound() {
        assertEquals(3, GameConfig.MIN_PRODUCTS_PER_ROUND);
    }

    @Test
    void testStartingHp() {
        assertEquals(30, GameConfig.STARTING_HP);
    }

    @Test
    void testMaxHp() {
        assertEquals(60, GameConfig.MAX_HP);
    }

    @Test
    void testMinHp() {
        assertEquals(0, GameConfig.MIN_HP);
    }

    @Test
    void testHpIncrease() {
        assertEquals(5, GameConfig.HP_INCREASE);
    }

    @Test
    void testHpDecrease() {
        assertEquals(-5, GameConfig.HP_DECREASE);
    }

    @Test
    void testScoreBio() {
        assertEquals(15, GameConfig.SCORE_BIO);
    }

    @Test
    void testScoreLocal() {
        assertEquals(15, GameConfig.SCORE_LOCAL);
    }

    @Test
    void testScoreImported() {
        assertEquals(-10, GameConfig.SCORE_IMPORTED);
    }

    @Test
    void testScoreMatchingIngredient() {
        assertEquals(10, GameConfig.SCORE_MATCHING_INGREDIENT);
    }

    @Test
    void testScoreFullMatch() {
        assertEquals(10, GameConfig.SCORE_FULL_MATCH);
    }

    @Test
    void testConstructorThrowsException() {
        Constructor<GameConfig> constructor;
        try {
            constructor = GameConfig.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
            assertTrue(thrown.getCause() instanceof UnsupportedOperationException);
            assertEquals("GameConfig is a utility class and cannot be instantiated", thrown.getCause().getMessage());
        } catch (NoSuchMethodException e) {
            // This should not happen as the constructor exists
            throw new AssertionError("Constructor not found", e);
        }
    }
}
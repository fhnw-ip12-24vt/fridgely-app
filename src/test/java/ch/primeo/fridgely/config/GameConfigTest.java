package ch.primeo.fridgely.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}

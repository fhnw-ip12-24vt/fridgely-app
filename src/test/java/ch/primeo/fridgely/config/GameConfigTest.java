package ch.primeo.fridgely.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameConfigTest {

    @Test
    void testThrowsOnInstantiation() {
        assertThrows(UnsupportedOperationException.class, () -> new GameConfig());
    }

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
        assertEquals(1, GameConfig.SCORE_BIO);
    }

    @Test
    void testScoreNonBio() {
        assertEquals(0, GameConfig.SCORE_NON_BIO);
    }

    @Test
    void testScoreLocal() {
        assertEquals(2, GameConfig.SCORE_LOCAL);
    }

    @Test
    void testScoreNonLocal() {
        assertEquals(-2, GameConfig.SCORE_NON_LOCAL);
    }

    @Test
    void testScoreHighCo2() {
        assertEquals(-3, GameConfig.SCORE_HIGH_CO2);
    }

    @Test
    void testScoreLowCo2() {
        assertEquals(1, GameConfig.SCORE_LOW_CO2);
    }

    @Test
    void testScorePlayer1Increase() {
        assertEquals(8, GameConfig.SCORE_PLAYER1_INCREASE);
    }

    @Test
    void testScorePlayer1Decrease() {
        assertEquals(-12, GameConfig.SCORE_PLAYER1_DECREASE);
    }

    @Test
    void testScorePlayer2Increase() {
        assertEquals(8, GameConfig.SCORE_PLAYER2_INCREASE);
    }

    @Test
    void testScorePlayer2Decrease() {
        assertEquals(-4, GameConfig.SCORE_PLAYER2_DECREASE);
    }

    @Test
    void testScoreExcellent() {
        assertEquals(24, GameConfig.SCORE_EXCELLENT);
    }

    @Test
    void testScoreGood() {
        assertEquals(12, GameConfig.SCORE_GOOD);
    }

    @Test
    void testScoreOkay() {
        assertEquals(0, GameConfig.SCORE_OKAY);
    }

    @Test
    void testScoreCritical() {
        assertEquals(-12, GameConfig.SCORE_CRITICAL);
    }

    @Test
    void testScoreDead() {
        assertEquals(-24, GameConfig.SCORE_DEAD);
    }
}

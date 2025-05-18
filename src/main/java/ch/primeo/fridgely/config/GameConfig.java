package ch.primeo.fridgely.config;

/**
 * Configuration constants for the multiplayer game mode.
 * Contains default values for game settings such as rounds, minimum products, and HP values.
 */
public class GameConfig {

    protected GameConfig() {
        // Prevent instantiation
        throw new UnsupportedOperationException("GameConfig is a utility class and cannot be instantiated");
    }

    /**
     * Default number of rounds in a game.
     */
    public static final int DEFAULT_ROUNDS = 3;

    /**
     * Minimum number of products that must be scanned per round.
     */
    public static final int MIN_PRODUCTS_PER_ROUND = 3;

    /**
     * Maximum number of products that can be scanned per round.
     */
    public static final int MAX_PRODUCTS = 20;

    /**
     * Starting HP value for the penguin.
     */
    public static final int STARTING_HP = 30;

    /**
     * Maximum HP value for the penguin.
     */
    public static final int MAX_HP = 60;

    /**
     * Minimum HP value for the penguin.
     */
    public static final int MIN_HP = 0;

    /**
     * HP increase for environmentally friendly products.
     */
    public static final int HP_INCREASE = 5;

    /**
     * HP decrease for environmentally unfriendly products.
     */
    public static final int HP_DECREASE = -5;

    /**
     * Score for player 1.
     */
    public static final int SCORE_PLAYER1_INCREASE = 8;

    /**
     * Score penalty for player 1.
     */
    public static final int SCORE_PLAYER1_DECREASE = -12;

    /**
     * Score for Bio products.
     */
    public static final int SCORE_BIO = 1;

    /**
     * Penalty for non-bio products
     */
    public static final int SCORE_NON_BIO = 0;

    /**
     * Score for Local products.
     */
    public static final int SCORE_LOCAL = 2;

    /**
     * Score penalty for Imported products.
     */
    public static final int SCORE_NON_LOCAL = -2;

    /**
     * Score penalty for high co2 products.
     */
    public static final int SCORE_HIGH_CO2 = -3;

    /**
     * Score for products with low co2
     */
    public static final int SCORE_LOW_CO2 = 1;

    /**
     * Score for player 2.
     */
    public static final int SCORE_PLAYER2_INCREASE = 8;

    /**
     * Score penalty for player 2.
     */
    public static final int SCORE_PLAYER2_DECREASE = -4;

    /**
     * Score values for excellent rounds;
     */
    public static final int SCORE_EXCELLENT = 24;

    /**
     * Score values for good rounds;
     */
    public static final int SCORE_GOOD = 12;

    /**
     * Score values for okay rounds;
     */
    public static final int SCORE_OKAY = 0;

    /**
     * Score values for critical rounds;
     */
    public static final int SCORE_CRITICAL = -12;

    /**
     * Score values for game over/dead rounds;
     */
    public static final int SCORE_DEAD = -24;
}

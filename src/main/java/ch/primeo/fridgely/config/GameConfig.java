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


    public static final int SCORE_PLAYER1_INCREASE = 8;
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

    public static final int SCORE_PLAYER2_INCREASE = 8;
    public static final int SCORE_PLAYER2_DECREASE = -6;

    /**
     * Score values for the rounds;
     */
    public static final int SCORE_EXCELLENT = 24;
    public static final int SCORE_GOOD = 12;
    public static final int SCORE_OKAY = 0;
    public static final int SCORE_CRITICAL = -12;
    public static final int SCORE_DEAD = -24;
}

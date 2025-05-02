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
    
    /**
     * Score for Bio products.
     */
    public static final int SCORE_BIO = 15;
    
    /**
     * Score for Local products.
     */
    public static final int SCORE_LOCAL = 15;
    
    /**
     * Score penalty for Imported products.
     */
    public static final int SCORE_IMPORTED = -10;
    
    /**
     * Score for each matching recipe ingredient.
     */
    public static final int SCORE_MATCHING_INGREDIENT = 10;
    
    /**
     * Bonus score for a complete recipe match.
     */
    public static final int SCORE_FULL_MATCH = 10;
}

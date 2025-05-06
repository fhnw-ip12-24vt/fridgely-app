package ch.primeo.fridgely.model.singleplayer;

import ch.primeo.fridgely.config.GameConfig;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Model for tracking the overall game state in the singleplayer game mode.
 * Manages round number and scores.
 */
public class SingleplayerGameStateModel {

    /**
     * Property name for changes in the current round.
     */
    public static final String PROP_CURRENT_ROUND = "currentRound";

    public static final String PROP_CURRENT_PLAYER = "currentPlayer";

    /**
     * Property name for changes in player 1's score.
     */
    public static final String PROP_PLAYER_SCORE = "playerScore";

    /**
     * Property name for changes in the game over status.
     */
    public static final String PROP_GAME_OVER = "gameOver";

    private int currentRound;
    private int playerScore;
    private boolean gameOver;
    private final int totalRounds;
    private final PropertyChangeSupport propertyChangeSupport;

    /**
     * Constructs a new game state model with the default number of rounds.
     */
    public SingleplayerGameStateModel() {
        this(GameConfig.DEFAULT_ROUNDS);
    }

    /**
     * Constructs a new game state model with the specified number of rounds.
     *
     * @param totRounds the total number of rounds in the game
     */
    public SingleplayerGameStateModel(int totRounds) {
        this.currentRound = 1;
        this.playerScore = 0;
        this.gameOver = false;
        this.totalRounds = totRounds;
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    /**
     * Gets the current round number.
     *
     * @return the current round number
     */
    public int getCurrentRound() {
        return currentRound;
    }

    /**
     * Gets the total number of rounds in the game.
     *
     * @return the total number of rounds
     */
    public int getTotalRounds() {
        return totalRounds;
    }

    /**
     * Gets player score.
     *
     * @return player score
     */
    public int getPlayerScore() {
        return playerScore;
    }

    /**
     * Checks if the game is over.
     *
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Advances to the next round. Sets the game to over if this was the last round.
     */
    public void advanceRound() {
        int oldRound = currentRound;
        currentRound++;
        propertyChangeSupport.firePropertyChange(PROP_CURRENT_ROUND, oldRound, currentRound);

        if (currentRound > totalRounds) {
            boolean oldGameOver = gameOver;
            gameOver = true;
            propertyChangeSupport.firePropertyChange(PROP_GAME_OVER, oldGameOver, gameOver);
        }


    }

    /**
     * Adds points to player score.
     *
     * @param points the points to add
     */
    public void addPlayerScore(int points) {
        int oldScore = playerScore;
        playerScore += points;
        propertyChangeSupport.firePropertyChange(PROP_PLAYER_SCORE, oldScore, playerScore);
    }

    /**
     * Resets the game state to start a new game.
     */
    public void resetGame() {
        int oldRound = currentRound;
        int oldPlayerScore = playerScore;
        boolean oldGameOver = gameOver;

        currentRound = 1;
        playerScore = 0;
        gameOver = false;

        propertyChangeSupport.firePropertyChange(PROP_CURRENT_ROUND, oldRound, currentRound);
        propertyChangeSupport.firePropertyChange(PROP_PLAYER_SCORE, oldPlayerScore, playerScore);
        propertyChangeSupport.firePropertyChange(PROP_GAME_OVER, oldGameOver, gameOver);
    }

    /**
     * Adds a property change listener.
     *
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Removes a property change listener.
     *
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}

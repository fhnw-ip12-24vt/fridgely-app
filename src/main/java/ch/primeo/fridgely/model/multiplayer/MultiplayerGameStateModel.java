package ch.primeo.fridgely.model.multiplayer;

import ch.primeo.fridgely.config.GameConfig;
import lombok.Getter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Model for tracking the overall game state in the multiplayer game mode. Manages round number, current player, and
 * scores.
 */
public class MultiplayerGameStateModel {

    /**
     * Enum representing the current player in the game.
     */
    public enum Player {
        /**
         * Player 1 (Scanner).
         */
        PLAYER1,

        /**
         * Player 2 (Chef).
         */
        PLAYER2
    }

    /**
     * Property name for changes in the current round.
     */
    public static final String PROP_CURRENT_ROUND = "currentRound";

    /**
     * Property name for changes in the current player.
     */
    public static final String PROP_CURRENT_PLAYER = "currentPlayer";

    /**
     * Property name for changes in player 1's score.
     */
    public static final String PROP_PLAYER1_SCORE = "player1Score";

    /**
     * Property name for changes in player 2's score.
     */
    public static final String PROP_PLAYER2_SCORE = "player2Score";

    /**
     * Property name for changes in the game over status.
     */
    public static final String PROP_GAME_OVER = "gameOver";

    @Getter
    private int currentRound;

    @Getter
    private Player currentPlayer;

    @Getter
    private int player1Score;

    @Getter
    private int player2Score;

    @Getter
    private boolean gameOver;

    @Getter
    private final int totalRounds;

    private final PropertyChangeSupport propertyChangeSupport;

    /**
     * Constructs a new game state model with the default number of rounds.
     */
    public MultiplayerGameStateModel() {
        this(GameConfig.DEFAULT_ROUNDS);
    }

    /**
     * Constructs a new game state model with the specified number of rounds.
     *
     * @param totRounds the total number of rounds in the game
     */
    public MultiplayerGameStateModel(int totRounds) {
        this.currentRound = 1;
        this.currentPlayer = Player.PLAYER1;
        this.player1Score = 0;
        this.player2Score = 0;
        this.gameOver = false;
        this.totalRounds = totRounds;
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    /**
     * Switches to the next player. If currently player 2, advances to the next round.
     */
    public void nextPlayer() {
        Player oldPlayer = currentPlayer;
        if (currentPlayer == Player.PLAYER1) {
            currentPlayer = Player.PLAYER2;
        } else {
            currentPlayer = Player.PLAYER1;
            advanceRound();
        }
        propertyChangeSupport.firePropertyChange(PROP_CURRENT_PLAYER, oldPlayer, currentPlayer);
    }

    /**
     * Advances to the next round. Sets the game to over if this was the last round.
     */
    private void advanceRound() {
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
     * Adds points to player 1's score.
     *
     * @param points the points to add
     */
    public void addPlayer1Score(int points) {
        int oldScore = player1Score;
        player1Score += points;
        propertyChangeSupport.firePropertyChange(PROP_PLAYER1_SCORE, oldScore, player1Score);
    }

    /**
     * Adds points to player 2's score.
     *
     * @param points the points to add
     */
    public void addPlayer2Score(int points) {
        int oldScore = player2Score;
        player2Score += points;
        propertyChangeSupport.firePropertyChange(PROP_PLAYER2_SCORE, oldScore, player2Score);
    }

    /**
     * Resets the game state to start a new game.
     */
    public void resetGame() {
        int oldRound = currentRound;
        Player oldPlayer = currentPlayer;
        int oldPlayer1Score = player1Score;
        int oldPlayer2Score = player2Score;
        boolean oldGameOver = gameOver;

        currentRound = 1;
        currentPlayer = Player.PLAYER1;
        player1Score = 0;
        player2Score = 0;
        gameOver = false;

        propertyChangeSupport.firePropertyChange(PROP_CURRENT_ROUND, oldRound, currentRound);
        propertyChangeSupport.firePropertyChange(PROP_CURRENT_PLAYER, oldPlayer, currentPlayer);
        propertyChangeSupport.firePropertyChange(PROP_PLAYER1_SCORE, oldPlayer1Score, player1Score);
        propertyChangeSupport.firePropertyChange(PROP_PLAYER2_SCORE, oldPlayer2Score, player2Score);
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

    /**
     * Gets the winner of the game, or null if there's a tie or the game isn't over.
     *
     * @return Player.PLAYER1, Player.PLAYER2, or null for a tie
     */
    public Player getWinner() {
        if (!gameOver) {
            return null;
        }

        if (player1Score > player2Score) {
            return Player.PLAYER1;
        } else if (player2Score > player1Score) {
            return Player.PLAYER2;
        } else {
            return null; // Tie
        }
    }
}

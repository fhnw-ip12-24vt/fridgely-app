package ch.primeo.fridgely.model.multiplayer;

import ch.primeo.fridgely.config.GameConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;

class MultiplayerGameStateModelTest {

    private MultiplayerGameStateModel gameState;

    @BeforeEach
    void setUp() {
        gameState = new MultiplayerGameStateModel(3); // 3 Runden für Tests
    }

    @Test
    void testInitialState() {
        // Arrange & Act
        // Assert
        assertEquals(1, gameState.getCurrentRound());
        assertEquals(MultiplayerGameStateModel.Player.PLAYER1, gameState.getCurrentPlayer());
        assertEquals(0, gameState.getPlayer1Score());
        assertEquals(0, gameState.getPlayer2Score());
        assertFalse(gameState.isGameOver());
        assertEquals(3, gameState.getTotalRounds());
    }

    @Test
    void testNextPlayerSwitchesCorrectly() {
        // Arrange
        MultiplayerGameStateModel.Player initialPlayer = gameState.getCurrentPlayer();

        // Act
        gameState.nextPlayer();

        // Assert
        assertNotEquals(initialPlayer, gameState.getCurrentPlayer());
        assertEquals(MultiplayerGameStateModel.Player.PLAYER2, gameState.getCurrentPlayer());
    }

    @Test
    void testNextPlayerAdvancesRound() {
        // Arrange
        gameState.nextPlayer(); // Zu Player 2 wechseln

        // Act
        gameState.nextPlayer(); // Runde sollte voranschreiten

        // Assert
        assertEquals(2, gameState.getCurrentRound());
        assertEquals(MultiplayerGameStateModel.Player.PLAYER1, gameState.getCurrentPlayer());
    }

    @Test
    void testGameOverAfterLastRound() {
        // Arrange
        gameState.nextPlayer(); // Runde 1, Player 2
        gameState.nextPlayer(); // Runde 2, Player 1
        gameState.nextPlayer(); // Runde 2, Player 2
        gameState.nextPlayer(); // Runde 3, Player 1
        gameState.nextPlayer(); // Runde 3, Player 2

        // Act
        gameState.nextPlayer(); // Runde 4, Spiel vorbei

        // Assert
        assertTrue(gameState.isGameOver());
    }

    @Test
    void testAddPlayer1Score() {
        // Arrange
        int initialScore = gameState.getPlayer1Score();

        // Act
        gameState.addPlayer1Score(10);

        // Assert
        assertEquals(initialScore + 10, gameState.getPlayer1Score());
    }

    @Test
    void testAddPlayer2Score() {
        // Arrange
        int initialScore = gameState.getPlayer2Score();

        // Act
        gameState.addPlayer2Score(15);

        // Assert
        assertEquals(initialScore + 15, gameState.getPlayer2Score());
    }

    @Test
    void testGetWinnerPlayer1Wins() {
        // Arrange
        gameState.addPlayer1Score(20);
        gameState.addPlayer2Score(10);

        // Act
        while(!gameState.isGameOver()){
            gameState.nextPlayer();
        }
        MultiplayerGameStateModel.Player winner = gameState.getWinner();

        // Assert
        assertEquals(MultiplayerGameStateModel.Player.PLAYER1, winner);
    }

    @Test
    void testGetWinnerPlayer2Wins() {
        // Arrange
        gameState.addPlayer1Score(10);
        gameState.addPlayer2Score(20);

        // Act
        while(!gameState.isGameOver()){
            gameState.nextPlayer();
        }
        MultiplayerGameStateModel.Player winner = gameState.getWinner();

        // Assert
        assertEquals(MultiplayerGameStateModel.Player.PLAYER2, winner);
    }

    @Test
    void testGetWinnerTie() {
        // Arrange
        gameState.addPlayer1Score(15);
        gameState.addPlayer2Score(15);
        gameState.nextPlayer(); // Spiel beenden
        gameState.nextPlayer();
        gameState.nextPlayer();
        gameState.nextPlayer();
        gameState.nextPlayer();

        // Act
        MultiplayerGameStateModel.Player winner = gameState.getWinner();

        // Assert
        assertNull(winner);
    }

    @Test
    void testResetGame() {
        // Arrange
        gameState.addPlayer1Score(10);
        gameState.addPlayer2Score(20);
        gameState.nextPlayer();
        gameState.nextPlayer();
        gameState.nextPlayer();

        // Act
        gameState.resetGame();

        // Assert
        assertEquals(1, gameState.getCurrentRound());
        assertEquals(MultiplayerGameStateModel.Player.PLAYER1, gameState.getCurrentPlayer());
        assertEquals(0, gameState.getPlayer1Score());
        assertEquals(0, gameState.getPlayer2Score());
        assertFalse(gameState.isGameOver());
    }

    @Test
    void testPropertyChangeListener() {
        // Arrange
        PropertyChangeListener mockListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                assertEquals(MultiplayerGameStateModel.PROP_CURRENT_PLAYER, evt.getPropertyName());
                assertEquals(MultiplayerGameStateModel.Player.PLAYER1, evt.getOldValue());
                assertEquals(MultiplayerGameStateModel.Player.PLAYER2, evt.getNewValue());
            }
        };
        gameState.addPropertyChangeListener(mockListener);

        // Act & Assert
        // Listener wird automatisch geprüft
        assertDoesNotThrow(() -> gameState.nextPlayer()); // Sicherstellen, dass keine Exception geworfen wird
    }
}
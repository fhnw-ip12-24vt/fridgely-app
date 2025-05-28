package ch.primeo.fridgely.model.multiplayer;

import org.junit.jupiter.api.*;

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
        assertEquals(0, gameState.getScore());
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
    void testAddScore() {
        // Arrange
        int initialScore = gameState.getScore();

        // Act
        gameState.addScore(10);

        // Assert
        assertEquals(initialScore + 10, gameState.getScore());
    }

    @Test
    void testResetGame() {
        // Arrange
        gameState.addScore(10);
        gameState.nextPlayer();
        gameState.nextPlayer();
        gameState.nextPlayer();

        // Act
        gameState.resetGame();

        // Assert
        assertEquals(1, gameState.getCurrentRound());
        assertEquals(MultiplayerGameStateModel.Player.PLAYER1, gameState.getCurrentPlayer());
        assertEquals(0, gameState.getScore());
        assertFalse(gameState.isGameOver());
    }

    @Test
    void testPropertyChangeListener() {
        // Arrange
        PropertyChangeListener mockListener = evt -> {
            assertEquals(MultiplayerGameStateModel.PROP_CURRENT_PLAYER, evt.getPropertyName());
            assertEquals(MultiplayerGameStateModel.Player.PLAYER1, evt.getOldValue());
            assertEquals(MultiplayerGameStateModel.Player.PLAYER2, evt.getNewValue());
        };
        gameState.addPropertyChangeListener(mockListener);

        // Act & Assert
        // Listener wird automatisch geprüft
        assertDoesNotThrow(() -> gameState.nextPlayer()); // Sicherstellen, dass keine Exception geworfen wird
    }

    @Test
    void testRemovePropertyChangeListener() {
        // Arrange
        final boolean[] listenerCalled = {false};
        PropertyChangeListener mockListener = evt -> listenerCalled[0] = true;

        gameState.addPropertyChangeListener(mockListener);
        gameState.removePropertyChangeListener(mockListener);

        // Act
        gameState.nextPlayer(); // Trigger a property change

        // Assert
        assertFalse(listenerCalled[0], "Listener should not be called after being removed.");
    }
}

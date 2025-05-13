package ch.primeo.fridgely.controller.multiplayer;

import ch.primeo.fridgely.FridgelyContext;
import ch.primeo.fridgely.controller.ChooseGameModeController;
import ch.primeo.fridgely.model.multiplayer.MultiplayerGameStateModel;
import ch.primeo.fridgely.service.ProductRepository;
import ch.primeo.fridgely.service.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class MultiplayerGameControllerTest {

    private MultiplayerGameController controller;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        RecipeRepository recipeRepository = mock(RecipeRepository.class);
        controller = new MultiplayerGameController(productRepository, recipeRepository);
    }

    @Test
    void testInitialization() {
        assertNotNull(controller.getGameStateModel());
        assertNotNull(controller.getPlayer1Controller());
        assertNotNull(controller.getPlayer2Controller());
    }

    @Test
    void testStartNewGame() {
        controller.startNewGame();
        MultiplayerGameStateModel gameState = controller.getGameStateModel();
        assertEquals(1, gameState.getCurrentRound());
        assertEquals(MultiplayerGameStateModel.Player.PLAYER1, gameState.getCurrentPlayer());
        assertEquals(0, gameState.getScore());
        assertFalse(gameState.isGameOver());
    }

    @Test
    void testGetters() {
        assertNotNull(controller.getGameStateModel());
        assertNotNull(controller.getPenguinModel());
        assertNotNull(controller.getFridgeStockModel());
        assertNotNull(controller.getRecipeModel());
        assertNotNull(controller.getPlayer1Controller());
        assertNotNull(controller.getPlayer2Controller());
    }

    @Test
    void testGetProductRepository() {
        assertSame(productRepository, controller.getProductRepository());
    }

    @Test
    void testExitGame() {
        // Arrange
        ChooseGameModeController mockChooseGameController = mock(ChooseGameModeController.class);

        try (MockedStatic<FridgelyContext> mockedStatic = Mockito.mockStatic(FridgelyContext.class)) {
            // Setup static mock
            mockedStatic.when(() -> FridgelyContext.getBean(ChooseGameModeController.class))
                    .thenReturn(mockChooseGameController);

            // Act
            controller.exitGame();

            // Assert
            verify(mockChooseGameController).showChooseGameModeView();
        }
    }
}

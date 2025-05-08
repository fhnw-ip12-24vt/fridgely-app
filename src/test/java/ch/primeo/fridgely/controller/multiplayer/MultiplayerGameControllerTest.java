package ch.primeo.fridgely.controller.multiplayer;

import ch.primeo.fridgely.service.ProductRepository;
import ch.primeo.fridgely.service.RecipeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import ch.primeo.fridgely.model.multiplayer.MultiplayerGameStateModel;

class MultiplayerGameControllerTest {

    private MultiplayerGameController controller;
    private ProductRepository productRepository;
    private RecipeRepository recipeRepository;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        recipeRepository = mock(RecipeRepository.class);
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
        assertEquals(0, gameState.getPlayer1Score());
        assertEquals(0, gameState.getPlayer2Score());
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
}
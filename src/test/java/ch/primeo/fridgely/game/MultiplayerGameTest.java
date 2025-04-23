package ch.primeo.fridgely.game;

import ch.primeo.fridgely.config.GameConfig;
import ch.primeo.fridgely.controller.GameController;
import ch.primeo.fridgely.model.FridgeStockModel;
import ch.primeo.fridgely.model.GameStateModel;
import ch.primeo.fridgely.model.PenguinModel;
import ch.primeo.fridgely.model.Product;
import ch.primeo.fridgely.service.ProductRepository;
import ch.primeo.fridgely.service.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for the multiplayer game implementation.
 */
public class MultiplayerGameTest {

    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private RecipeRepository recipeRepository;
    
    private GameController gameController;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        gameController = new GameController(productRepository, recipeRepository);
    }
    
    @Test
    public void testGameInitialization() {
        // Verify initial game state
        GameStateModel gameState = gameController.getGameStateModel();
        assertEquals(1, gameState.getCurrentRound());
        assertEquals(GameStateModel.Player.PLAYER1, gameState.getCurrentPlayer());
        assertEquals(0, gameState.getPlayer1Score());
        assertEquals(0, gameState.getPlayer2Score());
        assertFalse(gameState.isGameOver());
        
        // Verify penguin initial HP
        PenguinModel penguin = gameController.getPenguinModel();
        assertEquals(GameConfig.STARTING_HP, penguin.getHP());
        
        // Verify fridge starts empty
        FridgeStockModel fridgeStock = gameController.getFridgeStockModel();
        assertEquals(0, fridgeStock.getProductCount());
    }
    
    @Test
    public void testProductScoring() {
        // Create test products
        Product bioProd = new Product("123", "Bio Apple", "", "", "An organic apple", "", "", false, true, false);
        Product localProd = new Product("456", "Local Carrot", "", "", "A local carrot", "", "", false, false, true);
        Product importedProd = new Product("789", "Imported Banana", "", "", "An imported banana", "", "", false, false, false);
        Product bioLocalProd = new Product("101", "Bio Local Tomato", "", "", "A bio and local tomato", "", "", false, true, true);
        
        // Mock repository to return our test products
        when(productRepository.findByBarcode("123")).thenReturn(bioProd);
        when(productRepository.findByBarcode("456")).thenReturn(localProd);
        when(productRepository.findByBarcode("789")).thenReturn(importedProd);
        when(productRepository.findByBarcode("101")).thenReturn(bioLocalProd);
        
        // Test bio product scoring
        gameController.getPlayer1Controller().scanProduct("123");
        assertEquals(GameConfig.SCORE_BIO, gameController.getGameStateModel().getPlayer1Score());
        assertEquals(GameConfig.STARTING_HP + GameConfig.HP_INCREASE, gameController.getPenguinModel().getHP());
        
        // Reset for next test
        gameController.startNewGame();
        
        // Test local product scoring
        gameController.getPlayer1Controller().scanProduct("456");
        assertEquals(GameConfig.SCORE_LOCAL, gameController.getGameStateModel().getPlayer1Score());
        assertEquals(GameConfig.STARTING_HP + GameConfig.HP_INCREASE, gameController.getPenguinModel().getHP());
        
        // Reset for next test
        gameController.startNewGame();
        
        // Test imported product scoring
        gameController.getPlayer1Controller().scanProduct("789");
        assertEquals(GameConfig.SCORE_IMPORTED, gameController.getGameStateModel().getPlayer1Score());
        assertEquals(GameConfig.STARTING_HP + GameConfig.HP_DECREASE, gameController.getPenguinModel().getHP());
        
        // Reset for next test
        gameController.startNewGame();
        
        // Test bio+local product scoring
        gameController.getPlayer1Controller().scanProduct("101");
        assertEquals(GameConfig.SCORE_BIO + GameConfig.SCORE_LOCAL, gameController.getGameStateModel().getPlayer1Score());
        assertEquals(GameConfig.STARTING_HP + GameConfig.HP_INCREASE, gameController.getPenguinModel().getHP());
    }
}

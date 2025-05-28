package ch.primeo.fridgely.controller.multiplayer;

import ch.primeo.fridgely.config.GameConfig;
import ch.primeo.fridgely.model.FridgeStockModel;
import ch.primeo.fridgely.model.PenguinModel;
import ch.primeo.fridgely.model.Product;
import ch.primeo.fridgely.model.Recipe;
import ch.primeo.fridgely.model.RecipeModel;
import ch.primeo.fridgely.model.multiplayer.MultiplayerGameStateModel;
import ch.primeo.fridgely.service.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Integration test to verify that the soft lock bug is fixed when no recipes are available.
 */
class MultiplayerNoRecipesSoftLockTest {

    private MultiplayerPlayer1Controller controller;
    private FridgeStockModel fridgeStockModel;
    private MultiplayerGameStateModel gameStateModel;
    private RecipeModel recipeModel;

    @BeforeEach
    void setUp() {
        fridgeStockModel = mock(FridgeStockModel.class);
        gameStateModel = mock(MultiplayerGameStateModel.class);
        PenguinModel penguinModel = mock(PenguinModel.class);
        recipeModel = mock(RecipeModel.class);
        ProductRepository productRepository = mock(ProductRepository.class);

        controller = new MultiplayerPlayer1Controller(fridgeStockModel, gameStateModel, penguinModel, productRepository, recipeModel);
    }

    @Test
    void testSoftLockPrevention_WithNoRecipesAvailable() {
        // Arrange: Set up scenario where player has minimum products but no recipes are available
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < GameConfig.MIN_PRODUCTS_PER_ROUND; i++) {
            products.add(mock(Product.class));
        }
        
        when(fridgeStockModel.getFridgeProducts()).thenReturn(products);
        when(fridgeStockModel.getProducts()).thenReturn(products);
        when(recipeModel.getPossibleRecipes(products)).thenReturn(List.of()); // No recipes available

        // Act: Try to finish turn
        boolean turnFinished = controller.finishTurn();

        // Assert: Turn should not finish, preventing soft lock
        assertFalse(turnFinished, "Turn should not finish when no recipes are available");
        verify(gameStateModel, never()).addScore(anyInt());
        verify(gameStateModel, never()).nextPlayer();
    }

    @Test
    void testSoftLockPrevention_WithRecipesAvailable() {
        // Arrange: Set up scenario where player has minimum products and recipes are available
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < GameConfig.MIN_PRODUCTS_PER_ROUND; i++) {
            products.add(mock(Product.class));
        }
        
        when(fridgeStockModel.getFridgeProducts()).thenReturn(products);
        when(fridgeStockModel.getProducts()).thenReturn(products);
        when(recipeModel.getPossibleRecipes(products)).thenReturn(List.of(mock(Recipe.class))); // Recipes available

        // Act: Try to finish turn
        boolean turnFinished = controller.finishTurn();

        // Assert: Turn should finish successfully
        assertTrue(turnFinished, "Turn should finish when recipes are available");
        verify(gameStateModel).addScore(anyInt());
        verify(gameStateModel).nextPlayer();
    }

    @Test
    void testHasAvailableRecipes_Integration() {
        // Arrange
        List<Product> products = List.of(mock(Product.class), mock(Product.class));
        when(fridgeStockModel.getProducts()).thenReturn(products);
        
        // Test with no recipes
        when(recipeModel.getPossibleRecipes(products)).thenReturn(List.of());
        assertFalse(controller.hasAvailableRecipes(), "Should return false when no recipes available");
        
        // Test with recipes
        when(recipeModel.getPossibleRecipes(products)).thenReturn(List.of(mock(Recipe.class)));
        assertTrue(controller.hasAvailableRecipes(), "Should return true when recipes are available");
    }

    @Test
    void testFinishTurnRequirements_AllConditions() {
        // Test all conditions that must be met for finishTurn to succeed
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < GameConfig.MIN_PRODUCTS_PER_ROUND; i++) {
            products.add(mock(Product.class));
        }
        
        when(fridgeStockModel.getFridgeProducts()).thenReturn(products);
        when(fridgeStockModel.getProducts()).thenReturn(products);
        when(recipeModel.getPossibleRecipes(products)).thenReturn(List.of(mock(Recipe.class)));

        // All conditions met - should succeed
        assertTrue(controller.finishTurn(), "Should succeed when all conditions are met");
        
        // Reset mocks
        reset(gameStateModel);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        
        // Test with insufficient products
        List<Product> fewProducts = List.of(mock(Product.class)); // Less than MIN_PRODUCTS_PER_ROUND
        when(fridgeStockModel.getFridgeProducts()).thenReturn(fewProducts);
        assertFalse(controller.finishTurn(), "Should fail with insufficient products");
        
        // Reset and test wrong player
        reset(gameStateModel);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER2);
        assertFalse(controller.finishTurn(), "Should fail when not player 1's turn");
    }
}

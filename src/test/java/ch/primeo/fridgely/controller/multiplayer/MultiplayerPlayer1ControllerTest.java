package ch.primeo.fridgely.controller.multiplayer;

import ch.primeo.fridgely.model.FridgeStockModel;
import ch.primeo.fridgely.config.GameConfig;
import ch.primeo.fridgely.model.PenguinModel;
import ch.primeo.fridgely.model.RecipeModel;
import ch.primeo.fridgely.model.multiplayer.MultiplayerGameStateModel;
import ch.primeo.fridgely.service.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import ch.primeo.fridgely.model.Product;

class MultiplayerPlayer1ControllerTest {

    private MultiplayerPlayer1Controller controller;
    private FridgeStockModel fridgeStockModel;
    private MultiplayerGameStateModel gameStateModel;
    private PenguinModel penguinModel;
    private RecipeModel recipeModel;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        fridgeStockModel = mock(FridgeStockModel.class);
        gameStateModel = mock(MultiplayerGameStateModel.class);
        penguinModel = mock(PenguinModel.class);
        recipeModel = mock(RecipeModel.class);
        productRepository = mock(ProductRepository.class);

        controller = new MultiplayerPlayer1Controller(
            fridgeStockModel, gameStateModel, penguinModel, productRepository, recipeModel
        );
    }

    @Test
    void testScanProductValidBarcode() {
        String validBarcode = "123456";
        Product mockProduct = mock(Product.class);
        when(productRepository.getProductByBarcode(validBarcode)).thenReturn(mockProduct);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        when(fridgeStockModel.addProduct(mockProduct)).thenReturn(true);

        controller.scanProduct(validBarcode);

        verify(fridgeStockModel).addProduct(mockProduct);
        verify(gameStateModel).addPlayer1Score(anyInt());
        verify(penguinModel).modifyHP(anyInt());
    }

    @Test
    void testScanProductInvalidBarcode() {
        String invalidBarcode = "invalid";
        when(productRepository.getProductByBarcode(invalidBarcode)).thenReturn(null);

        controller.scanProduct(invalidBarcode);

        verify(fridgeStockModel, never()).addProduct(any());
        verify(gameStateModel, never()).addPlayer1Score(anyInt());
        verify(penguinModel, never()).modifyHP(anyInt());
    }

    @Test
    void testRemoveProduct() {
        Product mockProduct = mock(Product.class);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        when(fridgeStockModel.removeProduct(mockProduct)).thenReturn(true);

        controller.removeProduct(mockProduct);

        verify(fridgeStockModel).removeProduct(mockProduct);
        verify(gameStateModel).addPlayer1Score(anyInt());
    }

    @Test
    void testFinishTurn() {
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        when(fridgeStockModel.getProductCount()).thenReturn(GameConfig.MIN_PRODUCTS_PER_ROUND);

        controller.finishTurn();

        verify(gameStateModel).nextPlayer();
        verify(recipeModel).loadAvailableRecipes(anyList());
    }

    @Test
    void testResetForNewRound() {
        controller.resetForNewRound();

        verify(fridgeStockModel).clear();
    }
}
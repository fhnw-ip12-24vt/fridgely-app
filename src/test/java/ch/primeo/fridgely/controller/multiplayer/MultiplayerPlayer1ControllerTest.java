package ch.primeo.fridgely.controller.multiplayer;

import ch.primeo.fridgely.config.GameConfig;
import ch.primeo.fridgely.model.FridgeStockModel;
import ch.primeo.fridgely.model.PenguinModel;
import ch.primeo.fridgely.model.Product;
import ch.primeo.fridgely.model.RecipeModel;
import ch.primeo.fridgely.model.multiplayer.MultiplayerGameStateModel;
import ch.primeo.fridgely.service.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        controller = new MultiplayerPlayer1Controller(fridgeStockModel, gameStateModel, penguinModel, productRepository);
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
    }

    @Test
    void testScanProductInvalidBarcode() {
        String invalidBarcode = "invalid";
        when(productRepository.getProductByBarcode(invalidBarcode)).thenReturn(null);

        controller.scanProduct(invalidBarcode);

        verify(fridgeStockModel, never()).addProduct(any());
        verify(gameStateModel, never()).addScore(anyInt());
        verify(penguinModel, never()).modifyHP(anyInt());
    }

    @Test
    void testScanProductWhenNotPlayer1Turn() {
        String validBarcode = "123456";
        Product mockProduct = mock(Product.class);
        when(productRepository.getProductByBarcode(validBarcode)).thenReturn(mockProduct);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER2);

        Product result = controller.scanProduct(validBarcode);

        assertNull(result);
        verify(fridgeStockModel, never()).addProduct(any());
        verify(gameStateModel, never()).addScore(anyInt());
    }

    @Test
    void testScanProductAdditionFailed() {
        String barcode = "123456";
        Product mockProduct = mock(Product.class);
        when(productRepository.getProductByBarcode(barcode)).thenReturn(mockProduct);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        when(fridgeStockModel.getProducts()).thenReturn(new ArrayList<>());
        when(fridgeStockModel.addProduct(mockProduct)).thenReturn(false);

        Product result = controller.scanProduct(barcode);

        assertNull(result);
        verify(gameStateModel, never()).addScore(anyInt());
        verify(penguinModel, never()).modifyHP(anyInt());
    }

    @Test
    void testFinishTurnWhenNotPlayer1Turn() {
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER2);

        controller.finishTurn();

        verify(gameStateModel, never()).nextPlayer();
        verify(recipeModel, never()).getPossibleRecipes(anyList());
    }

    @Test
    void testFinishTurnWithNotEnoughProducts() {
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        when(fridgeStockModel.getFridgeProducts()).thenReturn(List.of(mock(Product.class), mock(Product.class)));;

        controller.finishTurn();

        verify(gameStateModel, never()).nextPlayer();
        verify(recipeModel, never()).getPossibleRecipes(anyList());
    }

    @Test
    void testScanProductNullProduct() {
        // Setup when the product repository returns null
        String barcode = "123456";
        when(productRepository.getProductByBarcode(barcode)).thenReturn(null);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);

        Product result = controller.scanProduct(barcode);

        assertNull(result);
        verify(fridgeStockModel, never()).addProduct(any());
        verify(fridgeStockModel, never()).getProducts();
        verify(gameStateModel, never()).addScore(anyInt());
        verify(penguinModel, never()).modifyHP(anyInt());
    }
}

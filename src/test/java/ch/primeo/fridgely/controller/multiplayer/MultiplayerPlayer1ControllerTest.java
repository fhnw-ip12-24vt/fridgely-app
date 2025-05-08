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
import java.util.ArrayList;
import java.util.List;

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

    @Test
    void testScanProductWhenNotPlayer1Turn() {
        String validBarcode = "123456";
        Product mockProduct = mock(Product.class);
        when(productRepository.getProductByBarcode(validBarcode)).thenReturn(mockProduct);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER2);

        Product result = controller.scanProduct(validBarcode);
        
        assertNull(result);
        verify(fridgeStockModel, never()).addProduct(any());
        verify(gameStateModel, never()).addPlayer1Score(anyInt());
    }

    @Test
    void testScanProductToggleRemoval() {
        // Setup for removing an existing product
        String barcode = "123456";
        Product mockProduct = mock(Product.class);
        List<Product> productList = new ArrayList<>();
        productList.add(mockProduct);
        
        when(productRepository.getProductByBarcode(barcode)).thenReturn(mockProduct);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        when(fridgeStockModel.getProducts()).thenReturn(productList);
        when(fridgeStockModel.removeProduct(mockProduct)).thenReturn(true);
        when(mockProduct.isBio()).thenReturn(true);
        
        Product result = controller.scanProduct(barcode);
        
        assertEquals(mockProduct, result);
        verify(fridgeStockModel).removeProduct(mockProduct);
        verify(gameStateModel).addPlayer1Score(anyInt());
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
        verify(gameStateModel, never()).addPlayer1Score(anyInt());
        verify(penguinModel, never()).modifyHP(anyInt());
    }
    
    @Test
    void testRemoveProductWhenNotPlayer1Turn() {
        Product mockProduct = mock(Product.class);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER2);

        controller.removeProduct(mockProduct);
        
        verify(fridgeStockModel, never()).removeProduct(any());
        verify(gameStateModel, never()).addPlayer1Score(anyInt());
    }
    
    @Test
    void testRemoveProductFailed() {
        Product mockProduct = mock(Product.class);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        when(fridgeStockModel.removeProduct(mockProduct)).thenReturn(false);

        controller.removeProduct(mockProduct);
        
        verify(gameStateModel, never()).addPlayer1Score(anyInt());
        verify(penguinModel, never()).modifyHP(anyInt());
    }

    @Test
    void testFinishTurnWhenNotPlayer1Turn() {
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER2);
        
        controller.finishTurn();
        
        verify(gameStateModel, never()).nextPlayer();
        verify(recipeModel, never()).loadAvailableRecipes(anyList());
    }
    
    @Test
    void testFinishTurnWithNotEnoughProducts() {
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        when(fridgeStockModel.getProductCount()).thenReturn(GameConfig.MIN_PRODUCTS_PER_ROUND - 1);
        
        controller.finishTurn();
        
        verify(gameStateModel, never()).nextPlayer();
        verify(recipeModel, never()).loadAvailableRecipes(anyList());
    }
    
    @Test
    void testScanProductWithBioLocalProduct() {
        String barcode = "123456";
        Product mockProduct = mock(Product.class);
        when(productRepository.getProductByBarcode(barcode)).thenReturn(mockProduct);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        when(fridgeStockModel.getProducts()).thenReturn(new ArrayList<>());
        when(fridgeStockModel.addProduct(mockProduct)).thenReturn(true);
        
        // Specific attributes that affect score and HP
        when(mockProduct.isBio()).thenReturn(true);
        when(mockProduct.isLocal()).thenReturn(true);
        
        controller.scanProduct(barcode);
        
        // Verify correct score was added (Bio + Local)
        verify(gameStateModel).addPlayer1Score(GameConfig.SCORE_BIO + GameConfig.SCORE_LOCAL);
        verify(penguinModel).modifyHP(GameConfig.HP_INCREASE);
    }
    
    @Test
    void testScanProductWithImportedNonBioProduct() {
        String barcode = "123456";
        Product mockProduct = mock(Product.class);
        when(productRepository.getProductByBarcode(barcode)).thenReturn(mockProduct);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        when(fridgeStockModel.getProducts()).thenReturn(new ArrayList<>());
        when(fridgeStockModel.addProduct(mockProduct)).thenReturn(true);
        
        // Non-bio, non-local product
        when(mockProduct.isBio()).thenReturn(false);
        when(mockProduct.isLocal()).thenReturn(false);
        
        controller.scanProduct(barcode);
        
        // Verify correct score was added (Imported penalty)
        verify(gameStateModel).addPlayer1Score(GameConfig.SCORE_IMPORTED);
        verify(penguinModel).modifyHP(GameConfig.HP_DECREASE);
    }
    
    @Test
    void testRemoveProductNullProduct() {
        // Test with null product
        controller.removeProduct(null);
        
        verify(fridgeStockModel, never()).removeProduct(any());
        verify(gameStateModel, never()).addPlayer1Score(anyInt());
        verify(penguinModel, never()).modifyHP(anyInt());
    }
    
    @Test
    void testRemoveProductUpdatePenguinHPForBioProduct() {
        Product mockProduct = mock(Product.class);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        when(fridgeStockModel.removeProduct(mockProduct)).thenReturn(true);
        
        // Test with Bio product
        when(mockProduct.isBio()).thenReturn(true);
        when(mockProduct.isLocal()).thenReturn(false);
        
        controller.removeProduct(mockProduct);
        
        // Verify HP is decreased (reverse of the increase)
        verify(penguinModel).modifyHP(-GameConfig.HP_INCREASE);
    }
    
    @Test
    void testRemoveProductUpdatePenguinHPForNonBioLocalProduct() {
        Product mockProduct = mock(Product.class);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        when(fridgeStockModel.removeProduct(mockProduct)).thenReturn(true);
        
        // Test with Local (but not Bio) product
        when(mockProduct.isBio()).thenReturn(false);
        when(mockProduct.isLocal()).thenReturn(true);
        
        controller.removeProduct(mockProduct);
        
        // Verify HP is decreased (reverse of the increase)
        verify(penguinModel).modifyHP(-GameConfig.HP_INCREASE);
    }
    
    @Test
    void testRemoveProductUpdatePenguinHPForNonBioNonLocalProduct() {
        Product mockProduct = mock(Product.class);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        when(fridgeStockModel.removeProduct(mockProduct)).thenReturn(true);
        
        // Test with non-Bio, non-Local product
        when(mockProduct.isBio()).thenReturn(false);
        when(mockProduct.isLocal()).thenReturn(false);
        
        controller.removeProduct(mockProduct);
        
        // Verify HP is increased (reverse of the decrease)
        verify(penguinModel).modifyHP(-GameConfig.HP_DECREASE);
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
        verify(gameStateModel, never()).addPlayer1Score(anyInt());
        verify(penguinModel, never()).modifyHP(anyInt());
    }
    
    @Test
    void testScanProductWithBioOnlyProduct() {
        String barcode = "123456";
        Product mockProduct = mock(Product.class);
        when(productRepository.getProductByBarcode(barcode)).thenReturn(mockProduct);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        when(fridgeStockModel.getProducts()).thenReturn(new ArrayList<>());
        when(fridgeStockModel.addProduct(mockProduct)).thenReturn(true);
        
        // Bio but not local
        when(mockProduct.isBio()).thenReturn(true);
        when(mockProduct.isLocal()).thenReturn(false);
        
        controller.scanProduct(barcode);
        
        // Verify correct score and HP changes
        verify(gameStateModel).addPlayer1Score(GameConfig.SCORE_BIO + GameConfig.SCORE_IMPORTED);
        verify(penguinModel).modifyHP(GameConfig.HP_INCREASE);
    }
    
    @Test
    void testScanProductWithLocalOnlyProduct() {
        String barcode = "123456";
        Product mockProduct = mock(Product.class);
        when(productRepository.getProductByBarcode(barcode)).thenReturn(mockProduct);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        when(fridgeStockModel.getProducts()).thenReturn(new ArrayList<>());
        when(fridgeStockModel.addProduct(mockProduct)).thenReturn(true);
        
        // Local but not bio
        when(mockProduct.isBio()).thenReturn(false);
        when(mockProduct.isLocal()).thenReturn(true);
        
        controller.scanProduct(barcode);
        
        // Verify correct score was added (Local only)
        verify(gameStateModel).addPlayer1Score(GameConfig.SCORE_LOCAL);
        verify(penguinModel).modifyHP(GameConfig.HP_INCREASE);
    }
}
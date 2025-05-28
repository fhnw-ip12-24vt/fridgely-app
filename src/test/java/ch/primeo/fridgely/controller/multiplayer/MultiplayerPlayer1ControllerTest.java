package ch.primeo.fridgely.controller.multiplayer;

import ch.primeo.fridgely.config.GameConfig;
import ch.primeo.fridgely.model.*;
import ch.primeo.fridgely.model.multiplayer.MultiplayerGameStateModel;
import ch.primeo.fridgely.service.ProductRepository;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

        controller = new MultiplayerPlayer1Controller(fridgeStockModel, gameStateModel, penguinModel, productRepository, recipeModel);
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
        when(fridgeStockModel.getFridgeProducts()).thenReturn(List.of(mock(Product.class), mock(Product.class)));

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

    @Test
    void testScanProductWithBioProduct() {
        // Arrange
        String barcode = "123456";
        Product mockProduct = mock(Product.class);
        when(mockProduct.isBio()).thenReturn(true);
        when(mockProduct.isLocal()).thenReturn(false);
        when(mockProduct.isLowCo2()).thenReturn(false);

        when(productRepository.getProductByBarcode(barcode)).thenReturn(mockProduct);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        when(fridgeStockModel.getProducts()).thenReturn(new ArrayList<>());
        when(fridgeStockModel.addProduct(mockProduct)).thenReturn(true);

        // Act
        Product result = controller.scanProduct(barcode);

        // Assert
        assertSame(mockProduct, result);
        verify(penguinModel).modifyHP(GameConfig.HP_INCREASE); // Bio products increase HP
    }

    @Test
    void testScanProductWithNonBioProduct() {
        // Arrange
        String barcode = "123456";
        Product mockProduct = mock(Product.class);
        when(mockProduct.isBio()).thenReturn(false);
        when(mockProduct.isLocal()).thenReturn(false);
        when(mockProduct.isLowCo2()).thenReturn(false);

        when(productRepository.getProductByBarcode(barcode)).thenReturn(mockProduct);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        when(fridgeStockModel.getProducts()).thenReturn(new ArrayList<>());
        when(fridgeStockModel.addProduct(mockProduct)).thenReturn(true);

        // Act
        Product result = controller.scanProduct(barcode);

        // Assert
        assertSame(mockProduct, result);
        verify(penguinModel).modifyHP(GameConfig.HP_DECREASE); // Non-bio and non-local products decrease HP
    }

    @Test
    void testFinishTurnWithEnoughProducts() {
        // Arrange
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < GameConfig.MIN_PRODUCTS_PER_ROUND; i++) {
            products.add(mock(Product.class));
        }
        when(fridgeStockModel.getFridgeProducts()).thenReturn(products);
        when(fridgeStockModel.getProducts()).thenReturn(products);
        when(recipeModel.getPossibleRecipes(products)).thenReturn(List.of(mock(Recipe.class)));

        // Act
        boolean result = controller.finishTurn();

        // Assert
        assertTrue(result);
        verify(gameStateModel).addScore(anyInt());
        verify(gameStateModel).nextPlayer();
    }

    @Test
    void testScanProductWithLocalProduct() {
        // Arrange
        String barcode = "123456";
        Product mockProduct = mock(Product.class);
        when(mockProduct.isBio()).thenReturn(false);
        when(mockProduct.isLocal()).thenReturn(true);
        when(mockProduct.isLowCo2()).thenReturn(false);

        when(productRepository.getProductByBarcode(barcode)).thenReturn(mockProduct);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        when(fridgeStockModel.getProducts()).thenReturn(new ArrayList<>());
        when(fridgeStockModel.addProduct(mockProduct)).thenReturn(true);

        // Act
        Product result = controller.scanProduct(barcode);

        // Assert
        assertSame(mockProduct, result);
        verify(penguinModel).modifyHP(GameConfig.HP_INCREASE); // Local products increase HP
    }

    @Test
    void testScanProductWithLowCO2Product() {
        // Arrange
        String barcode = "123456";
        Product mockProduct = mock(Product.class);
        when(mockProduct.isBio()).thenReturn(false);
        when(mockProduct.isLocal()).thenReturn(false);
        when(mockProduct.isLowCo2()).thenReturn(true);

        when(productRepository.getProductByBarcode(barcode)).thenReturn(mockProduct);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        when(fridgeStockModel.getProducts()).thenReturn(new ArrayList<>());
        when(fridgeStockModel.addProduct(mockProduct)).thenReturn(true);

        // Act
        Product result = controller.scanProduct(barcode);

        // Assert
        assertSame(mockProduct, result);
        // Low CO2 products don't affect HP directly, but they do affect score
        verify(penguinModel).modifyHP(GameConfig.HP_DECREASE); // Still decreases HP because it's not bio or local
    }

    @Test
    void testScanProductAlreadyInFridge() {
        // Arrange
        String barcode = "123456";
        Product mockProduct = mock(Product.class);
        List<Product> existingProducts = new ArrayList<>();
        existingProducts.add(mockProduct);

        when(productRepository.getProductByBarcode(barcode)).thenReturn(mockProduct);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        when(fridgeStockModel.getProducts()).thenReturn(existingProducts);

        // Act
        Product result = controller.scanProduct(barcode);

        // Assert
        assertNull(result);
        verify(fridgeStockModel, never()).addProduct(any());
        verify(penguinModel, never()).modifyHP(anyInt());
    }

    @Test
    void testCalculateRoundScoreWithZeroItems() {
        // This test requires reflection to access the private method
        // We'll test it indirectly through finishTurn

        // Arrange
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < GameConfig.MIN_PRODUCTS_PER_ROUND; i++) {
            products.add(mock(Product.class));
        }
        when(fridgeStockModel.getFridgeProducts()).thenReturn(products);
        when(fridgeStockModel.getProducts()).thenReturn(products);
        when(recipeModel.getPossibleRecipes(products)).thenReturn(List.of(mock(Recipe.class)));

        // We need to ensure roundScannedItems is 0
        // This is a bit tricky since it's a private field
        // We'll use a fresh controller instance
        MultiplayerPlayer1Controller freshController =
            new MultiplayerPlayer1Controller(fridgeStockModel, gameStateModel, penguinModel, productRepository, recipeModel);

        // Act
        boolean result = freshController.finishTurn();

        // Assert
        assertTrue(result);
        // With 0 scanned items, the score should be 0
        verify(gameStateModel).addScore(0);
        verify(gameStateModel).nextPlayer();
    }

    @Test
    void testCalculateRoundScoreFullCoverage() {
        // Arrange
        // Common setup for all sub-cases
        List<Product> minProductsForTurn = new ArrayList<>();
        for (int i = 0; i < GameConfig.MIN_PRODUCTS_PER_ROUND; i++) {
            minProductsForTurn.add(mock(Product.class));
        }
        // Ensure finishTurn can proceed by having enough products in the fridge mock
        // This mock will be used by all controller instances via fridgeStockModel
        when(fridgeStockModel.getFridgeProducts()).thenReturn(minProductsForTurn);
        when(fridgeStockModel.getProducts()).thenReturn(minProductsForTurn);
        when(recipeModel.getPossibleRecipes(minProductsForTurn)).thenReturn(List.of(mock(Recipe.class)));

        int maxPossibleProductScore = GameConfig.SCORE_BIO + GameConfig.SCORE_LOCAL + GameConfig.SCORE_LOW_CO2;
        int minPossibleProductScore = GameConfig.SCORE_NON_BIO + GameConfig.SCORE_NON_LOCAL + GameConfig.SCORE_HIGH_CO2;

        // --- Test case 1: Positive score path ---
        clearInvocations(gameStateModel, penguinModel, fridgeStockModel, productRepository); // Clear for this sub-case
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);

        MultiplayerPlayer1Controller positiveScoreController =
            new MultiplayerPlayer1Controller(fridgeStockModel, gameStateModel, penguinModel, productRepository, recipeModel);

        String barcode1 = "good123";
        Product goodProduct = mock(Product.class);
        when(goodProduct.isBio()).thenReturn(true);
        when(goodProduct.isLocal()).thenReturn(true);
        when(goodProduct.isLowCo2()).thenReturn(true);
        when(productRepository.getProductByBarcode(barcode1)).thenReturn(goodProduct);
        when(fridgeStockModel.getProducts()).thenReturn(new ArrayList<>()); // Product not in fridge yet
        when(fridgeStockModel.addProduct(goodProduct)).thenReturn(true);

        positiveScoreController.scanProduct(barcode1); // This sets internal roundScore and roundScannedItems

        // Mock recipe availability for finishTurn
        List<Product> productsAfterScan = List.of(goodProduct);
        when(fridgeStockModel.getProducts()).thenReturn(productsAfterScan);
        when(recipeModel.getPossibleRecipes(productsAfterScan)).thenReturn(List.of(mock(Recipe.class)));

        positiveScoreController.finishTurn();

        // Expected score calculation for positive case (1 item)
        int goodProductRawScore = GameConfig.SCORE_BIO + GameConfig.SCORE_LOCAL + GameConfig.SCORE_LOW_CO2;
        double rhPositive = 0.5;
        int avgGoodProductScoreRounded = (int) (((double) goodProductRawScore) + rhPositive);

        int expectedPositiveScore;
        // Note: Controller's maxScore/minScore are denominators for scaling the avgProductScoreRounded.
        // These are max/min possible *average* product scores, which for 1 item is the product score itself.
        // However, the controller uses the sum of GameConfig constants as denominators.
        double scaledScore = (double) avgGoodProductScoreRounded / maxPossibleProductScore * GameConfig.SCORE_PLAYER1_INCREASE;
        expectedPositiveScore = (int) (scaledScore + rhPositive);
        verify(gameStateModel).addScore(expectedPositiveScore);


        // --- Test case 2: Negative score path ---
        clearInvocations(gameStateModel, penguinModel, fridgeStockModel, productRepository);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);

        MultiplayerPlayer1Controller negativeScoreController =
            new MultiplayerPlayer1Controller(fridgeStockModel, gameStateModel, penguinModel, productRepository, recipeModel);

        String barcode2 = "bad123";
        Product badProduct = mock(Product.class);
        when(badProduct.isBio()).thenReturn(false);
        when(badProduct.isLocal()).thenReturn(false);
        when(badProduct.isLowCo2()).thenReturn(false);
        when(productRepository.getProductByBarcode(barcode2)).thenReturn(badProduct);
        when(fridgeStockModel.getProducts()).thenReturn(new ArrayList<>());
        when(fridgeStockModel.addProduct(badProduct)).thenReturn(true);

        negativeScoreController.scanProduct(barcode2);

        // Mock recipe availability for finishTurn
        List<Product> productsAfterScan2 = List.of(badProduct);
        when(fridgeStockModel.getProducts()).thenReturn(productsAfterScan2);
        when(recipeModel.getPossibleRecipes(productsAfterScan2)).thenReturn(List.of(mock(Recipe.class)));

        negativeScoreController.finishTurn();

        int badProductRawScore = GameConfig.SCORE_NON_BIO + GameConfig.SCORE_NON_LOCAL + GameConfig.SCORE_HIGH_CO2;
        double rhNegative = -0.5;
        int avgBadProductScoreRounded = (int) (((double) badProductRawScore) + rhNegative);

        int expectedNegativeScore;
        scaledScore = (double) avgBadProductScoreRounded / minPossibleProductScore * GameConfig.SCORE_PLAYER1_DECREASE;
        expectedNegativeScore = (int) (scaledScore + rhNegative);
        verify(gameStateModel).addScore(expectedNegativeScore);


        // --- Test case 3: Mixed score path ---
        clearInvocations(gameStateModel, penguinModel, fridgeStockModel, productRepository);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);

        MultiplayerPlayer1Controller mixedScoreController =
            new MultiplayerPlayer1Controller(fridgeStockModel, gameStateModel, penguinModel, productRepository, recipeModel);

        String barcode3 = "mixed123";
        Product mixedProduct = mock(Product.class);
        when(mixedProduct.isBio()).thenReturn(true);    // Positive
        when(mixedProduct.isLocal()).thenReturn(false); // Negative
        when(mixedProduct.isLowCo2()).thenReturn(false); // Negative
        when(productRepository.getProductByBarcode(barcode3)).thenReturn(mixedProduct);
        when(fridgeStockModel.getProducts()).thenReturn(new ArrayList<>());
        when(fridgeStockModel.addProduct(mixedProduct)).thenReturn(true);

        mixedScoreController.scanProduct(barcode3);

        // Mock recipe availability for finishTurn
        List<Product> productsAfterScan3 = List.of(mixedProduct);
        when(fridgeStockModel.getProducts()).thenReturn(productsAfterScan3);
        when(recipeModel.getPossibleRecipes(productsAfterScan3)).thenReturn(List.of(mock(Recipe.class)));

        mixedScoreController.finishTurn();

        int mixedProductRawScore = GameConfig.SCORE_BIO + GameConfig.SCORE_NON_LOCAL + GameConfig.SCORE_HIGH_CO2;
        double rhMixed = -0.5;
        int avgMixedProductScoreRounded = (int) (((double) mixedProductRawScore) + rhMixed);

        int expectedMixedScore;
        // Handle potential division by zero if minPossibleProductScore could be 0
        // Avoid div by zero, maintain sign if applicable
        scaledScore = (double) avgMixedProductScoreRounded / (double) minPossibleProductScore * GameConfig.SCORE_PLAYER1_DECREASE;
        expectedMixedScore = (int) (scaledScore + rhMixed);
        verify(gameStateModel).addScore(expectedMixedScore);
    }

    @Test
    void scanProduct_autoFinishesTurn_whenMaxProductsReached() {
        // Arrange
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        Product mockProduct = mock(Product.class);
        when(productRepository.getProductByBarcode("barcode_max_trigger")).thenReturn(mockProduct);
        when(fridgeStockModel.getProducts()).thenReturn(new ArrayList<>()); // Product not in fridge initially
        when(fridgeStockModel.addProduct(mockProduct)).thenReturn(true); // Product added successfully

        List<Product> fridgeAtMaxCapacity = new ArrayList<>();
        for (int i = 0; i < GameConfig.MAX_PRODUCTS; i++) {
            fridgeAtMaxCapacity.add(mock(Product.class));
        }
        // This mock simulates the state of the fridge *after* the product is added and its size is checked.
        when(fridgeStockModel.getFridgeProducts()).thenReturn(fridgeAtMaxCapacity);
        // Also mock getProducts() for recipe checking
        when(fridgeStockModel.getProducts()).thenReturn(fridgeAtMaxCapacity);
        when(recipeModel.getPossibleRecipes(fridgeAtMaxCapacity)).thenReturn(List.of(mock(Recipe.class)));

        // Act
        controller.scanProduct("barcode_max_trigger");

        // Assert
        // Verify that finishTurn() was called and executed its main logic.
        // This assumes GameConfig.MAX_PRODUCTS >= GameConfig.MIN_PRODUCTS_PER_ROUND for finishTurn to proceed.
        verify(gameStateModel).addScore(anyInt());
        verify(gameStateModel).nextPlayer();
    }

    @Test
    void scanProduct_doesNotAutoFinishTurn_whenBelowMaxProducts() {
        // This test is meaningful if MAX_PRODUCTS > 0.
        // If MAX_PRODUCTS is 0, the condition size >= MAX_PRODUCTS is always true for non-negative sizes.
        Assumptions.assumeTrue(GameConfig.MAX_PRODUCTS > 0, "Test skipped if MAX_PRODUCTS is 0, as auto-finish would always trigger.");

        // Arrange
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        Product mockProduct = mock(Product.class);
        when(productRepository.getProductByBarcode("barcode_below_max")).thenReturn(mockProduct);
        when(fridgeStockModel.getProducts()).thenReturn(new ArrayList<>()); // Product not in fridge initially
        when(fridgeStockModel.addProduct(mockProduct)).thenReturn(true); // Product added successfully

        List<Product> fridgeBelowMaxCapacity = new ArrayList<>();
        // Simulate fridge having one less than MAX_PRODUCTS *after* the current product is notionally added.
        for (int i = 0; i < GameConfig.MAX_PRODUCTS - 1; i++) {
            fridgeBelowMaxCapacity.add(mock(Product.class));
        }
        // This mock simulates the state of the fridge *after* the product is added and its size is checked.
        when(fridgeStockModel.getFridgeProducts()).thenReturn(fridgeBelowMaxCapacity);

        // Act
        controller.scanProduct("barcode_below_max");

        // Assert
        // Verify that finishTurn() was not called, so no change in player or score addition from it.
        verify(gameStateModel, never()).addScore(anyInt()); // Check specifically score by finishTurn
        verify(gameStateModel, never()).nextPlayer();
    }

    @Test
    void testHasAvailableRecipes_WithRecipes() {
        // Arrange
        List<Product> products = List.of(mock(Product.class));
        when(fridgeStockModel.getProducts()).thenReturn(products);
        when(recipeModel.getPossibleRecipes(products)).thenReturn(List.of(mock(Recipe.class)));

        // Act
        boolean result = controller.hasAvailableRecipes();

        // Assert
        assertTrue(result);
    }

    @Test
    void testHasAvailableRecipes_WithoutRecipes() {
        // Arrange
        List<Product> products = List.of(mock(Product.class));
        when(fridgeStockModel.getProducts()).thenReturn(products);
        when(recipeModel.getPossibleRecipes(products)).thenReturn(List.of());

        // Act
        boolean result = controller.hasAvailableRecipes();

        // Assert
        assertFalse(result);
    }

    @Test
    void testFinishTurnWithNoAvailableRecipes() {
        // Arrange
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < GameConfig.MIN_PRODUCTS_PER_ROUND; i++) {
            products.add(mock(Product.class));
        }
        when(fridgeStockModel.getFridgeProducts()).thenReturn(products);
        when(fridgeStockModel.getProducts()).thenReturn(products);
        when(recipeModel.getPossibleRecipes(products)).thenReturn(List.of()); // No recipes available

        // Act
        boolean result = controller.finishTurn();

        // Assert
        assertFalse(result);
        verify(gameStateModel, never()).addScore(anyInt());
        verify(gameStateModel, never()).nextPlayer();
    }
}

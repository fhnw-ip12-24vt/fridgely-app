package ch.primeo.fridgely.controller.multiplayer;

import ch.primeo.fridgely.config.GameConfig;
import ch.primeo.fridgely.model.FridgeStockModel;
import ch.primeo.fridgely.model.PenguinModel;
import ch.primeo.fridgely.model.Product;
import ch.primeo.fridgely.model.RecipeModel;
import ch.primeo.fridgely.model.multiplayer.MultiplayerGameStateModel;
import ch.primeo.fridgely.service.ProductRepository;

/**
 * Controller for Player 1 (Scanner) in the multiplayer game mode. Handles scanning products and updating the fridge
 * stock.
 */
public class MultiplayerPlayer1Controller {

    private final FridgeStockModel fridgeStockModel;
    private final MultiplayerGameStateModel gameStateModel;
    private final ProductRepository productRepository;
    private final PenguinModel penguinModel;
    private final RecipeModel recipeModel;

    /**
     * Constructs a new Player 1 controller.
     *
     * @param stockModel  the model for the fridge stock
     * @param stateModel  the model for the game state
     * @param penguModel  the model for the penguin HP
     * @param productRepo the repository for accessing products
     * @param recipeModel the model for recipes
     */
    public MultiplayerPlayer1Controller(FridgeStockModel stockModel, MultiplayerGameStateModel stateModel,
            PenguinModel penguModel, ProductRepository productRepo, RecipeModel recipeModel) {
        this.fridgeStockModel = stockModel;
        this.gameStateModel = stateModel;
        this.penguinModel = penguModel;
        this.productRepository = productRepo;
        this.recipeModel = recipeModel;
    }

    /**
     * Scans a product barcode and toggles its presence in the fridge stock. If the product is already in the stock, it
     * will be removed. If the product is not in the stock, it will be added. Updates the score and penguin HP based on
     * product attributes.
     *
     * @param barcode the barcode of the product to scan
     * @return the product that was scanned or null if not found/invalid
     */
    public Product scanProduct(String barcode) {
        // Check if it's player 1's turn
        if (gameStateModel.getCurrentPlayer() != MultiplayerGameStateModel.Player.PLAYER1) {
            return null;
        }

        // Look up the product
        Product product = productRepository.getProductByBarcode(barcode);
        if (product == null) {
            return null;
        }

        // Toggle the product: if it's in the fridge stock, remove it; otherwise, add it
        if (fridgeStockModel.getProducts().contains(product)) {
            // Product is in the stock, so remove it
            removeProduct(product);
            return product;
        } else {
            // Product is not in the stock, so add it
            boolean added = fridgeStockModel.addProduct(product);
            if (!added) {
                return null;
            }

            // Calculate and add score
            int score = calculateProductScore(product);
            gameStateModel.addPlayer1Score(score);

            // Update penguin HP
            updatePenguinHPForProduct(product);

            return product;
        }
    }

    /**
     * Removes a product from the fridge stock.
     *
     * @param product the product to remove
     */
    public void removeProduct(Product product) {
        // Check if it's player 1's turn
        if (gameStateModel.getCurrentPlayer() != MultiplayerGameStateModel.Player.PLAYER1) {
            return;
        }

        // Remove the product
        boolean removed = fridgeStockModel.removeProduct(product);
        if (!removed) {
            return;
        }

        // Subtract the score
        int score = -calculateProductScore(product);
        gameStateModel.addPlayer1Score(score);

        // Reverse the penguin HP change
        if (product.isBio() || product.isLocal()) {
            penguinModel.modifyHP(-GameConfig.HP_INCREASE);
        } else {
            penguinModel.modifyHP(-GameConfig.HP_DECREASE); // Double negative becomes positive
        }

    }

    /**
     * Calculates the score for a product based on its attributes.
     *
     * @param product the product to calculate the score for
     * @return the score value for the product
     */
    private int calculateProductScore(Product product) {
        int score = 0;

        if (product.isBio()) {
            score += GameConfig.SCORE_BIO;
        }

        if (product.isLocal()) {
            score += GameConfig.SCORE_LOCAL;
        }

        // Check if imported (for now, we consider non-local products as imported)
        if (!product.isLocal()) {
            score += GameConfig.SCORE_IMPORTED; // This is negative in the config
        }

        return score;
    }

    /**
     * Updates the penguin HP based on product attributes.
     *
     * @param product the product to evaluate
     */
    private void updatePenguinHPForProduct(Product product) {
        // Bio and local products are good for the environment
        if (product.isBio() || product.isLocal()) {
            penguinModel.modifyHP(GameConfig.HP_INCREASE);
        } else {
            penguinModel.modifyHP(GameConfig.HP_DECREASE);
        }
    }

    /**
     * Finishes Player 1's turn if enough products have been scanned.
     */
    public void finishTurn() {
        // Check if it's player 1's turn
        if (gameStateModel.getCurrentPlayer() != MultiplayerGameStateModel.Player.PLAYER1) {
            return;
        }

        // Ensure minimum number of products are in the fridge
        if (fridgeStockModel.getProductCount() < GameConfig.MIN_PRODUCTS_PER_ROUND) {
            return;
        }

        this.recipeModel.loadAvailableRecipes(fridgeStockModel.getProducts());

        // Switch to player 2's turn
        gameStateModel.nextPlayer();
    }

    /**
     * Resets Player 1's state for a new round.
     */
    public void resetForNewRound() {
        fridgeStockModel.clear();
    }
}

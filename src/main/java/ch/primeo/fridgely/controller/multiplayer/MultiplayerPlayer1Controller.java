package ch.primeo.fridgely.controller.multiplayer;


import ch.primeo.fridgely.config.GameConfig;
import ch.primeo.fridgely.model.*;
import ch.primeo.fridgely.model.multiplayer.MultiplayerGameStateModel;
import ch.primeo.fridgely.service.ProductRepository;

import java.util.List;

/**
 * Controller for Player 1 (Scanner) in the multiplayer game mode. Handles scanning products and updating the fridge
 * stock.
 */
public class MultiplayerPlayer1Controller {

    private final FridgeStockModel fridgeStockModel;
    private final MultiplayerGameStateModel gameStateModel;
    private final PenguinModel penguinModel;
    private final ProductRepository productRepository;
    private final RecipeModel recipeModel;

    private int roundScannedItems;
    private int roundScore;

    /**
     * Constructs a new Player 1 controller.
     *
     * @param stockModel   the model for the fridge stock
     * @param stateModel   the model for the game state
     * @param penguinModel the model for the penguin HP
     * @param productRepo  the repository for accessing products
     * @param recipeModel  the model for recipes
     */
    public MultiplayerPlayer1Controller(FridgeStockModel stockModel, MultiplayerGameStateModel stateModel,
                                        PenguinModel penguinModel, ProductRepository productRepo, RecipeModel recipeModel) {
        this.fridgeStockModel = stockModel;
        this.gameStateModel = stateModel;
        this.penguinModel = penguinModel;
        this.productRepository = productRepo;
        this.recipeModel = recipeModel;
        roundScannedItems = 0;
        roundScore = 0;
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

        // If product not in the fridge stock, add it; otherwise ignore
        if (product != null && !fridgeStockModel.getProducts().contains(product)) {
            // Product is not in the stock, so add it
            boolean added = fridgeStockModel.addProduct(product);
            if (!added) {
                return null;
            }

            roundScore += calculateProductScore(product);
            roundScannedItems++;

            // Update penguin HP
            updatePenguinHPForProduct(product);

            if (fridgeStockModel.getFridgeProducts().size() >= GameConfig.MAX_PRODUCTS) {
                finishTurn();
            }

            return product;
        }
        return null;
    }

    /**
     * Calculates the score for a product based on its attributes.
     *
     * @param product the product to calculate the score for
     * @return the score value for the product
     */
    private int calculateProductScore(Product product) {
        int score = 0;
        //Check if product is bio or not and add score
        score += product.isBio() ? GameConfig.SCORE_BIO : GameConfig.SCORE_NON_BIO;
        // Check if imported (for now, we consider non-local products as imported)
        score += product.isLocal() ? GameConfig.SCORE_LOCAL : GameConfig.SCORE_NON_LOCAL;
        // Check if product has a low or high co2 footprint and add score
        score += product.isLowCo2() ? GameConfig.SCORE_LOW_CO2 : GameConfig.SCORE_HIGH_CO2;
        return score;
    }

    private int calculateRoundScore() {
        // Prevent division by zero
        if (roundScannedItems == 0) {
            return 0;
        }

        double roundHelper = roundScore < 0 ? -0.5 : 0.5;
        int sum = (int) ((double) (roundScore / roundScannedItems) + roundHelper);

        int maxScore = GameConfig.SCORE_BIO + GameConfig.SCORE_LOCAL + GameConfig.SCORE_LOW_CO2;
        int minScore = GameConfig.SCORE_NON_BIO + GameConfig.SCORE_NON_LOCAL + GameConfig.SCORE_HIGH_CO2;

        if (sum < 0) {
            sum = (int) ((double) sum / minScore * GameConfig.SCORE_PLAYER1_DECREASE + roundHelper);
        } else {
            sum = (int) ((double) sum / maxScore * GameConfig.SCORE_PLAYER1_INCREASE + roundHelper);
        }

        return sum;
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
     * Checks if any recipes can be made with the current products in the fridge.
     *
     * @return true if at least one recipe can be made, false otherwise
     */
    public boolean hasAvailableRecipes() {
        List<Product> productsInStorage = fridgeStockModel.getProducts();
        List<Recipe> possibleRecipes = recipeModel.getPossibleRecipes(productsInStorage);
        return !possibleRecipes.isEmpty();
    }

    /**
     * Finishes Player 1's turn if enough products have been scanned and recipes are available.
     * If no recipes are available, the turn cannot be finished to prevent soft lock.
     *
     * @return true if the turn was successfully finished, false if it cannot be finished
     */
    public boolean finishTurn() {
        // Check if it's player 1's turn
        if (gameStateModel.getCurrentPlayer() != MultiplayerGameStateModel.Player.PLAYER1) {
            return false;
        }

        // Ensure minimum number of products are in the fridge
        if (fridgeStockModel.getFridgeProducts().size() < GameConfig.MIN_PRODUCTS_PER_ROUND) {
            return false;
        }

        // Check if any recipes can be made with current products
        if (!hasAvailableRecipes()) {
            return false; // Cannot finish turn if no recipes are available
        }

        // Switch to player 2's turn and add the score of player 1 round
        gameStateModel.addScore(calculateRoundScore());
        gameStateModel.nextPlayer();

        //reset for next round
        roundScore = 0;
        roundScannedItems = 0;
        return true;
    }
}

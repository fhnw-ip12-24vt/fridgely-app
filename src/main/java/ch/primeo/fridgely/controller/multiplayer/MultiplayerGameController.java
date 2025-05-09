package ch.primeo.fridgely.controller.multiplayer;

import ch.primeo.fridgely.model.FridgeStockModel;
import ch.primeo.fridgely.model.PenguinModel;
import ch.primeo.fridgely.model.RecipeModel;
import ch.primeo.fridgely.model.multiplayer.MultiplayerGameStateModel;
import ch.primeo.fridgely.service.ProductRepository;
import ch.primeo.fridgely.service.RecipeRepository;
import lombok.Getter;

/**
 * Main controller for the multiplayer game mode.
 * Coordinates between the player controllers and manages the game flow.
 */
@Getter
public class MultiplayerGameController {

    private final MultiplayerGameStateModel gameStateModel;
    private final PenguinModel penguinModel;
    private final FridgeStockModel fridgeStockModel;
    private final RecipeModel recipeModel;
    private final MultiplayerPlayer1Controller player1Controller;
    private final MultiplayerPlayer2Controller player2Controller;
    private final ProductRepository productRepository;

    /**
     * Constructs a new game controller.
     * 
     * @param productRepo the repository for accessing products
     * @param recipeRepository the repository for accessing recipes
     */
    public MultiplayerGameController(ProductRepository productRepo, RecipeRepository recipeRepository) {
        this.productRepository = productRepo;
        // Initialize models
        this.gameStateModel = new MultiplayerGameStateModel();
        this.penguinModel = new PenguinModel();
        this.fridgeStockModel = new FridgeStockModel();
        this.recipeModel = new RecipeModel(recipeRepository, fridgeStockModel.getProducts());

        // Initialize controllers
        this.player1Controller = new MultiplayerPlayer1Controller(
                fridgeStockModel, gameStateModel, penguinModel, productRepository, this.recipeModel);
        this.player2Controller = new MultiplayerPlayer2Controller(
                fridgeStockModel, gameStateModel, penguinModel, recipeModel);
    }

    /**
     * Starts a new game.
     */
    public void startNewGame() {
        gameStateModel.resetGame();
        penguinModel.resetHP();
        fridgeStockModel.clear();
        recipeModel.selectRecipe(null);
    }
}

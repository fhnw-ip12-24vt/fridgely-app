package ch.primeo.fridgely.controller.multiplayer;

import ch.primeo.fridgely.controller.GameController;
import ch.primeo.fridgely.model.multiplayer.MultiplayerGameStateModel;
import ch.primeo.fridgely.service.ProductRepository;
import ch.primeo.fridgely.service.RecipeRepository;

/**
 * Main controller for the multiplayer game mode.
 * Coordinates between the player controllers and manages the game flow.
 */
public class MultiplayerGameController extends GameController {

    private final MultiplayerGameStateModel gameStateModel;
    private final MultiplayerPlayer1Controller player1Controller;
    private final MultiplayerPlayer2Controller player2Controller;

    /**
     * Constructs a new game controller.
     * 
     * @param productRepo      the repository for accessing products
     * @param recipeRepository the repository for accessing recipes
     */
    public MultiplayerGameController(ProductRepository productRepo, RecipeRepository recipeRepository) {
        super(productRepo, recipeRepository);
        this.gameStateModel = new MultiplayerGameStateModel();
        this.player1Controller = new MultiplayerPlayer1Controller(
                fridgeStockModel, gameStateModel, penguinModel, productRepository);
        this.player2Controller = new MultiplayerPlayer2Controller(
                fridgeStockModel, gameStateModel, penguinModel, recipeModel);
    }

    /**
     * Gets the game state model.
     * 
     * @return the game state model
     */
    public MultiplayerGameStateModel getGameStateModel() {
        return gameStateModel;
    }

    /**
     * Gets the Player 1 controller.
     * 
     * @return the Player 1 controller
     */
    public MultiplayerPlayer1Controller getPlayer1Controller() {
        return player1Controller;
    }

    /**
     * Gets the Player 2 controller.
     * 
     * @return the Player 2 controller
     */
    public MultiplayerPlayer2Controller getPlayer2Controller() {
        return player2Controller;
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

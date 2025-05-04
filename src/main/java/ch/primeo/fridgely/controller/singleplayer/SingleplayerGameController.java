package ch.primeo.fridgely.controller.singleplayer;

import ch.primeo.fridgely.controller.GameController;
import ch.primeo.fridgely.model.singleplayer.SingleplayerGameStateModel;
import ch.primeo.fridgely.service.ProductRepository;
import ch.primeo.fridgely.service.RecipeRepository;

/**
 * Main controller for the singleplayer game mode.
 * Coordinates between the player controllers and manages the game flow.
 */
public class SingleplayerGameController extends GameController {

    private final SingleplayerGameStateModel gameStateModel;
    private final SingleplayerPlayerController playerController;

    /**
     * Constructs a new game controller.
     * 
     * @param productRepo      the repository for accessing products
     * @param recipeRepository the repository for accessing recipes
     */
    public SingleplayerGameController(ProductRepository productRepo, RecipeRepository recipeRepository) {
        super(productRepo, recipeRepository);
        this.gameStateModel = new SingleplayerGameStateModel();
        this.playerController = new SingleplayerPlayerController(
                fridgeStockModel, gameStateModel, penguinModel, recipeModel);
    }

    /**
     * Gets the game state model.
     * 
     * @return the game state model
     */
    public SingleplayerGameStateModel getGameStateModel() {
        return gameStateModel;
    }

    /**
     * Gets the Player controller.
     * 
     * @return the Player controller
     */
    public SingleplayerPlayerController getPlayerController() {
        return playerController;
    }

    /**
     * Starts a new round by selecting random products from the ProductRepository
     * and adding them to the FridgeStockModel.
     */
    public void startNewRound() {
        fridgeStockModel.clear();

        // TODO: Fill fridge with random products or predefined product sets to ensure a
        // valid recipe generation possability
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

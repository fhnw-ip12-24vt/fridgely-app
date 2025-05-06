package ch.primeo.fridgely.controller.singleplayer;

import ch.primeo.fridgely.controller.GameController;
import ch.primeo.fridgely.model.Product;
import ch.primeo.fridgely.model.singleplayer.SingleplayerGameStateModel;
import ch.primeo.fridgely.service.ProductRepository;
import ch.primeo.fridgely.service.RecipeRepository;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
        startNewGame();
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
        playerController.finishTurn();
        fridgeStockModel.clear();
        // TODO: Fill fridge with random products or predefined product sets to ensure a
        // valid recipe generation possability
        Random rand = new Random();
        int listSize = 12 - (3*(gameStateModel.getCurrentRound())-1);
        List<Product> products = productRepository.getAllProducts();
        List<Product> randProducts = rand
                .ints(listSize, 0, products.size())
                .mapToObj(products::get)
                .collect(Collectors.toList());
        fridgeStockModel.addProducts(randProducts);
    }

    /**
     * Starts a new game.
     */
    public void startNewGame() {
        gameStateModel.resetGame();
        penguinModel.resetHP();
        recipeModel.selectRecipe(null);
        startNewRound();
    }
}

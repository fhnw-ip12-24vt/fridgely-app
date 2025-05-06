package ch.primeo.fridgely.controller.singleplayer;

import ch.primeo.fridgely.controller.GameController;
import ch.primeo.fridgely.model.Product;
import ch.primeo.fridgely.model.Recipe;
import ch.primeo.fridgely.model.RecipeIngredient;
import ch.primeo.fridgely.model.singleplayer.SingleplayerGameStateModel;
import ch.primeo.fridgely.service.ProductRepository;
import ch.primeo.fridgely.service.RecipeRepository;
import org.hibernate.Hibernate;

import javax.sound.sampled.Port;
import java.util.Collections;
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
    private List<Recipe> selectedRecipes;

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

        Random rand = new Random();

        // Select 3 random recipes
        List<Recipe> allRecipes = recipeModel.getAvailableRecipes();
        selectedRecipes = rand
                .ints(0, allRecipes.size())
                .mapToObj(allRecipes::get)
                .limit(3)
                .toList();

        // Load all products from the selected recipes
        List<Product> products = selectedRecipes.stream()
                .flatMap(recipe -> recipe.getIngredients().stream())
                .map(RecipeIngredient::getProduct)
                .distinct()
                .toList();

        fridgeStockModel.addProducts(products);
    }

    /**
     * Gets a list of random products from the ProductRepository.
     *
     * @param count the number of random products to get
     * @return a list of random products
     */
    private List<Product> getRandomProducts(int count){
        Random rand = new Random();
        List<Product> products = productRepository.getAllProducts();
        return rand
                .ints(count, 0, products.size())
                .mapToObj(products::get)
                .collect(Collectors.toList());
    }

    /**
     * Checks if the selected products can create at least two recipes.
     *
     * @param products the list of products to check
     * @return true if at least two recipes can be created, false otherwise
     */
    private boolean checkRandomProductsRecipe(List<Product> products) {
        int count = 0;
        List<Recipe> recipes = recipeModel.getAvailableRecipes();
        for(Recipe recipe : recipes) {
            count += recipeModel.canMakeRecipe(recipe, products) ? 1 : 0;
        }
        return count >= 2;
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

    public List<Recipe> getSelectedRecipes() { return selectedRecipes; }
}

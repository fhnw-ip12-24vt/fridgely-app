package ch.primeo.fridgely.controller;

import ch.primeo.fridgely.model.FridgeStockModel;
import ch.primeo.fridgely.model.PenguinModel;
import ch.primeo.fridgely.model.RecipeModel;
import ch.primeo.fridgely.service.ProductRepository;
import ch.primeo.fridgely.service.RecipeRepository;

/**
 * Abstract base class for game controllers.
 * Contains common functionality for singleplayer and multiplayer game modes.
 */
public abstract class GameController {

    protected final PenguinModel penguinModel;
    protected final FridgeStockModel fridgeStockModel;
    protected final RecipeModel recipeModel;
    protected final ProductRepository productRepository;

    /**
     * Constructs a new game controller.
     * 
     * @param productRepo      the repository for accessing products
     * @param recipeRepository the repository for accessing recipes
     */
    protected GameController(ProductRepository productRepo, RecipeRepository recipeRepository) {
        this.productRepository = productRepo;
        this.penguinModel = new PenguinModel();
        this.fridgeStockModel = new FridgeStockModel();
        this.recipeModel = new RecipeModel(recipeRepository);
    }

    /**
     * Gets the penguin model.
     * 
     * @return the penguin model
     */
    public PenguinModel getPenguinModel() {
        return penguinModel;
    }

    /**
     * Gets the fridge stock model.
     * 
     * @return the fridge stock model
     */
    public FridgeStockModel getFridgeStockModel() {
        return fridgeStockModel;
    }

    /**
     * Gets the recipe model.
     * 
     * @return the recipe model
     */
    public RecipeModel getRecipeModel() {
        return recipeModel;
    }

    /**
     * Gets the product repository.
     * 
     * @return the product repository
     */
    public ProductRepository getProductRepository() {
        return productRepository;
    }
}

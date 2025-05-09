package ch.primeo.fridgely.model;

import ch.primeo.fridgely.service.RecipeRepository;
import lombok.Getter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Model for recipes in the multiplayer game mode. Contains recipes and functionality to match ingredients to products
 * in the fridge.
 */
public class RecipeModel {

    private static final Logger LOGGER = Logger.getLogger(RecipeModel.class.getName());

    /**
     * Property name for changes in the available recipes.
     */
    public static final String PROP_AVAILABLE_RECIPES = "availableRecipes";

    /**
     * Property name for changes in the selected recipe.
     */
    public static final String PROP_SELECTED_RECIPE = "selectedRecipe";

    private final List<Recipe> availableRecipes;

    @Getter
    private Recipe selectedRecipe;
    private final RecipeRepository recipeRepository;
    private final PropertyChangeSupport propertyChangeSupport;

    /**
     * Constructs a new recipe model.
     *
     * @param recipeRepo        the repository for accessing recipes
     * @param availableProducts the list of available products
     */
    public RecipeModel(RecipeRepository recipeRepo, List<Product> availableProducts) {
        this.recipeRepository = recipeRepo;
        this.availableRecipes = new ArrayList<>();
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        loadAvailableRecipes(availableProducts);
    }

    /**
     * Loads available recipes from the repository. Only includes recipes for which the user has at least one
     * ingredient.
     *
     * @param availableProducts the list of available products
     */
    public void loadAvailableRecipes(List<Product> availableProducts) {
        List<Recipe> oldRecipes = new ArrayList<>(availableRecipes);
        availableRecipes.clear();

        try {
            // Get all recipes from repository
            List<Recipe> allRecipes = new ArrayList<>();
            List<RecipeRepository.RecipeDTO> recipeDTOs = recipeRepository.getAllRecipes();

            // Convert DTOs to Recipe objects
            if (recipeDTOs != null && !recipeDTOs.isEmpty()) {
                for (RecipeRepository.RecipeDTO dto : recipeDTOs) {
                    try {
                        recipeRepository.findById(dto.getRecipeId()).ifPresent(allRecipes::add);
                    } catch (Exception e) {
                        System.err.println("Error processing recipe ID " + dto.getRecipeId() + ": " + e.getMessage());
                    }
                }
            }

            // Filter recipes to only include those with at least one matching ingredient
            for (Recipe recipe : allRecipes) {
                if (getMatchingIngredientsCount(recipe, availableProducts) > 0) {
                    availableRecipes.add(recipe);
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Could not load available recipes", e);
        }

        System.out.println("Loaded " + availableRecipes.size() + " recipes with at least one matching ingredient");
        propertyChangeSupport.firePropertyChange(PROP_AVAILABLE_RECIPES, oldRecipes, new ArrayList<>(availableRecipes));
    }

    /**
     * Gets an unmodifiable view of the available recipes.
     *
     * @return the available recipes
     */
    public List<Recipe> getAvailableRecipes() {
        return List.copyOf(availableRecipes);
    }

    /**
     * Selects a recipe.
     *
     * @param recipe the recipe to select
     */
    public void selectRecipe(Recipe recipe) {
        Recipe oldRecipe = selectedRecipe;
        selectedRecipe = recipe;
        propertyChangeSupport.firePropertyChange(PROP_SELECTED_RECIPE, oldRecipe, selectedRecipe);
    }

    /**
     * Gets ingredients for a recipe from the repository.
     *
     * @param recipe the recipe to get ingredients for
     * @return the list of ingredients, or an empty list if none are found
     */
    public List<String> getRecipeIngredientBarcodes(Recipe recipe) {
        if (recipe == null) {
            return List.of();
        }

        try {
            // Use the repository's method to get ingredient barcodes
            return recipeRepository.getRecipeIngredientBarcodes(recipe.getRecipeId());
        } catch (Exception e) {
            // Return empty list if method doesn't exist or fails
            return List.of();
        }
    }

    /**
     * Checks if a recipe can be made with the given products.
     *
     * @param recipe   the recipe to check
     * @param products the available products
     * @return true if the recipe can be made, false otherwise
     */
    public boolean canMakeRecipe(Recipe recipe, List<Product> products) {
        if (recipe == null || products == null) {
            return false;
        }

        // Get the ingredient barcodes for the recipe
        List<String> ingredientBarcodes = getRecipeIngredientBarcodes(recipe);

        // Create a set of product barcodes for quick lookup
        Map<String, Product> productMap = new HashMap<>();
        for (Product product : products) {
            productMap.put(product.getBarcode(), product);
        }

        // Check if all ingredients are available
        for (String barcode : ingredientBarcodes) {
            if (!productMap.containsKey(barcode)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Gets the matching ingredients count for a recipe.
     *
     * @param recipe   the recipe to check
     * @param products the available products
     * @return the number of ingredients that match products in the fridge
     */
    public int getMatchingIngredientsCount(Recipe recipe, List<Product> products) {
        if (recipe == null || products == null) {
            return 0;
        }

        // Get the ingredient barcodes for the recipe
        List<String> ingredientBarcodes = getRecipeIngredientBarcodes(recipe);

        int count = 0;

        for (var product : products) {
            if (ingredientBarcodes.contains(product.getBarcode())) {
                count++;
            }
        }

        return count;
    }

    /**
     * Gets the total number of ingredients for a recipe.
     *
     * @param recipe the recipe to check
     * @return the total number of ingredients
     */
    public int getTotalIngredientsCount(Recipe recipe) {
        if (recipe == null) {
            return 0;
        }

        List<String> ingredientBarcodes = getRecipeIngredientBarcodes(recipe);
        return ingredientBarcodes.size();
    }

    /**
     * Adds a property change listener.
     *
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Removes a property change listener.
     *
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}

package ch.primeo.fridgely.model;

//import ch.primeo.fridgely.model.RecipeIngredient; // Not needed anymore
import ch.primeo.fridgely.service.RecipeRepository;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model for recipes in the multiplayer game mode.
 * Contains recipes and functionality to match ingredients to products in the fridge.
 */
public class RecipeModel {
    
    /**
     * Property name for changes in the available recipes.
     */
    public static final String PROP_AVAILABLE_RECIPES = "availableRecipes";
    
    /**
     * Property name for changes in the selected recipe.
     */
    public static final String PROP_SELECTED_RECIPE = "selectedRecipe";
    
    private final List<Recipe> availableRecipes;
    private Recipe selectedRecipe;
    private final RecipeRepository recipeRepository;
    private final PropertyChangeSupport propertyChangeSupport;
    
    /**
     * Constructs a new recipe model.
     * 
     * @param recipeRepo the repository for accessing recipes
     */
    public RecipeModel(RecipeRepository recipeRepo) {
        this.recipeRepository = recipeRepo;
        this.availableRecipes = new ArrayList<>();
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        loadAvailableRecipes();
    }
      /**
     * Loads available recipes from the repository.
     */
      void loadAvailableRecipes() {
        List<Recipe> oldRecipes = new ArrayList<>(availableRecipes);
        availableRecipes.clear();
        
        try {
            // Directly get recipes using the repository's methods
            List<RecipeRepository.RecipeDTO> recipeDTOs = recipeRepository.getAllRecipes();
            
            // Convert DTOs to Recipe objects
            if (recipeDTOs != null && !recipeDTOs.isEmpty()) {
                for (RecipeRepository.RecipeDTO dto : recipeDTOs) {
                    try {
                        recipeRepository.findById(dto.getRecipeId()).ifPresent(availableRecipes::add);
                    } catch (Exception e) {
                        System.err.println("Error processing recipe ID " + dto.getRecipeId() + ": " + e.getMessage());
                    }
                }
            }
            
            // If no recipes were loaded, try direct entity query as fallback
            if (availableRecipes.isEmpty()) {
                System.out.println("Attempting to load recipes directly from JPA repository...");
                availableRecipes.addAll(recipeRepository.getAllRecipesEntities());
            }
            
        } catch (Exception e) {
            System.err.println("Error loading recipes: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback: Create some dummy recipes for testing
            Recipe dummyRecipe = new Recipe();
            dummyRecipe.setRecipeId(1);
            dummyRecipe.setName("Dummy Recipe");
            dummyRecipe.setDescription("A test recipe");
            availableRecipes.add(dummyRecipe);
        }
        
        System.out.println("Loaded " + availableRecipes.size() + " recipes");
        //propertyChangeSupport.firePropertyChange(PROP_AVAILABLE_RECIPES, oldRecipes, new ArrayList<>(availableRecipes));
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
     * Gets the currently selected recipe.
     * 
     * @return the selected recipe, or null if none is selected
     */
    public Recipe getSelectedRecipe() {
        return selectedRecipe;
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
     * @param recipe the recipe to check
     * @param products the available products
     * @return true if the recipe can be made, false otherwise
     */
    public boolean canMakeRecipe(Recipe recipe, List<Product> products) {
        if (recipe == null || products == null) {
            return false;
        }

        List<Product> recipeProducts = recipe.getProducts();

        if (recipeProducts.size() > productsInStorage.size()) {
            return false;
        }

        // Sequential search for matching products
        int i = 0;
        while(i < recipeProducts.size() && productsInStorage.contains(recipeProducts.get(i))) i++;

        return i == recipeProducts.size();
    }
    
    /**
     * Gets the matching ingredients count for a recipe.
     * 
     * @param recipe the recipe to check
     * @param products the available products
     * @return the number of ingredients that match products in the fridge
     */
    public int getMatchingIngredientsCount(Recipe recipe, List<Product> products) {
        if (recipe == null || products == null) {
            return 0;
        }
        
        // Get the ingredient barcodes for the recipe
        List<String> ingredientBarcodes = getRecipeIngredientBarcodes(recipe);
        
        // Create a set of product barcodes for quick lookup
        Map<String, Product> productMap = new HashMap<>();
        for (Product product : products) {
            productMap.put(product.getBarcode(), product);
        }
        
        // Count matching ingredients
        int count = 0;
        for (String barcode : ingredientBarcodes) {
            if (productMap.containsKey(barcode)) {
                count++;
            }
        }
        
        return count;
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

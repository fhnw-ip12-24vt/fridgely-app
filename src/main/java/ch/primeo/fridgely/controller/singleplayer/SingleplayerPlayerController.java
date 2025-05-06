package ch.primeo.fridgely.controller.singleplayer;

import ch.primeo.fridgely.config.GameConfig;
import ch.primeo.fridgely.model.FridgeStockModel;
import ch.primeo.fridgely.model.PenguinModel;
import ch.primeo.fridgely.model.RecipeModel;
import ch.primeo.fridgely.model.singleplayer.SingleplayerGameStateModel;
import ch.primeo.fridgely.model.Recipe;

/**
 * Controller Player in singleplayer game mode. Handles recipe
 * selection and validation.
 */
public class SingleplayerPlayerController {

    private final FridgeStockModel fridgeStockModel;
    private final SingleplayerGameStateModel gameStateModel;
    private final PenguinModel penguinModel;
    private final RecipeModel recipeModel;

    /**
     * Constructs a new player controller.
     *
     * @param stockModel the model for the fridge stock
     * @param stateModel the model for the game state
     * @param penguModel the model for the penguin HP
     * @param recipModel the model for recipes
     */
    public SingleplayerPlayerController(FridgeStockModel stockModel, SingleplayerGameStateModel stateModel,
            PenguinModel penguModel, RecipeModel recipModel) {
        this.fridgeStockModel = stockModel;
        this.gameStateModel = stateModel;
        this.penguinModel = penguModel;
        this.recipeModel = recipModel;
    }

    /**
     * Selects a recipe and calculates the score based on matching ingredients.
     *
     * @param recipe the recipe to select
     */
    public void selectRecipe(Recipe recipe) {
        // Select the recipe
        recipeModel.selectRecipe(recipe);

    }

    /**
     * Calculates the score for a recipe based on matching ingredients.
     *
     * @param matchingIngredients the number of ingredients that match products in
     *                            the fridge
     * @param totalIngredients    the total number of ingredients in the recipe
     * @return the score for the recipe
     */
    private int calculateRecipeScore(int matchingIngredients, int totalIngredients) {
        int score = matchingIngredients * GameConfig.SCORE_MATCHING_INGREDIENT;

        // Bonus for full match
        if (matchingIngredients == totalIngredients) {
            score += GameConfig.SCORE_FULL_MATCH;
        }

        return score;
    }

    /**
     * Updates the penguin HP based on how well the recipe matches.
     *
     * @param matchRatio the ratio of matching ingredients to total ingredients
     */
    private void updatePenguinHPForRecipe(double matchRatio) {
        // If match ratio is above 0.5, increase HP, otherwise decrease HP
        if (matchRatio >= 0.5) {
            penguinModel.modifyHP(GameConfig.HP_INCREASE);
        } else {
            penguinModel.modifyHP(GameConfig.HP_DECREASE);
        }
    }

    /**
     * Finishes Player 2's turn and moves to the next round.
     */
    public void finishTurn() {
        // Check if a recipe has been selected
        Recipe recipe = recipeModel.getSelectedRecipe();
        if (recipe == null) {
            return;
        }

        // Calculate and add score
        int matchingIngredients = recipeModel.getMatchingIngredientsCount(recipe, fridgeStockModel.getProducts());
        int totalIngredients = recipeModel.getTotalIngredientsCount(recipe);
        int score = calculateRecipeScore(matchingIngredients, totalIngredients);
        gameStateModel.addPlayerScore(score);

        // Update penguin HP
        double matchRatio = (double) matchingIngredients / totalIngredients;
        updatePenguinHPForRecipe(matchRatio);

        // Switch to next round
        // TODO: Implement round switching logic if needed
        gameStateModel.advanceRound();


        // Reset for new round if the game isn't over
        if (!gameStateModel.isGameOver()) {
            recipeModel.selectRecipe(null);

            // Clear the fridge stock for the next round
            fridgeStockModel.clear();
        }
    }
}

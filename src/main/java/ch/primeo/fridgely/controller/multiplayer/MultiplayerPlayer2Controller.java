package ch.primeo.fridgely.controller.multiplayer;

import ch.primeo.fridgely.config.GameConfig;
import ch.primeo.fridgely.model.FridgeStockModel;
import ch.primeo.fridgely.model.PenguinModel;
import ch.primeo.fridgely.model.RecipeModel;
import ch.primeo.fridgely.model.multiplayer.MultiplayerGameStateModel;
import ch.primeo.fridgely.model.Recipe;

/**
 * Controller for Player 2 (Chef) in the multiplayer game mode. Handles recipe selection and validation.
 */
public class MultiplayerPlayer2Controller {

    private final FridgeStockModel fridgeStockModel;
    private final MultiplayerGameStateModel gameStateModel;
    private final PenguinModel penguinModel;
    private final RecipeModel recipeModel;

    /**
     * Constructs a new Player 2 controller.
     *
     * @param stockModel the model for the fridge stock
     * @param stateModel the model for the game state
     * @param penguModel the model for the penguin HP
     * @param recipModel the model for recipes
     */
    public MultiplayerPlayer2Controller(FridgeStockModel stockModel, MultiplayerGameStateModel stateModel,
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
        // Check if it's player 2's turn
        if (gameStateModel.getCurrentPlayer() != MultiplayerGameStateModel.Player.PLAYER2) {
            return;
        }

        // No longer checking if recipe can be made - allow any recipe selection

        // Select the recipe
        recipeModel.selectRecipe(recipe);

    }

    /**
     * Calculates the score for a recipe based on matching ingredients.
     *
     * @param matchingIngredients the number of ingredients that match products in the fridge
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
        // Check if it's player 2's turn
        if (gameStateModel.getCurrentPlayer() != MultiplayerGameStateModel.Player.PLAYER2) {
            return;
        }
        // Check if a recipe has been selected
        Recipe recipe = recipeModel.getSelectedRecipe();
        if (recipe == null) {
            return;
        }

        // Calculate and add score
        int matchingIngredients = recipeModel.getMatchingIngredientsCount(recipe, fridgeStockModel.getProducts());
        int totalIngredients = recipeModel.getTotalIngredientsCount(recipe);
        int score = calculateRecipeScore(matchingIngredients, totalIngredients);
        gameStateModel.addPlayer2Score(score);

        // Update penguin HP
        double matchRatio = (double) matchingIngredients / totalIngredients;
        updatePenguinHPForRecipe(matchRatio);

        // Switch to next player/round
        gameStateModel.nextPlayer();

        // Reset for new round if the game isn't over
        if (!gameStateModel.isGameOver()) {
            recipeModel.selectRecipe(null);

            // Clear the fridge stock for the next round
            fridgeStockModel.clear();
        }

    }
}

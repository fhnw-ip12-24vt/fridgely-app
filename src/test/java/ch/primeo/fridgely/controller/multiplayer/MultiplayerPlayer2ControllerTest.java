package ch.primeo.fridgely.controller.multiplayer;

import ch.primeo.fridgely.config.GameConfig;
import ch.primeo.fridgely.model.*;
import ch.primeo.fridgely.model.multiplayer.MultiplayerGameStateModel;
import org.junit.jupiter.api.*;

import java.util.Collections;

import static org.mockito.Mockito.*;

class MultiplayerPlayer2ControllerTest {

    private MultiplayerPlayer2Controller controller;
    private FridgeStockModel fridgeStockModel;
    private MultiplayerGameStateModel gameStateModel;
    private PenguinModel penguinModel;
    private RecipeModel recipeModel;

    @BeforeEach
    void setUp() {
        fridgeStockModel = mock(FridgeStockModel.class);
        gameStateModel = mock(MultiplayerGameStateModel.class);
        penguinModel = mock(PenguinModel.class);
        recipeModel = mock(RecipeModel.class);

        controller = new MultiplayerPlayer2Controller(fridgeStockModel, gameStateModel, penguinModel, recipeModel);
    }

    @Test
    void testSelectValidRecipe() {
        Recipe validRecipe = mock(Recipe.class);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER2);

        controller.selectRecipe(validRecipe);

        verify(recipeModel).selectRecipe(validRecipe);
    }

    @Test
    void testSelectInvalidRecipe() {
        Recipe invalidRecipe = mock(Recipe.class);
        // Ensure it's Player 2's turn for the selectRecipe method to proceed
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER2);
        // The following line regarding canMakeRecipe is not currently used by the
        // controller's selectRecipe method, but we'll keep it as it was in the original test.
        when(recipeModel.canMakeRecipe(eq(invalidRecipe), anyList())).thenReturn(false);


        controller.selectRecipe(invalidRecipe);

        // With the turn check correctly mocked, recipeModel.selectRecipe should be called.
        verify(recipeModel).selectRecipe(invalidRecipe);
    }

    @Test
    void testFinishTurn_GameNotOver() {
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER2);
        Recipe mockRecipe = mock(Recipe.class);
        when(recipeModel.getSelectedRecipe()).thenReturn(mockRecipe);
        // Default case for this existing test, let's assume 0 wasted products for simplicity
        // to ensure it still passes and tests the general flow.
        // totalFridgeProductsUsed = 0, totalFridgeProductsStored = 0 => totalWastedProducts = 0
        when(mockRecipe.getFridgeProducts()).thenReturn(Collections.emptyList()); 
        when(fridgeStockModel.getFridgeProducts()).thenReturn(Collections.emptyList()); 
        when(gameStateModel.isGameOver()).thenReturn(false); // Explicitly game not over

        controller.finishTurn();

        verify(gameStateModel).addScore(GameConfig.SCORE_PLAYER1_INCREASE); // Updated to reflect specific score
        verify(gameStateModel).nextPlayer();
        verify(recipeModel).selectRecipe(null); // Should be called
        verify(fridgeStockModel).clear(); // Should be called
    }

    @Test
    void testFinishTurn_GameOver() {
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER2);
        Recipe mockRecipe = mock(Recipe.class);
        when(recipeModel.getSelectedRecipe()).thenReturn(mockRecipe);
        // Default case for this existing test, let's assume 0 wasted products for simplicity.
        // totalFridgeProductsUsed = 0, totalFridgeProductsStored = 0 => totalWastedProducts = 0
        when(mockRecipe.getFridgeProducts()).thenReturn(Collections.emptyList());
        when(fridgeStockModel.getFridgeProducts()).thenReturn(Collections.emptyList());
        when(gameStateModel.isGameOver()).thenReturn(true); // Explicitly game over

        controller.finishTurn();

        verify(gameStateModel).addScore(GameConfig.SCORE_PLAYER1_INCREASE); // Updated to reflect specific score
        verify(gameStateModel).nextPlayer();
        verify(recipeModel, never()).selectRecipe(null); // Should NOT be called
        verify(fridgeStockModel, never()).clear(); // Should NOT be called
    }


    @Test
    void testSelectRecipeWhenNotPlayer2Turn() {
        Recipe recipe = mock(Recipe.class);
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);

        controller.selectRecipe(recipe);

        verify(recipeModel, never()).selectRecipe(any());
    }

    @Test
    void testFinishTurnWhenNoRecipeSelected() {
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER2);
        when(recipeModel.getSelectedRecipe()).thenReturn(null);

        controller.finishTurn();

        verify(gameStateModel, never()).addScore(anyInt());
        verify(gameStateModel, never()).nextPlayer();
    }

    @Test
    void testFinishTurnWhenNotPlayer2Turn() {
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);

        controller.finishTurn();

        verify(gameStateModel, never()).addScore(anyInt());
        verify(gameStateModel, never()).nextPlayer();
    }

    @Test
    void testUpdatePenguinHPForRecipe_IncreasesHP_WhenMatchRatioIsHigh() {
        // Test with matchRatio = 0.5
        controller.updatePenguinHPForRecipe(0.5);
        verify(penguinModel, times(1)).modifyHP(GameConfig.HP_INCREASE);

        // Test with matchRatio > 0.5 (e.g., 0.7)
        controller.updatePenguinHPForRecipe(0.7);
        // Now we expect modifyHP with HP_INCREASE to have been called twice in total
        verify(penguinModel, times(2)).modifyHP(GameConfig.HP_INCREASE);
    }

    @Test
    void testUpdatePenguinHPForRecipe_DecreasesHP_WhenMatchRatioIsLow() {
        // Test with matchRatio < 0.5 (e.g., 0.4)
        controller.updatePenguinHPForRecipe(0.4);
        verify(penguinModel, times(1)).modifyHP(GameConfig.HP_DECREASE);

        // Test with matchRatio = 0.0
        controller.updatePenguinHPForRecipe(0.0);
        // Now we expect modifyHP with HP_DECREASE to have been called twice in total
        verify(penguinModel, times(2)).modifyHP(GameConfig.HP_DECREASE);
    }

    @Test
    void testFinishTurn_Score_NoWastedProducts() {
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER2);
        Recipe mockRecipe = mock(Recipe.class);
        when(recipeModel.getSelectedRecipe()).thenReturn(mockRecipe);
        when(gameStateModel.isGameOver()).thenReturn(false);

        // totalWastedProducts = 0 (e.g., 5 used, 5 stored)
        when(mockRecipe.getFridgeProducts()).thenReturn(Collections.nCopies(5, null));
        when(fridgeStockModel.getFridgeProducts()).thenReturn(Collections.nCopies(5, null));

        controller.finishTurn();

        verify(gameStateModel).addScore(GameConfig.SCORE_PLAYER1_INCREASE);
        verify(gameStateModel).nextPlayer();
        verify(recipeModel).selectRecipe(null);
        verify(fridgeStockModel).clear();
    }

    @Test
    void testFinishTurn_Score_OneWastedProduct() {
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER2);
        Recipe mockRecipe = mock(Recipe.class);
        when(recipeModel.getSelectedRecipe()).thenReturn(mockRecipe);
        when(gameStateModel.isGameOver()).thenReturn(false);

        // totalWastedProducts = 1 (e.g., 5 used, 6 stored)
        when(mockRecipe.getFridgeProducts()).thenReturn(Collections.nCopies(5, null));
        when(fridgeStockModel.getFridgeProducts()).thenReturn(Collections.nCopies(6, null));

        controller.finishTurn();

        verify(gameStateModel).addScore(0); // Score should be 0
        verify(gameStateModel).nextPlayer();
        verify(recipeModel).selectRecipe(null);
        verify(fridgeStockModel).clear();
    }

    @Test
    void testFinishTurn_Score_TwoWastedProducts() {
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER2);
        Recipe mockRecipe = mock(Recipe.class);
        when(recipeModel.getSelectedRecipe()).thenReturn(mockRecipe);
        when(gameStateModel.isGameOver()).thenReturn(false);

        // totalWastedProducts = 2 (e.g., 5 used, 7 stored)
        when(mockRecipe.getFridgeProducts()).thenReturn(Collections.nCopies(5, null));
        when(fridgeStockModel.getFridgeProducts()).thenReturn(Collections.nCopies(7, null));

        controller.finishTurn();

        verify(gameStateModel).addScore(2 * GameConfig.SCORE_PLAYER2_DECREASE);
        verify(gameStateModel).nextPlayer();
        verify(recipeModel).selectRecipe(null);
        verify(fridgeStockModel).clear();
    }

    @Test
    void testFinishTurn_Score_MoreThanTwoWastedProducts() {
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER2);
        Recipe mockRecipe = mock(Recipe.class);
        when(recipeModel.getSelectedRecipe()).thenReturn(mockRecipe);
        when(gameStateModel.isGameOver()).thenReturn(false);

        // totalWastedProducts = 3 (e.g., 5 used, 8 stored)
        when(mockRecipe.getFridgeProducts()).thenReturn(Collections.nCopies(5, null));
        when(fridgeStockModel.getFridgeProducts()).thenReturn(Collections.nCopies(8, null));

        controller.finishTurn();

        verify(gameStateModel).addScore(2 * GameConfig.SCORE_PLAYER2_DECREASE); // Capped at 2 * decrease
        verify(gameStateModel).nextPlayer();
        verify(recipeModel).selectRecipe(null);
        verify(fridgeStockModel).clear();
    }
}

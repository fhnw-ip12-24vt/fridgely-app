package ch.primeo.fridgely.controller.multiplayer;

import ch.primeo.fridgely.model.FridgeStockModel;
import ch.primeo.fridgely.model.PenguinModel;
import ch.primeo.fridgely.model.Recipe;
import ch.primeo.fridgely.model.RecipeModel;
import ch.primeo.fridgely.model.multiplayer.MultiplayerGameStateModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        controller = new MultiplayerPlayer2Controller(
            fridgeStockModel, gameStateModel, penguinModel, recipeModel
        );
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
        when(recipeModel.canMakeRecipe(eq(invalidRecipe), anyList())).thenReturn(false);

        controller.selectRecipe(invalidRecipe);

        verify(recipeModel, never()).selectRecipe(invalidRecipe);
    }

    @Test
    void testFinishTurn() {
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER2);
        Recipe mockRecipe = mock(Recipe.class);
        when(recipeModel.getSelectedRecipe()).thenReturn(mockRecipe);
        when(recipeModel.getMatchingIngredientsCount(eq(mockRecipe), anyList())).thenReturn(3);
        when(recipeModel.getTotalIngredientsCount(eq(mockRecipe))).thenReturn(5);

        controller.finishTurn();

        verify(gameStateModel).addPlayer2Score(anyInt());
        verify(penguinModel).modifyHP(anyInt());
        verify(fridgeStockModel).clear();
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

        verify(gameStateModel, never()).addPlayer2Score(anyInt());
        verify(gameStateModel, never()).nextPlayer();
    }

    @Test
    void testFinishTurnWhenNotPlayer2Turn() {
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER1);

        controller.finishTurn();

        verify(gameStateModel, never()).addPlayer2Score(anyInt());
        verify(gameStateModel, never()).nextPlayer();
    }

    @Test
    void testFinishTurnWithGoodMatchRatio() {
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER2);
        Recipe mockRecipe = mock(Recipe.class);
        when(recipeModel.getSelectedRecipe()).thenReturn(mockRecipe);
        when(recipeModel.getMatchingIngredientsCount(eq(mockRecipe), anyList())).thenReturn(4);
        when(recipeModel.getTotalIngredientsCount(eq(mockRecipe))).thenReturn(6);

        controller.finishTurn();

        // 4 matching ingredients out of 6 = 2/3 ratio which is > 0.5
        verify(penguinModel).modifyHP(anyInt());
        verify(gameStateModel).addPlayer2Score(anyInt());
        verify(gameStateModel).nextPlayer();
    }

    @Test
    void testFinishTurnWithPoorMatchRatio() {
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER2);
        Recipe mockRecipe = mock(Recipe.class);
        when(recipeModel.getSelectedRecipe()).thenReturn(mockRecipe);
        when(recipeModel.getMatchingIngredientsCount(eq(mockRecipe), anyList())).thenReturn(2);
        when(recipeModel.getTotalIngredientsCount(eq(mockRecipe))).thenReturn(6);

        controller.finishTurn();

        // 2 matching ingredients out of 6 = 1/3 ratio which is < 0.5
        verify(penguinModel).modifyHP(anyInt());
        verify(gameStateModel).addPlayer2Score(anyInt());
        verify(gameStateModel).nextPlayer();
    }

    @Test
    void testFinishTurnWithPerfectMatch() {
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER2);
        Recipe mockRecipe = mock(Recipe.class);
        when(recipeModel.getSelectedRecipe()).thenReturn(mockRecipe);
        when(recipeModel.getMatchingIngredientsCount(eq(mockRecipe), anyList())).thenReturn(5);
        when(recipeModel.getTotalIngredientsCount(eq(mockRecipe))).thenReturn(5);

        controller.finishTurn();

        verify(gameStateModel).addPlayer2Score(anyInt());
        verify(gameStateModel).nextPlayer();
    }

    @Test
    void testFinishTurnWhenGameIsNotOver() {
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER2);
        Recipe mockRecipe = mock(Recipe.class);
        when(recipeModel.getSelectedRecipe()).thenReturn(mockRecipe);
        when(recipeModel.getMatchingIngredientsCount(eq(mockRecipe), anyList())).thenReturn(3);
        when(recipeModel.getTotalIngredientsCount(eq(mockRecipe))).thenReturn(5);
        when(gameStateModel.isGameOver()).thenReturn(false);

        controller.finishTurn();

        verify(recipeModel).selectRecipe(null);
        verify(fridgeStockModel).clear();
    }

    @Test
    void testFinishTurnWhenGameIsOver() {
        when(gameStateModel.getCurrentPlayer()).thenReturn(MultiplayerGameStateModel.Player.PLAYER2);
        Recipe mockRecipe = mock(Recipe.class);
        when(recipeModel.getSelectedRecipe()).thenReturn(mockRecipe);
        when(recipeModel.getMatchingIngredientsCount(eq(mockRecipe), anyList())).thenReturn(3);
        when(recipeModel.getTotalIngredientsCount(eq(mockRecipe))).thenReturn(5);
        when(gameStateModel.isGameOver()).thenReturn(true);

        controller.finishTurn();

        verify(recipeModel, never()).selectRecipe(null);
        verify(fridgeStockModel, never()).clear();
    }
}
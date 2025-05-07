package ch.primeo.fridgely.controller.multiplayer;

import ch.primeo.fridgely.model.FridgeStockModel;
import ch.primeo.fridgely.model.PenguinModel;
import ch.primeo.fridgely.model.Recipe;
import ch.primeo.fridgely.model.RecipeModel;
import ch.primeo.fridgely.model.multiplayer.MultiplayerGameStateModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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
}
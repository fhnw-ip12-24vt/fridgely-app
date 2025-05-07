package ch.primeo.fridgely.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {

    @Test
    void testGetAndSetRecipeId() {
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);
        assertEquals(1, recipe.getRecipeId());
    }

    @Test
    void testGetAndSetName() {
        Recipe recipe = new Recipe();
        recipe.setName("Pasta");
        assertEquals("Pasta", recipe.getName());
    }

    @Test
    void testGetAndSetNameDE() {
        Recipe recipe = new Recipe();
        recipe.setNameDE("Nudeln");
        assertEquals("Nudeln", recipe.getNameDE());
    }

    @Test
    void testGetAndSetNameFR() {
        Recipe recipe = new Recipe();
        recipe.setNameFR("Pâtes");
        assertEquals("Pâtes", recipe.getNameFR());
    }

    @Test
    void testGetAndSetDescription() {
        Recipe recipe = new Recipe();
        recipe.setDescription("Delicious pasta recipe");
        assertEquals("Delicious pasta recipe", recipe.getDescription());
    }

    @Test
    void testGetAndSetDescriptionDE() {
        Recipe recipe = new Recipe();
        recipe.setDescriptionDE("Leckeres Nudelrezept");
        assertEquals("Leckeres Nudelrezept", recipe.getDescriptionDE());
    }

    @Test
    void testGetAndSetDescriptionFR() {
        Recipe recipe = new Recipe();
        recipe.setDescriptionFR("Recette de pâtes délicieuse");
        assertEquals("Recette de pâtes délicieuse", recipe.getDescriptionFR());
    }

    @Test
    void testGetAndSetIngredients() {
        Recipe recipe = new Recipe();
        List<RecipeIngredient> ingredients = new ArrayList<>();
        recipe.setIngredients(ingredients);
        assertEquals(ingredients, recipe.getIngredients());
    }

    @Test
    void testGetLocalizedName() {
        Recipe recipe = new Recipe();
        recipe.setName("Pasta");
        recipe.setNameDE("Nudeln");
        recipe.setNameFR("Pâtes");

        assertEquals("Pasta", recipe.getLocalizedName("en"));
        assertEquals("Nudeln", recipe.getLocalizedName("de"));
        assertEquals("Pâtes", recipe.getLocalizedName("fr"));
        assertEquals("Pasta", recipe.getLocalizedName("es")); // Fallback to English
    }

    @Test
    void testGetLocalizedDescription() {
        Recipe recipe = new Recipe();
        recipe.setDescription("Delicious pasta recipe");
        recipe.setDescriptionDE("Leckeres Nudelrezept");
        recipe.setDescriptionFR("Recette de pâtes délicieuse");

        assertEquals("Delicious pasta recipe", recipe.getLocalizedDescription("en"));
        assertEquals("Leckeres Nudelrezept", recipe.getLocalizedDescription("de"));
        assertEquals("Recette de pâtes délicieuse", recipe.getLocalizedDescription("fr"));
        assertEquals("Delicious pasta recipe", recipe.getLocalizedDescription("es")); // Fallback to English
    }

    @Test
    void testToString() {
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);
        recipe.setName("Pasta");
        assertEquals("Recipe{recipeId=1, name='Pasta'}", recipe.toString());
    }
}
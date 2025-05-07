package ch.primeo.fridgely.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeIngredientTest {

    @Test
    void testGetAndSetId() {
        // Arrange
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        Long expectedId = 1L;

        // Act
        recipeIngredient.setId(expectedId);
        Long actualId = recipeIngredient.getId();

        // Assert
        assertEquals(expectedId, actualId);
    }

    @Test
    void testGetAndSetRecipe() {
        // Arrange
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        Recipe expectedRecipe = new Recipe();
        expectedRecipe.setRecipeId(1);

        // Act
        recipeIngredient.setRecipe(expectedRecipe);
        Recipe actualRecipe = recipeIngredient.getRecipe();

        // Assert
        assertEquals(expectedRecipe, actualRecipe);
    }

    @Test
    void testGetAndSetProduct() {
        // Arrange
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        Product expectedProduct = new Product();
        expectedProduct.setBarcode("123456789");

        // Act
        recipeIngredient.setProduct(expectedProduct);
        Product actualProduct = recipeIngredient.getProduct();

        // Assert
        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    void testToString() {
        // Arrange
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);
        Product product = new Product();
        product.setBarcode("123456789");
        recipeIngredient.setRecipe(recipe);
        recipeIngredient.setProduct(product);

        // Act
        String actualString = recipeIngredient.toString();

        // Assert
        assertEquals("RecipeIngredient{id=null, recipeId=1, productBarcode='123456789'}", actualString);
    }
}
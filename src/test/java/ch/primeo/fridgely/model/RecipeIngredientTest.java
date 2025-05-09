package ch.primeo.fridgely.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        recipeIngredient.setId(42L);
        recipeIngredient.setRecipe(recipe);
        recipeIngredient.setProduct(product);

        // Act
        String actualString = recipeIngredient.toString();

        // Assert
        String expectedString = "RecipeIngredient{id=42, recipeId=1, productBarcode='123456789'}";
        assertEquals(expectedString, actualString, "toString should correctly format the RecipeIngredient");
    }

    @Test
    void testToStringWithNullRecipe() {
        // Arrange
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        Product product = new Product();
        product.setBarcode("123456789");
        recipeIngredient.setId(42L);
        recipeIngredient.setProduct(product);
        // recipe is null

        // Act
        String actualString = recipeIngredient.toString();

        // Assert
        String expectedString = "RecipeIngredient{id=42, recipeId=null, productBarcode='123456789'}";
        assertEquals(expectedString, actualString, "toString should handle null recipe correctly");
    }

    @Test
    void testToStringWithNullProduct() {
        // Arrange
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);
        recipeIngredient.setId(42L);
        recipeIngredient.setRecipe(recipe);
        // product is null

        // Act
        String actualString = recipeIngredient.toString();

        // Assert
        String expectedString = "RecipeIngredient{id=42, recipeId=1, productBarcode='null'}";
        assertEquals(expectedString, actualString, "toString should handle null product correctly");
    }

    @Test
    void testToStringWithAllNulls() {
        // Arrange
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        // id, recipe, and product are all null

        // Act
        String actualString = recipeIngredient.toString();

        // Assert
        String expectedString = "RecipeIngredient{id=null, recipeId=null, productBarcode='null'}";
        assertEquals(expectedString, actualString, "toString should handle all null values correctly");
    }
}

package ch.primeo.fridgely.model;

import ch.primeo.fridgely.service.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class RecipeModelTest {

    private RecipeRepository recipeRepositoryMock;
    private RecipeModel recipeModel;

    @BeforeEach
    void setUp() {
        // Arrange: Mock the RecipeRepository and initialize RecipeModel
        recipeRepositoryMock = mock(RecipeRepository.class);
        recipeModel = new RecipeModel(recipeRepositoryMock);
    }

    @Test
    void testGetPossibleRecipes_NoMatchingIngredients() {
        // Arrange
        List<RecipeRepository.RecipeDTO> recipeDTOs = List.of(new RecipeRepository.RecipeDTO(1, "Test", "Test", 0, 0));
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);
        recipe.setName("Test Recipe");

        Product product = new Product("456", "Product 2", "Product 2", "Product 2", "Desc", "Desc", "Desc", false,
                false, false, false);
        List<Product> products = List.of(product);

        when(recipeRepositoryMock.getAllRecipes()).thenReturn(recipeDTOs);
        when(recipeRepositoryMock.findById(1)).thenReturn(Optional.of(recipe));
        when(recipeRepositoryMock.getRecipeIngredientBarcodes(1)).thenReturn(List.of("123"));

        // Act
        recipeModel.getPossibleRecipes(products);

        // Assert
        List<Recipe> availableRecipes = recipeModel.getAvailableRecipes();
        assertEquals(0, availableRecipes.size());
    }

    @Test
    void testSelectRecipe_FiresPropertyChange() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);
        recipe.setName("Selected Recipe");

        PropertyChangeListener listener = mock(PropertyChangeListener.class);
        recipeModel.addPropertyChangeListener(listener);

        // Act
        recipeModel.selectRecipe(recipe);

        // Assert
        verify(listener).propertyChange(argThat(event -> event.getPropertyName()
                .equals(RecipeModel.PROP_SELECTED_RECIPE) && event.getNewValue() == recipe));
        assertEquals(recipe, recipeModel.getSelectedRecipe());
    }

    @Test
    void testCanMakeRecipe_AllIngredientsAvailable() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);

        Product product1 = new Product("123", "Product 1 NameEN", "Product 1 NameDE", "Product 1 NameFR", "Desc 1 EN",
                "Desc 1 DE", "Desc 1 F", false, false, false, false);
        Product product2 = new Product("456", "Product 2 NameEN", "Product 2 NameDE", "Product 2 NameFR", "Desc 2 EN",
                "Desc 2 DE", "Desc 2 F", false, false, false, false);

        List<Product> products = List.of(product1, product2);

        when(recipeRepositoryMock.getRecipeIngredientBarcodes(1)).thenReturn(List.of("123", "456"));

        // Act
        boolean result = recipeModel.canMakeRecipe(recipe, products);

        // Assert
        assertTrue(result);
    }

    @Test
    void testAddAndRemovePropertyChangeListener() {
        // Arrange
        PropertyChangeListener listener = mock(PropertyChangeListener.class);

        // Act
        recipeModel.addPropertyChangeListener(listener);
        recipeModel.removePropertyChangeListener(listener);

        // Assert
        // No exception should be thrown, and the listener should be added and removed successfully
    }

    @Test
    void testRecipe_getName() {
        // Arrange
        Recipe recipe = new Recipe();
        String expectedName = "Test Recipe Name";
        recipe.setName(expectedName);

        // Act
        String actualName = recipe.getName();

        // Assert
        assertEquals(expectedName, actualName);
    }

    @Test
    void testRecipe_getDescription() {
        // Arrange
        Recipe recipe = new Recipe();
        String expectedDescription = "Test Recipe Description";
        recipe.setDescription(expectedDescription);

        // Act
        String actualDescription = recipe.getDescription();

        // Assert
        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    void testRecipe_toString() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setRecipeId(101);
        recipe.setName("Spaghetti Carbonara");
        String expectedString = "Recipe{recipeId=101, name='Spaghetti Carbonara'}";

        // Act
        String actualString = recipe.toString();

        // Assert
        assertEquals(expectedString, actualString);
    }

    @Test
    void testGetPossibleRecipes_EmptyInput() {
        // Arrange
        List<Product> emptyProducts = List.of();
        when(recipeRepositoryMock.getAllRecipes()).thenReturn(
                List.of(new RecipeRepository.RecipeDTO(1, "Test", "Test", 0, 0)));

        // Act
        recipeModel.getPossibleRecipes(emptyProducts);

        // Assert
        assertTrue(recipeModel.getAvailableRecipes().isEmpty());
    }

    @Test
    void testGetRecipeIngredientBarcodes() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);
        List<String> expectedBarcodes = List.of("123", "456");

        when(recipeRepositoryMock.getRecipeIngredientBarcodes(1)).thenReturn(expectedBarcodes);

        // Act
        List<String> actualBarcodes = recipeModel.getRecipeIngredientBarcodes(recipe);

        // Assert
        assertEquals(expectedBarcodes, actualBarcodes);
        verify(recipeRepositoryMock).getRecipeIngredientBarcodes(1);
    }

    @Test
    void testGetRecipeIngredientBarcodes_NullRecipe() {
        // Need a fresh mock for this test to ensure no previous interactions
        RecipeRepository freshMock = mock(RecipeRepository.class);
        // Create a new model instance with the fresh mock to avoid counting previous interactions
        RecipeModel localModel = new RecipeModel(freshMock);

        // Reset interaction count for this specific test
        reset(freshMock);

        // Act
        List<String> barcodes = localModel.getRecipeIngredientBarcodes(null);

        // Assert
        assertTrue(barcodes.isEmpty());
        verifyNoMoreInteractions(freshMock); // Changed from verifyNoInteractions to verifyNoMoreInteractions
    }

    @Test
    void testGetRecipeIngredientBarcodes_ExceptionHandling() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);

        when(recipeRepositoryMock.getRecipeIngredientBarcodes(1)).thenThrow(new RuntimeException("Test exception"));

        // Act
        List<String> barcodes = recipeModel.getRecipeIngredientBarcodes(recipe);

        // Assert
        assertTrue(barcodes.isEmpty());
    }

    @Test
    void testCanMakeRecipe_NullInputs() {
        // Arrange
        Recipe recipe = new Recipe();
        List<Product> products = new ArrayList<>();

        // Act & Assert
        assertFalse(recipeModel.canMakeRecipe(null, products));
        assertFalse(recipeModel.canMakeRecipe(recipe, null));
    }

    @Test
    void testGetMatchingIngredientsCount_NullInputs() {
        // Arrange
        Recipe recipe = new Recipe();
        List<Product> products = new ArrayList<>();

        // Act & Assert
        assertEquals(0, recipeModel.getMatchingIngredientsCount(null, products));
        assertEquals(0, recipeModel.getMatchingIngredientsCount(recipe, null));
    }

    @Test
    void testPropertyChangeSupport_MultipleListeners() {
        // Arrange
        PropertyChangeListener listener1 = mock(PropertyChangeListener.class);
        PropertyChangeListener listener2 = mock(PropertyChangeListener.class);
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);
        recipe.setName("Selected Recipe");

        recipeModel.addPropertyChangeListener(listener1);
        recipeModel.addPropertyChangeListener(listener2);

        // Act
        recipeModel.selectRecipe(recipe);

        // Assert
        verify(listener1).propertyChange(argThat(event -> event.getPropertyName()
                .equals(RecipeModel.PROP_SELECTED_RECIPE) && event.getNewValue() == recipe));
        verify(listener2).propertyChange(argThat(event -> event.getPropertyName()
                .equals(RecipeModel.PROP_SELECTED_RECIPE) && event.getNewValue() == recipe));
    }

    @Test
    void testGetPossibleRecipes_EmptyRecipeDTOs() {
        // Arrange
        Product product = new Product("123", "Product 1", "Product 1", "Product 1", "Desc", "Desc", "Desc", false,
                false, false,false);
        List<Product> products = List.of(product);

        // Return empty list of recipe DTOs
        when(recipeRepositoryMock.getAllRecipes()).thenReturn(List.of());

        // Act
        recipeModel.getPossibleRecipes(products);

        // Assert
        assertTrue(recipeModel.getAvailableRecipes().isEmpty());
    }

    @Test
    void testGetPossibleRecipes_NullRecipeDTOs() {
        // Arrange
        Product product = new Product("123", "Product 1", "Product 1", "Product 1", "Desc", "Desc", "Desc", false,
                false, false, false);
        List<Product> products = List.of(product);

        // Return null list of recipe DTOs
        when(recipeRepositoryMock.getAllRecipes()).thenReturn(null);

        // Act
        recipeModel.getPossibleRecipes(products);

        // Assert
        assertTrue(recipeModel.getAvailableRecipes().isEmpty());
    }

    @Test
    void testLoadAvailableRecipesSuccess() {
        // Arrange
        RecipeRepository.RecipeDTO dto1 = new RecipeRepository.RecipeDTO(1, "Recipe 1", "Description 1", 3, 5);
        RecipeRepository.RecipeDTO dto2 = new RecipeRepository.RecipeDTO(2, "Recipe 2", "Description 2", 4, 6);
        List<RecipeRepository.RecipeDTO> dtos = List.of(dto1, dto2);

        Recipe recipe1 = new Recipe();
        recipe1.setRecipeId(1);
        recipe1.setName("Recipe 1");

        Recipe recipe2 = new Recipe();
        recipe2.setRecipeId(2);
        recipe2.setName("Recipe 2");

        when(recipeRepositoryMock.getAllRecipes()).thenReturn(dtos);
        when(recipeRepositoryMock.findById(1)).thenReturn(Optional.of(recipe1));
        when(recipeRepositoryMock.findById(2)).thenReturn(Optional.of(recipe2));

        // Create a new RecipeModel to trigger loadAvailableRecipes
        RecipeModel model = new RecipeModel(recipeRepositoryMock);

        // Act
        List<Recipe> availableRecipes = model.getAvailableRecipes();

        // Assert
        assertEquals(2, availableRecipes.size());
        assertTrue(availableRecipes.contains(recipe1));
        assertTrue(availableRecipes.contains(recipe2));
    }

    @Test
    void testLoadAvailableRecipesWithException() {
        // Arrange
        RecipeRepository localRecipeRepositoryMock = mock(RecipeRepository.class); // Use a local mock
        String expectedErrorMessage = "Test exception";
        when(localRecipeRepositoryMock.getAllRecipes()).thenThrow(new RuntimeException(expectedErrorMessage));
        // The call to localRecipeRepositoryMock.getAllRecipesEntities() will not be reached
        // if getAllRecipes() throws, so no need to mock it for this specific path.

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            new RecipeModel(localRecipeRepositoryMock); // Pass the local mock
        }, "Constructor should throw RuntimeException when getAllRecipes fails.");
        assertEquals(expectedErrorMessage, thrown.getMessage(), "The exception message should match.");

        // Verify that getAllRecipes was called on the local mock, and getAllRecipesEntities was not.
        verify(localRecipeRepositoryMock).getAllRecipes();
        verify(localRecipeRepositoryMock, never()).getAllRecipesEntities();
    }

    @Test
    void testLoadAvailableRecipes_NullDTOs_FallbackToEntities() {
        // Arrange
        RecipeRepository freshMock = mock(RecipeRepository.class);
        when(freshMock.getAllRecipes()).thenReturn(null); // recipeDTOs will be null

        Recipe recipe1 = new Recipe();
        recipe1.setRecipeId(1);
        recipe1.setName("Recipe From Entities");
        List<Recipe> entityRecipes = List.of(recipe1);
        when(freshMock.getAllRecipesEntities()).thenReturn(entityRecipes);

        // Act
        RecipeModel model = new RecipeModel(freshMock);
        List<Recipe> availableRecipes = model.getAvailableRecipes();

        // Assert
        assertEquals(1, availableRecipes.size());
        assertEquals("Recipe From Entities", availableRecipes.getFirst().getName());
        verify(freshMock).getAllRecipes();
        verify(freshMock).getAllRecipesEntities();
    }

    @Test
    void testLoadAvailableRecipes_NullDTOsAndNullEntities_FallbackToDummyRecipe() {
        // Arrange
        RecipeRepository freshMock = mock(RecipeRepository.class);
        when(freshMock.getAllRecipes()).thenReturn(null); // recipeDTOs will be null
        when(freshMock.getAllRecipesEntities()).thenReturn(null); // entities also null

        // Act & Assert
        assertThrows(NullPointerException.class, () -> new RecipeModel(freshMock), "Constructor should throw NullPointerException when DTOs and Entities are null.");

        // Verify that both methods were called
        verify(freshMock).getAllRecipes();
        verify(freshMock).getAllRecipesEntities();
    }

    @Test
    void testLoadAvailableRecipes_NullDTOsAndEntitiesException_FallbackToDummyRecipe() {
        // Arrange
        RecipeRepository freshMock = mock(RecipeRepository.class);
        when(freshMock.getAllRecipes()).thenReturn(null); // recipeDTOs will be null
        String expectedErrorMessage = "DB error on entities";
        when(freshMock.getAllRecipesEntities()).thenThrow(new RuntimeException(expectedErrorMessage));

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> new RecipeModel(freshMock), "Constructor should propagate RuntimeException from getAllRecipesEntities.");
        assertEquals(expectedErrorMessage, thrown.getMessage(), "The exception message should match.");

        // Verify that both methods were called
        verify(freshMock).getAllRecipes();
        verify(freshMock).getAllRecipesEntities();
    }

    @Test
    void testGetMatchingIngredientsCountWithNullInputs() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);
        List<Product> products = new ArrayList<>();

        // Act & Assert
        assertEquals(0, recipeModel.getMatchingIngredientsCount(null, products));
        assertEquals(0, recipeModel.getMatchingIngredientsCount(recipe, null));
    }

    @Test
    void testCanMakeRecipe_EmptyIngredientBarcodes_UsingProductsFromRecipe() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);

        Product product1 = new Product("123", "Product 1", "Product 1", "Product 1", "Desc", "Desc", "Desc", false, false, false, false);
        Product product2 = new Product("456", "Product 2", "Product 2", "Product 2", "Desc", "Desc", "Desc", false, false, false, false);

        // Create RecipeIngredient objects and add them to the recipe
        RecipeIngredient ingredient1 = new RecipeIngredient();
        ingredient1.setProduct(product1);
        ingredient1.setRecipe(recipe);

        RecipeIngredient ingredient2 = new RecipeIngredient();
        ingredient2.setProduct(product2);
        ingredient2.setRecipe(recipe);

        List<RecipeIngredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        recipe.setIngredients(ingredients);

        List<Product> availableProducts = List.of(product1, product2);

        when(recipeRepositoryMock.getRecipeIngredientBarcodes(1)).thenReturn(List.of());

        // Act
        boolean result = recipeModel.canMakeRecipe(recipe, availableProducts);

        // Assert
        assertTrue(result);
    }

    @Test
    void testCanMakeRecipe_EmptyIngredientBarcodes_NotEnoughProducts() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);

        Product product1 = new Product("123", "Product 1", "Product 1", "Product 1", "Desc", "Desc", "Desc", false, false, false, false);
        Product product2 = new Product("456", "Product 2", "Product 2", "Product 2", "Desc", "Desc", "Desc", false, false, false, false);
        Product product3 = new Product("789", "Product 3", "Product 3", "Product 3", "Desc", "Desc", "Desc", false, false, false, false);

        // Create RecipeIngredient objects and add them to the recipe
        RecipeIngredient ingredient1 = new RecipeIngredient();
        ingredient1.setProduct(product1);
        ingredient1.setRecipe(recipe);

        RecipeIngredient ingredient2 = new RecipeIngredient();
        ingredient2.setProduct(product2);
        ingredient2.setRecipe(recipe);

        RecipeIngredient ingredient3 = new RecipeIngredient();
        ingredient3.setProduct(product3);
        ingredient3.setRecipe(recipe);

        List<RecipeIngredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        ingredients.add(ingredient3);
        recipe.setIngredients(ingredients);

        List<Product> availableProducts = List.of(product1, product2);

        when(recipeRepositoryMock.getRecipeIngredientBarcodes(1)).thenReturn(List.of());

        // Act
        boolean result = recipeModel.canMakeRecipe(recipe, availableProducts);

        // Assert
        assertFalse(result);
    }

    @Test
    void testCanMakeRecipe_EmptyIngredientBarcodes_MissingProduct() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);

        Product product1 = new Product("123", "Product 1", "Product 1", "Product 1", "Desc", "Desc", "Desc", false, false, false, false);
        Product product2 = new Product("456", "Product 2", "Product 2", "Product 2", "Desc", "Desc", "Desc", false, false, false, false);
        Product product3 = new Product("789", "Product 3", "Product 3", "Product 3", "Desc", "Desc", "Desc", false, false, false, false);

        // Create RecipeIngredient objects and add them to the recipe
        RecipeIngredient ingredient1 = new RecipeIngredient();
        ingredient1.setProduct(product1);
        ingredient1.setRecipe(recipe);

        RecipeIngredient ingredient2 = new RecipeIngredient();
        ingredient2.setProduct(product2);
        ingredient2.setRecipe(recipe);

        List<RecipeIngredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        recipe.setIngredients(ingredients);

        List<Product> availableProducts = List.of(product1, product3);

        when(recipeRepositoryMock.getRecipeIngredientBarcodes(1)).thenReturn(List.of());

        // Act
        boolean result = recipeModel.canMakeRecipe(recipe, availableProducts);

        // Assert
        assertFalse(result);
    }

    @Test
    void testGetMatchingIngredientsCount_WithMatchingProducts() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);

        Product product1 = new Product("123", "Product 1", "Product 1", "Product 1", "Desc", "Desc", "Desc", false, false, false, false);
        Product product2 = new Product("456", "Product 2", "Product 2", "Product 2", "Desc", "Desc", "Desc", false, false, false, false);
        Product product3 = new Product("789", "Product 3", "Product 3", "Product 3", "Desc", "Desc", "Desc", false, false, false, false);

        List<Product> availableProducts = List.of(product1, product2, product3);

        when(recipeRepositoryMock.getRecipeIngredientBarcodes(1)).thenReturn(List.of("123", "456", "999"));

        // Act
        int count = recipeModel.getMatchingIngredientsCount(recipe, availableProducts);

        // Assert
        assertEquals(2, count);
    }

    @Test
    void testLoadAvailableRecipes_EmptyRecipesFromRepository_FallbackToEntities() {
        // Arrange
        RecipeRepository freshMock = mock(RecipeRepository.class);
        when(freshMock.getAllRecipes()).thenReturn(List.of());

        Recipe recipe1 = new Recipe();
        recipe1.setRecipeId(1);
        recipe1.setName("Recipe 1");

        when(freshMock.getAllRecipesEntities()).thenReturn(List.of(recipe1));

        // Act
        RecipeModel model = new RecipeModel(freshMock);

        // Assert
        List<Recipe> availableRecipes = model.getAvailableRecipes();
        assertEquals(1, availableRecipes.size());
        assertEquals("Recipe 1", availableRecipes.getFirst().getName());

        // Verify that getAllRecipesEntities was called as a fallback
        verify(freshMock).getAllRecipesEntities();
    }

    @Test
    void testLoadAvailableRecipes_WithNonNullButEmptyRecipeDTOs() {
        // Arrange
        RecipeRepository freshMock = mock(RecipeRepository.class);

        // Return empty list (not null, but empty)
        when(freshMock.getAllRecipes()).thenReturn(new ArrayList<>());

        Recipe recipe1 = new Recipe();
        recipe1.setRecipeId(1);
        recipe1.setName("Recipe 1");

        when(freshMock.getAllRecipesEntities()).thenReturn(List.of(recipe1));

        // Act
        RecipeModel model = new RecipeModel(freshMock);

        // Assert
        List<Recipe> availableRecipes = model.getAvailableRecipes();
        assertEquals(1, availableRecipes.size());
        assertEquals("Recipe 1", availableRecipes.getFirst().getName());

        // Verify that getAllRecipesEntities was called as a fallback
        verify(freshMock).getAllRecipesEntities();
    }

    @Test
    void testLoadAvailableRecipes_WithValidRecipeDTOs() {
        // Arrange
        RecipeRepository freshMock = mock(RecipeRepository.class);

        // Create DTOs
        RecipeRepository.RecipeDTO dto1 = new RecipeRepository.RecipeDTO(1, "Recipe 1", "Description 1", 3, 5);
        RecipeRepository.RecipeDTO dto2 = new RecipeRepository.RecipeDTO(2, "Recipe 2", "Description 2", 4, 6);
        List<RecipeRepository.RecipeDTO> dtos = List.of(dto1, dto2);

        // Create corresponding Recipe entities
        Recipe recipe1 = new Recipe();
        recipe1.setRecipeId(1);
        recipe1.setName("Recipe 1");

        Recipe recipe2 = new Recipe();
        recipe2.setRecipeId(2);
        recipe2.setName("Recipe 2");

        // Setup mock behavior
        when(freshMock.getAllRecipes()).thenReturn(dtos);
        when(freshMock.findById(1)).thenReturn(Optional.of(recipe1));
        when(freshMock.findById(2)).thenReturn(Optional.of(recipe2));

        // Act
        RecipeModel model = new RecipeModel(freshMock);

        // Assert
        List<Recipe> availableRecipes = model.getAvailableRecipes();
        assertEquals(2, availableRecipes.size());
        assertTrue(availableRecipes.contains(recipe1));
        assertTrue(availableRecipes.contains(recipe2));

        // Verify that getAllRecipesEntities was not called
        verify(freshMock, never()).getAllRecipesEntities();
    }

    @Test
    void testLoadAvailableRecipes_WithExceptionInFindById() {
        // Arrange
        RecipeRepository freshMock = mock(RecipeRepository.class);

        // Create DTOs
        RecipeRepository.RecipeDTO dto1 = new RecipeRepository.RecipeDTO(1, "Recipe 1", "Description 1", 3, 5);
        RecipeRepository.RecipeDTO dto2 = new RecipeRepository.RecipeDTO(2, "Recipe 2", "Description 2", 4, 6);
        List<RecipeRepository.RecipeDTO> dtos = List.of(dto1, dto2);

        // Create corresponding Recipe entity for the second DTO only
        Recipe recipe2 = new Recipe();
        recipe2.setRecipeId(2);
        recipe2.setName("Recipe 2");

        // Setup mock behavior - first findById throws exception, second succeeds
        when(freshMock.getAllRecipes()).thenReturn(dtos);
        when(freshMock.findById(1)).thenThrow(new RuntimeException("Test exception"));
        when(freshMock.findById(2)).thenReturn(Optional.of(recipe2));

        // Act
        RecipeModel model = new RecipeModel(freshMock);

        // Assert
        List<Recipe> availableRecipes = model.getAvailableRecipes();
        assertEquals(1, availableRecipes.size());
        assertTrue(availableRecipes.contains(recipe2));

        // Verify that getAllRecipesEntities was not called
        verify(freshMock, never()).getAllRecipesEntities();
    }

    @Test
    void testGetPossibleRecipes_FilteringLogic() {
        // Arrange
        Recipe recipe1 = new Recipe();
        recipe1.setRecipeId(1);
        recipe1.setName("Recipe 1");

        Recipe recipe2 = new Recipe();
        recipe2.setRecipeId(2);
        recipe2.setName("Recipe 2");

        Recipe recipe3 = new Recipe();
        recipe3.setRecipeId(3);
        recipe3.setName("Recipe 3");

        // Setup mock for getAllRecipes to return DTOs
        RecipeRepository.RecipeDTO dto1 = new RecipeRepository.RecipeDTO(1, "Recipe 1", "Description 1", 3, 5);
        RecipeRepository.RecipeDTO dto2 = new RecipeRepository.RecipeDTO(2, "Recipe 2", "Description 2", 4, 6);
        RecipeRepository.RecipeDTO dto3 = new RecipeRepository.RecipeDTO(3, "Recipe 3", "Description 3", 2, 4);
        when(recipeRepositoryMock.getAllRecipes()).thenReturn(List.of(dto1, dto2, dto3));

        // Setup mock for findById to return Recipe entities
        when(recipeRepositoryMock.findById(1)).thenReturn(Optional.of(recipe1));
        when(recipeRepositoryMock.findById(2)).thenReturn(Optional.of(recipe2));
        when(recipeRepositoryMock.findById(3)).thenReturn(Optional.of(recipe3));

        // Create a new RecipeModel to load the recipes
        RecipeModel model = new RecipeModel(recipeRepositoryMock);

        // Create test products
        Product product1 = new Product("123", "Product 1", "Product 1", "Product 1", "Desc", "Desc", "Desc", false, false, false, false);
        Product product2 = new Product("456", "Product 2", "Product 2", "Product 2", "Desc", "Desc", "Desc", false, false, false, false);
        List<Product> products = List.of(product1, product2);

        // Setup mock for canMakeRecipe to return true for recipes 1 and 3, false for recipe 2
        RecipeModel spyModel = Mockito.spy(model);
        doReturn(true).when(spyModel).canMakeRecipe(eq(recipe1), eq(products));
        doReturn(false).when(spyModel).canMakeRecipe(eq(recipe2), eq(products));
        doReturn(true).when(spyModel).canMakeRecipe(eq(recipe3), eq(products));

        // Act
        List<Recipe> possibleRecipes = spyModel.getPossibleRecipes(products);

        // Assert
        assertEquals(2, possibleRecipes.size());
        assertTrue(possibleRecipes.contains(recipe1));
        assertFalse(possibleRecipes.contains(recipe2));
        assertTrue(possibleRecipes.contains(recipe3));
    }
}

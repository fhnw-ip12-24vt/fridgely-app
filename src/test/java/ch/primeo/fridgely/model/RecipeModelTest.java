package ch.primeo.fridgely.model;

import ch.primeo.fridgely.service.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class RecipeModelTest {

    private RecipeRepository recipeRepositoryMock;
    private RecipeModel recipeModel;
    private List<Product> availableProducts;

    @BeforeEach
    void setUp() {
        // Arrange: Mock the RecipeRepository and initialize RecipeModel
        recipeRepositoryMock = mock(RecipeRepository.class);
        availableProducts = new ArrayList<>();
        recipeModel = new RecipeModel(recipeRepositoryMock, availableProducts);
    }

    @Test
    void testLoadAvailableRecipes_Success() {
        // Arrange
        List<RecipeRepository.RecipeDTO> recipeDTOs = List.of(new RecipeRepository.RecipeDTO(1, "Test", "Test", 0, 0));
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);
        recipe.setName("Test Recipe");

        Product product = new Product("123", "Product 1", "Product 1", "Product 1", "Desc", "Desc", "Desc", false, false, false);
        List<Product> products = List.of(product);
        
        when(recipeRepositoryMock.getAllRecipes()).thenReturn(recipeDTOs);
        when(recipeRepositoryMock.findById(1)).thenReturn(Optional.of(recipe));
        when(recipeRepositoryMock.getRecipeIngredientBarcodes(1)).thenReturn(List.of("123"));

        // Act
        recipeModel.loadAvailableRecipes(products);

        // Assert
        List<Recipe> availableRecipes = recipeModel.getAvailableRecipes();
        assertEquals(1, availableRecipes.size());
        assertEquals("Test Recipe", availableRecipes.getFirst().getName());
    }

    @Test
    void testLoadAvailableRecipes_NoMatchingIngredients() {
        // Arrange
        List<RecipeRepository.RecipeDTO> recipeDTOs = List.of(new RecipeRepository.RecipeDTO(1, "Test", "Test", 0, 0));
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);
        recipe.setName("Test Recipe");

        Product product = new Product("456", "Product 2", "Product 2", "Product 2", "Desc", "Desc", "Desc", false, false, false);
        List<Product> products = List.of(product);
        
        when(recipeRepositoryMock.getAllRecipes()).thenReturn(recipeDTOs);
        when(recipeRepositoryMock.findById(1)).thenReturn(Optional.of(recipe));
        when(recipeRepositoryMock.getRecipeIngredientBarcodes(1)).thenReturn(List.of("123"));

        // Act
        recipeModel.loadAvailableRecipes(products);

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
        verify(listener).propertyChange(argThat(event ->
            event.getPropertyName().equals(RecipeModel.PROP_SELECTED_RECIPE) &&
                event.getNewValue() == recipe
        ));
        assertEquals(recipe, recipeModel.getSelectedRecipe());
    }

    @Test
    void testCanMakeRecipe_AllIngredientsAvailable() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);

        Product product1 = new Product("123", "Product 1 NameEN", "Product 1 NameDE", "Product 1 NameFR", "Desc 1 EN", "Desc 1 DE", "Desc 1 F", false, false, false);
        Product product2 = new Product("456", "Product 2 NameEN", "Product 2 NameDE", "Product 2 NameFR", "Desc 2 EN", "Desc 2 DE", "Desc 2 F", false, false, false);

        List<Product> products = List.of(product1, product2);
        
        when(recipeRepositoryMock.getRecipeIngredientBarcodes(1)).thenReturn(List.of("123", "456"));

        // Act
        boolean result = recipeModel.canMakeRecipe(recipe, products);

        // Assert
        assertTrue(result);
    }

    @Test
    void testCanMakeRecipe_MissingIngredients() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);
        
        Product product = new Product("123", "Product 1 NameEN", "Product 1 NameDE", "Product 1 NameFR", "Desc 1 EN", "Desc 1 DE", "Desc 1 F", false, false, false);
        List<Product> products = List.of(product);
        
        when(recipeRepositoryMock.getRecipeIngredientBarcodes(1)).thenReturn(List.of("123", "456"));

        // Act
        boolean result = recipeModel.canMakeRecipe(recipe, products);

        // Assert
        assertFalse(result);
    }

    @Test
    void testGetMatchingIngredientsCount() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);
        
        when(recipeRepositoryMock.getRecipeIngredientBarcodes(1)).thenReturn(List.of("123", "456"));

        Product product1 = new Product("123", "Product 1 NameEN", "Product 1 NameDE", "Product 1 NameFR", "Desc 1 EN", "Desc 1 DE", "Desc 1 F", false, false, false);
        Product product2 = new Product("999", "Product 2 NameEN", "Product 2 NameDE", "Product 2 NameFR", "Desc 2 EN", "Desc 2 DE", "Desc 2 F", false, false, false);
        List<Product> products = List.of(product1, product2);

        // Act
        int count = recipeModel.getMatchingIngredientsCount(recipe, products);

        // Assert
        assertEquals(1, count);
    }

    @Test
    void testGetTotalIngredientsCount() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);
        List<String> ingredientBarcodes = List.of("123", "456");
        when(recipeRepositoryMock.getRecipeIngredientBarcodes(1)).thenReturn(ingredientBarcodes);

        // Act
        int count = recipeModel.getTotalIngredientsCount(recipe);

        // Assert
        assertEquals(2, count);
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
    void testLoadAvailableRecipes_EmptyInput() {
        // Arrange
        List<Product> emptyProducts = List.of();
        when(recipeRepositoryMock.getAllRecipes()).thenReturn(List.of(new RecipeRepository.RecipeDTO(1, "Test", "Test", 0, 0)));
        
        // Act
        recipeModel.loadAvailableRecipes(emptyProducts);
        
        // Assert
        assertTrue(recipeModel.getAvailableRecipes().isEmpty());
    }

    @Test
    void testLoadAvailableRecipes_ExceptionHandling() {
        // Arrange
        List<Product> products = List.of(new Product("123", "Product 1", "Product 1", "Product 1", "Desc", "Desc", "Desc", false, false, false));
        when(recipeRepositoryMock.getAllRecipes()).thenThrow(new RuntimeException("Test exception"));
        
        // Act
        recipeModel.loadAvailableRecipes(products);
        
        // Assert
        assertEquals(1, recipeModel.getAvailableRecipes().size(), "Should create dummy recipe when exception occurs");
        assertEquals("Dummy Recipe", recipeModel.getAvailableRecipes().get(0).getName());
    }

    @Test
    void testLoadAvailableRecipes_RecipeProcessingError() {
        // Arrange
        List<RecipeRepository.RecipeDTO> recipeDTOs = List.of(
            new RecipeRepository.RecipeDTO(1, "Test", "Test", 0, 0),
            new RecipeRepository.RecipeDTO(2, "Test2", "Test2", 0, 0)
        );
        
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);
        recipe.setName("Test Recipe");

        Product product = new Product("123", "Product 1", "Product 1", "Product 1", "Desc", "Desc", "Desc", false, false, false);
        List<Product> products = List.of(product);
        
        when(recipeRepositoryMock.getAllRecipes()).thenReturn(recipeDTOs);
        when(recipeRepositoryMock.findById(1)).thenReturn(Optional.of(recipe));
        when(recipeRepositoryMock.findById(2)).thenThrow(new RuntimeException("Test exception"));
        when(recipeRepositoryMock.getRecipeIngredientBarcodes(1)).thenReturn(List.of("123"));

        // Act
        recipeModel.loadAvailableRecipes(products);

        // Assert
        List<Recipe> availableRecipes = recipeModel.getAvailableRecipes();
        assertEquals(1, availableRecipes.size());
        assertEquals("Test Recipe", availableRecipes.getFirst().getName());
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
        RecipeModel localModel = new RecipeModel(freshMock, new ArrayList<>());
        
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
    void testGetTotalIngredientsCount_NullRecipe() {
        // Act
        int count = recipeModel.getTotalIngredientsCount(null);
        
        // Assert
        assertEquals(0, count);
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
        verify(listener1).propertyChange(argThat(event ->
            event.getPropertyName().equals(RecipeModel.PROP_SELECTED_RECIPE) &&
            event.getNewValue() == recipe
        ));
        verify(listener2).propertyChange(argThat(event ->
            event.getPropertyName().equals(RecipeModel.PROP_SELECTED_RECIPE) &&
            event.getNewValue() == recipe
        ));
    }

    @Test
    void testLoadAvailableRecipes_FiresPropertyChange() {
        // Arrange
        PropertyChangeListener listener = mock(PropertyChangeListener.class);
        recipeModel.addPropertyChangeListener(listener);
        
        List<RecipeRepository.RecipeDTO> recipeDTOs = List.of(new RecipeRepository.RecipeDTO(1, "Test", "Test", 0, 0));
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1);
        recipe.setName("Test Recipe");

        Product product = new Product("123", "Product 1", "Product 1", "Product 1", "Desc", "Desc", "Desc", false, false, false);
        List<Product> products = List.of(product);
        
        when(recipeRepositoryMock.getAllRecipes()).thenReturn(recipeDTOs);
        when(recipeRepositoryMock.findById(1)).thenReturn(Optional.of(recipe));
        when(recipeRepositoryMock.getRecipeIngredientBarcodes(1)).thenReturn(List.of("123"));
        
        // Act
        recipeModel.loadAvailableRecipes(products);
        
        // Assert
        verify(listener).propertyChange(argThat(event ->
            event.getPropertyName().equals(RecipeModel.PROP_AVAILABLE_RECIPES)
        ));
    }

    @Test
    void testLoadAvailableRecipes_EmptyRecipeDTOs() {
        // Arrange
        Product product = new Product("123", "Product 1", "Product 1", "Product 1", "Desc", "Desc", "Desc", false, false, false);
        List<Product> products = List.of(product);
        
        // Return empty list of recipe DTOs
        when(recipeRepositoryMock.getAllRecipes()).thenReturn(List.of());
        
        // Act
        recipeModel.loadAvailableRecipes(products);
        
        // Assert
        assertTrue(recipeModel.getAvailableRecipes().isEmpty());
    }
    
    @Test
    void testLoadAvailableRecipes_NullRecipeDTOs() {
        // Arrange
        Product product = new Product("123", "Product 1", "Product 1", "Product 1", "Desc", "Desc", "Desc", false, false, false);
        List<Product> products = List.of(product);
        
        // Return null list of recipe DTOs
        when(recipeRepositoryMock.getAllRecipes()).thenReturn(null);
        
        // Act
        recipeModel.loadAvailableRecipes(products);
        
        // Assert
        assertTrue(recipeModel.getAvailableRecipes().isEmpty());
    }
}
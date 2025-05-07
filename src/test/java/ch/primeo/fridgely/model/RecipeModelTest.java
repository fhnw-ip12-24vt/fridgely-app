package ch.primeo.fridgely.model;

import ch.primeo.fridgely.service.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    void testDefaultConstructor() {
        // Act
        Product product = new Product();

        // Assert
        assertNotNull(product);
        assertNull(product.getBarcode());
        assertNull(product.getName());
        assertNull(product.getNameDE());
        assertNull(product.getNameFR());
        assertNull(product.getDescription());
        assertNull(product.getDescriptionDE());
        assertNull(product.getDescriptionFR());
        assertFalse(product.isDefaultProduct());
        assertFalse(product.isBio());
        assertFalse(product.isLocal());
    }

    @Test
    void testFullConstructor() {
        // Arrange
        String barcode = "123456789";
        String nameE = "Apple";
        String nameD = "Apfel";
        String nameF = "Pomme";
        String descriptionE = "A sweet apple";
        String descriptionD = "Ein süßer Apfel";
        String descriptionF = "Une pomme sucrée";
        boolean defaultProduct = true;
        boolean bio = true;
        boolean local = false;

        // Act
        Product product = new Product(barcode, nameE, nameD, nameF, descriptionE, descriptionD, descriptionF, defaultProduct, bio, local);

        // Assert
        assertEquals(barcode, product.getBarcode());
        assertEquals(nameE, product.getName());
        assertEquals(nameD, product.getNameDE());
        assertEquals(nameF, product.getNameFR());
        assertEquals(descriptionE, product.getDescription());
        assertEquals(descriptionD, product.getDescriptionDE());
        assertEquals(descriptionF, product.getDescriptionFR());
        assertTrue(product.isDefaultProduct());
        assertTrue(product.isBio());
        assertFalse(product.isLocal());
    }

    @Test
    void testLoadAvailableRecipes_Success() {
        // Arrange
        List<RecipeRepository.RecipeDTO> recipeDTOs = List.of(new RecipeRepository.RecipeDTO(1, "Test", "Test", 0, 0, new ArrayList<>()));
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
        List<RecipeRepository.RecipeDTO> recipeDTOs = List.of(new RecipeRepository.RecipeDTO(1, "Test", "Test", 0, 0, new ArrayList<>()));
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

        // Assert & Act
        // No exception should be thrown, and the listener should be added and removed successfully
        assertDoesNotThrow(() -> {
            recipeModel.addPropertyChangeListener(listener);
            recipeModel.removePropertyChangeListener(listener);
        });
    }

    @Test
    void testLoadAvailableRecipes_RepositoryThrowsException() {
        // Arrange
        when(recipeRepositoryMock.getAllRecipes()).thenThrow(new RuntimeException("Repository error"));

        // Act
        recipeModel.loadAvailableRecipes();

        // Assert
        List<Recipe> availableRecipes = recipeModel.getAvailableRecipes();
        assertEquals(1, availableRecipes.size());
        assertEquals("Dummy Recipe", availableRecipes.getFirst().getName());
    }

    @Test
    void testLoadAvailableRecipes_EmptyListFromRepository() {
        // Arrange
        when(recipeRepositoryMock.getAllRecipes()).thenReturn(new ArrayList<>());

        // Act
        recipeModel.loadAvailableRecipes();

        // Assert
        List<Recipe> availableRecipes = recipeModel.getAvailableRecipes();
        assertEquals(0, availableRecipes.size());
    }

    @Test
    void testLoadAvailableRecipes_ErrorProcessingSingleRecipe() {
        // Arrange
        List<RecipeRepository.RecipeDTO> recipeDTOs = List.of(
                new RecipeRepository.RecipeDTO(1, "Valid Recipe", "Test", 0, 0, new ArrayList<>()),
                new RecipeRepository.RecipeDTO(2, "Invalid Recipe", "Test", 0, 0, new ArrayList<>())
        );
        Recipe validRecipe = new Recipe();
        validRecipe.setRecipeId(1);
        validRecipe.setName("Valid Recipe");

        when(recipeRepositoryMock.getAllRecipes()).thenReturn(recipeDTOs);
        when(recipeRepositoryMock.findById(1)).thenReturn(Optional.of(validRecipe));
        when(recipeRepositoryMock.findById(2)).thenThrow(new RuntimeException("Error processing recipe"));

        // Act
        recipeModel.loadAvailableRecipes();

        // Assert
        List<Recipe> availableRecipes = recipeModel.getAvailableRecipes();
        assertEquals(1, availableRecipes.size());
        assertEquals("Valid Recipe", availableRecipes.getFirst().getName());
    }
}
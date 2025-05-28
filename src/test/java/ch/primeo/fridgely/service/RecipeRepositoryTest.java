package ch.primeo.fridgely.service;

import ch.primeo.fridgely.model.QRecipe;
import ch.primeo.fridgely.model.QRecipeIngredient;
import ch.primeo.fridgely.model.Recipe;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeRepositoryTest {

    private final QRecipe qRecipe = QRecipe.recipe;
    @Mock
    private AppLocalizationService localizationService;
    @Mock
    private FridgeStockRepository fridgeStockRepository;
    @Mock
    private RecipeJpaRepository recipeJpaRepository;
    @InjectMocks
    private RecipeRepository recipeRepository;
    private QRecipeIngredient qRecipeIngredient;

    @BeforeEach
    void setUp() {
        qRecipeIngredient = QRecipeIngredient.recipeIngredient;
    }

    @Test
    void getAllRecipes_shouldReturnListOfRecipeDTOs() {
        // Mocks
        AppLocalizationService localizationService = mock(AppLocalizationService.class);
        FridgeStockRepository fridgeStockRepository = mock(FridgeStockRepository.class);
        RecipeJpaRepository recipeJpaRepository = mock(RecipeJpaRepository.class);
        JPAQueryFactory mockQueryFactory = mock(JPAQueryFactory.class);
        recipeRepository = new RecipeRepository(localizationService, fridgeStockRepository, recipeJpaRepository,
                mockQueryFactory);

        // Create recipes
        List<Recipe> recipes = getRecipes();

        // Mock recipes query
        JPAQuery<Recipe> recipeQueryMock = mockJPAQuery();
        when(mockQueryFactory.selectFrom(qRecipe)).thenReturn(recipeQueryMock);
        when(recipeQueryMock.fetch()).thenReturn(recipes);

        // Mock ingredients query
        JPAQuery<Tuple> ingredientsQueryMock = mockJPAQuery();
        when(mockQueryFactory.select(qRecipeIngredient.recipe.recipeId, qRecipeIngredient.product.barcode)).thenReturn(
                ingredientsQueryMock);
        when(ingredientsQueryMock.from(qRecipeIngredient)).thenReturn(ingredientsQueryMock);

        Tuple tuple1 = mock(Tuple.class);
        when(tuple1.get(qRecipeIngredient.recipe.recipeId)).thenReturn(1);
        when(tuple1.get(qRecipeIngredient.product.barcode)).thenReturn("barcode1");

        Tuple tuple2 = mock(Tuple.class);
        when(tuple2.get(qRecipeIngredient.recipe.recipeId)).thenReturn(1);
        when(tuple2.get(qRecipeIngredient.product.barcode)).thenReturn("barcode2");

        Tuple tuple3 = mock(Tuple.class);
        when(tuple3.get(qRecipeIngredient.recipe.recipeId)).thenReturn(2);
        when(tuple3.get(qRecipeIngredient.product.barcode)).thenReturn("barcode3");

        when(ingredientsQueryMock.fetch()).thenReturn(Arrays.asList(tuple1, tuple2, tuple3));

        // Mock localization and fridge stock
        when(localizationService.getLanguage()).thenReturn("en");
        when(fridgeStockRepository.getAllBarcodesInStockAsSet()).thenReturn(Set.of("barcode1", "barcode3"));

        // Run
        List<RecipeRepository.RecipeDTO> result = recipeRepository.getAllRecipes();

        // Assertions
        assertEquals(2, result.size());

        RecipeRepository.RecipeDTO dto1 = result.stream().filter(dto -> dto.getRecipeId() == 1).findFirst()
                .orElse(null);

        assertNotNull(dto1);
        assertEquals("Recipe1_en", dto1.getName());
        assertEquals("Desc1_en", dto1.getDescription());
        assertEquals("Recipe1_en (1/2 available)", dto1.toString());

        RecipeRepository.RecipeDTO dto2 = result.stream().filter(dto -> dto.getRecipeId() == 2).findFirst()
                .orElse(null);

        assertNotNull(dto2);
        assertEquals("Recipe2_en", dto2.getName());
        assertEquals("Desc2_en", dto2.getDescription());
        assertEquals("Recipe2_en (1/1 available)", dto2.toString());

        verify(localizationService).getLanguage();
        verify(fridgeStockRepository).getAllBarcodesInStockAsSet();
    }

    private static List<Recipe> getRecipes() {
        Recipe recipe1 = new Recipe();
        recipe1.setRecipeId(1);
        recipe1.setName("Recipe1_en");
        recipe1.setDescription("Desc1_en");
        recipe1.setNameDE("Recipe1_de");
        recipe1.setDescriptionDE("Desc1_de");
        recipe1.setNameFR("Recipe1_fr");
        recipe1.setDescriptionFR("Desc1_fr");

        Recipe recipe2 = new Recipe();
        recipe2.setRecipeId(2);
        recipe2.setName("Recipe2_en");
        recipe2.setDescription("Desc2_en");

        return Arrays.asList(recipe1, recipe2);
    }

    @Test
    void getAllRecipes_shouldReturnEmptyListWhenNoRecipes() {
        when(localizationService.getLanguage()).thenReturn("en");

        List<RecipeRepository.RecipeDTO> result = recipeRepository.getAllRecipes();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllRecipes_shouldHandleExceptionAndReturnEmptyList() {
        when(localizationService.getLanguage()).thenReturn("en");

        List<RecipeRepository.RecipeDTO> result = recipeRepository.getAllRecipes();

        assertTrue(result.isEmpty());
    }

    @Test
    void getRecipeIngredientBarcodes_shouldReturnBarcodesForRecipeId() {
        JPAQueryFactory queryFactory = mock(JPAQueryFactory.class);
        recipeRepository = new RecipeRepository(localizationService, fridgeStockRepository, recipeJpaRepository,
                queryFactory);

        int recipeId = 1;
        List<String> expectedBarcodes = Arrays.asList("barcode1", "barcode2");

        // Setup complete query chain
        JPAQuery<String> queryMock = mockJPAQuery();
        JPAQuery<String> fromQueryMock = mockJPAQuery();
        JPAQuery<String> whereQueryMock = mockJPAQuery();

        when(queryFactory.select(qRecipeIngredient.product.barcode)).thenReturn(queryMock);
        when(queryMock.from(qRecipeIngredient)).thenReturn(fromQueryMock);
        when(fromQueryMock.where(qRecipeIngredient.recipe.recipeId.eq(recipeId))).thenReturn(whereQueryMock);
        when(whereQueryMock.fetch()).thenReturn(expectedBarcodes);

        List<String> result = recipeRepository.getRecipeIngredientBarcodes(recipeId);

        assertEquals(expectedBarcodes, result);
    }

    @Test
    void getRecipeIngredientBarcodes_shouldReturnEmptyListForNonExistentRecipeId() {
        int recipeId = 99;

        List<String> result = recipeRepository.getRecipeIngredientBarcodes(recipeId);

        assertTrue(result.isEmpty());
    }

    @Test
    void getRecipeIngredientBarcodes_shouldHandleExceptionAndReturnEmptyList() {
        int recipeId = 1;

        List<String> result = recipeRepository.getRecipeIngredientBarcodes(recipeId);

        assertTrue(result.isEmpty());
    }

    @Test
    void findById_shouldReturnRecipeWhenFound() {
        int recipeId = 1;
        Recipe recipe = new Recipe();
        recipe.setRecipeId(recipeId);
        when(recipeJpaRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

        Optional<Recipe> result = recipeRepository.findById(recipeId);

        assertTrue(result.isPresent());
        assertEquals(recipeId, result.get().getRecipeId());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotFound() {
        int recipeId = 1;
        when(recipeJpaRepository.findById(recipeId)).thenReturn(Optional.empty());

        Optional<Recipe> result = recipeRepository.findById(recipeId);

        assertFalse(result.isPresent());
    }

    @Test
    void findById_shouldHandleExceptionAndReturnEmpty() {
        int recipeId = 1;
        when(recipeJpaRepository.findById(recipeId)).thenThrow(new RuntimeException("DB error"));

        Optional<Recipe> result = recipeRepository.findById(recipeId);

        assertFalse(result.isPresent());
    }

    // --- RecipeDTO Tests ---

    @Test
    void testRecipeDTO_getName_returnsCorrectName() {
        RecipeRepository.RecipeDTO dto = new RecipeRepository.RecipeDTO(1, "Apple Pie", "Delicious apple pie", 2, 5);
        assertEquals("Apple Pie", dto.getName());
    }

    @Test
    void testRecipeDTO_getDescription_returnsCorrectDescription() {
        RecipeRepository.RecipeDTO dto = new RecipeRepository.RecipeDTO(1, "Apple Pie", "Delicious apple pie", 2, 5);
        assertEquals("Delicious apple pie", dto.getDescription());
    }

    @Test
    void testRecipeDTO_getRecipeId_returnsCorrectId() {
        RecipeRepository.RecipeDTO dto = new RecipeRepository.RecipeDTO(123, "Apple Pie", "Delicious apple pie", 2, 5);
        assertEquals(123, dto.getRecipeId());
    }

    @Test
    void testRecipeDTO_toString_formatsCorrectly_allIngredientsAvailable() {
        RecipeRepository.RecipeDTO dto = new RecipeRepository.RecipeDTO(1, "Pasta Carbonara", "Classic Italian pasta",
                5, 5);
        assertEquals("Pasta Carbonara (5/5 available)", dto.toString());
    }

    @Test
    void testRecipeDTO_toString_formatsCorrectly_someIngredientsAvailable() {
        RecipeRepository.RecipeDTO dto = new RecipeRepository.RecipeDTO(2, "Salad Bowl", "Healthy salad", 2, 5);
        assertEquals("Salad Bowl (2/5 available)", dto.toString());
    }

    @Test
    void testRecipeDTO_toString_formatsCorrectly_noIngredientsAvailable() {
        RecipeRepository.RecipeDTO dto = new RecipeRepository.RecipeDTO(3, "Fruit Smoothie", "Refreshing smoothie", 0,
                3);
        assertEquals("Fruit Smoothie (0/3 available)", dto.toString());
    }

    @Test
    void testRecipeDTO_toString_formatsCorrectly_zeroTotalIngredients() {
        // This case might represent a recipe with no ingredients listed yet, or an edge case.
        RecipeRepository.RecipeDTO dto = new RecipeRepository.RecipeDTO(4, "Empty Recipe", "No ingredients", 0, 0);
        assertEquals("Empty Recipe (0/0 available)", dto.toString());
    }

    @Test
    void testRecipeDTO_constructor_setsCountsCorrectly() {
        // This test indirectly verifies availableIngredientCount and totalIngredientCount via toString,
        // as they don't have public getters.
        int id = 1;
        String name = "Test Recipe";
        String description = "Test Description";
        int available = 3;
        int total = 7;
        RecipeRepository.RecipeDTO dto = new RecipeRepository.RecipeDTO(id, name, description, available, total);
        assertTrue(dto.toString().contains(name));
        assertTrue(dto.toString().contains("(" + available + "/" + total + " available)"));
    }

    @Test
    void primaryConstructor_shouldInitializeRepository() {
        // Mocks
        AppLocalizationService mockLocalization = mock(AppLocalizationService.class);
        FridgeStockRepository mockStockRepo = mock(FridgeStockRepository.class);
        RecipeJpaRepository mockRecipeJpaRepo = mock(RecipeJpaRepository.class);
        EntityManager mockEntityManager = mock(EntityManager.class);

        // Create repository instance using primary constructor
        RecipeRepository repository = new RecipeRepository(mockLocalization, mockStockRepo, mockRecipeJpaRepo,
                mockEntityManager);

        // Verify repository is initialized by calling a method
        when(mockLocalization.getLanguage()).thenReturn("en");

        // We expect an empty list because we haven't set up the query factory
        // that would be created internally from the EntityManager
        List<RecipeRepository.RecipeDTO> recipes = repository.getAllRecipes();

        // Verify the repository attempted to get language from service
        verify(mockLocalization).getLanguage();

        // Since EntityManager is mocked and doesn't create a real JPAQueryFactory,
        // we expect an empty list (the error handling path)
        assertTrue(recipes.isEmpty());
    }

    @Test
    void getAllRecipesEntities_shouldReturnRecipesFromRepository() {
        // Arrange
        List<Recipe> expectedRecipes = getRecipes();
        when(recipeJpaRepository.findAll()).thenReturn(expectedRecipes);

        // Act
        List<Recipe> result = recipeRepository.getAllRecipesEntities();

        // Assert
        assertEquals(expectedRecipes, result);
        assertEquals(2, result.size());
    }

    @Test
    void getAllRecipesEntities_shouldHandleExceptionAndReturnEmptyList() {
        // Arrange
        when(recipeJpaRepository.findAll()).thenThrow(new RuntimeException("Test exception"));

        // Act
        List<Recipe> result = recipeRepository.getAllRecipesEntities();

        // Assert
        assertTrue(result.isEmpty());
    }

    @SuppressWarnings("unchecked")
    private <T> JPAQuery<T> mockJPAQuery() {
        return (JPAQuery<T>) mock(JPAQuery.class);
    }
}

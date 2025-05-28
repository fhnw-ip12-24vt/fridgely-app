package ch.primeo.fridgely.service;

import ch.primeo.fridgely.model.QRecipe;
import ch.primeo.fridgely.model.QRecipeIngredient;
import ch.primeo.fridgely.model.Recipe;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Repository to manage recipes using Spring Data JPA and QueryDSL.
 */
@Service
public class RecipeRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeRepository.class.getName());

    private final AppLocalizationService localizationService;
    private final FridgeStockRepository fridgeStockRepository;
    private final JPAQueryFactory queryFactory;
    private final RecipeJpaRepository recipeJpaRepository;

    // Define Q-classes
    private final QRecipe qRecipe = QRecipe.recipe;
    private final QRecipeIngredient qRecipeIngredient = QRecipeIngredient.recipeIngredient;

    @Autowired
    public RecipeRepository(AppLocalizationService localization, FridgeStockRepository stockRepo,
                            RecipeJpaRepository recipeJpaRepo, EntityManager entityManager) {
        this.localizationService = localization;
        this.fridgeStockRepository = stockRepo;
        this.recipeJpaRepository = recipeJpaRepo;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    // Constructor for testing only
    protected RecipeRepository(AppLocalizationService localizationService,
                               FridgeStockRepository fridgeStockRepository,
                               RecipeJpaRepository recipeJpaRepository,
                               JPAQueryFactory queryFactory) {
        this.localizationService = localizationService;
        this.fridgeStockRepository = fridgeStockRepository;
        this.recipeJpaRepository = recipeJpaRepository;
        this.queryFactory = queryFactory;
    }

    // Remove JDBC helper methods (createRecipeFromResultSet, parseIngredients)

    /**
     * Returns all recipes with localized names/descriptions and availability.
     *
     * @return a list of Recipe DTO objects
     */
    @Transactional(readOnly = true)
    public List<RecipeDTO> getAllRecipes() {
        String language = localizationService.getLanguage();

        try {
            // Fetch all recipes
            List<Recipe> recipes = queryFactory.selectFrom(qRecipe).fetch();

            // Fetch all ingredients for all recipes efficiently
            Map<Integer, List<String>> ingredientsByRecipeId = queryFactory.select(qRecipeIngredient.recipe.recipeId,
                    qRecipeIngredient.product.barcode).from(qRecipeIngredient).fetch().stream().collect(
                    Collectors.groupingBy(tuple -> tuple.get(qRecipeIngredient.recipe.recipeId),
                            Collectors.mapping(tuple -> tuple.get(qRecipeIngredient.product.barcode),
                                    Collectors.toList())));

            // Get stock info once
            Set<String> barcodesInStock = fridgeStockRepository.getAllBarcodesInStockAsSet();

            // Create DTOs
            return recipes.stream().map(recipe -> {
                List<String> ingredients = ingredientsByRecipeId.getOrDefault(recipe.getRecipeId(), List.of());
                return createRecipeDTO(recipe, ingredients, barcodesInStock, language);
            }).collect(Collectors.toList());

        } catch (Exception e) {
            LOGGER.error("Error fetching all recipes: {}", e.getMessage());
            return List.of();
        }
    }

    /**
     * Returns all ingredient barcodes for a given recipe using QueryDSL.
     *
     * @param recipeId the recipe ID
     * @return a list of ingredient barcodes
     */
    @Transactional(readOnly = true)
    public List<String> getRecipeIngredientBarcodes(int recipeId) {
        try {
            return queryFactory.select(qRecipeIngredient.product.barcode).from(qRecipeIngredient)
                    .where(qRecipeIngredient.recipe.recipeId.eq(recipeId)).fetch();
        } catch (Exception e) {
            LOGGER.error("Error fetching ingredients for recipe ID {}: {}", recipeId, e.getMessage());
            return List.of();
        }
    }

    /**
     * Finds a Recipe entity by its ID.
     *
     * @param recipeId the recipe ID to look up
     * @return an Optional containing the Recipe if found, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<Recipe> findById(int recipeId) {
        try {
            return recipeJpaRepository.findById(recipeId);
        } catch (Exception e) {
            LOGGER.error("Error finding recipe by ID {}: {}", recipeId, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Returns all Recipe entities directly from the database. This is a simplified method that doesn't include
     * availability information.
     *
     * @return a list of Recipe entities
     */
    @Transactional(readOnly = true)
    public List<Recipe> getAllRecipesEntities() {
        try {
            return recipeJpaRepository.findAll();
        } catch (Exception e) {
            LOGGER.error("Error fetching all recipe entities: {}", e.getMessage());
            return List.of();
        }
    }

    // --- Helper Methods --- //

    /**
     * Creates a RecipeDTO from a Recipe entity and related data.
     */
    private RecipeDTO createRecipeDTO(Recipe recipe, List<String> ingredientBarcodes, Set<String> barcodesInStock,
                                      String language) {
        List<String> availableIngredients = ingredientBarcodes.stream().filter(barcodesInStock::contains)
                .toList();

        return new RecipeDTO(recipe.getRecipeId(), recipe.getLocalizedName(language),
                // Use localization method from entity
                recipe.getLocalizedDescription(language), // Use localization method from entity
                availableIngredients.size(), ingredientBarcodes.size());
    }

    // --- DTO Class --- //

    /**
     * Data Transfer Object for Recipe information, including availability. This replaces the direct manipulation of the
     * Recipe model for view purposes.
     */
    public static class RecipeDTO {
        @Getter
        private final int recipeId;
        @Getter
        private final String name;
        @Getter
        private final String description;
        private final int availableIngredientCount;
        private final int totalIngredientCount;

        public RecipeDTO(int id, String nameE, String descriptionE, int availableIngredient, int totalIngredient) {
            this.recipeId = id;
            this.name = nameE;
            this.description = descriptionE;
            this.availableIngredientCount = availableIngredient;
            this.totalIngredientCount = totalIngredient;
        }

        @Override
        public String toString() {
            // Fix syntax error: remove extra closing parenthesis
            return name + " (" + availableIngredientCount + "/" + totalIngredientCount + " available)";
        }
        // equals and hashCode can be added if needed
    }
}

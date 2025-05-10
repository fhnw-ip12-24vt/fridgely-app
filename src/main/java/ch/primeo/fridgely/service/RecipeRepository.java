package ch.primeo.fridgely.service;

import ch.primeo.fridgely.model.QRecipe;
import ch.primeo.fridgely.model.QRecipeIngredient;
import ch.primeo.fridgely.model.Recipe;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Repository to manage recipes using Spring Data JPA and QueryDSL.
 */
@Service
public class RecipeRepository {

    private static final Logger LOGGER = Logger.getLogger(RecipeRepository.class.getName());

    private final AppLocalizationService localizationService;
    private final FridgeStockRepository fridgeStockRepository;
    private final JPAQueryFactory queryFactory;
    private final RecipeJpaRepository recipeJpaRepository;

    // Define Q-classes
    private final QRecipe qRecipe = QRecipe.recipe;
    private final QRecipeIngredient qRecipeIngredient = QRecipeIngredient.recipeIngredient;

    public RecipeRepository(AppLocalizationService localization, FridgeStockRepository stockRepo,
            RecipeJpaRepository recipeJpaRepo, EntityManager entityManager) {
        this.localizationService = localization;
        this.fridgeStockRepository = stockRepo;
        this.recipeJpaRepository = recipeJpaRepo;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    // Remove JDBC helper methods (createRecipeFromResultSet, parseIngredients)

    /**
     * Returns a single recipe by its ID with localized name/description and availability.
     *
     * @param recipeId the recipe ID
     * @return An Optional containing the Recipe DTO, or empty if not found.
     */
    @Transactional(readOnly = true)
    public Optional<RecipeDTO> getRecipeById(int recipeId) {
        String language = localizationService.getLanguage();

        try {
            // Fetch basic recipe info
            Recipe recipe = queryFactory.selectFrom(qRecipe).where(qRecipe.recipeId.eq(recipeId)).fetchOne();

            if (recipe == null) {
                return Optional.empty();
            }

            // Fetch ingredients for the recipe
            List<String> ingredientBarcodes = getRecipeIngredientBarcodes(recipeId);

            // Get stock info
            Set<String> barcodesInStock = fridgeStockRepository.getAllBarcodesInStockAsSet();

            // Create and populate DTO
            RecipeDTO dto = createRecipeDTO(recipe, ingredientBarcodes, barcodesInStock, language);
            return Optional.of(dto);

        } catch (Exception e) {
            LOGGER.severe("Error fetching recipe by ID " + recipeId + ": " + e.getMessage());
            return Optional.empty();
        }
    }

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
            LOGGER.severe("Error fetching all recipes: " + e.getMessage());
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
            LOGGER.severe("Error fetching ingredients for recipe ID " + recipeId + ": " + e.getMessage());
            return List.of();
        }
    }

    // getRecipesByAvailabilityCategories might need rethinking with DTOs or separate queries
    // This implementation provides a basic structure but might be inefficient.

    /**
     * Returns recipes categorized by how many of the given ingredients they use. Note: This implementation fetches all
     * recipes and then categorizes them in memory. Consider optimizing if performance is critical.
     *
     * @param scannedBarcodes list of scanned ingredient barcodes
     * @return a map of match count to list of Recipe DTO objects
     */
    @Transactional(readOnly = true)
    public Map<Integer, List<RecipeDTO>> getRecipesByAvailabilityCategories(List<String> scannedBarcodes) {
        Set<String> scannedBarcodesSet = new HashSet<>(scannedBarcodes);
        List<RecipeDTO> allRecipeDTOs = getAllRecipes(); // Reuse existing method
        Map<Integer, List<RecipeDTO>> categories = new HashMap<>();

        for (RecipeDTO dto : allRecipeDTOs) {
            int matchCount = 0;
            List<String> requiredIngredients = getRecipeIngredientBarcodes(dto.getRecipeId());
            for (String ingredient : requiredIngredients) {
                if (scannedBarcodesSet.contains(ingredient)) {
                    matchCount++;
                }
            }
            categories.computeIfAbsent(matchCount, k -> new ArrayList<>()).add(dto);
        }

        // Ensure all categories from 0 to scannedBarcodes.size() exist, even if empty
        for (int i = 0; i <= scannedBarcodes.size(); i++) {
            categories.putIfAbsent(i, new ArrayList<>());
        }

        return categories;
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
            LOGGER.severe("Error finding recipe by ID " + recipeId + ": " + e.getMessage());
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
            LOGGER.severe("Error fetching all recipe entities: " + e.getMessage());
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
                .collect(Collectors.toList());

        return new RecipeDTO(recipe.getRecipeId(), recipe.getLocalizedName(language),
                // Use localization method from entity
                recipe.getLocalizedDescription(language), // Use localization method from entity
                availableIngredients.size(), ingredientBarcodes.size(), availableIngredients);
    }

    // --- DTO Class --- //

    /**
     * Data Transfer Object for Recipe information, including availability. This replaces the direct manipulation of the
     * Recipe model for view purposes.
     */
    public static class RecipeDTO {
        private final int recipeId;
        private final String name;
        private final String description;
        private final int availableIngredientCount;
        private final int totalIngredientCount;
        private final List<String> availableIngredients;

        public RecipeDTO(int id, String nameE, String descriptionE, int availableIngredient, int totalIngredient,
                List<String> availableIngredientsList) {
            this.recipeId = id;
            this.name = nameE;
            this.description = descriptionE;
            this.availableIngredientCount = availableIngredient;
            this.totalIngredientCount = totalIngredient;
            this.availableIngredients = Collections.unmodifiableList(availableIngredientsList); // Make list immutable
        }

        // Getters for all fields
        public int getRecipeId() {
            return recipeId;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public int getAvailableIngredientCount() {
            return availableIngredientCount;
        }

        public int getTotalIngredientCount() {
            return totalIngredientCount;
        }

        public List<String> getAvailableIngredients() {
            return availableIngredients;
        }

        @Override
        public String toString() {
            // Fix syntax error: remove extra closing parenthesis
            return name + " (" + availableIngredientCount + "/" + totalIngredientCount + " available)";
        }
        // equals and hashCode can be added if needed
    }
}

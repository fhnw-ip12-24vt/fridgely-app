package ch.primeo.fridgely.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a recipe entity mapping to the 'Recipes' table.
 */
@Entity
// No explicit table name, Hibernate will use lowercase "recipe"
public class Recipe {
    /**
     * Unique identifier for the recipe. Hibernate will map this to "recipe_id" column
     */
    @Id // Mark as primary key
    private int recipeId;

    /**
     * Base name of the recipe (e.g., English). Hibernate will map this to "name" column
     */
    private String name;

    /**
     * German name of the recipe. Hibernate will map this to "name_de" column
     */
    private String nameDE;

    /**
     * French name of the recipe. Hibernate will map this to "name_fr" column
     */
    private String nameFR;

    /**
     * Base description of the recipe. Hibernate will map this to "description" column
     */
    private String description;

    /**
     * German description of the recipe. Hibernate will map this to "description_de" column
     */
    private String descriptionDE;

    /**
     * French description of the recipe. Hibernate will map this to "description_fr" column
     */
    private String descriptionFR;

    /**
     * List of ingredients associated with this recipe. Mapped by the 'recipe' field in the RecipeIngredient entity.
     */
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    /**
     * Default constructor required by JPA.
     */
    public Recipe() {
    }

    /**
     * Constructs a new Recipe with essential information. Note: This constructor might need adjustment based on actual
     * usage or removed if only the default constructor is needed.
     *
     * @param id                the unique recipe ID
     * @param recipeName        the base recipe name
     * @param recipeDescription the base recipe description
     */
    public Recipe(int id, String recipeName, String recipeDescription) {
        this.recipeId = id;
        this.name = recipeName;
        this.description = recipeDescription;
        // Initialize other fields as needed, e.g., localized names/descriptions
    }

    // --- Getters ---

    public int getRecipeId() {
        return recipeId;
    }

    public String getName(String language) {
        return switch (language != null ? language.toLowerCase() : "") {
            case "de" -> nameDE != null ? nameDE : name;
            case "fr" -> nameFR != null ? nameFR : name;
            default -> name;
        };
    }

    public String getName() {
        return name;
    }

    public String getNameDE() {
        return nameDE;
    }

    public String getNameFR() {
        return nameFR;
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionDE() {
        return descriptionDE;
    }

    public String getDescriptionFR() {
        return descriptionFR;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public List<Product> getProducts() {
        return ingredients.stream().map(RecipeIngredient::getProduct).toList();
    }

    public List<Product> getDefaultProducts() {
        return ingredients.stream().map(RecipeIngredient::getProduct).filter(Product::isDefaultProduct).toList();
    }

    public List<Product> getFridgeProducts() {
        return ingredients.stream().map(RecipeIngredient::getProduct).filter(Product::isNotDefaultProduct).toList();
    }

    // --- Setters ---

    public void setRecipeId(int id) {
        this.recipeId = id;
    }

    public void setName(String nameE) {
        this.name = nameE;
    }

    public void setNameDE(String nameD) {
        this.nameDE = nameD;
    }

    public void setNameFR(String nameF) {
        this.nameFR = nameF;
    }

    public void setDescription(String descriptionE) {
        this.description = descriptionE;
    }

    public void setDescriptionDE(String descriptionD) {
        this.descriptionDE = descriptionD;
    }

    public void setDescriptionFR(String descriptionF) {
        this.descriptionFR = descriptionF;
    }

    public void setIngredients(List<RecipeIngredient> ingr) {
        this.ingredients = ingr;
    }

    // --- Convenience methods for localization (can be moved to service layer if preferred) ---

    /**
     * Gets the localized name based on the provided language code.
     *
     * @param language "en", "de", or "fr"
     * @return Localized name or base name if localization not found.
     */
    @Transient // Not persisted
    public String getLocalizedName(String language) {
        return switch (language) {
            case "de" -> getNameDE() != null ? getNameDE() : getName();
            case "fr" -> getNameFR() != null ? getNameFR() : getName();
            default -> getName(); // Default to base name (English)
        };
    }

    /**
     * Gets the localized description based on the provided language code.
     *
     * @param language "en", "de", or "fr"
     * @return Localized description or base description if localization not found.
     */
    @Transient // Not persisted
    public String getLocalizedDescription(String language) {
        return switch (language) {
            case "de" -> getDescriptionDE() != null ? getDescriptionDE() : getDescription();
            case "fr" -> getDescriptionFR() != null ? getDescriptionFR() : getDescription();
            default -> getDescription(); // Default to base description (English)
        };
    }

    // toString, equals, hashCode methods can be added if needed
    @Override
    public String toString() {
        // Fix invalid character constant by escaping single quotes
        return "Recipe{" + "recipeId=" + recipeId + ", name='" + name + '\'' + '}';
    }
}

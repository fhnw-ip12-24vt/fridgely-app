package ch.primeo.fridgely.model;

import jakarta.persistence.*;

/**
 * Represents the relationship between a Recipe and a Product (ingredient),
 * mapping to the 'RecipeIngredients' join table.
 */
@Entity
// No explicit table name, Hibernate will use snake_case: "recipe_ingredient"
public class RecipeIngredient {

    /**
     * A unique identifier for the recipe-ingredient relationship.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use auto-incrementing ID
    private Long id; // Using Long for generated ID

    /**
     * The recipe this ingredient belongs to.
     * Hibernate will use "recipe_id" as the foreign key column name
     */
    @ManyToOne(fetch = FetchType.LAZY) // Lazy fetch is often preferred for performance
    private Recipe recipe;

    /**
     * The product (ingredient) required for the recipe.
     * Hibernate will use "product_id" as the foreign key column name
     * and automatically reference the primary key of the Product entity
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    /**
     * Default constructor required by JPA.
     */
    public RecipeIngredient() {
    }

    // --- Getters ---

    public Long getId() {
        return id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public Product getProduct() {
        return product;
    }

    // --- Setters ---

    public void setId(Long id) {
        this.id = id;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // toString, equals, hashCode methods can be added if needed
    @Override
    public String toString() {
        return "RecipeIngredient{" +
               "id=" + id +
               ", recipeId=" + (recipe != null ? recipe.getRecipeId() : "null") +
               ", productBarcode='" + (product != null ? product.getBarcode() : "null") + "'" +
               '}';
    }
}

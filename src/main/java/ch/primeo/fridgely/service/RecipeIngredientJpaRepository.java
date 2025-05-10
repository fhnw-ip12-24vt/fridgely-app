package ch.primeo.fridgely.service;

import ch.primeo.fridgely.model.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the RecipeIngredient entity.
 */
@Repository
public interface RecipeIngredientJpaRepository extends JpaRepository<RecipeIngredient, Long> {
    // Example custom query: Find ingredients by Recipe ID
    List<RecipeIngredient> findByRecipeRecipeId(int recipeId);

    // Example custom query: Find ingredients by Product Barcode
    List<RecipeIngredient> findByProductBarcode(String barcode);
}

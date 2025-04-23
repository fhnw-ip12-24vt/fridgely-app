package ch.primeo.fridgely.service;

import ch.primeo.fridgely.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Recipe entity.
 */
@Repository
public interface RecipeJpaRepository extends JpaRepository<Recipe, Integer> {
    // Basic CRUD methods are inherited from JpaRepository
    // Custom query methods can be added here if needed
}

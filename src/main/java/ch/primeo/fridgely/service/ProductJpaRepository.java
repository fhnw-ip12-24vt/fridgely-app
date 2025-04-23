package ch.primeo.fridgely.service;

import ch.primeo.fridgely.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Product entity.
 * Includes Querydsl support via QuerydslPredicateExecutor.
 */
@Repository
public interface ProductJpaRepository extends JpaRepository<Product, String>, QuerydslPredicateExecutor<Product> {
    // JpaRepository provides basic CRUD operations (findById, findAll, save, delete, etc.)
    // QuerydslPredicateExecutor provides methods to execute Querydsl Predicates (findOne, findAll, count, exists)

    // Custom query methods can be added here if needed, following Spring Data naming conventions
    // e.g., List<Product> findByIsDefaultProductTrue();
}

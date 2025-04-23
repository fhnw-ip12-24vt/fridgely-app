package ch.primeo.fridgely.service;

import ch.primeo.fridgely.model.FridgeStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FridgeStock entity.
 */
@Repository
public interface FridgeStockJpaRepository extends JpaRepository<FridgeStock, String> {
    // Basic CRUD methods are inherited from JpaRepository
    // Custom query methods can be added here if needed, e.g., findByBarcodeIn(List<String> barcodes)
}

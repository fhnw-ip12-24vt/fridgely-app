package ch.primeo.fridgely.service;

import ch.primeo.fridgely.model.FridgeStock;
import ch.primeo.fridgely.model.QFridgeStock; // Import generated Q-class
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service; // Change from @Component to @Service for consistency
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Repository to manage the fridge stock using Spring Data JPA and QueryDSL.
 */
@Service
public class FridgeStockRepository {

    private final FridgeStockJpaRepository fridgeStockJpaRepository;
    private final JPAQueryFactory queryFactory;
    private final QFridgeStock qFridgeStock = QFridgeStock.fridgeStock;

    /**
     * Initializes a new FridgeStockRepository with JPA dependencies.
     */

    public FridgeStockRepository(FridgeStockJpaRepository fridgeStockJpaRepository, EntityManager entityManager) {
        this.fridgeStockJpaRepository = fridgeStockJpaRepository;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    /**
     * Adds multiple products to the fridge stock using JPA.
     * @param barcodes list of product barcodes to add
     * @return true if all products were added successfully (or already existed)
     */
    @Transactional // Add transactionality for write operations
    public boolean addProductsToStock(List<String> barcodes) {
        if (barcodes == null || barcodes.isEmpty()) {
            return true;
        }

        try {
            List<FridgeStock> stockItems = barcodes.stream()
                    .map(FridgeStock::new)
                    .collect(Collectors.toList());
            fridgeStockJpaRepository.saveAll(stockItems);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Adds a single product to the fridge stock.
     * @param barcode the product barcode to add
     * @return true if the product was added successfully
     */
    @Transactional
    public boolean addProductToStock(String barcode) {
        if (barcode == null || barcode.isEmpty()) {
            return false;
        }
        return addProductsToStock(List.of(barcode));
    }

    /**
     * Removes multiple products from the fridge stock using QueryDSL.
     * @param barcodes list of product barcodes to remove
     * @return true if products were removed successfully
     */
    @Transactional
    public boolean removeProductsFromStock(List<String> barcodes) {
        if (barcodes == null || barcodes.isEmpty()) {
            return true;
        }

        try {
            queryFactory.delete(qFridgeStock).where(qFridgeStock.barcode.in(barcodes)).execute();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Removes a single product from the fridge stock.
     * @param barcode the product barcode to remove
     * @return true if the product was removed successfully
     */
    @Transactional
    public boolean removeProductFromStock(String barcode) {
        if (barcode == null || barcode.isEmpty()) {
            return false;
        }
        return removeProductsFromStock(List.of(barcode));
    }

    /**
     * Checks if a single product exists in stock using JPA repository.
     * @param barcode the product barcode to check
     * @return true if the product is in stock
     */
    @Transactional(readOnly = true) // Read-only transaction
    public boolean isProductInStock(String barcode) {
        if (barcode == null || barcode.isEmpty()) {
            return false;
        }
        return fridgeStockJpaRepository.existsById(barcode);
    }

    /**
     * Gets all product barcodes in stock using QueryDSL.
     * @return A list of all product barcodes currently in stock.
     */
    @Transactional(readOnly = true)
    public List<String> getAllBarcodesInStock() {
        return queryFactory
                    .select(qFridgeStock.barcode)
                    .from(qFridgeStock)
                    .fetch();
    }

    /**
     * Gets all product barcodes in stock as a Set using QueryDSL.
     * Useful for efficient lookups.
     * @return A set of all product barcodes currently in stock.
     */
    @Transactional(readOnly = true)
    public Set<String> getAllBarcodesInStockAsSet() {
        return Set.copyOf(getAllBarcodesInStock());
    }

    /**
     * Clears all products from stock using QueryDSL.
     * @return true if the stock was cleared successfully
     */
    @Transactional
    public boolean clearStock() {
        try {
            queryFactory.delete(qFridgeStock).execute();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

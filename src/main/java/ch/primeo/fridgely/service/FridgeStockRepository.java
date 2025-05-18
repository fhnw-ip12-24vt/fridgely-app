package ch.primeo.fridgely.service;

import ch.primeo.fridgely.model.QFridgeStock; // Import generated Q-class
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service; // Change from @Component to @Service for consistency
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Repository to manage the fridge stock using Spring Data JPA and QueryDSL.
 */
@Service
public class FridgeStockRepository {

    private final JPAQueryFactory queryFactory;
    private final QFridgeStock qFridgeStock = QFridgeStock.fridgeStock;

    /**
     * Initializes a new FridgeStockRepository with JPA dependencies.
     *
     * @param manager the EntityManager for QueryDSL
     */

    public FridgeStockRepository(EntityManager manager) {
        this.queryFactory = new JPAQueryFactory(manager);
    }

    /**
     * Gets all product barcodes in stock using QueryDSL.
     *
     * @return A list of all product barcodes currently in stock.
     */
    @Transactional(readOnly = true)
    public List<String> getAllBarcodesInStock() {
        return queryFactory.select(qFridgeStock.barcode).distinct().from(qFridgeStock).fetch();
    }

    /**
     * Gets all product barcodes in stock as a Set using QueryDSL. Useful for efficient lookups.
     *
     * @return A set of all product barcodes currently in stock.
     */
    @Transactional(readOnly = true)
    public Set<String> getAllBarcodesInStockAsSet() {
        return Set.copyOf(getAllBarcodesInStock());
    }
}

package ch.primeo.fridgely.service;

import ch.primeo.fridgely.model.Product;
import ch.primeo.fridgely.model.QProduct;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Repository to manage the products using Spring Data JPA and QueryDSL.
 */
@Service
public class ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final EntityManager entityManager;
    private final QProduct qProduct = QProduct.product;

    public ProductRepository(ProductJpaRepository productRepo, EntityManager entityManager) {
        this.productJpaRepository = productRepo;
        this.entityManager = entityManager;
    }

    /**
     * Returns the product for the given barcode using JpaRepository.
     *
     * @param barcode the product barcode
     * @return the Product object, or null if not found
     */
    @Transactional(readOnly = true)
    public Product getProductByBarcode(String barcode) {
        if (barcode == null) {
            return null;
        }

        Optional<Product> productOptional = productJpaRepository.findById(barcode);
        return productOptional.orElse(null);
    }

    /**
     * Returns all default products in the database using QueryDSL.
     *
     * @return a list of default Product objects
     */
    @Transactional(readOnly = true)
    public List<Product> getAllDefaultProducts() {
        return createQueryFactory()
                .selectFrom(qProduct)
                .where(qProduct.isDefaultProduct.isTrue())
                .fetch();
    }
    
    /**
     * Creates a new JPAQueryFactory using the entity manager.
     * This method is extracted to allow for better testing.
     * 
     * @return a new JPAQueryFactory
     */
    protected JPAQueryFactory createQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}

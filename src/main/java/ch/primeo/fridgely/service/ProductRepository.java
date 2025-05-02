package ch.primeo.fridgely.service;

import ch.primeo.fridgely.model.Product;
import ch.primeo.fridgely.model.QProduct; // Import the generated Q-class
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository to manage the products using Spring Data JPA and QueryDSL.
 */
@Service
public class ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final JPAQueryFactory queryFactory;
    private final QProduct qProduct = QProduct.product;

    public ProductRepository(ProductJpaRepository productRepo, EntityManager entityManager) {
        this.productJpaRepository = productRepo;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * Returns the products mapped by barcode for the given list of barcodes using QueryDSL.
     *
     * @param barcodes the list of product barcodes
     * @return a map of barcode to Product
     */
    @Transactional(readOnly = true)
    public Map<String, Product> getProductsByBarcodes(List<String> barcodes) {
        if (barcodes == null || barcodes.isEmpty()) {
            return Map.of();
        }

        List<Product> productList = queryFactory.selectFrom(qProduct).where(qProduct.barcode.in(barcodes)).fetch();

        return productList.stream().collect(Collectors.toMap(Product::getBarcode, product -> product));
    }

    /**
     * Returns the products for the given barcodes as a list.
     *
     * @param barcodes the list of product barcodes
     * @return a list of Product objects
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByBarcodesAsList(List<String> barcodes) {
        if (barcodes == null || barcodes.isEmpty()) {
            return List.of();
        }

        return (List<Product>) productJpaRepository.findAll(qProduct.barcode.in(barcodes));
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
     * Returns all products in the database using JpaRepository.
     *
     * @return a list of all Product objects
     */
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productJpaRepository.findAll();
    }

    /**
     * Returns all default products in the database using QueryDSL.
     *
     * @return a list of default Product objects
     */
    @Transactional(readOnly = true)
    public List<Product> getAllDefaultProducts() {
        return queryFactory.selectFrom(qProduct).where(qProduct.isDefaultProduct.isTrue()).fetch();
    }
}

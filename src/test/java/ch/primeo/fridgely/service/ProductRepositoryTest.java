package ch.primeo.fridgely.service;

import ch.primeo.fridgely.model.Product;
import ch.primeo.fridgely.model.QProduct;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @Mock
    private ProductJpaRepository productJpaRepository;
    @Mock
    private EntityManager entityManager;
    @Mock
    private JPAQueryFactory queryFactory;
    @Mock
    private JPAQuery<Product> jpaQuery;
    private ProductRepository productRepository;
    private Product product1;
    private Product defaultProduct;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setBarcode("123");
        product1.setName("Product 1");
        product1.setDefaultProduct(false);

        defaultProduct = new Product();
        defaultProduct.setBarcode("789");
        defaultProduct.setName("Default Product");
        defaultProduct.setDefaultProduct(true);

        // Create a spy of the repository that uses mocked queryFactory
        productRepository = Mockito.spy(new ProductRepository(productJpaRepository, entityManager));
    }

    @Test
    void getProductByBarcode_givenExistingBarcode_returnsProduct() {
        when(productJpaRepository.findById("123")).thenReturn(Optional.of(product1));
        Product result = productRepository.getProductByBarcode("123");
        assertEquals(product1, result);
    }

    @Test
    void getProductByBarcode_givenNonExistingBarcode_returnsNull() {
        when(productJpaRepository.findById("nonexistent")).thenReturn(Optional.empty());
        Product result = productRepository.getProductByBarcode("nonexistent");
        assertNull(result);
    }

    @Test
    void getProductByBarcode_givenNullBarcode_returnsNull() {
        Product result = productRepository.getProductByBarcode(null);
        assertNull(result);
    }

    @Test
    void getAllDefaultProducts_whenDefaultProductsExist_returnsListOfDefaultProducts() {
        Mockito.doReturn(queryFactory).when(productRepository).createQueryFactory();

        // Setup the query chain
        when(queryFactory.selectFrom(QProduct.product)).thenReturn(jpaQuery);
        when(jpaQuery.where(QProduct.product.isDefaultProduct.isTrue())).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(Collections.singletonList(defaultProduct));

        List<Product> result = productRepository.getAllDefaultProducts();

        assertEquals(1, result.size());
        assertTrue(result.contains(defaultProduct));
        assertFalse(result.contains(product1));
    }

    @Test
    void getAllDefaultProducts_whenNoDefaultProductsExist_returnsEmptyList() {
        Mockito.doReturn(queryFactory).when(productRepository).createQueryFactory();

        // Setup the query chain
        when(queryFactory.selectFrom(QProduct.product)).thenReturn(jpaQuery);
        when(jpaQuery.where(QProduct.product.isDefaultProduct.isTrue())).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(Collections.emptyList());

        List<Product> result = productRepository.getAllDefaultProducts();

        assertTrue(result.isEmpty());
    }

    @Test
    void createQueryFactory_returnsNonNullJPAQueryFactory() {
        // Create a non-spy instance of ProductRepository with the real dependencies
        ProductRepository realProductRepository = new ProductRepository(productJpaRepository, entityManager);

        // Call the method under test
        JPAQueryFactory result = realProductRepository.createQueryFactory();

        // Verify the result is not null
        assertNotNull(result);
    }
}

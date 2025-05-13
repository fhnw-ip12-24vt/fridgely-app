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
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
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

    @Test
    void testGetProductsByBarcodesWithEmptyList() {
        // Act
        var result = productRepository.getProductsByBarcodes(List.of());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetProductsByBarcodesWithNullList() {
        // Act
        var result = productRepository.getProductsByBarcodes(null);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetProductsByBarcodesAsListWithEmptyList() {
        // Act
        var result = productRepository.getProductsByBarcodesAsList(List.of());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetProductsByBarcodesAsListWithNullList() {
        // Act
        var result = productRepository.getProductsByBarcodesAsList(null);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getProductsByBarcodes_ExistingBarcodes_ReturnsMapOfProducts() {
        // Arrange
        List<String> barcodes = List.of("123", "789");
        List<Product> expectedProducts = List.of(product1, defaultProduct);

        Mockito.doReturn(queryFactory).when(productRepository).createQueryFactory();
        when(queryFactory.selectFrom(QProduct.product)).thenReturn(jpaQuery);
        when(jpaQuery.where(QProduct.product.barcode.in(barcodes))).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(expectedProducts);

        // Act
        Map<String, Product> result = productRepository.getProductsByBarcodes(barcodes);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(product1, result.get("123"));
        assertEquals(defaultProduct, result.get("789"));
        verify(productRepository).createQueryFactory();
    }

    @Test
    void getProductsByBarcodes_NoMatchingProducts_ReturnsEmptyMap() {
        // Arrange
        List<String> barcodes = List.of("nonexistent1", "nonexistent2");

        Mockito.doReturn(queryFactory).when(productRepository).createQueryFactory();
        when(queryFactory.selectFrom(QProduct.product)).thenReturn(jpaQuery);
        when(jpaQuery.where(QProduct.product.barcode.in(barcodes))).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(Collections.emptyList());

        // Act
        Map<String, Product> result = productRepository.getProductsByBarcodes(barcodes);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).createQueryFactory();
    }

    @Test
    void testGetProductsByBarcodesAsList() {
        // Arrange
        List<String> barcodes = List.of("123", "456");
        List<Product> products = List.of(product1);

        when(productJpaRepository.findAll(QProduct.product.barcode.in(barcodes))).thenReturn(products);

        // Act
        var result = productRepository.getProductsByBarcodesAsList(barcodes);

        // Assert
        assertEquals(1, result.size());
        assertEquals(product1, result.get(0));
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        List<Product> products = List.of(product1, defaultProduct);
        when(productJpaRepository.findAll()).thenReturn(products);

        // Act
        var result = productRepository.getAllProducts();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(product1));
        assertTrue(result.contains(defaultProduct));
    }
}

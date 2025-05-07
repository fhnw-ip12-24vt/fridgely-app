package ch.primeo.fridgely.service;

import ch.primeo.fridgely.model.Product;
import ch.primeo.fridgely.model.QProduct;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductRepositoryTest {

   /* @Mock
    private ProductJpaRepository productJpaRepositoryMock;

    @Mock
    private EntityManager entityManagerMock;

    @Mock
    private JPAQueryFactory queryFactoryMock;

    @Mock
    private JPAQuery<Product> jpaQueryMock;

    @InjectMocks
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productRepository = new ProductRepository(productJpaRepositoryMock, entityManagerMock);
    }

    @Test
    void testConstructor() {
        // Arrange & Act
        ProductRepository repository = new ProductRepository(productJpaRepositoryMock, entityManagerMock);

        // Assert
        assertNotNull(repository);
    }

    @Test
    void testGetProductsByBarcodes_WithValidBarcodes() {
        // Arrange
        List<String> barcodes = List.of("123", "456");
        Product product1 = new Product("123", "Product1","Product1", "Product1", "Product1", "Product1", "Product1",false, false,false );
        Product product2 =  new Product("456", "Product2","Product2", "Product2", "Product2", "Product2", "Product2",false, false,false );
        when(queryFactoryMock.selectFrom(QProduct.product)).thenReturn(jpaQueryMock);
        when(jpaQueryMock.where(QProduct.product.barcode.in(barcodes))).thenReturn(jpaQueryMock);
        when(jpaQueryMock.fetch()).thenReturn(List.of(product1, product2));

        // Act
        Map<String, Product> result = productRepository.getProductsByBarcodes(barcodes);

        // Assert
        assertEquals(2, result.size());
        assertEquals(product1, result.get("123"));
        assertEquals(product2, result.get("456"));
    }

    @Test
    void testGetProductsByBarcodes_WithEmptyBarcodes() {
        // Arrange
        List<String> barcodes = List.of();

        // Act
        Map<String, Product> result = productRepository.getProductsByBarcodes(barcodes);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetProductsByBarcodesAsList_WithValidBarcodes() {
        // Arrange
        List<String> barcodes = List.of("123", "456");
        Product product1 = new Product("123", "Product1","Product1", "Product1", "Product1", "Product1", "Product1",false, false,false );
        Product product2 =  new Product("456", "Product2","Product2", "Product2", "Product2", "Product2", "Product2",false, false,false );
        when(productJpaRepositoryMock.findAll(QProduct.product.barcode.in(barcodes))).thenReturn(List.of(product1, product2));

        // Act
        List<Product> result = productRepository.getProductsByBarcodesAsList(barcodes);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(product1));
        assertTrue(result.contains(product2));
    }

    @Test
    void testGetProductsByBarcodesAsList_WithEmptyBarcodes() {
        // Arrange
        List<String> barcodes = List.of();

        // Act
        List<Product> result = productRepository.getProductsByBarcodesAsList(barcodes);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetProductByBarcode_WithValidBarcode() {
        // Arrange
        String barcode = "123";
        Product product = new Product(barcode, "Product1", "Product1", "Product1", "Product1", "Product1", "Product1", false, false, false);
        when(productJpaRepositoryMock.findById(barcode)).thenReturn(Optional.of(product));

        // Act
        Product result = productRepository.getProductByBarcode(barcode);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testGetProductByBarcode_WithNullBarcode() {
        // Act
        Product result = productRepository.getProductByBarcode(null);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetProductByBarcode_WithNonExistentBarcode() {
        // Arrange
        String barcode = "123";
        when(productJpaRepositoryMock.findById(barcode)).thenReturn(Optional.empty());

        // Act
        Product result = productRepository.getProductByBarcode(barcode);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        Product product1 = new Product("123", "Product1","Product1", "Product1", "Product1", "Product1", "Product1",false, false,false );
        Product product2 =  new Product("456", "Product2","Product2", "Product2", "Product2", "Product2", "Product2",false, false,false );
        when(productJpaRepositoryMock.findAll()).thenReturn(List.of(product1, product2));

        // Act
        List<Product> result = productRepository.getAllProducts();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(product1));
        assertTrue(result.contains(product2));
    }

    @Test
    void testGetAllDefaultProducts() {
        // Arrange
        Product product1 = new Product("123", "Product1", "Product1", "Product1", "Product1", "Product1", "Product1", false, false, false);
        Product product2 = new Product("456", "Product2", "Product2", "Product2", "Product2", "Product2", "Product2", false, false, false);
        when(queryFactoryMock.selectFrom(QProduct.product)).thenReturn(jpaQueryMock);
        when(jpaQueryMock.where(QProduct.product.isDefaultProduct.isTrue())).thenReturn(jpaQueryMock);
        when(jpaQueryMock.fetch()).thenReturn(List.of(product1, product2));

        // Act
        List<Product> result = productRepository.getAllDefaultProducts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(product1, product2)));
    }*/
}
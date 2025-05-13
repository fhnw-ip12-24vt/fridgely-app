package ch.primeo.fridgely.model;

import ch.primeo.fridgely.service.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for the ProductModel.
 */
@ExtendWith(MockitoExtension.class)
class ProductModelTest {

    @Mock
    private ProductRepository productRepository;

    private ProductModel productModel;
    private List<Product> mockProducts;

    @BeforeEach
    void setUp() {
        // Create mock products
        mockProducts = new ArrayList<>();
        
        Product product1 = new Product();
        product1.setBarcode("123");
        product1.setName("Product 1");
        product1.setDefaultProduct(false);
        
        Product product2 = new Product();
        product2.setBarcode("456");
        product2.setName("Product 2");
        product2.setDefaultProduct(true);
        
        Product product3 = new Product();
        product3.setBarcode("789");
        product3.setName("Product 3");
        product3.setDefaultProduct(true);
        
        mockProducts.add(product1);
        mockProducts.add(product2);
        mockProducts.add(product3);
        
        // Mock the repository to return our mock products
        when(productRepository.getAllProducts()).thenReturn(mockProducts);
    }

    @Test
    void testConstructor() {
        // Act
        productModel = new ProductModel(productRepository);
        
        // Assert
        verify(productRepository).getAllProducts();
        assertNotNull(productModel);
    }

    @Test
    void testGetProducts() {
        // Arrange
        productModel = new ProductModel(productRepository);
        
        // Act
        List<Product> result = productModel.getProducts();
        
        // Assert
        assertEquals(mockProducts, result);
        assertEquals(3, result.size());
    }

    @Test
    void testGetDefaultProducts() {
        // Arrange
        productModel = new ProductModel(productRepository);
        
        // Act
        List<Product> result = productModel.getDefaultProducts();
        
        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(Product::isDefaultProduct));
        assertFalse(result.contains(mockProducts.get(0))); // First product is not default
        assertTrue(result.contains(mockProducts.get(1))); // Second product is default
        assertTrue(result.contains(mockProducts.get(2))); // Third product is default
    }

    @Test
    void testGetDefaultProductsWithNoDefaultProducts() {
        // Arrange
        List<Product> noDefaultProducts = new ArrayList<>();
        Product product = new Product();
        product.setBarcode("123");
        product.setName("Product 1");
        product.setDefaultProduct(false);
        noDefaultProducts.add(product);
        
        when(productRepository.getAllProducts()).thenReturn(noDefaultProducts);
        
        productModel = new ProductModel(productRepository);
        
        // Act
        List<Product> result = productModel.getDefaultProducts();
        
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetDefaultProductsWithEmptyProductList() {
        // Arrange
        when(productRepository.getAllProducts()).thenReturn(new ArrayList<>());
        
        productModel = new ProductModel(productRepository);
        
        // Act
        List<Product> result = productModel.getDefaultProducts();
        
        // Assert
        assertTrue(result.isEmpty());
    }
}

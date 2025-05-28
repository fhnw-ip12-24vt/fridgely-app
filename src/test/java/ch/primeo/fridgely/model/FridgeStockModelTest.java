package ch.primeo.fridgely.model;

import lombok.Getter;
import org.junit.jupiter.api.*;

import java.beans.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FridgeStockModelTest {

    private FridgeStockModel fridgeStockModel;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        fridgeStockModel = new FridgeStockModel(null);
        product1 = new Product("123456789", "Apple", "Apfel", "Pomme", "Fresh apple", "Frischer Apfel", "Pomme fraîche",
                true, true, false, false);
        product2 = new Product("987654321", "Banana", "Banane", "Banane", "Fresh banana", "Frische Banane",
                "Banane fraîche", false, false, true, false);
    }

    @Test
    void testAddProduct() {
        // Arrange
        Product product = new Product("123456789", "Apple", "Apfel", "Pomme", "Fresh apple", "Frischer Apfel",
                "Pomme fraîche", true, true, false, false);

        // Act
        boolean firstAddResult = fridgeStockModel.addProduct(product);
        boolean secondAddResult = fridgeStockModel.addProduct(product);
        int productCount = fridgeStockModel.getFridgeProducts().size();

        // Assert
        assertTrue(firstAddResult);
        assertFalse(secondAddResult); // Duplicate
        assertEquals(1, productCount);
    }

    @Test
    void testAddNullProduct() {
        // Act
        boolean result = fridgeStockModel.addProduct(null);
        int productCount = fridgeStockModel.getFridgeProducts().size();

        // Assert
        assertFalse(result);
        assertEquals(0, productCount);
    }

    @Test
    void testGetProducts() {
        // Arrange
        fridgeStockModel.addProduct(product1);
        fridgeStockModel.addProduct(product2);

        // Act
        List<Product> products = fridgeStockModel.getFridgeProducts();

        // Assert
        assertEquals(2, products.size());
        assertTrue(products.contains(product1));
        assertTrue(products.contains(product2));
    }

    @Test
    void testClear() {
        // Arrange
        fridgeStockModel.addProduct(product1);
        fridgeStockModel.addProduct(product2);

        // Act
        fridgeStockModel.clear();
        int productCount = fridgeStockModel.getFridgeProducts().size();

        // Assert
        assertEquals(0, productCount);
    }

    @Test
    void testgetFridgeProductsCount() {
        // Arrange
        fridgeStockModel.addProduct(product1);

        // Act
        int productCount = fridgeStockModel.getFridgeProducts().size();

        // Assert
        assertEquals(1, productCount);
    }

    @Test
    void testRemoveProduct_ExistingProduct() {
        // Arrange
        fridgeStockModel.addProduct(product1);
        fridgeStockModel.addProduct(product2);
        assertEquals(2, fridgeStockModel.getFridgeProducts().size());

        // Act
        fridgeStockModel.removeProduct(product1);

        // Assert
        assertEquals(1, fridgeStockModel.getFridgeProducts().size());
        assertFalse(fridgeStockModel.getFridgeProducts().contains(product1));
        assertTrue(fridgeStockModel.getFridgeProducts().contains(product2));
    }

    @Test
    void testRemoveProduct_NonExistingProduct() {
        // Arrange
        fridgeStockModel.addProduct(product1);
        assertEquals(1, fridgeStockModel.getFridgeProducts().size());

        Product nonExistingProduct = new Product("999", "Non-existing", "Non-existing", "Non-existing",
                "Desc", "Desc", "Desc", false, false, false, false);

        // Act
        fridgeStockModel.removeProduct(nonExistingProduct);

        // Assert
        assertEquals(1, fridgeStockModel.getFridgeProducts().size());
        assertTrue(fridgeStockModel.getFridgeProducts().contains(product1));
    }

    @Test
    void testRemoveProduct_NullProduct() {
        // Arrange
        fridgeStockModel.addProduct(product1);
        assertEquals(1, fridgeStockModel.getFridgeProducts().size());

        // Act
        fridgeStockModel.removeProduct(null);

        // Assert
        assertEquals(1, fridgeStockModel.getFridgeProducts().size());
        assertTrue(fridgeStockModel.getFridgeProducts().contains(product1));
    }

    @Test
    void testGetProducts_WithDefaultProducts() {
        // Arrange
        Product defaultProduct1 = new Product("def1", "Default 1", "Default 1", "Default 1",
                "Desc", "Desc", "Desc", true, false, false, false);
        Product defaultProduct2 = new Product("def2", "Default 2", "Default 2", "Default 2",
                "Desc", "Desc", "Desc", true, false, false, false);

        List<Product> defaultProducts = List.of(defaultProduct1, defaultProduct2);
        FridgeStockModel modelWithDefaults = new FridgeStockModel(defaultProducts);

        modelWithDefaults.addProduct(product1);

        // Act
        List<Product> allProducts = modelWithDefaults.getProducts();

        // Assert
        assertEquals(3, allProducts.size());
        assertTrue(allProducts.contains(product1));
        assertTrue(allProducts.contains(defaultProduct1));
        assertTrue(allProducts.contains(defaultProduct2));
    }

    @Test
    void testGetProducts_NoDefaultProducts() {
        // Arrange - create a model with empty default products list
        FridgeStockModel model = new FridgeStockModel(new ArrayList<>());
        model.addProduct(product1);
        model.addProduct(product2);

        // Act
        List<Product> allProducts = model.getProducts();

        // Assert
        assertEquals(2, allProducts.size());
        assertTrue(allProducts.contains(product1));
        assertTrue(allProducts.contains(product2));
    }

    @Test
    void testGetDefaultProducts() {
        // Arrange
        Product defaultProduct1 = new Product("def1", "Default 1", "Default 1", "Default 1",
                "Desc", "Desc", "Desc", true, false, false, false);
        Product defaultProduct2 = new Product("def2", "Default 2", "Default 2", "Default 2",
                "Desc", "Desc", "Desc", true, false, false, false);

        List<Product> defaultProducts = List.of(defaultProduct1, defaultProduct2);
        FridgeStockModel modelWithDefaults = new FridgeStockModel(defaultProducts);

        // Add a non-default product
        modelWithDefaults.addProduct(product1);

        // Act
        List<Product> retrievedDefaultProducts = modelWithDefaults.getDefaultProducts();

        // Assert
        assertEquals(2, retrievedDefaultProducts.size());
        assertTrue(retrievedDefaultProducts.contains(defaultProduct1));
        assertTrue(retrievedDefaultProducts.contains(defaultProduct2));
        assertFalse(retrievedDefaultProducts.contains(product1));
    }

    @Test
    void testGetDefaultProducts_EmptyDefaults() {
        // Arrange - create a model with empty default products list
        FridgeStockModel model = new FridgeStockModel(new ArrayList<>());

        // Act
        List<Product> retrievedDefaultProducts = model.getDefaultProducts();

        // Assert
        assertTrue(retrievedDefaultProducts.isEmpty());
    }

    @Test
    void testPropertyChangeListener() {
        // Arrange
        PropertyChangeListenerMock listener = new PropertyChangeListenerMock();
        fridgeStockModel.addPropertyChangeListener(listener);

        // Act
        fridgeStockModel.addProduct(product1);
        PropertyChangeEvent eventAfterAdd = listener.getLastEvent();

        fridgeStockModel.removePropertyChangeListener(listener);
        listener.reset();
        fridgeStockModel.addProduct(product2);
        PropertyChangeEvent eventAfterRemove = listener.getLastEvent();

        // Assert
        assertNotNull(eventAfterAdd);
        assertEquals(FridgeStockModel.PROP_FRIDGE_CONTENTS, eventAfterAdd.getPropertyName());
        assertNull(eventAfterRemove); // Listener removed
    }

    // Mock class for PropertyChangeListener
    @Getter
    private static final class PropertyChangeListenerMock implements PropertyChangeListener {
        private PropertyChangeEvent lastEvent;

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            this.lastEvent = evt;
        }

        public void reset() {
            this.lastEvent = null;
        }
    }
}

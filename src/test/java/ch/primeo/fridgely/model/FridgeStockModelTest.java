package ch.primeo.fridgely.model;

import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FridgeStockModelTest {
/*
    private FridgeStockModel fridgeStockModel;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        fridgeStockModel = new FridgeStockModel();
        product1 = new Product("123456789", "Apple", "Apfel", "Pomme", "Fresh apple", "Frischer Apfel", "Pomme fraîche",
                true, true, false);
        product2 = new Product("987654321", "Banana", "Banane", "Banane", "Fresh banana", "Frische Banane",
                "Banane fraîche", false, false, true);
    }

    @Test
    void testAddProduct() {
        // Arrange
        Product product = new Product("123456789", "Apple", "Apfel", "Pomme", "Fresh apple", "Frischer Apfel",
                "Pomme fraîche", true, true, false);

        // Act
        boolean firstAddResult = fridgeStockModel.addProduct(product);
        boolean secondAddResult = fridgeStockModel.addProduct(product);
        int productCount = fridgeStockModel.getProductCount();

        // Assert
        assertTrue(firstAddResult);
        assertFalse(secondAddResult); // Duplicate
        assertEquals(1, productCount);
    }

    @Test
    void testAddNullProduct() {
        // Act
        boolean result = fridgeStockModel.addProduct(null);
        int productCount = fridgeStockModel.getProductCount();

        // Assert
        assertFalse(result);
        assertEquals(0, productCount);
    }

    @Test
    void testRemoveProduct() {
        // Arrange
        fridgeStockModel.addProduct(product1);

        // Act
        boolean firstRemoveResult = fridgeStockModel.removeProduct(product1);
        boolean secondRemoveResult = fridgeStockModel.removeProduct(product1);
        int productCount = fridgeStockModel.getProductCount();

        // Assert
        assertTrue(firstRemoveResult);
        assertFalse(secondRemoveResult); // Not in stock
        assertEquals(0, productCount);
    }

    @Test
    void testRemoveNullProduct() {
        // Arrange
        fridgeStockModel.addProduct(product1);

        // Act
        boolean result = fridgeStockModel.removeProduct(null);
        int productCount = fridgeStockModel.getProductCount();

        // Assert
        assertFalse(result);
        assertEquals(1, productCount);
    }

    @Test
    void testGetProducts() {
        // Arrange
        fridgeStockModel.addProduct(product1);
        fridgeStockModel.addProduct(product2);

        // Act
        List<Product> products = fridgeStockModel.getProducts();

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
        int productCount = fridgeStockModel.getProductCount();

        // Assert
        assertEquals(0, productCount);
    }

    @Test
    void testGetProductCount() {
        // Arrange
        fridgeStockModel.addProduct(product1);

        // Act
        int productCount = fridgeStockModel.getProductCount();

        // Assert
        assertEquals(1, productCount);
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
    }*/
}

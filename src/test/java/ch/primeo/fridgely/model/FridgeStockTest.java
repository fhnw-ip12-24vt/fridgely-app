package ch.primeo.fridgely.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the FridgeStock entity.
 */
public class FridgeStockTest {

    @Test
    void testGetAndSetBarcode() {
        // Arrange
        FridgeStock fridgeStock = new FridgeStock();
        String expectedBarcode = "123456789";

        // Act
        fridgeStock.setBarcode(expectedBarcode);
        String actualBarcode = fridgeStock.getBarcode();

        // Assert
        assertEquals(expectedBarcode, actualBarcode);
    }

    @Test
    void testConstructorWithBarcode() {
        // Arrange
        String expectedBarcode = "987654321";

        // Act
        FridgeStock fridgeStock = new FridgeStock(expectedBarcode);

        // Assert
        assertEquals(expectedBarcode, fridgeStock.getBarcode());
    }

    @Test
    void testToString() {
        // Arrange
        FridgeStock fridgeStock = new FridgeStock("123456789");

        // Act
        String actualString = fridgeStock.toString();

        // Assert
        assertEquals("FridgeStock{barcode='123456789'}", actualString);
    }

    @Test
    void testEquals_SameObject() {
        // Arrange
        FridgeStock fridgeStock = new FridgeStock("123456789");
        // Act & Assert
        assertEquals(fridgeStock, new FridgeStock("123456789"));
    }

    @Test
    void testEquals_DifferentObjectsSameBarcode() {
        // Arrange
        FridgeStock fridgeStock1 = new FridgeStock("123456789");
        FridgeStock fridgeStock2 = new FridgeStock("123456789");

        // Act & Assert
        assertEquals(fridgeStock1, fridgeStock2);
    }

    @Test
    void testEquals_DifferentObjectsDifferentBarcode() {
        // Arrange
        FridgeStock fridgeStock1 = new FridgeStock("123456789");
        FridgeStock fridgeStock2 = new FridgeStock("987654321");

        // Act & Assert
        assertNotEquals(fridgeStock1, fridgeStock2);
    }

    @Test
    void testHashCode_SameBarcode() {
        // Arrange
        FridgeStock fridgeStock1 = new FridgeStock("123456789");
        FridgeStock fridgeStock2 = new FridgeStock("123456789");

        // Act & Assert
        assertEquals(fridgeStock1.hashCode(), fridgeStock2.hashCode());
    }

    @Test
    void testHashCode_DifferentBarcode() {
        // Arrange
        FridgeStock fridgeStock1 = new FridgeStock("123456789");
        FridgeStock fridgeStock2 = new FridgeStock("987654321");

        // Act & Assert
        assertNotEquals(fridgeStock1.hashCode(), fridgeStock2.hashCode());
    }

    @Test
    void testEquals() {
        // Create identical FridgeStock objects
        FridgeStock stock1 = new FridgeStock("12345");
        FridgeStock stock2 = new FridgeStock("12345");
        FridgeStock stock3 = new FridgeStock("67890");

        // Test equality with same object
        assertTrue(stock1.equals(stock1), "An object should be equal to itself");

        // Test equality with equivalent object
        assertTrue(stock1.equals(stock2), "Objects with the same barcode should be equal");
        assertTrue(stock2.equals(stock1), "Equality should be symmetric");

        // Test inequality with different barcode
        assertFalse(stock1.equals(stock3), "Objects with different barcodes should not be equal");

        // Test inequality with null
        assertFalse(stock1.equals(null), "Object should not be equal to null");

        // Test inequality with different class
        assertFalse(stock1.equals("12345"), "Object should not be equal to an instance of a different class");
    }

    @Test
    void testHashCode() {
        FridgeStock stock1 = new FridgeStock("12345");
        FridgeStock stock2 = new FridgeStock("12345");

        // Equal objects must have equal hash codes
        assertEquals(stock1.hashCode(), stock2.hashCode(), "Equal objects must have equal hash codes");
    }
}
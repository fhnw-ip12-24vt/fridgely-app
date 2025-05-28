package ch.primeo.fridgely.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
        assertEquals(new FridgeStock("123456789"), fridgeStock);
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
    public void testEqualsMethod() {
        // Same object
        FridgeStock stock1 = new FridgeStock("12345");
        assertEquals(stock1, stock1, "An object should be equal to itself");

        // Equal barcodes
        FridgeStock stock2 = new FridgeStock("12345");
        assertEquals(stock1, stock2, "Objects with same barcode should be equal");

        // Different barcodes
        FridgeStock stock3 = new FridgeStock("99999");
        assertNotEquals(stock1, stock3, "Objects with different barcodes should not be equal");

        // Null comparison
        assertNotEquals(null, stock1, "Object should not be equal to null");

        // Different class
        Object notStock = new Object();  // or any non-FridgeStock class
        assertNotEquals(stock1, notStock, "Object should not be equal to an instance of a different class");
    }

    @Test
    void testHashCode() {
        FridgeStock stock1 = new FridgeStock("12345");
        FridgeStock stock2 = new FridgeStock("12345");

        // Equal objects must have equal hash codes
        assertEquals(stock1.hashCode(), stock2.hashCode(), "Equal objects must have equal hash codes");
    }
}

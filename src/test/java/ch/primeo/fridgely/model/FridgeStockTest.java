package ch.primeo.fridgely.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FridgeStockTest {

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
}
package ch.primeo.fridgely.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testGetAndSetBarcode() {
        // Arrange
        Product product = new Product();
        String expectedBarcode = "123456789";

        // Act
        product.setBarcode(expectedBarcode);
        String actualBarcode = product.getBarcode();

        // Assert
        assertEquals(expectedBarcode, actualBarcode);
    }

    @Test
    void testGetAndSetName() {
        // Arrange
        Product product = new Product();
        String expectedName = "Apple";

        // Act
        product.setName(expectedName);
        String actualName = product.getName();

        // Assert
        assertEquals(expectedName, actualName);
    }

    @Test
    void testGetNameWithLocalization() {
        // Arrange
        Product product = new Product();
        product.setName("Apple");
        product.setNameDE("Apfel");
        product.setNameFR("Pomme");

        // Act & Assert
        assertEquals("Apple", product.getName("en")); // Default
        assertEquals("Apfel", product.getName("de")); // German
        assertEquals("Pomme", product.getName("fr")); // French
        assertEquals("Apple", product.getName("es")); // Fallback
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        Product product1 = new Product();
        product1.setBarcode("123456789");

        Product product2 = new Product();
        product2.setBarcode("123456789");

        Product product3 = new Product();
        product3.setBarcode("987654321");

        // Act & Assert
        assertEquals(product1, product2); // Same barcode
        assertNotEquals(product1, product3); // Different barcode
        assertEquals(product1.hashCode(), product2.hashCode()); // Same hashcode
        assertNotEquals(product1.hashCode(), product3.hashCode()); // Different hashcode
    }

    @Test
    void testGetProductImagePath() {
        // Arrange
        Product product = new Product();
        String barcode = "123456789";
        product.setBarcode(barcode);

        // Act
        String imagePath = product.getProductImagePath();

        // Assert
        assertEquals("/ch/primeo/fridgely/productimages/123456789.png", imagePath);
    }

    @Test
    void testIsAndSetBio() {
        // Arrange
        Product product = new Product();

        // Act
        product.setBio(true);

        // Assert
        assertTrue(product.isBio());
    }

    @Test
    void testIsAndSetLocal() {
        // Arrange
        Product product = new Product();

        // Act
        product.setLocal(true);

        // Assert
        assertTrue(product.isLocal());
    }

    @Test
    void testLanguageKeyGeneration(){
        Product product = new Product("", "", "", "", "something random", "", "", false, true, false, true);
        String key = product.getExplanationKey();
        String validKey = "product.bio.foreign.lowCO2";
        assertEquals(validKey, key);
        product.setDescription("meat");
        product.setLocal(true);
        key = product.getExplanationKey();
        validKey = "product.bio.local.meat";
        assertEquals(validKey, key);
        product.setDescription("water_intensive");
        product.setBio(false);
        key = product.getExplanationKey();
        validKey = "product.non_bio.local.water_intensive";
        assertEquals(validKey, key);
        product.setDescription("coffee_belt");
        key = product.getExplanationKey();
        validKey = "product.non_bio.local.coffee_belt";
        assertEquals(validKey, key);
    }
}
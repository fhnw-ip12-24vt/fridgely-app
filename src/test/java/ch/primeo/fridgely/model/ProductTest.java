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
    void testConstructorWithAllParameters() {
        // Arrange & Act
        String barcode = "123456789";
        String name = "Test Product";
        String nameDE = "Test Produkt";
        String nameFR = "Produit Test";
        String description = "A test product";
        String descriptionDE = "Ein Testprodukt";
        String descriptionFR = "Un produit test";
        boolean isDefaultProduct = true;
        boolean bio = true;
        boolean local = true;
        boolean isLowCO2 = false;

        Product product = new Product(barcode, name, nameDE, nameFR, description, descriptionDE, descriptionFR,
                isDefaultProduct, bio, local, isLowCO2);

        // Assert
        assertEquals(barcode, product.getBarcode());
        assertEquals(name, product.getName());
        assertEquals(nameDE, product.getNameDE());
        assertEquals(nameFR, product.getNameFR());
        assertEquals(description, product.getDescription());
        assertEquals(descriptionDE, product.getDescriptionDE());
        assertEquals(descriptionFR, product.getDescriptionFR());
        assertTrue(product.isDefaultProduct());
        assertTrue(product.isBio());
        assertTrue(product.isLocal());
        assertFalse(product.isLowCo2()); // Added assertion for isLowCO2 based on constructor params
    }

    @Test
    void testGetAndSetNameDE() {
        // Arrange
        Product product = new Product();
        String expectedName = "Apfel";

        // Act
        product.setNameDE(expectedName);
        String actualName = product.getNameDE();

        // Assert
        assertEquals(expectedName, actualName);
    }

    @Test
    void testGetAndSetNameFR() {
        // Arrange
        Product product = new Product();
        String expectedName = "Pomme";

        // Act
        product.setNameFR(expectedName);
        String actualName = product.getNameFR();

        // Assert
        assertEquals(expectedName, actualName);
    }

    @Test
    void testGetAndSetDescription() {
        // Arrange
        Product product = new Product();
        String expectedDescription = "A fresh apple";

        // Act
        product.setDescription(expectedDescription);
        String actualDescription = product.getDescription();

        // Assert
        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    void testGetAndSetDescriptionDE() {
        // Arrange
        Product product = new Product();
        String expectedDescription = "Ein frischer Apfel";

        // Act
        product.setDescriptionDE(expectedDescription);
        String actualDescription = product.getDescriptionDE();

        // Assert
        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    void testGetAndSetDescriptionFR() {
        // Arrange
        Product product = new Product();
        String expectedDescription = "Une pomme fraîche";

        // Act
        product.setDescriptionFR(expectedDescription);
        String actualDescription = product.getDescriptionFR();

        // Assert
        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    void testGetAndSetDefaultProduct() {
        // Arrange
        Product product = new Product();

        // Act
        product.setDefaultProduct(true);

        // Assert
        assertTrue(product.isDefaultProduct());
    }

    @Test
    void testDefaultIsDefaultProductValue() {
        // Arrange
        Product product = new Product();

        // Act & Assert
        assertFalse(product.isDefaultProduct());
    }

    @Test
    void testGetDescriptionWithLocalization() {
        // Arrange
        Product product = new Product();
        product.setDescription("Fresh Apple");
        product.setDescriptionDE("Frischer Apfel");
        product.setDescriptionFR("Pomme Fraîche");

        // Act & Assert
        assertEquals("Fresh Apple", product.getDescription());
        assertEquals("Frischer Apfel", product.getDescriptionDE());
        assertEquals("Pomme Fraîche", product.getDescriptionFR());
    }

    @Test
    void testGetNameWithEmptyLanguage() {
        // Arrange
        Product product = new Product();
        product.setName("Apple");
        product.setNameDE("Apfel");
        product.setNameFR("Pomme");

        // Act & Assert
        assertEquals("Apple", product.getName(""));
    }

    @Test
    void testGetNameWithUppercaseLanguageCode() {
        // Arrange
        Product product = new Product();
        product.setName("Apple");
        product.setNameDE("Apfel");
        product.setNameFR("Pomme");

        // Act & Assert
        assertEquals("Apfel", product.getName("DE"));
        assertEquals("Pomme", product.getName("FR"));
    }

    @Test
    void testGetNameWithNullLocalizedName() {
        // Arrange
        Product product = new Product();
        product.setName("Apple");
        product.setNameDE(null);
        product.setNameFR(null);

        // Act & Assert
        assertEquals("Apple", product.getName("de"));
        assertEquals("Apple", product.getName("fr"));
    }

    @Test
    void testEqualsWithNull() {
        // Arrange
        Product product = new Product();
        product.setBarcode("123456789");

        // Act & Assert
        // assertNotEquals(null, product); // Original line
        assertFalse(product.equals(null)); // Ensures product.equals(null) is called
    }

    @Test
    void testEqualsWithDifferentClass() {
        // Arrange
        Product product = new Product();
        product.setBarcode("123456789");
        String notAProduct = "123456789";

        // Act & Assert
        // assertNotEquals(notAProduct, product); // Original line, calls String.equals(Product)
        assertFalse(product.equals(notAProduct)); // Ensures product.equals(String) is called
    }

    @Test
    void testGetProductImagePathWithNullBarcode() {
        // Arrange
        Product product = new Product();
        product.setBarcode(null);

        // Act
        String imagePath = product.getProductImagePath();

        // Assert
        assertEquals("/ch/primeo/fridgely/productimages/notfound.png", imagePath);
    }

    @Test
    void testGetNameWithNullLanguage() {
        // Arrange
        Product product = new Product();
        product.setName("Apple");
        product.setNameDE("Apfel");
        product.setNameFR("Pomme");

        // Act & Assert
        assertEquals("Apple", product.getName(null));
    }

    @Test
    void testDefaultBioAndLocalValues() {
        // Arrange
        Product product = new Product();

        // Act & Assert
        assertFalse(product.isBio());
        assertFalse(product.isLocal());
    }

    @Test
    void testEqualsWithNullBarcodes() {
        // Arrange
        Product product1 = new Product();
        product1.setBarcode(null);

        Product product2 = new Product();
        product2.setBarcode(null);

        // Act & Assert
        assertNotEquals(product1, product2); // Both barcodes are null
    }

    @Test
    void testEqualsWithOtherBarcodeNull() {
        // Arrange
        Product product1 = new Product();
        product1.setBarcode("123456789");

        Product product2 = new Product();
        product2.setBarcode(null);

        // Act & Assert
        assertNotEquals(product1, product2); // This barcode not null, other barcode null
    }

    @Test
    void testEqualsWithThisBarcodeNull() {
        // Arrange
        Product product1 = new Product();
        product1.setBarcode(null);

        Product product2 = new Product();
        product2.setBarcode("123456789");

        // Act & Assert
        assertNotEquals(product1, product2); // This barcode null, other barcode not null
    }

    @Test
    void testHashCodeWithNullBarcode() {
        // Arrange
        Product product = new Product();
        product.setBarcode(null);

        // Act
        int hashCode = product.hashCode();

        // Assert
        assertEquals(0, hashCode); // Barcode is null, hashCode should be 0
    }

    @Test
    void testGetExplanationKey_BioLocal() {
        // Arrange
        Product product = new Product();
        product.setBio(true);
        product.setLocal(true);
        product.setLowCo2(false);
        product.setDescription("default");

        // Act
        String key = product.getExplanationKey();

        // Assert
        assertEquals("product.bio.local.highCO2", key);
    }

    @Test
    void testGetExplanationKey_NonBioForeign() {
        // Arrange
        Product product = new Product();
        product.setBio(false);
        product.setLocal(false);
        product.setLowCo2(true);
        product.setDescription("default");

        // Act
        String key = product.getExplanationKey();

        // Assert
        assertEquals("product.non_bio.foreign.lowCO2", key);
    }

    @Test
    void testGetExplanationKey_WithSpecificDescription() {
        // Arrange
        Product product = new Product();
        product.setBio(true);
        product.setLocal(false);
        product.setDescription("meat");

        // Act
        String key = product.getExplanationKey();

        // Assert
        assertEquals("product.bio.foreign.meat", key);
    }

    @Test
    void testGetExplanationKey_WaterIntensive() {
        // Arrange
        Product product = new Product();
        product.setBio(false);
        product.setLocal(true);
        product.setDescription("water_intensive");

        // Act
        String key = product.getExplanationKey();

        // Assert
        assertEquals("product.non_bio.local.water_intensive", key);
    }

    @Test
    void testGetExplanationKey_CoffeeBelt() {
        // Arrange
        Product product = new Product();
        product.setBio(true);
        product.setLocal(false);
        product.setDescription("coffee_belt");

        // Act
        String key = product.getExplanationKey();

        // Assert
        assertEquals("product.bio.foreign.coffee_belt", key);
    }

    @Test
    void testIsNotDefaultProduct_True() {
        // Arrange
        Product product = new Product();
        product.setDefaultProduct(false);

        // Act & Assert
        assertTrue(product.isNotDefaultProduct());
    }

    @Test
    void testIsNotDefaultProduct_False() {
        // Arrange
        Product product = new Product();
        product.setDefaultProduct(true);

        // Act & Assert
        assertFalse(product.isNotDefaultProduct());
    }

    @Test
    void testIsLowCo2_True() {
        // Arrange
        Product product = new Product();
        product.setLowCo2(true);

        // Act & Assert
        assertTrue(product.isLowCo2());
    }

    @Test
    void testIsLowCo2_False() {
        // Arrange
        Product product = new Product();
        product.setLowCo2(false);

        // Act & Assert
        assertFalse(product.isLowCo2());
    }

    @Test
    void testSetLowCo2() {
        // Arrange
        Product product = new Product();

        // Act
        product.setLowCo2(true);

        // Assert
        assertTrue(product.isLowCo2());

        // Act again
        product.setLowCo2(false);

        // Assert again
        assertFalse(product.isLowCo2());
    }

    // Nested subclass to trigger getClass() mismatch in equals()
    static class ExtendedProduct extends Product {
        ExtendedProduct(String code) {
            super(code, null, null, null, null, null, null, false, false, false, false);
        }
    }

    @Test
    void testEqualsWithDifferentSubclass() {
        Product product = new Product();
        product.setBarcode("123456789");

        Product extendedProduct = new ExtendedProduct("123456789");
        assertNotEquals(product, extendedProduct);
        assertNotEquals(extendedProduct, product);
    }

    @Test
    void testEqualsWithNullObject() {
        Product product = new Product();
        product.setBarcode("123456789");

        // assertNotEquals(null, product); // Original line
        assertFalse(product.equals(null)); // Ensures product.equals(null) is called
    }
}

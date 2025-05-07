package ch.primeo.fridgely.service;

import ch.primeo.fridgely.model.FridgeStock;
import ch.primeo.fridgely.model.QFridgeStock;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FridgeStockRepositoryTest {


   /* @Mock
    private FridgeStockJpaRepository fridgeStockJpaRepositoryMock;

    @Mock
    private EntityManager entityManagerMock;

    @Mock
    private JPAQueryFactory queryFactoryMock;

    private FridgeStockRepository fridgeStockRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fridgeStockRepository = new FridgeStockRepository(fridgeStockJpaRepositoryMock, entityManagerMock);
    }

    @Test
    void testAddProductsToStock_Success() {
        // Arrange
        List<String> barcodes = List.of("123", "456");
        when(fridgeStockJpaRepositoryMock.saveAll(any())).thenReturn(null);

        // Act
        boolean result = fridgeStockRepository.addProductsToStock(barcodes);

        // Assert
        assertTrue(result);
        verify(fridgeStockJpaRepositoryMock, times(1)).saveAll(any());
    }

    @Test
    void testAddProductsToStock_EmptyList() {
        // Arrange
        List<String> barcodes = List.of();

        // Act
        boolean result = fridgeStockRepository.addProductsToStock(barcodes);

        // Assert
        assertTrue(result);
        verify(fridgeStockJpaRepositoryMock, never()).saveAll(any());
    }

    @Test
    void testAddProductToStock_Success() {
        // Arrange
        String barcode = "123";
        doReturn(true).when(fridgeStockRepository).addProductsToStock(List.of(barcode));

        // Act
        boolean result = fridgeStockRepository.addProductToStock(barcode);

        // Assert
        assertTrue(result);
    }

    @Test
    void testAddProductToStock_InvalidBarcode() {
        // Arrange
        String barcode = "";

        // Act
        boolean result = fridgeStockRepository.addProductToStock(barcode);

        // Assert
        assertFalse(result);
    }

//    @Test
//    void testRemoveProductsFromStock_Success() {
//        // Arrange
//        List<String> barcodes = List.of("123", "456");
//        when(queryFactoryMock.delete(QFridgeStock.fridgeStock)).thenReturn(queryFactoryMock);
//        when(queryFactoryMock.where(any())).thenReturn(queryFactoryMock);
//
//        // Act
//        boolean result = fridgeStockRepository.removeProductsFromStock(barcodes);
//
//        // Assert
//        assertTrue(result);
//        verify(queryFactoryMock, times(1)).delete(QFridgeStock.fridgeStock);
//    }

    @Test
    void testRemoveProductsFromStock_EmptyList() {
        // Arrange
        List<String> barcodes = List.of();

        // Act
        boolean result = fridgeStockRepository.removeProductsFromStock(barcodes);

        // Assert
        assertTrue(result);
        verify(queryFactoryMock, never()).delete(any());
    }

    @Test
    void testRemoveProductFromStock_Success() {
        // Arrange
        String barcode = "123";
        doReturn(true).when(fridgeStockRepository).removeProductsFromStock(List.of(barcode));

        // Act
        boolean result = fridgeStockRepository.removeProductFromStock(barcode);

        // Assert
        assertTrue(result);
    }

    @Test
    void testRemoveProductFromStock_InvalidBarcode() {
        // Arrange
        String barcode = "";

        // Act
        boolean result = fridgeStockRepository.removeProductFromStock(barcode);

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsProductInStock_ProductExists() {
        // Arrange
        String barcode = "123";
        when(fridgeStockJpaRepositoryMock.existsById(barcode)).thenReturn(true);

        // Act
        boolean result = fridgeStockRepository.isProductInStock(barcode);

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsProductInStock_ProductDoesNotExist() {
        // Arrange
        String barcode = "123";
        when(fridgeStockJpaRepositoryMock.existsById(barcode)).thenReturn(false);

        // Act
        boolean result = fridgeStockRepository.isProductInStock(barcode);

        // Assert
        assertFalse(result);
    }

//    @Test
//    void testGetAllBarcodesInStock() {
//        // Arrange
//        List<String> barcodes = List.of("123", "456");
//        when(queryFactoryMock.select(QFridgeStock.fridgeStock.barcode)).thenReturn(queryFactoryMock);
//        when(queryFactoryMock.from(QFridgeStock.fridgeStock)).thenReturn(queryFactoryMock);
//        when(queryFactoryMock.fetch()).thenReturn(barcodes);
//
//        // Act
//        List<String> result = fridgeStockRepository.getAllBarcodesInStock();
//
//        // Assert
//        assertEquals(barcodes, result);
//    }

    @Test
    void testGetAllBarcodesInStockAsSet() {
        // Arrange
        Set<String> barcodes = Set.of("123", "456");
        doReturn(List.copyOf(barcodes)).when(fridgeStockRepository).getAllBarcodesInStock();

        // Act
        Set<String> result = fridgeStockRepository.getAllBarcodesInStockAsSet();

        // Assert
        assertEquals(barcodes, result);
    }

//    @Test
//    void testClearStock_Success() {
//        // Arrange
//        when(queryFactoryMock.delete(QFridgeStock.fridgeStock)).thenReturn(queryFactoryMock);
//
//        // Act
//        boolean result = fridgeStockRepository.clearStock();
//
//        // Assert
//        assertTrue(result);
//        verify(queryFactoryMock, times(1)).delete(QFridgeStock.fridgeStock);
//    }*/
}
package ch.primeo.fridgely.service;

import ch.primeo.fridgely.model.QFridgeStock;
import com.querydsl.jpa.impl.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FridgeStockRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private JPAQuery<String> jpaQuery; // Mock for the query object itself

    // FridgeStockRepository instance will be created within tests where needed,
    // especially when using MockedConstruction.

   @Test
    void constructor_initializesJpaQueryFactory() {
        try (MockedConstruction<JPAQueryFactory> mockedFactoryConstruction = Mockito.mockConstruction(
                JPAQueryFactory.class, (mock, context) -> {
                    // Assert that the constructor of JPAQueryFactory was called with the mocked EntityManager
                    assertEquals(1, context.arguments().size(), "JPAQueryFactory constructor should have 1 argument");
                    assertSame(entityManager, context.arguments().getFirst(),
                            "JPAQueryFactory should be constructed with the provided EntityManager");
                })) {

            FridgeStockRepository repository = new FridgeStockRepository(entityManager);
            assertNotNull(repository, "Repository instance should not be null");
            // Verify that one JPAQueryFactory was indeed constructed
            assertEquals(1, mockedFactoryConstruction.constructed().size(),
                    "One JPAQueryFactory instance should have been constructed");
        }
    }

    @Test
    void getAllBarcodesInStock_noBarcodes() {
        // Configure the chained calls on the jpaQuery mock
        when(jpaQuery.from(QFridgeStock.fridgeStock)).thenReturn(jpaQuery);
        when(jpaQuery.distinct()).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(Collections.emptyList());

        try (MockedConstruction<JPAQueryFactory> mockedFactoryConstruction = Mockito.mockConstruction(
                JPAQueryFactory.class, (constructedMockFactory, context) -> {
                    // When FridgeStockRepository's constructor creates a JPAQueryFactory,
                    // this constructedMockFactory will be used.
                    // Configure its 'select' method to return our @Mock jpaQuery.
                    when(constructedMockFactory.select(QFridgeStock.fridgeStock.barcode)).thenReturn(jpaQuery);
                })) {

            FridgeStockRepository repository = new FridgeStockRepository(entityManager);
            List<String> barcodes = repository.getAllBarcodesInStock();

            assertNotNull(barcodes, "Returned list should not be null");
            assertTrue(barcodes.isEmpty(), "Returned list should be empty");

            // Verify 'select' was called on the constructed JPAQueryFactory instance
            JPAQueryFactory factoryInstance = mockedFactoryConstruction.constructed().getFirst();
            verify(factoryInstance).select(QFridgeStock.fridgeStock.barcode);

            // Verify the rest of the chain on our @Mock jpaQuery
            verify(jpaQuery).from(QFridgeStock.fridgeStock);
            verify(jpaQuery).distinct();
            verify(jpaQuery).fetch();
        }
    }

    @Test
    void getAllBarcodesInStock_withBarcodes() {
        List<String> expectedBarcodes = Arrays.asList("123", "456", "789");

        // Configure the chained calls on the jpaQuery mock
        when(jpaQuery.from(QFridgeStock.fridgeStock)).thenReturn(jpaQuery);
        when(jpaQuery.distinct()).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(expectedBarcodes);

        try (MockedConstruction<JPAQueryFactory> mockedFactoryConstruction = Mockito.mockConstruction(
                JPAQueryFactory.class, (constructedMockFactory, context) ->
                        when(constructedMockFactory.select(QFridgeStock.fridgeStock.barcode)).thenReturn(jpaQuery))) {

            FridgeStockRepository repository = new FridgeStockRepository(entityManager);
            List<String> actualBarcodes = repository.getAllBarcodesInStock();

            assertNotNull(actualBarcodes, "Returned list should not be null");
            assertEquals(expectedBarcodes.size(), actualBarcodes.size(), "List size should match expected");
            assertEquals(expectedBarcodes, actualBarcodes, "List content should match expected");

            JPAQueryFactory factoryInstance = mockedFactoryConstruction.constructed().getFirst();
            verify(factoryInstance).select(QFridgeStock.fridgeStock.barcode);
            verify(jpaQuery).from(QFridgeStock.fridgeStock);
            verify(jpaQuery).distinct();
            verify(jpaQuery).fetch();
        }
    }

    @Test
    void getAllBarcodesInStockAsSet_emptyList() {
        // For this method, we spy on FridgeStockRepository to mock the call to getAllBarcodesInStock()
        FridgeStockRepository spiedRepository = spy(new FridgeStockRepository(entityManager));
        doReturn(Collections.emptyList()).when(spiedRepository).getAllBarcodesInStock();

        Set<String> barcodesSet = spiedRepository.getAllBarcodesInStockAsSet();

        assertNotNull(barcodesSet, "Returned set should not be null");
        assertTrue(barcodesSet.isEmpty(), "Returned set should be empty");
        verify(spiedRepository).getAllBarcodesInStock(); // Verify the mocked method was called
    }

    @Test
    void getAllBarcodesInStockAsSet_uniqueBarcodes() {
        FridgeStockRepository spiedRepository = spy(new FridgeStockRepository(entityManager));
        List<String> uniqueList = Arrays.asList("123", "456", "789");
        doReturn(uniqueList).when(spiedRepository).getAllBarcodesInStock();

        Set<String> barcodesSet = spiedRepository.getAllBarcodesInStockAsSet();

        assertNotNull(barcodesSet, "Returned set should not be null");
        assertEquals(3, barcodesSet.size(), "Set size should be 3");
        assertTrue(barcodesSet.containsAll(uniqueList), "Set should contain all unique elements");
        verify(spiedRepository).getAllBarcodesInStock();
    }

    @Test
    void getAllBarcodesInStockAsSet_duplicateBarcodes() {
        FridgeStockRepository spiedRepository = spy(new FridgeStockRepository(entityManager));
        List<String> duplicateList = Arrays.asList("123", "456", "123", "789", "456");
        Set<String> expectedSet = new HashSet<>(Arrays.asList("123", "456", "789"));
        doReturn(duplicateList).when(spiedRepository).getAllBarcodesInStock();

        Set<String> barcodesSet = spiedRepository.getAllBarcodesInStockAsSet();

        assertNotNull(barcodesSet, "Returned set should not be null");
        assertEquals(3, barcodesSet.size(), "Set size should be 3 after deduplication");
        assertEquals(expectedSet, barcodesSet, "Set content should match expected unique elements");
        verify(spiedRepository).getAllBarcodesInStock();
    }
}

package ch.primeo.fridgely;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for the FridgelyContext class.
 */
class FridgelyContextTest {

    @Mock
    private ApplicationContext mockApplicationContext;

    private FridgelyContext fridgelyContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fridgelyContext = new FridgelyContext();
        FridgelyContext.context = null;
    }

    @Test
    void setApplicationContext_shouldSetStaticContext() {
        // Act
        fridgelyContext.setApplicationContext(mockApplicationContext);

        // Prepare mock for getBean call
        when(mockApplicationContext.getBean(String.class)).thenReturn("test");

        // Assert - test the static method
        String result = FridgelyContext.getBean(String.class);
        assertEquals("test", result);
        verify(mockApplicationContext).getBean(String.class);
    }

    @Test
    void getBean_shouldCallApplicationContextGetBean() {
        // Arrange
        fridgelyContext.setApplicationContext(mockApplicationContext);
        when(mockApplicationContext.getBean(Integer.class)).thenReturn(42);

        // Act
        Integer result = FridgelyContext.getBean(Integer.class);

        // Assert
        assertEquals(42, result);
        verify(mockApplicationContext).getBean(Integer.class);
    }

    @Test
    void getBean_whenContextIsNull_shouldThrowNullPointerException() {
        // Arrange: FridgelyContext.context is null due to setUp()

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            FridgelyContext.getBean(String.class); // Call static method
        }, "getBean should throw NullPointerException when context is null");
    }
}

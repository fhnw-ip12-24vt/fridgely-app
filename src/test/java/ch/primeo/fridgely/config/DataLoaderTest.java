package ch.primeo.fridgely.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataLoaderTest {

    @Mock
    private DataSource mockDataSource;

    @InjectMocks
    private DataLoader dataLoader;

    @Captor
    private ArgumentCaptor<ResourceDatabasePopulator> populatorCaptor;

    @Test
    void run_shouldExecuteDatabasePopulatorWithCorrectDataSource() throws Exception {
        // Arrange
        Connection mockConnection = mock(Connection.class);
        lenient().when(mockDataSource.getConnection()).thenReturn(mockConnection); // Needed for populator execution

        // Use a static mock for DatabasePopulatorUtils to intercept the execute call
        try (MockedStatic<DatabasePopulatorUtils> populatorUtilsMockedStatic =
                     Mockito.mockStatic(DatabasePopulatorUtils.class)) {

            // Act
            dataLoader.run();

            // Assert
            // 1. Verify that DatabasePopulatorUtils.execute was called
            populatorUtilsMockedStatic.verify(() ->
                    DatabasePopulatorUtils.execute(
                            populatorCaptor.capture(), // Capture the populator instance
                            eq(mockDataSource)         // Ensure it's called with our mock DataSource
                    )
            );

            // 2. Verify the captured populator
            ResourceDatabasePopulator capturedPopulator = populatorCaptor.getValue();
            assertNotNull(capturedPopulator, "Captured populator should not be null.");

            // Verification of the script "ch/primeo/fridgely/sql/data.sql" being added to the
            // populator is challenging without refactoring DataLoader or using more advanced
            // mocking tools (like PowerMock to mock `new ClassPathResource(...)` or `new ResourceDatabasePopulator(...)`).
            // The DataLoader class hardcodes this script path.
            // For this test, we confirm the populator mechanism is invoked correctly.
        }
    }

    @Test
    void run_whenScriptExecutionFails_shouldPropagateException() throws Exception {
        // Arrange
        RuntimeException scriptExecutionException = new RuntimeException("Simulated script execution error");

        // Mock DatabasePopulatorUtils to throw an exception when execute is called
        // This simulates a failure during the actual script execution by the populator.
        try (MockedStatic<DatabasePopulatorUtils> populatorUtilsMockedStatic =
                     Mockito.mockStatic(DatabasePopulatorUtils.class)) {

            populatorUtilsMockedStatic.when(() ->
                    DatabasePopulatorUtils.execute(any(ResourceDatabasePopulator.class), eq(mockDataSource)))
                    .thenThrow(scriptExecutionException);

            // Act & Assert
            // DataLoader's run method will catch scriptExecutionException and re-throw it as a new RuntimeException.
            RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
                dataLoader.run();
            });

            assertEquals("Error loading initial data", thrown.getMessage());
            assertNotNull(thrown.getCause(), "RuntimeException should have a cause.");
            assertEquals(scriptExecutionException, thrown.getCause(), "The cause should be the original scriptExecutionException.");
        }
    }

    @Test
    void run_whenDataSourceConnectionFails_shouldPropagateException() throws SQLException {
        // Arrange
        SQLException sqlException = new SQLException("Simulated DataSource connection error");

        // Mock dataSource.getConnection() to throw an SQLException.
        // This simulates a failure to obtain a database connection.
        // ResourceDatabasePopulator.execute() will attempt to get a connection.
        when(mockDataSource.getConnection()).thenThrow(sqlException);
            
        // Act & Assert
        // DataLoader's run method will catch the exception (SQLException or an exception wrapping it
        // thrown by ResourceDatabasePopulator) and re-throw it as a new RuntimeException.
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            dataLoader.run();
        });

        assertEquals("Error loading initial data", thrown.getMessage());
        assertNotNull(thrown.getCause(), "RuntimeException should have a cause.");
        // Assert the chain of exceptions: RuntimeException -> UncategorizedScriptException -> CannotGetJdbcConnectionException -> SQLException
        assertEquals(org.springframework.jdbc.datasource.init.UncategorizedScriptException.class, thrown.getCause().getClass(),
                "Cause of RuntimeException should be UncategorizedScriptException.");

        Throwable uncategorizedScriptException = thrown.getCause();
        assertNotNull(uncategorizedScriptException.getCause(), "UncategorizedScriptException should have a cause.");
        assertEquals(org.springframework.jdbc.CannotGetJdbcConnectionException.class,
                uncategorizedScriptException.getCause().getClass(),
                "Cause of UncategorizedScriptException should be CannotGetJdbcConnectionException.");

        Throwable cannotGetJdbcConnectionException = uncategorizedScriptException.getCause();
        assertNotNull(cannotGetJdbcConnectionException.getCause(), "CannotGetJdbcConnectionException should have a cause.");
        assertEquals(java.sql.SQLException.class, cannotGetJdbcConnectionException.getCause().getClass(),
                "Cause of CannotGetJdbcConnectionException should be SQLException.");

        Throwable actualSqlException = cannotGetJdbcConnectionException.getCause();
        assertEquals("Simulated DataSource connection error", actualSqlException.getMessage(),
                "SQLException message should match.");
        // Verify that the cause is the specific SQLException instance we mocked
        assertEquals(sqlException, actualSqlException,
                "The final cause should be the originally mocked SQLException instance.");
    }
}
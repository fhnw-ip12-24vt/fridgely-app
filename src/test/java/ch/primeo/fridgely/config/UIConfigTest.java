package ch.primeo.fridgely.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


import static org.junit.jupiter.api.Assertions.*;

class UIConfigTest {

    @Test
    void testBackgroundColor() {
        assertEquals(new Color(248, 248, 255), UIConfig.BACKGROUND_COLOR, "BACKGROUND_COLOR should match the expected value.");
    }

    @Test
    void testActiveColor() {
        assertEquals(new Color(131, 180, 225), UIConfig.ACTIVE_COLOR, "ACTIVE_COLOR should match the expected value.");
    }

    @Test
    void testDisabledColor() {
        assertEquals(new Color(193, 193, 193), UIConfig.DISABLED_COLOR, "DISABLED_COLOR should match the expected value.");
    }

    @Test
    void testFontSize() {
        assertEquals(24, UIConfig.FONT_SIZE, "FONT_SIZE should match the expected value.");
    }

    @Test
    void testSetUIFontWithCustomFont() {
        Font testFont = new Font("Arial", Font.PLAIN, 12);
        UIConfig.setUIFont(new javax.swing.plaf.FontUIResource(testFont));

        // Verify that the font is applied to UIManager defaults
        Object defaultFont = UIManager.get("Label.font");
        assertTrue(defaultFont instanceof javax.swing.plaf.FontUIResource, "Default font should be an instance of FontUIResource.");
        assertEquals(testFont.getName(), ((Font) defaultFont).getName(), "Font name should match the test font.");
        assertEquals(testFont.getSize(), ((Font) defaultFont).getSize(), "Font size should match the test font.");
    }

  @Test
    void testSetUIFontWithResourceFont() {
        // This test ensures no exceptions are thrown when loading the font resource
        assertDoesNotThrow(() -> UIConfig.setUIFont(), "setUIFont should not throw any exceptions.");
    }

    // Helper class to capture log records
    private static class ListLogHandler extends Handler {
        private final List<LogRecord> records = new ArrayList<>();

        @Override
        public void publish(LogRecord record) {
            records.add(record);
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }

        public List<LogRecord> getRecords() {
            return records;
        }
    }

    @Test
    void testSetUIFontLogsWarningOnException() throws IOException {
        Logger uiConfigLogger = UIConfig.LOGGER; // Or Logger.getLogger(UIConfig.class.getName())
        ListLogHandler listLogHandler = new ListLogHandler();

        // Backup original handlers and settings
        Handler[] originalHandlers = uiConfigLogger.getHandlers();
        for (Handler handler : originalHandlers) {
            uiConfigLogger.removeHandler(handler);
        }
        boolean originalUseParentHandlers = uiConfigLogger.getUseParentHandlers();
        uiConfigLogger.setUseParentHandlers(false); // Avoid console output during test from parent
        uiConfigLogger.addHandler(listLogHandler);

        ResourceLoader mockResourceLoader = Mockito.mock(ResourceLoader.class);
        Resource mockResource = Mockito.mock(Resource.class);
        String expectedExceptionMessage = "Simulated IOException for testing font loading";

        Mockito.when(mockResourceLoader.getResource(Mockito.anyString())).thenReturn(mockResource);
        Mockito.when(mockResource.getInputStream()).thenThrow(new IOException(expectedExceptionMessage));

        try {
            UIConfig.setUIFont(mockResourceLoader); // Call the refactored method with the mock loader

            List<LogRecord> loggedRecords = listLogHandler.getRecords();
            assertEquals(1, loggedRecords.size(), "Expected one log record.");

            LogRecord logRecord = loggedRecords.get(0);
            assertEquals(Level.WARNING, logRecord.getLevel(), "Log level should be WARNING.");
            assertEquals(expectedExceptionMessage, logRecord.getMessage(), "Logged message should match the exception message.");
        } finally {
            // Restore original logger state
            uiConfigLogger.removeHandler(listLogHandler);
            for (Handler handler : originalHandlers) {
                uiConfigLogger.addHandler(handler);
            }
            uiConfigLogger.setUseParentHandlers(originalUseParentHandlers);
        }
    }

    @Test
    void testUtilityClassConstructor() {
        // Ensure the constructor is inaccessible
        assertThrows(UnsupportedOperationException.class, () -> {
            new UIConfig();
        }, "UIConfig constructor should throw UnsupportedOperationException.");
    }
}

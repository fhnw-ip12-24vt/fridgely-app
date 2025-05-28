package ch.primeo.fridgely.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.core.io.*;

import javax.swing.*;
import javax.swing.plaf.*;
import java.awt.*;
import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UIConfigTest {

    private UIConfig uiConfig;
    private ResourceLoader mockResourceLoader;
    private Resource mockResource;
    private ListAppender<ILoggingEvent> listAppender;
    private Logger uiConfigLogger;

    @BeforeEach
    void setUp() {
        mockResourceLoader = Mockito.mock(ResourceLoader.class);
        mockResource = Mockito.mock(Resource.class);
        uiConfig = new UIConfig(mockResourceLoader);

        // Setup Logback ListAppender
        uiConfigLogger = (Logger) org.slf4j.LoggerFactory.getLogger(UIConfig.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        uiConfigLogger.addAppender(listAppender);
    }

    @AfterEach
    void tearDown() {
        // Detach and stop the appender to avoid interference between tests and memory leaks
        if (uiConfigLogger != null && listAppender != null) {
            uiConfigLogger.detachAppender(listAppender);
            listAppender.stop();
        }
    }

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
        ResourceLoader mockResourceLoader = Mockito.mock(ResourceLoader.class);
        UIConfig uiConfig = new UIConfig(mockResourceLoader);
        Font testFont = new Font("Arial", Font.PLAIN, 12);
        uiConfig.setUIFont(new javax.swing.plaf.FontUIResource(testFont));

        // Verify that the font is applied to UIManager defaults
        Object defaultFont = UIManager.get("Label.font");
        assertInstanceOf(FontUIResource.class, defaultFont, "Default font should be an instance of FontUIResource.");
        assertEquals(testFont.getName(), ((Font) defaultFont).getName(), "Font name should match the test font.");
        assertEquals(testFont.getSize(), ((Font) defaultFont).getSize(), "Font size should match the test font.");
    }

    @Test
    void testSetUIFontWithResourceFont() throws IOException {
        ResourceLoader mockResourceLoader = Mockito.mock(ResourceLoader.class);
        Resource mockResource = Mockito.mock(Resource.class);
        // Use a non-null InputStream; an empty one might cause FontFormatException,
        // which is caught by UIConfig's generic Exception handler.
        InputStream mockInputStream = new ByteArrayInputStream(new byte[0]);

        Mockito.when(mockResourceLoader.getResource("classpath:ch/primeo/fridgely/fonts/bangers_regular.ttf")).thenReturn(mockResource);
        Mockito.when(mockResource.getInputStream()).thenReturn(mockInputStream);

        UIConfig uiConfig = new UIConfig(mockResourceLoader);
        assertDoesNotThrow(() -> uiConfig.setUIFont(), "setUIFont should not throw any exceptions.");

        // Verify that the mocked resource loader and resource were used
        Mockito.verify(mockResourceLoader).getResource("classpath:ch/primeo/fridgely/fonts/bangers_regular.ttf");
        Mockito.verify(mockResource).getInputStream();
    }

    @Test
    void testSetUIFontLogsWarningOnException() throws IOException {
        String expectedExceptionMessage = "Simulated IOException for testing font loading";

        Mockito.when(mockResourceLoader.getResource(Mockito.anyString())).thenReturn(mockResource);
        Mockito.when(mockResource.getInputStream()).thenThrow(new IOException(expectedExceptionMessage));

        uiConfig.setUIFont(); // Call the instance method

        List<ILoggingEvent> logsList = listAppender.list;
        assertEquals(1, logsList.size(), "Expected one log record.");

        ILoggingEvent logEvent = logsList.getFirst();
        assertEquals(Level.WARN, logEvent.getLevel(), "Log level should be WARNING.");
        assertTrue(logEvent.getFormattedMessage().contains(expectedExceptionMessage),
                   "Logged message should contain the exception message. Logged: " + logEvent.getFormattedMessage());
    }

    @Test
    void testSetUIFontLoadsAndAppliesTrueTypeFont() {
        // Use the real classpath loader so we load src/main/resources/ch/primeo/fridgely/fonts/bangers_regular.ttf
        ResourceLoader realLoader = new DefaultResourceLoader();
        UIConfig uiConfig = new UIConfig(realLoader);

        // should not blow up
        assertDoesNotThrow(() -> uiConfig.setUIFont(),
          "setUIFont() loading the real TTF should not throw");

        // pick a known key that was originally a FontUIResource
        Object labelFont = UIManager.get("Label.font");
        assertNotNull(labelFont);
        assertInstanceOf(FontUIResource.class, labelFont, "After loading, Label.font must be a FontUIResource");

        Font f = (Font) labelFont;
        assertEquals(UIConfig.FONT_SIZE, f.getSize(),
          "Derived font must have size = FONT_SIZE");
    }
}

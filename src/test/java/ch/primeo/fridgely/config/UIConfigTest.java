package ch.primeo.fridgely.config;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

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

    @Test
    void testUtilityClassConstructor() {
        // Ensure the constructor is inaccessible
        assertThrows(UnsupportedOperationException.class, () -> {
            new UIConfig();
        }, "UIConfig constructor should throw UnsupportedOperationException.");
    }
}

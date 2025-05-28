package ch.primeo.fridgely.config;

import org.springframework.context.annotation.*;
import org.springframework.core.io.*;
import org.springframework.stereotype.*;

import javax.annotation.PostConstruct;
import javax.swing.UIManager;
import javax.swing.plaf.*;
import java.awt.Color;
import java.awt.Font;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.logging.Logger;

@Component
@Scope("singleton")
public class UIConfig {

    /**
     * Background color for the game UI.
     */
    public static final Color BACKGROUND_COLOR = new Color(248, 248, 255);

    /**
     * Color for the active button in the game UI.
     */
    public static final Color ACTIVE_COLOR = new Color(131, 180, 225);

    /**
     * Color for the inactive button in the game UI.
     */
    public static final Color DISABLED_COLOR = new Color(193, 193, 193);

    static final Logger LOGGER = Logger.getLogger(UIConfig.class.getName());
    /**
     * Default number of rounds in a game.
     */
    public static final int FONT_SIZE = 24;

    private final ResourceLoader resourceLoader;

    /**
     * Constructor for UIConfig.
     *
     * @param resourceLoader the resource loader to use for loading resources
     */
    public UIConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setUIFont(FontUIResource f) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    // Refactored method to accept ResourceLoader
    @PostConstruct
    public void setUIFont() {
        LOGGER.info("UIConfig.setUIFont() called on thread: " + Thread.currentThread().getName());
        try {
            Resource resource = resourceLoader.getResource("classpath:ch/primeo/fridgely/fonts/bangers_regular.ttf");
            InputStream fontStream = resource.getInputStream();
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.PLAIN, FONT_SIZE);
            LOGGER.info("Custom font created: " + font.toString());
            setUIFont(new FontUIResource(font));
            LOGGER.info("UIConfig.setUIFont() finished on thread: " + Thread.currentThread().getName());
        } catch (Exception e) {
            LOGGER.warning("Error in UIConfig.setUIFont(): " + e.getMessage());
        }
    }
}

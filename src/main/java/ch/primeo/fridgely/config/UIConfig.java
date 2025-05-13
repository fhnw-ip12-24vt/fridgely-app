package ch.primeo.fridgely.config;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Font;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.logging.Logger;

public class UIConfig {

    protected UIConfig() {
        // Prevent instantiation
        throw new UnsupportedOperationException("GameConfig is a utility class and cannot be instantiated");
    }

    public static final Color BACKGROUND_COLOR = new Color(248, 248, 255);
    public static final Color ACTIVE_COLOR = new Color(131, 180, 225);
    public static final Color DISABLED_COLOR = new Color(193, 193, 193);

    static final Logger LOGGER = Logger.getLogger(UIConfig.class.getName());
    /**
     * Default number of rounds in a game.
     */
    public static final int FONT_SIZE = 24;

    public static void setUIFont (javax.swing.plaf.FontUIResource f){
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put (key, f);
        }
    }

    // New parameterless method
    public static void setUIFont() {
        setUIFont(new org.springframework.core.io.DefaultResourceLoader());
    }

    // Refactored method to accept ResourceLoader
    public static void setUIFont(ResourceLoader resourceLoader) {
        try{
           // Use the provided resourceLoader instance
           Resource resource = resourceLoader.getResource("classpath:ch/primeo/fridgely/fonts/bangers_regular.ttf");
           InputStream fontStream = resource.getInputStream();
            //File fontFile = new File("src/main/resources/ch/primeo/fridgely/fonts/bangers_regular.ttf");
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.PLAIN, FONT_SIZE);
            setUIFont(new javax.swing.plaf.FontUIResource(font));
        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
            //setUIFont(new javax.swing.plaf.FontUIResource("SansSerif", Font.PLAIN, FONT_SIZE));
        }
    }
}

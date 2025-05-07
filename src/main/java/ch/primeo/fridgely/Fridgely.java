package ch.primeo.fridgely;

import ch.primeo.fridgely.controller.ChooseGameModeController;
import ch.primeo.fridgely.util.ImageLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.swing.SwingUtilities;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.logging.Logger;

@SpringBootApplication
public class Fridgely {
    /**
     * Logger for the application.
     */
    private static final Logger LOGGER = Logger.getLogger(Fridgely.class.getName());
    public static GraphicsDevice mainAppScreen;
    public static GraphicsDevice scannedItemsScreen;

    public static void main(String[] args) {
        var context = new SpringApplicationBuilder(Fridgely.class).headless(false).run(args);
        var imageLoader = context.getBean(ImageLoader.class);

        // Preload all images before showing UI
        try {
            imageLoader.preloadAllImages();
        } catch (Exception e) {
            LOGGER.severe("Error preloading images: " + e.getMessage());
            System.exit(1);
        }

        // Hide loading screen and show main UI
        SwingUtilities.invokeLater(() -> {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] screens = ge.getScreenDevices();

            for (GraphicsDevice screen : screens) {
                Rectangle bounds = screen.getDefaultConfiguration().getBounds();
                // Assign screens based on new requirement:
                // Main app on smaller screen, scanned items on bigger screen.
                // bounds reversed because the screen is rotated 90 degrees
                if (bounds.width == 600 && bounds.height == 1024) { // Smaller screen (e.g., HDMI-1)
                    mainAppScreen = screen;
                    LOGGER.info("Identified main app screen (1024x600): " + screen.getIDstring());
                } else if (bounds.width == 1080 && bounds.height == 1920) { // Bigger screen (e.g., HDMI-2)
                    scannedItemsScreen = screen;
                    LOGGER.info("Identified scanned items screen (1920x1080): " + screen.getIDstring());
                }
            }

            // Fallbacks
            if (mainAppScreen == null) {
                mainAppScreen = ge.getDefaultScreenDevice();
                LOGGER.warning("Target main app screen (1024x600) not found. Using default: " + mainAppScreen.getIDstring());
            }
            if (scannedItemsScreen == null) {
                if (screens.length > 1) {
                    for (GraphicsDevice screen : screens) {
                        if (screen != mainAppScreen) { // Pick a different screen
                            scannedItemsScreen = screen;
                            LOGGER.warning("Target scanned items screen (1920x1080) not found. Using a different screen: " + scannedItemsScreen.getIDstring());
                            break;
                        }
                    }
                }
                if (scannedItemsScreen == null) { // Still null (e.g. only one screen or default was already picked)
                    scannedItemsScreen = mainAppScreen; 
                    LOGGER.warning("Scanned items screen will use the main app screen as target (1920x1080) was not found and no other distinct screen is available.");
                }
            }
            
            context.getBean(ChooseGameModeController.class);
        });
    }
}

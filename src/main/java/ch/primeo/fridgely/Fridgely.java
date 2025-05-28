package ch.primeo.fridgely;

import ch.primeo.fridgely.controller.ChooseGameModeController;
import ch.primeo.fridgely.util.ImageLoader;
import lombok.Getter;
import org.slf4j.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.awt.*;

@SpringBootApplication
public class Fridgely {
    private static final Logger LOGGER = LoggerFactory.getLogger(Fridgely.class.getName());
    /**
     * Main application screen (1024x600)
     */
    @Getter
    private static GraphicsDevice mainAppScreen;

    /**
     * Scanned items screen (1920x1080)
     */
    @Getter
    private static GraphicsDevice scannedItemsScreen;

    /**
     * Flag to indicate if the application is running on a single display
     */
    @Getter
    private static boolean isSingleDisplay = false;

    public static void main(String[] args) {
        detectScreens();


        // Now start the Spring application context after screen detection is complete
        var context = new SpringApplicationBuilder(Fridgely.class).headless(false).run(args);

        var imageLoader = context.getBean(ImageLoader.class);

        // Preload all images before showing UI
        try {
            imageLoader.preloadAllImages();
        } catch (Exception e) {
            LOGGER.error("Error preloading images: {}", e.getMessage());
            System.exit(1);
        }

        context.getBean(ChooseGameModeController.class);
    }

    public static void detectScreens() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] screens = ge.getScreenDevices();

            for (GraphicsDevice screen : screens) {
                Rectangle bounds = screen.getDefaultConfiguration().getBounds();
                // Assign screens based on new requirement:
                if (bounds.width == 600 && bounds.height == 1024) {
                    mainAppScreen = screen;
                    LOGGER.info("Identified main app screen (1024x600): {}", screen.getIDstring());
                } else if (bounds.width == 1080 && bounds.height == 1920) {
                    scannedItemsScreen = screen;
                    LOGGER.info("Identified scanned items screen (1920x1080): {}", screen.getIDstring());
                }
            }

            // Fallback logic
            if (mainAppScreen == null) {
                mainAppScreen = ge.getDefaultScreenDevice();
                LOGGER.warn("Target main app screen (1024x600) not found. Using default: {}",
                        mainAppScreen.getIDstring());
            }
            if (scannedItemsScreen == null) {
                if (screens.length > 1) {
                    for (GraphicsDevice screen : screens) {
                        if (screen != mainAppScreen) {
                            scannedItemsScreen = screen;
                            LOGGER.warn("Target scanned items screen (1920x1080) not found. Using a different screen: {}", scannedItemsScreen.getIDstring());
                            break;
                        }
                    }
                }
                if (scannedItemsScreen == null) {
                    scannedItemsScreen = mainAppScreen;
                    isSingleDisplay = true;
                    LOGGER.warn("Scanned items screen will use the main app screen as target.");
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error during screen detection: {}", e.getMessage());
        }
    }
}

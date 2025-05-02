package ch.primeo.fridgely;

import ch.primeo.fridgely.controller.ChooseGameModeController;
import ch.primeo.fridgely.util.ImageLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.swing.SwingUtilities;
import java.util.logging.Logger;

@SpringBootApplication
public class Fridgely {
    /**
     * Logger for the application.
     */
    private static final Logger LOGGER = Logger.getLogger(Fridgely.class.getName());

    public static void main(String[] args) {
        var context = new SpringApplicationBuilder(Fridgely.class).headless(false).run(args);

        // Preload all images before showing UI
        try {
            ImageLoader.preloadAllImages();
        } catch (Exception e) {
            LOGGER.severe("Error preloading images: " + e.getMessage());
            System.exit(1);
        }

        // Hide loading screen and show main UI
        SwingUtilities.invokeLater(() -> {
            // Create and set up MainPage
            context.getBean(ChooseGameModeController.class);
        });

        ImageLoader.clearCache();
    }
}

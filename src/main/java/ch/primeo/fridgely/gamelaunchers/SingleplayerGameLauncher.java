package ch.primeo.fridgely.gamelaunchers;

import ch.primeo.fridgely.util.ImageLoader;
import ch.primeo.fridgely.view.ScannedItemsView;
import ch.primeo.fridgely.view.singleplayer.SingleplayerGameView;
import ch.primeo.fridgely.controller.singleplayer.SingleplayerGameController;
import ch.primeo.fridgely.service.ProductRepository;
import ch.primeo.fridgely.service.RecipeRepository;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Launcher for the singleplayer game mode. Initializes and starts the
 * 
 * singleplayer game.
 */
@Component
@Scope("singleton")
public class SingleplayerGameLauncher {

    private final ProductRepository productRepository;
    private final RecipeRepository recipeRepository;
    private final AppLocalizationService localizationService;
    private final ImageLoader imageLoader;

    /**
     * Constructs a new singleplayer game launcher.
     *
     * @param productRepo  the repository for accessing products
     * @param recipeRepo   the repository for accessing recipes
     * @param localization the service for text localization
     */
    public SingleplayerGameLauncher(ProductRepository productRepo, RecipeRepository recipeRepo,
                                    AppLocalizationService localization, ImageLoader imageLoader) {
        this.productRepository = productRepo;
        this.recipeRepository = recipeRepo;
        this.localizationService = localization;
        this.imageLoader = imageLoader;
    }

    /**
     * Launches the singleplayer game.
     */
    public void launchGame() {
        SwingUtilities.invokeLater(() -> {
            // Create the game controller
            SingleplayerGameController gameController = new SingleplayerGameController(productRepository,
                    recipeRepository);

            // Create the main game frame fullscreen and undecorated
            JFrame gameFrame = new JFrame("Fridgely - Singleplayer Game");
            gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            gameFrame.setUndecorated(true); // Remove window decorations
            gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen

            // Create the game view
            new SingleplayerGameView(gameController, localizationService, gameFrame, imageLoader);

            //
            // Create a second frame for displaying scanned items - also fullscreen and
            // undecorated
            JFrame scannedItemsFrame = new JFrame("Fridgely - Scanned Items");
            scannedItemsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            scannedItemsFrame.setUndecorated(true); // Remove window decorations
            scannedItemsFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen

            // Add window listener to dispose both frames together
            gameFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    scannedItemsFrame.dispose();
                }
            });

            // Add keyboard escape key listener to exit fullscreen mode
            gameFrame.getRootPane().getInputMap()
                    .put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "escape");
            gameFrame.getRootPane().getActionMap().put("escape", new javax.swing.AbstractAction() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    gameFrame.dispose();
                }
            });

            // Create the scanned items view
            new ScannedItemsView(gameController, localizationService, scannedItemsFrame, imageLoader);
        });
    }
}

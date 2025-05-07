package ch.primeo.fridgely.gamelaunchers;

import ch.primeo.fridgely.Fridgely;
import ch.primeo.fridgely.util.ImageLoader;
import ch.primeo.fridgely.view.ScannedItemsView;
import ch.primeo.fridgely.view.multiplayer.MultiplayerGameView;
import ch.primeo.fridgely.controller.multiplayer.MultiplayerGameController;
import ch.primeo.fridgely.service.ProductRepository;
import ch.primeo.fridgely.service.RecipeRepository;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Launcher for the multiplayer game mode. Initializes and starts the multiplayer game.
 */
@Component
@Scope("singleton")
public class MultiplayerGameLauncher {

    private final ProductRepository productRepository;
    private final RecipeRepository recipeRepository;
    private final AppLocalizationService localizationService;
    private final ImageLoader imageLoader;

    /**
     * Constructs a new multiplayer game launcher.
     *
     * @param productRepo  the repository for accessing products
     * @param recipeRepo   the repository for accessing recipes
     * @param localization the service for text localization
     */
    public MultiplayerGameLauncher(ProductRepository productRepo, RecipeRepository recipeRepo,
            AppLocalizationService localization, ImageLoader imageLoader) {
        this.productRepository = productRepo;
        this.recipeRepository = recipeRepo;
        this.localizationService = localization;
        this.imageLoader = imageLoader;
    }

    /**
     * Launches the multiplayer game.
     */
    public void launchGame() {
        SwingUtilities.invokeLater(() -> {
            // Create the game controller
            MultiplayerGameController gameController = new MultiplayerGameController(productRepository,
                    recipeRepository);

            // Create the main game frame
            JFrame gameFrame = new JFrame("Fridgely - Multiplayer Game");
            gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            gameFrame.setUndecorated(true); // Remove window decorations

            // Create the game view
            new MultiplayerGameView(gameController, localizationService, gameFrame, imageLoader);

            // Create a second frame for displaying scanned items
            JFrame scannedItemsFrame = new JFrame("Fridgely - Scanned Items");
            scannedItemsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            scannedItemsFrame.setUndecorated(true); // Remove window decorations


            // Add window listener to dispose both frames together
            gameFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    scannedItemsFrame.dispose();
                }
            });

            // Add escape key to close game frame
            gameFrame.getRootPane().getInputMap()
                    .put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "escape");
            gameFrame.getRootPane().getActionMap().put("escape", new javax.swing.AbstractAction() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    gameFrame.dispose();
                }
            });

            if(!Fridgely.isSingleDisplay){
                // Position and display frames on their respective screens
                Fridgely.mainAppScreen.setFullScreenWindow(gameFrame);
                Fridgely.scannedItemsScreen.setFullScreenWindow(scannedItemsFrame);
            } else {
                var screenBounds = Fridgely.mainAppScreen.getDefaultConfiguration().getBounds();

                for (JFrame frame : new JFrame[]{gameFrame, scannedItemsFrame}) {
                    frame.setBounds(screenBounds);
                }
            }

            // Create the scanned items view
            new ScannedItemsView(gameController, localizationService, scannedItemsFrame, imageLoader);
        });
    }
}

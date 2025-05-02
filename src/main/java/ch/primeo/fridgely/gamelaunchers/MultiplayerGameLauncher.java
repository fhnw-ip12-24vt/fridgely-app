package ch.primeo.fridgely.gamelaunchers;

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
 * Launcher for the multiplayer game mode.
 * Initializes and starts the multiplayer game.
 */
@Component
@Scope("singleton")
public class MultiplayerGameLauncher {
    
    private final ProductRepository productRepository;
    private final RecipeRepository recipeRepository;
    private final AppLocalizationService localizationService;
    
    /**
     * Constructs a new multiplayer game launcher.
     * 
     * @param productRepository the repository for accessing products
     * @param recipeRepository the repository for accessing recipes
     * @param localizationService the service for text localization
     */
    public MultiplayerGameLauncher(
            ProductRepository productRepository,
            RecipeRepository recipeRepository,
            AppLocalizationService localizationService) {
        this.productRepository = productRepository;
        this.recipeRepository = recipeRepository;
        this.localizationService = localizationService;
    }    /**
     * Launches the multiplayer game.
     */
    public void launchGame() {
        SwingUtilities.invokeLater(() -> {
            // Create the game controller
            MultiplayerGameController gameController = new MultiplayerGameController(productRepository, recipeRepository);
            
            // Create the main game frame fullscreen and undecorated
            JFrame gameFrame = new JFrame("Fridgely - Multiplayer Game");
            gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            gameFrame.setUndecorated(true); // Remove window decorations
            gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
            
            // Create the game view
            new MultiplayerGameView(gameController, localizationService, gameFrame);
            
            // Create a second frame for displaying scanned items - also fullscreen and undecorated
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
            gameFrame.getRootPane().getInputMap().put(
                    javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "escape");
            gameFrame.getRootPane().getActionMap().put("escape", new javax.swing.AbstractAction() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    gameFrame.dispose();
                }
            });
            
            // Create the scanned items view
            new ScannedItemsView(gameController, localizationService, scannedItemsFrame);
        });
    }
}

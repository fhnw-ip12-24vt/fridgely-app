package ch.primeo.fridgely.gamelaunchers;

import ch.primeo.fridgely.Fridgely;
import ch.primeo.fridgely.factory.FrameFactory;
import ch.primeo.fridgely.util.ImageLoader;
import ch.primeo.fridgely.view.ScannedItemsView;
import ch.primeo.fridgely.view.multiplayer.MultiplayerGameView;
import ch.primeo.fridgely.controller.multiplayer.MultiplayerGameController;
import ch.primeo.fridgely.service.ProductRepository;
import ch.primeo.fridgely.service.RecipeRepository;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.*;

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
    private final FrameFactory frameFactory;

    /**
     * Constructor for MultiplayerGameLauncher.
     *
     * @param productRepo  the product repository
     * @param recipeRepo   the recipe repository
     * @param localization the localization service
     * @param imageLoader  the image loader
     * @param frameFactory the frame factory
     */
    public MultiplayerGameLauncher(ProductRepository productRepo, RecipeRepository recipeRepo,
                                   AppLocalizationService localization, ImageLoader imageLoader,
                                   FrameFactory frameFactory) {
        this.productRepository = productRepo;
        this.recipeRepository = recipeRepo;
        this.localizationService = localization;
        this.imageLoader = imageLoader;
        this.frameFactory = frameFactory;
    }


    /**
     * Creates a new JFrame for the game. This method can be overridden for testing purposes.
     *
     * @param title the title of the frame
     * @return a new JFrame instance
     */
    protected JFrame createFrame(String title) {
        return frameFactory.create(title);
    }

    /**
     * Launches the multiplayer game.
     */
    public void launchGame() {
        runOnEDT(this::initGame);
    }

    /**
     * Executes the given task on the Swing Event Dispatch Thread. Override in tests to run synchronously.
     */
    protected void runOnEDT(Runnable task) {
        SwingUtilities.invokeLater(task);
    }

    /**
     * Contains the original body of launchGame() (everything inside the invokeLater lambda). Extracted so it can be
     * called directly in tests (e.g. headless JUnit).
     */
    protected void initGame() {
        // Create the game controller
        MultiplayerGameController gameController = new MultiplayerGameController(productRepository, recipeRepository);

        // Create the main game frame
        JFrame gameFrame = createFrame("Fridgely - Multiplayer Game");
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setUndecorated(true); // Remove window decorations

        // Create the game view
        MultiplayerGameView gameView = new MultiplayerGameView(gameController, localizationService, gameFrame,
                imageLoader);
        gameFrame.setContentPane(gameView);

        // Create a second frame for displaying scanned items
        JFrame scannedItemsFrame = createFrame("Fridgely - Scanned Items");
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
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
        gameFrame.getRootPane().getActionMap().put("escape", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                gameFrame.dispose();
            }
        });

        if (!Fridgely.isSingleDisplay()) {
            // Position and display frames on their respective screens
            Fridgely.getMainAppScreen().setFullScreenWindow(gameFrame);
            Fridgely.getScannedItemsScreen().setFullScreenWindow(scannedItemsFrame);
        } else {
            var screenBounds = Fridgely.getMainAppScreen().getDefaultConfiguration().getBounds();

            for (JFrame frame : new JFrame[]{gameFrame, scannedItemsFrame}) {
                frame.setBounds(screenBounds);
            }
        }

        // Create the scanned items view
        ScannedItemsView scannedItemsView = new ScannedItemsView(gameController, localizationService, scannedItemsFrame,
                imageLoader);
        scannedItemsFrame.setContentPane(scannedItemsView);
    }
}

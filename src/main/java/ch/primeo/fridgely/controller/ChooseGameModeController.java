package ch.primeo.fridgely.controller;

import ch.primeo.fridgely.gamelaunchers.MultiplayerGameLauncher;
import ch.primeo.fridgely.model.GameMode;
import ch.primeo.fridgely.model.PenguinFacialExpression;
import ch.primeo.fridgely.model.PenguinHPState;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import ch.primeo.fridgely.view.ChooseGameModeView;
import ch.primeo.fridgely.view.component.LanguageSwitchButton;
import ch.primeo.fridgely.view.util.DialogBox;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.JLabel;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;

/**
 * Controller for the game mode selection screen.
 */
@Component
@Scope("singleton")
public class ChooseGameModeController implements BaseController {
    
    private final AppLocalizationService localizationService;
    private final ChooseGameModeView view;
    private final MultiplayerGameLauncher multiplayerGameLauncher;

    /**
     * Constructs the controller and sets up UI event handlers.
     * @param localizationService the localization service for text updates
     * @param languageSwitchButton the button for switching languages
     * @param multiplayerGameLauncher the launcher for multiplayer game mode
     */
    public ChooseGameModeController(
            AppLocalizationService localizationService,
            LanguageSwitchButton languageSwitchButton,
            MultiplayerGameLauncher multiplayerGameLauncher) {

        this.localizationService = localizationService;
        this.multiplayerGameLauncher = multiplayerGameLauncher;
        this.view = new ChooseGameModeView(languageSwitchButton, this.localizationService);

        localizationService.subscribe(this::updateUIText);

        view.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose(); // Cleanup
            }
        });

        setupClickableBehavior(view.getSinglePlayerImageLabel(), "gamemode.singleplayer.tooltip",
                () -> selectGameMode(GameMode.SinglePlayer));

        setupClickableBehavior(view.getMultiplayerImageLabel(), "gamemode.multiplayer.tooltip",
                () -> selectGameMode(GameMode.Multiplayer));

        updateUIText();

        this.view.setVisible(true);
    }

    /**
     * Updates the UI text to match the current language.
     */
    public void updateUIText() {
        view.getTitleLabel().setText(localizationService.get("gamemode.title"));
        view.getSinglePlayerTextLabel().setText(localizationService.get("gamemode.singleplayer"));
        view.getMultiplayerTextLabel().setText(localizationService.get("gamemode.multiplayer"));
        view.getSinglePlayerImageLabel().setToolTipText(localizationService
                .get("gamemode.singleplayer.tooltip"));
        view.getMultiplayerImageLabel().setToolTipText(localizationService
                .get("gamemode.multiplayer.tooltip"));
        view.getLangButton().setText(localizationService.get("home.button.lang"));
    }

    /**
     * Sets up click and tooltip behavior for a label.
     * @param label the JLabel to make clickable
     * @param tooltipKey the localization key for the tooltip
     * @param onClick the action to perform on click
     */
    private void setupClickableBehavior(JLabel label, String tooltipKey, Runnable onClick) {
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.setToolTipText(localizationService.get(tooltipKey));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClick.run();
            }
        });
    }

    /**
     * Handles game mode selection and launches the appropriate tutorial or game.
     * @param mode the selected GameMode
     */
    private void selectGameMode(GameMode mode) {
        if (mode == GameMode.Multiplayer) {
            // Show tutorial dialog first, then start multiplayer game
            dispose();
            showMultiplayerTutorial();
        } else if (mode == GameMode.SinglePlayer) {
            // For single player, we can optionally show a tutorial as well
            dispose();
            showSinglePlayerTutorial();
        }
    }

    /**
     * Shows the multiplayer tutorial dialog.
     */
    private void showMultiplayerTutorial() {
        // Tutorial messages explaining multiplayer game rules using localization
        List<String> tutorialMessages = Arrays.asList(
                localizationService.get("tutorial.welcome"),
                localizationService.get("tutorial.game.rounds"),
                localizationService.get("tutorial.multiplayer.player1"),
                localizationService.get("tutorial.multiplayer.player2"),
                localizationService.get("tutorial.points.explanation"),
                localizationService.get("tutorial.game.winner")
        );

        // Show the tutorial dialog
        new DialogBox(tutorialMessages,
                PenguinFacialExpression.HAPPY,
                PenguinHPState.OKAY,
                this::startMultiplayerGame).showDialog();
    }
    
    /**
     * Starts the multiplayer game after the tutorial.
     */
    private void startMultiplayerGame() {
        multiplayerGameLauncher.launchGame();
    }

    /**
     * Shows the single player tutorial dialog.
     */
    private void showSinglePlayerTutorial() {
        // Tutorial messages explaining single player game rules using localization
        List<String> tutorialMessages = Arrays.asList(
                localizationService.get("tutorial.welcome"),
                localizationService.get("tutorial.singleplayer.explanation"),
                localizationService.get("tutorial.singleplayer.recipe"),
                localizationService.get("tutorial.singleplayer.score")
        );

        // Important: Create the dialog before any game initialization
        // and block until it's explicitly completed by the user
        // Start game after dialog completion
        new DialogBox(tutorialMessages,
                PenguinFacialExpression.HAPPY,
                PenguinHPState.OKAY,
                this::startSinglePlayerGame).showDialog();
    }

    /**
     * Starts the single player game after the tutorial.
     */
    private void startSinglePlayerGame() {
    }

    /**
     * Disposes of the controller and releases resources.
     */
    public void dispose() {
        // Perform any necessary cleanup here
        localizationService.unsubscribe(this::updateUIText);
        view.dispose();
    }
}

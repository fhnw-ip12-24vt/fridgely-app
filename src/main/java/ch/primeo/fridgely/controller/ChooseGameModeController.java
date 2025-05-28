package ch.primeo.fridgely.controller;


import ch.primeo.fridgely.gamelaunchers.MultiplayerGameLauncher;
import ch.primeo.fridgely.model.*;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import ch.primeo.fridgely.util.ImageLoader;
import ch.primeo.fridgely.view.ChooseGameModeView;
import ch.primeo.fridgely.view.component.LanguageSwitchButton;
import ch.primeo.fridgely.view.util.DialogBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.JLabel;
import java.awt.Cursor;
import java.awt.event.*;
import java.util.*;

/**
 * Controller for the game mode selection screen.
 */
@Component
@Scope("singleton")
public class ChooseGameModeController implements BaseController {

    // Made package-private for testing
    final AppLocalizationService localizationService;
    ChooseGameModeView view;
    final MultiplayerGameLauncher multiplayerGameLauncher;
    final ImageLoader imageLoader;
    final LanguageSwitchButton languageSwitchButton;

    /**
     * Constructor for ChooseGameModeController.
     *
     * @param localization         the localization service
     * @param languageSwitchButton the button for switching languages
     * @param launcher             the multiplayer game launcher
     * @param imageLoader          the image loader
     */
    @Autowired
    public ChooseGameModeController(
            AppLocalizationService localization,
            LanguageSwitchButton languageSwitchButton,
            MultiplayerGameLauncher launcher,
            ImageLoader imageLoader) {

        this.localizationService = localization;
        this.multiplayerGameLauncher = launcher;
        this.imageLoader = imageLoader;
        this.languageSwitchButton = languageSwitchButton;

        showChooseGameModeView();
    }

    // Constructor for testing purposes
    protected ChooseGameModeController(AppLocalizationService localization, MultiplayerGameLauncher launcher,
                                       ImageLoader imageLoader, ChooseGameModeView view,
                                       LanguageSwitchButton languageSwitchButton) {

        this.localizationService = localization;
        this.multiplayerGameLauncher = launcher;
        this.imageLoader = imageLoader;
        this.languageSwitchButton = languageSwitchButton; // Assign the passed languageSwitchButton
        this.view = view;

        this.view.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        localizationService.subscribe(view);
        view.onLocaleChanged();
        view.setVisible(true);
    }

    public void showChooseGameModeView() {
        // Use the createView method which can be mocked for testing
        this.view = createView(this.languageSwitchButton);

        this.view.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setupClickableBehavior(view.getMultiplayerImageLabel(), "gamemode.multiplayer.tooltip",
                () -> selectGameMode(GameMode.Multiplayer));

        localizationService.subscribe(view);

        view.onLocaleChanged();

        this.view.setVisible(true);
    }

    /**
     * Creates the view. Extracted for testing.
     *
     * @return the created view
     */
    protected ChooseGameModeView createView() {
        return new ChooseGameModeView(null, this.localizationService, imageLoader);
    }

    /**
     * Creates the view with a language switch button. Extracted for testing.
     *
     * @param languageSwitchButton the button for switching languages
     * @return the created view
     */
    protected ChooseGameModeView createView(LanguageSwitchButton languageSwitchButton) {
        return new ChooseGameModeView(languageSwitchButton, this.localizationService, imageLoader);
    }

    /**
     * Creates a dialog box. Extracted for testing.
     */
    protected DialogBox createDialogBox(List<String> messages, PenguinFacialExpression expression, PenguinHPState state,
                                        Runnable onComplete, ImageLoader imageLoader) {

        if (messages == null || expression == null || state == null || onComplete == null || imageLoader == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        return new DialogBox(messages, expression, state, onComplete, imageLoader);
    }

    /**
     * Sets up click and tooltip behavior for a label.
     *
     * @param label      the JLabel to make clickable
     * @param tooltipKey the localization key for the tooltip
     * @param onClick    the action to perform on click
     */
    void setupClickableBehavior(JLabel label, String tooltipKey, Runnable onClick) {
        if (label == null || tooltipKey == null || onClick == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
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
     *
     * @param mode the selected GameMode
     */
    void selectGameMode(GameMode mode) {
        if (mode == GameMode.Multiplayer) {
            // Show tutorial dialog first, then start multiplayer game
            dispose();
            showMultiplayerTutorial();
        }
    }

    /**
     * Shows the multiplayer tutorial dialog.
     */
    void showMultiplayerTutorial() {
        // Tutorial messages explaining multiplayer game rules using localization
        List<String> tutorialMessages = Arrays.asList(localizationService.get("tutorial.welcome"),
                localizationService.get("tutorial.game.rounds"),
                localizationService.get("tutorial.multiplayer.player1"),
                localizationService.get("tutorial.multiplayer.player2"),
                localizationService.get("tutorial.points.explanation"),
                localizationService.get("tutorial.game.winner"));

        // Show the tutorial dialog
        createDialogBox(tutorialMessages, PenguinFacialExpression.HAPPY, PenguinHPState.OKAY,
                this::startMultiplayerGame, imageLoader).showDialog();
    }

    /**
     * Starts the multiplayer game after the tutorial.
     */
    void startMultiplayerGame() {
        multiplayerGameLauncher.launchGame();
    }

    /**
     * Disposes of the controller and releases resources.
     */
    public void dispose() {
        // Perform any necessary cleanup here
        localizationService.unsubscribe(view);
        view.dispose();
    }
}

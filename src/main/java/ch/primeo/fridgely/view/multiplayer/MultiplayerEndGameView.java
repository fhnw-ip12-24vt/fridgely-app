package ch.primeo.fridgely.view.multiplayer;

import ch.primeo.fridgely.controller.multiplayer.MultiplayerGameController;
import ch.primeo.fridgely.model.PenguinHPState;
import ch.primeo.fridgely.model.PenguinModel;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import ch.primeo.fridgely.service.localization.LocalizationObserver;
import ch.primeo.fridgely.util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MultiplayerEndGameView extends JPanel implements PropertyChangeListener, LocalizationObserver {

    private static final String KEY_CONFIRM_NEW_GAME = "confirm.new_game.message";
    private static final String KEY_CONFIRM_NEW_GAME_TITLE = "confirm.new_game.title";
    private static final String KEY_CONFIRM_EXIT_GAME = "confirm.exit_game.message";
    private static final String KEY_CONFIRM_EXIT_GAME_TITLE = "confirm.exit_game.title";

    private final MultiplayerGameController gameController;
    private final AppLocalizationService localizationService;
    private final ImageLoader imageLoader;

    private JLabel titleLabel;
    private JLabel penguinImageLabel;
    private JLabel scoreLabel;
    private JLabel messageLabel;
    private JButton playAgainButton;
    private JButton menuButton;


    public MultiplayerEndGameView(MultiplayerGameController gameController, AppLocalizationService localizationService, ImageLoader imageLoader) {
        this.gameController = gameController;
        this.localizationService = localizationService;
        this.imageLoader = imageLoader;

        initializeComponent();
        setupLaylout();
        registerListeners();
    }

    public void initializeComponent(){
        // Titel
        titleLabel = new JLabel(localizationService.get("endscreen.multiplayer.title"));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Pinguin-Bild
        penguinImageLabel = new JLabel();
        penguinImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon penguinIcon = imageLoader.loadImage(PenguinHPState.fromHP(gameController.getGameStateModel().getScore()).getSpritePath());
            Image scaledImage = penguinIcon.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
            penguinImageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            penguinImageLabel.setText("Penguin Image Placeholder");
        }

        int score = gameController.getGameStateModel().getScore();

        //Message
        messageLabel = new JLabel(String.format((localizationService.get("endscreen.multiplayer." + PenguinHPState.fromHP(score).name().toLowerCase()))));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // Punktzahl
        scoreLabel = new JLabel(String.format(localizationService.get("label.player_score"), score));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Buttons
        playAgainButton = new JButton(localizationService.get("endscreen.multiplayer.playagain"));
        menuButton = new JButton(localizationService.get("endscreen.multiplayer.menu"));

    }

    public void setupLaylout(){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titel hinzufügen
        gbc.gridy = 0;
        add(titleLabel, gbc);

        // Pinguin-Bild hinzufügen
        gbc.gridy = 1;
        add(penguinImageLabel, gbc);

        //Message hinzufügen
        gbc.gridy = 2;
        add(messageLabel, gbc);

        // Punktzahl hinzufügen
        gbc.gridy = 3;
        add(scoreLabel, gbc);

        // Buttons hinzufügen
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.add(playAgainButton);
        buttonPanel.add(menuButton);

        gbc.gridy = 4;
        add(buttonPanel, gbc);

    }


    private void registerListeners() {
        gameController.getGameStateModel().addPropertyChangeListener(this);
        gameController.getPenguinModel().addPropertyChangeListener(this);

        playAgainButton.addActionListener(e -> startNewGame());
        menuButton.addActionListener(e -> exitGame());

    }

    private void startNewGame() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame frame) {
            frame.getContentPane().removeAll(); // Entfernt die aktuelle View
            frame.setContentPane(new MultiplayerGameView(gameController, localizationService, frame, imageLoader));
            frame.revalidate();
            frame.repaint();
        }
        gameController.startNewGame();
    }

    private void exitGame() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            window.dispose();
        }
        localizationService.unsubscribe(this);
        gameController.exitGame();
    }

    @Override
    public void onLocaleChanged() {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}

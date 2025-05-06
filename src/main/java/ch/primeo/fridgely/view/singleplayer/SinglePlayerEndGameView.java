package ch.primeo.fridgely.view.singleplayer;

import ch.primeo.fridgely.controller.singleplayer.SingleplayerGameController;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import ch.primeo.fridgely.util.ImageLoader;
import ch.primeo.fridgely.view.ChooseGameModeView;
import ch.primeo.fridgely.view.component.LanguageSwitchButton;

import javax.swing.*;
import java.awt.*;

public class SinglePlayerEndGameView extends JPanel {
    private final SingleplayerGameController gameController;
    private final AppLocalizationService localizationService;
    private final ImageLoader imageLoader;

    private JLabel titleLabel;
    private JLabel penguinImageLabel;
    private JLabel scoreLabel;
    private JButton playAgainButton;
    private JButton menuButton;

    public SinglePlayerEndGameView(SingleplayerGameController gameController,
                                   AppLocalizationService localizationService,
                                   ImageLoader imageLoader) {
        this.gameController = gameController;
        this.localizationService = localizationService;
        this.imageLoader = imageLoader;

        initializeComponents();
        setupLayout();
        registerListeners();
    }

    private void initializeComponents() {
        // Titel
        titleLabel = new JLabel("DANKE FÜR SPIELEN!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Pinguin-Bild
        penguinImageLabel = new JLabel();
        penguinImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon penguinIcon = imageLoader.loadImage("/ch/primeo/fridgely/sprites/happy.png");
            Image scaledImage = penguinIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            penguinImageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            penguinImageLabel.setText("Penguin Image Placeholder");
        }

        // Punktzahl
        scoreLabel = new JLabel(String.format("Score: %d", gameController.getGameStateModel().getPlayerScore()));
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Buttons
        playAgainButton = new JButton("Nochmal spielen");
        menuButton = new JButton("Menü");
    }

    private void setupLayout() {
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

        // Punktzahl hinzufügen
        gbc.gridy = 2;
        add(scoreLabel, gbc);

        // Buttons hinzufügen
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.add(playAgainButton);
        buttonPanel.add(menuButton);

        gbc.gridy = 3;
        add(buttonPanel, gbc);
    }

    private void registerListeners(){
        playAgainButton.addActionListener(e -> {
            gameController.startNewGame();
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new SingleplayerGameView(gameController, localizationService, frame, imageLoader));
            frame.revalidate();
            frame.repaint();
        });
        menuButton.addActionListener(e -> {
            LanguageSwitchButton btn = new LanguageSwitchButton(localizationService);
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            ChooseGameModeView view = new ChooseGameModeView(btn, localizationService, imageLoader);
            frame.setContentPane(view);
            frame.revalidate();
            frame.repaint();
        });
    }
}
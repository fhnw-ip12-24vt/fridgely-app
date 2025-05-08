package ch.primeo.fridgely.view.multiplayer;

import ch.primeo.fridgely.controller.multiplayer.MultiplayerGameController;
import ch.primeo.fridgely.model.PenguinModel;
import ch.primeo.fridgely.model.multiplayer.MultiplayerGameStateModel;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import ch.primeo.fridgely.service.localization.LocalizationObserver;
import ch.primeo.fridgely.util.ImageLoader;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MultiplayerGameView extends JPanel implements PropertyChangeListener, LocalizationObserver {

    private final MultiplayerGameController gameController;
    private final AppLocalizationService localizationService;
    private final ImageLoader imageLoader;

    // Localization keys
    private static final String KEY_PENGUIN_TITLE = "panel.penguin.title";
    private static final String KEY_GAME_STATUS_TITLE = "panel.status.title";
    private static final String KEY_CONTROLS_TITLE = "panel.controls.title";
    private static final String KEY_NEW_GAME = "button.new_game";
    private static final String KEY_EXIT = "button.exit";
    private static final String KEY_HP_LABEL = "label.hp";
    private static final String KEY_ROUND_LABEL = "label.round";
    private static final String KEY_PLAYER1_SCORE = "label.player1_score";
    private static final String KEY_PLAYER2_SCORE = "label.player2_score";
    private static final String KEY_GAME_OVER_PLAYER1 = "game.over.player1";
    private static final String KEY_GAME_OVER_PLAYER2 = "game.over.player2";
    private static final String KEY_GAME_OVER_TIE = "game.over.tie";
    private static final String KEY_CONFIRM_NEW_GAME = "confirm.new_game.message";
    private static final String KEY_CONFIRM_NEW_GAME_TITLE = "confirm.new_game.title";
    private static final String KEY_CONFIRM_EXIT_GAME = "confirm.exit_game.message";
    private static final String KEY_CONFIRM_EXIT_GAME_TITLE = "confirm.exit_game.title";
    private static final String KEY_PENGUIN_PLACEHOLDER = "placeholder.penguin";

    private JPanel mainPanel;
    private JPanel playerPanel;
    private JPanel gameInfoPanel;
    private JPanel penguinPanel;
    private JPanel scorePanel;
    private JPanel controlPanel;

    private JLabel penguinImageLabel;
    private JLabel penguinHPLabel;
    private JLabel roundLabel;
    private JLabel player1ScoreLabel;
    private JLabel player2ScoreLabel;
    private JButton newGameButton;
    private JButton exitButton;

    private CardLayout playerCardLayout;

    public MultiplayerGameView(MultiplayerGameController controller, AppLocalizationService localization,
            JFrame frame, ImageLoader imageLoader) {
        this.gameController = controller;
        this.localizationService = localization;
        this.imageLoader = imageLoader;

        initializeComponents();
        setupLayout();
        registerListeners();
        updateGameInfo();
        onLocaleChanged(); // Set text initially
        showCurrentPlayerView();

        localizationService.subscribe(this); // Register for future locale changes

        frame.setVisible(true);
    }

    private void initializeComponents() {
        mainPanel = new JPanel();
        gameInfoPanel = new JPanel();
        penguinPanel = new JPanel();
        scorePanel = new JPanel();
        controlPanel = new JPanel();

        MultiplayerPlayer1View player1View = new MultiplayerPlayer1View(gameController, localizationService, imageLoader);
        MultiplayerPlayer2View player2View = new MultiplayerPlayer2View(gameController, localizationService, imageLoader);

        playerCardLayout = new CardLayout();
        playerPanel = new JPanel(playerCardLayout);
        playerPanel.add(player1View, "player1");
        playerPanel.add(player2View, "player2");

        penguinImageLabel = new JLabel();
        try {
            ImageIcon penguinIcon = imageLoader.loadImage("/ch/primeo/fridgely/sprites/happy.png");
            Image scaledImage = penguinIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            penguinImageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            penguinImageLabel.setText("");
        }

        penguinHPLabel = new JLabel();
        roundLabel = new JLabel();
        player1ScoreLabel = new JLabel();
        player2ScoreLabel = new JLabel();
        newGameButton = new JButton();
        exitButton = new JButton();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        gameInfoPanel.setLayout(new BoxLayout(gameInfoPanel, BoxLayout.X_AXIS));
        gameInfoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        penguinPanel.setLayout(new BoxLayout(penguinPanel, BoxLayout.Y_AXIS));
        penguinPanel.add(penguinImageLabel);
        penguinPanel.add(Box.createVerticalStrut(5));
        penguinPanel.add(penguinHPLabel);

        scorePanel.setLayout(new GridLayout(3, 1, 5, 5));
        scorePanel.add(roundLabel);
        scorePanel.add(player1ScoreLabel);
        scorePanel.add(player2ScoreLabel);

        controlPanel.setLayout(new GridLayout(2, 1, 5, 5));
        controlPanel.add(newGameButton);
        controlPanel.add(exitButton);

        gameInfoPanel.add(penguinPanel);
        gameInfoPanel.add(Box.createHorizontalStrut(10));
        gameInfoPanel.add(scorePanel);
        gameInfoPanel.add(Box.createHorizontalStrut(10));
        gameInfoPanel.add(controlPanel);

        mainPanel.add(gameInfoPanel, BorderLayout.NORTH);
        mainPanel.add(playerPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void registerListeners() {
        gameController.getGameStateModel().addPropertyChangeListener(this);
        gameController.getPenguinModel().addPropertyChangeListener(this);

        newGameButton.addActionListener(e -> startNewGame());
        exitButton.addActionListener(e -> exitGame());
    }

    private void showCurrentPlayerView() {
        MultiplayerGameStateModel.Player currentPlayer = gameController.getGameStateModel().getCurrentPlayer();
        if (currentPlayer == MultiplayerGameStateModel.Player.PLAYER1) {
            playerCardLayout.show(playerPanel, "player1");
        } else {
            playerCardLayout.show(playerPanel, "player2");
        }
    }

    private void updateGameInfo() {
        MultiplayerGameStateModel gameState = gameController.getGameStateModel();
        PenguinModel penguinModel = gameController.getPenguinModel();

        roundLabel.setText(String.format(localizationService.get(KEY_ROUND_LABEL), gameState.getCurrentRound(),
                gameState.getTotalRounds()));

        player1ScoreLabel.setText(
                String.format(localizationService.get(KEY_PLAYER1_SCORE), gameState.getPlayer1Score()));
        player2ScoreLabel.setText(
                String.format(localizationService.get(KEY_PLAYER2_SCORE), gameState.getPlayer2Score()));

        penguinHPLabel.setText(String.format(localizationService.get(KEY_HP_LABEL), penguinModel.getHP(), 60));

        try {
            penguinImageLabel.setIcon(imageLoader.loadScaledImage(penguinModel.getImagePathForHP(), 100, 100));
        } catch (Exception e) {
            penguinImageLabel.setText(localizationService.get(KEY_PENGUIN_PLACEHOLDER));
        }

        if (gameState.isGameOver()) {
            MultiplayerGameStateModel.Player winner = gameState.getWinner();
            if (winner == MultiplayerGameStateModel.Player.PLAYER1) {
                JOptionPane.showMessageDialog(this, localizationService.get(KEY_GAME_OVER_PLAYER1));
            } else if (winner == MultiplayerGameStateModel.Player.PLAYER2) {
                JOptionPane.showMessageDialog(this, localizationService.get(KEY_GAME_OVER_PLAYER2));
            } else {
                JOptionPane.showMessageDialog(this, localizationService.get(KEY_GAME_OVER_TIE));
            }
        }
    }

    private void startNewGame() {
        int confirm = JOptionPane.showConfirmDialog(this, localizationService.get(KEY_CONFIRM_NEW_GAME),
                localizationService.get(KEY_CONFIRM_NEW_GAME_TITLE), JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            gameController.startNewGame();
        }
    }

    private void exitGame() {
        int confirm = JOptionPane.showConfirmDialog(this, localizationService.get(KEY_CONFIRM_EXIT_GAME),
                localizationService.get(KEY_CONFIRM_EXIT_GAME_TITLE), JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JFrame) {
                window.dispose();
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof MultiplayerGameStateModel || evt.getSource() instanceof PenguinModel) {
            updateGameInfo();
        }
        if (MultiplayerGameStateModel.PROP_CURRENT_PLAYER.equals(evt.getPropertyName())) {
            showCurrentPlayerView();
        }
    }

    @Override
    public void onLocaleChanged() {
        penguinPanel.setBorder(BorderFactory.createTitledBorder(localizationService.get(KEY_PENGUIN_TITLE)));
        scorePanel.setBorder(BorderFactory.createTitledBorder(localizationService.get(KEY_GAME_STATUS_TITLE)));
        controlPanel.setBorder(BorderFactory.createTitledBorder(localizationService.get(KEY_CONTROLS_TITLE)));

        newGameButton.setText(localizationService.get(KEY_NEW_GAME));
        exitButton.setText(localizationService.get(KEY_EXIT));

        updateGameInfo(); // Re-apply localized labels and scores
    }
}

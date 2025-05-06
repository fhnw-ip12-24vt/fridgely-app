package ch.primeo.fridgely.view.singleplayer;

import ch.primeo.fridgely.Fridgely;
import ch.primeo.fridgely.controller.singleplayer.SingleplayerGameController;
import ch.primeo.fridgely.model.PenguinModel;
import ch.primeo.fridgely.model.singleplayer.SingleplayerGameStateModel;
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

public class SingleplayerGameView extends JPanel implements PropertyChangeListener, LocalizationObserver {

    private final SingleplayerGameController gameController;
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
    private static final String KEY_PLAYER_SCORE = "label.player_score";
    private static final String KEY_GAME_OVER_PLAYER = "game.over.player";
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
    private JLabel playerScoreLabel;
    private JButton newGameButton;
    private JButton exitButton;

    private CardLayout playerCardLayout;

    public SingleplayerGameView(SingleplayerGameController controller, AppLocalizationService localization,
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

        frame.setContentPane(this);
        frame.setVisible(true);
    }

    private void initializeComponents() {
        mainPanel = new JPanel();
        gameInfoPanel = new JPanel();
        penguinPanel = new JPanel();
        scorePanel = new JPanel();
        controlPanel = new JPanel();

        SingleplayerPlayerView playerView = new SingleplayerPlayerView(gameController, localizationService, imageLoader);

        playerCardLayout = new CardLayout();
        playerPanel = new JPanel(playerCardLayout);
        playerPanel.add(playerView, "player");

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
        playerScoreLabel = new JLabel();
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
        scorePanel.add(playerScoreLabel);

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
        playerCardLayout.show(playerPanel, "player");
    }

    private void updateGameInfo() {
        SingleplayerGameStateModel gameState = gameController.getGameStateModel();
        PenguinModel penguinModel = gameController.getPenguinModel();

        roundLabel.setText(String.format(localizationService.get(KEY_ROUND_LABEL), gameState.getCurrentRound(),
                gameState.getTotalRounds()));

        playerScoreLabel.setText(
                String.format(localizationService.get(KEY_PLAYER_SCORE), gameState.getPlayerScore()));

        penguinHPLabel.setText(String.format(localizationService.get(KEY_HP_LABEL), penguinModel.getHP(), 60));

        try {
            ImageIcon penguinIcon = imageLoader.loadImage("/ch/primeo/fridgely/sprites/happy.png");
            Image scaledImage = penguinIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            penguinImageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            penguinImageLabel.setText(localizationService.get(KEY_PENGUIN_PLACEHOLDER));
        }

        if (gameState.isGameOver()) {
            // TODO: Message game over
            JOptionPane.showMessageDialog(this, localizationService.get(KEY_GAME_OVER_TIE));

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
        if (evt.getSource() instanceof SingleplayerGameStateModel || evt.getSource() instanceof PenguinModel) {
            updateGameInfo();
        }
        if (SingleplayerGameStateModel.PROP_CURRENT_PLAYER.equals(evt.getPropertyName())) {
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

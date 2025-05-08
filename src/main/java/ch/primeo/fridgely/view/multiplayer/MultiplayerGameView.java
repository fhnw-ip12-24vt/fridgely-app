package ch.primeo.fridgely.view.multiplayer;

import ch.primeo.fridgely.controller.multiplayer.MultiplayerGameController;
import ch.primeo.fridgely.model.PenguinModel;
import ch.primeo.fridgely.model.multiplayer.MultiplayerGameStateModel;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import ch.primeo.fridgely.service.localization.LocalizationObserver;
import ch.primeo.fridgely.util.ImageLoader;
import ch.primeo.fridgely.view.component.PenguinScorePanel;

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
import java.awt.*;
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

    private MultiplayerPlayer1View player1View;
    private MultiplayerPlayer2View player2View;

    private PenguinScorePanel penguinScorePanel;

    private JPanel playerPanel;
    private JPanel gameInfoPanel;
    private JPanel scorePanel;
    private JPanel controlPanel;

    private JLabel roundLabel;
    private JLabel scoreLabel;
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

        frame.setContentPane(this);
        frame.setVisible(true);
    }

    private void initializeComponents() {
        player1View = new MultiplayerPlayer1View(gameController, localizationService, imageLoader);
        player2View = new MultiplayerPlayer2View(gameController, localizationService, imageLoader);
        penguinScorePanel = new PenguinScorePanel(imageLoader);

        gameInfoPanel = new JPanel();
        scorePanel = new JPanel();
        controlPanel = new JPanel();

        playerCardLayout = new CardLayout();
        playerPanel = new JPanel(playerCardLayout);
        playerPanel.add(player1View, "player1");
        playerPanel.add(player2View, "player2");

        roundLabel = new JLabel();
        roundLabel.setFont(new Font(roundLabel.getFont().getName(), Font.BOLD, 24));
        scoreLabel = new JLabel();
        scoreLabel.setFont(new Font(scoreLabel.getFont().getName(), Font.BOLD, 24));
        newGameButton = new JButton();
        exitButton = new JButton();
    }

    private void setupLayout() {
        setLayout(new BorderLayout(20, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(gameInfoPanel, BorderLayout.NORTH);
        add(playerPanel, BorderLayout.CENTER);

        gameInfoPanel.setLayout(new BorderLayout(0, 10));
        gameInfoPanel.add(penguinScorePanel, BorderLayout.WEST);
        gameInfoPanel.add(scorePanel, BorderLayout.CENTER);
        gameInfoPanel.add(controlPanel, BorderLayout.EAST);

        scorePanel.setLayout(new GridLayout(2, 1, 5, 5));
        scorePanel.add(roundLabel);
        scorePanel.add(scoreLabel);

        controlPanel.setLayout(new GridLayout(2, 1, 5, 5));
        controlPanel.add(newGameButton);
        controlPanel.add(exitButton);
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
            player2View.updateRecipeList();
        }
    }

    private void updateGameInfo() {
        MultiplayerGameStateModel gameState = gameController.getGameStateModel();
        PenguinModel penguinModel = gameController.getPenguinModel();

        roundLabel.setText(String.format(localizationService.get(KEY_ROUND_LABEL), gameState.getCurrentRound(),
                gameState.getTotalRounds()));

        scoreLabel.setText(
                String.format(localizationService.get(KEY_PLAYER1_SCORE), gameState.getScore())
        );


        penguinScorePanel.updatePenguinImage(gameState.getScore());

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
//        penguinPanel.setBorder(BorderFactory.createTitledBorder(localizationService.get(KEY_PENGUIN_TITLE)));
//        scorePanel.setBorder(BorderFactory.createTitledBorder(localizationService.get(KEY_GAME_STATUS_TITLE)));
//        controlPanel.setBorder(BorderFactory.createTitledBorder(localizationService.get(KEY_CONTROLS_TITLE)));

        newGameButton.setText(localizationService.get(KEY_NEW_GAME));
        exitButton.setText(localizationService.get(KEY_EXIT));

        updateGameInfo(); // Re-apply localized labels and scores
    }
}

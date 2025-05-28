package ch.primeo.fridgely.view.multiplayer;

import ch.primeo.fridgely.controller.multiplayer.MultiplayerGameController;
import ch.primeo.fridgely.model.PenguinModel;
import ch.primeo.fridgely.model.multiplayer.MultiplayerGameStateModel;
import ch.primeo.fridgely.service.localization.*;
import ch.primeo.fridgely.util.ImageLoader;
import ch.primeo.fridgely.view.component.*;

import javax.swing.*;
import java.awt.*;
import java.beans.*;

public class MultiplayerGameView extends JPanel implements PropertyChangeListener, LocalizationObserver {

    private final MultiplayerGameController gameController;
    private final AppLocalizationService localizationService;
    private final ImageLoader imageLoader;

    // Localization keys
    private static final String KEY_ROUND_LABEL = "label.round";
    private static final String KEY_PLAYER_SCORE = "label.player_score";
    private static final String KEY_CONFIRM_NEW_GAME = "confirm.new_game.message";
    private static final String KEY_CONFIRM_NEW_GAME_TITLE = "confirm.new_game.title";
    private static final String KEY_CONFIRM_EXIT_GAME = "confirm.exit_game.message";
    private static final String KEY_CONFIRM_EXIT_GAME_TITLE = "confirm.exit_game.title";

    private MultiplayerPlayer2View player2View;

    private PenguinScorePanel penguinScorePanel;

    private JPanel playerPanel;
    private JPanel gameInfoPanel;
    private JPanel scorePanel;
    private JPanel controlPanel;

    private JLabel roundLabel;
    private JLabel scoreLabel;
    private FButton newGameButton;
    private FButton exitButton;

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
        MultiplayerPlayer1View player1View = new MultiplayerPlayer1View(gameController, localizationService, imageLoader);
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
        scoreLabel = new JLabel();

        //Get Icons for buttons
        ImageIcon newGameIcon = imageLoader.loadScaledImage("/ch/primeo/fridgely/icons/restart.png", 50, 50);
        ImageIcon homeIcon = imageLoader.loadScaledImage("/ch/primeo/fridgely/icons/home.png", 50, 50);


        newGameButton = new FButton(newGameIcon, true);
        exitButton = new FButton(homeIcon, true);
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
        if (gameController.getGameStateModel().isGameOver()) {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JFrame frame) {
                frame.setContentPane(new MultiplayerEndGameView(gameController, localizationService, imageLoader));
                frame.revalidate();
                frame.repaint();
            }
        } else if (currentPlayer == MultiplayerGameStateModel.Player.PLAYER1) {
            playerCardLayout.show(playerPanel, "player1");
        } else {
            playerCardLayout.show(playerPanel, "player2");
            player2View.updateRecipeList();
        }
    }

    private void updateGameInfo() {
        MultiplayerGameStateModel gameState = gameController.getGameStateModel();

        roundLabel.setText(String.format(localizationService.get(KEY_ROUND_LABEL), gameState.getCurrentRound(),
                gameState.getTotalRounds()));

        scoreLabel.setText(
                String.format(localizationService.get(KEY_PLAYER_SCORE), gameState.getScore())
        );


        penguinScorePanel.updatePenguinImage(gameState.getScore());

    }

    private void startNewGame() {
        int confirm = JOptionPane.showConfirmDialog(this, localizationService.get(KEY_CONFIRM_NEW_GAME),
                localizationService.get(KEY_CONFIRM_NEW_GAME_TITLE), JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            gameController.startNewGame();

        }
    }

    private void exitGame() {
        // Create the JOptionPane but don't show it yet
        JOptionPane optionPane = new JOptionPane(
            localizationService.get(KEY_CONFIRM_EXIT_GAME),
            JOptionPane.QUESTION_MESSAGE,
            JOptionPane.YES_NO_OPTION
        );
        
        // Create a dialog using the JOptionPane
        Window window = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = optionPane.createDialog(window, localizationService.get(KEY_CONFIRM_EXIT_GAME_TITLE));
        
        // Ensure it's positioned relative to the parent window
        dialog.setLocationRelativeTo(window);
        dialog.setVisible(true);
        
        // Handle the result
        Object value = optionPane.getValue();
        if (value instanceof Integer && (Integer) value == JOptionPane.YES_OPTION) {
            if (window instanceof JFrame) {
                window.dispose();
            }
            localizationService.unsubscribe(this);
            gameController.exitGame();
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
        updateGameInfo();
    }
}

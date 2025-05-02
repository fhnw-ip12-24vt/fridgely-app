package ch.primeo.fridgely.view.multiplayer;

import ch.primeo.fridgely.Fridgely;
import ch.primeo.fridgely.controller.multiplayer.MultiplayerGameController;
import ch.primeo.fridgely.model.PenguinModel;
import ch.primeo.fridgely.model.multiplayer.MultiplayerGameStateModel;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import ch.primeo.fridgely.util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Main view for the multiplayer game mode.
 * Integrates the Player1View and Player2View with shared game information display.
 */
public class MultiplayerGameView extends JPanel implements PropertyChangeListener {
    
    private final MultiplayerGameController gameController;
    private final AppLocalizationService localizationService;
    
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
    private MultiplayerPlayer1View player1View;
    private MultiplayerPlayer2View player2View;
    
    /**
     * Constructs a new game view.
     * 
     * @param gameController the main game controller
     * @param localizationService the service for text localization
     * @param frame the parent JFrame for this view
     */
    public MultiplayerGameView(MultiplayerGameController gameController, AppLocalizationService localizationService, JFrame frame) {
        this.gameController = gameController;
        this.localizationService = localizationService;
        
        initializeComponents();
        setupLayout();
        registerListeners();        updateGameInfo();
        showCurrentPlayerView();
        
        // Set as content pane without packing to maintain fullscreen settings
        frame.setContentPane(this);
        // Don't call pack() as it would override the fullscreen setting
        frame.setVisible(true);
    }
    
    /**
     * Initializes the UI components.
     */
    private void initializeComponents() {
        mainPanel = new JPanel();
        gameInfoPanel = new JPanel();
        penguinPanel = new JPanel();
        scorePanel = new JPanel();
        controlPanel = new JPanel();
        
        // Create the player views
        player1View = new MultiplayerPlayer1View(gameController, localizationService);
        player2View = new MultiplayerPlayer2View(gameController, localizationService);
        
        // Create the player panel with card layout
        playerCardLayout = new CardLayout();
        playerPanel = new JPanel(playerCardLayout);
        playerPanel.add(player1View, "player1");
        playerPanel.add(player2View, "player2");
        
        // Initialize labels and buttons
        penguinImageLabel = new JLabel();
        try {
            ImageIcon penguinIcon = ImageLoader.loadImage("/ch/primeo/fridgely/sprites/happy.png", Fridgely.class);
            Image scaledImage = penguinIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            penguinImageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            penguinImageLabel.setText("[Penguin]");
        }
        
        penguinHPLabel = new JLabel("HP: 30/60");
        roundLabel = new JLabel("Round 1/3");
        player1ScoreLabel = new JLabel("Player 1: 0");
        player2ScoreLabel = new JLabel("Player 2: 0");
        
        newGameButton = new JButton("New Game");
        exitButton = new JButton("Exit");
    }
    
    /**
     * Sets up the layout of the view.
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Main panel layout
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Game info panel (top)
        gameInfoPanel.setLayout(new BoxLayout(gameInfoPanel, BoxLayout.X_AXIS));
        gameInfoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Penguin panel (left side of game info)
        penguinPanel.setLayout(new BoxLayout(penguinPanel, BoxLayout.Y_AXIS));
        penguinPanel.setBorder(BorderFactory.createTitledBorder("Penguin"));
        penguinPanel.add(penguinImageLabel);
        penguinPanel.add(Box.createVerticalStrut(5));
        penguinPanel.add(penguinHPLabel);
        
        // Score panel (center of game info)
        scorePanel.setLayout(new GridLayout(3, 1, 5, 5));
        scorePanel.setBorder(BorderFactory.createTitledBorder("Game Status"));
        scorePanel.add(roundLabel);
        scorePanel.add(player1ScoreLabel);
        scorePanel.add(player2ScoreLabel);
        
        // Control panel (right side of game info)
        controlPanel.setLayout(new GridLayout(2, 1, 5, 5));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Controls"));
        controlPanel.add(newGameButton);
        controlPanel.add(exitButton);
        
        // Add panels to game info panel
        gameInfoPanel.add(penguinPanel);
        gameInfoPanel.add(Box.createHorizontalStrut(10));
        gameInfoPanel.add(scorePanel);
        gameInfoPanel.add(Box.createHorizontalStrut(10));
        gameInfoPanel.add(controlPanel);
        
        // Add components to main panel
        mainPanel.add(gameInfoPanel, BorderLayout.NORTH);
        mainPanel.add(playerPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    /**
     * Registers event listeners for the UI components and models.
     */
    private void registerListeners() {
        // Register with models for updates
        gameController.getGameStateModel().addPropertyChangeListener(this);
        gameController.getPenguinModel().addPropertyChangeListener(this);
        
        // Button action listeners
        newGameButton.addActionListener(e -> startNewGame());
        exitButton.addActionListener(e -> exitGame());
    }
    
    /**
     * Shows the view for the current player.
     */
    private void showCurrentPlayerView() {
        MultiplayerGameStateModel.Player currentPlayer = gameController.getGameStateModel().getCurrentPlayer();
        if (currentPlayer == MultiplayerGameStateModel.Player.PLAYER1) {
            playerCardLayout.show(playerPanel, "player1");
        } else {
            playerCardLayout.show(playerPanel, "player2");
        }
    }
    
    /**
     * Updates the game information display.
     */
    private void updateGameInfo() {
        MultiplayerGameStateModel gameState = gameController.getGameStateModel();
        PenguinModel penguinModel = gameController.getPenguinModel();
        
        // Update round label
        roundLabel.setText(String.format("Round %d/%d", 
                gameState.getCurrentRound(), 
                gameState.getTotalRounds()));
        
        // Update score labels
        player1ScoreLabel.setText(String.format("Player 1: %d", gameState.getPlayer1Score()));
        player2ScoreLabel.setText(String.format("Player 2: %d", gameState.getPlayer2Score()));
          // Update penguin HP
        penguinHPLabel.setText(String.format("HP: %d/60", penguinModel.getHP()));
        
        // Update penguin image based on HP using the new getImageForHP method
        try {
            // Get the appropriate image based on the current HP
            ImageIcon penguinIcon = penguinModel.getImageForHP();
            
            // Scale the image to fit the display area
            Image scaledImage = penguinIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            penguinImageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            penguinImageLabel.setText("[Penguin]");
        }
        
        // If game is over, show game result
        if (gameState.isGameOver()) {
            MultiplayerGameStateModel.Player winner = gameState.getWinner();
            if (winner == MultiplayerGameStateModel.Player.PLAYER1) {
                JOptionPane.showMessageDialog(this, "Game Over! Player 1 wins!");
            } else if (winner == MultiplayerGameStateModel.Player.PLAYER2) {
                JOptionPane.showMessageDialog(this, "Game Over! Player 2 wins!");
            } else {
                JOptionPane.showMessageDialog(this, "Game Over! It's a tie!");
            }
        }
    }
    
    /**
     * Starts a new game.
     */
    private void startNewGame() {
        int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Are you sure you want to start a new game?", 
                "New Game", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            gameController.startNewGame();
        }
    }
    
    /**
     * Exits the game and returns to the main menu.
     */
    private void exitGame() {
        int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Are you sure you want to exit the game?", 
                "Exit Game", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // TODO: Implement returning to main menu
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JFrame) {
                window.dispose();
            }
        }
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof MultiplayerGameStateModel) {
            if (MultiplayerGameStateModel.PROP_CURRENT_PLAYER.equals(evt.getPropertyName())) {
                showCurrentPlayerView();
            }
            updateGameInfo();
        } else if (evt.getSource() instanceof PenguinModel) {
            updateGameInfo();
        }
    }
}

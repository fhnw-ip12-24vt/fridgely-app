package ch.primeo.fridgely.view;

import ch.primeo.fridgely.config.GameConfig;
import ch.primeo.fridgely.controller.GameController;
import ch.primeo.fridgely.controller.Player1Controller;
import ch.primeo.fridgely.model.FridgeStockModel;
import ch.primeo.fridgely.model.GameStateModel;
import ch.primeo.fridgely.model.PenguinFacialExpression;
import ch.primeo.fridgely.model.Product;
import ch.primeo.fridgely.service.localization.AppLocalizationService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.Window;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.KeyboardFocusManager;
import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

/**
 * View for Player 1 (Scanner) in the multiplayer game mode.
 * Shows the barcode scanning interface.
 * The scanned items are now displayed in a separate window.
 */
public class Player1View extends JPanel implements PropertyChangeListener {
    
    private final GameController gameController;
    private final Player1Controller player1Controller;
    private final AppLocalizationService localizationService;
    
    private JLabel scanPromptLabel;
    private String scanPromptBase = "Scan an item";
    private int scanPromptDotCount = 0;
    private javax.swing.Timer scanPromptTimer;
    private JButton finishTurnButton;
    private JLabel statusLabel;
    private JLabel minProductsLabel;
    
    /**
     * Constructs a new Player 1 view.
     * 
     * @param gameController the main game controller
     * @param localizationService the service for text localization
     */
    public Player1View(GameController gameController, AppLocalizationService localizationService) {
        this.gameController = gameController;
        this.player1Controller = gameController.getPlayer1Controller();
        this.localizationService = localizationService;
        
        initializeComponents();
        setupLayout();
        registerListeners();
        updateComponentStates();
    }
    
    /**
     * Initializes the UI components.
     */
    private void initializeComponents() {
        scanPromptLabel = new JLabel(scanPromptBase, SwingConstants.CENTER);
        scanPromptLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        scanPromptLabel.setAlignmentX(CENTER_ALIGNMENT);
        scanPromptLabel.setVisible(false);
        
        finishTurnButton = new JButton("Finish Turn");
        
        statusLabel = new JLabel("Player 1's Turn: Scan Products");
        statusLabel.setFont(new Font(statusLabel.getFont().getName(), Font.BOLD, 16));
        
        minProductsLabel = new JLabel(
                String.format("Scan at least %d products", GameConfig.MIN_PRODUCTS_PER_ROUND));
    }
    
    /**
     * Sets up the layout of the view.
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Status panel at the top
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.NORTH);

        // Centered scan prompt label
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(scanPromptLabel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Finish turn button at the bottom
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actionPanel.add(finishTurnButton);
        add(actionPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Registers event listeners for the UI components and models.
     */
    private void registerListeners() {
        // Register with models for updates
        gameController.getGameStateModel().addPropertyChangeListener(this);
        gameController.getFridgeStockModel().addPropertyChangeListener(this);
        
        // Button action listeners
        finishTurnButton.addActionListener(e -> {
            finishTurn();
            requestFocusInWindow();
        });
        
        // Instead, use a KeyEventDispatcher for global key capture
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            private StringBuilder barcodeBuffer = new StringBuilder();
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() != KeyEvent.KEY_PRESSED) return false;
                System.out.println("[DISPATCHER DEBUG] keyPressed: code=" + e.getKeyCode() + ", char='" + e.getKeyChar() + "'");
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String barcode = barcodeBuffer.toString().trim();
                    System.out.println("[DISPATCHER DEBUG] Enter pressed, barcodeBuffer='" + barcode + "'");
                    barcodeBuffer.setLength(0);
                    if (!barcode.isEmpty()) {
                        scanBarcode(barcode);
                    }
                } else {
                    char c = e.getKeyChar();
                    if (!Character.isISOControl(c)) {
                        barcodeBuffer.append(c);
                        System.out.println("[DISPATCHER DEBUG] Appended char: '" + c + "', buffer now: '" + barcodeBuffer + "'");
                    }
                }
                return false; // Let other components also process the event
            }
        });
        
        // Set focus to this panel by default to be ready for scanner input
        requestFocusInWindow();
        
        // Add a focus listener to always return focus to this panel after operations
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                requestFocusInWindow();
            }
        });
    }    /**
     * Scans a product based on the barcode in the barcode field.
     * Optimized for barcode scanner input which sends barcode as keyboard input followed by Enter key.
     * Toggles the product: adds it if not in stock or removes it if already in stock.
     */
    private void scanBarcode(String barcode) {
        System.out.println("[DEBUG] scanBarcode called with: '" + barcode + "'");
        setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
        statusLabel.setText("Scanning product...");
        boolean wasInStock = false;
        for (Product p : gameController.getFridgeStockModel().getProducts()) {
            if (p.getBarcode() != null && p.getBarcode().equals(barcode)) {
                wasInStock = true;
                break;
            }
        }
        Product product = player1Controller.scanProduct(barcode);
        if (product != null) {
            String language = localizationService.getLanguage();
            String productName = product.getName(language);
            boolean isGood = product.isBio() || product.isLocal();
            PenguinFacialExpression reaction;
            if (wasInStock) {
                // Removal
                statusLabel.setText("Removed from stock: " + productName);
                reaction = isGood ? PenguinFacialExpression.DISAPPOINTED : PenguinFacialExpression.HAPPY;
            } else {
                // Addition
                statusLabel.setText("Added to stock: " + productName);
                reaction = isGood ? PenguinFacialExpression.HAPPY : PenguinFacialExpression.DISAPPOINTED;
            }
            java.awt.Toolkit.getDefaultToolkit().beep();
            // Show penguin reaction overlay (1 second)
            Window topLevel = SwingUtilities.getWindowAncestor(this);
            PenguinReactionOverlay overlay = new PenguinReactionOverlay(topLevel, reaction);
            overlay.showAndAutoHide();
        } else {
            statusLabel.setText("Product not found: " + barcode);
        }
        setCursor(java.awt.Cursor.getDefaultCursor());
        updateComponentStates();
        requestFocusInWindow();
    }
    
    /**
     * Finishes Player 1's turn.
     */
    private void finishTurn() {
        player1Controller.finishTurn();
        updateComponentStates();
    }
    
    /**
     * Updates the enabled/disabled state of components based on the current game state.
     */
    private void updateComponentStates() {
        GameStateModel gameStateModel = gameController.getGameStateModel();
        FridgeStockModel fridgeStockModel = gameController.getFridgeStockModel();
        
        boolean isPlayer1Turn = gameStateModel.getCurrentPlayer() == GameStateModel.Player.PLAYER1;
        boolean isGameOver = gameStateModel.isGameOver();
        boolean hasEnoughProducts = fridgeStockModel.getProductCount() >= GameConfig.MIN_PRODUCTS_PER_ROUND;
        
        finishTurnButton.setEnabled(isPlayer1Turn && !isGameOver && hasEnoughProducts);
        
        // Animated scan prompt label logic
        scanPromptLabel.setVisible(isPlayer1Turn && !isGameOver && !hasEnoughProducts);
        if (scanPromptLabel.isVisible()) {
            if (scanPromptTimer == null) {
                scanPromptTimer = new javax.swing.Timer(500, e -> {
                    scanPromptDotCount = (scanPromptDotCount + 1) % 4;
                    StringBuilder dots = new StringBuilder();
                    for (int i = 0; i < scanPromptDotCount; i++) dots.append('.');
                    scanPromptLabel.setText(scanPromptBase + dots);
                });
                scanPromptTimer.start();
            } else if (!scanPromptTimer.isRunning()) {
                scanPromptTimer.start();
            }
        } else {
            if (scanPromptTimer != null && scanPromptTimer.isRunning()) {
                scanPromptTimer.stop();
            }
        }
        
        // Update the min products label
        int currentCount = fridgeStockModel.getProductCount();
        if (currentCount < GameConfig.MIN_PRODUCTS_PER_ROUND) {
            minProductsLabel.setText(
                String.format("Scan at least %d more products (%d/%d)",
                    GameConfig.MIN_PRODUCTS_PER_ROUND - currentCount,
                    currentCount,
                    GameConfig.MIN_PRODUCTS_PER_ROUND));
        } else {
            minProductsLabel.setText(
                String.format("Minimum products reached (%d/%d)! You can finish your turn.",
                    currentCount,
                    GameConfig.MIN_PRODUCTS_PER_ROUND));
        }
        
        // Update status label
        if (isGameOver) {
            statusLabel.setText("Game Over!");
        } else if (isPlayer1Turn) {
            statusLabel.setText(String.format("Round %d - Player 1's Turn: Scan Products", 
                    gameStateModel.getCurrentRound()));
        } else {
            statusLabel.setText(String.format("Round %d - Player 2's Turn: Select a Recipe", 
                    gameStateModel.getCurrentRound()));
        }
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof FridgeStockModel) {
            updateComponentStates();
        }
        
        if (evt.getSource() instanceof GameStateModel) {
            updateComponentStates();
        }
    }
}

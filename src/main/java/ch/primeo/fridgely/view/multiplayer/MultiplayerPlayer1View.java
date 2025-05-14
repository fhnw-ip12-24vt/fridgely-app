package ch.primeo.fridgely.view.multiplayer;

import ch.primeo.fridgely.config.GameConfig;
import ch.primeo.fridgely.controller.multiplayer.MultiplayerGameController;
import ch.primeo.fridgely.controller.multiplayer.MultiplayerPlayer1Controller;
import ch.primeo.fridgely.model.FridgeStockModel;
import ch.primeo.fridgely.model.PenguinFacialExpression;
import ch.primeo.fridgely.model.PenguinHPState;
import ch.primeo.fridgely.model.Product;
import ch.primeo.fridgely.model.multiplayer.MultiplayerGameStateModel;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import ch.primeo.fridgely.service.localization.LocalizationObserver;
import ch.primeo.fridgely.util.ImageLoader;
import ch.primeo.fridgely.view.PenguinReactionOverlay;
import ch.primeo.fridgely.view.component.ControlButton;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.event.KeyEvent;

/**
 * View for Player 1 (Scanner) in the multiplayer game mode. Shows the barcode scanning interface. The scanned items are
 * now displayed in a separate window.
 */
public class MultiplayerPlayer1View extends JPanel implements PropertyChangeListener, LocalizationObserver {

    // localization keys
    private static final String KEY_SCAN_PROMPT_BASE = "scan_prompt_base";
    private static final String KEY_STATUS_PLAYER1_TURN_SCAN = "status_player1_turn_scan";
    private static final String KEY_MIN_PRODUCTS_INITIAL_FMT = "min_products_initial_fmt";
    private static final String KEY_SCANNING_PRODUCT = "scanning_product";
    private static final String KEY_REMOVED_FROM_STOCK_FMT = "removed_from_stock_fmt";
    private static final String KEY_ADDED_TO_STOCK_FMT = "added_to_stock_fmt";
    private static final String KEY_PRODUCT_NOT_FOUND_FMT = "product_not_found_fmt";
    private static final String KEY_MIN_PRODUCTS_REMAINING_FMT = "min_products_remaining_fmt";
    private static final String KEY_MIN_PRODUCTS_REACHED_FMT = "min_products_reached_fmt";
    private static final String KEY_GAME_OVER = "game_over";
    private static final String KEY_ROUND_P1_SCAN_FMT = "round_player1_scan_fmt";
    private static final String KEY_ROUND_P2_SELECT_FMT = "round_player2_select_fmt";

    private final MultiplayerGameController gameController;
    private final MultiplayerPlayer1Controller player1Controller;
    private final AppLocalizationService localizationService;
    private final ImageLoader imageLoader;

    private JLabel scanPromptLabel;
    private JLabel scanningPenguinLabel;
    private String scanPromptBase;
    private int scanPromptDotCount = 0;
    private javax.swing.Timer scanPromptTimer;
    private JButton finishTurnButton;
    private JLabel statusLabel;
    private JLabel minProductsLabel;

    /**
     * Constructs a new Player 1 view.
     *
     * @param controller   the main game controller
     * @param localization the service for text localization
     */
    public MultiplayerPlayer1View(MultiplayerGameController controller, AppLocalizationService localization, ImageLoader imageLoader) {
        this.gameController = controller;
        this.player1Controller = gameController.getPlayer1Controller();
        this.localizationService = localization;
        this.imageLoader = imageLoader;

        initializeComponents();
        setupLayout();
        registerListeners();

        // subscribe and initialize texts
        localizationService.subscribe(this);
        onLocaleChanged();

        updateComponentStates();
    }

    /**
     * Initializes the UI components.
     */
    private void initializeComponents() {
        scanPromptLabel = new JLabel("", SwingConstants.CENTER);
        finishTurnButton = new ControlButton(localizationService.get("finish_turn_button"));
        statusLabel = new JLabel("");
        scanningPenguinLabel = new JLabel();
        minProductsLabel = new JLabel("");
        scanningPenguinLabel.setIcon(imageLoader.loadScaledImage("/ch/primeo/fridgely/sprites/penguin_scanning.png", 250, 250));
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

        scanningPenguinLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Centered scan prompt label
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(scanPromptLabel, BorderLayout.SOUTH);
        centerPanel.add(scanningPenguinLabel, BorderLayout.CENTER);
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

        // FButton action listeners
        finishTurnButton.addActionListener(e -> {
            finishTurn();
            requestFocusInWindow();
        });

        // Instead, use a KeyEventDispatcher for global key capture
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            private final StringBuilder barcodeBuffer = new StringBuilder();

            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() != KeyEvent.KEY_PRESSED) {
                    return false;
                }

                System.out.println(
                        "[DISPATCHER DEBUG] keyPressed: code=" + e.getKeyCode() + ", char='" + e.getKeyChar() + "'");
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
                        System.out.println(
                                "[DISPATCHER DEBUG] Appended char: '" + c + "', buffer now: '" + barcodeBuffer + "'");
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
    }

    /**
     * Scans a product based on the barcode in the barcode field. Optimized for barcode scanner input which sends
     * barcode as keyboard input followed by Enter key. Toggles the product: adds it if not in stock or removes it if
     * already in stock.
     */
    private void scanBarcode(String barcode) {
        System.out.println("[DEBUG] scanBarcode called with: '" + barcode + "'");
        setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
        statusLabel.setText(localizationService.get(KEY_SCANNING_PRODUCT));
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
                statusLabel.setText(String.format(localizationService.get(KEY_REMOVED_FROM_STOCK_FMT), productName));
                reaction = isGood ? PenguinFacialExpression.DISAPPOINTED : PenguinFacialExpression.HAPPY;
            } else {
                // Addition
                statusLabel.setText(String.format(localizationService.get(KEY_ADDED_TO_STOCK_FMT), productName));
                reaction = isGood ? PenguinFacialExpression.HAPPY : PenguinFacialExpression.DISAPPOINTED;
            }
            java.awt.Toolkit.getDefaultToolkit().beep();
            // Show penguin reaction overlay (1 second)
            Window topLevel = SwingUtilities.getWindowAncestor(this);
            PenguinReactionOverlay overlay = new PenguinReactionOverlay(topLevel, reaction, localizationService, imageLoader, product);
            overlay.showAndAutoHide();
        } else {
            statusLabel.setText(String.format(localizationService.get(KEY_PRODUCT_NOT_FOUND_FMT), barcode));
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
        MultiplayerGameStateModel gameStateModel = gameController.getGameStateModel();
        FridgeStockModel fridgeStockModel = gameController.getFridgeStockModel();

        boolean isPlayer1Turn = gameStateModel.getCurrentPlayer() == MultiplayerGameStateModel.Player.PLAYER1;
        boolean isGameOver = gameStateModel.isGameOver();
        boolean hasEnoughProducts = fridgeStockModel.getFridgeProducts().size() >= GameConfig.MIN_PRODUCTS_PER_ROUND;

        finishTurnButton.setEnabled(isPlayer1Turn && !isGameOver && hasEnoughProducts);

        // Animated scan prompt label logic
        //scanPromptLabel.setVisible(isPlayer1Turn && !isGameOver && !hasEnoughProducts);
        if (isPlayer1Turn && !isGameOver && !hasEnoughProducts) {
            if (scanPromptTimer == null) {
                scanPromptTimer = new javax.swing.Timer(500, e -> {
                    scanPromptDotCount = (scanPromptDotCount + 1) % 4;
                    scanPromptLabel.setText(scanPromptBase + ".".repeat(Math.max(0, scanPromptDotCount)));
                });
                scanPromptTimer.start();
            } else if (!scanPromptTimer.isRunning()) {
                scanPromptTimer.start();

            }
        } else {
            if (scanPromptTimer != null && scanPromptTimer.isRunning()) {
                scanPromptTimer.stop();
                scanPromptLabel.setText(" ");
            }
        }

        // Update the min products label
        int currentCount = fridgeStockModel.getProducts().size();
        if (currentCount < GameConfig.MIN_PRODUCTS_PER_ROUND) {
            minProductsLabel.setText(String.format(localizationService.get(KEY_MIN_PRODUCTS_REMAINING_FMT),
                    GameConfig.MIN_PRODUCTS_PER_ROUND - currentCount, currentCount, GameConfig.MIN_PRODUCTS_PER_ROUND));
        } else {
            minProductsLabel.setText(String.format(localizationService.get(KEY_MIN_PRODUCTS_REACHED_FMT), currentCount,
                    GameConfig.MIN_PRODUCTS_PER_ROUND));
        }

        // Update status label
        if (isGameOver) {
            statusLabel.setText(localizationService.get(KEY_GAME_OVER));
        } else if (isPlayer1Turn) {
            statusLabel.setText(
                    String.format(localizationService.get(KEY_ROUND_P1_SCAN_FMT), gameStateModel.getCurrentRound()));
        } else {
            statusLabel.setText(
                    String.format(localizationService.get(KEY_ROUND_P2_SELECT_FMT), gameStateModel.getCurrentRound()));
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof FridgeStockModel) {
            updateComponentStates();
        }

        if (evt.getSource() instanceof MultiplayerGameStateModel) {
            updateComponentStates();
        }
    }

    @Override
    public void onLocaleChanged() {
        scanPromptBase = localizationService.get(KEY_SCAN_PROMPT_BASE);
        scanPromptLabel.setText(scanPromptBase);
        statusLabel.setText(localizationService.get(KEY_STATUS_PLAYER1_TURN_SCAN));
        minProductsLabel.setText(String.format(localizationService.get(KEY_MIN_PRODUCTS_INITIAL_FMT),
                GameConfig.MIN_PRODUCTS_PER_ROUND));
    }
}

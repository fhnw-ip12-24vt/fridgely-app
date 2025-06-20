package ch.primeo.fridgely.view.multiplayer;

import ch.primeo.fridgely.config.GameConfig;
import ch.primeo.fridgely.controller.multiplayer.*;
import ch.primeo.fridgely.model.*;
import ch.primeo.fridgely.model.multiplayer.MultiplayerGameStateModel;
import ch.primeo.fridgely.service.localization.*;
import ch.primeo.fridgely.util.ImageLoader;
import ch.primeo.fridgely.view.PenguinReactionOverlay;
import ch.primeo.fridgely.view.component.ControlButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.beans.*;

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
    private static final String KEY_ADDED_TO_STOCK_FMT = "added_to_stock_fmt";
    private static final String KEY_PRODUCT_NOT_FOUND_FMT = "product_not_found_fmt";
    private static final String KEY_MIN_PRODUCTS_REMAINING_FMT = "min_products_remaining_fmt";
    private static final String KEY_MIN_PRODUCTS_REACHED_FMT = "min_products_reached_fmt";
    private static final String KEY_GAME_OVER = "game_over";
    private static final String KEY_ROUND_P1_SCAN_FMT = "round_player1_scan_fmt";
    private static final String KEY_ROUND_P2_SELECT_FMT = "round_player2_select_fmt";
    private static final String KEY_WARNING_NO_RECIPES_TITLE = "warning.no_recipes.title";
    private static final String KEY_WARNING_NO_RECIPES_MESSAGE = "warning.no_recipes.message";
    private static final String KEY_WARNING_NO_RECIPES_BUTTON = "warning.no_recipes.button";

    private final MultiplayerGameController gameController;
    private final MultiplayerPlayer1Controller player1Controller;
    private final AppLocalizationService localizationService;
    private final ImageLoader imageLoader;
    private final PenguinReactionOverlay overlay;
    private JLabel scanPromptLabel;
    private JLabel scanningPenguinLabel;
    private String scanPromptBase;
    private int scanPromptDotCount = 0;
    private javax.swing.Timer scanPromptTimer;
    private JButton finishTurnButton;
    private JLabel statusLabel;
    private JLabel minProductsLabel;
    private boolean hasShownNoRecipesWarning = false;

    /**
     * Constructs a new Player 1 view.
     *
     * @param controller   the main game controller
     * @param localization the service for text localization
     */
    public MultiplayerPlayer1View(MultiplayerGameController controller, AppLocalizationService localization,
                                  ImageLoader imageLoader) {
        this.gameController = controller;
        this.player1Controller = gameController.getPlayer1Controller();
        this.localizationService = localization;
        this.imageLoader = imageLoader;
        this.overlay = new PenguinReactionOverlay(SwingUtilities.getWindowAncestor(this), localizationService,
                imageLoader);
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
        scanningPenguinLabel.setIcon(imageLoader.loadScaledImage("/ch/primeo/fridgely/sprites/penguin_scanning.png",
                250, 250));
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

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String barcode = barcodeBuffer.toString().trim();
                    barcodeBuffer.setLength(0);
                    if (!barcode.isEmpty()) {
                        scanBarcode(barcode);
                    }
                } else {
                    char c = e.getKeyChar();
                    if (!Character.isISOControl(c)) {
                        barcodeBuffer.append(c);
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

        Product product = player1Controller.scanProduct(barcode);
        // If product not null --> product was added to fridge
        if (product != null) {
            String language = localizationService.getLanguage();
            String productName = product.getName(language);

            // TODO: add different reactions
            boolean isGood = product.isBio() || product.isLocal() || product.isLowCo2();
            PenguinFacialExpression reaction = isGood ? PenguinFacialExpression.HAPPY :
                    PenguinFacialExpression.DISAPPOINTED;

            // Addition
            statusLabel.setText(String.format(localizationService.get(KEY_ADDED_TO_STOCK_FMT), productName));
            overlay.showAndAutoHide(reaction, product);
            System.out.println("[DEBUG] continue");

            // java.awt.Toolkit.getDefaultToolkit().beep();

            // Show penguin reaction overlay

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
        // Try to finish the turn
        boolean turnFinished = player1Controller.finishTurn();

        // If turn couldn't be finished due to no available recipes, show warning
        if (!turnFinished && !player1Controller.hasAvailableRecipes()) {
            showNoRecipesWarning();
        } else if (turnFinished) {
            overlay.dispose();
        }
        updateComponentStates();
    }

    /**
     * Shows a custom undecorated warning dialog when no recipes are available with current ingredients.
     */
    private void showNoRecipesWarning() {
        createCustomWarningDialog();
    }

    /**
     * Creates a custom undecorated warning dialog with disappointed penguin sprite.
     */
    private void createCustomWarningDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), true);
        dialog.setUndecorated(true);
        dialog.setSize(600, 300);
        dialog.setLocationRelativeTo(this);

        // Create main panel with background
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Create content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        // Load and display disappointed penguin image
        BufferedImage penguinImage = imageLoader.loadBufferedImage(PenguinFacialExpression.DISAPPOINTED.getSprite());
        if (penguinImage != null) {
            // Scale the image to appropriate size
            Image scaledImage = penguinImage.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            JLabel penguinLabel = new JLabel(new ImageIcon(scaledImage));
            penguinLabel.setHorizontalAlignment(SwingConstants.CENTER);
            contentPanel.add(penguinLabel, BorderLayout.WEST);
        }

        // Create text panel
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        // Title label
        JLabel titleLabel = new JLabel(localizationService.get(KEY_WARNING_NO_RECIPES_TITLE));
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textPanel.add(titleLabel, BorderLayout.NORTH);

        // Message label
        JLabel messageLabel = new JLabel("<html><div style='text-align: center; width: 320px;'>" +
            localizationService.get(KEY_WARNING_NO_RECIPES_MESSAGE) + "</div></html>");
        messageLabel.setFont(messageLabel.getFont().deriveFont(14f));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        textPanel.add(messageLabel, BorderLayout.CENTER);

        // OK button
        JButton okButton = new JButton(localizationService.get(KEY_WARNING_NO_RECIPES_BUTTON));
        okButton.setPreferredSize(new Dimension(100, 35));
        okButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(okButton);
        textPanel.add(buttonPanel, BorderLayout.SOUTH);

        contentPanel.add(textPanel, BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        dialog.setContentPane(mainPanel);
        dialog.setVisible(true);
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
        boolean hasAvailableRecipes = hasEnoughProducts && player1Controller.hasAvailableRecipes();

        finishTurnButton.setEnabled(isPlayer1Turn && !isGameOver && hasEnoughProducts && hasAvailableRecipes);

        // Show warning if minimum products reached but no recipes available
        if (isPlayer1Turn && !isGameOver && hasEnoughProducts && !hasAvailableRecipes && !hasShownNoRecipesWarning) {
            hasShownNoRecipesWarning = true;
            // Use SwingUtilities.invokeLater to show dialog after current UI update completes
            SwingUtilities.invokeLater(this::showNoRecipesWarning);
        }

        // Reset warning flag when conditions change
        if (!isPlayer1Turn || isGameOver || !hasEnoughProducts || hasAvailableRecipes) {
            hasShownNoRecipesWarning = false;
        }

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

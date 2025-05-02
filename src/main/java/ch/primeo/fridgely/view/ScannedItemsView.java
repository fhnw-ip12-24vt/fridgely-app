package ch.primeo.fridgely.view;

import ch.primeo.fridgely.controller.multiplayer.MultiplayerGameController;
import ch.primeo.fridgely.model.FridgeStockModel;
import ch.primeo.fridgely.model.Product;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import ch.primeo.fridgely.service.localization.LocalizationObserver;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * View for displaying scanned items on a secondary screen in multiplayer mode.
 * This view shows all products scanned by Player 1 in real-time.
 */
public class ScannedItemsView extends JPanel implements PropertyChangeListener, LocalizationObserver {
    // define localization keys
    private static final String KEY_SCANNED_ITEMS_HEADER = "scanneditems.header";
    private static final String KEY_LABEL_BIO = "label.bio";
    private static final String KEY_LABEL_NON_BIO = "label.non_bio";
    private static final String KEY_LABEL_LOCAL = "label.local";
    private static final String KEY_LABEL_NON_LOCAL = "label.non_local";

    private final MultiplayerGameController gameController;
    private final AppLocalizationService localizationService;
    private Image backgroundImg;
    
    private JPanel productCardsPanel; // Regular products
    private JPanel defaultProductCardsPanel; // Default products
    private JLabel headerLabel;
    
    /**
     * Constructs a new ScannedItemsView.
     * 
     * @param gameController the main game controller
     * @param localizationService the service for text localization
     * @param frame the parent JFrame for this view
     */
    public ScannedItemsView(MultiplayerGameController gameController, AppLocalizationService localizationService, JFrame frame) {
        this.gameController = gameController;
        this.localizationService = localizationService;
        // Load fridge background image
        backgroundImg = new ImageIcon(getClass().getResource("/ch/primeo/fridgely/sprites/fridge_interior.png")).getImage();
        initializeComponents();
        setupLayout();
        registerListeners();
        // subscribe and apply localization
        localizationService.subscribe(this);
        onLocaleChanged();
        frame.setContentPane(this);
        
        // Handle multi-monitor setup for the second display
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();
        
        // If there's more than one screen, display on the second screen
        if (screens.length > 1) {
            // Use the second screen for this frame
            screens[1].setFullScreenWindow(frame);
        }
        
        frame.setVisible(true);
    }
    
    /**
     * Initializes the UI components.
     */
    private void initializeComponents() {
        productCardsPanel = new JPanel();
        productCardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 14, 24));
        productCardsPanel.setOpaque(false);

        defaultProductCardsPanel = new JPanel();
        defaultProductCardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 18));
        defaultProductCardsPanel.setOpaque(false);

        // placeholder; actual text set in onLocaleChanged()
        headerLabel = new JLabel();
        headerLabel.setFont(new Font(headerLabel.getFont().getName(), Font.BOLD, 18));
    }
    
    /**
     * Sets up the layout of the view.
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setOpaque(false);
        setPreferredSize(new Dimension(500, 600));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setOpaque(false);
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);
        add(productCardsPanel, BorderLayout.CENTER);
        add(defaultProductCardsPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Registers event listeners for the UI components and models.
     */
    private void registerListeners() {
        // Register with fridge model for updates
        gameController.getFridgeStockModel().addPropertyChangeListener(this);
    }
    
    /**
     * Updates the product list from the fridge stock model.
     */
    private void updateProductList() {
        productCardsPanel.removeAll();
        defaultProductCardsPanel.removeAll();
        List<Product> products = gameController.getFridgeStockModel().getProducts();
        List<Product> defaultProducts = gameController.getProductRepository().getAllDefaultProducts();
        java.util.Set<String> inStockBarcodes = new java.util.HashSet<>();
        for (Product p : products) inStockBarcodes.add(p.getBarcode());
        java.util.List<Product> filteredDefaults = new java.util.ArrayList<>();
        for (Product p : defaultProducts) {
            if (!inStockBarcodes.contains(p.getBarcode())) {
                filteredDefaults.add(p);
            }
        }
        // Add fridge stock products (non-default and default in stock) to regular panel
        for (Product product : products) {
            productCardsPanel.add(createProductCard(product));
        }
        // Add default products (not in stock) to default panel at the bottom
        for (Product product : filteredDefaults) {
            defaultProductCardsPanel.add(createDefaultProductCard(product));
        }
        productCardsPanel.revalidate();
        productCardsPanel.repaint();
        defaultProductCardsPanel.revalidate();
        defaultProductCardsPanel.repaint();
    }

    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(0, 0));
        card.setPreferredSize(new Dimension(160, 140)); // Smaller card
        card.setOpaque(true); // Enable background painting
        card.setBackground(new Color(255,255,255,240)); // Semi-opaque white
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180,180,180), 2, true),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        // Title at the top
        JLabel nameLabel = new JLabel(product.getName(localizationService.getLanguage()));
        nameLabel.setFont(new Font(nameLabel.getFont().getName(), Font.BOLD, 13));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(nameLabel, BorderLayout.NORTH);
        // Product image in the center
        ImageIcon icon = product.getProductImage();
        JLabel imageLabel = new JLabel();
        if (icon != null) {
            Image scaledImg = icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImg));
        }
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(48, 48));
        card.add(imageLabel, BorderLayout.CENTER);
        // Labels at the bottom
        JPanel tagsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        tagsPanel.setOpaque(false);
        // bio tag
        JLabel bioLabel = new JLabel(
            product.isBio()
                ? localizationService.get(KEY_LABEL_BIO)
                : localizationService.get(KEY_LABEL_NON_BIO)
        );
        bioLabel.setOpaque(true);
        bioLabel.setFont(new Font(nameLabel.getFont().getName(), Font.BOLD, 10));
        bioLabel.setForeground(Color.WHITE);
        bioLabel.setBackground(product.isBio() ? new Color(46, 204, 113) : new Color(189, 195, 199));
        bioLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(product.isBio() ? new Color(39, 174, 96) : new Color(127, 140, 141), 1, true),
            BorderFactory.createEmptyBorder(2, 8, 2, 8)));
        tagsPanel.add(bioLabel);
        // local tag
        JLabel localLabel = new JLabel(
            product.isLocal()
                ? localizationService.get(KEY_LABEL_LOCAL)
                : localizationService.get(KEY_LABEL_NON_LOCAL)
        );
        localLabel.setOpaque(true);
        localLabel.setFont(new Font(nameLabel.getFont().getName(), Font.BOLD, 10));
        localLabel.setForeground(Color.WHITE);
        localLabel.setBackground(product.isLocal() ? new Color(52, 152, 219) : new Color(189, 195, 199));
        localLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(product.isLocal() ? new Color(41, 128, 185) : new Color(127, 140, 141), 1, true),
            BorderFactory.createEmptyBorder(2, 8, 2, 8)));
        tagsPanel.add(localLabel);
        card.add(tagsPanel, BorderLayout.SOUTH);
        return card;
    }
    
    private JPanel createDefaultProductCard(Product product) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(0, 0));
        card.setPreferredSize(new Dimension(140, 110));
        card.setOpaque(true); // Enable background painting
        card.setBackground(new Color(255,255,255,225)); // Semi-opaque white, slightly more transparent
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createDashedBorder(new Color(180,180,180), 2, 4),
            BorderFactory.createEmptyBorder(6, 6, 6, 6)));
        // Title at the top
        JLabel nameLabel = new JLabel(product.getName(localizationService.getLanguage()));
        nameLabel.setFont(new Font(nameLabel.getFont().getName(), Font.PLAIN, 11));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(nameLabel, BorderLayout.NORTH);
        // Product image in the center
        ImageIcon icon = product.getProductImage();
        JLabel imageLabel = new JLabel();
        if (icon != null) {
            Image scaledImg = icon.getImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImg));
        }
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(36, 36));
        card.add(imageLabel, BorderLayout.CENTER);
        return card;
    }
    
    @Override
    public void onLocaleChanged() {
        headerLabel.setText(localizationService.get(KEY_SCANNED_ITEMS_HEADER));
        // refresh all product cards with updated labels
        updateProductList();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof FridgeStockModel && 
                FridgeStockModel.PROP_FRIDGE_CONTENTS.equals(evt.getPropertyName())) {
            updateProductList();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImg != null) {
            // Draw the background image scaled to fit the panel
            g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

package ch.primeo.fridgely.view;

import ch.primeo.fridgely.*;
import ch.primeo.fridgely.controller.multiplayer.MultiplayerGameController;
import ch.primeo.fridgely.model.FridgeStockModel;
import ch.primeo.fridgely.model.Product;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import ch.primeo.fridgely.service.localization.LocalizationObserver;
import ch.primeo.fridgely.util.ImageLoader;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * View for displaying scanned items on a secondary screen in multiplayer mode. This view shows all products scanned by
 * Player 1 in real-time.
 */
public class ScannedItemsView extends JPanel implements PropertyChangeListener, LocalizationObserver {

    private final ImageIcon LABEL_NON_LOCAL;
    private final ImageIcon LABEL_LOCAL;
    private final ImageIcon LABEL_NON_BIO;
    private final ImageIcon LABEL_BIO;

    private final MultiplayerGameController gameController;
    private final AppLocalizationService localizationService;
    private final ImageLoader imageLoader;
    private final Image backgroundImg;

    private JPanel productCardsPanel; // Regular products
    private JPanel defaultProductCardsPanel; // Default products
    //private JLabel headerLabel;

    private static final double PERCENTAGE_CARDHEIGHT = .168;
    private static final double PERCENTAGE_CARDWIDTH = .16;
    private static final double PERCENTAGE_VGAPSIZE = .026;
    private static final double PERCENTAGE_HGAPSIZE = .03;
    private static final double PERCENTAGE_FONTSIZE = .009;
    private static final double PERCENTAGE_IMAGESIZE = .85;
    private static final int CARD_OPACITY = 180;
    private static final int BG_CARD_OPACITY = 120;
    private final Dimension screenSize = new Dimension(1080, 1920);
    private final Dimension cardSize;
    private final int imageSize;
    private final int gapSizeH;
    private final int gapSizeV;
    private final int fontSize;

    /**
     * Constructs a new ScannedItemsView.
     *
     * @param controller   the main game controller
     * @param localization the service for text localization
     * @param frame        the parent JFrame for this view
     */
    public ScannedItemsView(MultiplayerGameController controller, AppLocalizationService localization, JFrame frame,
                            ImageLoader imageLoader) {
        this.gameController = controller;
        this.localizationService = localization;
        this.imageLoader = imageLoader;
        //screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        cardSize = new Dimension((int) (screenSize.width * PERCENTAGE_CARDWIDTH),
                (int) (screenSize.height * PERCENTAGE_CARDHEIGHT));
        imageSize = (int) (cardSize.width * PERCENTAGE_IMAGESIZE);
        gapSizeH = (int) ((screenSize.width * PERCENTAGE_HGAPSIZE));
        gapSizeV = (int) ((screenSize.height * PERCENTAGE_VGAPSIZE));
        fontSize = (int) (screenSize.height * PERCENTAGE_FONTSIZE);

        // Load images
        LABEL_LOCAL = imageLoader.loadScaledImage("/ch/primeo/fridgely/sprites/LocalAndBio/local.png", 50, 50);
        LABEL_NON_LOCAL = imageLoader.loadScaledImage("/ch/primeo/fridgely/sprites/LocalAndBio/non-local.png", 50, 50);
        LABEL_BIO = imageLoader.loadScaledImage("/ch/primeo/fridgely/sprites/LocalAndBio/bio.png", 50, 50);
        LABEL_NON_BIO = imageLoader.loadScaledImage("/ch/primeo/fridgely/sprites/LocalAndBio/non-bio.png", 50, 50);
        backgroundImg = imageLoader.loadImage("/ch/primeo/fridgely/sprites/fridge_interior.png").getImage();
        initializeComponents();
        setupLayout();
        registerListeners();
        // subscribe and apply localization
        localizationService.subscribe(this);
        onLocaleChanged();
        frame.setContentPane(this);

        // If there's more than one screen, display on the second screen
        if (!Fridgely.isSingleDisplay()) {
            // Use the second screen for this frame
            var screenBounds = Fridgely.getScannedItemsScreen().getDefaultConfiguration().getBounds();

            frame.setUndecorated(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setBounds(screenBounds);
        }

        frame.setVisible(true);
    }

    /**
     * Initializes the UI components.
     */
    private void initializeComponents() {
        productCardsPanel = new JPanel();
        productCardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, gapSizeH, gapSizeV));
        productCardsPanel.setOpaque(false);

        defaultProductCardsPanel = new JPanel();
        defaultProductCardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, gapSizeH, gapSizeV));
        defaultProductCardsPanel.setOpaque(false);

        // placeholder; actual text set in onLocaleChanged()
        //headerLabel = new JLabel();
        //headerLabel.setFont(new Font(headerLabel.getFont().getName(), Font.BOLD, 18));
    }

    /**
     * Sets up the layout of the view.
     */
    private void setupLayout() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setOpaque(false);
        setPreferredSize(screenSize);

        productCardsPanel.setBorder(BorderFactory.createEmptyBorder(45, 0, 0, 0));
        defaultProductCardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        //JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        //headerPanel.setOpaque(false);
        //headerPanel.add(headerLabel);
        //add(headerPanel, BorderLayout.NORTH);
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
        List<Product> products = gameController.getFridgeStockModel().getFridgeProducts();
        List<Product> defaultProducts = gameController.getFridgeStockModel().getDefaultProducts();
        java.util.Set<String> inStockBarcodes = new java.util.HashSet<>();

        for (Product p : products) {
            inStockBarcodes.add(p.getBarcode());
        }

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
        card.setPreferredSize(cardSize);
        card.setOpaque(true);
        card.setBackground(new Color(255, 255, 255, 0)); // Semi-opaque white
        //card.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 3));

        JPanel text = new JPanel();
        text.setLayout(new BorderLayout(0, 0));

        text.setOpaque(true); // Enable background painting
        text.setBackground(new Color(255, 255, 255, CARD_OPACITY)); // Semi-opaque white
        text.setBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 2, true),
                        BorderFactory.createEmptyBorder(4, 8, 8, 8)));

        // Title at the top
        String name = product.getName(localizationService.getLanguage());
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font(nameLabel.getFont().getName(), Font.BOLD, fontSize));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        text.add(nameLabel, BorderLayout.NORTH);
        // Product image in the bottom
        ImageIcon icon = imageLoader.loadScaledImage(product.getProductImagePath(), imageSize, imageSize);
        JLabel imageLabel = new JLabel();

        if (icon != null) {
            imageLabel.setIcon(icon);
        } else {
            imageLabel.setIcon(imageLoader.loadScaledImage(Product.PRODUCT_IMAGE_NOT_FOUND_PATH, imageSize, imageSize));
        }

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(imageSize, imageSize));
        card.add(imageLabel, BorderLayout.CENTER);
        // Labels at the bottom
        JPanel tagsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        tagsPanel.setOpaque(false);
        // bio tag
        JLabel bioLabel = new JLabel();
        bioLabel.setIcon(product.isBio() ? LABEL_BIO : LABEL_NON_BIO);
        bioLabel.setMaximumSize(new Dimension(cardSize.width / 3, cardSize.height / 3));
//        bioLabel.setOpaque(true);
//        bioLabel.setFont(new Font(nameLabel.getFont().getName(), Font.BOLD, (int)(fontSize*.6)));
//        bioLabel.setForeground(Color.WHITE);
//        bioLabel.setBackground(product.isBio() ? new Color(46, 204, 113) : new Color(189, 195, 199));
//        bioLabel.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(product.isBio() ? new Color(39, 174, 96) : new Color(127, 140, 141), 1,
//                        true), BorderFactory.createEmptyBorder(2, 8, 2, 8)));
        tagsPanel.add(bioLabel);
        // local tag
        JLabel localLabel = new JLabel();
        localLabel.setIcon(product.isLocal() ? LABEL_LOCAL : LABEL_NON_LOCAL);
        localLabel.setMaximumSize(new Dimension(cardSize.width / 3, cardSize.height / 3));

        tagsPanel.add(localLabel);
        text.add(tagsPanel, BorderLayout.CENTER);
        text.setMaximumSize(new Dimension(cardSize.width, (int) (cardSize.height * .2)));
        card.add(text, BorderLayout.PAGE_START);
        return card;
    }

    private JPanel createDefaultProductCard(Product product) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(0, 0));
        card.setPreferredSize(cardSize);
        card.setOpaque(true); // Enable background painting
        card.setBackground(new Color(255, 255, 255, BG_CARD_OPACITY)); // Semi-opaque white, slightly more transparent
        card.setBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createDashedBorder(new Color(180, 180, 180), 2, 4),
                        BorderFactory.createEmptyBorder(6, 6, 6, 6)));
        // Title at the top
        JLabel nameLabel = new JLabel(product.getName(localizationService.getLanguage()));
        nameLabel.setFont(new Font(nameLabel.getFont().getName(), Font.PLAIN, fontSize));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(nameLabel, BorderLayout.NORTH);
        // Product image in the center
        ImageIcon icon = imageLoader.loadScaledImage(product.getProductImagePath(), imageSize, imageSize);
        JLabel imageLabel = new JLabel();

        if (icon != null) {
            imageLabel.setIcon(icon);
        } else {
            imageLabel.setIcon(imageLoader.loadScaledImage(Product.PRODUCT_IMAGE_NOT_FOUND_PATH, imageSize, imageSize));
        }

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(imageSize, imageSize));
        card.add(imageLabel, BorderLayout.CENTER);
        return card;
    }

    @Override
    public void onLocaleChanged() {
        updateProductList();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof FridgeStockModel && FridgeStockModel.PROP_FRIDGE_CONTENTS.equals(
                evt.getPropertyName())) {
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

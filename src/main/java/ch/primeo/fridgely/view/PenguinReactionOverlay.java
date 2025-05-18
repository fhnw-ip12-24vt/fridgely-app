package ch.primeo.fridgely.view;

import ch.primeo.fridgely.model.PenguinFacialExpression;
import ch.primeo.fridgely.model.Product;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import ch.primeo.fridgely.util.ImageLoader;
import ch.primeo.fridgely.view.component.ControlButton;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Overlay panel to show a penguin reaction sprite for a short duration.
 */
public class PenguinReactionOverlay extends JWindow {
    private static final int OVERLAY_SIZE_X = 300;
    private static final int OVERLAY_SIZE_Y = 450;
    private static final int DISPLAY_TIME_MS = 5000;

    private final Window parent;
    private final ImageLoader imageLoader;
    private final AppLocalizationService localization;

    private BufferedImage penguinImage;

    private JPanel rootPanel;
    private JPanel reactionPanel;
    private JPanel descPanel;

    private JTextArea descTextArea;

    private JButton okButton;

    private Timer timer;

    public PenguinReactionOverlay(Window parent, AppLocalizationService localization, ImageLoader imageLoader) {
        super(parent);
        this.parent = parent;
        this.imageLoader = imageLoader;
        this.localization = localization;

        initializeControls();
        setupLayout();
    }

    public void initializeControls() {
        rootPanel = new JPanel();
        descPanel = new JPanel();
        reactionPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (penguinImage != null) {
                    int w = penguinImage.getWidth();
                    int h = penguinImage.getHeight();
                    int size = Math.min(OVERLAY_SIZE_X, Math.min(w, h));
                    g.drawImage(penguinImage, (OVERLAY_SIZE_X - size) / 2, (OVERLAY_SIZE_X - size) / 2, size, size,
                            null);
                }
            }
        };

        descTextArea = new JTextArea();

        okButton = new ControlButton("OK");
        okButton.addActionListener(e -> close());
    }

    public void setupLayout() {
//        setBackground(new Color(238, 238, 238));
        setBackground(new Color(0, 0, 0));
        setAlwaysOnTop(true);
        setSize(OVERLAY_SIZE_X, OVERLAY_SIZE_Y);
        setLocationRelativeTo(parent);
        setFocusableWindowState(false);
        setType(Type.POPUP);

        reactionPanel.setOpaque(false);

        rootPanel.setLayout(new BorderLayout(5, 5));
        descPanel.setLayout(new BorderLayout(5, 5));

        descTextArea.setEditable(false);
        descTextArea.setLineWrap(true);
        descTextArea.setOpaque(false);
        descTextArea.setBorder(BorderFactory.createEmptyBorder());
        descTextArea.setFont(UIManager.getFont("Label.font"));

        descPanel.add(descTextArea, BorderLayout.CENTER);
        descPanel.add(okButton, BorderLayout.SOUTH);

        rootPanel.setLayout(new BorderLayout());
        rootPanel.add(reactionPanel, BorderLayout.CENTER);
        rootPanel.add(descPanel, BorderLayout.SOUTH);

        setContentPane(rootPanel);
    }

    public void showAndAutoHide(PenguinFacialExpression expression, Product product) {
        if (timer != null && timer.isRunning()) timer.stop();

        penguinImage = imageLoader.loadBufferedImage(expression.getSprite());
        descTextArea.setText(getDescriptionText(product));
        setVisible(true);
        timer = new Timer(DISPLAY_TIME_MS, e -> close());
        timer.setRepeats(false);
        timer.start();
    }

    private void close() {
        setVisible(false);
        timer.stop();
    }

    private String getDescriptionText(Product product) {
        if (product == null) {
            return "";
        }

        return product.getName(localization.getLanguage()) + ":\n" +
                localization.get(product.isLocal() ? "product.isLocal" : "product.isNotLocal") + "\n" +
                localization.get(product.isBio() ? "product.isBio" : "product.isNotBio") + "\n" +
                localization.get(product.isLowCo2() ? "product.isLowCo2" : "product.isHighCo2");
    }
}

package ch.primeo.fridgely.view;

import ch.primeo.fridgely.config.UIConfig;
import ch.primeo.fridgely.model.PenguinFacialExpression;
import ch.primeo.fridgely.model.Product;
import ch.primeo.fridgely.util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Overlay panel to show a penguin reaction sprite for a short duration.
 */
public class PenguinReactionOverlay extends JWindow {
    private final BufferedImage penguinImage;
    private static final int OVERLAY_SIZE_X = 260;
    private static final int OVERLAY_SIZE_Y = 400;
    private static final int DISPLAY_TIME_MS = 4000;

    public PenguinReactionOverlay(Window parent, PenguinFacialExpression expression, ImageLoader imageLoader, Product product) {
        super(parent);
        this.penguinImage = imageLoader.loadBufferedImage(expression.getSprite());
        setBackground(new Color(238, 238, 238));
        setAlwaysOnTop(true);
        setSize(OVERLAY_SIZE_X, OVERLAY_SIZE_Y);
        setLocationRelativeTo(parent);
        setFocusableWindowState(false);
        setType(Type.POPUP);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (penguinImage != null) {
                    int w = penguinImage.getWidth();
                    int h = penguinImage.getHeight();
                    int size = Math.min(OVERLAY_SIZE_X, Math.min(w, h));
                    g.drawImage(penguinImage, (OVERLAY_SIZE_X - size) / 2, (OVERLAY_SIZE_X - size) / 2, size, size, null);
                }
            }
        };

        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout());
        rootPanel.add(panel, BorderLayout.CENTER);

        JPanel descPanel = new JPanel();
        descPanel.setLayout(new BorderLayout());

        JLabel descLabel = new JLabel();
        String text = (product.isLocal() ? "✅ Lokal\n" : "❌ Nicht Lokal\n") +
                (product.isBio() ? "✅ Bio\n" : "❌ Nicht Bio\n") +
                (product.isLowCo2() ? "✅ Niedriger CO2 Ausstoss" : "❌ Hoher CO2 Ausstoss");
        descLabel.setText(text);
        descPanel.add(descLabel, BorderLayout.CENTER);

        rootPanel.add(descPanel, BorderLayout.SOUTH);
        panel.setOpaque(false);
        setContentPane(rootPanel);
    }

    public void showAndAutoHide() {
        setVisible(true);
        Timer timer = new Timer(DISPLAY_TIME_MS, e -> setVisible(false));
        timer.setRepeats(false);
        timer.start();
    }
}

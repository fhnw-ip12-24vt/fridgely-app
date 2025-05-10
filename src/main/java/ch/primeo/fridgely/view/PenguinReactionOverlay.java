package ch.primeo.fridgely.view;

import ch.primeo.fridgely.config.UIConfig;
import ch.primeo.fridgely.model.PenguinFacialExpression;
import ch.primeo.fridgely.util.ImageLoader;

import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.image.BufferedImage;

/**
 * Overlay panel to show a penguin reaction sprite for a short duration.
 */
public class PenguinReactionOverlay extends JWindow {
    private final BufferedImage penguinImage;
    private static final int OVERLAY_SIZE = 260;
    private static final int DISPLAY_TIME_MS = 1000;

    public PenguinReactionOverlay(Window parent, PenguinFacialExpression expression, ImageLoader imageLoader) {
        super(parent);
        this.penguinImage = imageLoader.loadBufferedImage(expression.getSprite());
        setBackground(UIConfig.BACKGROUND_COLOR);
        setAlwaysOnTop(true);
        setSize(OVERLAY_SIZE, OVERLAY_SIZE);
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
                    int size = Math.min(OVERLAY_SIZE, Math.min(w, h));
                    g.drawImage(penguinImage, (OVERLAY_SIZE - size) / 2, (OVERLAY_SIZE - size) / 2, size, size, null);
                }
            }
        };
        panel.setOpaque(false);
        setContentPane(panel);
    }

    public void showAndAutoHide() {
        setVisible(true);
        Timer timer = new Timer(DISPLAY_TIME_MS, e -> setVisible(false));
        timer.setRepeats(false);
        timer.start();
    }
}

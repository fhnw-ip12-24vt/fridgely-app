package ch.primeo.fridgely.view.component;

import ch.primeo.fridgely.controller.multiplayer.MultiplayerGameController;
import ch.primeo.fridgely.model.*;
import ch.primeo.fridgely.service.ProductRepository;
import ch.primeo.fridgely.util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * A unified panel that displays recipes with their ingredients inline. This combines the recipe list and ingredient
 * details into a single view with lazy loading for improved performance.
 */
public class PenguinScorePanel extends JPanel {

    private final ImageLoader imageLoader;
    private JLabel penguinImageLabel;

    /**
     * Constructs a new unified recipe panel.
     *
     * @param imageLoader the image loader for loading images
     */
    public PenguinScorePanel(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        penguinImageLabel = new JLabel();
        try {
            penguinImageLabel.setIcon(imageLoader.loadScaledImage(PenguinHPState.fromHP(0).getSpritePath(), 250, 250));
        } catch (Exception e) {
            penguinImageLabel.setText("🐧");
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(penguinImageLabel, BorderLayout.CENTER);
    }

    public void updatePenguinImage(int score) {
        try {
            penguinImageLabel.setIcon(imageLoader.loadScaledImage(PenguinHPState.fromHP(score).getSpritePath(), 200, 200));
        } catch (Exception e) {
            penguinImageLabel.setText("🐧");
        }
    }
}

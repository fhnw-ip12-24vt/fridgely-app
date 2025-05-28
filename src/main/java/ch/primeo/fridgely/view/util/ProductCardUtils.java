package ch.primeo.fridgely.view.util;

import javax.swing.*;
import java.awt.*;

public class ProductCardUtils {
    public static void buildProductCard(JPanel card, JLabel nameLabel, JPanel text, ImageIcon icon, ImageIcon fallbackIcon, int imageSize){
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        if(text != null){
            text.add(nameLabel, BorderLayout.NORTH);
        }

        // Product image in the bottom
        JLabel imageLabel = new JLabel();

        if (icon != null) {
            imageLabel.setIcon(icon);
        } else {
            imageLabel.setIcon(fallbackIcon);
        }

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(imageSize, imageSize));
        card.add(imageLabel, BorderLayout.CENTER);

    }
}

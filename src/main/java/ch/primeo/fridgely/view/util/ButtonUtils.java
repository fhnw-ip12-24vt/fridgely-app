package ch.primeo.fridgely.view.util;

import javax.swing.*;
import java.awt.*;

public final class ButtonUtils {
    public static void styleLargeButton(JButton button, int buttonHeight){
        // Fix sizing issues
        Dimension buttonSize = new Dimension(button.getPreferredSize().width, buttonHeight);
        button.setPreferredSize(buttonSize);
        button.setMinimumSize(buttonSize);

        // Ensure button expands horizontally but maintains height
        button.setMaximumSize(new Dimension(Short.MAX_VALUE, buttonHeight));

        // Critical for proper rendering
        button.setOpaque(true);
        button.setHorizontalAlignment(SwingConstants.CENTER);

        // Override layout hints for parent containers
        button.putClientProperty("JComponent.sizeVariant", "large");
    }
}

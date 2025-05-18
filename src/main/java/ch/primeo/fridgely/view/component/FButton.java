package ch.primeo.fridgely.view.component;

import ch.primeo.fridgely.config.UIConfig;

import javax.swing.*;
import java.awt.*;

public class FButton extends JButton {
    private final boolean isTransparent = false;
    private final int fontSize = UIConfig.FONT_SIZE;

    public FButton(String text) {
        new FButton(text, fontSize, isTransparent);
    }

    public FButton(String text, int fontSize, boolean isTransparent) {
        super(text);
        if (isTransparent) {
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
        }
    }

    public FButton(ImageIcon icon, boolean isTransparent) {
        super(icon);
        if (isTransparent) {
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
        }
    }
}

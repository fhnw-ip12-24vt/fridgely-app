package ch.primeo.fridgely.view.component;

import javax.swing.*;

public class FButton extends JButton {

    public FButton(ImageIcon icon, boolean isTransparent) {
        super(icon);
        if (isTransparent) {
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
        }
    }
}

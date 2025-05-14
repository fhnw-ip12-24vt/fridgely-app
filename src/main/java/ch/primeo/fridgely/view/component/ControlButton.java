package ch.primeo.fridgely.view.component;

import ch.primeo.fridgely.config.UIConfig;

import javax.swing.*;
import java.awt.*;

/**
 * A button for switching the application language.
 */
public class ControlButton extends JButton {

    private static final int BUTTON_HEIGHT = 50;

    /**
     * Constructs a LanguageSwitchButton and subscribes to localization changes.
     *
     * @param text the text to display on the button
     */
    public ControlButton(String text) {
        super(text);
        configureButton();
        setEnabled(isEnabled());
    }

    public FinishTurnButton(String text, AppLocalizationService localizationService){
        super(text);
        this.localizationService = localizationService;
        configureButton();
        setEnabled(isEnabled());
    }

    /**
     * Configures the button's appearance and properties.
     */
    private void configureButton() {
        // Override default JButton UI behavior
        setContentAreaFilled(true);
        setFocusPainted(false);
        setBorderPainted(false);

        // Set visual properties
        //setFont(new Font("SansSerif", Font.BOLD, 24));

        // Fix sizing issues
        Dimension buttonSize = new Dimension(getPreferredSize().width, BUTTON_HEIGHT);
        setPreferredSize(buttonSize);
        setMinimumSize(buttonSize);

        // Ensure button expands horizontally but maintains height
        setMaximumSize(new Dimension(Short.MAX_VALUE, BUTTON_HEIGHT));

        // Critical for proper rendering
        setOpaque(true);
        setHorizontalAlignment(SwingConstants.CENTER);

        // Override layout hints for parent containers
        putClientProperty("JComponent.sizeVariant", "large");
    }

    /**
     * Returns the preferred size of the button, ensuring correct height.
     *
     * @return the preferred Dimension
     */
    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        return new Dimension(size.width, BUTTON_HEIGHT);
    }

    /**
     * Returns the minimum size of the button, ensuring correct height.
     *
     * @return the minimum Dimension
     */
    @Override
    public Dimension getMinimumSize() {
        Dimension size = super.getMinimumSize();
        return new Dimension(Math.max(size.width, 100), BUTTON_HEIGHT);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        if(b) {
            setBackground(UIConfig.ACTIVE_COLOR);
        } else {
            setBackground(UIConfig.DISABLED_COLOR);
        }
    }
}

package ch.primeo.fridgely.view.component;

import ch.primeo.fridgely.service.localization.AppLocalizationService;
import ch.primeo.fridgely.view.util.ButtonUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.JButton;
import java.awt.*;
import java.awt.event.*;

/**
 * A button for switching the application language.
 */
@Component
@Scope("singleton")
public class LanguageSwitchButton extends JButton {

    private final AppLocalizationService localizationService;

    private static final int BUTTON_HEIGHT = 50;
    private static final Color BUTTON_COLOR = new Color(0x3F3F3F);
    private static final Color HOVER_COLOR = new Color(0x5A5A5A);

    /**
     * Constructs a LanguageSwitchButton and subscribes to localization changes.
     *
     * @param localization the localization service for text updates
     */
    public LanguageSwitchButton(AppLocalizationService localization) {
        super("");
        this.localizationService = localization;

        configureButton();
        setupBehavior();
        localization.subscribe(this::updateText);
        updateText();
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
        setBackground(BUTTON_COLOR);
        setForeground(Color.WHITE);

        ButtonUtils.styleLargeButton(this, BUTTON_HEIGHT);
    }

    /**
     * Sets up the button's behavior, including click and hover effects.
     */
    private void setupBehavior() {
        addActionListener(e -> localizationService.toggleLocale());

        // Add hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(HOVER_COLOR);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(BUTTON_COLOR);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
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

    /**
     * Updates the button text to match the current language.
     */
    private void updateText() {
        setText(localizationService.get("home.button.lang"));
    }
}

package ch.primeo.fridgely.view;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Toolkit;

/**
 * Base class for all views in the application.
 * Provides a basic JFrame setup with fullscreen and close operation.
 */
public abstract class BaseView {

    private final JFrame frame = new JFrame();

    /**
     * Constructor for BaseView.
     * Initializes the JFrame with fullscreen size, undecorated, and default close operation.
     */
    public BaseView() {
        var screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

    /**
     * Returns the main application window (JFrame) for this view.
     * @return the JFrame for this view
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Disposes of the view and releases resources.
     */
    public void dispose() {
        frame.dispose();
    }

    /**
     * Sets the visibility of the view.
     * @param visible true to show, false to hide
     */
    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
}

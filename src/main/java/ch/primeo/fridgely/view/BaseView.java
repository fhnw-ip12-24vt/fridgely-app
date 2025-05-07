package ch.primeo.fridgely.view;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

/**
 * Base class for all views in the application.
 * Provides a basic JFrame setup with fullscreen and close operation.
 */
public abstract class BaseView {

    private final JFrame frame;

    /**
     * Constructor for BaseView.
     * Initializes the JFrame on the specified GraphicsDevice.
     * @param device The GraphicsDevice to display the frame on. If null, uses default screen.
     */
    public BaseView(GraphicsDevice device) {
        GraphicsDevice targetDevice = device;
        if (targetDevice == null) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            targetDevice = ge.getDefaultScreenDevice();
        }
        this.frame = new JFrame(targetDevice.getDefaultConfiguration());
        
        Rectangle bounds = targetDevice.getDefaultConfiguration().getBounds();
        frame.setBounds(bounds.x, bounds.y, bounds.width, bounds.height); // Position and size to the device
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        // frame.setLocationRelativeTo(null); // Not needed when using setBounds with screen coordinates
    }

    /**
     * Default constructor, uses the default screen.
     */
    public BaseView() {
        this(null); // Delegates to the constructor that handles null device as default
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

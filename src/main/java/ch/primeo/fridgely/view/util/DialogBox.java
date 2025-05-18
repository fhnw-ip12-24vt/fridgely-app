package ch.primeo.fridgely.view.util;

import ch.primeo.fridgely.Fridgely;
import ch.primeo.fridgely.FridgelyContext;
import ch.primeo.fridgely.model.PenguinFacialExpression;
import ch.primeo.fridgely.model.PenguinHPState;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import ch.primeo.fridgely.util.ImageLoader;
import ch.primeo.fridgely.view.component.ControlButton;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * A reusable dialog box with animated arrow that displays a sequence of messages
 * using the dialog box background images.
 */
public class DialogBox extends JPanel {

    private final JFrame frame;

    private final List<String> messages;
    int currentMessageIndex = 0;
    private final PenguinFacialExpression penguinExpression;
    private final PenguinHPState penguinHPState;
    private BufferedImage penguinImage;
    private BufferedImage penguinHPImage;
    private BufferedImage dialogArrowUp;
    private BufferedImage dialogArrowDown;
    private boolean showArrowUp = true;
    private final Timer arrowAnimationTimer;
    private Runnable onCompleteCallback; // Made non-final to be replaced by showDialog
    private final JLabel messageLabel;
    JButton skipButton;

    private static final int ARROW_ANIMATION_DELAY = 500; // milliseconds
    private static final int DIALOG_PADDING = 20;
    private static final int PENGUIN_SIZE = 70;
    private static final int HP_IMAGE_SIZE = 600; // New constant for HP image size
    private static final int SKIP_BUTTON_WIDTH = 220;
    private static final int SKIP_BUTTON_HEIGHT = 40;

    private final ImageLoader imageLoader;

    /**
     * Creates a dialog box with a sequence of messages, penguin expression, HP state, and a callback.
     * This constructor is for application use.
     *
     * @param msgs        the list of messages to display
     * @param expression  the penguin facial expression to show
     * @param state       the penguin HP state to show
     * @param callback    the callback to run when dialog is complete
     * @param imageLoader the image loader to use for loading images
     */
    public DialogBox(List<String> msgs, PenguinFacialExpression expression, PenguinHPState state, Runnable callback,
                     ImageLoader imageLoader) {
        this(msgs, expression, state, callback, imageLoader, new JFrame());
    }

    /**
     * Package-private constructor for testing, allowing JFrame injection.
     */
    DialogBox(List<String> msgs, PenguinFacialExpression expression, PenguinHPState state, Runnable callback,
              ImageLoader imageLoader, JFrame frameInstance) {

        this.imageLoader = imageLoader;
        AppLocalizationService localizationService = FridgelyContext.getBean(AppLocalizationService.class);
        this.frame = frameInstance;

        // Configure frame - these calls will go to the mock in tests
        this.frame.setUndecorated(true);
        if (Fridgely.getMainAppScreen() != null) {
            Fridgely.getMainAppScreen().setFullScreenWindow(this.frame);
        }
        this.frame.setVisible(true);
        // For tests with mock JFrame, ensure a default size if needed by other logic.
        // This might be better handled by stubbing frame.getSize() in the test itself.
        if (this.frame.getSize().width == 0 && this.frame.getSize().height == 0) {
            this.frame.setSize(800, 600); // Default size if not set (e.g. mock)
        }


        this.messages = new ArrayList<>(msgs);
        this.penguinExpression = expression;
        this.penguinHPState = state;
        this.onCompleteCallback = callback; // Initial callback

        setOpaque(false);
        setLayout(null); // Use absolute positioning for components

        // Load images
        loadImages();

        // Create the message label
        messageLabel = new JLabel();
        messageLabel.setHorizontalAlignment(SwingConstants.LEFT);
        messageLabel.setVerticalAlignment(SwingConstants.CENTER);
        messageLabel.setForeground(Color.BLACK);
        add(messageLabel);
        updateMessage();

        // Create the skip button - will be added to glass pane later
        skipButton = new ControlButton(localizationService.get("tutorial.button.skip"));
        skipButton.setFocusPainted(false);
        skipButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        skipButton.addActionListener(e -> skipToEnd());

        // Set up the animation timer
        arrowAnimationTimer = new Timer(ARROW_ANIMATION_DELAY, e -> {
            showArrowUp = !showArrowUp;
            repaint(); // Repaint the entire dialog box with the new arrow position
        });
        arrowAnimationTimer.start();

        // Add click listener to advance messages
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                nextMessage();
            }
        });
    }

    /**
     * Skips to the end of the dialog, completing it immediately.
     */
    private void skipToEnd() {
        arrowAnimationTimer.stop();
        onCompleteCallback.run();
        if (frame != null) {
            frame.dispose();
        }
    }

    /**
     * Loads images for the dialog box, penguin, HP, and arrows.
     */
    private void loadImages() {
        // Load Penguin image
        String spritePath = penguinExpression.getSprite();
        penguinImage = imageLoader.loadBufferedImage(spritePath);
        if (penguinImage == null) {
            System.err.println("Could not find Penguin sprite: " + spritePath);
        }

        // Load HP image
        String hpSpritePath = penguinHPState.getSpritePath();
        penguinHPImage = imageLoader.loadBufferedImage(hpSpritePath);
        if (penguinHPImage == null) {
            System.err.println("Could not find HP sprite: " + hpSpritePath);
        }

        // Load dialog box background images with arrows
        String arrowUpPath = "/ch/primeo/fridgely/vectors/dialog_arrow_up.png";
        dialogArrowUp = imageLoader.loadBufferedImage(arrowUpPath);
        if (dialogArrowUp == null) {
            System.err.println("Could not find dialog_arrow_up.png");
        }

        String arrowDownPath = "/ch/primeo/fridgely/vectors/dialog_arrow_down.png";
        dialogArrowDown = imageLoader.loadBufferedImage(arrowDownPath);
        if (dialogArrowDown == null) {
            System.err.println("Could not find dialog_arrow_down.png");
        }
    }

    /**
     * Updates the message label with the current message.
     */
    private void updateMessage() {
        if (currentMessageIndex < messages.size()) {
            String text = messages.get(currentMessageIndex);
            // Wrap the text to fit dialog width by using HTML
            messageLabel.setText("<html><body style='width:100%'>" + text + "</body></html>");
            //messageLabel.setText(text);

        }
    }

    /**
     * Advances to the next message or completes the dialog.
     */
    void nextMessage() {
        currentMessageIndex++;
        if (currentMessageIndex < messages.size()) {
            updateMessage();
        } else {
            arrowAnimationTimer.stop();
            onCompleteCallback.run();
            if (frame != null) {
                frame.dispose();
            }
        }
    }

    /**
     * Paints the dialog box, penguin, HP image, and arrow.
     *
     * @param g the Graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        int width = getWidth();
        int height = getHeight();

        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the dialog box background based on which arrow to show
        BufferedImage dialogBackground = showArrowUp ? dialogArrowUp : dialogArrowDown;
        if (dialogBackground != null) {
            // Draw the dialog background stretched to fit the panel
            g2d.drawImage(dialogBackground, 0, 0, width, height, null);
        } else {
            // Fallback if dialog background not loaded
            g2d.setColor(new Color(240, 240, 250, 230));
            g2d.fillRoundRect(0, 0, width, height, 20, 20);
            g2d.setColor(new Color(100, 100, 200));
            g2d.setStroke(new BasicStroke(3f));
            g2d.drawRoundRect(0, 0, width - 1, height - 1, 20, 20);
        }

        // Draw Penguin image if available
        if (penguinImage != null) {
            // Calculate scaled dimensions while maintaining aspect ratio
            double scale = (double) PENGUIN_SIZE / Math.max(penguinImage.getWidth(), penguinImage.getHeight());
            int scaledWidth = (int) (penguinImage.getWidth() * scale);
            int scaledHeight = (int) (penguinImage.getHeight() * scale);

            // Draw the image with more right padding (increased from DIALOG_PADDING)
            int penguinXPosition = DIALOG_PADDING * 3; // Increased horizontal position
            g2d.drawImage(penguinImage, penguinXPosition, (height - scaledHeight) / 2,
                    scaledWidth, scaledHeight, null);
        }

        // Draw HP image if available
        if (penguinHPImage != null) {
            int frameWidth = this.frame.getWidth();

            // Calculate position to center the HP image in the frame
            int hpXPosition = (frameWidth - penguinHPImage.getWidth()) / 2;
            int hpYPosition = 50; // Fixed position from top of screen

            g2d.drawImage(penguinHPImage, hpXPosition, hpYPosition, null);
        }

        g2d.dispose();
    }

    /**
     * Returns the preferred size of the dialog box.
     *
     * @return the preferred Dimension
     */
    @Override
    public Dimension getPreferredSize() {
        if (frame != null) {
            // Ensure frame.getWidth() doesn't cause issues with mocks if not stubbed
            int frameWidth = frame.getWidth();
            if (frameWidth == 0 && messages != null && !messages.isEmpty()) { // Basic fallback for mock
                frameWidth = 800; // Default if mock doesn't provide width
            }
            return new Dimension(frameWidth - 100, 200);
        }
        return new Dimension(700, 200); // Default preferred size
    }

    /**
     * Lays out the dialog box components.
     */
    @Override
    public void doLayout() {
        super.doLayout();

        int width = getWidth();
        int height = getHeight();

        // Position the message label with increased padding to respect the inner border of the PNG
        int penguinAreaWidth = PENGUIN_SIZE + (DIALOG_PADDING * 2);
        int horizontalPadding = DIALOG_PADDING * 2; // Increased horizontal padding
        int verticalPadding = DIALOG_PADDING * 2;   // Increased vertical padding

        messageLabel.setBounds(
                penguinAreaWidth + horizontalPadding,
                verticalPadding,
                Math.max((int) (width - penguinAreaWidth - (horizontalPadding * 2) - Math.max(20, width * 0.025)),
                        100), // Slightly wider, still responsive
                height - (verticalPadding * 2)
        );
    }

    /**
     * Shows the dialog box with the specified messages and callback.
     */
    public void showDialog() {
        // If frame is a mock, many operations here might need careful stubbing in tests
        // or this method might be out of scope for pure unit testing of DialogBox logic.
        if (frame == null) {
            System.err.println("DialogBox.showDialog() called with null frame. Skipping UI operations.");
            return;
        }

        Component oldGlassPane = frame.getGlassPane();

        // Create a glass pane that positions the dialog at the bottom
        JPanel glassPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // This makes the glass pane transparent
            }

            @Override
            public boolean isOpaque() {
                return false;
            }
        };
        glassPane.setLayout(null);

        // Add the dialog to the glass pane, positioned at the bottom
        Dimension frameSize = this.frame.getSize();
        int dialogWidth = frameSize.width - 100;
        int dialogHeight = 200;
        int xPos = 50; // 50px from left
        int yPos = frameSize.height - dialogHeight - 100; // 100px from bottom

        setBounds(xPos, yPos, dialogWidth, dialogHeight);
        glassPane.add(this);

        // Add the skip button to the glass pane in the top right corner
        int skipButtonX = frameSize.width - SKIP_BUTTON_WIDTH - 20; // 20px from right edge
        int skipButtonY = 20; // 20px from top edge
        skipButton.setBounds(skipButtonX, skipButtonY, SKIP_BUTTON_WIDTH, SKIP_BUTTON_HEIGHT);
        glassPane.add(skipButton);

        // Add HP image if available - now it will be drawn behind the dialog
        if (penguinHPImage != null) {
            // Calculate scaled dimensions while maintaining aspect ratio
            double scale = (double) HP_IMAGE_SIZE / Math.max(penguinHPImage.getWidth(), penguinHPImage.getHeight());
            int scaledWidth = (int) (penguinHPImage.getWidth() * scale);
            int scaledHeight = (int) (penguinHPImage.getHeight() * scale);

            // Create a scaled version of the image
            BufferedImage scaledHPImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaledHPImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(penguinHPImage, 0, 0, scaledWidth, scaledHeight, null);
            g2d.dispose();

            JLabel hpLabel = new JLabel(new ImageIcon(scaledHPImage));
            int hpX = (frameSize.width - scaledWidth) / 2; // NEW: Use frameSize
            int hpY = yPos - scaledHeight - 20; // Position above the dialog with some padding
            hpLabel.setBounds(hpX, hpY, scaledWidth, scaledHeight);
            hpLabel.setOpaque(false);
            // Add hpLabel only if glassPane is not null (which it should be if frame is not null)
            glassPane.add(hpLabel);
        }


        frame.setGlassPane(glassPane);
        glassPane.setVisible(true);

        // Store original callback
        final Runnable originalCallbackReference = this.onCompleteCallback;

        this.onCompleteCallback = () -> { // Replace instance's callback
            glassPane.setVisible(false);
            frame.setGlassPane(oldGlassPane);
            frame.dispose();
            if (originalCallbackReference != null) {
                SwingUtilities.invokeLater(originalCallbackReference);
            }
        };

        // Removed reflection for onCompleteCallback, as it's now non-final

        // Ensure the dialog gets focus so the user can click it
        this.requestFocusInWindow();
    }
}

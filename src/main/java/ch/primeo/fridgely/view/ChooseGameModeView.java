package ch.primeo.fridgely.view;

import ch.primeo.fridgely.Constants;
import ch.primeo.fridgely.Fridgely;
import ch.primeo.fridgely.util.ImageLoader;
import ch.primeo.fridgely.view.component.LanguageSwitchButton;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import ch.primeo.fridgely.service.localization.LocalizationObserver;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;

/**
 * View for choosing the game mode: single player or multiplayer. Displays options for single player and multiplayer
 * modes with corresponding images and text.
 */
public class ChooseGameModeView implements LocalizationObserver {

    // localization keys
    private static final String KEY_TITLE = "choose_mode_title";
    private static final String KEY_SINGLE_PLAYER = "single_player_mode";
    private static final String KEY_MULTIPLAYER = "multiplayer_mode";
    private static final String KEY_LANG_BUTTON = "button_language";
    private static final String KEY_SINGLE_PLAYER_TOOLTIP = "gamemode.singleplayer.tooltip";
    private static final String KEY_MULTIPLAYER_TOOLTIP = "gamemode.multiplayer.tooltip";

    private final JFrame frame = new JFrame();

    private final AppLocalizationService localizationService;
    private final JButton langButton;
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JPanel gameModePanel;
    private JPanel singlePlayerPanel;
    private JPanel multiplayerPanel;
    private JLabel singlePlayerImageLabel;
    private JLabel multiplayerImageLabel;
    private JLabel singlePlayerTextLabel;
    private JLabel multiplayerTextLabel;

    private static final String SINGLE_PLAYER_IMAGE = "/ch/primeo/fridgely/sprites/single_player.png";
    private static final String MULTIPLAYER_IMAGE = "/ch/primeo/fridgely/sprites/multi_player.png";

    private final ImageLoader imageLoader;

    /**
     * Constructor for the ChooseGameModeView. Initializes the components and sets up the layout.
     * @param button the language switch button
     * @param localization the localization service for text updates
     */
    public ChooseGameModeView(LanguageSwitchButton button, AppLocalizationService localization, ImageLoader imageLoader) {
        this.langButton = button;
        this.localizationService = localization;
        this.imageLoader = imageLoader;

        if(!Fridgely.isSingleDisplay) {
            Fridgely.mainAppScreen.setFullScreenWindow(frame);
        } else {
            var screenBounds = Fridgely.mainAppScreen.getDefaultConfiguration().getBounds();
            frame.setBounds(screenBounds);
            frame.setUndecorated(true);
        }


        initializeComponents();
        setupLayout();
        frame.setContentPane(mainPanel);
    }

    /**
     * Initializes the components of the view, such as panels, labels, and buttons.
     */
    private void initializeComponents() {
        mainPanel = new JPanel();
        titleLabel = new JLabel();
        gameModePanel = new JPanel();

        singlePlayerPanel = new JPanel();
        singlePlayerImageLabel = createImageLabel(SINGLE_PLAYER_IMAGE, 200, 200);
        singlePlayerTextLabel = new JLabel();

        multiplayerPanel = new JPanel();
        multiplayerImageLabel = createImageLabel(MULTIPLAYER_IMAGE, 250, 200);
        multiplayerTextLabel = new JLabel();
    }

    /**
     * Sets up the layout of the view, arranging the components in a specific order and manner.
     */
    private void setupLayout() {
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setBackground(Constants.BACKGROUND_COLOR);

        // Simply add the language button to the north position
        mainPanel.add(langButton, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBackground(Constants.BACKGROUND_COLOR);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 24));
        centerPanel.add(titleLabel, BorderLayout.NORTH);

        gameModePanel.setLayout(new BoxLayout(gameModePanel, BoxLayout.X_AXIS));
        gameModePanel.setBackground(Constants.BACKGROUND_COLOR);
        gameModePanel.add(Box.createHorizontalGlue());

        setupGameModePanel(singlePlayerPanel, singlePlayerImageLabel, singlePlayerTextLabel);
        setupGameModePanel(multiplayerPanel, multiplayerImageLabel, multiplayerTextLabel);

        gameModePanel.add(singlePlayerPanel);
        gameModePanel.add(Box.createRigidArea(new Dimension(50, 0)));
        gameModePanel.add(multiplayerPanel);
        gameModePanel.add(Box.createHorizontalGlue());

        JPanel gameModeWrapperPanel = new JPanel(new GridBagLayout());
        gameModeWrapperPanel.setBackground(Constants.BACKGROUND_COLOR);
        gameModeWrapperPanel.add(gameModePanel);

        centerPanel.add(gameModeWrapperPanel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Sets up a game mode panel with the given image and text labels.
     *
     * @param panel      the panel to set up
     * @param imageLabel the image label for the panel
     * @param textLabel  the text label for the panel
     */
    private void setupGameModePanel(JPanel panel, JLabel imageLabel, JLabel textLabel) {
        panel.setBackground(Constants.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(imageLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(textLabel);
    }

    /**
     * Creates a JLabel with an image icon.
     *
     * @param resourcePath the path to the image resource
     * @return the JLabel with the image icon
     */
    private JLabel createImageLabel(String resourcePath, int width, int height) {
        JLabel label = new JLabel();
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        ImageIcon scaledIcon = imageLoader.loadScaledImage(resourcePath, width, height);

        if (scaledIcon != null) {
            label.setIcon(scaledIcon);
        } else {
            label.setText("Image not found: " + resourcePath);
        }

        return label;
    }

    /**
     * Returns the title label.
     *
     * @return the JLabel for the title
     */
    public JLabel getTitleLabel() {
        return titleLabel;
    }

    /**
     * Returns the label for the single player text.
     *
     * @return the JLabel for single player text
     */
    public JLabel getSinglePlayerTextLabel() {
        return singlePlayerTextLabel;
    }

    /**
     * Returns the label for the multiplayer text.
     *
     * @return the JLabel for multiplayer text
     */
    public JLabel getMultiplayerTextLabel() {
        return multiplayerTextLabel;
    }

    /**
     * Returns the label for single player image.
     *
     * @return the JLabel for single player image
     */
    public JLabel getSinglePlayerImageLabel() {
        return singlePlayerImageLabel;
    }

    /**
     * Returns the label for multiplayer image.
     *
     * @return the JLabel for multiplayer image
     */
    public JLabel getMultiplayerImageLabel() {
        return multiplayerImageLabel;
    }

    /**
     * Returns the language switch button.
     *
     * @return the LanguageSwitchButton
     */
    public JButton getLangButton() {
        return langButton;
    }

    @Override
    public void onLocaleChanged() {
        titleLabel.setText(localizationService.get(KEY_TITLE));
        singlePlayerTextLabel.setText(localizationService.get(KEY_SINGLE_PLAYER));
        multiplayerTextLabel.setText(localizationService.get(KEY_MULTIPLAYER));
        singlePlayerImageLabel.setToolTipText(localizationService.get(KEY_SINGLE_PLAYER_TOOLTIP));
        multiplayerImageLabel.setToolTipText(localizationService.get(KEY_MULTIPLAYER_TOOLTIP));
        langButton.setText(localizationService.get(KEY_LANG_BUTTON));
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public void dispose() {
        frame.dispose();
    }
}

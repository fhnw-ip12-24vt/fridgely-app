package ch.primeo.fridgely.view.singleplayer;

import ch.primeo.fridgely.controller.singleplayer.SingleplayerGameController;
import ch.primeo.fridgely.controller.singleplayer.SingleplayerPlayerController;
import ch.primeo.fridgely.model.RecipeModel;
import ch.primeo.fridgely.model.singleplayer.SingleplayerGameStateModel;
import ch.primeo.fridgely.model.Recipe;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import ch.primeo.fridgely.service.localization.LocalizationObserver;
import ch.primeo.fridgely.view.component.UnifiedRecipePanel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * View for the player in the singleplayer game mode. Shows available recipes with their ingredients in a unified
 * display.
 */
public class SingleplayerPlayerView extends JPanel implements PropertyChangeListener, LocalizationObserver {

    // localization keys
    private static final String KEY_FINISH_TURN_BUTTON = "singleplayer.player.finish_turn_button";
    private static final String KEY_STATUS_GAME_OVER = "singleplayer.player.status_game_over";
    private static final String KEY_STATUS_PLAYER2_RECIPE_SELECTED
            = "singleplayer.player.status_player_recipe_selected";

    private static final String KEY_STATUS_PLAYER2_SELECT_RECIPE = "singleplayer.player.status_player_select_recipe";

    private final SingleplayerGameController gameController;
    private final SingleplayerPlayerController playerController;
    private final AppLocalizationService localizationService;

    private UnifiedRecipePanel unifiedRecipePanel;
    private JButton finishTurnButton;
    private JLabel statusLabel;

    /**
     * Constructs a new Player 2 view.
     *
     * @param controller      the main game controller
     * @param localization the service for text localization
     */
    public SingleplayerPlayerView(SingleplayerGameController controller,
            AppLocalizationService localization) {
        this.gameController = controller;
        this.playerController = gameController.getPlayerController();
        this.localizationService = localization;

        initializeComponents();
        setupLayout();
        registerListeners();

        // subscribe to locale changes and apply once
        localizationService.subscribe(this);
        onLocaleChanged();

        // Update the recipe list on startup
        updateRecipeList();
        updateComponentStates();
    }

    /**
     * Initializes the UI components.
     */
    private void initializeComponents() {
        // Create the unified recipe panel
        unifiedRecipePanel = new UnifiedRecipePanel(gameController, gameController.getProductRepository());

        // initialize with empty text; will be set in onLocaleChanged()
        finishTurnButton = new JButton();
        statusLabel = new JLabel();
        statusLabel.setFont(new Font(statusLabel.getFont().getName(), Font.BOLD, 16));
    }

    /**
     * Sets up the layout of the view.
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Status panel at the top
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.NORTH);

        // Add the unified recipe panel to the center
        add(unifiedRecipePanel, BorderLayout.CENTER);

        // Button panel at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(finishTurnButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Registers event listeners for the UI components and models.
     */
    private void registerListeners() {
        // Register with models for updates
        gameController.getGameStateModel().addPropertyChangeListener(this);
        gameController.getRecipeModel().addPropertyChangeListener(this);

        // Set up the recipe selection listener (type-safe)
        unifiedRecipePanel.setRecipeSelectionListener(this::selectRecipe);

        // Finish turn button listener
        finishTurnButton.addActionListener(e -> finishTurn());
    }

    /**
     * Selects the specified recipe.
     *
     * @param recipe the recipe to select
     */
    private void selectRecipe(Recipe recipe) {
        if (recipe != null) {
            playerController.selectRecipe(recipe);
            updateComponentStates();
        }
    }

    /**
     * Finishes Player 2's turn.
     */
    private void finishTurn() {
        playerController.finishTurn();
        updateComponentStates();
    }

    /**
     * Updates the recipe list with the current recipes from the model.
     */
    private void updateRecipeList() {
        unifiedRecipePanel.updateRecipeList();
    }

    /**
     * Updates the enabled/disabled state of components based on the current game state.
     */
    private void updateComponentStates() {
        SingleplayerGameStateModel gameStateModel = gameController.getGameStateModel();
        RecipeModel recipeModel = gameController.getRecipeModel();

        boolean isGameOver = gameStateModel.isGameOver();
        boolean hasSelectedRecipe = recipeModel.getSelectedRecipe() != null;

        // Update enabled states
        unifiedRecipePanel.setEnabled(!isGameOver && !hasSelectedRecipe);
        finishTurnButton.setEnabled(!isGameOver && hasSelectedRecipe);

        // Update status label
        if (isGameOver) {
            statusLabel.setText(localizationService.get(KEY_STATUS_GAME_OVER));
        } else {
            if (hasSelectedRecipe) {
                statusLabel.setText(String.format(localizationService.get(KEY_STATUS_PLAYER2_RECIPE_SELECTED),
                        gameStateModel.getCurrentRound()));
            } else {
                statusLabel.setText(String.format(localizationService.get(KEY_STATUS_PLAYER2_SELECT_RECIPE),
                        gameStateModel.getCurrentRound()));
            }
        }
    }

    @Override
    public void onLocaleChanged() {
        finishTurnButton.setText(localizationService.get(KEY_FINISH_TURN_BUTTON));
        // statusLabel text will be refreshed via updateComponentStates()
        updateComponentStates();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof SingleplayerGameStateModel) {
            updateComponentStates();
        } else if (evt.getSource() instanceof RecipeModel && RecipeModel.PROP_AVAILABLE_RECIPES.equals(
                evt.getPropertyName())) {
            updateRecipeList();
        } else if (evt.getSource() instanceof RecipeModel && RecipeModel.PROP_SELECTED_RECIPE.equals(
                evt.getPropertyName())) {
            updateComponentStates();
        }
    }
}

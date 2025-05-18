package ch.primeo.fridgely.view.multiplayer;

import ch.primeo.fridgely.controller.multiplayer.MultiplayerGameController;
import ch.primeo.fridgely.controller.multiplayer.MultiplayerPlayer2Controller;
import ch.primeo.fridgely.model.RecipeModel;
import ch.primeo.fridgely.model.multiplayer.MultiplayerGameStateModel;
import ch.primeo.fridgely.model.Recipe;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import ch.primeo.fridgely.service.localization.LocalizationObserver;
import ch.primeo.fridgely.util.ImageLoader;
import ch.primeo.fridgely.view.component.ControlButton;
import ch.primeo.fridgely.view.component.UnifiedRecipePanel;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * View for Player 2 (Chef) in the multiplayer game mode. Shows available recipes with their ingredients in a unified
 * display.
 */
public class MultiplayerPlayer2View extends JPanel implements PropertyChangeListener, LocalizationObserver {

    // localization keys
    private static final String KEY_FINISH_TURN_BUTTON = "multiplayer.player2.finish_turn_button";
    private static final String KEY_STATUS_GAME_OVER = "multiplayer.player2.status_game_over";
    private static final String KEY_STATUS_PLAYER2_RECIPE_SELECTED
            = "multiplayer.player2.status_player2_recipe_selected";

    private static final String KEY_STATUS_PLAYER2_SELECT_RECIPE = "multiplayer.player2.status_player2_select_recipe";
    private static final String KEY_STATUS_PLAYER1_SCAN_PRODUCTS = "multiplayer.player2.status_player1_scan_products";

    private final MultiplayerGameController gameController;
    private final MultiplayerPlayer2Controller player2Controller;
    private final AppLocalizationService localizationService;
    private final ImageLoader imageLoader;

    private UnifiedRecipePanel unifiedRecipePanel;
    private ControlButton finishTurnButton;
    private JLabel statusLabel;

    /**
     * Constructs a new Player 2 view.
     *
     * @param controller   the main game controller
     * @param localization the service for text localization
     */
    public MultiplayerPlayer2View(MultiplayerGameController controller,
                                  AppLocalizationService localization, ImageLoader imageLoader) {
        this.gameController = controller;
        this.player2Controller = gameController.getPlayer2Controller();
        this.localizationService = localization;
        this.imageLoader = imageLoader;

        initializeComponents();
        setupLayout();
        registerListeners();

        // subscribe to locale changes and apply once
        localizationService.subscribe(this);
        onLocaleChanged();

        // Update the recipe list on startup
        //updateRecipeList();
        updateComponentStates();
    }

    /**
     * Initializes the UI components.
     */
    private void initializeComponents() {
        // Create the unified recipe panel
        unifiedRecipePanel = new UnifiedRecipePanel(gameController, gameController.getProductRepository(),
                imageLoader, localizationService);

        // initialize with empty text; will be set in onLocaleChanged()
        finishTurnButton = new ControlButton(localizationService.get("finish_turn_button"));
        statusLabel = new JLabel();
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

        // FButton panel at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
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
            player2Controller.selectRecipe(recipe);
            updateComponentStates();
        }
    }

    /**
     * Finishes Player 2's turn.
     */
    private void finishTurn() {
        player2Controller.finishTurn();
        updateComponentStates();
    }

    /**
     * Updates the recipe list with the current recipes from the model.
     */
    void updateRecipeList() {
        unifiedRecipePanel.updateRecipeList();
    }

    /**
     * Updates the enabled/disabled state of components based on the current game state.
     */
    private void updateComponentStates() {
        MultiplayerGameStateModel gameStateModel = gameController.getGameStateModel();
        RecipeModel recipeModel = gameController.getRecipeModel();

        boolean isPlayer2Turn = gameStateModel.getCurrentPlayer() == MultiplayerGameStateModel.Player.PLAYER2;
        boolean isGameOver = gameStateModel.isGameOver();
        boolean hasSelectedRecipe = recipeModel.getSelectedRecipe() != null;

        // Update enabled states
        unifiedRecipePanel.setEnabled(isPlayer2Turn && !isGameOver && !hasSelectedRecipe);
        finishTurnButton.setEnabled(isPlayer2Turn && !isGameOver && hasSelectedRecipe);

        // Update status label
        if (isGameOver) {
            //statusLabel.setText(localizationService.get(KEY_STATUS_GAME_OVER));
        } else if (isPlayer2Turn) {
            if (hasSelectedRecipe) {
                statusLabel.setText(String.format(localizationService.get(KEY_STATUS_PLAYER2_RECIPE_SELECTED),
                        gameStateModel.getCurrentRound()));
            } else {
                statusLabel.setText(String.format(localizationService.get(KEY_STATUS_PLAYER2_SELECT_RECIPE),
                        gameStateModel.getCurrentRound()));
            }
        } else {
            statusLabel.setText(String.format(localizationService.get(KEY_STATUS_PLAYER1_SCAN_PRODUCTS),
                    gameStateModel.getCurrentRound()));
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
        if (evt.getSource() instanceof MultiplayerGameStateModel) {
            updateComponentStates();
//        } else if (evt.getSource() instanceof RecipeModel && RecipeModel.PROP_AVAILABLE_RECIPES.equals(
//                evt.getPropertyName())) {
//            updateRecipeList();
        } else if (evt.getSource() instanceof RecipeModel && RecipeModel.PROP_SELECTED_RECIPE.equals(
                evt.getPropertyName())) {
            updateComponentStates();
        }
    }
}

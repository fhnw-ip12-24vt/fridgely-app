package ch.primeo.fridgely.view;

import ch.primeo.fridgely.controller.GameController;
import ch.primeo.fridgely.controller.Player2Controller;
import ch.primeo.fridgely.model.GameStateModel;
import ch.primeo.fridgely.model.RecipeModel;
import ch.primeo.fridgely.model.Recipe;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import ch.primeo.fridgely.view.component.UnifiedRecipePanel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * View for Player 2 (Chef) in the multiplayer game mode.
 * Shows available recipes with their ingredients in a unified display.
 */
public class Player2View extends JPanel implements PropertyChangeListener {
    
    private final GameController gameController;
    private final Player2Controller player2Controller;
    private final AppLocalizationService localizationService;
    
    private UnifiedRecipePanel unifiedRecipePanel;
    private JButton finishTurnButton;
    private JLabel statusLabel;
    
    /**
     * Constructs a new Player 2 view.
     * 
     * @param gameController the main game controller
     * @param localizationService the service for text localization
     */
    public Player2View(GameController gameController, AppLocalizationService localizationService) {
        this.gameController = gameController;
        this.player2Controller = gameController.getPlayer2Controller();
        this.localizationService = localizationService;
        
        initializeComponents();
        setupLayout();
        registerListeners();
        
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
        
        // Finish turn button
        finishTurnButton = new JButton("Finish Turn");
        
        // Status label
        statusLabel = new JLabel("Player 2's Turn: Select a Recipe");
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
        unifiedRecipePanel.setRecipeSelectionListener(recipe -> {
            selectRecipe(recipe);
        });

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
    private void updateRecipeList() {
        unifiedRecipePanel.updateRecipeList();
    }
    
    /**
     * Updates the enabled/disabled state of components based on the current game state.
     */
    private void updateComponentStates() {
        GameStateModel gameStateModel = gameController.getGameStateModel();
        RecipeModel recipeModel = gameController.getRecipeModel();
        
        boolean isPlayer2Turn = gameStateModel.getCurrentPlayer() == GameStateModel.Player.PLAYER2;
        boolean isGameOver = gameStateModel.isGameOver();
        boolean hasSelectedRecipe = recipeModel.getSelectedRecipe() != null;
        
        // Update enabled states
        unifiedRecipePanel.setEnabled(isPlayer2Turn && !isGameOver && !hasSelectedRecipe);
        finishTurnButton.setEnabled(isPlayer2Turn && !isGameOver && hasSelectedRecipe);
        
        // Update status label
        if (isGameOver) {
            statusLabel.setText("Game Over!");
        } else if (isPlayer2Turn) {
            if (hasSelectedRecipe) {
                statusLabel.setText(String.format("Round %d - Player 2's Turn: Recipe Selected", 
                        gameStateModel.getCurrentRound()));
            } else {
                statusLabel.setText(String.format("Round %d - Player 2's Turn: Select a Recipe", 
                        gameStateModel.getCurrentRound()));
            }
        } else {
            statusLabel.setText(String.format("Round %d - Player 1's Turn: Scan Products", 
                    gameStateModel.getCurrentRound()));
        }
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof GameStateModel) {
            updateComponentStates();
        } else if (evt.getSource() instanceof RecipeModel && 
                RecipeModel.PROP_AVAILABLE_RECIPES.equals(evt.getPropertyName())) {
            updateRecipeList();
        } else if (evt.getSource() instanceof RecipeModel && 
                RecipeModel.PROP_SELECTED_RECIPE.equals(evt.getPropertyName())) {
            updateComponentStates();
        }
    }
}

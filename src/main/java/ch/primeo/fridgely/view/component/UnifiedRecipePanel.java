package ch.primeo.fridgely.view.component;

import ch.primeo.fridgely.model.RecipeModel;
import ch.primeo.fridgely.controller.multiplayer.MultiplayerGameController;
import ch.primeo.fridgely.model.Product;
import ch.primeo.fridgely.model.Recipe;
import ch.primeo.fridgely.service.ProductRepository;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import ch.primeo.fridgely.util.ImageLoader;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A unified panel that displays recipes with their ingredients inline. This combines the recipe list and ingredient
 * details into a single view with lazy loading for improved performance.
 */
public class UnifiedRecipePanel extends JPanel {

    private final int INGREDIENT_ICON_SIZE = 75; // Size of ingredient icons
    private final MultiplayerGameController gameController;
    private final RecipeModel recipeModel;
    private final ProductRepository productRepository;
    private final Color availableColor = new Color(75, 181, 67);  // Green for available ingredients
    private final Color missingColor = new Color(204, 51, 51);    // Red for missing ingredients

    // View components
    private final JPanel recipesViewport;  // Contains the visible recipe cards
    private final JScrollPane scrollPane;  // Main scroll container

    // Recipe data
    private List<Recipe> possibleRecipes;
    private Recipe selectedRecipe;

    // Lazy loading support
    private final Map<Recipe, JPanel> loadedRecipeCards;

    private final ImageLoader imageLoader;

    //Y-coordinate for mouse dragging/touchscrolling
    private int lastY;

    private final AppLocalizationService localizationService;


    /**
     * Constructs a new unified recipe panel.
     *
     * @param controller  the game controller
     * @param productRepo the repository for product data
     * @param imageLoader the image loader for loading images
     */
    public UnifiedRecipePanel(MultiplayerGameController controller, ProductRepository productRepo, ImageLoader imageLoader, AppLocalizationService localizationService) {
        this.gameController = controller;
        this.recipeModel = gameController.getRecipeModel();
        this.productRepository = productRepo;
        this.loadedRecipeCards = new HashMap<>();
        this.imageLoader = imageLoader;
        this.localizationService = localizationService;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a panel to hold the visible recipe cards
        recipesViewport = new JPanel();
        recipesViewport.setLayout(new BoxLayout(recipesViewport, BoxLayout.Y_AXIS));

        // Add scrolling to handle many recipes
        scrollPane = new JScrollPane(recipesViewport);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.setPreferredSize(new Dimension(20, scrollBar.getPreferredSize().height));
        scrollPane.setVerticalScrollBar(scrollBar);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Implement lazy loading when scrolling
        scrollPane.getViewport().addChangeListener(e -> SwingUtilities.invokeLater(this::checkVisibleRecipes));

        // Add mouse dragging support for touch devices
        scrollPane.getViewport().addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                JViewport viewport = scrollPane.getViewport();
                Point viewPosition = viewport.getViewPosition();
                int deltaY = lastY - e.getY(); // Unterschied zur letzten Y-Position
                viewPosition.translate(0, deltaY);

                // Begrenze die Scroll-Position
                int maxY = recipesViewport.getHeight() - viewport.getHeight();
                viewPosition.y = Math.max(0, Math.min(viewPosition.y, maxY));

                viewport.setViewPosition(viewPosition);
                lastY = e.getY(); // Letzte Y-Position aktualisieren
            }
        });

        // Add mouse press to start dragging
        scrollPane.getViewport().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                lastY = e.getY(); // Startpunkt für Dragging setzen
            }
        });


        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Listener interface for recipe selection.
     */
    public interface RecipeSelectionListener {
        void recipeSelected(Recipe recipe);
    }

    private RecipeSelectionListener recipeSelectionListener;



    /**
     * Sets the listener for recipe selection.
     *
     * @param listener the listener to set
     */
    public void setRecipeSelectionListener(RecipeSelectionListener listener) {
        this.recipeSelectionListener = listener;
    }
    /**
     * Updates the recipe list with the current recipes from the model. This clears and reloads the entire list.
     */
    public void updateRecipeList() {
        // Get fresh recipe data and randomize
        List<Product> productsInStorage = gameController.getFridgeStockModel().getProducts();
        possibleRecipes = new ArrayList<>(recipeModel.getPossibleRecipes(productsInStorage));
        Collections.shuffle(possibleRecipes);  // Randomize recipe order
        updateRecipeList(possibleRecipes);
    }

    public void updateRecipeList(List<Recipe> recipes) {
        // Clear old data
        recipesViewport.removeAll();
        loadedRecipeCards.clear();

        // Create placeholder panels for each recipe
        for (Recipe recipe : recipes) {
            JPanel placeholder = createRecipePlaceholder(recipe);
            recipesViewport.add(placeholder);
            recipesViewport.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // Reset selection
        selectedRecipe = null;

        // Update UI
        revalidate();
        repaint();

        // Check visible recipes after layout is complete
        SwingUtilities.invokeLater(this::checkVisibleRecipes);
    }


    /**
     * Creates a lightweight placeholder panel for a recipe. This will be replaced with the full card when it becomes
     * visible.
     *
     * @param recipe the recipe to create a placeholder for
     * @return a placeholder panel
     */
    private JPanel createRecipePlaceholder(Recipe recipe) {
        JPanel placeholder = new JPanel(new BorderLayout());
        placeholder.setPreferredSize(new Dimension(500, 150));  // Approximate size of actual card
        placeholder.putClientProperty("recipe", recipe);        // Store reference to recipe
        placeholder.setOpaque(false);
        return placeholder;
    }

    /**
     * Checks which recipes are currently visible and loads their full content. This is called when the viewport changes
     * due to scrolling.
     */
    private void checkVisibleRecipes() {
        Rectangle viewRect = scrollPane.getViewport().getViewRect();
        Component[] components = recipesViewport.getComponents();

        for (Component comp : components) {
            if (!(comp instanceof JPanel panel) || panel.getClientProperty("recipe") == null) {
                continue;  // Skip non-recipe panels (like spacers)
            }

            Recipe recipe = (Recipe) panel.getClientProperty("recipe");

            // Check if this panel is in the visible area
            Rectangle panelRect = panel.getBounds();
            boolean isVisible = viewRect.intersects(panelRect);

            // Load full content if visible and not already loaded
            if (isVisible && !loadedRecipeCards.containsKey(recipe)) {
                JPanel fullCard = createRecipeCard(recipe);
                loadedRecipeCards.put(recipe, fullCard);

                // Replace placeholder with full card
                int index = Arrays.asList(components).indexOf(panel);
                if (index >= 0 && index < components.length) {
                    recipesViewport.remove(index);
                    recipesViewport.add(fullCard, index);

                    // Apply selection if this is the selected recipe
                    if (recipe.equals(selectedRecipe)) {
                        highlightSelectedCard(fullCard);
                    }
                }
            }
        }

        recipesViewport.revalidate();
        recipesViewport.repaint();
    }

    /**
     * Creates a card-like panel for a single recipe with its ingredients.
     *
     * @param recipe the recipe to create a card for
     * @return a panel containing the recipe information and ingredients
     */
    private JPanel createRecipeCard(Recipe recipe) {
        JPanel card = new JPanel(new BorderLayout(5, 10));
        card.putClientProperty("recipe", recipe);  // Store reference to recipe
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // Create header panel with recipe name (no select button)
        JPanel headerPanel = new JPanel(new BorderLayout(5, 0));

        // Recipe title
        JLabel titleLabel = new JLabel(recipe.getName(localizationService.getLanguage()));
        headerPanel.add(titleLabel, BorderLayout.CENTER);


        // Create ingredients panel
        JPanel ingredientsPanel = createIngredientsPanel(recipe);


        // Assemble the card
        JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
        contentPanel.setOpaque(false);
        contentPanel.add(ingredientsPanel, BorderLayout.CENTER);

        card.add(headerPanel, BorderLayout.NORTH);
        card.add(contentPanel, BorderLayout.CENTER);

        // Add click behavior to select this recipe
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                setSelectedRecipe(recipe);
                if (recipeSelectionListener != null) {
                    recipeSelectionListener.recipeSelected(recipe);
                }
            }
        });

        // Add scroll behavior to the card
        card.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                scrollPane.getViewport().dispatchEvent(SwingUtilities.convertMouseEvent(card, e, scrollPane.getViewport()));
            }
        });
        // Add mouse press to start dragging
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                scrollPane.getViewport().dispatchEvent(SwingUtilities.convertMouseEvent(card, e, scrollPane.getViewport()));
            }
        });


        return card;
    }

    /**
     * Creates a panel showing all ingredients for a recipe, with availability status.
     *
     * @param recipe the recipe to show ingredients for
     * @return a panel containing the ingredient information
     */
    private JPanel createIngredientsPanel(Recipe recipe) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        // Get ingredients for the recipe
        List<Product> productsInRecipe = recipe.getProducts();

        //Load product image
        for(Product product : productsInRecipe) {
            // Load the image icon for the ingredient
            ImageIcon icon = getProductImageIcon(product.getBarcode());

            JLabel imageLabel = new JLabel(icon);
            imageLabel.setPreferredSize(new Dimension(INGREDIENT_ICON_SIZE, INGREDIENT_ICON_SIZE));

            panel.add(imageLabel);
        }

        return panel;
    }


    /**
     * Gets an image icon for a product barcode, with caching.
     *
     * @param barcode the product barcode
     * @return an image icon for the product
     */
    private ImageIcon getProductImageIcon(String barcode) {
        // Try to load from product images folder
        ImageIcon icon = imageLoader.loadScaledImage("/ch/primeo/fridgely/productimages/" + barcode + ".png", INGREDIENT_ICON_SIZE, INGREDIENT_ICON_SIZE);

        if (icon == null) {
            // If not found, try the default image
            icon = imageLoader.loadScaledImage("/ch/primeo/fridgely/productimages/notfound.png", INGREDIENT_ICON_SIZE, INGREDIENT_ICON_SIZE);
        }

        return icon;
    }

    /**
     * Sets the selected recipe and updates the UI to reflect the selection.
     *
     * @param recipe the recipe to select
     */
    public void setSelectedRecipe(Recipe recipe) {
        if (Objects.equals(this.selectedRecipe, recipe)) {
            // Already selected, do nothing
            return;
        }
        Recipe oldSelection = this.selectedRecipe;
        this.selectedRecipe = recipe;

        // Update UI for previously selected card if loaded
        if (oldSelection != null && loadedRecipeCards.containsKey(oldSelection)) {
            JPanel oldCard = loadedRecipeCards.get(oldSelection);
            oldCard.setBackground(UIManager.getColor("Panel.background"));
            oldCard.setBorder(
                    BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
                            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        }

        // Update UI for newly selected card if loaded
        if (recipe != null && loadedRecipeCards.containsKey(recipe)) {
            JPanel newCard = loadedRecipeCards.get(recipe);
            highlightSelectedCard(newCard);
        }
    }

    /**
     * Highlights a recipe card to show it's selected.
     *
     * @param card the card to highlight
     */
    private void highlightSelectedCard(JPanel card) {
        //card.setBackground(new Color(230, 240, 255));  // Light blue for selection
        card.setBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(100, 149, 237), 2, true),
                        // Cornflower blue
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
    }

    /**
     * Gets the currently selected recipe.
     *
     * @return the selected recipe or null if none is selected
     */
    public Recipe getSelectedRecipe() {
        return selectedRecipe;
    }
}

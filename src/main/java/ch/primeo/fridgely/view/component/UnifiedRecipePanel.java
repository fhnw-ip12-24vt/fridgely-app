package ch.primeo.fridgely.view.component;

import ch.primeo.fridgely.model.RecipeModel;
import ch.primeo.fridgely.controller.multiplayer.MultiplayerGameController;
import ch.primeo.fridgely.model.Product;
import ch.primeo.fridgely.model.Recipe;
import ch.primeo.fridgely.service.ProductRepository;
import ch.primeo.fridgely.util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * A unified panel that displays recipes with their ingredients inline.
 * This combines the recipe list and ingredient details into a single view
 * with lazy loading for improved performance.
 */
public class UnifiedRecipePanel extends JPanel {
    
    private final MultiplayerGameController gameController;
    private final RecipeModel recipeModel;
    private final ProductRepository productRepository;
    private final Map<String, ImageIcon> imageCache;
    private final Color availableColor = new Color(75, 181, 67);  // Green for available ingredients
    private final Color missingColor = new Color(204, 51, 51);    // Red for missing ingredients
    
    // View components
    private final JPanel recipesViewport;  // Contains the visible recipe cards
    private final JScrollPane scrollPane;  // Main scroll container
    
    // Recipe data
    private List<Recipe> allRecipes;
    private Recipe selectedRecipe;
    
    // Lazy loading support
    private final Map<Recipe, JPanel> loadedRecipeCards;
    
    /**
     * Constructs a new unified recipe panel.
     * 
     * @param gameController the game controller
     */
    public UnifiedRecipePanel(MultiplayerGameController gameController, ProductRepository productRepository) {
        this.gameController = gameController;
        this.recipeModel = gameController.getRecipeModel();
        this.productRepository = productRepository;
        this.loadedRecipeCards = new HashMap<>();
        this.imageCache = new HashMap<>();
        this.allRecipes = new ArrayList<>();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create a panel to hold the visible recipe cards
        recipesViewport = new JPanel();
        recipesViewport.setLayout(new BoxLayout(recipesViewport, BoxLayout.Y_AXIS));
        
        // Add scrolling to handle many recipes
        scrollPane = new JScrollPane(recipesViewport);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Implement lazy loading when scrolling
        scrollPane.getViewport().addChangeListener(e -> SwingUtilities.invokeLater(this::checkVisibleRecipes));
        
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
     * Updates the recipe list with the current recipes from the model.
     * This clears and reloads the entire list.
     */
    public void updateRecipeList() {
        // Clear old data
        recipesViewport.removeAll();
        loadedRecipeCards.clear();
        
        // Get fresh recipe data and randomize
        allRecipes = new ArrayList<>(recipeModel.getAvailableRecipes());
        Collections.shuffle(allRecipes);  // Randomize recipe order
        
        // Create placeholder panels for each recipe
        for (Recipe recipe : allRecipes) {
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
     * Creates a lightweight placeholder panel for a recipe.
     * This will be replaced with the full card when it becomes visible.
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
     * Checks which recipes are currently visible and loads their full content.
     * This is called when the viewport changes due to scrolling.
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
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        // Create header panel with recipe name (no select button)
        JPanel headerPanel = new JPanel(new BorderLayout(5, 0));
        
        // Recipe title
        JLabel titleLabel = new JLabel(recipe.getName());
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 16));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Description
        JLabel descriptionLabel = new JLabel(recipe.getDescription());
        descriptionLabel.setForeground(Color.DARK_GRAY);
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        
        // Create ingredients panel
        JPanel ingredientsPanel = createIngredientsPanel(recipe);
        ingredientsPanel.setBorder(BorderFactory.createTitledBorder("Ingredients"));
        
        // Assemble the card
        JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
        contentPanel.setOpaque(false);
        contentPanel.add(descriptionLabel, BorderLayout.NORTH);
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
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // Get ingredients for the recipe
        List<String> ingredientBarcodes = recipeModel.getRecipeIngredientBarcodes(recipe);
        List<Product> fridgeProducts = gameController.getFridgeStockModel().getProducts();
        
        // Map of barcode to products for quick lookup
        Map<String, Product> productMap = new HashMap<>();
        for (Product product : fridgeProducts) {
            productMap.put(product.getBarcode(), product);
        }
        
        // Calculate matching stats
        int matchingCount = recipeModel.getMatchingIngredientsCount(recipe, fridgeProducts);
        int totalCount = ingredientBarcodes.size();
        
        // Add matching ingredients info at the top
        JLabel matchingLabel = new JLabel(
                String.format("Matching ingredients: %d/%d", matchingCount, totalCount));
        matchingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        matchingLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));
        panel.add(matchingLabel);
        
        // Add each ingredient
        for (String barcode : ingredientBarcodes) {
            // Get product from fridge if available or look up in repository
            Product product;
            boolean isAvailable;
            if (productMap.containsKey(barcode)) {
                product = productMap.get(barcode);
                isAvailable = true;
            } else {
                // Try to look up the product in the repository
                product = productRepository.getProductByBarcode(barcode);
                if (product == null) {
                    product = new Product();
                    product.setBarcode(barcode);
                    product.setName("Unknown: " + barcode);
                }
                isAvailable = false;
            }
            JPanel ingredientPanel = createIngredientItemPanel(barcode, product, isAvailable);
            ingredientPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(ingredientPanel);
            
            // Add a small gap between ingredients
            panel.add(Box.createRigidArea(new Dimension(0, 3)));
        }
        
        return panel;
    }
    
    /**
     * Creates a panel for a single ingredient item.
     * 
     * @param barcode the ingredient barcode
     * @param product the product object (real or placeholder)
     * @param isAvailable whether the ingredient is available in the fridge
     * @return a panel representing the ingredient
     */
    private JPanel createIngredientItemPanel(String barcode, Product product, boolean isAvailable) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        
        // Load the image icon for the ingredient
        ImageIcon icon = getProductImageIcon(barcode);
        
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setPreferredSize(new Dimension(24, 24));
        
        // Get the product name
        JLabel nameLabel = new JLabel(product.getName());
        
        // Show availability status
        JLabel statusLabel = new JLabel(isAvailable ? "✓" : "✗");
        statusLabel.setForeground(isAvailable ? availableColor : missingColor);
        statusLabel.setFont(new Font(statusLabel.getFont().getName(), Font.BOLD, 14));
        
        panel.add(imageLabel, BorderLayout.WEST);
        panel.add(nameLabel, BorderLayout.CENTER);
        panel.add(statusLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Gets an image icon for a product barcode, with caching.
     * 
     * @param barcode the product barcode
     * @return an image icon for the product
     */
    private ImageIcon getProductImageIcon(String barcode) {
        if (imageCache.containsKey(barcode)) {
            return imageCache.get(barcode);
        }
        
        try {
            // Try to load from product images folder
            ImageIcon icon = ImageLoader.loadImage("/ch/primeo/fridgely/productimages/" + barcode + ".png", getClass());
            
            // Scale the icon to a reasonable size
            Image scaledImage = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            
            imageCache.put(barcode, scaledIcon);
            return scaledIcon;
        } catch (Exception e) {
            // If image not found, return a placeholder
            try {
                ImageIcon icon = ImageLoader.loadImage("/ch/primeo/fridgely/productimages/notfound.png", getClass());
                Image scaledImage = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                
                imageCache.put(barcode, scaledIcon);
                return scaledIcon;
            } catch (Exception ex) {
                // If placeholder not found, create a simple colored box
                BufferedImage img = new BufferedImage(24, 24, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = img.createGraphics();
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fillRect(0, 0, 24, 24);
                g2d.dispose();
                
                ImageIcon icon = new ImageIcon(img);
                imageCache.put(barcode, icon);
                return icon;
            }
        }
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
            oldCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
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
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2, true),  // Cornflower blue
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

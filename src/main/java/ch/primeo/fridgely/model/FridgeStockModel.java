package ch.primeo.fridgely.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Model for the fridge stock in the multiplayer game mode.
 * Holds the products scanned by Player 1 and notifies listeners of changes.
 */
public class FridgeStockModel {
    
    /**
     * Property name for changes in the fridge stock.
     */
    public static final String PROP_FRIDGE_CONTENTS = "fridgeContents";

    private final List<Product> fridgeProducts;
    private final List<Product> defaultProducts;
    private final PropertyChangeSupport propertyChangeSupport;
    
    /**
     * Constructs a new empty fridge stock model.
     */
    public FridgeStockModel(ProductRepository productRepository) {
        this.defaultProducts = productRepository.getAllDefaultProducts();
        this.fridgeProducts = new ArrayList<>();
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }
    
    /**
     * Adds a product to the fridge stock.
     * 
     * @param product the product to add
     * @return true if the product was added, false if it was already in the stock
     */
    public boolean addProduct(Product product) {
        if (product == null || fridgeProducts.contains(product)) {
            return false;
        }
        
        List<Product> oldProducts = new ArrayList<>(fridgeProducts);
        fridgeProducts.add(product);
        propertyChangeSupport.firePropertyChange(PROP_FRIDGE_CONTENTS, oldProducts, fridgeProducts);
        return true;
    }
    
    /**
     * Removes a product from the fridge stock.
     * 
     * @param product the product to remove
     */
    public void removeProduct(Product product) {
        if (product == null || !fridgeProducts.contains(product)) {
            return;
        }
        fridgeProducts.remove(product);
    }
    
    /**
     * Gets an unmodifiable view of the products in the fridge.
     * 
     * @return the products in the fridge
     */
    public List<Product> getProducts() {
        return Stream.concat(fridgeProducts.stream(), defaultProducts.stream()).toList();
    }

    /**
     * Gets an unmodifiable list of the fridge products in the fridge.
     */
    public List<Product> getFridgeProducts() {
        return List.copyOf(fridgeProducts);
    }

    /**
     * Gets an unmodifiable list of the default products in the fridge.
     */
    public List<Product> getDefaultProducts() {
        return List.copyOf(defaultProducts);
    }

    /**
     * Clears all products from the fridge.
     */
    public void clear() {
        if (products.isEmpty()) {
            return;
        }
        
        List<Product> oldProducts = new ArrayList<>(fridgeProducts);
        fridgeProducts.clear();
        propertyChangeSupport.firePropertyChange(PROP_FRIDGE_CONTENTS, oldProducts, new ArrayList<>());
    }
    
    /**
     * Gets the number of products in the fridge.
     * 
     * @return the number of products
     */
    public int getProductCount() {
        return products.size();
    }
    
    /**
     * Adds a property change listener.
     * 
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    
    /**
     * Removes a property change listener.
     * 
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}

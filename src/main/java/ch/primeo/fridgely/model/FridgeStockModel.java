package ch.primeo.fridgely.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * Model for the fridge stock in the multiplayer game mode.
 * Holds the products scanned by Player 1 and notifies listeners of changes.
 */
public class FridgeStockModel {
    
    /**
     * Property name for changes in the fridge stock.
     */
    public static final String PROP_FRIDGE_CONTENTS = "fridgeContents";
    
    private final List<Product> products;
    private final PropertyChangeSupport propertyChangeSupport;
    
    /**
     * Constructs a new empty fridge stock model.
     */
    public FridgeStockModel() {
        this.products = new ArrayList<>();
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }
    
    /**
     * Adds a product to the fridge stock.
     * 
     * @param product the product to add
     * @return true if the product was added, false if it was already in the stock
     */
    public boolean addProduct(Product product) {
        if (product == null || products.contains(product)) {
            return false;
        }
        
        List<Product> oldProducts = new ArrayList<>(products);
        products.add(product);
        propertyChangeSupport.firePropertyChange(PROP_FRIDGE_CONTENTS, oldProducts, new ArrayList<>(products));
        return true;
    }

    /**
     * Adds a list of products to the fridge stock.
     *
     * @param products the list of products to add
     */
    public void addProducts(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return;
        }

        List<Product> oldProducts = new ArrayList<>(this.products);
        this.products.addAll(products);
        propertyChangeSupport.firePropertyChange(PROP_FRIDGE_CONTENTS, oldProducts, new ArrayList<>(this.products));
    }
    
    /**
     * Removes a product from the fridge stock.
     * 
     * @param product the product to remove
     * @return true if the product was removed, false if it wasn't in the stock
     */
    public boolean removeProduct(Product product) {
        if (product == null || !products.contains(product)) {
            return false;
        }
        
        List<Product> oldProducts = new ArrayList<>(products);
        products.remove(product);
        propertyChangeSupport.firePropertyChange(PROP_FRIDGE_CONTENTS, oldProducts, new ArrayList<>(products));
        return true;
    }
    
    /**
     * Gets an unmodifiable view of the products in the fridge.
     * 
     * @return the products in the fridge
     */
    public List<Product> getProducts() {
        return List.copyOf(products);
    }
    
    /**
     * Clears all products from the fridge.
     */
    public void clear() {
        if (products.isEmpty()) {
            return;
        }
        
        List<Product> oldProducts = new ArrayList<>(products);
        products.clear();
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

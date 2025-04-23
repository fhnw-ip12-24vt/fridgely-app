package ch.primeo.fridgely.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import javax.swing.ImageIcon;
import java.util.Objects;

/**
 * Represents a product with localized names, descriptions, and attributes.
 * JPA entity for database persistence.
 */
@Entity
public class Product {
    
    @Id
    private String barcode;
    private String name;
    private String nameDE;
    private String nameFR;
    private String description;
    private String descriptionDE;
    private String descriptionFR;
    private boolean isDefaultProduct;
    private boolean isBio;
    private boolean isLocal;
    
    /**
     * Default constructor required by JPA
     */
    public Product() {
        // Required by JPA/Hibernate
    }
    
    /**
     * Full constructor for all product properties
     */
    public Product(
            String barcode, 
            String name, 
            String nameDE, 
            String nameFR, 
            String description, 
            String descriptionDE, 
            String descriptionFR, 
            boolean isDefaultProduct, 
            boolean isBio, 
            boolean isLocal) {
        this.barcode = barcode;
        this.name = name;
        this.nameDE = nameDE;
        this.nameFR = nameFR;
        this.description = description;
        this.descriptionDE = descriptionDE;
        this.descriptionFR = descriptionFR;
        this.isDefaultProduct = isDefaultProduct;
        this.isBio = isBio;
        this.isLocal = isLocal;
    }

    /**
     * Returns the product name based on the current application language.
     * @param language the language code ("de", "fr", etc.)
     * @return the product name in the specified language or default if not found
     */
    public String getName(String language) {
        return switch (language != null ? language.toLowerCase() : "") {
            case "de" -> nameDE != null ? nameDE : name;
            case "fr" -> nameFR != null ? nameFR : name;
            default -> name;
        };
    }
    
    // Getters and setters
    
    public String getBarcode() {
        return barcode;
    }
    
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getNameDE() {
        return nameDE;
    }
    
    public void setNameDE(String nameDE) {
        this.nameDE = nameDE;
    }
    
    public String getNameFR() {
        return nameFR;
    }
    
    public void setNameFR(String nameFR) {
        this.nameFR = nameFR;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDescriptionDE() {
        return descriptionDE;
    }
    
    public void setDescriptionDE(String descriptionDE) {
        this.descriptionDE = descriptionDE;
    }
    
    public String getDescriptionFR() {
        return descriptionFR;
    }
    
    public void setDescriptionFR(String descriptionFR) {
        this.descriptionFR = descriptionFR;
    }
    
    public boolean isDefaultProduct() {
        return isDefaultProduct;
    }
    
    public void setDefaultProduct(boolean isDefaultProduct) {
        this.isDefaultProduct = isDefaultProduct;
    }
    
    public boolean isBio() {
        return isBio;
    }
    
    public void setBio(boolean isBio) {
        this.isBio = isBio;
    }
    
    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }

    public ImageIcon getProductImage(){
        try {
            return new ImageIcon(Objects.requireNonNull(
                    getClass().getResourceAsStream("/ch/primeo/fridgely/productimages/" + barcode + ".png")).readAllBytes());
        } catch (Exception e) {
            // ignored
        }

        try {
            return new ImageIcon(Objects.requireNonNull(
                    getClass().getResourceAsStream("/ch/primeo/fridgely/productimages/notfound.png")).readAllBytes());
        }catch (Exception e) {
            return null;
        }
    }

    /**
     * Determines if this product is equal to another object.
     * Products are considered equal if they have the same barcode.
     *
     * @param o the object to compare with
     * @return true if the products have the same barcode, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return barcode != null && barcode.equals(product.barcode);
    }
    
    /**
     * Returns a hash code for this product based on its barcode.
     *
     * @return the hash code for this product
     */
    @Override
    public int hashCode() {
        return barcode != null ? barcode.hashCode() : 0;
    }
}

package ch.primeo.fridgely.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Represents a product with localized names, descriptions, and attributes. JPA entity for database persistence.
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

    public static final String PRODUCT_IMAGE_NOT_FOUND_PATH = "/ch/primeo/fridgely/productimages/notfound.png";

    /**
     * Default constructor required by JPA
     */
    public Product() {
        // Required by JPA/Hibernate
    }

    /**
     * Full constructor for all product properties
     *
     * @param code           the product barcode
     * @param nameE          the product name
     * @param nameD          the product name in German
     * @param nameF          the product name in French
     * @param descriptionE   the product description
     * @param descriptionD   the product description in German
     * @param descriptionF   the product description in French
     * @param defaultProduct whether the product is a default product
     * @param bio            whether the product is organic
     * @param local          whether the product is local
     */
    public Product(String code, String nameE, String nameD, String nameF, String descriptionE, String descriptionD,
            String descriptionF, boolean defaultProduct, boolean bio, boolean local) {
        this.barcode = code;
        this.name = nameE;
        this.nameDE = nameD;
        this.nameFR = nameF;
        this.description = descriptionE;
        this.descriptionDE = descriptionD;
        this.descriptionFR = descriptionF;
        this.isDefaultProduct = defaultProduct;
        this.isBio = bio;
        this.isLocal = local;
    }

    /**
     * Returns the product name based on the current application language.
     *
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

    public void setBarcode(String code) {
        this.barcode = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String nameE) {
        this.name = nameE;
    }

    public String getNameDE() {
        return nameDE;
    }

    public void setNameDE(String nameD) {
        this.nameDE = nameD;
    }

    public String getNameFR() {
        return nameFR;
    }

    public void setNameFR(String nameF) {
        this.nameFR = nameF;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descriptionE) {
        this.description = descriptionE;
    }

    public String getDescriptionDE() {
        return descriptionDE;
    }

    public void setDescriptionDE(String descriptionD) {
        this.descriptionDE = descriptionD;
    }

    public String getDescriptionFR() {
        return descriptionFR;
    }

    public void setDescriptionFR(String descriptionF) {
        this.descriptionFR = descriptionF;
    }

    public boolean isDefaultProduct() {
        return isDefaultProduct;
    }

    public void setDefaultProduct(boolean defaultProduct) {
        this.isDefaultProduct = defaultProduct;
    }

    public boolean isBio() {
        return isBio;
    }

    public void setBio(boolean bio) {
        this.isBio = bio;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        this.isLocal = local;
    }

    public String getProductImagePath() {
        if (barcode == null) {
            return "/ch/primeo/fridgely/productimages/notfound.png";
        }

        return "/ch/primeo/fridgely/productimages/" + barcode + ".png";
    }

    /**
     * Determines if this product is equal to another object. Products are considered equal if they have the same
     * barcode.
     *
     * @param o the object to compare with
     * @return true if the products have the same barcode, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

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

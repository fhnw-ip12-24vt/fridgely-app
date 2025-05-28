package ch.primeo.fridgely.model;

import ch.primeo.fridgely.service.localization.AppLocalizationService;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a product with localized names, descriptions, and attributes. JPA entity for database persistence.
 */
@Entity
@Getter
@Setter
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
    private boolean isLowCo2;

    /**
     * Path to the default image when the product image is not found.
     */
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
     * @param lowCo2         whether the product is lowCo2
     */
    public Product(String code, String nameE, String nameD, String nameF, String descriptionE, String descriptionD,
                   String descriptionF, boolean defaultProduct, boolean bio, boolean local, boolean lowCo2) {
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
        this.isLowCo2 = lowCo2;
    }

    /**
     * Returns the product name based on the current application language.
     *
     * @param language the language code ("de", "fr", etc.)
     * @return the product name in the specified language or default if not found
     */
    public String getName(String language) {
        return AppLocalizationService.getLocalizedString(language, name, nameDE, nameFR);
    }

    public String getExplanationKey() {
        String key = "product.";
        key += (isBio) ? "bio." : "non_bio.";

        key += (isLocal) ? "local." : "foreign.";

        switch (description) {
            case "meat" -> key = key + "meat";
            case "water_intensive" -> key = key + "water_intensive";
            case "coffee_belt" -> key = key + "coffee_belt";
            default -> key = key + (isLowCo2 ? "lowCO2" : "highCO2");
        }

        return key;
    }

    public boolean isNotDefaultProduct() {
        return !isDefaultProduct;
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

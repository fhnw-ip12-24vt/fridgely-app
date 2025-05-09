package ch.primeo.fridgely.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Represents an item in the fridge stock, mapping to the 'FridgeStock' table. Uses the product barcode as the primary
 * key.
 */
@Setter
@Getter
@Entity
@Table(name = "FridgeStock")
public class FridgeStock {

    /**
     * The barcode of the product in stock. This is the primary key.
     */
    @Id
    private String barcode;

    /**
     * Default constructor required by JPA.
     */
    public FridgeStock() {
    }

    /**
     * Constructs a new FridgeStock item.
     *
     * @param code The product barcode.
     */
    public FridgeStock(String code) {
        this.barcode = code;
    }

    // toString, equals, hashCode methods can be added if needed
    @Override
    public String toString() {
        return "FridgeStock{" + "barcode='" + barcode + "'" + '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o instanceof FridgeStock fridgeStock) {
            return Objects.equals(barcode, fridgeStock.barcode);
        }

        return false;
    }



    @Override
    public int hashCode() {
        return java.util.Objects.hash(barcode);
    }
}

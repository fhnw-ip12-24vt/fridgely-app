package ch.primeo.fridgely.model;

import ch.primeo.fridgely.service.ProductRepository;
import lombok.Getter;

import java.util.List;

/**
 * Model for recipes in the multiplayer game mode.
 * Contains recipes and functionality to match ingredients to products in the fridge.
 */
@Getter
public class ProductModel {

    private final List<Product> products;

    /**
     * Constructs a new recipe model.
     *
     * @param productRepository the repository for accessing products
     */
    public ProductModel(ProductRepository productRepository) {
        products = productRepository.getAllProducts();
    }

    public List<Product> getDefaultProducts() {
        return products.stream().filter(Product::isDefaultProduct).toList();
    }
}

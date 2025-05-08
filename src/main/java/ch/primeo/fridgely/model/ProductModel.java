package ch.primeo.fridgely.model;

import ch.primeo.fridgely.service.ProductRepository;
import java.util.List;

/**
 * Model for recipes in the multiplayer game mode.
 * Contains recipes and functionality to match ingredients to products in the fridge.
 */
public class ProductModel {

    private final ProductRepository productRepository;
    private final List<Product> products;

    /**
     * Constructs a new recipe model.
     *
     * @param productRepository the repository for accessing products
     */
    public ProductModel(ProductRepository productRepository) {
        this.productRepository = productRepository;
        products = this.productRepository.getAllProducts();
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Product> getDefaultProducts() {
        return products.stream().filter(Product::isDefaultProduct).toList();
    }


}

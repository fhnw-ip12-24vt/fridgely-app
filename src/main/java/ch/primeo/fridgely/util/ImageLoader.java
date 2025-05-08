package ch.primeo.fridgely.util;

import ch.primeo.fridgely.model.PenguinFacialExpression;

import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Utility class for loading and caching images to improve performance.
 * Provides methods for loading images as ImageIcons and BufferedImages,
 * with caching mechanisms to avoid redundant loading and scaling operations.
 */
@Component
@Scope("singleton")
public class ImageLoader {

    private final ResourceLoader resourceLoader;

    private final ConcurrentMap<String, ImageIcon> imageCache = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, ImageIcon> scaledImageCache = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, BufferedImage> bufferedImageCache = new ConcurrentHashMap<>();

    public ImageLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Loads an image as an ImageIcon from the given path.
     * The loaded image is cached for future use.
     *
     * @param imagePath the path to the image resource
     * @return the loaded ImageIcon, or null if the image is not found or an error occurs
     */
    public ImageIcon loadImage(String imagePath) {
        return imageCache.computeIfAbsent(imagePath, path -> {
            try {
                Resource resource = resourceLoader.getResource("classpath:" + path);
                return new ImageIcon(resource.getInputStream().readAllBytes());
            } catch (IOException e) {
                System.err.println("Error loading image: " + path);
                return null;
            }
        });
    }

    /**
     * Loads and scales an image to the specified width and height.
     * The scaled image is cached for future use.
     *
     * @param imagePath the path to the image resource
     * @param width the desired width of the scaled image
     * @param height the desired height of the scaled image
     * @return the scaled ImageIcon, or null if the image is not found or an error occurs
     */
    public ImageIcon loadScaledImage(String imagePath, int width, int height) {
        String cacheKey = imagePath + "_" + width + "x" + height;
        return scaledImageCache.computeIfAbsent(cacheKey, key -> {
            ImageIcon original = loadImage(imagePath);
            if (original == null) {
                return null;
            }
            Image scaledImage = original.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        });
    }

    /**
     * Loads a BufferedImage from the given resource path.
     * The loaded BufferedImage is cached for future use.
     *
     * @param resourcePath the path to the image resource
     * @return the loaded BufferedImage, or null if the image is not found or an error occurs
     */
    public BufferedImage loadBufferedImage(String resourcePath) {
        return bufferedImageCache.computeIfAbsent(resourcePath, path -> {
            try {
                Resource resource = resourceLoader.getResource("classpath:" + path);
                return ImageIO.read(resource.getInputStream());
            } catch (IOException e) {
                System.err.println("Error loading buffered image: " + resourcePath);
                return null;
            }
        });
    }

    /**
     * Preloads multiple images into the cache.
     *
     * @param imagePaths array of image resource paths to preload
     */
    public void preloadImages(String[] imagePaths) {
        for (String path : imagePaths) {
            loadImage(path);
        }
    }

    /**
     * Preloads multiple scaled images into the cache.
     *
     * @param imagePaths array of image resource paths to preload
     * @param width target width for scaling
     * @param height target height for scaling
     */
    public void preloadScaledImages(String[] imagePaths, int width, int height) {
        for (String path : imagePaths) {
            loadScaledImage(path, width, height);
        }
    }

    /**
     * Clears all cached images.
     */
    public void clearCache() {
        imageCache.clear();
        scaledImageCache.clear();
        bufferedImageCache.clear();
    }

    /**
     * Preloads all application images including:
     * - Penguin facial expressions
     * - Common UI elements
     */
    public void preloadAllImages() {
        String[] penguinExpressions = {
                PenguinFacialExpression.HAPPY.getSprite(),
                PenguinFacialExpression.NEUTRAL.getSprite(),
                PenguinFacialExpression.ALERT.getSprite(),
                PenguinFacialExpression.ANGRY.getSprite(),
                PenguinFacialExpression.CRITICAL.getSprite()
        };

        preloadScaledImages(penguinExpressions, 300, 300);

        // product sprites are under classpath:ch/primeo/fridgely/productimages/
        // get all .png files in the directory with ResourceLoader

        String[] productSprites;

        try {
            Resource[] resources = resolveProductImageResources();

            productSprites = Arrays.stream(resources)
                    .map(resource -> "/ch/primeo/fridgely/productimages/" + resource.getFilename())
                    .toArray(String[]::new);
        } catch (IOException e) {
            productSprites = new String[0];
        }

        preloadScaledImages(productSprites, 48, 48);

        String[] uiElements = {
                "/ch/primeo/fridgely/vectors/dialog_arrow_up.png",
                "/ch/primeo/fridgely/vectors/dialog_arrow_down.png",
                "/ch/primeo/fridgely/sprites/fridge_interior.png"
        };

        preloadImages(uiElements);
    }

    // This method is added for testing purposes apart from the main logic.
    protected Resource[] resolveProductImageResources() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        return resolver.getResources("classpath:/ch/primeo/fridgely/productimages/*.png");
    }
}

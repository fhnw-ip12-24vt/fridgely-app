package ch.primeo.fridgely.util;

import ch.primeo.fridgely.Fridgely;
import ch.primeo.fridgely.model.PenguinFacialExpression;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Utility class for loading and caching images to improve performance.
 * Provides methods for loading images as ImageIcons and BufferedImages,
 * with caching mechanisms to avoid redundant loading and scaling operations.
 */
public final class ImageLoader {
    /**
     * Cache for original size ImageIcons, keyed by resource path.
     */
    private static final Map<String, ImageIcon> IMAGE_CACHE = new HashMap<>();

    /**
     * Cache for scaled ImageIcons, keyed by resource path + dimensions (e.g. "path_100x100").
     */
    private static final Map<String, ImageIcon> SCALED_IMAGE_CACHE = new HashMap<>();

    /**
     * Cache for BufferedImages, keyed by resource path.
     */
    private static final Map<String, BufferedImage> BUFFERED_IMAGE_CACHE = new HashMap<>();

    /**
     * Private constructor to prevent instantiation of this utility class.
     * @throws UnsupportedOperationException always, since this class is not meant to be instantiated
     */
    private ImageLoader() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Loads an image as an ImageIcon from the given path and class context.
     * The loaded image is cached for future use.
     *
     * @param imagePath the path to the image resource
     * @param clazz the class for resource loading (used to access resources)
     * @return the loaded ImageIcon, or null if the image is not found or an error occurs
     */
    public static ImageIcon loadImage(String imagePath, Class<?> clazz) {
        return IMAGE_CACHE.computeIfAbsent(imagePath, path -> {
            try {

                return new ImageIcon(Objects.requireNonNull(clazz.getResourceAsStream(path))
                        .readAllBytes());
            } catch (Exception e) {
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
     * @param clazz the class for resource loading
     * @param width the desired width of the scaled image
     * @param height the desired height of the scaled image
     * @return the scaled ImageIcon, or null if the image is not found or an error occurs
     */
    public static ImageIcon loadScaledImage(String imagePath, Class<?> clazz, int width, int height) {
        String cacheKey = imagePath + "_" + width + "x" + height;
        return SCALED_IMAGE_CACHE.computeIfAbsent(cacheKey, key -> {
            ImageIcon original = loadImage(imagePath, clazz);

            if (original == null) {
                return null;
            }
            
            Image scaledImage = original.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

            return new ImageIcon(scaledImage);
        });
    }

    /**
     * Loads and scales an image using the class loader, returning an ImageIcon.
     * This method is a convenience wrapper for {@link #loadScaledImage(String, Class, int, int)}.
     *
     * @param resourcePath the path to the image resource
     * @param clazz the class for resource loading
     * @param width the desired width of the scaled image
     * @param height the desired height of the scaled image
     * @return the scaled ImageIcon, or null if the image is not found or an error occurs
     */
    public static ImageIcon loadScaledImageIconWithClassLoader(String resourcePath, Class<?> clazz,
            int width, int height) {
        return loadScaledImage(resourcePath, clazz, width, height);
    }

    /**
     * Loads a BufferedImage from the given resource path and class context.
     * The loaded BufferedImage is cached for future use.
     *
     * @param resourcePath the path to the image resource
     * @param clazz the class for resource loading
     * @return the loaded BufferedImage, or null if the image is not found or an error occurs
     */
    public static BufferedImage loadBufferedImage(String resourcePath, Class<?> clazz) {
        // Check if the image is already in the cache
        BufferedImage cachedImage = BUFFERED_IMAGE_CACHE.get(resourcePath);
        if (cachedImage != null) {
            return cachedImage;
        }

        try (InputStream is = clazz.getResourceAsStream(resourcePath)) {
            if (is != null) {
                BufferedImage image = javax.imageio.ImageIO.read(is);
                BUFFERED_IMAGE_CACHE.put(resourcePath, image); // Add the image to the cache

                return image;
            }
        } catch (IOException e) {
            System.err.println("Error loading buffered image: " + resourcePath);
        }
        return null;
    }

    /**
     * Preloads multiple images into the cache.
     * This method is used to improve performance by loading commonly used images at startup.
     *
     * @param imagePaths array of image resource paths to preload
     * @throws RuntimeException if any image fails to load, indicating a critical error
     */
    private static void preloadImages(String[] imagePaths) {
        try {
            for (String path : imagePaths) {
                try {
                    ImageIcon icon = new ImageIcon(Objects.requireNonNull(Fridgely.class.getResourceAsStream(path))
                            .readAllBytes());
                    IMAGE_CACHE.put(path, icon);
                } catch (Exception e) {
                    System.err.println("Error preloading image: " + path);
                    throw new RuntimeException("Failed to preload image: " + path, e);
                }
            }
        } catch (Exception e) {
            System.err.println("Error preloading images: " + e.getMessage());
            throw new RuntimeException("Failed to preload images", e);
        }
    }

    /**
     * Preloads multiple scaled images into the cache.
     * This method is used to improve performance by loading commonly used scaled images at startup.
     *
     * @param imagePaths array of image resource paths to preload
     * @param width target width for scaling
     * @param height target height for scaling
     * @throws RuntimeException if any image fails to load, indicating a critical error
     */
    private static void preloadScaledImages(String[] imagePaths, int width, int height) {
        try {
            for (String path : imagePaths) {
                try {
                    ImageIcon original = new ImageIcon(Objects.requireNonNull(Fridgely.class.getResourceAsStream(path))
                            .readAllBytes());
                    Image scaledImage = original.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    String cacheKey = path + "_" + width + "x" + height;
                    SCALED_IMAGE_CACHE.put(cacheKey, new ImageIcon(scaledImage));
                } catch (Exception e) {
                    System.err.println("Error preloading scaled image: " + path);
                    throw new RuntimeException("Failed to preload scaled image: " + path, e);
                }
            }
        } catch (Exception e) {
            System.err.println("Error preloading scaled images: " + e.getMessage());
            throw new RuntimeException("Failed to preload scaled images", e);
        }
    }

    /**
     * Loads a product icon with caching. If the image is missing, uses the fallback.
     * This method attempts to load the image from the specified path. If the image is not found
     * or an error occurs, it falls back to loading the image from the fallback path.
     *
     * @param imagePath the path to the image resource
     * @param fallbackPath the path to the fallback image resource
     * @param clazz the class for resource loading
     * @param width target width for scaling
     * @param height target height for scaling
     * @return the scaled ImageIcon, or the scaled fallback ImageIcon if the primary image fails to load
     */
    public static ImageIcon getProductIcon(String imagePath, String fallbackPath, Class<?> clazz,
            int width, int height) {
        String cacheKey = imagePath + "_" + width + "x" + height;
        if (SCALED_IMAGE_CACHE.containsKey(cacheKey)) {
            return SCALED_IMAGE_CACHE.get(cacheKey);
        }

        try {
            ImageIcon scaledIcon = loadAndScaleImage(imagePath, clazz, width, height);
            if (scaledIcon != null) {
                SCALED_IMAGE_CACHE.put(cacheKey, scaledIcon);
                return scaledIcon;
            }
        } catch (Exception e) {
            return orElse(fallbackPath, clazz, width, height);
        }
        return orElse(fallbackPath, clazz, width, height);
    }

    /**
     * Gets a fallback image from cache or loads it if not cached.
     * This method attempts to retrieve the fallback image from the cache. If the image is not
     * cached, it loads the image from the specified path and adds it to the cache.
     *
     * @param fallbackPath path to the fallback image resource
     * @param clazz the class for resource loading
     * @param width target width for scaling
     * @param height target height for scaling
     * @return the scaled fallback ImageIcon, or null if loading fails
     */
    public static ImageIcon orElse(String fallbackPath, Class<?> clazz, int width, int height) {
        String cacheKey = fallbackPath + "_" + width + "x" + height;
        if (SCALED_IMAGE_CACHE.containsKey(cacheKey)) {
            return SCALED_IMAGE_CACHE.get(cacheKey);
        }

        try {
            ImageIcon scaledFallbackIcon = loadAndScaleImage(fallbackPath, clazz, width, height);
            if (scaledFallbackIcon != null) {
                SCALED_IMAGE_CACHE.put(cacheKey, scaledFallbackIcon);
                return scaledFallbackIcon;
            }
        } catch (Exception e) {
            System.err.println("Error loading fallback image: " + fallbackPath);
            return null;
        }
        return null;
    }

    /**
     * Loads and scales an image from the given path.
     *
     * @param imagePath the path to the image resource
     * @param clazz the class for resource loading
     * @param width target width for scaling
     * @param height target height for scaling
     * @return the scaled ImageIcon, or null if loading fails
     */
    private static ImageIcon loadAndScaleImage(String imagePath, Class<?> clazz, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(clazz.getResourceAsStream(imagePath)).readAllBytes());
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (NullPointerException e) {
            // ignore this exception, as it indicates the image is not found
            return null;
        } catch (Exception e) {
            System.err.println("Error loading and scaling image: " + imagePath);
            return null;
        }
    }

    /**
     * Preloads all application images including:
     * - Penguin facial expressions
     * - Product images
     * - Common UI elements
     * Uses optimized sizes for different use cases.
     */
    public static void preloadAllImages() {
        // Preload penguin expressions
        String[] penguinExpressions = {
                PenguinFacialExpression.HAPPY.getSprite(),
                PenguinFacialExpression.NEUTRAL.getSprite(),
                PenguinFacialExpression.ALERT.getSprite(),
                PenguinFacialExpression.ANGRY.getSprite(),
                PenguinFacialExpression.CRITICAL.getSprite()
        };

        ImageLoader.preloadScaledImages(penguinExpressions, 300, 300);

        preloadProductImages();

        // Preload common UI elements
        String[] uiElements = {
            "/ch/primeo/fridgely/vectors/dialog_arrow_up.png",
            "/ch/primeo/fridgely/vectors/dialog_arrow_down.png",
            "/ch/primeo/fridgely/sprites/fridge_interior.png"
        };

        ImageLoader.preloadImages(uiElements);
        ImageLoader.preloadImages(penguinExpressions);
    }

    /**
     * Preloads all product images from the productimages directory.
     * Scans sequentially numbered images (001.png, 002.png, etc.)
     * and loads them in multiple sizes (80x80 and 60x60).
     */
    private static void preloadProductImages() {

        List<String> imagePaths = new ArrayList<>();
        imagePaths.add("/ch/primeo/fridgely/productimages/notfound.png");

        int width = 3;  // Number of digits (e.g., 3 for "000")

        int i = 1;
        while (true) {
            String formatted = String.format("%0" + width + "d", i);
            String name = "/ch/primeo/fridgely/productimages/" + formatted + ".png";

            try (InputStream imgStream = Fridgely.class.getResourceAsStream(name)) {
                if (imgStream == null) {
                    break;
                }

                imagePaths.add(name);

                i++;
            } catch (Exception e) {
                System.err.println("Error loading image: " + name);
                break;
            }
        }

        var imagePathsAsArray = imagePaths.toArray(new String[0]);

        ImageLoader.preloadScaledImages(imagePathsAsArray, 80, 80);
        ImageLoader.preloadScaledImages(imagePathsAsArray, 60, 60);
    }

    /**
     * Clears all cached images.
     * This method is used to release memory and force images to be reloaded from disk.
     */
    public static void clearCache() {
        IMAGE_CACHE.clear();
        SCALED_IMAGE_CACHE.clear();
        BUFFERED_IMAGE_CACHE.clear();
    }
}

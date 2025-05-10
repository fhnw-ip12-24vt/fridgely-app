package ch.primeo.fridgely.util;

import ch.primeo.fridgely.model.PenguinFacialExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ImageLoaderTest {

    private ResourceLoader resourceLoader;
    private ImageLoader imageLoader;
    private byte[] sampleImageBytes; // Added missing variable

    @BeforeEach
    void setUp() throws IOException {
        System.setProperty("java.awt.headless", "true");

        resourceLoader = mock(ResourceLoader.class);
        imageLoader = new ImageLoader(resourceLoader);

        // Create sample image bytes
        BufferedImage testImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics g = testImage.getGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, 10, 10);
        g.dispose();

        // Convert to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(testImage, "png", baos);
        sampleImageBytes = baos.toByteArray();
    }

    @Test
    void testLoadImage_success() throws Exception {
        String imagePath = "test.png";
        byte[] imageData = new byte[] {(byte) 0x89, 0x50, 0x4E, 0x47}; // PNG header bytes

        Resource resource = mock(Resource.class);
        when(resourceLoader.getResource("classpath:" + imagePath)).thenReturn(resource);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream(imageData));

        ImageIcon icon = imageLoader.loadImage(imagePath);

        assertNotNull(icon);
        verify(resourceLoader).getResource("classpath:" + imagePath);
    }

    @Test
    void testLoadImage_failure_returnsNull() throws Exception {
        String imagePath = "missing.png";

        Resource resource = mock(Resource.class);
        when(resourceLoader.getResource("classpath:" + imagePath)).thenReturn(resource);
        when(resource.getInputStream()).thenThrow(new IOException("Not found"));

        ImageIcon icon = imageLoader.loadImage(imagePath);

        assertNull(icon);
    }

    @Test
    void testLoadImage_caching() throws Exception {
        String imagePath = "cached.png";
        byte[] data = new byte[] {(byte) 0x89, 0x50};

        Resource resource = mock(Resource.class);
        when(resourceLoader.getResource("classpath:" + imagePath)).thenReturn(resource);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream(data));

        ImageIcon first = imageLoader.loadImage(imagePath);
        ImageIcon second = imageLoader.loadImage(imagePath);

        assertSame(first, second); // Same instance => cached
        verify(resourceLoader, times(1)).getResource("classpath:" + imagePath);
    }

    @Test
    void testLoadBufferedImage_success() throws Exception {
        String path = "test-buffered.png";

        BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        InputStream is = new ByteArrayInputStream(new byte[10]);

        Resource resource = mock(Resource.class);
        when(resourceLoader.getResource("classpath:" + path)).thenReturn(resource);
        when(resource.getInputStream()).thenReturn(is);

        // Override ImageIO.read to avoid real file reading
        try (var mocked = Mockito.mockStatic(ImageIO.class)) {
            mocked.when(() -> ImageIO.read(is)).thenReturn(dummyImage);

            BufferedImage result = imageLoader.loadBufferedImage(path);

            assertNotNull(result);
            assertSame(dummyImage, result);
        }
    }

    @Test
    void testLoadScaledImage_returnsScaledIcon() throws Exception {
        String path = "scaled.png";
        byte[] imageBytes = new byte[] {(byte) 0x89, 0x50};

        Resource resource = mock(Resource.class);
        when(resourceLoader.getResource("classpath:" + path)).thenReturn(resource);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream(imageBytes));

        ImageIcon scaledIcon = imageLoader.loadScaledImage(path, 100, 100);
        assertNotNull(scaledIcon);
    }

    @Test
    void testClearCache_emptiesAll() throws Exception {
        String path = "toClear.png";
        byte[] data = new byte[] {(byte) 0x89, 0x50};

        Resource resource = mock(Resource.class);
        when(resourceLoader.getResource("classpath:" + path)).thenReturn(resource);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream(data));

        ImageIcon beforeClear = imageLoader.loadImage(path);
        assertNotNull(beforeClear);

        imageLoader.clearCache();

        // Re-mock to simulate image no longer exists
        when(resource.getInputStream()).thenThrow(new IOException("Not found"));

        ImageIcon afterClear = imageLoader.loadImage(path);
        assertNull(afterClear);
    }
/*
    @Test
    void testPreloadAllImages_callsExpectedPreloadMethods() throws Exception {
        ImageLoader imageLoader = spy(new ImageLoader(resourceLoader));

        // Use real enum values
        String[] expectedPenguinSprites =
        {
            PenguinFacialExpression.HAPPY.getSprite(),
            PenguinFacialExpression.NEUTRAL.getSprite(), PenguinFacialExpression.ALERT.getSprite(),
            PenguinFacialExpression.ANGRY.getSprite(), PenguinFacialExpression.CRITICAL.getSprite()
        };

        // Mock resource listing
        Resource mockResource = mock(Resource.class);
        when(mockResource.getFilename()).thenReturn("milk.png");
        doReturn(new Resource[] {mockResource}).when(imageLoader).resolveProductImageResources();

        // Spy preload methods
        doNothing().when(imageLoader).preloadScaledImages(any(), anyInt(), anyInt());
        doNothing().when(imageLoader).preloadImages(any());

        // Act
        imageLoader.preloadAllImages();

        // Verify penguin preload
        verify(imageLoader).preloadScaledImages(
                argThat(arr -> new HashSet<>(List.of(arr)).containsAll(List.of(expectedPenguinSprites))), eq(300),
                eq(300));

        // Verify product image preload
        verify(imageLoader).preloadScaledImages(
                argThat(arr -> List.of(arr).contains("/ch/primeo/fridgely/productimages/milk.png")), eq(48), eq(48));

        // Verify UI elements preload
        verify(imageLoader).preloadImages(
                argThat(arr -> List.of(arr).contains("/ch/primeo/fridgely/vectors/dialog_arrow_up.png")));
    }
*/
    /**
     * Additional test to verify loadScaledImage handles null from loadImage
     */
    @Test
    void loadScaledImage_ShouldHandleNullFromLoadImage() {
        // Arrange - create a spy that returns null from loadImage
        ImageLoader spyLoader = spy(imageLoader);
        doReturn(null).when(spyLoader).loadImage(anyString());

        // Act
        ImageIcon result = spyLoader.loadScaledImage("any/path.png", 10, 10);

        // Assert
        assertNull(result, "Should return null when loadImage returns null");
        verify(spyLoader).loadImage("any/path.png");
    }

    /**
     * Test method for preloadImages that verifies all image paths are loaded
     */
    @Test
    void preloadImages_ShouldLoadAllImages() {
        // Arrange - create a spy to verify loadImage calls
        ImageLoader spyLoader = spy(imageLoader);
        String[] paths = {"path1.png", "path2.png", "path3.png"};

        // Create resources for each path
        Resource mockResource = new ByteArrayResource(sampleImageBytes);
        for (String path : paths) {
            when(resourceLoader.getResource("classpath:" + path)).thenReturn(mockResource);
        }

        // Act
        spyLoader.preloadImages(paths);

        // Assert - verify that loadImage was called for each path
        for (String path : paths) {
            verify(spyLoader).loadImage(path);
        }
    }

    /**
     * Test method for preloadScaledImages that verifies all image paths are loaded with scaling
     */
    @Test
    void preloadScaledImages_ShouldLoadAllImagesWithScaling() {
        // Arrange - create a spy to verify loadScaledImage calls
        ImageLoader spyLoader = spy(imageLoader);
        String[] paths = {"path1.png", "path2.png", "path3.png"};
        int width = 50;
        int height = 50;

        // Create resources for each path
        Resource mockResource = new ByteArrayResource(sampleImageBytes);
        for (String path : paths) {
            when(resourceLoader.getResource("classpath:" + path)).thenReturn(mockResource);
        }

        // Act
        spyLoader.preloadScaledImages(paths, width, height);

        // Assert - verify that loadScaledImage was called for each path with the correct dimensions
        for (String path : paths) {
            verify(spyLoader).loadScaledImage(path, width, height);
        }
    }

    /**
     * Test for resolveProductImageResources method using a simplified approach
     */
    /*
    @Test
    void resolveProductImageResources_BasicFunctionality() {
        // Create a subclass that overrides the method for testing
        ImageLoader testLoader = new ImageLoader(resourceLoader) {
            @Override
            protected Resource[] resolveProductImageResources() {
                // Return test data instead of making actual resolver call
                Resource mockResource1 = mock(Resource.class);
                Resource mockResource2 = mock(Resource.class);
                when(mockResource1.getFilename()).thenReturn("apple.png");
                when(mockResource2.getFilename()).thenReturn("milk.png");

                return new Resource[] {mockResource1, mockResource2};
            }
        };

        try {
            // Act
            Resource[] result = testLoader.resolveProductImageResources();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.length);
            assertEquals("apple.png", result[0].getFilename());
            assertEquals("milk.png", result[1].getFilename());
        } catch (IOException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
*/
    /**
     * Test for exception handling in the integration with resolveProductImageResources
     *//*
    @Test
    void preloadAllImages_WithResolverException() {
        // Create a custom ImageLoader that throws exception in resolveProductImageResources
        ImageLoader errorLoader = new ImageLoader(resourceLoader) {
            @Override
            protected Resource[] resolveProductImageResources() throws IOException {
                throw new IOException("Simulated resolver error");
            }
        };

        // Mock resources for the penguin expressions to avoid NPE
        for (PenguinFacialExpression expression : PenguinFacialExpression.values()) {
            String path = expression.getSprite();
            Resource mockResource = new ByteArrayResource(sampleImageBytes);
            when(resourceLoader.getResource("classpath:" + path)).thenReturn(mockResource);
        }

        // Also mock UI element resources
        String[] uiElements = {
            "/ch/primeo/fridgely/vectors/dialog_arrow_up.png",
            "/ch/primeo/fridgely/vectors/dialog_arrow_down.png",
            "/ch/primeo/fridgely/sprites/fridge_interior.png"
        };

        for (String path : uiElements) {
            Resource mockResource = new ByteArrayResource(sampleImageBytes);
            when(resourceLoader.getResource("classpath:" + path)).thenReturn(mockResource);
        }

        // Create spy to track method calls
        ImageLoader spyLoader = spy(errorLoader);

        // Act - this should not throw an exception despite the error in resolveProductImageResources
        spyLoader.preloadAllImages();

        // Assert - verify the other methods were still called despite the error
        verify(spyLoader).preloadScaledImages(any(String[].class), eq(300), eq(300));
        verify(spyLoader).preloadImages(any(String[].class));

        // Verify the product sprites array was empty
        verify(spyLoader).preloadScaledImages(argThat(arr -> arr.length == 0), eq(48), eq(48));
    }
*/
    /**
     * Test exception handling in loadImage
     */
    @Test
    void loadImage_ShouldHandleIOException() throws IOException {
        // Arrange
        String imagePath = "invalid/path.png";
        Resource mockResource = mock(Resource.class);
        when(resourceLoader.getResource("classpath:" + imagePath)).thenReturn(mockResource);
        when(mockResource.getInputStream()).thenThrow(new IOException("Simulated IO error"));

        // Act
        ImageIcon result = imageLoader.loadImage(imagePath);

        // Assert
        assertNull(result, "Should return null when an IOException occurs");
    }

    /**
     * Test exception handling in loadBufferedImage
     */
    @Test
    void loadBufferedImage_ShouldHandleIOException() throws IOException {
        // Arrange
        String imagePath = "invalid/path.png";
        Resource mockResource = mock(Resource.class);
        when(resourceLoader.getResource("classpath:" + imagePath)).thenReturn(mockResource);
        when(mockResource.getInputStream()).thenThrow(new IOException("Simulated IO error"));

        // Act
        BufferedImage result = imageLoader.loadBufferedImage(imagePath);

        // Assert
        assertNull(result, "Should return null when an IOException occurs");
    }
/*
    @Test
    public void testResolveProductImageResources() throws IOException {
        Resource[] resources = imageLoader.resolveProductImageResources();

        assertNotNull(resources, "Resources should not be null");
        assertTrue(resources.length > 0, "Should load at least one PNG file");

        for (Resource resource : resources) {
            assertTrue(Objects.requireNonNull(resource.getFilename()).endsWith(".png"),
                    "Each resource should be a .png file");

            assertTrue(resource.exists(), "Resource file should exist in the classpath");
        }
    }*/
}

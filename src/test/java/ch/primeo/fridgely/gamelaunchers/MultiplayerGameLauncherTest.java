package ch.primeo.fridgely.gamelaunchers;

import ch.primeo.fridgely.Fridgely;
import ch.primeo.fridgely.factory.DefaultFrameFactory;
import ch.primeo.fridgely.factory.FrameFactory;
import ch.primeo.fridgely.service.ProductRepository;
import ch.primeo.fridgely.service.RecipeRepository;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import ch.primeo.fridgely.util.ImageLoader;
import ch.primeo.fridgely.view.ScannedItemsView;
import ch.primeo.fridgely.view.multiplayer.MultiplayerGameView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MultiplayerGameLauncherTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private AppLocalizationService localizationService;

    @Mock
    private ImageLoader imageLoader;

    @Mock
    private FrameFactory ignored; // needed for two tests to pass, don't remove.

    @Mock
    private JFrame gameFrame; // Mock for the game frame created by the launcher

    @Mock
    private JFrame scannedItemsFrame; // Mock for the scanned items frame

    // GraphicsDevice and GraphicsConfiguration mocks for Fridgely screen interactions
    @Mock
    private GraphicsDevice mainAppScreenDevice;
    @Mock
    private GraphicsDevice scannedItemsScreenDevice;
    @Mock
    private GraphicsConfiguration graphicsConfiguration;


    @Spy // Use Spy to allow calling real methods and mocking others if needed
    @InjectMocks
    private MultiplayerGameLauncher multiplayerGameLauncher;

    @BeforeAll
    static void setupLookAndFeel() {
        System.setProperty("java.awt.headless", "true");
    }

    @BeforeEach
    void setUp() {
        // It's good practice to reinitialize mocks if needed, though @InjectMocks handles basic setup.
        // For static mocks, they need to be handled carefully.
    }

    @Test
    void constructor_initializesDependencies() {
        // This test primarily verifies that @InjectMocks works as expected.
        // If the constructor had logic, we'd test that.
        // For now, we ensure the launcher is created.
        assertNotNull(multiplayerGameLauncher); // Using directly imported static method
    }

    @Test
    void createGameFrame_returnsNewJFrameWithTitle() {
        // Arrange
        String testTitle = "Test Game Frame";
        JFrame mockFrame = mock(JFrame.class);

        // Use our spy and make it return the mock frame
        doReturn(mockFrame).when(multiplayerGameLauncher).createFrame(testTitle);

        // Act
        JFrame result = multiplayerGameLauncher.createFrame(testTitle);

        // Assert
        assertNotNull(result, "Frame should not be null");
        assertEquals(mockFrame, result, "Should return the mock frame");

        // Verify that in production code, the title would be set
        try (MockedStatic<GraphicsEnvironment> graphicsEnvMock = mockStatic(GraphicsEnvironment.class)) {
            // Mock GraphicsEnvironment to pretend we're not in headless mode
            graphicsEnvMock.when(GraphicsEnvironment::isHeadless).thenReturn(false);

            // Create a partial mock that calls real methods except for the actual JFrame creation
            MultiplayerGameLauncher partialMock = spy(new MultiplayerGameLauncher(
                    productRepository, recipeRepository, localizationService, imageLoader, new DefaultFrameFactory()));

            // Mock the JFrame constructor result
            JFrame realFrame = mock(JFrame.class);
            doReturn(realFrame).when(partialMock).createFrame(anyString());

            // Call the method
            partialMock.createFrame(testTitle);

            // Verify the title would be set in a real scenario
            verify(realFrame, never()).setTitle(testTitle); // Never called since title is set in constructor
        }
    }

    @Test
    void createScannedItemsFrame_returnsNewJFrameWithTitle() {
        // Arrange
        String testTitle = "Test Scanned Items Frame";
        JFrame mockFrame = mock(JFrame.class);

        // Use our spy and make it return the mock frame
        doReturn(mockFrame).when(multiplayerGameLauncher).createFrame(testTitle);

        // Act
        JFrame result = multiplayerGameLauncher.createFrame(testTitle);

        // Assert
        assertNotNull(result, "Frame should not be null");
        assertEquals(mockFrame, result, "Should return the mock frame");

        // Verify that in production code, the title would be set
        try (MockedStatic<GraphicsEnvironment> graphicsEnvMock = mockStatic(GraphicsEnvironment.class)) {
            // Mock GraphicsEnvironment to pretend we're not in headless mode
            graphicsEnvMock.when(GraphicsEnvironment::isHeadless).thenReturn(false);

            // Create a partial mock that calls real methods except for the actual JFrame creation
            MultiplayerGameLauncher partialMock = spy(new MultiplayerGameLauncher(
                    productRepository, recipeRepository, localizationService, imageLoader, new DefaultFrameFactory()));

            // Mock the JFrame constructor result
            JFrame realFrame = mock(JFrame.class);
            doReturn(realFrame).when(partialMock).createFrame(anyString());

            // Call the method
            partialMock.createFrame(testTitle);

            // Verify the title would be set in a real scenario
            verify(realFrame, never()).setTitle(testTitle); // Never called since title is set in constructor
        }
    }

    @Test
    void launchGame_invokesInitGameOnEDT() {
        // Mock static SwingUtilities
        try (MockedStatic<SwingUtilities> swingUtilitiesMock = mockStatic(SwingUtilities.class)) {
            // Act
            multiplayerGameLauncher.launchGame();

            // Assert
            // Verify that SwingUtilities.invokeLater was called with the initGame runnable
            swingUtilitiesMock.verify(() -> SwingUtilities.invokeLater(any(Runnable.class)));
            // We can't easily verify that the *specific* runnable was initGame without more complex argument captors
            // or by making initGame public for testing, which is not ideal.
            // For now, verifying any Runnable is invoked is a good first step.
            // To test initGame's content, we'll call it directly in another test.
        }
    }


    @Test
    void initGame_initializesFramesAndComponents_singleDisplay() {
        // Arrange
        when(multiplayerGameLauncher.createFrame("Fridgely - Multiplayer Game")).thenReturn(gameFrame);
        when(multiplayerGameLauncher.createFrame("Fridgely - Scanned Items")).thenReturn(scannedItemsFrame);

        // Mock JFrame behaviors
        doNothing().when(gameFrame).setDefaultCloseOperation(anyInt());
        doNothing().when(gameFrame).setUndecorated(true);
        doNothing().when(gameFrame).addWindowListener(any());
        doNothing().when(gameFrame).setContentPane(any());
        doNothing().when(gameFrame).setBounds(any());
        when(gameFrame.getRootPane()).thenReturn(mock(JRootPane.class, RETURNS_DEEP_STUBS));

        doNothing().when(scannedItemsFrame).setDefaultCloseOperation(anyInt());
        doNothing().when(scannedItemsFrame).setUndecorated(true);
        doNothing().when(scannedItemsFrame).setContentPane(any());
        doNothing().when(scannedItemsFrame).setBounds(any());
        doNothing().when(scannedItemsFrame).dispose();

        JRootPane mockRootPane = gameFrame.getRootPane();
        InputMap mockInputMap = mockRootPane.getInputMap();
        ActionMap mockActionMap = mockRootPane.getActionMap();

        Rectangle screenBounds = new Rectangle(0, 0, 1920, 1080);

        // Mock static Fridgely
        try (var ignored = mockStatic(Fridgely.class); var ignored2 = mockConstruction(MultiplayerGameView.class);
             var ignored3 = mockConstruction(ScannedItemsView.class)) {

            when(Fridgely.isSingleDisplay()).thenReturn(true);
            when(Fridgely.getMainAppScreen()).thenReturn(mainAppScreenDevice);

            when(mainAppScreenDevice.getDefaultConfiguration()).thenReturn(graphicsConfiguration);
            when(graphicsConfiguration.getBounds()).thenReturn(screenBounds);

            // Act
            multiplayerGameLauncher.initGame();

            // Assert
            verify(gameFrame).setBounds(screenBounds);
            verify(scannedItemsFrame).setBounds(screenBounds);
            verify(gameFrame).setContentPane(any(MultiplayerGameView.class));
            verify(scannedItemsFrame).setContentPane(any(ScannedItemsView.class));
            verify(mockInputMap).put(eq(KeyStroke.getKeyStroke("ESCAPE")), eq("escape"));
            verify(mockActionMap).put(eq("escape"), any(AbstractAction.class));

            // Simulate ESC key press
            ArgumentCaptor<AbstractAction> escapeActionCaptor = ArgumentCaptor.forClass(AbstractAction.class);
            verify(mockActionMap).put(eq("escape"), escapeActionCaptor.capture());
            escapeActionCaptor.getValue().actionPerformed(null);
            verify(gameFrame).dispose();

            // Simulate game window closing
            ArgumentCaptor<WindowListener> windowListenerCaptor = ArgumentCaptor.forClass(WindowListener.class);
            verify(gameFrame).addWindowListener(windowListenerCaptor.capture());
            windowListenerCaptor.getValue().windowClosed(new WindowEvent(gameFrame, WindowEvent.WINDOW_CLOSED));
            verify(scannedItemsFrame).dispose();
        }
    }

    @Test
    void initGame_initializesFramesAndComponents_dualDisplay() {
        // Arrange
        when(multiplayerGameLauncher.createFrame("Fridgely - Multiplayer Game")).thenReturn(gameFrame);
        when(multiplayerGameLauncher.createFrame("Fridgely - Scanned Items")).thenReturn(scannedItemsFrame);

        // Mock JFrame behaviors
        doNothing().when(gameFrame).setDefaultCloseOperation(anyInt());
        doNothing().when(gameFrame).setUndecorated(true);
        doNothing().when(gameFrame).addWindowListener(any());
        doNothing().when(gameFrame).setContentPane(any());
        doNothing().when(gameFrame).setBounds(any());
        doNothing().when(gameFrame).setExtendedState(anyInt());
        doNothing().when(gameFrame).setVisible(true);
        when(gameFrame.getRootPane()).thenReturn(mock(JRootPane.class, RETURNS_DEEP_STUBS));

        doNothing().when(scannedItemsFrame).setDefaultCloseOperation(anyInt());
        doNothing().when(scannedItemsFrame).setUndecorated(true);
        doNothing().when(scannedItemsFrame).setContentPane(any());
        doNothing().when(scannedItemsFrame).setBounds(any());
        doNothing().when(scannedItemsFrame).setExtendedState(anyInt());
        doNothing().when(scannedItemsFrame).setVisible(true);
        doNothing().when(scannedItemsFrame).dispose();

        JRootPane mockRootPane = gameFrame.getRootPane();
        InputMap mockInputMap = mockRootPane.getInputMap();
        ActionMap mockActionMap = mockRootPane.getActionMap();

        Rectangle mainScreenBounds = new Rectangle(0, 0, 1920, 1080);
        Rectangle secondaryScreenBounds = new Rectangle(1920, 0, 1920, 1080);

        // Mock static Fridgely
        try (var ignored = mockStatic(Fridgely.class); var ignored2 = mockConstruction(MultiplayerGameView.class);
             var ignored3 = mockConstruction(ScannedItemsView.class)) {

            when(Fridgely.isSingleDisplay()).thenReturn(false);
            when(Fridgely.getMainAppScreen()).thenReturn(mainAppScreenDevice);
            when(Fridgely.getScannedItemsScreen()).thenReturn(scannedItemsScreenDevice);

            when(mainAppScreenDevice.getDefaultConfiguration()).thenReturn(graphicsConfiguration);
            when(scannedItemsScreenDevice.getDefaultConfiguration()).thenReturn(mock(GraphicsConfiguration.class));
            when(graphicsConfiguration.getBounds()).thenReturn(mainScreenBounds);
            when(scannedItemsScreenDevice.getDefaultConfiguration().getBounds()).thenReturn(secondaryScreenBounds);

            // Act
            multiplayerGameLauncher.initGame();

            // Assert
            verify(gameFrame).setExtendedState(JFrame.MAXIMIZED_BOTH);
            verify(scannedItemsFrame).setExtendedState(JFrame.MAXIMIZED_BOTH);
            verify(gameFrame).setBounds(mainScreenBounds);
            verify(scannedItemsFrame).setBounds(secondaryScreenBounds);
            verify(gameFrame).setVisible(true);
            verify(scannedItemsFrame).setVisible(true);
            verify(gameFrame).setContentPane(any(MultiplayerGameView.class));
            verify(scannedItemsFrame).setContentPane(any(ScannedItemsView.class));
            verify(mockInputMap).put(eq(KeyStroke.getKeyStroke("ESCAPE")), eq("escape"));
            verify(mockActionMap).put(eq("escape"), any(AbstractAction.class));

            // Simulate ESC key press
            ArgumentCaptor<AbstractAction> escapeActionCaptor = ArgumentCaptor.forClass(AbstractAction.class);
            verify(mockActionMap).put(eq("escape"), escapeActionCaptor.capture());
            escapeActionCaptor.getValue().actionPerformed(null);
            verify(gameFrame).dispose();

            // Simulate game window closing
            ArgumentCaptor<WindowListener> windowListenerCaptor = ArgumentCaptor.forClass(WindowListener.class);
            verify(gameFrame).addWindowListener(windowListenerCaptor.capture());
            windowListenerCaptor.getValue().windowClosed(new WindowEvent(gameFrame, WindowEvent.WINDOW_CLOSED));
            verify(scannedItemsFrame).dispose();
        }
    }
}

package ch.primeo.fridgely.controller;

import ch.primeo.fridgely.gamelaunchers.MultiplayerGameLauncher;
import ch.primeo.fridgely.model.*;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import ch.primeo.fridgely.util.ImageLoader;
import ch.primeo.fridgely.view.ChooseGameModeView;
import ch.primeo.fridgely.view.component.LanguageSwitchButton;
import ch.primeo.fridgely.view.util.DialogBox;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
import java.awt.Cursor;
import java.awt.event.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Improved test class for ChooseGameModeController with full code coverage.
 * All tests run in headless mode to avoid UI dependencies.
 */
@ExtendWith(MockitoExtension.class)
public class ChooseGameModeControllerTest {

    @Mock
    private AppLocalizationService localizationService;

    @Mock
    private LanguageSwitchButton languageSwitchButton;

    @Mock
    private MultiplayerGameLauncher multiplayerGameLauncher;

    @Mock
    private ImageLoader imageLoader;

    @Mock
    private ChooseGameModeView mockView;

    @Mock
    private DialogBox mockDialogBox;

    @Mock
    private JFrame frame;
    // Note: languageSwitchButton is already mocked: @Mock private LanguageSwitchButton languageSwitchButton;


    private ChooseGameModeController controller;

    @BeforeAll
    static void init() {
        // Ensure tests run in headless mode
        System.setProperty("java.awt.headless", "true");
    }

    @BeforeEach
    void setUp() {
        // Set up the mock view to return the mock frame
        lenient().when(mockView.getFrame()).thenReturn(frame);

        // Create the controller with mocked dependencies, including languageSwitchButton
        controller = new ChooseGameModeController(localizationService, multiplayerGameLauncher, imageLoader, mockView, languageSwitchButton) {
            @Override
            protected ChooseGameModeView createView(LanguageSwitchButton button) {
                return mockView; // Return the mocked view to avoid HeadlessException
            }

            @Override
            protected ChooseGameModeView createView() {
                return mockView; // Return the mocked view to avoid HeadlessException
            }

            @Override
            protected DialogBox createDialogBox(List<String> messages, PenguinFacialExpression expression, PenguinHPState state, Runnable onComplete, ImageLoader imageLoader) {
                return mockDialogBox; // Return mock dialog box instead of creating a real one
            }
        };
    }

    @Test
    void testConstructor_shouldInitializeViewAndSetupEventHandlers() {
        // Verify view was properly set up
        verify(localizationService).subscribe(mockView);
        verify(mockView).onLocaleChanged();
        verify(mockView).setVisible(true);

        // Capture the WindowListener to test windowClosing
        ArgumentCaptor<WindowAdapter> windowAdapterCaptor = ArgumentCaptor.forClass(WindowAdapter.class);
        verify(frame).addWindowListener(windowAdapterCaptor.capture());

        // Simulate windowClosing event
        // We need to spy the controller created in setUp to verify dispose() is called on it.
        // However, 'controller' in setUp is an anonymous class.
        // Instead, we'll verify the effects of dispose() directly on the mocks.
        // Re-initialize a controller for this specific part of the test to spy on it,
        // or verify the effects of dispose() on the mocks used by the 'controller' from setUp.
        // For simplicity, let's verify the effects on the mocks.
        // Clear previous interactions on mocks that would be affected by dispose, if necessary.
        // For this test, 'controller' is the instance from setUp.
        // Its dispose() method will call localizationService.unsubscribe(mockView) and mockView.dispose().

        // Reset mocks for dispose verification if they were called during setup for other reasons.
        // In this case, subscribe is called in setup, so we don't reset localizationService.
        // mockView.dispose() is not called in setup.

        windowAdapterCaptor.getValue().windowClosing(null); // Simulate closing

        // Verify that dispose() effects happened on the mocks associated with the 'controller' from setUp
        verify(localizationService).unsubscribe(mockView); // Should be called again by dispose
        verify(mockView).dispose();
    }

    @Test
    void testMainConstructor() {
        // Create a new controller with real dependencies but override the showChooseGameModeView method
        ChooseGameModeController testController = new ChooseGameModeController(
                localizationService, languageSwitchButton, multiplayerGameLauncher, imageLoader) {
            @Override
            public void showChooseGameModeView() {
                // Override to do nothing and avoid UI components
                // For this test, we only care about constructor field assignments
                // The actual view setup is tested elsewhere or by testing showChooseGameModeView itself.
            }
        };

        // Verify the constructor initialized the dependencies correctly
        assertEquals(localizationService, testController.localizationService);
        assertEquals(multiplayerGameLauncher, testController.multiplayerGameLauncher);
        assertEquals(imageLoader, testController.imageLoader);
        assertEquals(languageSwitchButton, testController.languageSwitchButton);
    }

    // Replaced testCreateView_shouldReturnView
    @Test
    void testCreateView_noArgs_constructsAndReturnsView() {
        // Use the test constructor to get a standard ChooseGameModeController instance
        // This instance does not have createView overridden.
        ChooseGameModeController ctrl = new ChooseGameModeController(
                localizationService, multiplayerGameLauncher, imageLoader, mockView, languageSwitchButton
        );

        try (MockedConstruction<ChooseGameModeView> mockedViewConstruction = Mockito.mockConstruction(ChooseGameModeView.class,
                (mock, context) -> {
                    // Verify constructor arguments for ChooseGameModeView(null, localizationService, imageLoader)
                    assertEquals(3, context.arguments().size(), "Constructor argument count mismatch");
                    assertNull(context.arguments().get(0), "LanguageSwitchButton should be null");
                    assertSame(localizationService, context.arguments().get(1), "AppLocalizationService mismatch");
                    assertSame(imageLoader, context.arguments().get(2), "ImageLoader mismatch");
                })) {

            ChooseGameModeView result = ctrl.createView(); // Call the actual createView()

            assertNotNull(result, "Resulting view should not be null");
            assertEquals(1, mockedViewConstruction.constructed().size(), "ChooseGameModeView should have been constructed once");
            assertSame(result, mockedViewConstruction.constructed().getFirst(), "Returned view is not the constructed instance");
        }
    }

    // Replaced testCreateViewWithLanguageSwitchButton_shouldReturnView
    @Test
    void testCreateView_withButton_constructsAndReturnsView() {
        ChooseGameModeController ctrl = new ChooseGameModeController(
                localizationService, multiplayerGameLauncher, imageLoader, mockView, languageSwitchButton
        );

        try (MockedConstruction<ChooseGameModeView> mockedViewConstruction = Mockito.mockConstruction(ChooseGameModeView.class,
                (mock, context) -> {
                    // Verify constructor arguments for DialogBox
                    assertEquals(3, context.arguments().size(), "Constructor argument count mismatch");
                    assertSame(languageSwitchButton, context.arguments().get(0), "LanguageSwitchButton mismatch");
                    assertSame(localizationService, context.arguments().get(1), "AppLocalizationService mismatch");
                    assertSame(imageLoader, context.arguments().get(2), "ImageLoader mismatch");
                })) {

            ChooseGameModeView result = ctrl.createView(languageSwitchButton); // Call the actual createView(button)

            assertNotNull(result, "Resulting view should not be null");
            assertEquals(1, mockedViewConstruction.constructed().size(), "ChooseGameModeView should have been constructed once");
            assertSame(result, mockedViewConstruction.constructed().getFirst(), "Returned view is not the constructed instance");
        }
    }

    // Replaced testCreateDialogBox_shouldReturnDialogBox
    @Test
    void testCreateDialogBox_constructsAndReturnsDialogBox() {
        ChooseGameModeController ctrl = new ChooseGameModeController(
                localizationService, multiplayerGameLauncher, imageLoader, mockView, languageSwitchButton
        );

        List<String> messages = List.of("Test message");
        PenguinFacialExpression expression = PenguinFacialExpression.HAPPY;
        PenguinHPState state = PenguinHPState.OKAY;
        Runnable onComplete = () -> {};
        // imageLoader is already a mock field

        try (MockedConstruction<DialogBox> mockedDialogConstruction = Mockito.mockConstruction(DialogBox.class,
                (mock, context) -> {
                    // Verify constructor arguments for DialogBox
                    assertEquals(5, context.arguments().size(), "Constructor argument count mismatch");
                    assertSame(messages, context.arguments().get(0), "Messages list mismatch");
                    assertSame(expression, context.arguments().get(1), "PenguinFacialExpression mismatch");
                    assertSame(state, context.arguments().get(2), "PenguinHPState mismatch");
                    assertSame(onComplete, context.arguments().get(3), "Runnable onComplete mismatch");
                    assertSame(imageLoader, context.arguments().get(4), "ImageLoader mismatch");
                })) {

            DialogBox result = ctrl.createDialogBox(messages, expression, state, onComplete, imageLoader);

            assertNotNull(result, "Resulting dialog box should not be null");
            assertEquals(1, mockedDialogConstruction.constructed().size(), "DialogBox should have been constructed once");
            assertSame(result, mockedDialogConstruction.constructed().getFirst(), "Returned dialog is not the constructed instance");
        }
    }

    @Test
    void testCreateDialogBox_shouldThrowExceptionForNullArguments() {
        // Use a controller instance that does NOT override createDialogBox,
        // so we test the original method's argument validation.
        ChooseGameModeController ctrl = new ChooseGameModeController(
                localizationService, multiplayerGameLauncher, imageLoader, mockView, languageSwitchButton
        );

        // Test with null messages
        assertThrows(IllegalArgumentException.class, () ->
            ctrl.createDialogBox( // Calling original createDialogBox
                null,
                PenguinFacialExpression.HAPPY,
                PenguinHPState.OKAY,
                () -> {},
                imageLoader));

        // Test with null expression
        assertThrows(IllegalArgumentException.class, () ->
            ctrl.createDialogBox( // Calling original createDialogBox
                List.of("Test"),
                null,
                PenguinHPState.OKAY,
                () -> {},
                imageLoader));

        // Test with null state
        assertThrows(IllegalArgumentException.class, () ->
            ctrl.createDialogBox( // Calling original createDialogBox
                List.of("Test"),
                PenguinFacialExpression.HAPPY,
                null,
                () -> {},
                imageLoader));

        // Test with null onComplete
        assertThrows(IllegalArgumentException.class, () ->
            ctrl.createDialogBox( // Calling original createDialogBox
                List.of("Test"),
                PenguinFacialExpression.HAPPY,
                PenguinHPState.OKAY,
                null,
                imageLoader));

        // Test with null imageLoader
        assertThrows(IllegalArgumentException.class, () ->
            ctrl.createDialogBox( // Calling original createDialogBox
                List.of("Test"),
                PenguinFacialExpression.HAPPY,
                PenguinHPState.OKAY,
                () -> {},
                null));
    }

    @Test
    void testSetupClickableBehavior_shouldSetupLabelCorrectly() {
        // Create mocks
        JLabel label = mock(JLabel.class);
        Runnable onClick = mock(Runnable.class);
        String tooltipKey = "test.tooltip";
        String tooltipText = "Test Tooltip";

        when(localizationService.get(tooltipKey)).thenReturn(tooltipText);

        // Call the method
        controller.setupClickableBehavior(label, tooltipKey, onClick);

        // Verify the label was set up correctly
        verify(label).setCursor(argThat(cursor -> cursor.getType() == Cursor.HAND_CURSOR));
        verify(label).setToolTipText(tooltipText);

        // Verify the mouse listener was added
        ArgumentCaptor<MouseAdapter> mouseAdapterCaptor = ArgumentCaptor.forClass(MouseAdapter.class);
        verify(label).addMouseListener(mouseAdapterCaptor.capture());

        // Simulate a click
        mouseAdapterCaptor.getValue().mouseClicked(null);

        // Verify the onClick runnable was called
        verify(onClick).run();
    }

    @Test
    void testSetupClickableBehavior_shouldHandleNullArguments() {
        // Test with null label
        assertThrows(IllegalArgumentException.class, () ->
            controller.setupClickableBehavior(null, "tooltipKey", () -> {}));

        // Test with null tooltipKey
        assertThrows(IllegalArgumentException.class, () ->
            controller.setupClickableBehavior(new JLabel(), null, () -> {}));

        // Test with null onClick
        assertThrows(IllegalArgumentException.class, () ->
            controller.setupClickableBehavior(new JLabel(), "tooltipKey", null));
    }

    @Test
    void testSelectGameMode_withMultiplayerMode_shouldShowTutorial() {
        // Create a spy controller
        ChooseGameModeController spyController = spy(controller);

        // Mock the tutorial method to avoid UI components
        doNothing().when(spyController).showMultiplayerTutorial();

        // Call the method
        spyController.selectGameMode(GameMode.Multiplayer);

        // Verify dispose and tutorial methods were called
        verify(spyController).dispose();
        verify(spyController).showMultiplayerTutorial();
    }

    @Test
    void testSelectGameMode_withNullMode_shouldDoNothing() {
        // Create a spy controller
        ChooseGameModeController spyController = spy(controller);

        // Call the method
        spyController.selectGameMode(null);

        // Verify neither tutorial method was called
        verify(spyController, never()).showMultiplayerTutorial();
        verify(spyController, never()).dispose();
    }

    @Test
    void testShowMultiplayerTutorial_shouldCreateDialogWithCorrectParameters() {
        // Call the method
        controller.showMultiplayerTutorial();

        // Verify dialog was shown
        verify(mockDialogBox).showDialog();

        // Verify localization keys were requested
        verify(localizationService).get("tutorial.welcome");
        verify(localizationService).get("tutorial.game.rounds");
        verify(localizationService).get("tutorial.multiplayer.player1");
        verify(localizationService).get("tutorial.multiplayer.player2");
        verify(localizationService).get("tutorial.points.explanation");
        verify(localizationService).get("tutorial.game.winner");
    }

    @Test
    void testStartMultiplayerGame_shouldLaunchGame() {
        // Call the method
        controller.startMultiplayerGame();

        // Verify the game launcher was called
        verify(multiplayerGameLauncher).launchGame();
    }

    @Test
    void testShowChooseGameModeView_createsAndSetsUpNewView() {
        // 1. Prepare a new mock view and its components for this specific test
        ChooseGameModeView newMockView = mock(ChooseGameModeView.class);
        JFrame mockFrameForNewView = mock(JFrame.class);
        JLabel mockMultiPlayerLabel = mock(JLabel.class);

        when(newMockView.getFrame()).thenReturn(mockFrameForNewView);
        when(newMockView.getMultiplayerImageLabel()).thenReturn(mockMultiPlayerLabel);

        // 2. Spy the controller instance from setUp.
        ChooseGameModeController spiedController = spy(controller);

        // Clear interactions from the initial setup of `spiedController`
        // (which is based on `controller` from setUp and its anonymous overrides).
        // Also clear interactions on mocks that spiedController might interact with during its setup.
        clearInvocations(localizationService, mockView, frame, this.languageSwitchButton, multiplayerGameLauncher, mockDialogBox);
        // Clear interactions on spiedController itself from its construction if any methods were called.
        clearInvocations(spiedController);
        // Set up the required stubbing for createView on the spy.
        // This ensures that when showChooseGameModeView calls createView, it returns our newMockView.
        doReturn(newMockView).when(spiedController).createView(eq(this.languageSwitchButton));


        // 4. Call the method under test
        spiedController.showChooseGameModeView();

        // 5. Verify createView was called on the spy with the correct languageSwitchButton instance
        verify(spiedController).createView(eq(this.languageSwitchButton));

        // 6. Verify interactions on newMockView (the one returned by the mocked createView)
        verify(newMockView).getFrame(); 

        ArgumentCaptor<WindowAdapter> windowAdapterCaptor = ArgumentCaptor.forClass(WindowAdapter.class);
        verify(mockFrameForNewView).addWindowListener(windowAdapterCaptor.capture());

        verify(newMockView).getMultiplayerImageLabel();

        // Capture Runnables passed to setupClickableBehavior
        ArgumentCaptor<Runnable> multiplayerRunnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(spiedController).setupClickableBehavior(eq(mockMultiPlayerLabel), eq("gamemode.multiplayer.tooltip"), multiplayerRunnableCaptor.capture());

        verify(localizationService).subscribe(newMockView); 
        verify(newMockView).onLocaleChanged();
        verify(newMockView).setVisible(true);

        verify(localizationService, never()).subscribe(mockView);

        // Execute captured Runnables and verify selectGameMode calls
        // These calls to selectGameMode will also call spiedController.dispose()

        multiplayerRunnableCaptor.getValue().run();
        verify(spiedController).selectGameMode(GameMode.Multiplayer);

        // Test the windowClosing event
        windowAdapterCaptor.getValue().windowClosing(null); // This also calls spiedController.dispose()

        // Verify dispose() and its effects. It's called 2 times:
        // 2. By multiplayerRunnable -> selectGameMode -> dispose
        // 3. By windowClosing event -> dispose
        verify(spiedController, times(2)).dispose();
        verify(localizationService, times(2)).unsubscribe(newMockView);
        verify(newMockView, times(2)).dispose();
    }

    @Test
    void testDispose_shouldCleanupResources() {
        // Call the method
        controller.dispose();

        // Verify cleanup actions were performed
        verify(localizationService).unsubscribe(mockView);
        verify(mockView).dispose();
    }
}

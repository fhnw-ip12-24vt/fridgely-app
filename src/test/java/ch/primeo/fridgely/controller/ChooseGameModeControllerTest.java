package ch.primeo.fridgely.controller;

import ch.primeo.fridgely.gamelaunchers.MultiplayerGameLauncher;
import ch.primeo.fridgely.model.GameMode;
import ch.primeo.fridgely.model.PenguinFacialExpression;
import ch.primeo.fridgely.model.PenguinHPState;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import ch.primeo.fridgely.util.ImageLoader;
import ch.primeo.fridgely.view.ChooseGameModeView;
import ch.primeo.fridgely.view.component.LanguageSwitchButton;
import ch.primeo.fridgely.view.util.DialogBox;

import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

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
    private JLabel singlePlayerLabel;

    @Mock
    private JLabel multiplayerLabel;

    @Mock
    private JFrame frame;

    private ChooseGameModeController controller;

    @BeforeAll
    static void init() {
        System.setProperty("java.awt.headless", "true");
    }

//    @BeforeEach
//    void setUp() {
//        // Set up the mock view to return the mock frame and labels
//        when(mockView.getFrame()).thenReturn(frame);
//
//        // Create the controller with mocked dependencies
//        controller = new ChooseGameModeController(localizationService,  multiplayerGameLauncher, imageLoader, mockView) {
//            @Override
//            protected ChooseGameModeView createView(LanguageSwitchButton languageSwitchButton) {
//                return mockView; // Return the mocked view to avoid HeadlessException
//            }
//
//            @Override
//            protected ChooseGameModeView createView() {
//                return mockView; // Return the mocked view to avoid HeadlessException
//            }
//
//            @Override
//            protected DialogBox createDialogBox(List<String> messages, PenguinFacialExpression expression, PenguinHPState state, Runnable onComplete, ImageLoader imageLoader) {
//                return mockDialogBox; // Return mock dialog box instead of creating a real one
//            }
//        };
//    }

    @Test
    void constructor_shouldInitializeViewAndSetupEventHandlers() {
        // Verify view was properly set up
        verify(localizationService).subscribe(mockView);
        verify(mockView).onLocaleChanged();
        verify(mockView).setVisible(true);
        verify(frame).addWindowListener(any());
    }

//    @Test
//    void selectGameMode_shouldHandleNullGameMode() {
//        // Reset interactions from setup
//        reset(multiplayerGameLauncher);
//
//        // Act
//        controller.selectGameMode(null);
//
//        // Assert - verify no interactions with the launcher (but view was already interacted with in constructor)
//        verifyNoInteractions(multiplayerGameLauncher);
//    }
//
//    @Test
//    void setupClickableBehavior_shouldHandleNullArguments() {
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class, () -> controller.setupClickableBehavior(null, "tooltipKey", () -> {}));
//        assertThrows(IllegalArgumentException.class, () -> controller.setupClickableBehavior(new JLabel(), null, () -> {}));
//        assertThrows(IllegalArgumentException.class, () -> controller.setupClickableBehavior(new JLabel(), "tooltipKey", null));
//    }
//
//    @Test
//    void createDialogBox_shouldHandleNullArguments() {
//        // Create a new controller instance that doesn't override createDialogBox
//        ChooseGameModeController testController = new ChooseGameModeController(
//                localizationService, multiplayerGameLauncher, imageLoader, mockView) {
//            @Override
//            protected ChooseGameModeView createView(LanguageSwitchButton languageSwitchButton) {
//                return mockView;
//            }
//
//            @Override
//            protected ChooseGameModeView createView() {
//                return mockView;
//            }
//            // Importantly, we don't override createDialogBox here so the real implementation is used
//        };
//
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class, () ->
//            testController.createDialogBox(null, PenguinFacialExpression.HAPPY, PenguinHPState.OKAY, () -> {}, imageLoader));
//        assertThrows(IllegalArgumentException.class, () ->
//            testController.createDialogBox(List.of("Message"), null, PenguinHPState.OKAY, () -> {}, imageLoader));
//        assertThrows(IllegalArgumentException.class, () ->
//            testController.createDialogBox(List.of("Message"), PenguinFacialExpression.HAPPY, null, () -> {}, imageLoader));
//        assertThrows(IllegalArgumentException.class, () ->
//            testController.createDialogBox(List.of("Message"), PenguinFacialExpression.HAPPY, PenguinHPState.OKAY, null, imageLoader));
//        assertThrows(IllegalArgumentException.class, () ->
//            testController.createDialogBox(List.of("Message"), PenguinFacialExpression.HAPPY, PenguinHPState.OKAY, () -> {}, null));
//    }

//    @Test
//    void startSinglePlayerGame_shouldStartGame() {
//        // Arrange
//        ChooseGameModeController spyController = Mockito.spy(controller);
//
//        // Act
//        spyController.startSinglePlayerGame();
//
//        // Assert
//        verify(spyController).startSinglePlayerGame();
//    }
//
//    @Test
//    void selectGameMode_whenMultiplayer_shouldShowTutorialAndStartGame() {
//        // Use a spy to partially mock the controller
//        ChooseGameModeController spyController = Mockito.spy(controller);
//
//        // Mock the tutorial method to avoid UI components
//        doNothing().when(spyController).showMultiplayerTutorial();
//
//        // Call the method being tested
//        spyController.selectGameMode(GameMode.Multiplayer);
//
//        // Verify dispose and tutorial methods were called
//        verify(spyController).dispose();
//        verify(spyController).showMultiplayerTutorial();
//    }

//    @Test
//    void selectGameMode_whenSinglePlayer_shouldShowTutorial() {
//        // Use a spy to partially mock the controller
//        ChooseGameModeController spyController = Mockito.spy(controller);
//
//        // Mock the tutorial method to avoid UI components
//        doNothing().when(spyController).showSinglePlayerTutorial();
//
//        // Call the method being tested
//        spyController.selectGameMode(GameMode.SinglePlayer);
//
//        // Verify dispose and tutorial methods were called
//        verify(spyController).dispose();
//        verify(spyController).showSinglePlayerTutorial();
//    }

//    @Test
//    void startMultiplayerGame_shouldLaunchMultiplayerGame() {
//        // Call the method being tested
//        controller.startMultiplayerGame();
//
//        // Verify the game launcher was called
//        verify(multiplayerGameLauncher).launchGame();
//    }

//    @Test
//    void showMultiplayerTutorial_shouldCreateDialogWithCorrectParameters() {
//        // Call the method being tested
//        controller.showMultiplayerTutorial();
//
//        // Verify dialog was shown
//        verify(mockDialogBox).showDialog();
//
//        // Verify localization keys were requested
//        verify(localizationService).get("tutorial.welcome");
//        verify(localizationService).get("tutorial.game.rounds");
//        verify(localizationService).get("tutorial.multiplayer.player1");
//        verify(localizationService).get("tutorial.multiplayer.player2");
//        verify(localizationService).get("tutorial.points.explanation");
//        verify(localizationService).get("tutorial.game.winner");
//    }

    @Test
    void dispose_shouldCleanupResources() {
        // Call dispose
        controller.dispose();

        // Verify cleanup actions were performed
        verify(localizationService).unsubscribe(mockView);
        verify(mockView).dispose();
    }

    @Test
    void windowClosing_shouldCallDispose_onView() {
        ArgumentCaptor<WindowListener> listenerCaptor = ArgumentCaptor.forClass(WindowListener.class);
        verify(frame).addWindowListener(listenerCaptor.capture());

        WindowEvent mockEvent = mock(WindowEvent.class);
        listenerCaptor.getValue().windowClosing(mockEvent);

        verify(mockView).dispose();
        verify(localizationService).unsubscribe(mockView);
    }

//    @Test
//    void setupClickableBehavior_shouldSetTooltipAndTriggerOnClick() {
//        // Arrange
//        JLabel label = mock(JLabel.class);
//        Runnable onClick = mock(Runnable.class);
//        String tooltipKey = "gamemode.singleplayer.tooltip";
//        when(localizationService.get(tooltipKey)).thenReturn("Single Player Tooltip");
//
//        // Act
//        controller.setupClickableBehavior(label, tooltipKey, onClick);
//
//        // Assert
//        verify(label).setCursor(argThat(cursor -> cursor.getType() == Cursor.HAND_CURSOR));
//        verify(label).setToolTipText("Single Player Tooltip");
//
//        // Simulate a click
//        ArgumentCaptor<MouseAdapter> mouseAdapterCaptor = ArgumentCaptor.forClass(MouseAdapter.class);
//        verify(label).addMouseListener(mouseAdapterCaptor.capture());
//        mouseAdapterCaptor.getValue().mouseClicked(null);
//
//        verify(onClick).run();
//    }

//    @Test
//    void startSinglePlayerGame_shouldBeInvokedAfterTutorial() {
//        // Use a spy to partially mock the controller
//        ChooseGameModeController spyController = Mockito.spy(controller);
//
//        // Arrange
//        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
//
//        // First, call the method that triggers the dialog creation
//        spyController.showSinglePlayerTutorial();
//
//        // Now, verify the dialog creation and capture the Runnable
//        verify(spyController).createDialogBox(anyList(), any(), any(), runnableCaptor.capture(), eq(imageLoader));
//
//        // Act: run the captured Runnable, simulating the completion of the dialog
//        runnableCaptor.getValue().run();
//
//        // Assert
//        verify(mockDialogBox).showDialog();  // Ensure dialog is shown
//        verify(spyController).startSinglePlayerGame();  // Ensure game start is triggered
//    }

//    @Test
//    void showSinglePlayerTutorial_shouldCreateDialogWithCorrectParameters() {
//        // Use a spy to partially mock the controller
//        ChooseGameModeController spyController = Mockito.spy(controller);
//
//        // Act
//        spyController.showSinglePlayerTutorial();
//
//        // Assert
//        verify(mockDialogBox).showDialog();
//        verify(localizationService).get("tutorial.welcome");
//        verify(localizationService).get("tutorial.singleplayer.recipe");
//        verify(localizationService).get("tutorial.singleplayer.score");
//
//        // Capture and invoke the Runnable after method execution
//        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
//        verify(spyController).createDialogBox(anyList(), any(), any(), runnableCaptor.capture(), eq(imageLoader));
//        runnableCaptor.getValue().run();
//
//        verify(spyController).startSinglePlayerGame();
//    }

//    @Test
//    void constructor_shouldSetupClickableBehaviorForLabels() {
//        // Arrange
//        ChooseGameModeView mockView = mock(ChooseGameModeView.class);
//        JLabel singlePlayerLabel = mock(JLabel.class);
//        JLabel multiplayerLabel = mock(JLabel.class);
//        JFrame mockFrame = mock(JFrame.class);
//
//        when(mockView.getSinglePlayerImageLabel()).thenReturn(singlePlayerLabel);
//        when(mockView.getMultiplayerImageLabel()).thenReturn(multiplayerLabel);
//        when(mockView.getFrame()).thenReturn(mockFrame);
//        when(localizationService.get(anyString())).thenReturn("tooltip");
//
//        // Act: use constructor that DOES call setupClickableBehavior
//        new ChooseGameModeController(localizationService, languageSwitchButton, multiplayerGameLauncher, imageLoader) {
//            @Override
//            protected ChooseGameModeView createView(LanguageSwitchButton languageSwitchButton) {
//                return mockView;
//            }
//        };
//
//        // Assert: verify expected interactions
//        verify(singlePlayerLabel).setCursor(argThat(cursor -> cursor.getType() == Cursor.HAND_CURSOR));
//        verify(singlePlayerLabel).setToolTipText("tooltip");
//        verify(singlePlayerLabel).addMouseListener(any());
//
//        verify(multiplayerLabel).setCursor(argThat(cursor -> cursor.getType() == Cursor.HAND_CURSOR));
//        verify(multiplayerLabel).setToolTipText("tooltip");
//        verify(multiplayerLabel).addMouseListener(any());
//    }

    /*@Test
    void setupClickableBehavior_shouldTriggerRunnableOnClick() {
        // Arrange
        AppLocalizationService mockLocalizationService = mock(AppLocalizationService.class);
        MultiplayerGameLauncher mockLauncher = mock(MultiplayerGameLauncher.class);
        ImageLoader mockImageLoader = mock(ImageLoader.class);
        ChooseGameModeView mockView = mock(ChooseGameModeView.class);
        JFrame mockFrame = mock(JFrame.class);

        when(mockView.getFrame()).thenReturn(mockFrame);
        when(mockLocalizationService.get("gamemode.singleplayer.tooltip")).thenReturn("Tooltip text");

        ChooseGameModeController controller = new ChooseGameModeController(
                mockLocalizationService,
                mockLauncher,
                mockImageLoader,
                mockView
        );

        JLabel realLabel = new JLabel(); // must be real to register MouseListeners
        Runnable mockRunnable = mock(Runnable.class);

        // Act
        controller.setupClickableBehavior(realLabel, "gamemode.singleplayer.tooltip", mockRunnable);

        // Simulate click
        for (MouseListener listener : realLabel.getMouseListeners()) {
            listener.mouseClicked(new MouseEvent(realLabel, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, 0, 0, 1, false));
        }

        // Assert
        verify(mockRunnable).run();
    }

    @Test
    void testCreateView() {
        ChooseGameModeView view = controller.createView();
        assertNotNull(view, "The view should not be null");
    }

    @Test
    void testCreateViewWithLanguageSwitchButton() {

        when(mockView.getSinglePlayerImageLabel()).thenReturn(singlePlayerLabel);
        when(mockView.getMultiplayerImageLabel()).thenReturn(multiplayerLabel);
        // Mock localization service responses
        when(localizationService.get(anyString())).thenReturn("mocked text");
        // Mock the labels on the view
        when(mockView.getSinglePlayerImageLabel()).thenReturn(singlePlayerLabel);
        when(mockView.getMultiplayerImageLabel()).thenReturn(multiplayerLabel);

        // Create a new instance of the controller with mocked dependencies
        ChooseGameModeController controllerWithMockedView = new ChooseGameModeController(
                localizationService,
                languageSwitchButton,
                multiplayerGameLauncher,
                imageLoader
        ) {
            @Override
            protected ChooseGameModeView createView(LanguageSwitchButton languageSwitchButton) {
                return mockView; // Return the mocked view
            }
        };

        LanguageSwitchButton languageSwitchButton = mock(LanguageSwitchButton.class);
        ChooseGameModeView view = controllerWithMockedView.createView(languageSwitchButton);
        assertNotNull(view, "The view with language switch button should not be null");
    }

    @Test
    void testCreateDialogBox() {
        List<String> messages = Arrays.asList("Message 1", "Message 2");

        // Mock imageLoader to ensure no real image loading happens
        DialogBox dialogBox = controller.createDialogBox(
                messages,
                PenguinFacialExpression.HAPPY,
                PenguinHPState.OKAY,
                () -> {},
                imageLoader
        );

        // Just verify that a dialog box is returned and no exception is thrown
        assertNotNull(dialogBox, "The dialog box should not be null");
    }

    @Test
    void createView_shouldReturnMockedView() {
        // Arrange
        ChooseGameModeView mockedView = mock(ChooseGameModeView.class);
        ChooseGameModeController testController = new ChooseGameModeController(localizationService, multiplayerGameLauncher, imageLoader, mockView) {
            @Override
            protected ChooseGameModeView createView() {
                return mockedView; // Mock to avoid HeadlessException
            }
        };

        // Act
        ChooseGameModeView view = testController.createView();

        // Assert
        assertNotNull(view, "The created view should not be null");
        assertEquals(mockedView, view, "The returned view should match the mocked view");
    }

    @Test
    void createView_withLanguageSwitchButton_shouldReturnMockedView() {
        // Arrange
        LanguageSwitchButton mockButton = mock(LanguageSwitchButton.class);
        ChooseGameModeView mockedView = mock(ChooseGameModeView.class);
        ChooseGameModeController testController = new ChooseGameModeController(localizationService, multiplayerGameLauncher, imageLoader, mockView) {
            @Override
            protected ChooseGameModeView createView(LanguageSwitchButton languageSwitchButton) {
                return mockedView; // Mock to avoid HeadlessException
            }
        };

        // Act
        ChooseGameModeView view = testController.createView(mockButton);

        // Assert
        assertNotNull(view, "The created view with LanguageSwitchButton should not be null");
        assertEquals(mockedView, view, "The returned view should match the mocked view");
    }

    @Test
    void windowClosing_shouldCallDisposeMethodOnController() {
        // Reset counters to ensure clean state
        reset(localizationService, mockView);

        // Capture the window listener that was added in the constructor
        ArgumentCaptor<WindowListener> listenerCaptor = ArgumentCaptor.forClass(WindowListener.class);
        verify(frame).addWindowListener(listenerCaptor.capture());

        // Get the captured listener
        WindowListener capturedListener = listenerCaptor.getValue();
        assertNotNull(capturedListener, "Listener should have been captured.");

        // Act: Simulate window closing event
        WindowEvent mockEvent = mock(WindowEvent.class);
        capturedListener.windowClosing(mockEvent);

        // Assert: Verify the observable effects of dispose() being called
        verify(localizationService).unsubscribe(mockView);
        verify(mockView).dispose();
    }*/
}

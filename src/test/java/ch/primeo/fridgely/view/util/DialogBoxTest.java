package ch.primeo.fridgely.view.util;

import ch.primeo.fridgely.Fridgely;
import ch.primeo.fridgely.FridgelyContext;
import ch.primeo.fridgely.model.PenguinFacialExpression;
import ch.primeo.fridgely.model.PenguinHPState;
import ch.primeo.fridgely.service.localization.AppLocalizationService;
import ch.primeo.fridgely.util.ImageLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.GraphicsDevice;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DialogBoxTest {

    @Mock
    private ImageLoader mockImageLoader;

    @Mock
    private AppLocalizationService mockLocalizationService;

    @Mock
    private Runnable mockCallback;

    @Mock
    private GraphicsDevice mockGraphicsDevice;

    @Mock
    private JFrame mockFrame;

    @Mock
    private JPanel mockGlassPane;

    private List<String> testMessages;
    private DialogBox dialogBox;

    @BeforeEach
    void setUp() {
        testMessages = Arrays.asList("Message 1", "Message 2", "Message 3");

        // Mock the localization service
        when(mockLocalizationService.get("tutorial.button.skip")).thenReturn("Skip");

        // Stub mockFrame methods
        when(mockFrame.getSize()).thenReturn(new Dimension(800, 600)); // Default size for tests

        // Use try-with-resources to mock static methods
        try (MockedStatic<FridgelyContext> mockedFridgelyContext = mockStatic(FridgelyContext.class);
             MockedStatic<Fridgely> mockedFridgely = mockStatic(Fridgely.class)) {

            // Set up the mock for FridgelyContext
            mockedFridgelyContext.when(() -> FridgelyContext.getBean(AppLocalizationService.class))
                    .thenReturn(mockLocalizationService);

            // Set up the mock for Fridgely.getMainAppScreen()
            mockedFridgely.when(Fridgely::getMainAppScreen).thenReturn(mockGraphicsDevice);

            // Create the dialog box with our mocks, using the package-private constructor
            dialogBox = new DialogBox(
                    testMessages,
                    PenguinFacialExpression.HAPPY,
                    PenguinHPState.OKAY,
                    mockCallback,
                    mockImageLoader,
                    mockFrame // Inject mock JFrame
            );
        }
    }

    @Test
    void testSkipButtonExists() {
        // Access the skip button directly
        JButton skipButton = dialogBox.skipButton;

        // Verify the skip button exists and has the correct text
        assertNotNull(skipButton);
        assertEquals("Skip", skipButton.getText());

        // Verify the skip button is not added to the dialog itself
        // but will be added to the glass pane in showDialog()
        assertFalse(Arrays.asList(dialogBox.getComponents()).contains(skipButton));
    }

    @Test
    void testSkipButtonAction() {
        // Access the skip button directly
        JButton skipButton = dialogBox.skipButton;

        // Simulate clicking the skip button
        for (ActionListener listener : skipButton.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(skipButton, ActionEvent.ACTION_PERFORMED, ""));
        }

        // Verify the callback was called
        verify(mockCallback, times(1)).run();
    }

    @Test
    void testNextMessage() {
        // Access the current message index directly
        // Initial index should be 0
        assertEquals(0, dialogBox.currentMessageIndex);

        // Call the nextMessage method directly
        dialogBox.nextMessage();

        // Index should now be 1
        assertEquals(1, dialogBox.currentMessageIndex);

        // Call nextMessage twice more to reach the end
        dialogBox.nextMessage();
        dialogBox.nextMessage();

        // Verify the callback was called when we reached the end
        verify(mockCallback, times(1)).run();
    }
}

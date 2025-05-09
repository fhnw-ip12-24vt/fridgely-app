package ch.primeo.fridgely.model;

import ch.primeo.fridgely.config.GameConfig;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class PenguinModelTest {

    private PenguinModel penguinModel;

    @BeforeEach
    void setUp() {
        penguinModel = new PenguinModel();
    }

    @Test
    void testGetHP() {
        // Arrange
        int expectedHP = GameConfig.STARTING_HP;

        // Act
        int actualHP = penguinModel.getHP();

        // Assert
        assertEquals(expectedHP, actualHP);
    }

    @Test
    void testModifyHPIncrease() {
        // Arrange
        int change = 10;
        int expectedHP = Math.min(GameConfig.MAX_HP, GameConfig.STARTING_HP + change);

        // Act
        penguinModel.modifyHP(change);
        int actualHP = penguinModel.getHP();

        // Assert
        assertEquals(expectedHP, actualHP);
    }

    @Test
    void testModifyHPDecrease() {
        // Arrange
        int change = -20;
        int expectedHP = Math.max(GameConfig.MIN_HP, GameConfig.STARTING_HP + change);

        // Act
        penguinModel.modifyHP(change);
        int actualHP = penguinModel.getHP();

        // Assert
        assertEquals(expectedHP, actualHP);
    }

    @Test
    void testModifyHPClamping() {
        // Arrange
        int change = 1000; // Exceeds MAX_HP
        int expectedHP = GameConfig.MAX_HP;

        // Act
        penguinModel.modifyHP(change);
        int actualHP = penguinModel.getHP();

        // Assert
        assertEquals(expectedHP, actualHP);
    }

    @Test
    void testResetHP() {
        // Arrange
        penguinModel.modifyHP(-50); // Change HP to a different value
        int expectedHP = GameConfig.STARTING_HP;

        // Act
        penguinModel.resetHP();
        int actualHP = penguinModel.getHP();

        // Assert
        assertEquals(expectedHP, actualHP);
    }

    @Test
    void testGetImagePathForHP() {
        // Arrange
        penguinModel.modifyHP(GameConfig.MAX_HP - GameConfig.STARTING_HP);
        String expectedPath = "/ch/primeo/fridgely/sprites/ice_big_size.png";

        // Act
        String actualPath = penguinModel.getImagePathForHP();

        // Assert
        assertEquals(expectedPath, actualPath);
    }

    @Test
    void testGetImagePathForHP_MaxHP() {
        // Arrange
        penguinModel.modifyHP(GameConfig.MAX_HP - GameConfig.STARTING_HP);
        String expectedPath = "/ch/primeo/fridgely/sprites/ice_big_size.png";

        // Act
        String actualPath = penguinModel.getImagePathForHP();

        // Assert
        assertEquals(expectedPath, actualPath);
    }

    @Test
    void testGetImagePathForHP_AboveStartingHP() {
        // Arrange
        // Set HP between STARTING_HP and MAX_HP
        penguinModel.modifyHP(10); // Assuming this puts HP above STARTING_HP but below MAX_HP
        String expectedPath = "/ch/primeo/fridgely/sprites/ice_middle_size.png";

        // Act
        String actualPath = penguinModel.getImagePathForHP();

        // Assert
        assertEquals(expectedPath, actualPath);
    }

    @Test
    void testGetImagePathForHP_ExactlyThirty() {
        // Arrange
        // Set HP to exactly 30
        penguinModel.resetHP(); // Reset first to ensure we're at STARTING_HP
        penguinModel.modifyHP(30 - GameConfig.STARTING_HP); // Adjust to exactly 30
        String expectedPath = "/ch/primeo/fridgely/sprites/penguin_on_small_block_of_ice.png";

        // Act
        String actualPath = penguinModel.getImagePathForHP();

        // Assert
        assertEquals(expectedPath, actualPath);
    }

    @Test
    void testGetImagePathForHP_BetweenZeroAndThirty() {
        // Arrange
        // Set HP between 0 and 30 (exclusive of 30)
        penguinModel.resetHP();
        penguinModel.modifyHP(-(GameConfig.STARTING_HP - 15)); // Set HP to 15
        String expectedPath = "/ch/primeo/fridgely/sprites/penguin_swimming.png";

        // Act
        String actualPath = penguinModel.getImagePathForHP();

        // Assert
        assertEquals(expectedPath, actualPath);
    }

    @Test
    void testGetImagePathForHP_ZeroOrLess() {
        // Arrange
        // Set HP to 0 (minimum)
        penguinModel.modifyHP(-GameConfig.MAX_HP); // Large negative number to ensure reaching MIN_HP
        String expectedPath = "/ch/primeo/fridgely/sprites/penguin_unalive.png";

        // Act
        String actualPath = penguinModel.getImagePathForHP();

        // Assert
        assertEquals(expectedPath, actualPath);
    }

    @Test
    void testPropertyChangeListener() {
        // Arrange
        PropertyChangeListenerMock listener = new PropertyChangeListenerMock();
        penguinModel.addPropertyChangeListener(listener);
        int change = -10;

        // Act
        penguinModel.modifyHP(change);
        PropertyChangeEvent event = listener.getLastEvent();

        // Assert
        assertNotNull(event);
        assertEquals(PenguinModel.PROP_PENGUIN_HP, event.getPropertyName());
        assertEquals(GameConfig.STARTING_HP, event.getOldValue());
        assertEquals(GameConfig.STARTING_HP + change, event.getNewValue());
    }

    @Test
    void testRemovePropertyChangeListener() {
        // Arrange
        PropertyChangeListenerMock listener = new PropertyChangeListenerMock();
        penguinModel.addPropertyChangeListener(listener);
        penguinModel.removePropertyChangeListener(listener);

        // Act
        penguinModel.modifyHP(-10);
        PropertyChangeEvent event = listener.getLastEvent();

        // Assert
        assertNull(event); // Listener should not receive events
    }

    // Mock class for PropertyChangeListener
    @Getter
    private static final class PropertyChangeListenerMock implements PropertyChangeListener {
        private PropertyChangeEvent lastEvent;

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            this.lastEvent = evt;
        }

    }
}

package ch.primeo.fridgely.model;

import ch.primeo.fridgely.config.GameConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;

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
    private static class PropertyChangeListenerMock implements PropertyChangeListener {
        private PropertyChangeEvent lastEvent;

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            this.lastEvent = evt;
        }

        public PropertyChangeEvent getLastEvent() {
            return lastEvent;
        }

        public void reset() {
            this.lastEvent = null;
        }
    }
}

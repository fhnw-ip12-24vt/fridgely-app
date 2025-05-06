package ch.primeo.fridgely.model;

import ch.primeo.fridgely.config.GameConfig;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Model for the penguin health points in the multiplayer game mode. Tracks the HP of the penguin and notifies listeners
 * of changes.
 */
public class PenguinModel {

    /**
     * Property name for changes in the penguin HP.
     */
    public static final String PROP_PENGUIN_HP = "penguinHP";

    private int hp;
    private final PropertyChangeSupport propertyChangeSupport;

    /**
     * Constructs a new penguin model with the default starting HP.
     */
    public PenguinModel() {
        this.hp = GameConfig.STARTING_HP;
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    /**
     * Gets the current HP value of the penguin.
     *
     * @return the current HP
     */
    public int getHP() {
        return hp;
    }

    public String getImagePathForHP() {
        if (hp >= GameConfig.MAX_HP) {
            return "/ch/primeo/fridgely/sprites/ice_big_size.png";
        } else if (hp > GameConfig.STARTING_HP) {
            return "/ch/primeo/fridgely/sprites/ice_middle_size.png";
        } else if (hp == 30) {
            return "/ch/primeo/fridgely/sprites/penguin_on_small_block_of_ice.png";
        } else if (hp > 0) {
            return "/ch/primeo/fridgely/sprites/penguin_swimming.png";
        } else {
            return "/ch/primeo/fridgely/sprites/penguin_unalive.png";
        }
    }

    /**
     * Modifies the HP value of the penguin. The HP is clamped between MIN_HP and MAX_HP.
     *
     * @param change the amount to modify the HP by (positive for increase, negative for decrease)
     */
    public void modifyHP(int change) {
        int oldHP = hp;
        hp = Math.max(GameConfig.MIN_HP, Math.min(GameConfig.MAX_HP, hp + change));
        if (oldHP != hp) {
            propertyChangeSupport.firePropertyChange(PROP_PENGUIN_HP, oldHP, hp);
        }
    }

    /**
     * Resets the penguin HP to the default starting value.
     */
    public void resetHP() {
        int oldHP = hp;
        hp = GameConfig.STARTING_HP;
        if (oldHP != hp) {
            propertyChangeSupport.firePropertyChange(PROP_PENGUIN_HP, oldHP, hp);
        }
    }

    /**
     * Adds a property change listener.
     *
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Removes a property change listener.
     *
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}

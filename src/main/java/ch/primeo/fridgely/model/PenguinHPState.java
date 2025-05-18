package ch.primeo.fridgely.model;

import ch.primeo.fridgely.config.GameConfig;
import lombok.Getter;

/**
 * Represents the health state of the penguin, with associated sprite images.
 */
public enum PenguinHPState {
    /**
     * Represents an excellent health state.
     */
    EXCELLENT("ice_big_size.png"),
    /**
     * Represents a good health state.
     */
    GOOD("ice_middle_size.png"),
    /**
     * Represents an okay health state.
     */
    OKAY("penguin_on_small_block_of_ice.png"),
    /**
     * Represents an okay health state.
     */
    STRUGGLING("penguin_swimming.png"),
    /**
     * Represents a critical health state.
     */
    CRITICAL("penguin_on_fire.png"),
    /**
     * Represents a dead health state.
     */
    DEAD("penguin_unalive.png");

    @Getter
    private final String spritePath;

    /**
     * Constructs a PenguinHPState with the given sprite path.
     *
     * @param path the path to the sprite image
     */
    PenguinHPState(String path) {
        this.spritePath = "/ch/primeo/fridgely/sprites/" + path;
    }

    /**
     * Returns the PenguinHPState corresponding to the given HP value.
     *
     * @param hp the health points
     * @return the corresponding PenguinHPState
     */
    public static PenguinHPState fromHP(int hp) {
        if (hp >= GameConfig.SCORE_EXCELLENT) {
            return EXCELLENT;
        }

        if (hp >= GameConfig.SCORE_GOOD) {
            return GOOD;
        }

        if (hp <= GameConfig.SCORE_DEAD) {
            return DEAD;
        }

        if (hp <= GameConfig.SCORE_CRITICAL) {
            return CRITICAL;
        }

        return OKAY;
    }
}

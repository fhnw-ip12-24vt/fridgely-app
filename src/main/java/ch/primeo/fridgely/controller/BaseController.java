package ch.primeo.fridgely.controller;

/**
 * Base interface for all controllers, requiring a dispose method for cleanup.
 */
public interface BaseController {
    /**
     * Releases resources and performs cleanup for the controller.
     */
    void dispose();
}

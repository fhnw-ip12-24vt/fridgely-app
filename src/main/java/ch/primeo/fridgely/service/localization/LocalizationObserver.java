package ch.primeo.fridgely.service.localization;

/**
 * Observer interface for receiving locale change notifications.
 */
public interface LocalizationObserver {
    /**
     * Called when the application's locale has changed.
     */
    void onLocaleChanged();
}

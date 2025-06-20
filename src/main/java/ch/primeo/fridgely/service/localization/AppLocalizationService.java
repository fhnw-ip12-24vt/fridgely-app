package ch.primeo.fridgely.service.localization;

import ch.primeo.fridgely.Fridgely;
import ch.primeo.fridgely.config.AppConfig;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Scope("singleton")
public class AppLocalizationService {

    /**
     * Supported locales in the application (English, German, French).
     */
    private final List<Locale> locales = Arrays.asList(
            Locale.forLanguageTag("en"),
            Locale.forLanguageTag("de"),
            Locale.forLanguageTag("fr")
    );

    /**
     * Currently active locale.
     */
    private Locale currentLocale;

    /**
     * Resource bundle containing localized strings for current locale.
     */
    private ResourceBundle resource;

    /**
     * List of observers to notify when locale changes.
     */
    private final CopyOnWriteArrayList<LocalizationObserver> observers = new CopyOnWriteArrayList<>();

    /**
     * Constructs the AppLocalization with the given startup language.
     *
     * @param config the application configuration
     */
    public AppLocalizationService(AppConfig config) {
        var startupLanguage = config.getConfiguredAppLanguage() == null
                ? "de"
                : config.getConfiguredAppLanguage();

        if (locales.contains(Locale.forLanguageTag(startupLanguage))) {
            currentLocale = Locale.forLanguageTag(startupLanguage);
        } else {
            currentLocale = locales.getFirst();
        }

        resource = ResourceBundle.getBundle(Fridgely.class.getPackageName() + ".languages", currentLocale);
    }

    /**
     * Switches to the next locale and notifies observers.
     */
    public void toggleLocale() {
        var currentIndex = locales.indexOf(currentLocale);

        if (currentIndex == -1 || currentIndex == locales.size() - 1) {
            currentIndex = 0;
        } else {
            currentIndex++;
        }

        currentLocale = locales.get(currentIndex);

        resource = ResourceBundle.getBundle(Fridgely.class.getPackageName() + ".languages", currentLocale);
        notifyObservers();
    }

    /**
     * Gets the localized string for the given key.
     *
     * @param key the resource key
     * @return the localized string
     */
    public String get(String key) {
        return resource.getString(key);
    }

    /**
     * Subscribes an observer to locale changes.
     *
     * @param observer the observer to subscribe
     */
    public void subscribe(LocalizationObserver observer) {
        observers.add(observer);
    }

    /**
     * Unsubscribes an observer from locale changes.
     *
     * @param observer the observer to unsubscribe
     */
    public void unsubscribe(LocalizationObserver observer) {
        observers.remove(observer);
    }

    /**
     * Gets the current language code.
     *
     * @return the current language code
     */
    public String getLanguage() {
        return currentLocale.getLanguage();
    }

    /**
     * Notifies all observers of a locale change.
     */
    private void notifyObservers() {
        for (LocalizationObserver observer : observers) {
            observer.onLocaleChanged();
        }
    }

    public static String getLocalizedString(String language, String name, String nameDE, String nameFR) {
        return switch (language != null ? language.toLowerCase() : "") {
            case "de" -> nameDE != null ? nameDE : name;
            case "fr" -> nameFR != null ? nameFR : name;
            default -> name;
        };
    }
}

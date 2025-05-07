package ch.primeo.fridgely.service.localization;

import ch.primeo.fridgely.config.AppConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.lang.reflect.Field;
import java.util.Locale;

import java.util.MissingResourceException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppLocalizationServiceTest {

    @Mock
    private AppConfig mockAppConfig;

    private AppLocalizationService appLocalizationService;

    @BeforeEach
    void setUp() {
        // Default setup, can be overridden in specific tests
        when(mockAppConfig.getConfiguredAppLanguage()).thenReturn("en");
        appLocalizationService = new AppLocalizationService(mockAppConfig);
    }

    @Test
    void testGetLocalizedString_english() {
        assertEquals("English", appLocalizationService.get("home.button.lang"));
        assertEquals("Choose Game Mode", appLocalizationService.get("gamemode.title"));
    }

    @Test
    void testGetLocalizedString_german() {
        appLocalizationService.toggleLocale(); // Switch to German
        assertEquals("de", appLocalizationService.getLanguage());
        // Assuming "Deutsch" is the value for "home.button.lang" in languages_de.properties
        // This requires the actual german properties file to be present and correctly loaded
        // For this test, we'll check if it's different from English and not null
        assertNotNull(appLocalizationService.get("home.button.lang"));
        assertNotEquals("English", appLocalizationService.get("home.button.lang"));
    }

    @Test
    void testGetLocalizedString_french() {
        appLocalizationService.toggleLocale(); // en -> de
        appLocalizationService.toggleLocale(); // de -> fr
        assertEquals("fr", appLocalizationService.getLanguage());
        assertNotNull(appLocalizationService.get("home.button.lang"));
        assertNotEquals("English", appLocalizationService.get("home.button.lang"));
    }

    @Test
    void testGetLocalizedString_keyNotFound() {
        assertThrows(MissingResourceException.class, () -> {
            appLocalizationService.get("non.existent.key");
        });
    }

    @Test
    void testToggleLocale_cyclesThroughLanguages() {
        assertEquals("en", appLocalizationService.getLanguage());

        appLocalizationService.toggleLocale();
        assertEquals("de", appLocalizationService.getLanguage());

        appLocalizationService.toggleLocale();
        assertEquals("fr", appLocalizationService.getLanguage());

        appLocalizationService.toggleLocale(); // Cycle back to English
        assertEquals("en", appLocalizationService.getLanguage());
    }

    @Test
    void testToggleLocale_whenCurrentLocaleNotInList_resetsToFirstLocale() throws NoSuchFieldException, IllegalAccessException {
        // Arrange: Current default language is "en" due to setUp. Locales are [en, de, fr].
        // Use reflection to set currentLocale to a value not in the AppLocalizationService's locales list.
        Locale rogueLocale = Locale.forLanguageTag("xx"); // A locale not in the list [en, de, fr]

        Field currentLocaleField = AppLocalizationService.class.getDeclaredField("currentLocale");
        currentLocaleField.setAccessible(true);
        currentLocaleField.set(appLocalizationService, rogueLocale);

        // Act
        // Calling toggleLocale when currentLocale is "xx" (rogue)
        // locales.indexOf(rogueLocale) will be -1.
        // The condition if (currentIndex == -1 || currentIndex == locales.size() - 1) becomes true.
        // currentIndex will be set to 0.
        appLocalizationService.toggleLocale();

        // Assert
        // The language should reset to the first locale in the list ("en").
        assertEquals("en", appLocalizationService.getLanguage(),
                "Language should reset to the first locale (en) if current locale was not found.");
    }

    @Test
    void testGetLanguage() {
        assertEquals("en", appLocalizationService.getLanguage());
        appLocalizationService.toggleLocale();
        assertEquals("de", appLocalizationService.getLanguage());
    }

    @Test
    void testSubscribeAndNotifyObserver() {
        LocalizationObserver mockObserver = mock(LocalizationObserver.class);
        appLocalizationService.subscribe(mockObserver);

        appLocalizationService.toggleLocale(); // This should notify the observer

        verify(mockObserver, times(1)).onLocaleChanged();
    }

    @Test
    void testUnsubscribeObserver() {
        LocalizationObserver mockObserver = mock(LocalizationObserver.class);
        appLocalizationService.subscribe(mockObserver);
        appLocalizationService.unsubscribe(mockObserver);

        appLocalizationService.toggleLocale(); // This should NOT notify the observer

        verify(mockObserver, never()).onLocaleChanged();
    }

    @Test
    void testConstructor_withValidConfiguredLanguage_german() {
        when(mockAppConfig.getConfiguredAppLanguage()).thenReturn("de");
        appLocalizationService = new AppLocalizationService(mockAppConfig);
        assertEquals("de", appLocalizationService.getLanguage());
        // Assuming "Deutsch" for "home.button.lang" in German
        assertNotNull(appLocalizationService.get("home.button.lang"));
    }

    @Test
    void testConstructor_withValidConfiguredLanguage_french() {
        when(mockAppConfig.getConfiguredAppLanguage()).thenReturn("fr");
        appLocalizationService = new AppLocalizationService(mockAppConfig);
        assertEquals("fr", appLocalizationService.getLanguage());
        assertNotNull(appLocalizationService.get("home.button.lang"));
    }

    @Test
    void testConstructor_withInvalidConfiguredLanguage_defaultsToEnglish() {
        when(mockAppConfig.getConfiguredAppLanguage()).thenReturn("xx"); // Invalid language
        appLocalizationService = new AppLocalizationService(mockAppConfig);
        assertEquals("en", appLocalizationService.getLanguage());
        assertEquals("English", appLocalizationService.get("home.button.lang"));
    }

    @Test
    void testConstructor_withNullConfiguredLanguage_defaultsToEnglish() {
        when(mockAppConfig.getConfiguredAppLanguage()).thenReturn(null);
        appLocalizationService = new AppLocalizationService(mockAppConfig);
        assertEquals("en", appLocalizationService.getLanguage());
        assertEquals("English", appLocalizationService.get("home.button.lang"));
    }

    @Test
    void testResourceBundleLoading_forGerman() {
        when(mockAppConfig.getConfiguredAppLanguage()).thenReturn("de");
        appLocalizationService = new AppLocalizationService(mockAppConfig);
        // A simple check to ensure the bundle for German is loaded.
        // This assumes 'gamemode.title' exists in languages_de.properties
        // and its value is different from the English one.
        // A more robust test would require knowing the actual German translation.
        assertDoesNotThrow(() -> appLocalizationService.get("gamemode.title"));
        // Example: if "Spielmodus wählen" is the German translation
        // assertEquals("Spielmodus wählen", appLocalizationService.get("gamemode.title"));
        // For now, just check it's not the English one if we are sure it's different
        // This part of the test is limited without actual content of languages_de.properties
    }

    @Test
    void testResourceBundleLoading_forFrench() {
        when(mockAppConfig.getConfiguredAppLanguage()).thenReturn("fr");
        appLocalizationService = new AppLocalizationService(mockAppConfig);
        assertDoesNotThrow(() -> appLocalizationService.get("gamemode.title"));
        // Example: if "Choisir le mode de jeu" is the French translation
        // assertEquals("Choisir le mode de jeu", appLocalizationService.get("gamemode.title"));
    }
}
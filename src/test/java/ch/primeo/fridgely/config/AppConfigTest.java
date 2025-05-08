package ch.primeo.fridgely.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppConfigTest {

    @Mock
    private Environment environment;

    @InjectMocks
    private AppConfig appConfig;

    @Test
    void getConfiguredAppLanguage_whenPropertyIsSetToEn_shouldReturnEn() {
        when(environment.getProperty("app.language")).thenReturn("en");
        String language = appConfig.getConfiguredAppLanguage();
        assertEquals("en", language);
        verify(environment).getProperty("app.language");
    }

    @Test
    void getConfiguredAppLanguage_whenPropertyIsSetToDe_shouldReturnDe() {
        when(environment.getProperty("app.language")).thenReturn("de");
        String language = appConfig.getConfiguredAppLanguage();
        assertEquals("de", language);
        verify(environment).getProperty("app.language");
    }

    @Test
    void getConfiguredAppLanguage_whenPropertyIsSetToFr_shouldReturnFr() {
        when(environment.getProperty("app.language")).thenReturn("fr");
        String language = appConfig.getConfiguredAppLanguage();
        assertEquals("fr", language);
        verify(environment).getProperty("app.language");
    }

    @Test
    void getConfiguredAppLanguage_whenPropertyIsNotSet_shouldReturnNull() {
        when(environment.getProperty("app.language")).thenReturn(null);
        String language = appConfig.getConfiguredAppLanguage();
        assertNull(language);
        verify(environment).getProperty("app.language");
    }

    @Test
    void getConfiguredAppLanguage_whenPropertyIsEmpty_shouldReturnEmpty() {
        when(environment.getProperty("app.language")).thenReturn("");
        String language = appConfig.getConfiguredAppLanguage();
        assertEquals("", language);
        verify(environment).getProperty("app.language");
    }
}
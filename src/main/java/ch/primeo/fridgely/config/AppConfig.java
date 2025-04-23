package ch.primeo.fridgely.config;

import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Utility class for application configuration. Loads properties from the app.properties file
 * and provides access to configuration values such as application language.
 */
@Component
@Scope("singleton")
public final class AppConfig {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private AppConfig(Environment env) {
        this.env = env;
    }

    private final Environment env;

    /**
     * Returns the configured application language.
     *
     * @return the application language
     */
    public String getConfiguredAppLanguage() {
        return env.getProperty("app.language");
    }
}

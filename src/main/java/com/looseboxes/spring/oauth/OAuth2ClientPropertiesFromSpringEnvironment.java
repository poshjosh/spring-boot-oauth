package com.looseboxes.spring.oauth;

import java.util.Objects;
import java.util.Properties;
import org.springframework.core.env.Environment;

/**
 * @author hp
 */
public class OAuth2ClientPropertiesFromSpringEnvironment implements OAuth2ClientProperties{
    
    private final Environment environment;

    public OAuth2ClientPropertiesFromSpringEnvironment(Environment environment) {
        this.environment = Objects.requireNonNull(environment);
    }

    @Override
    public Properties get(String clientName) {
        Properties properties = new Properties();
        final String prefix = "spring.security.oauth2.client.registration.";
        final String apiKey = environment.getProperty(prefix+clientName+".api-key");
        if(apiKey != null && !apiKey.isEmpty()) {
            properties.setProperty(API_KEY, apiKey);
        }
        properties.setProperty(CLIENT_ID, environment.getProperty(prefix+clientName+".client-id"));
        properties.setProperty(USER_INFO_URI, environment.getProperty(PROPERTY_PREFIX + clientName + '.' + USER_INFO_URI));
        return properties;
    }
}

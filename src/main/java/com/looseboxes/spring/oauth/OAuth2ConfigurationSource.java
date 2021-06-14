package com.looseboxes.spring.oauth;

import com.looseboxes.spring.oauth.profile.ProfileConverterFactory;
import com.looseboxes.spring.oauth.profile.ProfileConverterFactoryImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;

/**
 * @author hp
 */
public abstract class OAuth2ConfigurationSource {
    
    @Bean public OAuth2ClientPropertiesFromSpringEnvironment 
        oAuth2ClientPropertiesFromSpringEnvironment(Environment env) {
        return new OAuth2ClientPropertiesFromSpringEnvironment(env);
    }
    
    @Bean public OAuth2 oAuth2AuthenticationAccess(
            OAuth2AuthorizedClientService clientService,
            OAuth2CacheProvider cacheProvider) {
        return new OAuth2(clientService, cacheProvider);
    }
    
    @Bean public ProfileConverterFactory profileConverterFactory() {
        return new ProfileConverterFactoryImpl();
    }

    @Bean public abstract OAuth2CacheProvider oauth2CacheProvider(ApplicationContext context);
}

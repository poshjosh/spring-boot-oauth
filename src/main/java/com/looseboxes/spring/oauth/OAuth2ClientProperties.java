package com.looseboxes.spring.oauth;

import java.util.Properties;

/**
 * @author hp
 */
public interface OAuth2ClientProperties {
    
    String PROPERTY_PREFIX = "looseboxes.spring.oauth2.";
    
    String API_KEY = "api-key";
    
    String CLIENT_ID = "client-id";
    
    String USER_INFO_URI = "user-info-uri";
    
    public Properties get(String clientName);
}

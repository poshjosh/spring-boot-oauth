package com.looseboxes.spring.oauth;

import org.springframework.security.oauth2.core.OAuth2AccessToken;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashSet;

/**
 * @author hp
 */
public class OAuth2AccessTokenFromCallbackResponseObject extends OAuth2AccessToken{

    public OAuth2AccessTokenFromCallbackResponseObject(OAuth2LoginVM callbackResponse) {
        super(TokenType.BEARER, 
                callbackResponse.getAccess_token(), 
                Instant.now(), 
                Instant.now().plusSeconds(callbackResponse.getExpires_in()), 
                callbackResponse.getScopes() == null || callbackResponse.getScopes().isEmpty() ? Collections.EMPTY_SET : 
                        Collections.unmodifiableSet(new LinkedHashSet(callbackResponse.getScopes())));
    }
}

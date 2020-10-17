package com.looseboxes.spring.oauth;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

/**
 * @author hp
 */
public class OAuth2 {
    
    private final Logger log = LoggerFactory.getLogger(OAuth2.class);
    
    public static final String NAME_ATTRIBUTE = "name";
    public static final String EMAIL_ATTRIBUTE = "email";
    
    private final OAuth2AuthorizedClientService clientService;
    
    private final OAuth2CacheProvider cacheProvider;
    
    public OAuth2(OAuth2AuthorizedClientService clientService, OAuth2CacheProvider cacheProvider) {
        this.clientService = Objects.requireNonNull(clientService);
        this.cacheProvider = Objects.requireNonNull(cacheProvider);
    }
    
    public boolean isValidToken(OAuth2AccessToken token, String value) {
        return token.getTokenValue().equals(value);
    }
    
    public boolean isValidToken(String tokenValue) {
        return this.getAccessToken()
                .map((accessToken) -> isValidToken(accessToken, tokenValue))
                .orElse(false);
    }
    
    public Optional<OAuth2AccessToken> getAccessToken() {
        return this.getAccessToken((String)null);
    }
    
    public Optional<OAuth2AccessToken> getAccessToken(String clientId) {
    
        Authentication authentication = this.getAuthentication(clientId);
        
        return toOAuth2Token(authentication)
                .flatMap((oauthToken) -> getAccessToken(clientId, oauthToken));
    }
    
    public Optional<OAuth2AccessToken> getAccessToken(String clientId, OAuth2AuthenticationToken oauthToken) {
        return this.getAuthorizedClient(clientId, oauthToken)
                .map((client) -> client.getAccessToken());
    }

    public Optional<OAuth2AuthorizedClient> getAuthorizedClient(String clientId) {
        
        Authentication authentication = this.getAuthentication(clientId);

        return toOAuth2Token(authentication)
                .flatMap((oauthToken) -> getAuthorizedClient(clientId, oauthToken));
    }
    
    public Optional<OAuth2AuthorizedClient> getAuthorizedClient(String clientId, OAuth2AuthenticationToken oauthToken) {
    
        final OAuth2AuthorizedClient client;
        
        String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();

        if (clientId == null || clientRegistrationId.equals(clientId)) {

            client = clientService.loadAuthorizedClient(clientRegistrationId, oauthToken.getName());

        }else{

            client = null;
        }
        
        return Optional.ofNullable(client);
    }

    public boolean isAuthenticated() {
        return this.getAuthenticationToken().isPresent();
    }

    public Optional<String> getAuthenticatedClientId() {
        return getAuthenticationToken().map(t -> t.getAuthorizedClientRegistrationId());
    }

    public Optional<OAuth2AuthenticationToken> getAuthenticationToken() {
    
        return this.toOAuth2Token(this.getAuthentication());
    }

    private Optional<OAuth2AuthenticationToken> toOAuth2Token(Authentication authentication) {
        
        OAuth2AuthenticationToken result = null;
        
        if (authentication != null && 
                authentication.getClass().isAssignableFrom(OAuth2AuthenticationToken.class)) {
            
            result = (OAuth2AuthenticationToken) authentication;
        }
        
        return Optional.ofNullable(result);
    }

    public Authentication getAuthentication() {
        return this.getAuthentication(null);
    }
    
    public Authentication getAuthentication(String clientId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated() && this.isOAuth(authentication)) {
            this.saveToCache((OAuth2AuthenticationToken)authentication);
        }else{
            OAuth2AuthenticationToken loaded = this.loadFromCache().orElse(null);
            authentication = loaded != null ? loaded : authentication;
        }
        return authentication;
    }

    public boolean isOAuth(Authentication authentication) {
        return authentication != null && 
                authentication.getClass().isAssignableFrom(OAuth2AuthenticationToken.class);
    }   
    
    public void saveToCache(OAuth2AuthenticationToken authentication) {
        log.debug("Saving to cache: {}", toString(authentication));
        this.cacheProvider.getCache()
                .ifPresent((cache) -> cache.put(getCacheKey(), authentication));
    }

    public Optional<OAuth2AuthenticationToken> loadFromCache() {
        Optional<OAuth2AuthenticationToken> result = this.cacheProvider.getCache()
                .map((cache) -> (OAuth2AuthenticationToken)cache.get(getCacheKey()));
        log.debug("Loaded from cache: {}", toString(result.orElse(null)));
        return result;
    }
    
    private String toString(OAuth2AuthenticationToken auth) {
        return auth == null ? "null" : 
                "OAuth2AuthenticationToken{client_id=" + auth.getAuthorizedClientRegistrationId() + ", user=" + auth.getName() + '}';
    }
    
    public void removeFromCache() {
        log.debug("Removing authentication to cache"); 
        this.cacheProvider.getCache()
                .ifPresent((cache) -> cache.remove(getCacheKey()));
    }

    private String getCacheKey() {
        return "oauth2-authentication";
    }

    public OAuth2AuthenticationToken createAuthentication(
            String clientId, String name, String email, String authority) {
        
        Objects.requireNonNull(name);
        
        final Map<String, Object> userDetails;
        if(email == null || email.isEmpty()) {
            userDetails = Collections.singletonMap(NAME_ATTRIBUTE, name);
        }else{
            userDetails = new HashMap<>(4, 0.75f);
            userDetails.put(NAME_ATTRIBUTE, name);
            userDetails.put(EMAIL_ATTRIBUTE, email);
        }
        
        GrantedAuthority grantedAuthority = new OAuth2UserAuthority(authority, userDetails);
        Collection<GrantedAuthority> authorities = Collections.singletonList(grantedAuthority);
        OAuth2User oauth2User = new DefaultOAuth2User(authorities, userDetails, NAME_ATTRIBUTE);        
        
        return new OAuth2AuthenticationToken(oauth2User, authorities, clientId);        
    }   
}

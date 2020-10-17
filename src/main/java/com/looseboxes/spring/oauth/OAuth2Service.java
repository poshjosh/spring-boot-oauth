package com.looseboxes.spring.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.looseboxes.spring.oauth.profile.OAuth2Profile;
import com.looseboxes.spring.oauth.facebook.Facebook;
import com.looseboxes.spring.oauth.google.Google;
import com.looseboxes.spring.oauth.profile.ProfileConverterFactory;
import com.looseboxes.spring.oauth.profile.ProfileConverterFactoryImpl;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Objects;
import java.util.Optional;
import javax.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.client.RestTemplate;

/**
 * @author hp
 */
public class OAuth2Service {
    
    private final Logger log = LoggerFactory.getLogger(OAuth2Service.class);
    
    public static final String GOOGLE = Google.CLIENT_ID;
    public static final String FACEBOOK = Facebook.CLIENT_ID;
    
    private final OAuth2 OAuth;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final OAuth2CacheProvider cacheProvider;
    private final ProfileConverterFactory converterFactory;
    private final OAuth2ClientProperties oauthProperties;

    public OAuth2Service(
            OAuth2AuthorizedClientService clientService, 
            OAuth2CacheProvider cacheProvider,
            Environment environment) { 
        this(new OAuth2(clientService, cacheProvider),
                new RestTemplate(), new ObjectMapper(),
                cacheProvider, new ProfileConverterFactoryImpl(),
                new OAuth2ClientPropertiesFromSpringEnvironment(environment));
    }
    
    public OAuth2Service(
            OAuth2 oauth2, RestTemplate restTemplate,
            ObjectMapper objectMapper, OAuth2CacheProvider cacheProvider, 
            ProfileConverterFactory converterFactory,
            OAuth2ClientProperties oauthProperties) { 
        this.OAuth = Objects.requireNonNull(oauth2);
        this.restTemplate = Objects.requireNonNull(restTemplate);
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.cacheProvider = Objects.requireNonNull(cacheProvider);
        this.converterFactory = Objects.requireNonNull(converterFactory);
        this.oauthProperties = Objects.requireNonNull(oauthProperties);
    }
    
    public boolean isValidToken(String accessToken) {
        return this.OAuth.isValidToken(accessToken);
    }

    public Optional<OAuth2Profile> getAuthenticatedUserProfile() {
        return OAuth.getAuthenticationToken()
                .flatMap((oauthToken) -> this.getUserProfile(oauthToken));
    }
    
    public OAuth2AccessToken createAccessToken(OAuth2LoginVM loginVM) {
        return new OAuth2AccessTokenFromCallbackResponseObject(loginVM);
    }

    public Optional<OAuth2Profile> getUserProfile(OAuth2AuthenticationToken oauthToken) {
        final String clientId = oauthToken.getAuthorizedClientRegistrationId();
        return OAuth.getAccessToken(clientId, oauthToken)
                .map((accessToken) -> getUserProfile(clientId, accessToken, oauthToken.getPrincipal()));
    }
    
    public OAuth2Profile getUserProfile(String clientId, OAuth2AccessToken authToken, OAuth2User user) {
        
        final String cacheKey = "profile_" + clientId + '_' + user.getName();
        
        Cache cache = cacheProvider.getCache().orElse(null);
        
        OAuth2Profile userProfile = null;
        
        if(cache != null) {
            userProfile = (OAuth2Profile)cache.get(cacheKey);
            log.debug("Profile from cache: {}", userProfile);
        }
        
        if(userProfile == null) {
            userProfile = fetchUserProfile(clientId, authToken, user).orElse(null);
            log.debug("Profile from OAuthClient: {}", userProfile);
            if(cache != null && userProfile != null) {
                cache.put(cacheKey, userProfile);
            }
        }
        
        return userProfile;
    }

    public Optional<OAuth2Profile> fetchUserProfile(
            String clientId, OAuth2AccessToken authToken, OAuth2User user) {
        OAuth2Profile result;
        try{
            result = this.getApiBinding(clientId, authToken).fetchOAuthProfile(user);
        }catch(Exception e) {
            log.warn("Failed to fetch OAuth2 " + clientId + "profile for: " + user, e);
            return null;
        }
        return Optional.ofNullable(result);
    }
    
    public OAuth2Profile fetchUserProfileOrException(
            String clientId, OAuth2AccessToken authToken, OAuth2User user) 
            throws JsonProcessingException{
    
        return this.getApiBinding(clientId, authToken).fetchOAuthProfile(user);
    }
    
    public OAuth2ApiBinding getApiBinding(String clientId, OAuth2AccessToken authToken) {
        
        final String userInfoUri = oauthProperties.get(clientId).getProperty(OAuth2ClientProperties.USER_INFO_URI);

// Spring boot's internal user info uri does not match this api UserProfile construct
// and will lead to errors
//        OAuth2AuthorizedClient client = this.OAuth.getAuthorizedClient(clientId)
//                .orElseThrow(() -> new RuntimeException());
//        final String userInfoUri = client.getClientRegistration()
//                .getProviderDetails().getUserInfoEndpoint().getUri();
        Objects.requireNonNull(userInfoUri, "OAuth2AuthorizedClient.clientRegistration.providerDetails.userInfoEndpoint.uri cannot be null");
        
        OAuth2ApiBinding result;
        switch(clientId) {
            case GOOGLE: result = new Google(
                    authToken.getTokenValue(), userInfoUri, restTemplate, 
                    objectMapper, this.converterFactory);
                break;
            case FACEBOOK: result = new Facebook(
                    authToken.getTokenValue(), userInfoUri, restTemplate, 
                    objectMapper, this.converterFactory);
                break;
            default: 
                throw new UnsupportedOperationException("Unsupported OAuth2 client: " + clientId);
        }
        return result;
    }
    
    public OAuth2 getOAuth() {
        return OAuth;
    }
}
/**
 * 
    
    // @see https://stackoverflow.com/questions/56420718/spring-boot-oauth2-facebook-login-json-parse-error-cannot-deserialize-instanc
    // @see https://github.com/spring-projects/spring-security/issues/6463
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> createAccessTokenResposeClient() {
        DefaultAuthorizationCodeTokenResponseClient client = new DefaultAuthorizationCodeTokenResponseClient();
//        RestTemplate restTemplate = new RestTemplate(Arrays.asList(
//                new FormHttpMessageConverter(), new OAuth2AccessTokenResponseHttpMessageConverter()));
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        restTemplate.getMessageConverters().add(new OAuth2AccessTokenResponseHttpMessageConverter());
        restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());
//        HttpClient requestFactory = HttpClientBuilder.create().build();
//        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(requestFactory));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        client.setRestOperations(restTemplate);
        return client;
    }    
 * 
 */
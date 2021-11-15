package com.looseboxes.spring.oauth;

/**
 * Intercept all rest calls to add the authorization (via the provide access token)
 * @author hp
 * @see https://spring.io/blog/2018/03/06/using-spring-security-5-to-integrate-with-oauth-2-secured-services-such-as-facebook-and-github
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.looseboxes.spring.oauth.profile.OAuth2Profile;
import com.looseboxes.spring.oauth.profile.OAuth2ProfileImpl;
import com.looseboxes.spring.oauth.profile.ProfileConverterFactory;
import com.looseboxes.spring.oauth.profile.UserProfile;
import java.io.IOException;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.client.RestTemplate;

public class ApiBinding<P> implements OAuth2ApiBinding {

    private static final Logger LOG = LoggerFactory.getLogger(ApiBinding.class);

    private final String clientId; 
    private final String userInfoUri; 
    private final Class<P> type;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper; 
    private final ProfileConverterFactory converterFactory;
    
    public ApiBinding(String clientId, String accessToken,
            String userInfoUri, Class<P> type,
            RestTemplate restTemplate, ObjectMapper objectMapper, 
            ProfileConverterFactory converterFactory) {
        this.clientId = Objects.requireNonNull(clientId);
        this.userInfoUri = Objects.requireNonNull(userInfoUri);
        this.type = Objects.requireNonNull(type);
        this.restTemplate = Objects.requireNonNull(restTemplate);
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.converterFactory = Objects.requireNonNull(converterFactory);

        this.restTemplate.getInterceptors().add(getBearerTokenInterceptor(accessToken));
    }
    
    @Override
    public OAuth2Profile<P> fetchOAuthProfile(OAuth2User user) throws JsonProcessingException{
    
// This failed with message:
// org.springframework.web.client.RestClientException: Error while extracting response for type [class java.lang.String] and content type [application/json]; nested exception is org.springframework.http.converter.HttpMessageNotReadableException: Invalid JSON input: Cannot deserialize instance of `java.lang.String` out of START_OBJECT token; nested exception is com.fasterxml.jackson.databind.exc.MismatchedInputException: Cannot deserialize instance of `java.lang.String` out of START_OBJECT token
//        restTemplate.getForObject(userInfoUri, String.class);

        String json = null;
        P profileFromOAuth2Service = null;
        try{
            profileFromOAuth2Service = restTemplate.getForObject(userInfoUri, type);
        }catch(Exception e) {
            try{
                json = restTemplate.getForObject(userInfoUri, String.class);
                LOG.error("{}", json);
            }catch(Exception inner) {
                e.addSuppressed(inner);
                throw new RuntimeException(e);
            }
        }
        
        if(profileFromOAuth2Service == null && json != null) {
            profileFromOAuth2Service = objectMapper.readValue(json, type);
        }else if(profileFromOAuth2Service != null && json == null) {
            try{
                json = objectMapper.writeValueAsString(profileFromOAuth2Service);
                LOG.debug("{}", json);
            }catch(JsonProcessingException e) {
                LOG.warn("Failed to write profile as json text", e);
                json = "{}";
            }
        }
        
        
        if(converterFactory.isSupported(type)) {
            
            return this.convertToOAuth2Profile(user, profileFromOAuth2Service, json);

        }else{
            
            throw new UnsupportedOperationException("Conversion from " + 
                    type + " to UserProfile not supported");
        }
    }

    protected OAuth2Profile<P> convertToOAuth2Profile(OAuth2User user, P profileFromOAuthService, String json) {

        UserProfile<P> userProfile = convertToUserProfile(user, profileFromOAuthService);

        return new OAuth2ProfileImpl(clientId, userInfoUri, user, userProfile, json);
    }

    protected UserProfile<P> convertToUserProfile(OAuth2User user, P profileFromOAuthService) {

        Converter<P, UserProfile> converter = this.converterFactory.getConverter(type);

        return converter.convert(profileFromOAuthService);
    }

    private ClientHttpRequestInterceptor getBearerTokenInterceptor(String accessToken) {
        Objects.requireNonNull(accessToken);
        return new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] bytes, ClientHttpRequestExecution execution) throws IOException {
                request.getHeaders().add("Authorization", "Bearer " + accessToken);
                return execution.execute(request, bytes);
            }
        };
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    public String getUserInfoUri() {
        return userInfoUri;
    }

    public Class<P> getType() {
        return type;
    }

    @Override
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public ProfileConverterFactory getConverterFactory() {
        return converterFactory;
    }
}
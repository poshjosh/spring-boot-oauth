# spring-boot-oauth

### OAuth helper library for spring boot based projects

__Greatly simplifies the use of spring boot oauth__

Defines models for profile data e.g `GoogleProfilePeopleApi`, which may be
used as follows:

```java
public class FetchGoogleProfile {

    private final String baseUrl = "https://people.googleapis.com/v1/people/me?personFields=emailAddresses,genders,metadata,names,phoneNumbers,photos,urls";
    
    public void test(String accessToken, String apiKey) {
        
        RestTemplate restTemplate = new RestTemplate();
        
        restTemplate.getInterceptors().add((request, bytes, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + accessToken);
            return execution.execute(request, bytes);
        });
        
        System.out.println("fetching google profile");
        
        GoogleProfilePeopleApi profile = restTemplate.getForObject(
                baseUrl + "&key=" + apiKey, GoogleProfilePeopleApi.class);
        
        System.out.println(profile);
    }
}
```

### Configure your project

__Add application properties__

Add the following application properties

```yml
spring:
  security: 
    # @see https://docs.spring.io/spring-security/site/docs/current/reference/html5/  
    oauth2:
      client:
        registration:
          google:
            api-key: 
            client-id: 
            client-secret: 
            scope: 
              - https://www.googleapis.com/auth/userinfo.email
              - https://www.googleapis.com/auth/userinfo.profile
          facebook:
            client-id: 
            client-secret: 
            scope: 
              - email
              - public_profile
looseboxes:
  spring:
    oauth2:
      google:
        # This property is dependent on property: spring.security.oauth2.client.registration.google.api-key
        user-info-uri: https://people.googleapis.com/v1/people/me?personFields=emailAddresses,genders,metadata,names,phoneNumbers,photos,urls&key=${spring.security.oauth2.client.registration.google.api-key}   
      facebook:
        user-info-uri: https://graph.facebook.com/v8.0/me?fields=id,first_name,middle_name,last_name,name,email,verified,is_verified,picture.width(250).height(250),gender,birthday
```

__Add jackson deserializer for `OAuth2LoginVM`__

Use the following helper method to configure your `ObjectMapper`

```java
import com.looseboxes.spring.oauth.OAuth2LoginVM;

public class ConfigurationHelper{

    public static ObjectMapper addOAuth2ResponseDeserializer(ObjectMapper objectMapper) {
	SimpleModule module = new SimpleModule();
	module.addDeserializer(OAuth2LoginVM.class, new OAuthResponseDeserializer(jsonNodeToOAuth2LoginVMConverter()));
	objectMapper.registerModule(module);        
        return objectMapper;
    }
}
```

__Add Configuration__

```java
import com.looseboxes.spring.oauth.OAuth2CacheProvider;
import com.looseboxes.spring.oauth.OAuth2ConfigurationSource;
import com.looseboxes.spring.oauth.profile.OAuth2Profile;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuth2Configuration extends OAuth2ConfigurationSource{
    
    @Override
    @Bean public OAuth2CacheProvider oauth2CacheProvider(ApplicationContext context) {
        //@TODO Return implementation of OAuth2CacheProvider
        // The interface just returns an instance of javax.cache.Cache
    }
}
```

__Add `OAuth2Service`__

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import com.looseboxes.spring.oauth.OAuth2;
import com.looseboxes.spring.oauth.OAuth2CacheProvider;
import com.looseboxes.spring.oauth.OAuth2ClientProperties;
import org.springframework.stereotype.Service;
import java.util.Collections;
import com.looseboxes.spring.oauth.profile.OAuth2Profile;
import com.looseboxes.spring.oauth.profile.ProfileConverterFactory;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.client.RestTemplate;

@Component
public class OAuth2Service extends com.looseboxes.spring.oauth.OAuth2Service {

    private final Logger log = LoggerFactory.getLogger(OAuth2Service.class);
    
    public OAuth2Service(
            OAuth2 oauth2,    
            RestTemplate restTemplate, 
            ObjectMapper objectMapper,
            OAuth2CacheProvider cacheProvider, 
            ProfileConverterFactory converterFactory,
            OAuthProfileToUserConverter<OAuth2Profile> userConverter,
            OAuth2ClientProperties oauth2ClientProperties) {
        
        super(oauth2, restTemplate, objectMapper, cacheProvider, converterFactory, oauth2ClientProperties);
    }
}
```

__Use the `OAuth2Service`__

```java
import com.myproject.security.oauth.OAuth2Service;
import com.looseboxes.spring.oauth.OAuth2;
import com.looseboxes.spring.oauth.OAuth2LoginVM;
import com.looseboxes.spring.oauth.profile.OAuth2Profile;
import java.util.Collections;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Service
public abstract class AuthenticationService {
    
    private final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    
    public static class AuthenticationException extends RuntimeException{
        public AuthenticationException() { }
        public AuthenticationException(String string) {
            super(string);
        }
        public AuthenticationException(String string, Throwable thrwbl) {
            super(string, thrwbl);
        }
    }
    
    @Autowired private OAuth2Service oauth2Service;
    
    protected abstract String createToken(Authentication authentication, boolean rememberMe);
    
    protected abstract Optional<Object> findUserByEmail(String email);
    
    protected abstract Object registerAccountAndActivate(OAuth2Profile userProfile);
    
    protected abstract Object save(OAuth2Profile<?> userProfile, Object user);
    
    public String authorizeAndRegisterNewAccountIfNeed(OAuth2LoginVM loginVM) throws AuthenticationException{
        
        log.debug("REST request to authenticate user by oauth: {}", loginVM);
        
        OAuth2 auth = oauth2Service.getOAuth();
        
        OAuth2AuthenticationToken authenticationToken = auth.getAuthenticationToken()
                .orElseThrow(() -> new AuthenticationException());
        
        if( ! authenticationToken.isAuthenticated()) {
            
            throw new AuthenticationException();
            
        }else{

            this.fetchProfileFromOAuthProviderAndRegisterUserIfNeed(loginVM);
            
            boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
            
            final String token = createToken(authenticationToken, rememberMe);
            
            log.debug("Authorization successful: {}", token != null);
            
            return token;
        }
    }

    private void fetchProfileFromOAuthProviderAndRegisterUserIfNeed(OAuth2LoginVM loginVM) throws AuthenticationException{
        try{
            oauth2Service.getAuthenticatedUserProfile()
                    .map((oauthProfile) -> getOrCreateAndActivateNew(oauthProfile))
                    .orElseThrow(() -> new AuthenticationException());
            
        }catch(Exception shouldNotAffectAuthentication_onlyWeWontHaveTheUsersInfoOnRecord) {
            log.warn("Failed to get or create: " + loginVM, shouldNotAffectAuthentication_onlyWeWontHaveTheUsersInfoOnRecord);
        }
    }
    
    private Object getOrCreateAndActivateNew(OAuth2Profile<?> userProfile) throws AuthenticationException{
        final String email = userProfile.getEmail().orElseThrow(() -> new AuthenticationException());
        return findUserByEmail(email)
                .orElseGet(() -> {
                    
                    log.debug("Registering oauth user: {}", userProfile);
                    
                    return getOrCreateUser(userProfile)
                            .map((user) -> save(userProfile, user)).orElse(null);
                });
    }
    
    public Optional<Object> getOrCreateUser(OAuth2Profile<?> userProfile) {
        
        Object user = null;
        
        // Check if a user exists
        //
        final String email = userProfile.getEmail().orElse(null);

        if(email != null) {

            user = findUserByEmail(email).orElse(null);
        }

        if(user == null) {

            // The user does not exist register and activate
            // No need to send an activation email if a user joins via oauth
            //
            user = registerAccountAndActivate(userProfile);
            
            // Ensure the newly registered user exists
            //
            user = findUserByEmail(newUser.getEmail()).orElse(null);
        }    
    
        return Optional.ofNullable(user);
    }
    
    private ResponseEntity<Object> respond(String token) {
        
        Object responseBody = Collections.singletonMap("id_token", token);
        
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + token);
        
        return new ResponseEntity<>(responseBody, httpHeaders, HttpStatus.OK);
    }
}
```


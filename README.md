# spring-boot-oauth

### OAuth helper library for spring boot based projects

__Greatly simplifies the use of spring booth oauth__

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

__Define model__

```java
import javax.validation.constraints.Size;

public class NewUserVM{

    public static final int PASSWORD_MIN_LENGTH = 6;

    public static final int PASSWORD_MAX_LENGTH = 100;

    private Long id;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Size(max = 256)
    private String imageUrl;

    private boolean activated = false;

    @Size(min = 2, max = 10)
    private String langKey;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Set<String> authorities;

    public NewUserVM() {
        // Empty constructor needed for Jackson.
    }

    // Add getters and setters
}
```

__Add jackson deserializer for `OAuth2LoginVM`__

```java
import com.looseboxes.spring.oauth.OAuth2LoginVM;

public class ConfigurationHelper{

    public ObjectMapper addOAuth2ResponseDeserializer(ObjectMapper objectMapper) {
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
        // Return implementation of OAuth2CacheProvider
        // The interface just returns an instance of javax.cache.Cache
    }
}
```

__Add Service__

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import com.looseboxes.spring.oauth.OAuth2;
import com.looseboxes.spring.oauth.OAuth2CacheProvider;
import com.looseboxes.spring.oauth.OAuth2ClientProperties;
import com.myproject.web.rest.vm.NewUserVM;
import org.springframework.stereotype.Service;
import java.util.Collections;
import com.looseboxes.spring.oauth.profile.OAuth2Profile;
import com.looseboxes.spring.oauth.profile.ProfileConverterFactory;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.client.RestTemplate;

@Service
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

    @Override
    public Optional<OAuth2Profile> getUserProfile(OAuth2AuthenticationToken oauthToken) {
        try{
            return super.getUserProfile(oauthToken);
        }catch(Exception e) {
            log.warn("Failed to get oauth2 user profile for: " + oauthToken.getPrincipal().getName(), e);
            return Optional.empty();
        }
    }
    
    public Object getConfig(String clientId) {
        return Collections.EMPTY_MAP;
    }
    
    public NewUserVM createModelForRegisteringNewUser(
            String clientId, OAuth2Profile userProfile) {

        NewUserVM model = new NewUserVM(;

        // Use the info in the oauth profile to update the model

        return model;
    }
}
```

__Use the service__

```java
import com.myproject.security.oauth.OAuth2Service;
import com.myproject.web.rest.vm.NewUserVM;
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

@RestController
public abstract class AuthenticationResourceExample {
    
    private final Logger log = LoggerFactory.getLogger(AuthenticationResourceExample.class);
    
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
    
    protected abstract Object registerAccountAndActivate(NewUserVM viewModel);
    
    protected abstract Object save(OAuth2Profile<?> userProfile, Object user);
    
    @PostMapping("/authenticate/oauth2")
    public ResponseEntity<Object> authorizeAndRegisterNewAccountIfNeed(
            @Valid @RequestBody OAuth2LoginVM loginVM) throws AuthenticationException{
        
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
            
            return this.respond(token);
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
        
        final String email = userProfile.getEmail().orElse(null);

        if(email != null) {

            user = findUserByEmail(email).orElse(null);
        }

        if(user == null) {

            NewUserVM newUser = this.oauth2Service
                    .createModelForRegisteringNewUser(userProfile.getClientId(), userProfile);

            user = registerAccountAndActivate(newUser);
            
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




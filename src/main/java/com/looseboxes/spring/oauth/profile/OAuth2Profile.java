package com.looseboxes.spring.oauth.profile;

import java.util.Optional;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * @author hp
 */
public interface OAuth2Profile<P> extends HasUserProfile<P>{
    String getClientId();
    String getUrl();
    String getUserId();
    
    /**
     * Return the OAuth2User's username.
     * The returned username is the same as the user name in the Spring SecurityContext
     * 
     * @return The OAuth2User's username.
     */
    default String getUsername() {
// When we used this, there was conflict between the generated value and
// the value held in SecurityContextHolder.getContext().getPricipal
// The later is used through out this application to reference the current
// logged in user. 
//        return "user_" + Long.toHexString(System.currentTimeMillis()) + SecurityUtils.generateCode(3);
        return getUser().getName();
    }
    
    Optional<String> getEmail();
    
    OAuth2User getUser();
    
    @Override
    UserProfile<P> getUserProfile();
    
    String getUserProfileJson();
}

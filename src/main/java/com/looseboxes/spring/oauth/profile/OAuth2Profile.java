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
    String getUsername();
    Optional<String> getEmail();
    OAuth2User getUser();
    @Override
    UserProfile<P> getUserProfile();
    String getUserProfileJson();
}

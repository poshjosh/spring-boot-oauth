package com.looseboxes.spring.oauth.profile;

//import com.looseboxes.spring.oauth.google.*;
import java.util.Objects;
import java.util.Optional;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * @author hp
 */
public final class OAuth2ProfileImpl implements OAuth2Profile{
    
    private final String clientId;
    
    private final String url;
    
    private final OAuth2User user;
    
    private final UserProfile userProfile;
    
    private final String userProfileJson;

    public OAuth2ProfileImpl(String clientId, String url, OAuth2User user, UserProfile profile, String userProfileJson) {
        this.clientId = Objects.requireNonNull(clientId);
        this.url = Objects.requireNonNull(url);
        this.user = Objects.requireNonNull(user);
        this.userProfile = Objects.requireNonNull(profile);
        this.userProfileJson = Objects.requireNonNull(userProfileJson);
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String getUserId() {
        return userProfile.getId();
    }

    /**
     * Return the OAuth2User's username.
     * The returned username is the same as the user name in the Spring SecurityContext
     * 
     * @return The OAuth2User's username.
     */
    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public Optional<String> getEmail() {
        return userProfile.getEmailAddress();
    }

    @Override
    public OAuth2User getUser() {
        return user;
    }

    @Override
    public UserProfile getUserProfile() {
        return userProfile;
    }

    @Override
    public String getUserProfileJson() {
        return userProfileJson;
    }

    @Override
    public String toString() {
        return "OAuth2ProfileImpl{" + "url=" + url + ", user=" + user.getName() + '}';
    }
}

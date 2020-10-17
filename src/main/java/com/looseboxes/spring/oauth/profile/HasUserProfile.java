package com.looseboxes.spring.oauth.profile;

/**
 * @author hp
 */
public interface HasUserProfile<P> {
    UserProfile<P> getUserProfile();
}
